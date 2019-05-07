package com.cc.sql.executor

import java.util.UUID

import com.cc.antlr.SqlParser.SqlContext
import com.cc.shell.engine.repl.SparkInterpreter
import com.cc.sql.ExecutorTracker
import org.apache.spark.sql.SparkSession

/*
 * User: chenchong
 * Date: 2019/4/9
 * description:
 */

class SelectExecutor(sql:String, var tableName: String) extends Executor {
  override protected def parse(session: SparkSession, interpreter: SparkInterpreter): ExecutorTracker = {
    commonParse{
      if (tableName.eq("")) {
        tableName  = UUID.randomUUID().toString.replace("-","")
      }
      session.sql(sql).createOrReplaceTempView(tableName)
      val rs = session.table(tableName).toJSON.collect()
//      val snapshot = readStdout(session.table(tableName).show(false))
      success(rs,s"success: 临时视图名称：${tableName}")
    }
  }
}

object SelectExecutor {
  def apply(context:SqlContext): SelectExecutor = {
    var tableName = ""
    var sql = context.getText
    if (sql.contains("as")) {
      tableName = context.tableName().getText
      sql = sql.split("as")(0)
    }
    new SelectExecutor(sql,tableName)
  }

}
