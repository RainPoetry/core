package rainpoetry.spark.rpc.common

import java.net.BindException

import io.netty.channel.unix.Errors.NativeIoException
import rainpoetry.spark.rpc.RpcConf
import rainpoetry.spark.rpc.error.RpcException

import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

object Utils extends Logging{

  def extractHostPortFromSparkUrl(sparkUrl: String): (String, Int) = {
    try {
      val uri = new java.net.URI(sparkUrl)
      val host = uri.getHost
      val port = uri.getPort
      if (uri.getScheme != "rpc" ||
        host == null ||
        port < 0 ||
        (uri.getPath != null && !uri.getPath.isEmpty) || // uri.getPath returns "" instead of null
        uri.getFragment != null ||
        uri.getQuery != null ||
        uri.getUserInfo != null) {
        throw new RpcException("Invalid master URL: " + sparkUrl)
      }
      (host, port)
    } catch {
      case e: java.net.URISyntaxException =>
        throw new RpcException("Invalid master URL: " + sparkUrl, e)
    }
  }

  def getRpcClassLoader: ClassLoader = getClass.getClassLoader

  def getContextOrSparkClassLoader: ClassLoader =
    Option(Thread.currentThread().getContextClassLoader).getOrElse(getRpcClassLoader)

  def classForName(className: String): Class[_] = {
    Class.forName(className, true, getContextOrSparkClassLoader)
  }

  def startServiceOnPort[T](
                             startPort: Int,
                             startService: Int => (T, Int),
                             conf: RpcConf,
                             serviceName: String = ""): (T, Int) = {
    require(startPort == 0 || (1024 <= startPort && startPort < 65536),
      "startPort should be between 1024 and 65535 (inclusive), or 0 for a random free port.")
    val serviceString = if (serviceName.isEmpty) "" else s" '$serviceName'"
    val maxRetries = portMaxRetries(conf)
    for (offset <- 0 to maxRetries) {
      // Do not increment port if startPort is 0, which is treated as a special port
      val tryPort = if (startPort == 0) {
        startPort
      } else {
        userPort(startPort, offset)
      }
      try {
        val (service, port) = startService(tryPort)
        info(s"Successfully started service$serviceString on port $port.")
        return (service, port)
      } catch {
        case e: Exception  =>
          if (startPort == 0) {
            // As startPort 0 is for a random free port, it is most possibly binding address is
            // not correct.
            warn(s"Service$serviceString could not bind on a random free port. " +
              "You may check whether configuring an appropriate binding address.")
          } else {
            warn(s"Service$serviceString could not bind on port $tryPort. " +
              s"Attempting port ${tryPort + 1}.")
          }
      }
    }
    // Should never happen
    throw new RpcException(s"Failed to start service$serviceString on port $startPort")
  }

  def portMaxRetries(conf: RpcConf): Int = {
    val maxRetries = conf.getOption("spark.port.maxRetries").map(_.toInt)
    maxRetries.getOrElse(16)
  }

  def userPort(base: Int, offset: Int): Int = {
    (base + offset - 1024) % (65536 - 1024) + 1024
  }

}
