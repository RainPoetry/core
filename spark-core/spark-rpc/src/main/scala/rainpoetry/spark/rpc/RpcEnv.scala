package rainpoetry.spark.rpc

import java.nio.channels.ReadableByteChannel

import rainpoetry.spark.rpc.common.RpcUtils
import rainpoetry.spark.rpc.netty.NettyRpcEnvFactory

import scala.concurrent.Future

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

object RpcEnv {

  def create(): RpcEnv = {
    create(name = "", "", 0, new RpcConf(), clientMode = true)
  }

  def create(
              name: String,
              host: String,
              port: Int,
              config: RpcConf,
              clientMode: Boolean = false): RpcEnv = {
    create(name, host, port, host, config, 0, clientMode)
  }


  def create(
              name: String,
              host: String,
              port: Int,
              advertiseAddress: String,
              config: RpcConf,
              cores: Int,
              clientMode: Boolean): RpcEnv = {
    val conf = new RpcEnvConfig(config, name, host, advertiseAddress, port, cores, clientMode)
    new NettyRpcEnvFactory().create(conf)
  }


}

abstract class RpcEnv(conf: RpcConf) {

  private[spark] val defaultLookupTimeout = RpcUtils.lookupRpcTimeout(conf)

  private[rpc] def endpointRef(endpoint: RpcEndpoint): RpcEndpointRef


  def address: RpcAddress


  def setupEndpoint(name: String, endpoint: RpcEndpoint): RpcEndpointRef


  def asyncSetupEndpointRefByURI(uri: String): Future[RpcEndpointRef]


  def setupEndpointRefByURI(uri: String): RpcEndpointRef = {
    defaultLookupTimeout.awaitResult(asyncSetupEndpointRefByURI(uri))
  }

  def setupEndpointRef(address: RpcAddress, endpointName: String): RpcEndpointRef = {
    setupEndpointRefByURI(RpcEndpointAddress(address, endpointName).toString)
  }

  def stop(endpoint: RpcEndpointRef): Unit

  def shutdown(): Unit

  def awaitTermination(): Unit

  def deserialize[T](deserializationAction: () => T): T

  def openChannel(uri: String): ReadableByteChannel

}

case class RpcEnvConfig(
                         config: RpcConf,
                         name: String,
                         bindAddress: String,
                         advertiseAddress: String,
                         port: Int,
                         cores: Int,
                         clientMode: Boolean)


