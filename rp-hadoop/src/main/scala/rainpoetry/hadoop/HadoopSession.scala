package rainpoetry.hadoop

import java.util.concurrent.atomic.AtomicReference

import rainpoetry.hadoop.hdfs.HdfsSession
import rainpoetry.hadoop.yarn.YarnSession

import scala.collection.mutable

/*
 * User: chenchong
 * Date: 2019/4/19
 * description:
 */

class HadoopSession(hadoopConf: HadoopConf) {

  var conf: HadoopConf = hadoopConf.clone

  lazy val HDFS: HdfsSession = new HdfsSession(this)
  lazy val YARN: YarnSession = new YarnSession(this)


}

object HadoopSession {

  private[hadoop] val activeSession = new AtomicReference[HadoopSession]

  def builder(): Builder = new Builder

  def getActiveSession(): Option[HadoopSession] = {
    Option(activeSession.get())
  }

  def setActiveSession(session: HadoopSession): Unit = {
    activeSession.set(session)
  }

  class Builder {
    private val options = new mutable.HashMap[String, String]()

    def user(user: String): Builder = config("user", user)

    def uri(uri: String): Builder = config("rp.fs.defaultFS", uri)

    def config(key: String, value: String): Builder = synchronized {
      options += key -> value
      this
    }

    def getOrCreate(): HadoopSession = {
      var session = activeSession.get()
      if (session ne null) {
        options.foreach {
          case (k, v) => session.conf.set(k, v)
        }
        return session
      }
      HadoopSession.synchronized {
        val conf: HadoopConf = new HadoopConf
        options.foreach {
          case (k, v) => conf.set(k, v)
        }
        session = new HadoopSession(conf)
        setActiveSession(session)
      }
      session
    }
  }

}
