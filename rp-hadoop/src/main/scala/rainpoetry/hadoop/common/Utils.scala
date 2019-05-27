package rainpoetry.hadoop.common

import java.io.File
import java.net._
import java.util.concurrent.{LinkedBlockingQueue, ThreadFactory, ThreadPoolExecutor, TimeUnit}

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.apache.commons.lang3.SystemUtils

import scala.collection.JavaConverters._
import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/22
 * description:
 */

object Utils extends Logging{

  val isWindows = SystemUtils.IS_OS_WINDOWS

  def resolveURI(path: String): URI = {
    try {
      val uri = new URI(path)
      if (uri.getScheme != null) {
        return uri;
      }
    } catch {
      case e:URISyntaxException =>
    }
    new File(path).getAbsoluteFile.toURI
  }

  def logUncaughtExceptions[T](f: => T): T = {
    try {
      f
    } catch {
      case t: Throwable =>
        error(s"Uncaught exception in thread ${Thread.currentThread().getName}", t)
        throw t
    }
  }

  private lazy val localIpAddress: InetAddress = findLocalInetAddress()

  /**
    *   127.xxx.xxx.xxx 属于"loopback" 地址，即只能你自己的本机可见，就是本机地址，比较常见的有127.0.0.1；
    *   192.168.xxx.xxx 属于private 私有地址(site local address)，属于本地组织内部访问，只能在本地局域网可见。同样10.xxx.xxx.xxx、从172.16.xxx.xxx 到 172.31.xxx.xxx都是私有地址，也是属于组织内部访问；
    *   169.254.xxx.xxx 属于连接本地地址（link local IP），在单独网段可用
    *  从224.xxx.xxx.xxx 到 239.xxx.xxx.xxx 属于组播地址
    *  比较特殊的255.255.255.255 属于广播地址
    *  除此之外的地址就是点对点的可用的公开IPv4地址
    * @return
    */
  private def findLocalInetAddress(): InetAddress = {
      val address = InetAddress.getLocalHost
      if (address.isLoopbackAddress) {
        // Address resolves to something like 127.0.1.1, which happens on Debian; try to find
        // a better address using the local network interfaces
        // getNetworkInterfaces returns ifs in reverse order compared to ifconfig output order
        // on unix-like system. On windows, it returns in index order.
        // It's more proper to pick ip address following system output order.
        val activeNetworkIFs = NetworkInterface.getNetworkInterfaces.asScala.toSeq
        val reOrderedNetworkIFs = if (isWindows) activeNetworkIFs else activeNetworkIFs.reverse
        for (ni <- reOrderedNetworkIFs) {
          val addresses = ni.getInetAddresses.asScala
            .filterNot(addr => addr.isLinkLocalAddress || addr.isLoopbackAddress).toSeq
          if (addresses.nonEmpty) {
            val addr = addresses.find(_.isInstanceOf[Inet4Address]).getOrElse(addresses.head)
            // because of Inet6Address.toHostName may add interface at the end if it knows about it
            val strippedAddress = InetAddress.getByAddress(addr.getAddress)
            // We've found an address that looks reasonable!
            warn("Your hostname, " + InetAddress.getLocalHost.getHostName + " resolves to" +
              " a loopback address: " + address.getHostAddress + "; using " +
              strippedAddress.getHostAddress + " instead (on interface " + ni.getName + ")")
            warn("Set SPARK_LOCAL_IP if you need to bind to another address")
            return strippedAddress
          }
        }
        warn("Your hostname, " + InetAddress.getLocalHost.getHostName + " resolves to" +
          " a loopback address: " + address.getHostAddress + ", but we couldn't find any" +
          " external IP address!")
        warn("Set SPARK_LOCAL_IP if you need to bind to another address")
      }
      address
  }

  def localHostName(): String = {
    localIpAddress.getHostAddress
  }

  def newDaemonCachedThreadPool(
                                 prefix: String, maxThreadNumber: Int, keepAliveSeconds: Int = 60): ThreadPoolExecutor = {
    val threadFactory = namedThreadFactory(prefix)
    val threadPool = new ThreadPoolExecutor(
      maxThreadNumber, // corePoolSize: the max number of threads to create before queuing the tasks
      maxThreadNumber, // maximumPoolSize: because we use LinkedBlockingDeque, this one is not used
      keepAliveSeconds,
      TimeUnit.SECONDS,
      new LinkedBlockingQueue[Runnable],
      threadFactory)
    threadPool.allowCoreThreadTimeOut(true)
    threadPool
  }

  def namedThreadFactory(prefix: String): ThreadFactory = {
    new ThreadFactoryBuilder().setDaemon(true).setNameFormat(prefix + "-%d").build()
  }


  def main(args: Array[String]): Unit = {
  println(InetAddress.getLocalHost().getHostAddress())
    val info = "d:/sasa"
    println(localIpAddress.getHostAddress)
  }
}
