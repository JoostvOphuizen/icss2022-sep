package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class listens for events triggered by the parser and creates an AST based on the parsed input.
 *
 * @author Joost van Ophuizen
 * @version 1.0
 */
public class ASTListener extends ICSSBaseListener {
	
	private final AST ast = new AST();
	private final IHANStack<ASTNode> currentContainer;

	public ASTListener() {
        currentContainer = new HANStack<>();
	}

	public AST getAST() {
        return ast;
    }

	/**
	 * This method is called when the parser has matched a stylesheet.
	 * It creates a new Stylesheet object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The stylesheet context
	 */
	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		Stylesheet stylesheet = new Stylesheet();
		currentContainer.push(stylesheet);
	}

	/**
	 * This method is called when the parser has matched a stylerule.
	 * It creates a new Stylerule object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The stylerule context
	 */
	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        ast.root = (Stylesheet) currentContainer.pop();
	}

	/**
	 * This method is called when the parser has matched a stylerule.
	 * It creates a new Stylerule object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The stylerule context
	 */
	@Override
	public void enterStylerule(ICSSParser.StyleruleContext ctx) {
		Stylerule stylerule = new Stylerule();
		if (ctx.getChild(0).getText().startsWith(".")) {
			stylerule.addChild(new ClassSelector(ctx.getChild(0).getText()));
		} else if (ctx.getChild(0).getText().startsWith("#")) {
			stylerule.addChild(new IdSelector(ctx.getChild(0).getText()));
		} else {
			stylerule.addChild(new TagSelector(ctx.getChild(0).getText()));
		}
		currentContainer.push(stylerule);
	}

	/**
	 * This method is called when the parser has matched a declaration.
	 * It creates a new Declaration object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The declaration context
	 */
	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		Stylerule stylerule = (Stylerule) currentContainer.pop();
		currentContainer.peek().addChild(stylerule);
	}

	/**
	 * This method is called when the parser has matched a property.
	 * It creates a new PropertyName object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The property context
	 */
	@Override
	public void enterFunction(ICSSParser.FunctionContext ctx) {
		Function function = new Function(new PropertyName(ctx.CAPITAL_IDENT().getText()));
		currentContainer.push(function);
	}

	/**
	 * This method is called when the parser has matched a function.
	 * It creates a new Function object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The function context
	 */
	@Override
	public void exitFunction(ICSSParser.FunctionContext ctx) {
		Function function = (Function) currentContainer.pop();
		currentContainer.peek().addChild(function);
	}

	/**
	 * This method is called when the parser has matched a declaration.
	 * It creates a new Declaration object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The declaration context
	 */
	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = new Declaration();
		declaration.addChild(new PropertyName(ctx.getChild(0).getText()));
		currentContainer.push(declaration);
	}

	/**
	 * This method is called when the parser has matched a declaration.
	 * It creates a new Declaration object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The declaration context
	 */
	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		Expression expression = (Expression) currentContainer.pop();
		Declaration declaration = (Declaration) currentContainer.pop();
		declaration.addChild(expression);

		if (currentContainer.peek() instanceof Expression){
			Expression parent = (Expression) currentContainer.pop();
			currentContainer.peek().addChild(declaration);
			currentContainer.push(parent);
		} else {
			currentContainer.peek().addChild(declaration);
		}
	}

	/**
	 * This method is called when the parser has matched a variableAssignment.
	 * It creates a new VariableAssignment object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The variableAssignment context
	 */
	@Override
	public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		VariableAssignment variableAssignment = new VariableAssignment();
		variableAssignment.addChild(new VariableReference(ctx.CAPITAL_IDENT().getText()));
		currentContainer.push(variableAssignment);
	}

	/**
	 * This method is called when the parser has matched a variableAssignment.
	 * It creates a new VariableAssignment object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The variableAssignment context
	 */
	@Override
	public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx)  {
		if (currentContainer.peek() instanceof Expression) {
			Expression expression = (Expression) currentContainer.pop();
			VariableAssignment variableAssignment = (VariableAssignment) currentContainer.pop();
			variableAssignment.addChild(expression);
			currentContainer.peek().addChild(variableAssignment);
		} else if (currentContainer.peek() instanceof VariableReference) {
			VariableReference variableReference = (VariableReference) currentContainer.pop();
			VariableAssignment variableAssignment = (VariableAssignment) currentContainer.pop();
			variableAssignment.addChild(variableReference);
			currentContainer.peek().addChild(variableAssignment);
		}
	}

	/**
	 * This method is called when the parser has matched an expression.
	 * It creates a new Expression object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The expression context
	 */
	@Override
	public void exitExpression(ICSSParser.ExpressionContext ctx) {
		if (ctx.getChildCount() == 1) {
			currentContainer.push(currentContainer.pop());
			return;
		}

		if(ctx.MUL() != null) {
			Operation operation = new MultiplyOperation();
			operation.addChild(currentContainer.pop());
			operation.addChild(currentContainer.pop());
			currentContainer.push(operation);
		} else if(ctx.PLUS() != null) {
			Operation operation = new AddOperation();
			operation.addChild(currentContainer.pop());
			operation.addChild(currentContainer.pop());
			currentContainer.push(operation);
		} else if(ctx.MIN() != null) {
			Operation operation = new SubtractOperation();
			operation.addChild(currentContainer.pop());
			operation.addChild(currentContainer.pop());
			currentContainer.push(operation);
		}
	}

	/**
	 * This method is called when the parser has matched a value.
	 * It creates a new Literal object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The value context
	 */
	@Override
	public void exitValue(ICSSParser.ValueContext ctx) {
		if (ctx.COLOR() != null) {
			ColorLiteral colorLiteral = new ColorLiteral(ctx.COLOR().getText());
			currentContainer.push(colorLiteral);
		} else if (ctx.SCALAR() != null) {
			ScalarLiteral scalarLiteral = new ScalarLiteral(ctx.SCALAR().getText());
			currentContainer.push(scalarLiteral);
		} else if (ctx.PIXELSIZE() != null) {
			PixelLiteral pixelLiteral = new PixelLiteral(ctx.PIXELSIZE().getText());
			currentContainer.push(pixelLiteral);
		} else if (ctx.PERCENTAGE() != null) {
			PercentageLiteral percentageLiteral = new PercentageLiteral(ctx.PERCENTAGE().getText());
			currentContainer.push(percentageLiteral);
		} else if (ctx.TRUE() != null || ctx.FALSE() != null) {
			BoolLiteral boolLiteral = new BoolLiteral(ctx.getText());
			currentContainer.push(boolLiteral);
		} else if (ctx.CAPITAL_IDENT() != null) {
			currentContainer.push(new VariableReference(ctx.CAPITAL_IDENT().getText()));
		}
	}

	/**
	 * This method is called when the parser has matched an if statement.
	 * It creates a new IfClause object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The if statement context
	 */
	@Override
	public void enterIfstatement(ICSSParser.IfstatementContext ctx) {
		IfClause ifClause = new IfClause();
		Expression expression = new VariableReference("test");
		ifClause.addChild(expression);
		currentContainer.push(ifClause);
	}

	/**
	 * This method is called when the parser has matched an if statement.
	 * It creates a new IfClause object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The if statement context
	 */
	@Override
	public void exitIfstatement(ICSSParser.IfstatementContext ctx) {
		IfClause ifClause = null;
		if (currentContainer.peek() instanceof Expression){
			Expression expression = (Expression) currentContainer.pop();
			ifClause = (IfClause) currentContainer.pop();
			ifClause.addChild(expression);
		}

		while (currentContainer.peek() instanceof Declaration) {
			ASTNode node = currentContainer.pop();
            assert ifClause != null;
            ifClause.addChild(node);
		}

		VariableReference variableReference = currentContainer.peek() instanceof VariableReference ? (VariableReference) currentContainer.pop() : null;
		currentContainer.peek().addChild(ifClause);
		if (variableReference != null) {
			currentContainer.push(variableReference);
		}
	}

	/**
	 * This method is called when the parser has matched an else statement.
	 * It creates a new ElseClause object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The else statement context
	 */
	@Override
	public void enterElsestatement(ICSSParser.ElsestatementContext ctx) {
		ElseClause elseClause = new ElseClause();
		currentContainer.push(elseClause);
	}

	/**
	 * This method is called when the parser has matched an else statement.
	 * It creates a new ElseClause object and pushes it onto the currentContainer stack.
	 *
	 * @param ctx The else statement context
	 */
	@Override
	public void exitElsestatement(ICSSParser.ElsestatementContext ctx) {
		ElseClause elseClause = (ElseClause) currentContainer.pop();
		VariableReference variableReference = currentContainer.peek() instanceof VariableReference ? (VariableReference) currentContainer.pop() : null;
		currentContainer.peek().addChild(elseClause);
		if (variableReference != null) {
			currentContainer.push(variableReference);
		}
	}

}