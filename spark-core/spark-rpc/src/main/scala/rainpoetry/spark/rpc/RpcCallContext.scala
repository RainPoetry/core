package rainpoetry.spark.rpc

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

trait RpcCallContext {

  def reply(response: Any): Unit

  def sendFailure(e: Throwable): Unit

  def senderAddress: RpcAddress
}
