package com.cc.sql

import com.cc.antlr.SqlBaseVisitor
import com.cc.antlr.SqlParser._
import com.cc.shell.engine.repl.SparkInterpreter
import com.cc.sql.executor._
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree
import org.apache.spark.sql.SparkSession

import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/8
 * description:
 */

class AstBuilder(session:SparkSession, interpreter:SparkInterpreter)  extends SqlBaseVisitor[AnyRef] with Logging{

  protected def typedVisit[T](ctx: ParseTree): T = {
    ctx.accept(this).asInstanceOf[T]
  }


  protected def plan(tree: ParserRuleContext): ExecutorTracker = typedVisit(tree)

  override def visitStatement(ctx: StatementContext): ExecutorTracker = {
    val father = new ExecutorTracker(ctx.code().size())
    for (id <- 0 to ctx.code().size() - 1) {
      father.append(id) {
        visit(ctx.code(id)).asInstanceOf[ExecutorTracker]
      }
    }
    father
  }

  override def visitSql(ctx: SqlContext): ExecutorTracker = {
    println(ctx.option.getText +" : " + ctx.getText)
    val executor = ctx.option.getType match {
      case LOAD => LoadExecutor(ctx)
      case SAVE => SaveExecutor(ctx)
      case SELECT => SelectExecutor(ctx)
      case INSERT => InsertExecutor(ctx)
      case CREATE => CreateExecutor(ctx)
      case DROP => DropExecutor(ctx)
      case SHOW => ShowExecutor(ctx)
      case EXPLAIN => ExplainExecutor(ctx)
//      case CODE => CodeExecutor(ctx)
    }
    executor.exec(session,interpreter)
  }

  override def visitOther(ctx: OtherContext): ExecutorTracker = {
      println("code : " + ctx.getText)
      new CodeExecutor(ctx.getText).exec(session,interpreter)
  }
}
