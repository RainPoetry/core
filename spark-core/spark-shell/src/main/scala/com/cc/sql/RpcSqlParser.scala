package com.cc.sql

import com.cc.shell.engine.repl.SparkInterpreter
import org.apache.spark.sql.SparkSession

/*
 * User: chenchong
 * Date: 2019/4/8
 * description:
 */

class RpcSqlParser(session: SparkSession, interpreter: SparkInterpreter) extends AbstractSqlParser {
  override def astBuilder: AstBuilder = new AstBuilder(session, interpreter)

  def command(command: String): (Array[JobStatus], Array[Long]) = {
    val tracker = execute(command)
    val times = Array.ofDim[Long](tracker.nums)
    val jobs = Array.ofDim[JobStatus](tracker.nums)
    (0 to tracker.nums-1).map(
      index => tracker.productElement(index) match {
        case e: ExecutorTracker =>
          times(index) = e.duration
          jobs(index) = e.jobStatus
      }
    )
    (jobs, times)
  }
}
