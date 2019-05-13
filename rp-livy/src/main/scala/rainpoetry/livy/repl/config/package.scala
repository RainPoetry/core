package rainpoetry.livy.repl

import java.util.concurrent.TimeUnit

import scala.rainpoetry.common.config.ConfigBuilder

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 */

package object config {

  val RETAINED_STATEMENTS = ConfigBuilder("repl.retained.statements")
    .intConf
    .createWithDefault(100)

  val JOB_CANCEL_TIMEOUT = ConfigBuilder("repl.job.cancel.timeout")
    .timeConf(TimeUnit.SECONDS)
    .createWithDefaultString("30s")

  val JOB_CANCEL_TRIGGER_INTERVAL = ConfigBuilder("repl.job.cancel.interval")
    .timeConf(TimeUnit.MILLISECONDS)
    .createWithDefaultString("300ms")

}
