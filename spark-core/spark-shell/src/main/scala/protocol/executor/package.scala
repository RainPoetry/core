package protocol

import com.cc.sql.JobStatus


/*
 * User: chenchong
 * Date: 2019/4/4
 * description:
 */

package object executor {

  case class Reply(data: Any, duration:Long, success: Boolean)

  case class BatchReply(data: Array[JobStatus], array: Array[Long])

}
