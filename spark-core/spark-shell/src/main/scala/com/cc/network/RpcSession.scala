package com.cc.network

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

  def send(msg: String): String = {
    executor.askSync[Reply](Execute(msg)).msg
  }

  def close(): Unit = {
    rpcEnv.shutdown()
  }


}

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

    val session = new RpcSession("localhost")
    val reply = session.send(sql2)
    info(s"${reply}")
    session.close()
  }
}
