package rainpoetry.hadoop

import scala.rainpoetry.common.config.{ByteUnit, ConfigBuilder}

/*
 * User: chenchong
 * Date: 2019/4/19
 * description:
 */

package object config {

  val AM_MEMORY = ConfigBuilder("rp.yarn.am.memory")
    .bytesConf(ByteUnit.MiB)
    .createWithDefaultString("512m")

  val AM_CORES = ConfigBuilder("rp.yarn.am.cores")
    .intConf
    .createWithDefault(1)


  val EXECUTOR_CORES = ConfigBuilder("rp.yarn.executor.cores")
    .intConf
    .createWithDefault(1)

  val EXECUTOR_MEMORY = ConfigBuilder("rp.yarn.executor.memory")
    .doc("Amount of memory to use per executor process, in MiB unless otherwise specified.")
    .bytesConf(ByteUnit.MiB)
    .createWithDefaultString("1g")

  val STAGING_DIR = ConfigBuilder("rp.yarn.stagingDir")
    .stringConf
    .createOptional

  val STAGING_FILE_REPLICATION = ConfigBuilder("rp.yarn.submit.file.replication")
    .doc("Replication factor for files uploaded by Spark to HDFS.")
    .intConf
    .createOptional

  val YARN_JARS = ConfigBuilder("rp.yarn.jars")
    .doc("Location of jars containing Spark classes.")
    .stringConf
    .toSequence
    .createOptional

  val MAIN_CLASS =  ConfigBuilder("rp.yarn.mainClass")
    .stringConf
    .createOptional

  val APP_NAME =  ConfigBuilder("rp.yarn.app.name")
    .stringConf
    .createOptional

  val QUEUE_NAME = ConfigBuilder("rp.yarn.queue")
    .stringConf
    .createWithDefault("default")

  val CONTAINER_LAUNCH_MAX_THREADS =
    ConfigBuilder("rp.yarn.containerLauncherMaxThreads")
      .intConf
      .createWithDefault(25)
}
