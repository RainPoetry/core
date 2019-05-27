package rainpoetry.hadoop.common

import java.util.PriorityQueue

import scala.rainpoetry.common.Logging
import scala.util.Try

/*
 * User: chenchong
 * Date: 2019/4/23
 * description:
 */

class ShutdownHookManager extends Logging{

  private val hooks = new PriorityQueue[SparkShutdownHook]()

  @volatile private var shuttingdown = false

  def install(): Unit = {
    val hookTask = new Runnable {
      override def run(): Unit = runAll()
    }
  }

  def runAll(): Unit = {
    shuttingdown = true
    var nextHook:SparkShutdownHook = null
    while({nextHook = hooks.synchronized{hooks.poll()};nextHook !=null}) {
      Try(Utils.logUncaughtExceptions(nextHook.run()))
    }
  }

}

object ShutdownHookManager {
  val DEFAULT_SHUTDOWN_PRIORITY = 100


  private lazy val shutdownHooks = {

  }
}


private class SparkShutdownHook(val priority: Int,
                                hook: () => Unit) extends Comparable[SparkShutdownHook] {
  override def compareTo(o: SparkShutdownHook): Int = {
    o.priority - priority
  }

  def run(): Unit = hook
}