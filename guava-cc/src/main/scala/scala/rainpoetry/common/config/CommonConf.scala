package scala.rainpoetry.common.config

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import scala.collection.JavaConverters._
import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

class CommonConf extends Logging {

  // 当 key 的前缀为 prefix, 会先从 setting 中取，然后再读取其默认值
  // 否则，只会取默认值
  val prefix: String = null

  // config setting
  protected val settings = new ConcurrentHashMap[String, String]()

  @transient private lazy val reader: ConfigReader = {
    val _reader = new ConfigReader(new CommonConfigProvider(settings, prefix))
    _reader.bindEnv(new ConfigProvider {
      override def get(key: String): Option[String] = Option(getenv(key))
    })
    _reader
  }

  def set(key: String, value: String): CommonConf = {
    if (key == null) {
      throw new NullPointerException("null key")
    }
    if (value == null) {
      throw new NullPointerException("null value for " + key)
    }
    settings.put(key, value)
    this
  }

  def set[T](entry: ConfigEntry[T], value: T): CommonConf = {
    set(entry.key, entry.stringConverter(value))
    this
  }

  def set[T](entry: OptionalConfigEntry[T], value: T): CommonConf = {
    set(entry.key, entry._stringConverter(value))
    this
  }

  def setIfMissing(key: String, value: String): CommonConf = {
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

  def getTimeAsSeconds(key: String, default: String): Long = {
    ConfigTools.timeStringAs(get(key, default), TimeUnit.SECONDS)
  }

  def getTimeAsSeconds(entry: ConfigEntry[Long]): Long = {
    ConfigTools.timeStringAs(entry.stringConverter.apply(get(entry)), TimeUnit.SECONDS)
  }

  def getTimeAsMS(key: String): Long = {
    ConfigTools.timeStringAs(get(key), TimeUnit.MILLISECONDS)
  }

  def getTimeAsMS(key: String, default: String): Long = {
    ConfigTools.timeStringAs(get(key, default), TimeUnit.MILLISECONDS)
  }

  def getTimeAsMS(entry: ConfigEntry[Long]): Long = {
    ConfigTools.timeStringAs(entry.stringConverter.apply(get(entry)), TimeUnit.MILLISECONDS)
  }

  def getAll: Array[(String, String)] = {
    settings.entrySet().asScala.map(x => (x.getKey, x.getValue)).toArray
  }

  private[config] def getenv(name: String): String = System.getenv(name)

  override def clone: CommonConf = {
    import scala.collection.JavaConverters._
    val cloned = new CommonConf
    settings.entrySet().asScala.foreach { e =>
      cloned.set(e.getKey(), e.getValue())
    }
    cloned
  }
}

private class CommonConfigProvider(conf: java.util.Map[String, String], prefix: String) extends ConfigProvider {

  import ConfigEntry._

  override def get(key: String): Option[String] = {
    if (prefix == null) {
      Option(conf.get(key)).orElse(defaultValueString(key))
    } else if (prefix != null && key.startsWith(prefix)) {
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
