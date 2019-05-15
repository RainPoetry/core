package rainpoetry.app.analyst.server

import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}
import java.util.{Arrays => JArrays}

import com.google.common.eventbus.EventBus
import com.rainpoetry.common.utils.{Strings, TimeUnit}
import rainpoetry.app.analyst.config._
import rainpoetry.app.analyst.core.AnalystConf
import rainpoetry.app.analyst.server.delay.DelayCloseSession
import rainpoetry.kafka.timewheel.delay.DelayedOperationPurgatory
import rainpoetry.livy.repl.bean.Statement
import rainpoetry.livy.utils.Logging

import scala.collection.mutable

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

class SessionManager(executorId: Int,
                     conf: AnalystConf) extends Logging {

  import SessionManager._

  val date: AtomicReference[String] = new AtomicReference[String](null)
  val counts: AtomicInteger = new AtomicInteger(0)

  val sessionTimeout = conf.getTimeAsMS(SESSION_TIMEOUT)

  val inflightSessions = new mutable.HashMap[String, RpcSession]()
  val idleSessions = new mutable.HashMap[String, RpcSession]()

  val purgatory = new DelayedOperationPurgatory[DelayCloseSession]("close-session")

  val eventBus = new EventBus(s"session_${executorId}")

  val spark = SparkBuilder.create()

  val busMember = {
    conf.get(SESSION_PERSIST).split(",").map {
      name =>
        val instance = Class.forName(name).getConstructor().newInstance()
        eventBus.register(instance)
        instance
    }.toList
  }

  def allocate(name: String): String = {
    val now = TimeUnit.nowDate()
    if (date.get() != now) {
      this.synchronized {
        if (date.get() != now) {
          date.set(now)
          counts.set(0)
        }
      }
    }
    var appName = name
    if (appName == null) {
      appName = default_name
    }
    val newSpark = spark.newSession()
    SparkBuilder.set(newSpark, "spark.app.name", appName)
    val sessionId = generateSessionId
    synchronized {
      val session = idleSessions.get(sessionId) match {
        case Some(session) =>
          session
        case None =>
          val rpcSession = new RpcSession(newSpark, executorId, sessionId, this, sessionTimeout)
          idleSessions(sessionId) =rpcSession
          rpcSession
      }
      purgatory.tryCompleteElseWatch(session.delayedOperation, JArrays.asList(sessionId))
    }
    sessionId
  }

  def inflightToIdle(sessionId: String): Unit = {
    synchronized {
      inflightSessions.remove(sessionId) match {
        case Some(session) =>
          purgatory.tryCompleteElseWatch(session.delayedOperation, JArrays.asList(sessionId))
          debug(s"put session to purgatory: ${sessionId}")
          idleSessions(sessionId) = session
        case None =>
          throw new IllegalStateException(s"{inflightToIdle} sessionId is unknown : ${sessionId}")
      }
    }
  }

  def idleToInflight(sessionId: String): Unit = {
    idleSessions.remove(sessionId) match {
      case Some(session) =>
        inflightSessions(sessionId) = session
      case None =>
        throw new IllegalStateException(s"{startExecute} sessionId is unknown : ${sessionId}")
    }
   }

  def releaseIdleSession(sessionId: String) = {
    idleSessions.remove(sessionId) match {
      case Some(session) =>
        eventBus.post(session)
        info(s"release the idle session: ${sessionId}")
      case None =>
        warn(s"session is unknown when we prepare to close it : ${sessionId}")
    }
  }

  def generateSessionId(): String = {
    date.get() + "_" + Strings.lpad(counts.getAndIncrement().toString, 6, "0")
  }

  override def toString: String = {
    s"""
       |Running Session: ${inflightSessions.size}
       |Idle Session: ${idleSessions}
      """.stripMargin
  }

  def close(): Unit = {
    busMember.foreach(eventBus.unregister(_))
  }

  def executeDetail(sessionId: String, statementId: Int): Option[Statement] = {
    idleSessions.get(sessionId) match {
      case Some(session) =>
        session.statement(statementId)
      case None =>
        inflightSessions.get(sessionId) match {
          case Some(inflightSession) =>
            inflightSession.statement(statementId)
          case None =>
            warn(s"session is not exist :${sessionId}")
            None
        }
    }
  }
}

object SessionManager {


  val default_name = "default"
}
