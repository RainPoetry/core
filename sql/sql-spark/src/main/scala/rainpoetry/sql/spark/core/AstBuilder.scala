package rainpoetry.sql.spark.core

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree
import rainpoetry.sql.spark.antlr.SqlBaseVisitor
import rainpoetry.sql.spark.antlr.SqlParser._
import rainpoetry.sql.spark.core.nodes._

import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/8
 * description:
 */

class AstBuilder extends SqlBaseVisitor[AnyRef] with Logging {

  var plan: ExecutePlan = _

  protected def typedVisit[T](ctx: ParseTree): T = {
    ctx.accept(this).asInstanceOf[T]
  }

  import scala.collection.JavaConverters._

  protected def plan(tree: ParserRuleContext): ExecutePlan = typedVisit(tree)

  override def visitStatement(ctx: StatementContext): ExecutePlan = {
    for (code <- ctx.code().asScala) {
       plan = visit(code).asInstanceOf[ExecutePlan]
    }
    plan
  }

  override def visitSql(ctx: SqlContext): ExecutePlan = {
    ctx.option.getType match {
      case LOAD => Load(ctx, Option.apply(plan))
      case SAVE => Save(ctx, Option.apply(plan))
      case SELECT => Select(ctx.getText, Option.apply(plan))
      case INSERT => Insert(ctx.getText, Option.apply(plan))
      case CREATE => Create(ctx.getText, Option.apply(plan))
      case DROP => Drop(ctx.getText, Option.apply(plan))
      case SHOW => Show(ctx.getText, Option.apply(plan))
      case EXPLAIN => Explain(ctx.getText, Option.apply(plan))
    }
  }

  override def visitOther(ctx: OtherContext): ExecutePlan = {
    OriginSparkCode(ctx.getText, Option.apply(plan))
  }
}
