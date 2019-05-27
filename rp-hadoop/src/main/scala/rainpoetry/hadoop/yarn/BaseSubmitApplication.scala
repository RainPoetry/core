package rainpoetry.hadoop.yarn

import java.io.File
import java.net.URI
import java.nio.ByteBuffer

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.permission.FsPermission
import org.apache.hadoop.fs.{FileStatus, FileSystem, FileUtil, Path}
import org.apache.hadoop.mapreduce.MRJobConfig
import org.apache.hadoop.security.UserGroupInformation
import org.apache.hadoop.util.StringUtils
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse
import org.apache.hadoop.yarn.api.records._
import org.apache.hadoop.yarn.client.api.{YarnClient, YarnClientApplication}
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.{ConverterUtils, Records}
import rainpoetry.hadoop.HadoopConf
import rainpoetry.hadoop.common.Utils
import rainpoetry.hadoop.config.{STAGING_DIR, STAGING_FILE_REPLICATION, YARN_JARS, _}
import rainpoetry.hadoop.yarn.am.ApplicationMaster

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.{HashMap, HashSet, ListBuffer, Map}
import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/23
 * description:
 */

class BaseSubmitApplication(cfg: HadoopConf) extends Logging {

  import YarnSession._

  cfg.getOption("user").map(System.setProperty("HADOOP_USER_NAME", _))

  val yarnClient = YarnClient.createYarnClient()

  val user = UserGroupInformation.getCurrentUser.getUserName

  val amClass = cfg.get(MAIN_CLASS).getOrElse(throw new IllegalArgumentException(s"${MAIN_CLASS.key} is unknown"))

  val conf = YarnUtils.newConfiguration(cfg)

  private var appMaster: ApplicationMaster = _

  val amMemory = cfg.get(AM_MEMORY).toInt
  val amCores = cfg.get(AM_CORES)

  def submitApplication(): ApplicationId = {

    var appId: ApplicationId = null

    // Init YarnClient then start
    yarnClient.init(conf)
    yarnClient.start()

    // GET a new Application from RM
    val newApp = yarnClient.createApplication()
    val newAppResponse = newApp.getNewApplicationResponse
    appId = newAppResponse.getApplicationId

    // prepare stage dir
    val stagingBaseDir = cfg.get(STAGING_DIR)
      .map(new Path(_, UserGroupInformation.getCurrentUser.getShortUserName))
      .getOrElse(FileSystem.get(conf).getHomeDirectory)

    val stagingDir = new Path(stagingBaseDir, buildPath(YARN_STAGING, appId.toString))

    println(s"stagingBaseDir: ${stagingBaseDir}")
    // set up the appropriate context to launch the AM

    val containerContext = createContainerLaunchContext(newAppResponse, stagingDir)
    val appContext = createApplicationSubmissionContext(newApp, containerContext)

    info(s"Submitting application $appId to ResourceManager")
    yarnClient.submitApplication(appContext)

    appId
  }

  def createApplicationSubmissionContext(newApp: YarnClientApplication,
                                         containerContext: ContainerLaunchContext): ApplicationSubmissionContext = {
    val appContext = newApp.getApplicationSubmissionContext
    appContext.setApplicationName(cfg.get(APP_NAME).getOrElse(s"${user}_default"))
    appContext.setQueue(cfg.get(QUEUE_NAME))
    appContext.setAMContainerSpec(containerContext)
    appContext.setApplicationType(user)

    val capability = Records.newRecord(classOf[Resource])
    capability.setMemorySize(amMemory)
    capability.setVirtualCores(amCores)
    appContext.setResource(capability)

    appContext
  }

