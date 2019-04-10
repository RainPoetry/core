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

  def command(command: String): (String, Long) = {
    val tracker = execute(command)
    var time: Long = 0
    val array = tracker.children.map(
      child => {
        val childTime = child.duration
        time += childTime
        val data = child.jobStatus.data
        val msg = child.jobStatus.msg
        if (childTime > 1000) {
          " 耗时： " + (childTime / 1000) + "s" + " , " + msg + "\r\n" + data
        } else {
          " 耗时： " + childTime+ "ms" + " , " + msg + "\r\n" + data
        }
      }
    )
    if (array.length == 0) {
      (s"无法解析的语句: ${command}", time)
    } else {
      (array(array.length-1), time)
    }
  }
}
