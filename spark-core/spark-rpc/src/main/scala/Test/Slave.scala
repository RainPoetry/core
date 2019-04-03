package Test

import com.rainpoetry.common.utils.Logging
import rainpoetry.spark.rpc.{RpcCallContext, RpcEnv, ThreadSafeRpcEndpoint}

/*
 * User: chenchong
 * Date: 2019/4/3
 * description:
 */

class Slave (override val rpcEnv: RpcEnv) extends Logging with ThreadSafeRpcEndpoint{
  override def receive: PartialFunction[Any, Unit] = {
    case Reply(info) => println(info)
  }

  /**
    * Process messages from `RpcEndpointRef.ask`. If receiving a unmatched message,
    * `SparkException` will be thrown and sent to `onError`.
    */
  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case Message(info) =>
      context.reply(Reply(s"I have recire your message: ${info}"))
  }

  override def onStart(): Unit = {
    println("Slave start")
  }

  override def onStop(): Unit = {
    println("Slave close")
  }
}

object Slave{

}
