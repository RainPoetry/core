package rainpoetry.livy.repl.session


import java.util.Map.Entry
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.util.{LinkedHashMap => JLinkedHashMap}

import org.apache.spark.sql.SparkSession
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods.{compact, render}
import rainpoetry.livy.repl.Interpreter.{ExecuteAborted, ExecuteError, ExecuteIncomplete, ExecuteSuccess}
import rainpoetry.livy.repl.bean.{Statement, StatementState}
import rainpoetry.livy.repl.config._
import rainpoetry.livy.repl.spark.SparkInterpreter
import rainpoetry.livy.repl.sql.SQLInterpreter
import rainpoetry.livy.repl.{Interpreter, Kind, SQL, Spark}
import rainpoetry.livy.utils.Logging

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

class ReplSession(var spark: SparkSession, replConf: ReplConf,
                  sessionId: String,
                  stateChangedCallback: SessionState => Unit = { _ => }) extends Logging {

  import ReplSession._

  // 定义线程池
  private val interpreterExecutor = ExecutionContext.fromExecutorService(
    Executors.newSingleThreadExecutor())

  private val cancelExecutor = ExecutionContext.fromExecutorService(
    Executors.newSingleThreadExecutor())

  private var _state: SessionState = SessionState.NotStarted

  private implicit val formats = DefaultFormats

  private val numRetainedStatements = replConf.get(RETAINED_STATEMENTS)

  private val _statements = new JLinkedHashMap[Int, Statement] {
    protected override def removeEldestEntry(eldest: Entry[Int, Statement]): Boolean = {
      size() > numRetainedStatements
    }
  }.asScala

  private val newStatementId = new AtomicInteger(0)

  private val interpGroup = new mutable.HashMap[Kind, Interpreter]()

  def state: SessionState = _state

  def statements: collection.Map[Int, Statement] = _statements.synchronized {
    _statements.toMap
  }

  var session: SparkSession = _

  def start(): Future[Unit] = {
    val future = Future {
      changeState(SessionState.Starting)
      // Always start SparkInterpreter after beginning, because we rely on SparkInterpreter to
      // initialize SparkContext and create SparkEntries.
      val sparkInterp = new SparkInterpreter(spark)
      sparkInterp.start()
      spark = sparkInterp.getSpark()
      interpGroup.synchronized {
        interpGroup.put(Spark, sparkInterp)
      }
      changeState(SessionState.Idle)
    }(interpreterExecutor)
    future.onFailure { case _ => changeState(SessionState.Error()) }(interpreterExecutor)
    future
  }

  // 同步执行任务
  def executeSync(code: String, codeType: String): Int = {
    commonExecute(code, codeType) {
      (statementId, statement, tpe, code) =>
        callInterperter(statementId, statement, tpe, code)
    }
  }

  // 异步执行任务
  def execute(code: String, codeType: String): Int = {
    commonExecute(code, codeType) {
      (statementId, statement, tpe, code) =>
        Future {
          callInterperter(statementId, statement, tpe, code)
        }(interpreterExecutor)
    }
  }

  def callInterperter(statementId: Int, statement: Statement, tpe: Kind, code: String): Unit = {
    setJobGroup(tpe, statementId)
    statement.compareAndTransit(StatementState.Waiting, StatementState.Running)

    if (statement._state.get() == StatementState.Running) {
      statement.output = executeCode(interpreter(tpe), statementId, code)
    }

    statement.compareAndTransit(StatementState.Running, StatementState.Available)
    statement.compareAndTransit(StatementState.Cancelling, StatementState.Cancelled)
    statement.updateProgress(1.0)
  }

  def commonExecute(code: String, codeType: String)
                   (f: (Int, Statement, Kind, String) => Unit): Int = {
    val tpe = if (codeType != null) {
      Kind(codeType)
    } else {
      throw new IllegalArgumentException(s"Code type should be specified if session kind is shared")
    }

    val statementId = newStatementId.getAndIncrement()
    val statement = new Statement(statementId, code, StatementState.Waiting, null)
    _statements.synchronized {
      _statements(statementId) = statement
    }
    f(statementId, statement, tpe, code)
    statementId
  }



  def cancel(statementId: Int): Unit = {

    val statementOpt = _statements.synchronized {
      _statements.get(statementId)
    }
    if (statementOpt.isEmpty) {
      return
    }

    val statement = statementOpt.get
    if (statement._state.get().isOneOf(
      StatementState.Available, StatementState.Cancelled, StatementState.Cancelling)) {
      return
    } else {
      // statement 1 is running and statement 2 is waiting. User cancels
      // statement 2 then cancels statement 1. The 2nd cancel call will loop and block the 1st
      // cancel call since cancelExecutor is single threaded. To avoid this, set the statement
      // state to cancelled when cancelling a waiting statement.
      statement.compareAndTransit(StatementState.Waiting, StatementState.Cancelled)
      statement.compareAndTransit(StatementState.Running, StatementState.Cancelling)
    }

    info(s"Cancelling statement $statementId...")

    Future {
      val deadline = replConf.getTimeAsMS(JOB_CANCEL_TIMEOUT).millis.fromNow

      while (statement._state.get() == StatementState.Cancelling) {
        if (deadline.isOverdue()) {
          info(s"Failed to cancel statement $statementId.")
          statement.compareAndTransit(StatementState.Cancelling, StatementState.Cancelled)
        } else {
          spark.sparkContext.cancelJobGroup(statementIdToJobGroup(statementId))
          if (statement._state.get() == StatementState.Cancelling) {
            Thread.sleep(replConf.getTimeAsMS(JOB_CANCEL_TRIGGER_INTERVAL))
          }
        }
      }

      if (statement._state.get() == StatementState.Cancelled) {
        info(s"Statement $statementId cancelled.")
      }
    }(cancelExecutor)
  }

  private def executeCode(interp: Option[Interpreter],
                          statementId: Int,
                          code: String): String = {
    def transitToIdle() = {
      val executingLastStatement = statementId == newStatementId.intValue() - 1
      if (_statements.isEmpty || executingLastStatement) {
        changeState(SessionState.Idle)
      }
    }

    val resultInJson = interp.map { i =>
      try {
        i.execute(code) match {
          case ExecuteSuccess(data) =>
            transitToIdle()
            (STATUS -> OK) ~
              (STATEMENTID -> statementIdToJobGroup(statementId)) ~
              (DATA -> data)

          case ExecuteIncomplete() =>
            transitToIdle()

            (STATUS -> ERROR) ~
              (STATEMENTID -> statementIdToJobGroup(statementId)) ~
              (ENAME -> "Error") ~
              (EVALUE -> "incomplete statement") ~
              (TRACEBACK -> Seq.empty[String])

          case ExecuteError(ename, evalue, traceback) =>
            transitToIdle()

            (STATUS -> ERROR) ~
              (STATEMENTID -> statementIdToJobGroup(statementId)) ~
              (ENAME -> ename) ~
              (EVALUE -> evalue) ~
              (TRACEBACK -> traceback)

          case ExecuteAborted(message) =>
            changeState(SessionState.Error())

            (STATUS -> ERROR) ~
              (STATEMENTID -> statementIdToJobGroup(statementId)) ~
              (ENAME -> "Error") ~
              (EVALUE -> f"Interpreter died:\n$message") ~
              (TRACEBACK -> Seq.empty[String])

        }
      } catch {
        case e: Throwable =>
          error("Exception when executing code", e)

          transitToIdle()

          (STATUS -> ERROR) ~
            (STATEMENTID -> statementIdToJobGroup(statementId)) ~
            (ENAME -> f"Internal Error: ${e.getClass.getName}") ~
            (EVALUE -> e.getMessage) ~
            (TRACEBACK -> Seq.empty[String])
      }
    }.getOrElse {
      transitToIdle()
      (STATUS -> ERROR) ~
        (STATEMENTID -> statementId) ~
        (ENAME -> "InterpreterError") ~
        (EVALUE -> "Fail to start interpreter") ~
        (TRACEBACK -> Seq.empty[String])
    }
    compact(render(resultInJson))
  }

  private def interpreter(kind: Kind): Option[Interpreter] = interpGroup.synchronized {
    if (interpGroup.contains(kind)) {
      Some(interpGroup(kind))
    } else {
      try {
        val interp = kind match {
          case Spark =>
            throw new IllegalStateException("SparkInterpreter should not be lazily created.")
          case SQL => new SQLInterpreter(spark)
        }
        interp.start()
        interpGroup(kind) = interp
        Some(interp)
      } catch {
        case NonFatal(e) =>
          warn(s"Fail to start interpreter $kind", e)
          None
      }
    }
  }

  private def setJobGroup(codeType: Kind, statementId: Int): Unit = {
    val jobGroupId = statementIdToJobGroup(statementId)
    codeType match {
      case Spark | SQL =>
        spark.sparkContext.setJobGroup(jobGroupId,
          s"Job Group for session ${sessionId}")
    }
  }

  private def changeState(newState: SessionState): Unit = {
    synchronized {
      _state = newState
    }
    stateChangedCallback(newState)
  }

  private def statementIdToJobGroup(statementId: Int): String = {
    sessionId + "_" + statementId.toString
  }

  def close(): Unit = {
    interpreterExecutor.shutdown()
    cancelExecutor.shutdown()
    interpGroup.values.foreach(_.close())
  }

  def progressOfStatement(stmtId: Int): Double = {
    val jobGroup = statementIdToJobGroup(stmtId)
    val sc = spark.sparkContext
    val jobIds = sc.statusTracker.getJobIdsForGroup(jobGroup)
    val jobs = jobIds.flatMap { id => sc.statusTracker.getJobInfo(id) }
    val stages = jobs.flatMap { job =>
      job.stageIds().flatMap(sc.statusTracker.getStageInfo)
    }

    val taskCount = stages.map(_.numTasks).sum
    val completedTaskCount = stages.map(_.numCompletedTasks).sum
    if (taskCount == 0) {
      0.0
    } else {
      completedTaskCount.toDouble / taskCount
    }
  }

}

object ReplSession {
  val STATUS = "status"
  val OK = "ok"
  val ERROR = "error"
  val STATEMENTID = "statementId"
  val DATA = "data"
  val ENAME = "ename"
  val EVALUE = "evalue"
  val TRACEBACK = "traceback"

  def main(args: Array[String]): Unit = {

    val conf = new ReplConf

    println(conf.getTimeAsMS(JOB_CANCEL_TIMEOUT))

    //    println(JOB_CANCEL_TIMEOUT.stringConverter.apply(
    //      conf.get(JOB_CANCEL_TIMEOUT)
    //    ))
  }
}
