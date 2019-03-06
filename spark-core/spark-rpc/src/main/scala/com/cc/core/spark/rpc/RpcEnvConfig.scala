package com.cc.core.spark.rpc

import com.cc.core.spark.config.AbstractConfig

/**
  * User: chenchong
  * Date: 2019/3/2
  * description:
  */
case class RpcSessionConfig(maxRetries:Long = 10,
                        retryWaitMs:Long = 30)

case class RpcEnvConfig(
                           name: String,
                           host: String,
                           port: Int,
                           conf: RpcSessionConfig
                           )


