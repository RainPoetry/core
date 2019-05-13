package rainpoetry.livy.repl.sql

import java.lang.reflect.InvocationTargetException

import org.apache.spark.sql.SparkSession
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.{Extraction, _}
import rainpoetry.livy.repl.{APPLICATION_JSON, Interpreter}
import rainpoetry.livy.utils.Logging

import scala.util.control.NonFatal


/*
 * User: chenchong
 * Date: 2019/5/9
 * description:
 */

class SQLInterpreter(spark: SparkSession) extends Interpreter with Logging{

  implicit val formats = DefaultFormats


  override def kind: String = "sql"

  /**
    * Start the Interpreter.
    */
  override def start(): Unit = {

  }

  /**
    * Execute the code and return the result, it may
    * take some time to execute.
    */
  override protected[repl] def execute(code: String): Interpreter.ExecuteResponse = {
    try {
      val result = spark.sql(code)
      val schema = parse(result.schema.json)

      // Get the row data
      val rows = result.take(100)
        .map {
          _.toSeq.map {
            // Convert java BigDecimal type to Scala BigDecimal, because current version of
            // Json4s doesn't support java BigDecimal as a primitive type (LIVY-455).
            case i: java.math.BigDecimal => BigDecimal(i)
            case e => e
          }
        }

      val jRows = Extraction.decompose(rows)

      Interpreter.ExecuteSuccess(
        APPLICATION_JSON -> (("schema" -> schema) ~ ("data" -> jRows)))
    } catch {
      case e: InvocationTargetException =>
        warn(s"Fail to execute query $code", e.getTargetException)
        val cause = e.getTargetException
        Interpreter.ExecuteError("Error", cause.getMessage, cause.getStackTrace.map(_.toString))
      case NonFatal(f) =>
        warn(s"Fail to execute query $code", f)
        Interpreter.ExecuteError("Error", f.getMessage, f.getStackTrace.map(_.toString))
    }
  }

  /** Shut down the interpreter. */
  override def close(): Unit = {}
}
