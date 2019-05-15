package rainpoetry.app.analyst.server


import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean

import org.apache.spark.sql.SparkSession
import rainpoetry.app.analyst.server.delay.DelayCloseSession
import rainpoetry.livy.repl.bean.Statement
import rainpoetry.livy.repl.session.{ReplSession, SessionState}
import rainpoetry.livy.utils.Logging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

class RpcSession(spark: SparkSession,
                 val executorId: Int,
                 val sessionId: String,
                 sessionManager: SessionManager,
                 sessionTimeout: Long) extends Logging {

  val session = new ReplSession(spark, sessionId)
  // 等待 session 启动

  var isStart = new AtomicBoolean(false)

  val future = session.start()

  val delayedOperation = new DelayCloseSession(sessionTimeout, this)

  def execute(code: String, codeType: String): Int = {
    session.execute(code, codeType)
  }

  def executeSync(code: String, codeType: String): Int = {
    session.executeSync(code, codeType)
  }

  def cancel(statementId: Int): Unit = {
    session.cancel(statementId)
  }

  def progress(statementId: Int): Double = {
    session.progressOfStatement(statementId)
  }

  def statement(statementId: Int): Option[Statement] = {
    session.statements.get(statementId)
  }

  def statements(): List[Statement] = {
    session.statements.map(_._2).toList
  }

  def onExpiry(): Unit = {
    sessionManager.releaseIdleSession(sessionId)
    close()
  }

  def state(): SessionState = {
    session.state
  }

  def changeStatusToRun(): Boolean = {
    if (delayedOperation.completable.compareAndSet(false, true)) {
      debug(s"remove session from purgatory: ${sessionId}")
      delayedOperation.forceComplete()
    } else {
      false
    }
  }

  def changeStatusToIdle() = {
    delayedOperation.reset()
    sessionManager.inflightToIdle(sessionId)
  }

  def close(): Unit = {
    session.close()
  }


  def onComplete(): Unit = {
    sessionManager.idleToInflight(sessionId)
  }

  def waitComplete(): Unit = {
    if (!isStart.get()) {
      val start = Instant.now()
      Await.result(future, Duration.Inf)
      val end = Instant.now()
      println(s"wait start duration: ${java.time.Duration.between(start, end)}")
      isStart.compareAndSet(false, true)
    }
  }
}

object RpcSession {
  val NOTEXIST = -1
  val ERROR = -2
}