package com.cc.sql

import scala.reflect.ClassTag

/*
 * User: chenchong
 * Date: 2019/4/8
 * description:
 */

// 元组
class ExecutorTracker(val nums: Int = 0) extends Product {

  var jobStatus:JobStatus = _
  var duration:Long = _

  var children: Array[ExecutorTracker] = Array.ofDim(nums)

  override def productElement(n: Int): Any = children(n)

  // 元素个数
  override def productArity: Int = nums

  override def canEqual(that: Any): Boolean = false

  def mapProductIterator[B: ClassTag](f: Any => B): Array[B] = {
    // 创建一个数据为 null 的数组
    val arr = Array.ofDim[B](productArity)
    var i = 0
    while (i < arr.length) {
      arr(i) = f(productElement(i))
      i += 1
    }
    arr
  }

  def append(id: Int)(child: ExecutorTracker): Unit = {
    children(id) = child
  }

  def status(job: JobStatus): ExecutorTracker = {
    jobStatus = job
    this
  }

  def duration(time: Long) : ExecutorTracker = {
    duration = time
    this
  }
}

case class JobStatus(
                    msg: String,
                    data: Any,
                    status:Status.kind
                  )


object Status extends Enumeration {
  type kind = Value
  val Success, Failure = Value
}