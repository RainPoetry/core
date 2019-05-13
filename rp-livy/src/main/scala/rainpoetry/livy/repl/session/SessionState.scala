package rainpoetry.livy.repl.session

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 *
 *   sealed 作用：
 *     1. 不能在类定义的文件之外定义任何新的子类
 *     2. 在检查模式匹配的时候，让 Scala  知道这些 case 的情况
 */

sealed abstract class SessionState(val state: String, val isActive: Boolean) {
  override def toString: String = state
}

class FinishedSessionState(
                            override val state: String,
                            override val isActive: Boolean,
                            val time: Long
                          ) extends SessionState(state, isActive)

object SessionState {

  def apply(s: String): SessionState = s match {
    case "not_started" => NotStarted
    case "starting" => Starting
    case "recovering" => Recovering
    case "idle" => Idle
    case "running" => Running
    case "busy" => Busy
    case "shutting_down" => ShuttingDown
    case "error" => Error()
    case "dead" => Dead()
    case "killed" => Killed()
    case "success" => Success()
    case _ => throw new IllegalArgumentException(s"Illegal session state: $s")
  }

  object NotStarted extends SessionState("not_started", true)

  object Starting extends SessionState("starting", true)

  object Recovering extends SessionState("recovering", true)

  object Idle extends SessionState("idle", true)

  object Running extends SessionState("running", true)

  object Busy extends SessionState("busy", true)

  object ShuttingDown extends SessionState("shutting_down", false)

  case class Killed(override val time: Long = System.nanoTime()) extends
    FinishedSessionState("killed", false, time)

  case class Error(override val time: Long = System.nanoTime()) extends
    FinishedSessionState("error", true, time)

  case class Dead(override val time: Long = System.nanoTime()) extends
    FinishedSessionState("dead", false, time)

  case class Success(override val time: Long = System.nanoTime()) extends
    FinishedSessionState("success", false, time)
}

