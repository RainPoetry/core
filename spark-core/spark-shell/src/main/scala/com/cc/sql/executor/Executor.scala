package com.cc.sql.executor

import java.io.ByteArrayOutputStream

import com.cc.shell.engine.repl.Interpreter._
import com.cc.shell.engine.repl.SparkInterpreter
import com.cc.sql.{ExecutorTracker, JobStatus, Status}
import org.apache.spark.sql.SparkSession

/*
 * User: chenchong
 * Date: 2019/4/9
 * description:
 */

abstract class Executor {

  protected val outputStream = new ByteArrayOutputStream()

  protected def parse(session: SparkSession, interpreter: SparkInterpreter): ExecutorTracker

  def  commonParse(f: => ExecutorTracker): ExecutorTracker ={
    try {
      f
    } catch {
      case e:Exception =>
        e.printStackTrace()
        failure("failure: " + e.getMessage)
    }
  }

  def exec(session: SparkSession, interpreter: SparkInterpreter): ExecutorTracker = {
    val (tracker, duration) = timeMeasure(parse(session, interpreter))
    tracker.duration(duration)
  }

  def timeMeasure[T](f: => T): (T, Long) = {
    val start = System.currentTimeMillis()
    val result = f
    val stop = System.currentTimeMillis()
    (result, stop - start)
  }

  def responseMessage(response: ExecuteResponse): JobStatus = {
    response match {
      case _: ExecuteIncomplete => responseMessage(response)
      case e: ExecuteSuccess =>
        new JobStatus("", e.content.values.values.mkString("\n"), Status.Success)
      case e: ExecuteError =>
        new JobStatus(e.traceback.mkString("\n"), e.evalue, Status.Failure)
      case e: ExecuteAborted =>
        new JobStatus("", e.message, Status.Failure)
    }
  }

  def success(data:String,msg: String = ""): ExecutorTracker = {
    new ExecutorTracker().status( new JobStatus(msg, data, Status.Success))
  }

  def failure(data: String): ExecutorTracker = {
    new ExecutorTracker().status( new JobStatus("", data, Status.Failure))
  }

  protected def readStdout[T](f: =>Unit) :String = {
    consoleToStream(f)
    val output = outputStream.toString("UTF-8")
    outputStream.reset()
    output
  }

  private def consoleToStream(f: =>Unit): Unit = {
    scala.Console.withOut(outputStream) {
      f
    }
  }

}
