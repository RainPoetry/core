package rainpoetry.spark.rpc.netty

import java.lang
import java.util.Map

import rainpoetry.spark.rpc.RpcConf
import rainpoetry.spark.rpc.util.{ConfigProvider, NettyUtils, TransportConf}

import scala.collection.JavaConverters._
/*
 * User: chenchong
 * Date: 2019/4/2
 * description:
 */

object RpcTransportConfig {

  def fromRpcConf(_conf: RpcConf, module: String, cores: Int = 0): TransportConf= {
    val conf = _conf.clone
    val numThreads = NettyUtils.defaultNumThreads(cores)

    conf.setIfMissing(s"rpc.$module.io.serverThreads", numThreads.toString)
    conf.setIfMissing(s"rpc.$module.io.clientThreads", numThreads.toString)

    new TransportConf(module, new ConfigProvider {
      override def get(name: String): String = conf.get(name)
      override def get(name: String, defaultValue: String): String = conf.get(name, defaultValue)
      override def getAll(): lang.Iterable[Map.Entry[String, String]] = {
        conf.getAll.toMap.asJava.entrySet()
      }
    })
  }

}
