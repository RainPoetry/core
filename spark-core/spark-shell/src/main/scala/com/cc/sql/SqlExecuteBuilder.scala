package com.cc.sql

import com.cc.antlr.SqlParser._
import com.cc.antlr.{SqlBaseVisitor, SqlParser}

import scala.rainpoetry.common.Logging

/*
 * User: chenchong
 * Date: 2019/4/8
 * description:
 */

class SqlExecuteBuilder extends SqlBaseVisitor[Unit] with Logging{


  override def visitSql(ctx: SqlParser.SqlContext): Unit = {

  }


  override def visitStatement(ctx: StatementContext): Unit = {
    println(s"statement: ${ctx.getText}")
  }

}

object Deal{
  def main(args: Array[String]): Unit = {


  }
}


