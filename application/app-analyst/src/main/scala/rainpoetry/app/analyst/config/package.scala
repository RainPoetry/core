package rainpoetry.app.analyst

import java.util.concurrent.TimeUnit

import scala.rainpoetry.common.config.ConfigBuilder

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

package object config {

  val ANALYST_PARALLELISM = ConfigBuilder("analyst.parallelism")
    .intConf
    .createWithDefault(1)

  val SESSION_TIMEOUT = ConfigBuilder("analyst.session.timeout")
    .timeConf(TimeUnit.MILLISECONDS)
    .createWithDefaultString("60000ms")

  val SESSION_PERSIST = ConfigBuilder("analyst.session.persist")
    .stringConf
    .createWithDefaultString("rainpoetry.app.analyst.busEvent.session.FilePersist")

}
