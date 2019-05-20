// Generated from F:/code/core/sql/sql-spark/src/main/resources\Sql.g4 by ANTLR 4.7.2
package rainpoetry.sql.spark.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SqlParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SqlParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sql}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql(SqlParser.SqlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comment}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(SqlParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code other}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOther(SqlParser.OtherContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#scala}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScala(SqlParser.ScalaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#numPartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumPartition(SqlParser.NumPartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#overwrite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverwrite(SqlParser.OverwriteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#append}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAppend(SqlParser.AppendContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#errorIfExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitErrorIfExists(SqlParser.ErrorIfExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#ignore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgnore(SqlParser.IgnoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(SqlParser.UpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpression(SqlParser.BooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#format}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormat(SqlParser.FormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(SqlParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SqlParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(SqlParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(SqlParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#ender}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnder(SqlParser.EnderContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#strictIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrictIdentifier(SqlParser.StrictIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#seperate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeperate(SqlParser.SeperateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#col}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol(SqlParser.ColContext ctx);
}