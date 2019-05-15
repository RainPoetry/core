package rainpoetry.app.analyst.server



import com.google.common.eventbus.Subscribe

/*
 * User: chenchong
 * Date: 2019/5/15
 * description:
 */

trait SessionBus {

  @Subscribe def persist(session:RpcSession): Unit

}
