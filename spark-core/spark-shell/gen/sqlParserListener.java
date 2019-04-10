// Generated from F:/code/core/spark-core/spark-shell/src/main/resources\SqlParser.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SqlParserParser}.
 */
public interface SqlParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(SqlParserParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(SqlParserParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#sql}.
	 * @param ctx the parse tree
	 */
	void enterSql(SqlParserParser.SqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#sql}.
	 * @param ctx the parse tree
	 */
	void exitSql(SqlParserParser.SqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#overwrite}.
	 * @param ctx the parse tree
	 */
	void enterOverwrite(SqlParserParser.OverwriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#overwrite}.
	 * @param ctx the parse tree
	 */
	void exitOverwrite(SqlParserParser.OverwriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#append}.
	 * @param ctx the parse tree
	 */
	void enterAppend(SqlParserParser.AppendContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#append}.
	 * @param ctx the parse tree
	 */
	void exitAppend(SqlParserParser.AppendContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#errorIfExists}.
	 * @param ctx the parse tree
	 */
	void enterErrorIfExists(SqlParserParser.ErrorIfExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#errorIfExists}.
	 * @param ctx the parse tree
	 */
	void exitErrorIfExists(SqlParserParser.ErrorIfExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#ignore}.
	 * @param ctx the parse tree
	 */
	void enterIgnore(SqlParserParser.IgnoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#ignore}.
	 * @param ctx the parse tree
	 */
	void exitIgnore(SqlParserParser.IgnoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(SqlParserParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(SqlParserParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(SqlParserParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(SqlParserParser.BooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SqlParserParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SqlParserParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#ender}.
	 * @param ctx the parse tree
	 */
	void enterEnder(SqlParserParser.EnderContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#ender}.
	 * @param ctx the parse tree
	 */
	void exitEnder(SqlParserParser.EnderContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#format}.
	 * @param ctx the parse tree
	 */
	void enterFormat(SqlParserParser.FormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#format}.
	 * @param ctx the parse tree
	 */
	void exitFormat(SqlParserParser.FormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(SqlParserParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(SqlParserParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#db}.
	 * @param ctx the parse tree
	 */
	void enterDb(SqlParserParser.DbContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#db}.
	 * @param ctx the parse tree
	 */
	void exitDb(SqlParserParser.DbContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SqlParserParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SqlParserParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(SqlParserParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(SqlParserParser.FunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#col}.
	 * @param ctx the parse tree
	 */
	void enterCol(SqlParserParser.ColContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#col}.
	 * @param ctx the parse tree
	 */
	void exitCol(SqlParserParser.ColContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(SqlParserParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(SqlParserParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SqlParserParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SqlParserParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterStrictIdentifier(SqlParserParser.StrictIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitStrictIdentifier(SqlParserParser.StrictIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifier(SqlParserParser.QuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifier(SqlParserParser.QuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParserParser#numPartition}.
	 * @param ctx the parse tree
	 */
	void enterNumPartition(SqlParserParser.NumPartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParserParser#numPartition}.
	 * @param ctx the parse tree
	 */
	void exitNumPartition(SqlParserParser.NumPartitionContext ctx);
}