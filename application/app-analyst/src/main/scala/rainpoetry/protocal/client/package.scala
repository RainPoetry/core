package rainpoetry.protocal

/*
 * User: chenchong
 * Date: 2019/5/10
 * description:
 */

package object client {

  case class RequireExecutor()

  case class RegisterSession(name: String = null)

  case class Execute(code: String, codeType: String, sessionId:String, isSync:Boolean = true)

  case class CloseSession(sessionId:String)

  case class Cancel(statementId: Int, sessionId:String)

  case class ExecuteDetail(statementId: Int, sessionId:String)

}
