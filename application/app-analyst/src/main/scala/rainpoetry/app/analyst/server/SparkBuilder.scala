package rainpoetry.app.analyst.server

import org.apache.spark.sql.SparkSession

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 */

object SparkBuilder {

  def create(): SparkSession = {
    SparkSession.builder()
      .appName("data-analyst")
      .master("local")
      .getOrCreate()
  }

  def set(spark: SparkSession, k: String, v: String): Unit = {
    spark.sessionState.conf.setConfString(k, v)
  }

}
