package rainpoetry.sql.spark.core.nodes

import rainpoetry.sql.spark.core.{ExecutePlan, SparkCodePlan}

/*
 * User: chenchong
 * Date: 2019/5/20
 * description:
 */

case class OriginSparkCode(code:String,
                           plan:Option[ExecutePlan]) extends SparkCodePlan{
  override def children: Seq[ExecutePlan] = plan.toSeq
}
