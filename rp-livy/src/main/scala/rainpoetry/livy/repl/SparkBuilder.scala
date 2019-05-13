package rainpoetry.livy.repl

import org.apache.spark.sql.SparkSession

/*
 * User: chenchong
 * Date: 2019/5/9
 * description:
 */

class SparkBuilder {


}

object SparkBuilder {

  lazy val spark: SparkSession = {
    SparkSession.builder()
      .master("local")
      .appName("data-analyst")
      .getOrCreate()
  }

}
