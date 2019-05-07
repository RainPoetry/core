package rainpoetry.spark.rpc

import rainpoetry.spark.rpc.common.Utils

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

case class RpcAddress(host: String, port: Int) {

  def hostPort: String = host + ":" + port

  def toRpcURL: String = "rpc://" + hostPort

  override def toString: String = hostPort

}

object RpcAddress {

  def fromURIString(uri: String): RpcAddress = {
    val uriObj = new java.net.URI(uri)
    RpcAddress(uriObj.getHost, uriObj.getPort)
  }

  def fromRpckURL(url: String): RpcAddress = {
    val (host, port) = Utils.extractHostPortFromSparkUrl(url)
    RpcAddress(host, port)
  }
}
