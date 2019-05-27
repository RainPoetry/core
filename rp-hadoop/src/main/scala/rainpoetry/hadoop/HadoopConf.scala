package rainpoetry.hadoop

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import scala.collection.JavaConverters._
import scala.rainpoetry.common.Logging
import scala.rainpoetry.common.config._

/*
 * User: chenchong
 * Date: 2019/4/19
 * description:
 */

class HadoopConf extends Logging{

  // config setting
  private val settings = new ConcurrentHashMap[String, String]()

  @transient private lazy val reader: ConfigReader = {
    val _reader = new ConfigReader(new HadoopConfigProvider(settings))
    _reader.bindEnv(new ConfigProvider {
      override def get(key: String): Option[String] = Option(getenv(key))
    })
    _reader
  }

  def set(key: String, value: String): HadoopConf = {
    if (key == null) {
      throw new NullPointerException("null key")
    }
    if (value == null) {
      throw new NullPointerException("null value for " + key)
    }
    settings.put(key, value)
    this
  }

  def set[T](entry: ConfigEntry[T], value: T): HadoopConf = {
    set(entry.key, entry.stringConverter(value))
    this
  }

  def set[T](entry: OptionalConfigEntry[T], value: T): HadoopConf = {
    set(entry.key, entry._stringConverter(value))
    this
  }

  def setIfMissing(key: String, value: String): HadoopConf = {
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

  private[hadoop] def getenv(name: String): String = System.getenv(name)

  override def clone: HadoopConf = {
    val cloned = new HadoopConf
    settings.entrySet().asScala.foreach { e =>
      cloned.set(e.getKey(), e.getValue())
    }
    cloned
  }

}


private class HadoopConfigProvider(conf: java.util.Map[String, String]) extends ConfigProvider {

  import ConfigEntry._

  override def get(key: String): Option[String] = {
    if (key.startsWith("rp.")) {
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

