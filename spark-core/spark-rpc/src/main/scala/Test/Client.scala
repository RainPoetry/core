package Test

import rainpoetry.spark.rpc.{RpcAddress, RpcConf, RpcEnv}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/*
 * User: chenchong
 * Date: 2019/4/3
 * description:
 */

object Client {

  def main(args: Array[String]): Unit = {

    val serverName = "client"
    val conf = new RpcConf
    val rpcENv = RpcEnv.create
    val ref  = rpcENv.setupEndpointRef(RpcAddress("localhost",8087),"master")

    val future = ref.ask[Reply](Message("sas"))
    future.onComplete {
      case Success(info) => println(s"sucess:$info")
      case Failure(info) => println(s"failure: $info")
    }
//    Await.result(future,Duration.Inf)
    rpcENv.awaitTermination()
  }

}
