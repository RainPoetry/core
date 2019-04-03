package rainpoetry.spark.rpc

import java.util.concurrent.TimeUnit

import scala.rainpoetry.common.config.ConfigBuilder

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

package object config {

  val RPC_NUM_RETRIES = ConfigBuilder("rpc.numRetries")
    .intConf
    .createWithDefault(3)

  val RPC_RETRY_WAIT = ConfigBuilder("rpc.retry.wait")
    .timeConf(TimeUnit.MILLISECONDS)
    .createWithDefaultString("3s")

  val RPC_ASK_TIMEOUT = ConfigBuilder("rpc.askTimeout")
    .stringConf
    .createOptional

  val NETWORK_TIMEOUT = ConfigBuilder("rpc.network.timeout")
    .timeConf(TimeUnit.SECONDS)
    .createWithDefaultString("120s")

  val RPC_LOOKUP_TIMEOUT = ConfigBuilder("rpc.lookupTimeout")
      .stringConf
      .createOptional

  val SERIALIZE = ConfigBuilder("rpc.serialize")
    .stringConf
    .createWithDefault("rainpoetry.spark.rpc.serialize.ProtostuffSerialize")

  val RPC_IO_THREADS = ConfigBuilder("spark.rpc.io.threads")
      .intConf
      .createOptional

  val RPC_CONNECT_THREADS = ConfigBuilder("rpc.connect.threads")
      .intConf
      .createWithDefault(64)

  val RPC_NETTY_DISPATCHER_NUM_THREADS = ConfigBuilder("spark.rpc.netty.dispatcher.numThreads")
      .intConf
      .createOptional

}
