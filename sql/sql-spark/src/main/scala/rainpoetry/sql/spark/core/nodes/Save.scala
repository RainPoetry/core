package rainpoetry.sql.spark.core.nodes

import rainpoetry.sql.spark.antlr.SqlParser._
import rainpoetry.sql.spark.core.{ExecutePlan, SparkCodePlan}

/*
 * User: chenchong
 * Date: 2019/5/20
 * description:
 */

case class Save(override val code: String,
                plan: Option[ExecutePlan]) extends SparkCodePlan(code) {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

object Save extends CommonOperate {
  def apply(ctx: SqlContext,plan: Option[ExecutePlan]): Save = {
    var mode = "SaveMode.ErrorIfExists"
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
          mode = "SaveMode.Overwrite"
        case _: AppendContext =>
          mode = "SaveMode.Append"
        case _: ErrorIfExistsContext =>
          mode = "SaveMode.ErrorIfExists"
        case _: IgnoreContext =>
          mode = "SaveMode.Ignore"
        case _: UpdateContext =>
          option += ("savemode" -> "update")
        case s: ColContext =>
          partitionByCol = cleanStr(s.getText).split(",")
        case e: ExpressionContext =>
          pairStr(e.expr().getText, e.STRING.getText) {
            case (k, v) => option += (k -> v)
          }
        case b: BooleanExpressionContext =>
          pairStr(b.expression.expr.getText, b.expression.STRING.getText) {
            case (k, v) => option += (k -> v)
          }
        case s: NumPartitionContext =>
          numPartition = s.getText.toInt
        case _ =>
      }
    }
    val code = generateCode(mode, final_path, format, option, tableName, partitionByCol, numPartition)
    Save(code,plan)
  }

  def generateCode(mode: String,
                   path: String,
                   format: String,
                   option: Map[String, String],
                   tableName: String,
                   partitionByCol: Array[String],
                   numPartition: Int): String = {
    val newPartitions = partitionByCol.map(k=>s"'${k.trim}'")
    var code =
      s"""
         |spark.table('${tableName.trim}').coalesce(${numPartition}).write
         |.format('${format}').mode(${mode}).partitionBy(${newPartitions.mkString(",")})
      """.stripMargin
    option.foreach {
      case (k, v) =>
        code += s".option(${k},${v})"
    }
    format match {
      case "json" | "csv" | "orc" | "parquet" | "text" =>
        code += s".option('header', 'true').save('${path}')"
      case "kafka" =>
        code += s".option('topics','${path}').save()"
      case "jdbc" =>
        code += s".option('dbtable', '${path}').save()"
      case _ =>
        code += s".save('${path}')"
    }
    code
  }
}
