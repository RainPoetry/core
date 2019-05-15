package rainpoetry.protocal

import rainpoetry.livy.repl.bean.Statement
import rainpoetry.spark.rpc.RpcAddress

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 */

package object server {

  case class ResponseExecutor(address: RpcAddress, name: String)

  case class ResponseSession(sessionId: String)

  case class ResultIndex(statementId: Int, msg:String = null)

  case class Result(statement: Statement)


}
