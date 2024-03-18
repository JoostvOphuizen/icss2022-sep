package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

import javax.swing.text.Style;
import java.util.ArrayList;
import java.util.HashMap;



public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        checkStyleSheet(ast.root);
    }

    public void checkStyleSheet(Stylesheet stylesheet) {
        for (ASTNode astNode : stylesheet.getChildren()) {
            if (astNode instanceof Stylerule) {
                checkStylerule((Stylerule) astNode);
            } else if (astNode instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) astNode);
            }
        }

    }

    private void checkVariableAssignment(VariableAssignment assignment) {
        System.out.println("VARIABLE!" + assignment);
    }

    private void checkStylerule(Stylerule stylerule) {
        System.out.println("STYLERULE!" + stylerule);
        // check selectors
        for (Selector selector : stylerule.selectors) {
            checkSelector(selector);
        }

        // check body
        checkBody(stylerule.body);
    }

    private void checkSelector(Selector selector) {

    }

    private void checkBody(ArrayList<ASTNode> body){
        for (ASTNode astNode : body) {
            if (astNode instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) astNode);
            } else if (astNode instanceof IfClause) {
                checkIfClause((IfClause) astNode);
            } else if (astNode instanceof Declaration) {
                checkDeclaration((Declaration) astNode);
            }
        }
    }

    private void checkDeclaration(Declaration declaration) {
        System.out.println("DECLARATION!" + declaration);
        switch (declaration.property.name){
            case "color":
                if (!(declaration.expression instanceof ColorLiteral)) {
                    declaration.setError("Color must be a color literal");
                }
                break;
            case "background-color":
                if (!(declaration.expression instanceof ColorLiteral)) {
                    declaration.setError("Background-color must be a color literal");
                }
                break;
            case "height":
                if (!(declaration.expression instanceof PixelLiteral)) {
                    declaration.setError("Height must be a pixel literal");
                }
                break;
            case "width":
                if (!(declaration.expression instanceof PixelLiteral)) {
                    declaration.setError("Width must be a pixel literal");;
                }
                break;
            default:
                declaration.setError("Unknown property");
        }

    }

    private void checkIfClause(IfClause ifClause) {
        // Check if expression
        if (ifClause.conditionalExpression instanceof VariableReference) {
            checkVariableReference((VariableReference) ifClause.conditionalExpression);
        } else if (ifClause.conditionalExpression instanceof BoolLiteral) {
            checkLiteral((BoolLiteral) ifClause.conditionalExpression);
        } else {
            ifClause.setError("If clause must be a variable reference, operation or literal");
        }
        // then check body
        checkBody(ifClause.body);

        if (ifClause.elseClause != null) {
            checkBody(ifClause.elseClause.body);
        }
    }

    private void checkLiteral(Literal literal) {

    }

    private void checkOperation(Operation operation) {

    }

    private void checkVariableReference(VariableReference variableReference) {

    }


}
