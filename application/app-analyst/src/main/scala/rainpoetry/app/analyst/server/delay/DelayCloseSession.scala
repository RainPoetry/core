package rainpoetry.app.analyst.server.delay

import java.util.concurrent.atomic.AtomicBoolean

import rainpoetry.app.analyst.server.RpcSession
import rainpoetry.kafka.timewheel.delay.DelayedOperation

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 */

class DelayCloseSession(delayMs: Long,
                        rpcSession: RpcSession) extends DelayedOperation(delayMs) {

  // 手动控制任务是否执行完毕
  val completable = new AtomicBoolean(false)

  override def onExpiration(): Unit = rpcSession.onExpiry()

  override def onComplete(): Unit = {
    if (completable.get()) {
      rpcSession.onComplete()
    }
  }

  override def tryComplete(): Boolean = {
    if (completable.get()) {
      forceComplete()
    } else {
      false
    }
  }

  def reset(): Unit = {
    completable.set(false)
    completed.set(false)
  }

}
