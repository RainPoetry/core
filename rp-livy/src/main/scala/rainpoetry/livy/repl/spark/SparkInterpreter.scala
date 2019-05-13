/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rainpoetry.livy.repl.spark

import java.nio.file.{Files, Paths}

import org.apache.spark.repl.SparkILoop
import org.apache.spark.sql.SparkSession

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.Completion.ScalaCompleter
import scala.tools.nsc.interpreter.Results.Result
import scala.tools.nsc.interpreter.{IMain, JLineCompletion, JPrintWriter}

/**
  * This represents a Spark interpreter. It is not thread safe.
  */
class SparkInterpreter(protected override val spark: SparkSession) extends AbstractSparkInterpreter {

  private var sparkILoop: SparkILoop = _

  override def start(): Unit = {
    require(sparkILoop == null)
    val rootDir = getConf("spark.repl.classdir", System.getProperty("java.io.tmpdir"))
    val outputDir = Files.createTempDirectory(Paths.get(rootDir), "spark").toFile
    outputDir.deleteOnExit()
    config("spark.repl.class.outputDir", outputDir.getAbsolutePath)

    val settings = new Settings()
    settings.processArguments(List("-Yrepl-class-based",
      "-Yrepl-outdir", s"${outputDir.getAbsolutePath}"), true)
    settings.usejavacp.value = true
    settings.embeddedDefaults(Thread.currentThread().getContextClassLoader())

    sparkILoop = new SparkILoop(None, new JPrintWriter(outputStream, true))
    sparkILoop.settings = settings
    sparkILoop.createInterpreter()
    sparkILoop.initializeSynchronous()

    restoreContextClassLoader {
      sparkILoop.setContextClassLoader()

      postStart()
    }
  }

  override def close(): Unit = synchronized {
    super.close()

    if (sparkILoop != null) {
      sparkILoop.closeInterpreter()
      sparkILoop = null
    }
  }

  override protected def isStarted(): Boolean = {
    sparkILoop != null
  }

  override protected def interpret(code: String): Result = {
    sparkILoop.interpret(code)
  }

  override protected def completeCandidates(code: String, cursor: Int): Array[String] = {
    val completer: ScalaCompleter = {
      try {
        val cls = Class.forName("scala.tools.nsc.interpreter.PresentationCompilerCompleter")
        cls.getDeclaredConstructor(classOf[IMain]).newInstance(sparkILoop.intp)
          .asInstanceOf[ScalaCompleter]
      } catch {
        case e: ClassNotFoundException => new JLineCompletion(sparkILoop.intp).completer
      }
    }
    completer.complete(code, cursor).candidates.toArray
  }

  override protected def valueOfTerm(name: String): Option[Any] = {
    // IMain#valueOfTerm will always return None, so use other way instead.
    Option(sparkILoop.lastRequest.lineRep.call("$result"))
  }

  override protected def bind(name: String,
                              tpe: String,
                              value: Object,
                              modifier: List[String]): Unit = {
    sparkILoop.beQuietDuring {
      sparkILoop.bind(name, tpe, value, modifier)
    }
  }


}
