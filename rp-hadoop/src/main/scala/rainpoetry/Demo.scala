package rainpoetry

import org.apache.hadoop.yarn.conf.YarnConfiguration
import rainpoetry.hadoop.HadoopSession

/*
 * User: chenchong
 * Date: 2019/4/19
 * description:
 */

object Demo {

  def main(args: Array[String]): Unit = {

    HadoopSession.builder()
      .config(s"rp.hadoop.${YarnConfiguration.RM_ADDRESS}", "store01.hdp:8050")
      .config(s"rp.hadoop.${YarnConfiguration.RM_HOSTNAME}", "store01.hdp")
      .config(s"rp.hadoop.${YarnConfiguration.RM_SCHEDULER_ADDRESS}", "store01.hdp:8030")
      .config(s"rp.hadoop.${YarnConfiguration.RM_RESOURCE_TRACKER_ADDRESS}", "store01.hdp:8031")
      .config(s"rp.hadoop.${YarnConfiguration.RM_WEBAPP_ADDRESS}", "store01.hdp:8088")
      .config(s"rp.hadoop.fs.defaultFS", "hdfs://172.18.130.100:8020")
      .config(s"rp.yarn.mainClass", "com.cc.Demo")
      .config(s"rp.yarn.jars", "G:\\tmp\\demo.jar")
      .user("hdfs")
      .getOrCreate()
      .YARN.submitApplication()

  }

}
