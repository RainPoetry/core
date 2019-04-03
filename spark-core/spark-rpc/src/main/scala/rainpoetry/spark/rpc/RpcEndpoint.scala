package rainpoetry.spark.rpc

import rainpoetry.spark.rpc.error.RpcException

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */
trait RpcEnvFactory {
  def create(config: RpcEnvConfig): RpcEnv
}

trait RpcEndpoint {


  val rpcEnv: RpcEnv

  final def self: RpcEndpointRef = {
    require(rpcEnv != null, "rpcEnv has not been initialized")
    rpcEnv.endpointRef(this)
  }

  def receive: PartialFunction[Any, Unit] = {
    case _ => throw new RpcException(self + " does not implement 'receive'")
  }

  /**
    * Process messages from `RpcEndpointRef.ask`. If receiving a unmatched message,
    * `SparkException` will be thrown and sent to `onError`.
    */
  def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case _ => context.sendFailure(new RpcException(self + " won't reply anything"))
  }

  /**
    * Invoked when any exception is thrown during handling messages.
    */
  def onError(cause: Throwable): Unit = {
    throw cause
  }


  def onConnected(remoteAddress: RpcAddress): Unit = {}


  def onDisconnected(remoteAddress: RpcAddress): Unit = {}


  def onNetworkError(cause: Throwable, remoteAddress: RpcAddress): Unit = {}

  def onStart(): Unit = {}


  def onStop(): Unit = {}

  final def stop(): Unit = {
    val _self = self
    if (_self != null) {
      rpcEnv.stop(_self)
    }
  }


}

trait ThreadSafeRpcEndpoint extends RpcEndpoint