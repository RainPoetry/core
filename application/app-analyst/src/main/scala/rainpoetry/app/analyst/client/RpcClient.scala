package rainpoetry.app.analyst.client

import rainpoetry.app.analyst.server.{Master, RpcSession}
import rainpoetry.livy.utils.Logging
import rainpoetry.protocal.client._
import rainpoetry.protocal.server._
import rainpoetry.spark.rpc.{RpcAddress, RpcEndpointRef, RpcEnv}

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 */

class RpcClient(host: String, port: Int = Master.port) extends Logging {

  private val address = RpcAddress(host, port)

  private val rpcEnv = RpcEnv.create

  private var executor:RpcEndpointRef = _
  private var sessionId:String = _

  val master = rpcEnv.setupEndpointRef(address, Master.ENDPOINT_NAME)

  allocateResource

  def execute(code: String, codeType: String = "spark"): Unit = {
    var result = executor.askSync[ResultIndex](Execute(code, codeType, sessionId))
    if (result.statementId == RpcSession.NOTEXIST) {
      allocateResource()
      result = executor.askSync[ResultIndex](Execute(code, codeType, sessionId))
      if (result.statementId == RpcSession.NOTEXIST) {
        error("无法获取有效 session")
      }
    } else {
      val statement = executor.askSync[Result](ExecuteDetail(result.statementId, sessionId))
      println(statement.statement.output)
    }
  }

  private def allocateResource(): (RpcEndpointRef, String) = {
    executor = {
      val response = master.askSync[ResponseExecutor](RequireExecutor)
      rpcEnv.setupEndpointRef(response.address, response.name)
    }
    sessionId = executor.askSync[ResponseSession](RegisterSession()).sessionId
    info(s"分配的 sessionId : ${sessionId}")
    (executor, sessionId)
  }

  def close(): Unit = {
    rpcEnv.shutdown()
  }

}
