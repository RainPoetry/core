package com.cc.sql.executor

import com.cc.antlr.SqlParser.SqlContext
import com.cc.shell.engine.repl.SparkInterpreter
import com.cc.sql.ExecutorTracker
import org.apache.spark.sql.SparkSession


/*
 * User: chenchong
 * Date: 2019/4/9
 * description:
 */

class CodeExecutor(code: String) extends Executor {

  override protected def parse(session: SparkSession, interpreter: SparkInterpreter): ExecutorTracker = {
    val response = interpreter.execute(code)
    val status = responseMessage(response)
    new ExecutorTracker().status(status)
  }
}

object CodeExecutor {
  def apply(sql: SqlContext): CodeExecutor = {
    new CodeExecutor(sql.getText)
  }
}
