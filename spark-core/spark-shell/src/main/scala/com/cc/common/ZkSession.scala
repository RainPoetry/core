package com.cc.common

import com.cc.zookeeper.ZkClient
import rainpoetry.spark.rpc.RpcAddress

/*
 * User: chenchong
 * Date: 2019/4/3
 * description:
 */

class ZkSession(zkServers: String) extends ZkClient(zkServers, 6000, 6000, 100) {

  private val enginePath = "/rpc_engine/path"

  if (!existNode(enginePath)) {
    createRecursive(enginePath, Array())
  }

  def register(address: RpcAddress): String = {
    createSequentialEphemeralPath(enginePath + "/", address.toRpcURL.getBytes)
  }

  def register(path:String, name: String): Unit = {
    createSequentialEphemeralPath(path, name.getBytes())
  }
}