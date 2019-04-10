package com.cc.shell.engine

import com.cc.shell.engine.repl.SparkInterpreter
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

object SparkBuilder {

  def createSpark(sparkConf: SparkConf) = {
    val spark = SparkSession
      .builder
      .appName("cc_shell")
      .config(sparkConf)
      //动态资源调整
      //            .config("spark.dynamicAllocation.enabled", "true")
      //            .config("spark.dynamicAllocation.executorIdleTimeout", "30s")
      //            .config("spark.dynamicAllocation.maxExecutors", "100")
      //            .config("spark.dynamicAllocation.minExecutors", "0")
      //            //动态分区
      //            .config("hive.exec.dynamic.partition", "true")
      //            .config("hive.exec.dynamic.partition.mode", "nonstrict")
      //            .config("hive.exec.max.dynamic.partitions", 20000)
      //            //调度模式
      //            .config("spark.scheduler.mode", "FAIR")
      //            .config("spark.executor.memoryOverhead", "512")
                .master("local[*]")
//      .enableHiveSupport()
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    spark
  }

  def main(args: Array[String]): Unit = {
    val interpreter = new SparkInterpreter()
    val sparkConf = interpreter.start()
//    interpreter.execute()
  }

}
