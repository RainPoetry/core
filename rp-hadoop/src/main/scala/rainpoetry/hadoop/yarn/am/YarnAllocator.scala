package rainpoetry.hadoop.yarn.am

import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import org.apache.hadoop.yarn.api.records.{Container, ContainerId, LocalResource, Resource}
import org.apache.hadoop.yarn.client.api.AMRMClient
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.RackResolver
import rainpoetry.hadoop.HadoopConf
import rainpoetry.hadoop.common.Utils
import rainpoetry.hadoop.config._

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/24
 * description:
 */

class YarnAllocator(
                     amClient: AMRMClient[ContainerRequest],
                     localResources: Map[String, LocalResource],
                     cfg: HadoopConf,
                     conf: YarnConfiguration
                   ) extends Logging {

  val ANY_HOST = "*"

  private val runningExecutors = Collections.newSetFromMap[String](
    new ConcurrentHashMap[String, java.lang.Boolean]())

  private val releasedContainers = Collections.newSetFromMap[ContainerId](
    new ConcurrentHashMap[ContainerId, java.lang.Boolean])

  private val numExecutorsStarting = new AtomicInteger(0)

  protected val executorCores = cfg.get(EXECUTOR_CORES)
  protected val executorMemory = cfg.get(EXECUTOR_MEMORY).toInt

  private val launcherPool = Utils.newDaemonCachedThreadPool("ContainerLaunch", cfg.get(CONTAINER_LAUNCH_MAX_THREADS))

  private[yarn] val resource: Resource = {
    val resource = Resource.newInstance(
      executorMemory, executorCores)
    // 对 resource 资源进行配置 需要 yarn 3.0 + 的版本
    //    val infomation = ResourceInformation.newInstance("name","G",1)
    //    resource.setResourceInformation("name",infomation)
    resource
  }

  def allocate(): Unit = synchronized {
    // AM 向 RM 申请资源
    val allocateResponse = amClient.allocate(0.1f)

    val allocatedContainers = allocateResponse.getAllocatedContainers()

    if (allocatedContainers.size() > 0) {
      info(("Allocated containers: %d. Current executor count: %d. " +
        "Launching executor count: %d. Cluster resources: %s.")
        .format(
          allocatedContainers.size,
          runningExecutors.size,
          numExecutorsStarting.get,
          allocateResponse.getAvailableResources))
      handleAllocatedContainers(allocatedContainers.asScala)
    }


  }

  // Handle container granted by the RM by launching executors on them
  def handleAllocatedContainers(allocatedContainers: Seq[Container]): Unit = {
    val containersToUse = new ArrayBuffer[Container](allocatedContainers.size)

    // Match incoming requests by host
    val remainingAfterHostMatches = new ArrayBuffer[Container]
    for (allocatedContainer <- allocatedContainers) {
      matchContainerToRequest(allocatedContainer, allocatedContainer.getNodeId.getHost,
        containersToUse, remainingAfterHostMatches)
    }

    // Match remaining by rack
    val remainingAfterRackMatches = new ArrayBuffer[Container]
    for (allocatedContainer <- remainingAfterHostMatches) {
      val rack = RackResolver.resolve(conf, allocatedContainer.getNodeId.getHost).getNetworkLocation()
      matchContainerToRequest(allocatedContainer, rack, containersToUse,
        remainingAfterRackMatches)
    }

    // Assign remaining that are neither node-local nor rack-local
    val remainingAfterOffRackMatches = new ArrayBuffer[Container]
    for (allocatedContainer <- remainingAfterRackMatches) {
      matchContainerToRequest(allocatedContainer, ANY_HOST, containersToUse,
        remainingAfterOffRackMatches)
    }

    if (!remainingAfterOffRackMatches.isEmpty) {
      warn(s"Releasing ${remainingAfterOffRackMatches.size} unneeded containers that were " +
        s"allocated to us")
      for (container <- remainingAfterOffRackMatches) {
        internalReleaseContainer(container)
      }
    }

    // 运行本配置在本主机的 Container
    runAllocatedContainers(containersToUse)

    info("Received %d containers from YARN, launching executors on %d of them."
      .format(allocatedContainers.size, containersToUse.size))

  }

  def runAllocatedContainers(containers: ArrayBuffer[Container]): Unit = {
    for (container <- containers) {
      val executorHostName = container.getNodeId.getHost
      val containerId = container.getId
      val executorId = 1
      launcherPool.execute(new Runnable {
        override def run(): Unit = {

        }
      })
    }
  }

  private def internalReleaseContainer(container: Container): Unit = {
    releasedContainers.add(container.getId())
    amClient.releaseAssignedContainer(container.getId())
  }


  private def matchContainerToRequest(
                                       allocatedContainer: Container,
                                       location: String,
                                       containersToUse: ArrayBuffer[Container],
                                       remaining: ArrayBuffer[Container]
                                     ): Unit = {
    val matchingResource = Resource.newInstance(allocatedContainer.getResource.getMemorySize, resource.getVirtualCores)
    val matchingRequests = amClient.getMatchingRequests(allocatedContainer.getPriority, location,
      matchingResource)
    // Match the allocation to a request
    if (!matchingRequests.isEmpty) {
      val containerRequest = matchingRequests.get(0).iterator.next
      debug(s"Removing container request via AM client: $containerRequest")
      amClient.removeContainerRequest(containerRequest)
      containersToUse += allocatedContainer
    } else {
      remaining += allocatedContainer
    }
  }
}
