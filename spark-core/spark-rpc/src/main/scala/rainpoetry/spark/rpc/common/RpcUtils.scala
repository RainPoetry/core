package rainpoetry.spark.rpc.common

import rainpoetry.spark.rpc.{RpcConf, RpcTimeout}
import rainpoetry.spark.rpc.config._

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

object RpcUtils {

  def numRetries(conf: RpcConf): Int = {
    conf.get(RPC_NUM_RETRIES)
  }

  /** Returns the configured number of milliseconds to wait on each retry */
  def retryWaitMs(conf: RpcConf): Long = {
    conf.get(RPC_RETRY_WAIT)
  }

  /** Returns the default Spark timeout to use for RPC ask operations. */
  def askRpcTimeout(conf: RpcConf): RpcTimeout = {
    RpcTimeout(conf, Seq(RPC_ASK_TIMEOUT.key, NETWORK_TIMEOUT.key), "120s")
  }

  def lookupRpcTimeout(conf: RpcConf): RpcTimeout = {
    import rainpoetry.spark.rpc.config._
    RpcTimeout(conf, Seq(RPC_LOOKUP_TIMEOUT.key, NETWORK_TIMEOUT.key), "120s")
  }

}
