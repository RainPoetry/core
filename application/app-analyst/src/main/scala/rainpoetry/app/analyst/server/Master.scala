package rainpoetry.app.analyst.server

import rainpoetry.app.analyst.core.AnalystConf
import rainpoetry.protocal.client.RequireExecutor
import rainpoetry.protocal.executor._
import rainpoetry.protocal.server.ResponseExecutor
import rainpoetry.spark.rpc.{RpcCallContext, RpcConf, RpcEnv, ThreadSafeRpcEndpoint}

import scala.collection.mutable
import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

class Master(override val rpcEnv: RpcEnv) extends Logging with ThreadSafeRpcEndpoint {

  import rainpoetry.app.analyst.config._

  val conf = new AnalystConf

  val parallelism = conf.get(ANALYST_PARALLELISM)
  var cacheExecutor = new mutable.HashMap[String, Executor]()

  override def onStart(): Unit = {
    (0 to parallelism - 1).foreach(id => {
      val executor = new Executor(rpcEnv, id, conf)
      executor.start()
    })
  }

  override def receive: PartialFunction[Any, Unit] = {
    case RegisterWorker(executor) =>
      info(s"executor register to the master ：${executor.ENDPOINT_NAME}")
      cacheExecutor(executor.ENDPOINT_NAME) =  executor
    case UnRegisterWorker(executor) =>
      cacheExecutor.remove(executor.ENDPOINT_NAME)
      info(s"executor Unregister to the master ：${executor.ENDPOINT_NAME}")
  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case RequireExecutor =>
      val executor = cacheExecutor.minBy(_._2.connects)
      context.reply(ResponseExecutor(rpcEnv.address, executor._2.ENDPOINT_NAME))
  }

  override def onStop(): Unit = {
    for (executor <- cacheExecutor) {
      executor._2.onStop()
    }
  }
}

object Master {
  val SYSTEM_NAME = "Analyst_Master"
  val ENDPOINT_NAME = "master"
  val port = 17077

  def main(args: Array[String]): Unit = {
    val rpcEnv = RpcEnv.create(SYSTEM_NAME, "localhost", port, new RpcConf)
    rpcEnv.setupEndpoint(ENDPOINT_NAME, new Master(rpcEnv))
    rpcEnv.awaitTermination()
  }
}
