package rainpoetry.hadoop.yarn.am


import org.apache.hadoop.yarn.client.api.AMRMClient
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest
import org.apache.hadoop.yarn.conf.YarnConfiguration
import rainpoetry.hadoop.HadoopConf

import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/24
 * description:   Handles registering and unregistering the application with Yarn ResourceManager
 */

class YarnRMClient extends Logging{

  private var amClient:AMRMClient[ContainerRequest] = _
  private var uiHistoryAddress: String = _
  private var registered: Boolean = false

  def register(
              driverHost:String,
              driverPort:Int,
              conf: YarnConfiguration,
              cfg: HadoopConf,
              uiHistoryAddress: String): Unit = {
    amClient = AMRMClient.createAMRMClient[ContainerRequest]()
    amClient.init(conf)
    amClient.start()
    this.uiHistoryAddress = uiHistoryAddress

//    val trackingUrl = uiAddress.getOrElse(uiHistoryAddress)
    val trackingUrl = uiHistoryAddress
    info("Registering the ApplicationMaster")

    synchronized {
      amClient.registerApplicationMaster(driverHost,driverPort,trackingUrl)
      registered = true
    }
  }

}
