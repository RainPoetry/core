package com.cc.sql.executor

import com.cc.antlr.SqlParser._
import com.cc.shell.engine.repl.SparkInterpreter
import com.cc.sql.ExecutorTracker
import org.apache.spark.sql.{SaveMode, SparkSession}

/*
 * User: chenchong
 * Date: 2019/4/9
 * description:
 */

class SaveExecutor(mode:SaveMode,
                   path: String,
                   format: String,
                   option: Map[String, String],
                   tableName: String,
                   partitionByCol:Array[String],
                   numPartition: Int) extends Executor {
  override protected def parse(session: SparkSession, interpreter: SparkInterpreter): ExecutorTracker = {
    commonParse {
      val oldDataFrame = session.table(tableName)
      var writer = oldDataFrame.coalesce(numPartition).write
      writer = writer.format(format).mode(mode).partitionBy(partitionByCol: _*).options(option)
      format match {
        case "json" | "csv" | "orc" | "parquet" | "text" =>
          writer.option("header", "true").save(path)
        case "kafka" =>
          writer.option("topics", path).save()
        case "jdbc" =>
          writer.option ("dbtable",path).save()
        case _ =>
          writer.save(path)
      }
      success(s"success: 存储路径：${path}")
    }
  }
}

object SaveExecutor extends CommonOperate {

  def apply(ctx: SqlContext): SaveExecutor = {
    var mode = SaveMode.ErrorIfExists
    var final_path = ""
    var format = ""
    var option = Map[String, String]()
    var tableName = ""
    var partitionByCol = Array[String]()
    var numPartition: Int = 1
    (0 until ctx.getChildCount).foreach { tokenIndex =>
      ctx.getChild(tokenIndex) match {
        case s: FormatContext =>
          format = s.getText.trim
        case s: PathContext =>
          final_path = cleanStr(s.getText)
        case s: TableNameContext =>
          tableName = s.getText
        case _: OverwriteContext =>
          mode = SaveMode.Overwrite
        case _: AppendContext =>
          mode = SaveMode.Append
        case _: ErrorIfExistsContext =>
          mode = SaveMode.ErrorIfExists
        case _: IgnoreContext =>
          mode = SaveMode.Ignore
        case _: UpdateContext =>
          option += ("savemode" -> "update")
        case s: ColContext =>
          partitionByCol = cleanStr(s.getText).split(",")
        case e:ExpressionContext =>
          pairStr(e.expr().getText, e.STRING.getText) {
            case (k,v) => option += (k -> v)
          }
        case b:BooleanExpressionContext =>
          pairStr(b.expression.expr.getText, b.expression.STRING.getText) {
            case (k, v) => option += (k -> v)
          }
        case s: NumPartitionContext =>
          numPartition = s.getText.toInt
        case _ =>
      }
    }
    new SaveExecutor(mode,final_path,format,option,tableName,partitionByCol,numPartition)
  }
}
