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

class ExplainExecutor(sql: String) extends Executor {
  override protected def parse(session: SparkSession, interpreter: SparkInterpreter): ExecutorTracker = {
    commonParse{
      success("successs:"+session.sql(sql).queryExecution.toString())
    }
  }
}

object ExplainExecutor {
  def apply(sql:SqlContext): ExplainExecutor = new ExplainExecutor(sql.getText)
}
