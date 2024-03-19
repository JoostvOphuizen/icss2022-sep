package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private final IHANStack<ASTNode> currentContainer;
	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}
    public AST getAST() {
        return ast;
    }

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		Stylesheet stylesheet = new Stylesheet();
		currentContainer.push(stylesheet);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        ast.root = (Stylesheet) currentContainer.pop();
	}

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

	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		Stylerule stylerule = (Stylerule) currentContainer.pop();
		currentContainer.peek().addChild(stylerule);
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = new Declaration();
		declaration.addChild(new PropertyName(ctx.getChild(0).getText()));
		currentContainer.push(declaration);
	}

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

	@Override
	public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		VariableAssignment variableAssignment = new VariableAssignment();
		variableAssignment.addChild(new VariableReference(ctx.CAPITAL_IDENT().getText()));
		currentContainer.push(variableAssignment);
	}

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

	@Override
	public void exitExpression(ICSSParser.ExpressionContext ctx) {
		if (ctx.getChildCount() == 1) {
			currentContainer.push(currentContainer.pop());
			return;
		}

		if(ctx.MUL() != null) {
			Operation operation = new MultiplyOperation();
			operation.addChild((Expression) currentContainer.pop());
			operation.addChild((Expression) currentContainer.pop());
			currentContainer.push(operation);
		} else if(ctx.PLUS() != null) {
			Operation operation = new AddOperation();
			operation.addChild((Expression) currentContainer.pop());
			operation.addChild((Expression) currentContainer.pop());
			currentContainer.push(operation);
		} else if(ctx.MIN() != null) {
			Operation operation = new SubtractOperation();
			operation.addChild((Expression) currentContainer.pop());
			operation.addChild((Expression) currentContainer.pop());
			currentContainer.push(operation);
		}
	}

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

	@Override
	public void enterIfstatement(ICSSParser.IfstatementContext ctx) {
		IfClause ifClause = new IfClause();
		Expression expression = new VariableReference("test");
		ifClause.addChild(expression);
		currentContainer.push(ifClause);
	}

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
			System.out.printf("");
			ifClause.addChild(node);
		}

		VariableReference variableReference = currentContainer.peek() instanceof VariableReference ? (VariableReference) currentContainer.pop() : null;
		currentContainer.peek().addChild(ifClause);
		if (variableReference != null) {
			currentContainer.push(variableReference);
		}
	}

	@Override
	public void enterElsestatement(ICSSParser.ElsestatementContext ctx) {
		ElseClause elseClause = new ElseClause();
		currentContainer.push(elseClause);
	}

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