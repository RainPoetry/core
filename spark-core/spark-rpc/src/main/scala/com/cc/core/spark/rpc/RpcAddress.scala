package com.cc.core.spark.rpc

/**
  * User: chenchong
  * Date: 2019/3/2
  * description:
  */
case class RpcAddress(host: String, port: Int) {

    def hostPort: String = host + ":" + port

    override def toString: String = hostPort



}
