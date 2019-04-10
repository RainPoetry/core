// Generated from F:/code/core/spark-core/spark-shell/src/main/resources\SqlParser.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SqlParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SqlParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SqlParserParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#sql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql(SqlParserParser.SqlContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#overwrite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverwrite(SqlParserParser.OverwriteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#append}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAppend(SqlParserParser.AppendContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#errorIfExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitErrorIfExists(SqlParserParser.ErrorIfExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#ignore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgnore(SqlParserParser.IgnoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(SqlParserParser.UpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpression(SqlParserParser.BooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SqlParserParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#ender}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnder(SqlParserParser.EnderContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#format}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormat(SqlParserParser.FormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(SqlParserParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#db}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDb(SqlParserParser.DbContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(SqlParserParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(SqlParserParser.FunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#col}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol(SqlParserParser.ColContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(SqlParserParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(SqlParserParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#strictIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrictIdentifier(SqlParserParser.StrictIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedIdentifier(SqlParserParser.QuotedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParserParser#numPartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumPartition(SqlParserParser.NumPartitionContext ctx);
}