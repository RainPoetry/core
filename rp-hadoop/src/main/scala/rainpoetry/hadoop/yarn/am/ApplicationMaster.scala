package rainpoetry.hadoop.yarn.am

import java.net.URI
import java.security.PrivilegedExceptionAction

import org.apache.hadoop.fs.Path
import org.apache.hadoop.security.UserGroupInformation
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.records.{ApplicationAttemptId, LocalResource, LocalResourceType, LocalResourceVisibility}
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.Records
import rainpoetry.hadoop.HadoopConf
import rainpoetry.hadoop.common.Utils
import rainpoetry.hadoop.config._
import rainpoetry.hadoop.yarn.YarnUtils

import scala.collection.mutable
import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/19
 * description:
 *
 *  一. AM 和 RM 交互(主要用于申请资源)
 *      1. registerApplicationMaster:  向 ResourceManager 注册 AM
 *      2. allocate:  向 ResourceManager 申请资源
 *      3. finishApplicationMaster: 告诉 ResourceManager 应用程序执行完毕，并退出
 *
 *   二.AM 和 NM 交互(主要是启动容器、查询容器状态、停止容器)
 *      1. startContainer: 与对应的 NodeManager 通信以启动 Container
 *      2. getContainerStatus: 向 NodeManager 查询 Container 运行状态
 *      3. stopContainer: 释放 Container
 *
 */

class ApplicationMaster(args: ApplicationMasterArguments,
                        cfg: HadoopConf,
                        yarnConf: YarnConfiguration) extends Logging {

  private val appAttemptId =
    if (System.getenv(ApplicationConstants.Environment.CONTAINER_ID.name()) != null) {
      YarnUtils.getContainerId.getApplicationAttemptId()
    } else {
      null
    }

  private val client: YarnRMClient = new YarnRMClient

  @volatile private var exitCode = 0

  def run(): Int = {

    info("ApplicationAttemptId: " + appAttemptId)


    exitCode
  }

  def execute(): Unit = {
    val hostName = Utils.localHostName()
    val amCores = cfg.get(AM_CORES)

    // 注册 AM 到 RM 上
    registerAM(hostName, -1, cfg,appAttemptId)

  }

  def createAllocator(): Unit = {

  }

  def prepareLocalResources(): Map[String, LocalResource] = {
    info("Preparing Local resources")

    val resources = mutable.HashMap[String, LocalResource]()

    def setupDistributeCache(
                            file: String,
                            rtype:LocalResourceType,
                            timestamp:String,
                            size:String,
                            vis: String
                            ): Unit = {
      val uri = new URI(file)
      val amJar = Records.newRecord(classOf[LocalResource])
      amJar.setType(rtype)
      amJar.setVisibility(LocalResourceVisibility.valueOf(vis))
      amJar.setTimestamp(timestamp.toLong)
      amJar.setSize(size.toLong)

      val fileName= Option(uri.getFragment).getOrElse(new Path(uri).getName)
      resources(fileName) = amJar
    }


  }
  def registerAM(
                  host: String,
                  port: Int,
                  conf: HadoopConf,
                  appAttempt: ApplicationAttemptId): Unit = {
    val appId = appAttempt.getApplicationId.toString
    val attemptId = appAttempt.getAttemptId.toString
    val historyAddress = ""
    client.register(host,port,yarnConf,conf,historyAddress)
  }

}

object ApplicationMaster {

  private var master: ApplicationMaster = _

  def main(args: Array[String]): Unit = {
    val amArgs = new ApplicationMasterArguments(args)
    val config = new HadoopConf
    val conf = new YarnConfiguration(YarnUtils.newConfiguration(config))
    master = new ApplicationMaster(amArgs, config, conf)

    val user = Option(System.getenv("SPARK_USER"))
      .getOrElse(UserGroupInformation.getCurrentUser().getShortUserName())
    val ugi = UserGroupInformation.createRemoteUser(user)
    ugi.addCredentials(UserGroupInformation.getCurrentUser().getCredentials)
    ugi.doAs(new PrivilegedExceptionAction[Unit]() {
      override def run(): Unit = System.exit(master.run())
    })
  }
}
