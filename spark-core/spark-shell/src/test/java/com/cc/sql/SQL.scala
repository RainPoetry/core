package com.cc.sql

import com.cc.antlr.{SqlLexer, SqlParser}
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

/*
 * User: chenchong
 * Date: 2019/4/10
 * description:
 */

class SQL {

  def command(command: String) {
    val lexer = new SqlLexer(CharStreams.fromString(command))
    val tokenStream = new CommonTokenStream(lexer)
    /** 语法解析器 */
    val parser = new SqlParser(tokenStream)
    parser.removeErrorListeners()
    val tree  = parser.statement()
    println(tree.toStringTree(parser))
  }

  def load(): Unit = {
    val sql =
      """
        |load text.'G:/ccc' where data = '17895' as demo
      """.stripMargin
    command(sql)
  }

  def save(): Unit ={
    val sql = "save overwrite data as text.'G:/result.txt';"
    command(sql)
  }


  def select(): Unit = {
    val sql = "select * from dual as demo"
    command(sql)
  }

  def code(): Unit = {
    val sql = "spark.deal.make.to('sasa');"
    command(sql)
  }

  def sum(): Unit = {
    val sql  =
      """
        |load text.'sasa_sas' where data = '123' as data;
        |save overwrite data as text.'G:/result.txt';
        |select * from data;
      """.stripMargin
    command(sql)
  }
}

object SQL{
  def main(args: Array[String]): Unit = {
    val a = new SQL
    a.sum
  }
}
