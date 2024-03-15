package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.TagSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private final IHANStack<ASTNode> currentContainer;
	private final IHANStack<HashMap<String, VariableAssignment>> variableScopes;

	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
		variableScopes = new HANStack<>();
		variableScopes.push(new HashMap<>());
	}
    public AST getAST() {
        return ast;
    }

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		variableScopes.push(new HashMap<>());
		Stylesheet stylesheet = new Stylesheet();
		currentContainer.push(stylesheet);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		variableScopes.pop();
        ast.root = (Stylesheet) currentContainer.pop();
	}

	@Override
	public void enterStylerule(ICSSParser.StyleruleContext ctx) {
		variableScopes.push(new HashMap<>());
		Stylerule stylerule = new Stylerule();
		stylerule.addChild(new TagSelector(ctx.getChild(0).getText()));
		currentContainer.push(stylerule);
	}

	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		variableScopes.pop();
		Stylerule stylerule = (Stylerule) currentContainer.pop();
		currentContainer.peek().addChild(stylerule);
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = new Declaration();
		declaration.addChild(new PropertyName(ctx.getChild(0).getText()));
		//System.out.println(new PropertyName(ctx.getChild(0).getText()));
		currentContainer.push(declaration);
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		System.out.println("BEFORE DECLARATION:  "+currentContainer.peek());

		Expression expression = (Expression) currentContainer.pop();
		Declaration declaration = (Declaration) currentContainer.pop();
		declaration.addChild(expression);
		currentContainer.peek().addChild(declaration);
		System.out.println("AFTER DECLARATION:  "+currentContainer.peek());
	}

	@Override
	public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		VariableAssignment variableAssignment = new VariableAssignment();
		variableAssignment.addChild(new VariableReference(ctx.CAPITAL_IDENT().getText()));
		currentContainer.push(variableAssignment);
	}

	@Override
	public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		Expression expression = (Expression) currentContainer.pop();
		VariableAssignment variableAssignment = (VariableAssignment) currentContainer.pop();
		variableAssignment.addChild(expression);
		variableScopes.peek().put(variableAssignment.name.name, variableAssignment);
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
		} else if (ctx.TRUE() != null) {
			BoolLiteral boolLiteral = new BoolLiteral(ctx.TRUE().getText());
			currentContainer.push(boolLiteral);
		} else if (ctx.FALSE() != null) {
			BoolLiteral boolLiteral = new BoolLiteral(ctx.FALSE().getText());
			currentContainer.push(boolLiteral);
		} else if (ctx.CAPITAL_IDENT() != null) {
			VariableReference variableReference = new VariableReference(ctx.CAPITAL_IDENT().getText());
			VariableAssignment variableAssignment = resolveVariable(variableReference.name);
			if (variableAssignment != null) {
				currentContainer.push(variableAssignment.expression);
			} else {
				throw new IllegalStateException("Variable " + variableReference.name + " not found");
			}
		}
	}

	private VariableAssignment resolveVariable(String variableName) {
		for (int i = 0; i <= variableScopes.size()-1; i++) {
			HashMap<String, VariableAssignment> scope = variableScopes.get(i);
			if (scope.containsKey(variableName)) {
				return scope.get(variableName);
			}
		}
		return null;
	}

	@Override
	public void enterIfstatement(ICSSParser.IfstatementContext ctx) {
		variableScopes.push(new HashMap<>());
		IfClause ifClause = new IfClause();
		currentContainer.push(ifClause);
	}

	@Override
	public void exitIfstatement(ICSSParser.IfstatementContext ctx) {
		ElseClause elseClause = null;
		if (currentContainer.peek() instanceof ElseClause) {
			elseClause = (ElseClause) currentContainer.pop();
		}

		Expression expression = (Expression) currentContainer.pop();
		IfClause ifClause = (IfClause) currentContainer.pop();
		//System.out.println(currentContainer.peek());
		ifClause.addChild(expression);

		if (elseClause != null) {
			ifClause.addChild(elseClause);
		}

		while (currentContainer.peek() instanceof Declaration) {
			ASTNode node = currentContainer.pop();
			ifClause.addChild(node);
		}

		variableScopes.pop();
		currentContainer.peek().addChild(ifClause);
	}

	@Override
	public void enterElsestatement(ICSSParser.ElsestatementContext ctx) {
		ElseClause elseClause = new ElseClause();
		currentContainer.push(elseClause);
	}


}