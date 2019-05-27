package rainpoetry.hadoop.yarn.am

import java.nio.ByteBuffer
import java.util

import org.apache.hadoop.yarn.api.records.{ContainerId, ContainerStatus, Resource}
import org.apache.hadoop.yarn.client.api.async.NMClientAsync

/*
 * User: chenchong
 * Date: 2019/4/23
 * description:
 */

class NMCallbackHandler extends NMClientAsync.AbstractCallbackHandler{

  override def onGetContainerStatusError(containerId: ContainerId, throwable: Throwable): Unit = {}

  override def onContainerStatusReceived(containerId: ContainerId, containerStatus: ContainerStatus): Unit = {}

  override def onContainerResourceIncreased(containerId: ContainerId, resource: Resource): Unit = {}

  override def onStopContainerError(containerId: ContainerId, throwable: Throwable): Unit = {}

  override def onContainerStopped(containerId: ContainerId): Unit = {}

  override def onIncreaseContainerResourceError(containerId: ContainerId, throwable: Throwable): Unit = {}

  override def onStartContainerError(containerId: ContainerId, throwable: Throwable): Unit = {}

  override def onUpdateContainerResourceError(containerId: ContainerId, throwable: Throwable): Unit = {}

  override def onContainerStarted(containerId: ContainerId, map: util.Map[String, ByteBuffer]): Unit = {}

  override def onContainerResourceUpdated(containerId: ContainerId, resource: Resource): Unit = {}
}
