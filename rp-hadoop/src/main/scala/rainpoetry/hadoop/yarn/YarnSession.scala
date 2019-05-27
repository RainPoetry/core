package rainpoetry.hadoop.yarn


import org.apache.hadoop.fs.permission.FsPermission
import rainpoetry.hadoop.HadoopSession

import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/19
 * description:
 */

class YarnSession(hadoopSession: HadoopSession) extends Logging {

    def submitApplication(): Unit = {
      val conf = hadoopSession.conf.clone
      val application = new BaseSubmitApplication(conf)
      val appId = application.submitApplication()

    }
}

private object YarnSession {


  val LOCALIZED_LIB_DIR = "_libs_"
  val YARN_STAGING: String = ".yarnStaging"


  // rwx--------
  val STAGING_DIR_PERMISSION: FsPermission =
    FsPermission.createImmutable(Integer.parseInt("700", 8).toShort)

  // rw-r--r--
  val APP_FILE_PERMISSION: FsPermission =
    FsPermission.createImmutable(Integer.parseInt("644", 8).toShort)
}

