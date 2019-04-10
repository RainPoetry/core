package com.cc.network.server

import java.util.concurrent.atomic.AtomicInteger

import _root_.protocol.server.ResponseExecutor
import com.cc.common.{PropsUtils, ZkSession}
import com.cc.network.Executor
import com.cc.shell.engine.repl.SparkInterpreter
import com.rainpoetry.common.utils.Logging
import protocol.client.RequireExecutor
import rainpoetry.spark.rpc._

/*
 * User: chenchong
 * Date: 2019/4/3
 * description: 服务器端，管理 Executor
 */

class Master(override val rpcEnv: RpcEnv) extends Logging with ThreadSafeRpcEndpoint {

  var cacheExecutor: Array[Executor] = _
  var zk: ZkSession = _
  var interpreter: SparkInterpreter = _
  val requests: AtomicInteger = new AtomicInteger(0)

//  override def receive: PartialFunction[Any, Unit] = {
//    case Execute(sql) =>
//      val resolveSQL = sql.trim.replaceAll("\\r\\n","")
//      val sqlParser = new RpcSqlParser
//      println("execute: " + resolveSQL)
//      sqlParser.execute(resolveSQL)
//  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case RequireExecutor =>
      val counts = requests.addAndGet(1)
      val executor = cacheExecutor(counts%cacheExecutor.length)
      context.reply(ResponseExecutor(rpcEnv.address, executor.name))
  }

  override def onStart(): Unit = {
    zk = new ZkSession(PropsUtils.get("zkServers").getOrElse(throw new Exception("zkServers 配置信息不存在")))
    val masterPath = zk.register(RpcAddress("localhost", Master.port))
    val parallelism = PropsUtils.get("parallelism").getOrElse("5").toInt
    cacheExecutor = new Array[Executor](parallelism)
    val interpreter = new SparkInterpreter()
    val sparkConf = interpreter.start()
    // 根据
    (0 to parallelism-1).foreach(id => {
      val executor = new Executor(rpcEnv, interpreter, sparkConf, masterPath, id.toString)
      rpcEnv.setupEndpoint(executor.name, executor)
      cacheExecutor(id) = executor
    })
  }

  override def onStop(): Unit = {
    zk.close()
    interpreter.close()
  }

}

object Master {
  val SYSTEM_NAME = "RPC_Master"
  val ENDPOINT_NAME = "master"
  val port = 18087

  def main(args: Array[String]): Unit = {
        val rpcEnv = RpcEnv.create(SYSTEM_NAME, "localhost", port, new RpcConf)
        rpcEnv.setupEndpoint(ENDPOINT_NAME, new Master(rpcEnv))
        rpcEnv.awaitTermination()
  }
}
