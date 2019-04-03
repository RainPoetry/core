package rainpoetry.spark.rpc

import rainpoetry.spark.rpc.common.RpcUtils

import scala.concurrent.Future
import scala.rainpoetry.common.Logging
import scala.reflect.ClassTag

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

abstract class RpcEndpointRef(conf: RpcConf) extends Logging{

  private[this] val maxRetries = RpcUtils.numRetries(conf)
  private[this] val retryWaitMs = RpcUtils.retryWaitMs(conf)
  private[this] val defaultAskTimeout = RpcUtils.askRpcTimeout(conf)

  def address: RpcAddress

  def name: String

  def send(message: Any): Unit

  def ask[T: ClassTag](message: Any, timeout: RpcTimeout): Future[T]

  def ask[T: ClassTag](message: Any): Future[T] = ask(message, defaultAskTimeout)

  def askSync[T: ClassTag](message: Any): T = askSync(message, defaultAskTimeout)

  def askSync[T: ClassTag](message: Any, timeout: RpcTimeout): T = {
    val future = ask[T](message, timeout)
    timeout.awaitResult(future)
  }

}
