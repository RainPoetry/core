package rainpoetry.app.analyst.busEvent.session



import rainpoetry.app.analyst.server.{RpcSession, SessionBus}

/*
 * User: chenchong
 * Date: 2019/5/15
 * description:
 */

class FilePersist extends SessionBus {

  println("FilePersist initialize")

  override def persist(session:RpcSession): Unit = {
    val sessionId = session.sessionId
    val statements = session.statements()
    println(s"file persist, sessionId : ${sessionId} --------------------------------")
    statements.foreach{data=>
      println(s"id:${data.id}  state:${data._state}  result:${data.output}")
    }
    println("file persist sessionId : ${sessionId} --------------------------------")
  }
}
