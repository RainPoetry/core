package rainpoetry.sql.spark.core.nodes

import java.util.UUID

import rainpoetry.sql.spark.antlr.SqlParser._
import rainpoetry.sql.spark.core.{ExecutePlan, SparkCodePlan}

/*
 * User: chenchong
 * Date: 2019/5/20
 * description:
 */

case class Load(override val code: String,
                plan: Option[ExecutePlan]) extends SparkCodePlan(code) {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

object Load extends CommonOperate {

  def apply(ctx: SqlContext, plan: Option[ExecutePlan]): Load = {
    var format = ""
    var path = ""
    var config = Map[String, String]()
    var tableName = ""
    (0 until ctx.getChildCount).foreach(tokenIndex =>
      ctx.getChild(tokenIndex) match {
        case f: FormatContext => format = cleanStr(f.getText.trim)
        case p: PathContext => path = cleanStr(p.getText)
        case e: ExpressionContext =>
          pairStr(e.expr.getText, e.STRING.getText) {
            case (k, v) => config += (k -> v)
          }
        case b: BooleanExpressionContext =>
          pairStr(b.expression.expr.getText, b.expression.STRING.getText) {
            case (k, v) => config += (k -> v)
          }
        case t: TableNameContext => tableName = t.getText
        case _ => new Exception
      })
    val code = generateCode(format, path, config, tableName)
    Load(code, plan)
  }

  private def generateCode(format: String,
                           path: String = "",
                           config: Map[String, String],
                            tableName: String = ""): String = {

    var code = "val source = spark.read"
    config.foreach{
      case (k, v) => code += s".config('${k.trim}','${v}')"
    }
    if (config.contains("schema")) {
      code += s".schema(${config("schema")})"
    }
    format match {
      case "jdbc" =>
        code += s".format('${format}').load()"
      case "json" | "csv" | "orc" | "parquet" | "text" =>
        code += s".format('${format}').option('header', 'true').load('${path.trim}')"
      case "kafka" =>
        code += s".format('${format}').load() " +
          s".selectExpr('CAST(key AS STRING)', 'CAST(value AS STRING)', 'topic', 'partition', 'offset', 'timestamp', 'timestampType')"
      case _ =>
        code += s".format('${format}').load() "
    }
    code += ";"
    var viewNames  = tableName.trim
    if (tableName.eq("")) {
      viewNames = UUID.randomUUID().toString.replace("-", "")
    }
    code += s"source.createOrReplaceTempView('${viewNames}');"
    code += s"spark.table('${viewNames}').toJSON.collect()"
    code
  }
}

