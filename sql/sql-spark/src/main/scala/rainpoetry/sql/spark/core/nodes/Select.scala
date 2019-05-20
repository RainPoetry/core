package rainpoetry.sql.spark.core.nodes

import rainpoetry.sql.spark.core.{ExecutePlan, SqlPlan}

/*
 * User: chenchong
 * Date: 2019/5/20
 * description:
 */

case class Select(code: String,
                  plan: Option[ExecutePlan]) extends SqlPlan {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Create(code: String,
                  plan: Option[ExecutePlan]) extends SqlPlan {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Drop(code: String,
                plan: Option[ExecutePlan]) extends SqlPlan {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Explain(code: String,
                   plan: Option[ExecutePlan]) extends SqlPlan {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Insert(code: String,
                   plan: Option[ExecutePlan]) extends SqlPlan {

  override def children: Seq[ExecutePlan] = plan.toSeq
}

case class Show(code: String,
                  plan: Option[ExecutePlan]) extends SqlPlan {

  override def children: Seq[ExecutePlan] = plan.toSeq
}
