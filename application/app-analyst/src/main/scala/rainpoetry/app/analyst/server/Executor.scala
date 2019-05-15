package rainpoetry.app.analyst.server

import rainpoetry.app.analyst.core.AnalystConf
import rainpoetry.protocal.client._
import rainpoetry.protocal.executor._
import rainpoetry.protocal.server._
import rainpoetry.spark.rpc._

import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

class Executor(override val rpcEnv: RpcEnv,
               executorId: Int,
               conf: AnalystConf) extends ThreadSafeRpcEndpoint with Logging {

  import Executor._

  val ENDPOINT_NAME = prefix + executorId

  val sessionManager = new SessionManager(executorId,conf)

  var master:RpcEndpointRef = _

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case RegisterSession(name) =>
      context.reply {
        ResponseSession(sessionManager.allocate(name))
      }

    case Execute(code, codeType, sessionId, isSync) =>
      errorDeal(context) {
        sessionManager.idleSessions.get(sessionId) match {
          case Some(session) =>
            // session 从定时器中移除
            // session 从 IdleSessions -> inflightSessions
            session.changeStatusToRun()
            session.waitComplete()
            val statementId = isSync match {
              case true => session.executeSync(code, codeType)
              case false => session.execute(code, codeType)
            }
            context.reply(ResultIndex(statementId))
            session.changeStatusToIdle()
          case None =>
            context.reply(ResultIndex(RpcSession.NOTEXIST))
        }
      }

    case ExecuteDetail(statementId: Int, sessionId: String) =>
      errorDeal(context) {
         sessionManager.executeDetail(sessionId,statementId) match {
           case Some(statement) =>
             context.reply(Result(statement))
           case None =>
             context.reply(Result(null))
         }
      }
  }

  def errorDeal(context: RpcCallContext)(f: => Unit): Unit = {
    try {
      f
    } catch {
      case e: Throwable =>
        error(e.getMessage)
        context.reply(ResultIndex(RpcSession.ERROR, e.getMessage))
    }
  }

  override def onStart(): Unit = {
    master = rpcEnv.setupEndpointRef(rpcEnv.address,Master.ENDPOINT_NAME)
    master.send(RegisterWorker(this))
  }

  def start() {
    rpcEnv.setupEndpoint(ENDPOINT_NAME,this)
  }

  override def onStop(): Unit = {
    sessionManager.close()
    master.send(UnRegisterWorker(this))
  }

  def connects = sessionManager.inflightSessions.size

  def compare(e: Executor): Int = {
    this.connects - e.connects
  }

}

object Executor {
  val prefix = "executor-";

}

case class ExecutorInfo(name: String,
                        address: RpcAddress)
