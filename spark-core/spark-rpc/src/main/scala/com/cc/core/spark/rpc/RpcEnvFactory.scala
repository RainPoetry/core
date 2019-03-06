package com.cc.core.spark.rpc

/**
  * User: chenchong
  * Date: 2019/3/2
  * description:
  */
trait RpcEnvFactory {

  def create(conf: RpcEnvConfig): RpcEnv

}
