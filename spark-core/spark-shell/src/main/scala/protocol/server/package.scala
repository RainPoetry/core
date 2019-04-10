package protocol

import rainpoetry.spark.rpc.RpcAddress

/*
 * User: chenchong
 * Date: 2019/4/3
 * description:
 */

package object server {

    case class ResponseExecutor(address:RpcAddress, name: String)

}
