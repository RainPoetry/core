package com.cc.sql

import java.util.Collections

import com.cc.antlr.{SqlLexer, SqlParser}
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.atn.ATNSimulator
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTreeWalker

import scala.rainpoetry.common.Logging


/*
 * User: chenchong
 * Date: 2019/4/8
 * description:
 */

abstract class AbstractSqlParser extends Logging {

  def astBuilder: AstBuilder

  protected def execute(command: String): ExecutorTracker = parse(command) {
        println(s"command: ${command} ")
    parser =>
      astBuilder.visitStatement(parser.statement)
  }

  def parse[T](command: String)(toResult: SqlParser => T): T = {
    /** 词法解析器 */
    val lexer = new SqlLexer(CharStreams.fromString(command))
    val tokenStream = new CommonTokenStream(lexer)
    /** 语法解析器 */
    val parser = new SqlParser(tokenStream)
    parser.removeErrorListeners()
    parser.addErrorListener(new ErrorListener)
    toResult(parser)
  }

}

private[sql] class ErrorListener extends BaseErrorListener with Logging {
//  override def syntaxError(recognizer: Recognizer[_, _ <: ATNSimulator],
//                           offendingSymbol: Any, line: Int, charPositionInLine: Int, msg: String,
//                           e: RecognitionException): Unit = {
//    val stack: util.List[String] = recognizer.asInstanceOf[Parser].getRuleInvocationStack
//    Collections.reverse(stack)
//    error(s"rule stack: ${stack}, line = ${line} : ${charPositionInLine} at  ${offendingSymbol} : ${msg}")
//  }
//  override def syntaxError(recognizer: Recognizer[_, _ <: ATNSimulator],
//                           offendingSymbol: scala.Any, line: Int, charPositionInLine: Int,
//                           msg: String, e: RecognitionException): Unit = {
//      val stack: java.util.List[String] = recognizer.asInstanceOf[Parser].getRuleInvocationStack
//      Collections.reverse(stack)
//      error(s"rule stack: ${stack}, line = ${line} : ${charPositionInLine} at  ${offendingSymbol} : ${msg}")
//  }

  override def syntaxError(
                            recognizer: Recognizer[_,  _],
                            offendingSymbol: scala.Any,
                            line: Int,
                            charPositionInLine: Int,
                            msg: String,
                            e: RecognitionException): Unit = {
    val stack: java.util.List[String] = recognizer.asInstanceOf[Parser].getRuleInvocationStack
    val token = recognizer.asInstanceOf[Parser].getCurrentToken.getText
    if (!token.matches("\\s+")) {
      Collections.reverse(stack)
      error(s"rule stack:{ ${stack.toArray.mkString(" > ")} } , error token (${token}) at  line = [${line}:${charPositionInLine}] : ${msg}")
    }
  }

}
