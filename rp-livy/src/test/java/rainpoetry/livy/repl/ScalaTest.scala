package rainpoetry.livy.repl

/*
 * User: chenchong
 * Date: 2019/5/8
 * description:
 */

import java.io.ByteArrayOutputStream

import org.apache.spark.SparkConf
import org.apache.spark.launcher.SparkLauncher
import org.apache.spark.sql.SparkSession
import org.json4s.DefaultFormats
import org.scalatest.FunSuite
import rainpoetry.livy.repl.Interpreter.{ExecuteError, ExecuteSuccess}
import rainpoetry.livy.repl.session.{ReplConf, ReplSession}
import rainpoetry.livy.repl.spark.SparkInterpreter
import rainpoetry.livy.repl.sql.SQLInterpreter

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ScalaTest extends FunSuite {

  test("add") {
    val conf = new SparkConf()
      .setAppName("demo")
      .setMaster("local")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    val interperter = new SparkInterpreter(spark)
    interperter.start()
    val data =
      """
        |1+1
        |2*9
      """.stripMargin
    interperter.execute(data) match {
      case ExecuteSuccess(info) => println(info)
    }
    interperter.close()
  }

  test("scala console to outputStream") {
    val outputStream = new ByteArrayOutputStream()
    scala.Console.withOut(outputStream) {
      println("hello")
    }
    println(s"out: ${outputStream.toString}")
  }


  test("session") {
    val spark = SparkSession.builder()
      .master("local")
      .appName("demo")
      .getOrCreate()

    SparkSession.clearDefaultSession()
    val s2 = SparkSession.builder()
      .getOrCreate()

    println(s2.conf.getAll)
  }

  // 最终还是调用的 Spark-Submit 脚本
  test("spark launcher") {
    val launcher = new SparkLauncher()
      .setSparkHome(System.getenv("SPARK_HOME"))
      .setAppResource(SparkLauncher.NO_RESOURCE)
      .addJar("G:/tmp/demo.jar")
      .setMaster("local")
    //    launcher.setPropertiesFile(confFile.getAbsolutePath)
    launcher.setMainClass("com.cc.Demo")
    //    val process = launcher.launch()
    //    val exitCode = process.waitFor
    //    if (exitCode != 0) {
    //     println("Child process exited with code {}.", exitCode)
    //    }
    launcher.startApplication()
    Thread.sleep(1000000)
  }

  test("sql test") {
    implicit val formats = DefaultFormats
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("demo")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    val interpreter = new SQLInterpreter(spark)
    interpreter.execute("select * from dual") match {
      case ExecuteError(name, value, trace) => println(s"name:${name} \n value:${value} \n trace:${trace}")
      case ExecuteSuccess(info) => println(info.obj)
    }
  }

  test("session  repl") {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("demo")
      .getOrCreate()
    val session = new ReplSession(spark, new ReplConf,"11")
    val f = session.start()
    Await.result(f,Duration.Inf)
    val data =
      """
        |1+1
        |2*9
      """.stripMargin
    val statementId = session.executeSync(data,"spark")

    val st =  session.statements(statementId)
    println(st.progress + "  " + st._state.get() +"   " + st.output)


  }
}

