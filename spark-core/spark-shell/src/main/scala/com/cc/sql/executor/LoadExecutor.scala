package com.cc.sql.executor

import java.util.UUID

import com.cc.antlr.SqlParser._
import com.cc.shell.engine.repl.SparkInterpreter
import com.cc.sql.ExecutorTracker
import org.apache.spark.sql.{DataFrame, SparkSession}

/*
 * User: chenchong
 * Date: 2019/4/9
 * description:
 */

class LoadExecutor(format: String,
                   path: String = "",
                   config: Map[String, String],
                   var tableName: String = "") extends Executor {
  override protected def parse(session: SparkSession, interpreter: SparkInterpreter): ExecutorTracker = {
    commonParse{
      var table: DataFrame = null
      var reader = session.read.options(config)
      if (config.contains("schema")) {
        reader.schema(config("schema"))
      }
      format match {
        case "jdbc" =>
          table = reader.format(format).load()
        case "json" | "csv" | "orc" | "parquet" | "text" =>
          table = reader.format(format).option("header", "true").load(path)
        case "kafka" =>
          table = reader.format(format).load()
            .selectExpr("CAST(key AS STRING)","CAST(value AS STRING)","topic","partition","offset","timestamp","timestampType")
        case _ =>
          table = reader.format(format).load()
      }
      if (tableName.eq("")) {
        tableName  = UUID.randomUUID().toString.replace("-","")
      }
      table.createOrReplaceTempView(tableName)
      val rs = session.table(tableName).toJSON.collect()
//      val snapshot = readStdout(session.table(tableName).show(false))
      success(rs,s"视图名称：${tableName}")
    }
  }
}

object LoadExecutor extends CommonOperate {

  def apply(ctx: SqlContext): LoadExecutor = {
    var format = ""
    var path  = ""
    var config = Map[String, String]()
    var tableName = ""
    (0 until ctx.getChildCount).foreach ( tokenIndex =>
      ctx.getChild(tokenIndex) match {
      case f:FormatContext => format = cleanStr(f.getText.trim)
      case p:PathContext => path = cleanStr(p.getText)
      case e:ExpressionContext =>
        pairStr(e.expr.getText, e.STRING.getText) {
          case (k,v) => config += (k -> v)
        }
      case b:BooleanExpressionContext =>
        pairStr(b.expression.expr.getText, b.expression.STRING.getText) {
          case (k, v) => config += (k -> v)
        }
      case t:TableNameContext => tableName = t.getText
      case _ => new Exception
    })
    new LoadExecutor(format, path, config , tableName)
  }
}
