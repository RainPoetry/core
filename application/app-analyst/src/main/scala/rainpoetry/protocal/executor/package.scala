package rainpoetry.protocal

import rainpoetry.app.analyst.server.Executor

/*
 * User: chenchong
 * Date: 2019/5/14
 * description:
 */

package object executor {


  case class RegisterWorker(executor:Executor)

  case class UnRegisterWorker(executor:Executor)

  case class HeartBeat(executorName:String)



}
