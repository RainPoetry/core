/*
package com.guava.cc.grammer

import java.util

import javax.swing.JList

/*
 * User: chenchong
 * Date: 2019/3/8
 * description:
 */

object MyImplicit {




  def main(args: Array[String]): Unit = {
    val a = 79
    val jlist = new util.ArrayList[Int]()
    jlist.add(1)
    jlist.add(2)
    jlist.add(3)
    jlist.add(4)

    import FunctorOps2._
//    val jlist2 = jlist map (_ * 3)
  }


}
class demo() {

  implicit val o = "ccc"
  def person(implicit name: String): Unit ={
    println(s"say: ${name}")
  }

  def trans(msg: String): Unit ={
    println(msg)
  }
}

class MethodInject {

}

// 传入 F[A] 和 Function
// 将 F[A] 转换位 F[B]
trait Functor[F[_]] {
  def map[A,B](fa: F[A])(f: A=>B): F[B]
}

// 提供 function
//
final class FunctorOps[F[_],A](fa: F[A])(implicit func:Functor[F]) {
  def map[A, B](f: A=>B): F[B] = func.map(fa)(f)
}

object FunctorOps2 {

   implicit def listFunc[E](list: util.List[E]): FunctorOps[util.List, E] = new FunctorOps[util.List, E](list)

  implicit val  createFunctor : Functor[util.List] = new Functor[util.List] {
    override def map[A, B](fa: util.List[A])(f: A => B): util.List[B] = {
      val fb = new util.ArrayList[B]
      for(a <- fa) {
        fb.add(f(a))
      }
      fb
    }
  }

}



*/
