// Generated from C:/dev/icss2022-sep/CompilerTestOmgeving/src/Expressions.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExpressionsParser}.
 */
public interface ExpressionsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExpressionsParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(ExpressionsParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionsParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(ExpressionsParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(ExpressionsParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(ExpressionsParser.TermContext ctx);
}