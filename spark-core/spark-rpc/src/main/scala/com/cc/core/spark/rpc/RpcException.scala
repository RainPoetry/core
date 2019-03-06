package com.cc.core.spark.rpc

/**
  * User: chenchong
  * Date: 2019/3/2
  * description:
  */
class RpcException(message: String, cause: Throwable)
  extends Exception(message, cause) {

  def this(message: String) = this(message, null)
}
