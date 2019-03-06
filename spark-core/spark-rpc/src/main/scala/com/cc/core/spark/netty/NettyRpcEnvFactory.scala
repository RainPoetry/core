package com.cc.core.spark.netty

import com.cc.core.spark.common.SparkLog
import com.cc.core.spark.rpc.{RpcEnv, RpcEnvConfig, RpcEnvFactory}
import sun.misc.ObjectInputFilter.Config

/**
  * User: chenchong
  * Date: 2019/3/2
  * description:
  */
class NettyRpcEnvFactory extends RpcEnvFactory with SparkLog{
  override def create(config: RpcEnvConfig): RpcEnv = {

  }
}
