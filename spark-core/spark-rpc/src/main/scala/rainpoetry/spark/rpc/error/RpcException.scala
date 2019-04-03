package rainpoetry.spark.rpc.error

/*
 * User: chenchong
 * Date: 2019/4/1
 * description:
 */

class RpcException(message: String, cause: Throwable)
  extends Exception(message, cause) {
  def this(message: String) = this(message, null)
}
