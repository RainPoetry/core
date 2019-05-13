package scala.rainpoetry.common.config

import org.scalatest.FunSuite

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

class ConfigTest extends FunSuite {



  test("1") {
    val conf = new MyConf
    // result: hello
    println(conf.get(Config))
  }

  test("2") {
    val conf = new MyConf
    // result: throw Exception
    println(conf.get("demo"))
  }

  test("3") {
    val conf = new MyConf
    conf.set("demo","888888")
    // result: 88888
    printf(conf.get("demo"))
  }

  test("4") {
    val conf = new MyConf
    conf.set(Config,"99999")
    println(conf.get(Config))
  }


  val Config = ConfigBuilder("demo")
    .stringConf
    .createWithDefault("hello")
}

class MyConf extends CommonConf {
  override val prefix: String = "11"

}
