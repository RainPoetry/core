// Generated from F:/code/core/sql/sql-spark/src/main/resources\Sql.g4 by ANTLR 4.7.2
package rainpoetry.sql.spark.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SqlParser}.
 */
public interface SqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(SqlParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(SqlParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sql}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 */
	void enterSql(SqlParser.SqlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sql}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 */
	void exitSql(SqlParser.SqlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comment}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 */
	void enterComment(SqlParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comment}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 */
	void exitComment(SqlParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code other}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 */
	void enterOther(SqlParser.OtherContext ctx);
	/**
	 * Exit a parse tree produced by the {@code other}
	 * labeled alternative in {@link SqlParser#code}.
	 * @param ctx the parse tree
	 */
	void exitOther(SqlParser.OtherContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#scala}.
	 * @param ctx the parse tree
	 */
	void enterScala(SqlParser.ScalaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#scala}.
	 * @param ctx the parse tree
	 */
	void exitScala(SqlParser.ScalaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#numPartition}.
	 * @param ctx the parse tree
	 */
	void enterNumPartition(SqlParser.NumPartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#numPartition}.
	 * @param ctx the parse tree
	 */
	void exitNumPartition(SqlParser.NumPartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#overwrite}.
	 * @param ctx the parse tree
	 */
	void enterOverwrite(SqlParser.OverwriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#overwrite}.
	 * @param ctx the parse tree
	 */
	void exitOverwrite(SqlParser.OverwriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#append}.
	 * @param ctx the parse tree
	 */
	void enterAppend(SqlParser.AppendContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#append}.
	 * @param ctx the parse tree
	 */
	void exitAppend(SqlParser.AppendContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#errorIfExists}.
	 * @param ctx the parse tree
	 */
	void enterErrorIfExists(SqlParser.ErrorIfExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#errorIfExists}.
	 * @param ctx the parse tree
	 */
	void exitErrorIfExists(SqlParser.ErrorIfExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#ignore}.
	 * @param ctx the parse tree
	 */
	void enterIgnore(SqlParser.IgnoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#ignore}.
	 * @param ctx the parse tree
	 */
	void exitIgnore(SqlParser.IgnoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(SqlParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(SqlParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(SqlParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(SqlParser.BooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#format}.
	 * @param ctx the parse tree
	 */
	void enterFormat(SqlParser.FormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#format}.
	 * @param ctx the parse tree
	 */
	void exitFormat(SqlParser.FormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(SqlParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(SqlParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SqlParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SqlParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SqlParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SqlParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(SqlParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(SqlParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#ender}.
	 * @param ctx the parse tree
	 */
	void enterEnder(SqlParser.EnderContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#ender}.
	 * @param ctx the parse tree
	 */
	void exitEnder(SqlParser.EnderContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterStrictIdentifier(SqlParser.StrictIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitStrictIdentifier(SqlParser.StrictIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#seperate}.
	 * @param ctx the parse tree
	 */
	void enterSeperate(SqlParser.SeperateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#seperate}.
	 * @param ctx the parse tree
	 */
	void exitSeperate(SqlParser.SeperateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#col}.
	 * @param ctx the parse tree
	 */
	void enterCol(SqlParser.ColContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#col}.
	 * @param ctx the parse tree
	 */
	void exitCol(SqlParser.ColContext ctx);
}