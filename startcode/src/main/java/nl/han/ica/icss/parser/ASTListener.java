package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.TagSelector;

import java.util.HashMap;

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
		// Push HashMap to variableScopes for global scope
		variableScopes.push(new HashMap<>());
	}
    public AST getAST() {
        return ast;
    }

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		// Create a new scope for the stylesheet
		variableScopes.push(new HashMap<>());
		Stylesheet stylesheet = new Stylesheet();
		currentContainer.push(stylesheet);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		// Pop the scope for the stylesheet
		variableScopes.pop();
        ast.root = (Stylesheet) currentContainer.pop();
	}

	@Override
	public void enterStylerule(ICSSParser.StyleruleContext ctx) {
		// Create a new scope for the stylerule
		variableScopes.push(new HashMap<>());
		Stylerule stylerule = new Stylerule();
		stylerule.addChild(new TagSelector(ctx.getChild(0).getText()));
		currentContainer.push(stylerule);
	}

	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		// Pop the scope for the stylerule
		variableScopes.pop();
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
		currentContainer.peek().addChild(declaration);
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
			currentContainer.push(variableReference);
		}
	}

}