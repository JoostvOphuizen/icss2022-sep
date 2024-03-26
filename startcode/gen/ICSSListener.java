// Generated from C:/dev/icss2022-sep/startcode/src/main/antlr4/nl/han/ica/icss/parser/ICSS.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ICSSParser}.
 */
public interface ICSSListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void enterStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void exitStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#rules}.
	 * @param ctx the parse tree
	 */
	void enterRules(ICSSParser.RulesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#rules}.
	 * @param ctx the parse tree
	 */
	void exitRules(ICSSParser.RulesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylerule}.
	 * @param ctx the parse tree
	 */
	void enterStylerule(ICSSParser.StyleruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylerule}.
	 * @param ctx the parse tree
	 */
	void exitStylerule(ICSSParser.StyleruleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ICSSParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ICSSParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#variableAssignment}.
	 * @param ctx the parse tree
	 */
	void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#variableAssignment}.
	 * @param ctx the parse tree
	 */
	void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#codeBlock}.
	 * @param ctx the parse tree
	 */
	void enterCodeBlock(ICSSParser.CodeBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#codeBlock}.
	 * @param ctx the parse tree
	 */
	void exitCodeBlock(ICSSParser.CodeBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#ifstatement}.
	 * @param ctx the parse tree
	 */
	void enterIfstatement(ICSSParser.IfstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#ifstatement}.
	 * @param ctx the parse tree
	 */
	void exitIfstatement(ICSSParser.IfstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#elsestatement}.
	 * @param ctx the parse tree
	 */
	void enterElsestatement(ICSSParser.ElsestatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#elsestatement}.
	 * @param ctx the parse tree
	 */
	void exitElsestatement(ICSSParser.ElsestatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(ICSSParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(ICSSParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(ICSSParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(ICSSParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(ICSSParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(ICSSParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(ICSSParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(ICSSParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(ICSSParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(ICSSParser.ExpressionListContext ctx);
}