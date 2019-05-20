package rainpoetry.sql.spark.core.nodes

/*
 * User: chenchong
 * Date: 2019/4/9
 * description:
 */

class CommonOperate {

  def singleStr[T](str: String)(f: String => T): T = {
    f(cleanStr(str))
  }

  def pairStr[T](k: String, v: String)(f:Tuple2[String,String] => T): T = {
    f(Tuple2(cleanStr(k), cleanStr(v)))
  }

  def cleanStr(str: String) : String = {
    str.replaceAll("'","").replaceAll("\"","")
  }
}
