package rainpoetry.hadoop.yarn.am

import scala.collection.mutable.ArrayBuffer

/*
 * User: chenchong
 * Date: 2019/4/23
 * description:
 */

class ApplicationMasterArguments(val args: Array[String]) {

  var params: String = null

  parseArgs(args.toList)

  private def parseArgs(inputArgs:List[String]) :Unit = {
    val argsBuffer = new ArrayBuffer[String]()
    var args = inputArgs
    while (!args.isEmpty) {
      args match {
        case ("--args") :: value :: tail =>
          params = value
          args = tail
        case _ =>
          printUsageAndExit(1,args)
      }
    }
  }

  def printUsageAndExit(exitCode: Int, unknownParam: Any = null) {
    // scalastyle:off println
    if (unknownParam != null) {
      System.err.println("Unknown/unsupported param " + unknownParam)
    }
    System.err.println("""
                         |Usage: org.apache.spark.deploy.yarn.ApplicationMaster [options]
                         |Options:
                         |  --jar JAR_PATH       Path to your application's JAR file
                         |  --class CLASS_NAME   Name of your application's main class
                         |  --primary-py-file    A main Python file
                         |  --primary-r-file     A main R file
                         |  --arg ARG            Argument to be passed to your application's main class.
                         |                       Multiple invocations are possible, each will be passed in order.
                         |  --properties-file FILE Path to a custom Spark properties file.
                       """.stripMargin)
    // scalastyle:on println
    System.exit(exitCode)
  }

}
