package rainpoetry.hadoop.yarn.am

import java.util

import org.apache.hadoop.yarn.api.records.{Container, ContainerStatus, NodeReport, UpdatedContainer}
import org.apache.hadoop.yarn.client.api.async.AMRMClientAsync

/*
 * User: chenchong
 * Date: 2019/4/23
 * description:
 */

class RMCallbackHandler extends AMRMClientAsync.AbstractCallbackHandler{

  override def onContainersUpdated(list: util.List[UpdatedContainer]): Unit = {}

  override def onError(throwable: Throwable): Unit = {}

  override def onShutdownRequest(): Unit = {}

  override def onContainersCompleted(list: util.List[ContainerStatus]): Unit = {}

  override def getProgress: Float = ???

  override def onNodesUpdated(list: util.List[NodeReport]): Unit = {}

  override def onContainersAllocated(list: util.List[Container]): Unit = {}
}
