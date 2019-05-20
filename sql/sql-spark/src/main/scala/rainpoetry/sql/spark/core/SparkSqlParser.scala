package rainpoetry.sql.spark.core

/*
 * User: chenchong
 * Date: 2019/5/20
 * description:
 */

class SparkSqlParser extends AbstractSqlParser {

  def command(code: String): ExecutePlan = {
      execute(code)
  }

}

object SparkSqlParser{
  def main(args: Array[String]): Unit = {
    val sql  =
      """
        |load text.'sasa_sas' where data = '123' and name = 'ok' partitionBy 'data' as data;
        |save overwrite data as text.'G:/result.txt'  partitionBy 'data';
        |select * from data;
        |spark.dea.say.hello;
      """.stripMargin

    val parser = new SparkSqlParser
    val plan = parser.command(sql)
    plan.foreach{
      case plan:SqlPlan =>
        println("sql: "+ plan.code)
      case plan:SparkCodePlan =>
        println("code:"+plan.code)
    }

  }
}
