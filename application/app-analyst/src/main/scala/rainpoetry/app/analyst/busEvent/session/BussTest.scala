package rainpoetry.app.analyst.busEvent.session

import com.google.common.eventbus.{EventBus, Subscribe}

/*
 * User: chenchong
 * Date: 2019/5/15
 * description:
 */

object BussTest {

  def main(args: Array[String]): Unit = {

      val bus = new EventBus("demo")
    bus.register(new Demo)
    bus.post("helo")
  }

}

trait Bus{
  @Subscribe
  def deal(name:String):Unit
}

class Demo extends Bus{
  override def deal(name: String): Unit = {
    println(s"name:${name}")
  }
}