package com.cc.network.client

import com.rainpoetry.common.utils.Logging
import rainpoetry.spark.rpc._

/*
 * User: chenchong
 * Date: 2019/4/3
 * description:
 */

class Worker (override val rpcEnv: RpcEnv) extends Logging with ThreadSafeRpcEndpoint{

//  override def receive: PartialFunction[Any, Unit] = {
//    case sparkResult(info) => println(s"resullt: ${info}")
//  }
//
//  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
//    case sparkResult(info) => println(s"resullt: ${info}")
//  }

  override def onStart(): Unit = {}

  override def onStop(): Unit = {}
}

object Worker{

  val SYSTEM_NAME = "RPC_Worker"
  val ENDPOINT_NAME = "worker"
  val port = 18087

  def main(args: Array[String]): Unit = {
//    val rpcEnv = RpcEnv.create
//    val master = rpcEnv.setupEndpointRef(RpcAddress("localhost",Master.port),"master")
//    val result = master.askSync[sparkResult](Query("hello"))
//    println(result)
//    rpcEnv.shutdown()
  }

}
