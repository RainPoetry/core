package rainpoetry.livy.repl.bean

import org.apache.spark.sql.SparkSession

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 */

class SparkEntry(sparkSession:SparkSession) {

  def session() = sparkSession

}
