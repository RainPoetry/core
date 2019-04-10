package protocol

/*
 * User: chenchong
 * Date: 2019/4/3
 * description:
 */
package object client {

  // client 端的请求

  case class RequireExecutor()

  case class Execute(result: String)

}