  def createContainerLaunchContext(appResponse: GetNewApplicationResponse,
                                   stagingPath: Path)
  : ContainerLaunchContext = {
    info("Setting up Container launch context for the AM")
    val appId = appResponse.getApplicationId

    // 配置运行环境
    val launchEnv = setupLaunchEnv(stagingPath)
    // 配置资源文件
    val localResources = prepareLocalResources(stagingPath)

    val amContainer = Records.newRecord(classOf[ContainerLaunchContext])
    amContainer.setLocalResources(localResources.asJava)
    amContainer.setEnvironment(launchEnv.asJava)

    val javaOpts = ListBuffer[String]()
    // Add Xmx for AM memory
    javaOpts += "-Xmx" + amMemory + "m"

    val tmpDir = new Path(Environment.PWD.$$(), YarnConfiguration.DEFAULT_CONTAINER_TEMP_DIR)
    javaOpts += "-Djava.io.tmpdir=" + tmpDir

    val amArgs =
      Seq(amClass)

    val commands =
      Seq(Environment.JAVA_HOME.$$() + "/bin/java", "-server") ++
        javaOpts ++ amArgs ++
        Seq(
          "1>", ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout",
          "2>", ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr")

    val printableCommands = commands.map(s => if (s == null) "null" else s).toList
    amContainer.setCommands(printableCommands.asJava)
    val currentUser = UserGroupInformation.getCurrentUser()
    val credentials = currentUser.getCredentials()
    val serializedCreds = YarnUtils.serialize(credentials)
    amContainer.setTokens(ByteBuffer.wrap(serializedCreds))
    amContainer
  }


  def prepareLocalResources(destDir: Path): mutable.HashMap[String, LocalResource] = {
    info("Preparing resources for our AM container")
    // 用于上传 jar 包到 HDFS
    val fs = destDir.getFileSystem(conf)
    val distributeUris = new mutable.HashSet[String]()
    // 副本
    val replication = cfg.get(STAGING_FILE_REPLICATION).map(_.toShort)
      .getOrElse(fs.getDefaultReplication(destDir))

    val localResources = HashMap[String, LocalResource]()

    // 创建 HDFS 上传目录
    FileSystem.mkdirs(fs, destDir, new FsPermission(STAGING_DIR_PERMISSION))

    val distributedUris = new HashSet[String]
    val distributedNames = new HashSet[String]

    val statCache: Map[URI, FileStatus] = HashMap[URI, FileStatus]()

    def addDistributedUri(uri: URI): Boolean = {
      val uriStr = uri.toString()
      val fileName = new File(uri.getPath).getName
      if (distributedUris.contains(uriStr)) {
        warn(s"Same path resource $uri added multiple times to distributed cache.")
        false
      } else if (distributedNames.contains(fileName)) {
        warn(s"Same name resource $uri added multiple times to distributed cache")
        false
      } else {
        distributedUris += uriStr
        distributedNames += fileName
        true
      }
    }

    def distribute(
                    hadoopConf: Configuration,
                    path: String,
                    resType: LocalResourceType = LocalResourceType.FILE,
                    destName: Option[String] = None,
                    targetDir: Option[String] = None): (Boolean, String) = {
      val trimmedPath = path.trim()
      val localURI = Utils.resolveURI(trimmedPath)
      if (addDistributedUri(localURI)) {
        val localPath = getQualifiedLocalPath(localURI, hadoopConf)
        val linkName = targetDir.map(_ + "/").getOrElse("") +
          destName.orElse(Option(localURI.getFragment())).getOrElse(localPath.getName())
        // 文件上传到 HDFS 中
        val destPath = copyFileToRemote(destDir, localPath, replication)
        val destFs = FileSystem.get(destPath.toUri(), hadoopConf)
        addSource(destFs, destPath, localResources, resType, linkName, statCache)
        (false, linkName)
      } else {
        (false, null)
      }
    }

    // jars 配置
    cfg.get(YARN_JARS) match {
      case Some(jars) =>
        jars.foreach {
          jar =>
            val path = getQualifiedLocalPath(Utils.resolveURI(jar), conf)
            val pathFs = FileSystem.get(path.toUri(), conf)
            pathFs.globStatus(path).filter(_.isFile()).foreach { entry =>
              val uri = entry.getPath().toUri()
              statCache.update(uri, entry)
              distribute(conf, uri.toString(), targetDir = Some(LOCALIZED_LIB_DIR))
            }
        }
      case None =>
        throw new IllegalArgumentException(" set (rp.yarn.jars) is unknown")
      // 在 Spark 中，会将 assembly 项目中的 jar 包压缩到一个 zip 文件中然后上传到 HDFS 上
    }

    localResources
  }

  def addSource(fs: FileSystem,
                destPath: Path,
                localResources: HashMap[String, LocalResource],
                resourceType: LocalResourceType,
                link: String,
                statCache: Map[URI, FileStatus]): Unit = {
    val destStatus = statCache.getOrElse(destPath.toUri(), fs.getFileStatus(destPath))
    val amJarRsrc = Records.newRecord(classOf[LocalResource])
    amJarRsrc.setType(resourceType)
    amJarRsrc.setVisibility(LocalResourceVisibility.PUBLIC)
    amJarRsrc.setResource(ConverterUtils.getYarnUrlFromPath(destPath))
    amJarRsrc.setTimestamp(destStatus.getModificationTime())
    amJarRsrc.setSize(destStatus.getLen())
    require(link != null && link.nonEmpty, "You must specify a valid link name.")
    localResources(link) = amJarRsrc
  }

  def copyFileToRemote(
                        destDir: Path,
                        srcPath: Path,
                        replication: Short,
                        destName: Option[String] = None): Path = {
    val destFs = destDir.getFileSystem(conf)
    val srcFs = srcPath.getFileSystem(conf)
    val destPath = new Path(destDir, destName.getOrElse(srcPath.getName()))
    info(s"Uploading resource $srcPath -> $destPath")
    FileUtil.copy(srcFs, srcPath, destFs, destPath, false, conf)
    destFs.setReplication(destPath, replication)
    destFs.setPermission(destPath, new FsPermission(APP_FILE_PERMISSION))
    destPath
  }

  def getQualifiedLocalPath(localURI: URI, hadoopConf: Configuration): Path = {
    val qualifiedURI =
      if (localURI.getScheme == null) {
        // If not specified, assume this is in the local filesystem to keep the behavior
        // consistent with that of Hadoop
        new URI(FileSystem.getLocal(hadoopConf).makeQualified(new Path(localURI)).toString)
      } else {
        localURI
      }
    new Path(qualifiedURI)
  }

  // 搭建运行环境
  def setupLaunchEnv(stagingDirPath: Path): mutable.HashMap[String, String] = {
    val env = new mutable.HashMap[String, String]

    // set ClassPath
    addClassPath(env)

    env("SPARK_YARN_STAGING_DIR") = stagingDirPath.toString
    env("SPARK_USER") = UserGroupInformation.getCurrentUser().getShortUserName()

    env
  }

  def addClassPath(env: mutable.HashMap[String, String]): mutable.HashMap[String, String] = {
    // 将 PWD 加入到 ClassPath 下
    addClassPathEntry(Environment.PWD.$$(), env)
    // 将 jar 包添加到 ClassPath 下
    addClassPathEntry(buildPath(Environment.PWD.$$(), LOCALIZED_LIB_DIR, "*"), env)
    // 添加 Hadoop 环境 jar
    populateHadoopClasspath(env)
    env
  }

  private def populateHadoopClasspath(env: mutable.HashMap[String, String])
  : Unit = {
    val classPathElementsToAdd = getYarnAppClasspath() ++ getMRAppClasspath()
    classPathElementsToAdd.foreach { c =>
      YarnUtils.addPathToEnvironment(env, Environment.CLASSPATH.name, c.trim)
    }
  }

  private def getYarnAppClasspath(): Seq[String] =
    Option(conf.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH)) match {
      case Some(s) => s.toSeq
      case None => getDefaultYarnApplicationClasspath
    }

  private def getMRAppClasspath(): Seq[String] =
    Option(conf.getStrings("mapreduce.application.classpath")) match {
      case Some(s) => s.toSeq
      case None => getDefaultMRApplicationClasspath
    }

  private def getDefaultYarnApplicationClasspath: Seq[String] =
    YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH.toSeq.map(changePathToLinux)

  private[yarn] def getDefaultMRApplicationClasspath: Seq[String] =
    StringUtils.getStrings(MRJobConfig.DEFAULT_MAPREDUCE_APPLICATION_CLASSPATH).toSeq.map(changePathToLinux)


  def changePathToLinux(value: String): String = {
    val pattern = "%([a-zA-Z1-9_-]*?)%".r
    pattern.replaceAllIn(value, "\\$" + _.subgroups(0))
  }

  // set ClassPath into env
  def addClassPathEntry(path: String, env: mutable.HashMap[String, String]): Unit = {
    YarnUtils.addPathToEnvironment(env, Environment.CLASSPATH.name, path)
  }

  def buildPath(components: String*): String = {
    components.mkString(Path.SEPARATOR)
  }

  def addFileToClasspath(uri: URI, env: mutable.HashMap[String, String]): Unit = {

  }

}
