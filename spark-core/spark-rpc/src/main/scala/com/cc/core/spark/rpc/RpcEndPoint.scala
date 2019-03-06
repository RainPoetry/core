package com.cc.core.spark.rpc

/**
  * User: chenchong
  * Date: 2019/3/2
  * description:
  */
trait RpcEndPoint {

  val rpcEnv: RpcEnv

  final def self: RpcEndpointRef = {
    require(rpcEnv != null, "rpcEnv has not been initialized")

  }

}
