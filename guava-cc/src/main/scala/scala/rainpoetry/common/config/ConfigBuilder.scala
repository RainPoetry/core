package scala.rainpoetry.common.config

import java.util.concurrent.TimeUnit

case class ConfigBuilder(key: String) {

  import ConfigHelpers._

  var _doc = ""
  var _onCreate: Option[ConfigEntry[_] => Unit] = None

  def doc(s: String): ConfigBuilder = {
    _doc = s
    this
  }

  def onCreate(callback: ConfigEntry[_] => Unit): ConfigBuilder = {
    _onCreate = Option(callback)
    this
  }

  def intConf: TypedConfigBuilder[Int] = {
    new TypedConfigBuilder[Int](this, toNumber(_, _.toInt, key, "int"))
  }

  def longConf: TypedConfigBuilder[Long] = {
    new TypedConfigBuilder(this, toNumber(_, _.toLong, key, "long"))
  }

  def doubleConf: TypedConfigBuilder[Double] = {
    new TypedConfigBuilder(this, toNumber(_, _.toDouble, key, "double"))
  }

  def timeConf(unit: TimeUnit): TypedConfigBuilder[Long] = {
    new TypedConfigBuilder(this, timeFromString(_, unit), timeToString(_, unit))
  }

  def booleanConf: TypedConfigBuilder[Boolean] = {
    new TypedConfigBuilder(this, toBoolean(_, key))
  }

  def stringConf: TypedConfigBuilder[String] = {
    new TypedConfigBuilder(this, v => v)
  }

  def bytesConf(unit: ByteUnit): TypedConfigBuilder[Long] = {
    new TypedConfigBuilder(this, byteFromString(_, unit), byteToString(_, unit))
  }

  def timeFromString(str: String, unit: TimeUnit): Long = ConfigTools.timeStringAs(str, unit)

  def timeToString(v: Long, unit: TimeUnit): String = TimeUnit.MILLISECONDS.convert(v, unit) + "ms"

  def byteFromString(str: String, unit: ByteUnit): Long = {
    val (input, multiplier) =
      if (str.length() > 0 && str.charAt(0) == '-') {
        (str.substring(1), -1)
      } else {
        (str, 1)
      }
    multiplier * ConfigUtils.byteStringAs(input, unit)
  }

  def byteToString(v: Long, unit: ByteUnit): String = unit.convertTo(v, ByteUnit.BYTE) + "b"

}

class TypedConfigBuilder[T](
                             val parent: ConfigBuilder,
                             val converter: String => T,
                             val stringConverter: T => String) {

  import ConfigHelpers._

  def this(parent: ConfigBuilder, converter: String => T) = {
    this(parent, converter, Option(_).map(_.toString).orNull)
  }

  def createOptional: OptionalConfigEntry[T] = {
    val entry: OptionalConfigEntry[T] = new OptionalConfigEntry[T](parent.key, converter, stringConverter)
    parent._onCreate.foreach(_ (entry))
    entry
  }

  def toSequence: TypedConfigBuilder[Seq[T]] = {
    new TypedConfigBuilder[Seq[T]](parent, stringToSeq(_, converter), seqToString(_, stringConverter))
  }

  def createWithDefault(default: T): ConfigEntry[T] = {
    if (default.isInstanceOf[String]) {
      createWithDefaultString(default.asInstanceOf[String])
    } else {
      val transformedDefault: T = converter(stringConverter(default))
      val entry: ConfigEntryWithDefault[T] = new ConfigEntryWithDefault[T](parent.key, transformedDefault, converter, stringConverter)
      parent._onCreate.foreach(_ (entry))
      entry
    }
  }

  def createWithDefaultString(default: String): ConfigEntry[T] = {
    val entry = new ConfigEntryWithDefaultString[T](parent.key, default, converter, stringConverter)
    parent._onCreate.foreach(_ (entry))
    entry
  }
}

private object ConfigHelpers {

  def toNumber[T](s: String, converter: String => T, key: String, configType: String): T = {
    try {
      converter(s)
    } catch {
      case _: NumberFormatException =>
        throw new IllegalArgumentException(s"$key should be $configType, but was $s")
    }
  }

  def toBoolean(s: String, key: String): Boolean = {
    try {
      s.toBoolean
    } catch {
      case _: IllegalArgumentException =>
        throw new IllegalArgumentException(s"$key should be boolean, but was $s")
    }
  }

  def stringToSeq[T](str: String, converter: String => T): Seq[T] = {
    str.split(",").map(_.trim()).filter(_.nonEmpty).map(converter)
  }

  def seqToString[T](v: Seq[T], stringConverter: T => String): String = {
    v.map(stringConverter).mkString(",")
  }

  //	def timeFromString(str: String): Long = {
  //		ParseUtils.parseTime(str)
  //	}

  def timeToString(v: Long): String = v + "ms"

}
