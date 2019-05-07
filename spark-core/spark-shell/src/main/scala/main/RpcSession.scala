package main

import com.cc.network.server.Master
import protocol.client.{Execute, RequireExecutor}
import protocol.executor.Reply
import protocol.server.ResponseExecutor
import rainpoetry.spark.rpc.{RpcAddress, RpcEnv}

import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/4
 * description: Client 端的交互接口
 */

class RpcSession(host: String, port: Int = Master.port) {

  val address = RpcAddress(host, port)

  val rpcEnv = RpcEnv.create

  val executor = {
    val master = rpcEnv.setupEndpointRef(address, Master.ENDPOINT_NAME)
    val response = master.askSync[ResponseExecutor](RequireExecutor)
    rpcEnv.setupEndpointRef(response.address, response.name)
  }

  def send(msg: String): Response = {
    val reply = executor.askSync[Reply](Execute(msg))
    reply.data match {
      case a:Array[String] => Response(a,reply.success,reply.duration)
      case s:String => Response(Array(s),reply.success,reply.duration)
    }
  }

  def close(): Unit = {
    rpcEnv.shutdown()
  }

}

case class Response(data:Array[String], success: Boolean, duration:Long)

object RpcSession extends Logging{
  def main(args: Array[String]): Unit = {

    val sql =
      """
        |val df = spark.read.schema("date Date ,hour int,column3 double,column4 double,
        |column5 double,column6 double,column7 double,column8 double,column9 double,
        |column10 double,column11 double").option("delimiter", " ")
        |.csv("hdfs://172.18.130.100/test/testfile/59431format.txt");
        |df.createOrReplaceTempView("test");
        |spark.sql("select column3 from test where date='1998-01-01' and hour='10'").show;
      """.stripMargin


    val sql2  =
      """
        |load text.'G:/demo.txt' where data = '123' as data;
        |save overwrite data as text.'G:/result.txt';
        |select * from data;
      """.stripMargin

    // 与服务器端建立连接
    val session = new RpcSession("localhost",18088)
    val response = session.send(sql2)
    info(s"${response.duration}   ${response.success} ")
    println(s"${response.data.mkString("\r\n")}")
    // 关闭连接
    session.close()
  }


}
