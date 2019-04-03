package rainpoetry.spark.rpc

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import scala.rainpoetry.common.Logging
import scala.rainpoetry.common.config._
import scala.collection.JavaConverters._
/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

class RpcConf extends Logging{

  // config setting
  private val settings = new ConcurrentHashMap[String, String]()

  @transient private lazy val reader: ConfigReader = {
    val _reader = new ConfigReader(new SparkConfigProvider(settings))
    _reader.bindEnv(new ConfigProvider {
      override def get(key: String): Option[String] = Option(getenv(key))
    })
    _reader
  }

  def set(key: String, value: String): RpcConf = {
    if (key == null) {
      throw new NullPointerException("null key")
    }
    if (value == null) {
      throw new NullPointerException("null value for " + key)
    }
    settings.put(key, value)
    this
  }

  def set[T](entry: ConfigEntry[T], value: T): RpcConf = {
    set(entry.key, entry.stringConverter(value))
    this
  }

  def set[T](entry: OptionalConfigEntry[T], value: T): RpcConf = {
    set(entry.key, entry._stringConverter(value))
    this
  }

  def setIfMissing(key: String, value: String): RpcConf = {
    settings.putIfAbsent(key, value)
    this
  }

  def getOption(key: String): Option[String] = {
    Option(settings.get(key))
  }

  /** Get a parameter; throws a NoSuchElementException if it's not set */
  def get(key: String): String = {
    getOption(key).getOrElse(throw new NoSuchElementException(key))
  }

  /** Get a parameter, falling back to a default if not set */
  def get(key: String, defaultValue: String): String = {
    getOption(key).getOrElse(defaultValue)
  }

  def get[T](entry: ConfigEntry[T]): T = {
    entry.readFrom(reader)
  }

  def getTimeAsSeconds(key: String): Long = {
    ConfigTools.timeStringAs(get(key), TimeUnit.SECONDS)
  }

  def getTimeAsSeconds(key: String, default: String): Long =  {
    ConfigTools.timeStringAs(get(key, default), TimeUnit.SECONDS)
  }

  def getAll: Array[(String, String)] = {
    settings.entrySet().asScala.map(x => (x.getKey, x.getValue)).toArray
  }

  private[rpc] def getenv(name: String): String = System.getenv(name)

  override def clone: RpcConf = {
    val cloned = new RpcConf

    settings.entrySet().asScala.foreach { e =>
      cloned.set(e.getKey(), e.getValue())
    }
    cloned
  }

}

private class SparkConfigProvider(conf: java.util.Map[String, String]) extends ConfigProvider {

  import ConfigEntry._

  override def get(key: String): Option[String] = {
    if (key.startsWith("rpc.")) {
      Option(conf.get(key)).orElse(defaultValueString(key))
    } else {
      None
    }
  }

  private def defaultValueString(key: String): Option[String] = {
    findEntry(key) match {
      case e: ConfigEntryWithDefault[_] => Option(e.defaultValueString)
      case e: ConfigEntryWithDefaultString[_] => Option(e.defaultValueString)
      case e: FallbackConfigEntry[_] => get(e.fallback.key)
      case _ => None
    }
  }

}
