package rainpoetry.sql.spark.core.nodes

import rainpoetry.sql.spark.core.{ExecutePlan, SqlPlan}

/*
 * User: chenchong
 * Date: 2019/5/20
 * description:
 */

case class Select(override val code: String,
                  plan: Option[ExecutePlan]) extends SqlPlan(code) {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Create(override val code: String,
                  plan: Option[ExecutePlan]) extends SqlPlan(code)  {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Drop(override val code: String,
                plan: Option[ExecutePlan]) extends SqlPlan(code)  {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Explain(override val code: String,
                   plan: Option[ExecutePlan]) extends SqlPlan(code)  {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Insert(override val code: String,
                   plan: Option[ExecutePlan]) extends SqlPlan(code)  {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Show(override val code: String,
                  plan: Option[ExecutePlan]) extends SqlPlan(code)  {

  override def children: Seq[ExecutePlan] = plan.toSeq
}
