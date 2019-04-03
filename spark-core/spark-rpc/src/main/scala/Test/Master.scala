package Test

import com.rainpoetry.common.utils.Logging
import rainpoetry.spark.rpc.{RpcCallContext, RpcConf, RpcEnv, ThreadSafeRpcEndpoint}

import scala.util.{Failure, Success}

import scala.concurrent.ExecutionContext.Implicits.global

/*
 * User: chenchong
 * Date: 2019/4/3
 * description:
 */

class Master(override val rpcEnv: RpcEnv) extends Logging with ThreadSafeRpcEndpoint{
  override def receive: PartialFunction[Any, Unit] = {
    case Hello => println("hello")
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
    println("Master start")
  }

  override def onStop(): Unit = {
    println("Master close")
  }
}

object Master{
  def main(args: Array[String]): Unit = {
    val serverName = "master_cc"
    val conf = new RpcConf
    val rpcEnv = RpcEnv.create(serverName,"localhost",8087,conf)
    val endpoint = rpcEnv.setupEndpoint("master",new Master(rpcEnv))
    val slave = rpcEnv.setupEndpoint("slave",new Slave(rpcEnv))
    val future = slave.ask[Reply](Message("sas"))
    future.onComplete {
      case Success(info) => println(s"sucess:$info")
      case Failure(info) => println(s"failure: $info")
    }
    rpcEnv.awaitTermination()
  }
}