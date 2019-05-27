package rainpoetry.hadoop.yarn

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, DataOutputStream}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.security.Credentials
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.records.ContainerId
import org.apache.hadoop.yarn.util.ConverterUtils
import rainpoetry.hadoop.HadoopConf

import scala.collection.mutable

/*
 * User: chenchong
 * Date: 2019/4/22
 * description:
 */

object YarnUtils {

  def addPathToEnvironment(env: mutable.HashMap[String, String], key: String, value: String): Unit = {
    val newValue =
      if (env.contains(key)) {
        env(key) + ApplicationConstants.CLASS_PATH_SEPARATOR + value
      } else {
        value
      }
    env.put(key, newValue)
  }

  def serialize(creds: Credentials): Array[Byte] = {
    val byteStream = new ByteArrayOutputStream
    val dataStream = new DataOutputStream(byteStream)
    creds.writeTokenStorageToStream(dataStream)
    byteStream.toByteArray
  }

  def deserialize(tokenBytes: Array[Byte]): Credentials = {
    val tokensBuf = new ByteArrayInputStream(tokenBytes)

    val creds = new Credentials()
    creds.readTokenStorageStream(new DataInputStream(tokensBuf))
    creds
  }

  def newConfiguration(conf: HadoopConf): Configuration = {
    conf.getOption("rp.hadoop.fs.defaultFS")
      .orElse(throw new IllegalArgumentException("rp.hadoop.fs.defaultFS is null, " +
        "you need to config it, like hdfs://host:port(default:8020)"))
    val config = new Configuration
    for ((key, value) <- conf.getAll if key.startsWith("rp.hadoop.")) {
      config.set(key.substring("rp.hadoop.".length), value)
    }
    config
  }

  def getContainerId: ContainerId = {
    val containerIdString = System.getenv(ApplicationConstants.Environment.CONTAINER_ID.name())
    ConverterUtils.toContainerId(containerIdString)
  }
}
