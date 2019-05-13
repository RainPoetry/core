package scala.rainpoetry.common.config

import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

import com.google.common.collect.ImmutableMap

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

object ConfigTools {

  def timeStringAs(str: String, unit: TimeUnit): Long = {
    val lower = str.toLowerCase(Locale.ROOT).trim
    try {
      val m = Pattern.compile("(-?[0-9]+)([a-z]+)?").matcher(lower)
      if (!m.matches) throw new NumberFormatException("Failed to parse time string: " + str)
      val info = java.lang.Long.parseLong(m.group(1))
      val suffix = m.group(2)
      // Check for invalid suffixes
      if (suffix != null && !timeSuffixes.containsKey(suffix)) throw new NumberFormatException("Invalid suffix: \"" + suffix + "\"")
      // If suffix is valid use that, otherwise none was provided and use the default passed
      unit.convert(info, if (suffix != null) timeSuffixes.get(suffix)
      else unit)
    } catch {
      case e: NumberFormatException =>
        val timeError = "Time must be specified as seconds (s), " + "milliseconds (ms), microseconds (us), minutes (m or min), hour (h), or day (d). " + "E.g. 50s, 100ms, or 250us."
        throw new NumberFormatException(timeError + "\n" + e.getMessage)
    }
  }

  private val timeSuffixes = ImmutableMap.builder[String, TimeUnit]
    .put("us", TimeUnit.MICROSECONDS)
    .put("ms", TimeUnit.MILLISECONDS)
    .put("s", TimeUnit.SECONDS)
    .put("m", TimeUnit.MINUTES)
    .put("min", TimeUnit.MINUTES)
    .put("h", TimeUnit.HOURS)
    .put("d", TimeUnit.DAYS).build



}
