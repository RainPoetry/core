package com.cc.core.spark.rpc

/**
  * User: chenchong
  * Date: 2019/3/2
  * description:
  */
object RpcEnv {

  def create(name: String, host: String, port: Int): RpcEnv = {
    create(name,host,port,new RpcEnvConfig())
  }

  def create(name: String,
             host: String,
             port: Int,
             conf: RpcEnvConfig): RpcEnv = {

  }

}

private[spark] abstract class RpcEnv() {

}
