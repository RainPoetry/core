package rainpoetry.app.analyst.client

/*
 * User: chenchong
 * Date: 2019/5/15
 * description:
 */

object ClientDemo {

  def main(args: Array[String]): Unit = {

    val client = new RpcClient("localhost")
    client.execute("2+2")
    client.execute("3+3")
    client.execute("4+4")
    client.close()
  }

}
