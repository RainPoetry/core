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

class ShowExecutor(sql:String) extends Executor {
  override protected def parse(session: SparkSession, interpreter: SparkInterpreter): ExecutorTracker = ???
}

object ShowExecutor{
  def apply(sql:SqlContext): ShowExecutor = new ShowExecutor(sql.getText)
}
