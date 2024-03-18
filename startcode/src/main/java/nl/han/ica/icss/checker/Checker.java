package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import javax.swing.text.Style;
import java.util.ArrayList;
import java.util.HashMap;



public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    // todo: Controleer of er geen variabelen worden gebruikt die niet gedefinieerd zijn.
    // todo: Controleer of variabelen enkel binnen hun scope gebruikt worden
    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        checkStyleSheet(ast.root);
    }

    public void checkStyleSheet(Stylesheet stylesheet) {
        variableTypes.addFirst(new HashMap<>());
        for (ASTNode astNode : stylesheet.getChildren()) {
            if (astNode instanceof Stylerule) {
                checkStylerule((Stylerule) astNode);
            } else if (astNode instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) astNode);
            }
        }
        variableTypes.removeFirst();
    }

    private void checkVariableAssignment(VariableAssignment assignment) {
        // add variable to current scope
        if (assignment.expression instanceof BoolLiteral){
            variableTypes.getFirst().put(assignment.name.name, ExpressionType.BOOL);
        } else if (assignment.expression instanceof ColorLiteral){
            variableTypes.getFirst().put(assignment.name.name, ExpressionType.COLOR);
        } else if (assignment.expression instanceof PixelLiteral){
            variableTypes.getFirst().put(assignment.name.name, ExpressionType.PIXEL);
        } else if (assignment.expression instanceof PercentageLiteral){
            variableTypes.getFirst().put(assignment.name.name, ExpressionType.PERCENTAGE);
        } else if (assignment.expression instanceof ScalarLiteral){
            variableTypes.getFirst().put(assignment.name.name, ExpressionType.SCALAR);
        } else if (assignment.expression instanceof VariableReference){
            if (!variableTypes.getFirst().containsKey(((VariableReference) assignment.expression).name)){
                assignment.setError("Variable not found");
            } else {
                variableTypes.getFirst().put(assignment.name.name, variableTypes.getFirst().get(((VariableReference) assignment.expression).name));
            }
        } else if (assignment.expression instanceof Operation){
            checkOperation((Operation) assignment.expression);
        }
    }

    private void checkStylerule(Stylerule stylerule) {
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
        variableTypes.addFirst(new HashMap<>());
        for (ASTNode astNode : body) {
            if (astNode instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) astNode);
            } else if (astNode instanceof IfClause) {
                checkIfClause((IfClause) astNode);
            } else if (astNode instanceof Declaration) {
                checkDeclaration((Declaration) astNode);
            }
        }
        variableTypes.removeFirst();
    }

    /* Controleer of bij declaraties het type van de value klopt met de property.
    Declaraties zoals width: #ff0000 of color: 12px zijn natuurlijk onzin.
     */
    private void checkDeclaration(Declaration declaration) {
        ExpressionType expression = null;
        if (declaration.expression instanceof VariableReference) {
            for (HashMap<String, ExpressionType> scope : variableTypes) {
                if (scope.containsKey(((VariableReference) declaration.expression).name)) {
                    expression = scope.get(((VariableReference) declaration.expression).name);
                    break;
                }
            }
            if (expression == null) {
                declaration.setError("Variable not found");
                return;
            }
        }

        if (declaration.expression instanceof Operation) {
            checkOperation((Operation) declaration.expression);
            return;
        }
        switch (declaration.property.name){
            case "color":
                if (!(declaration.expression instanceof ColorLiteral) && expression != ExpressionType.COLOR) {
                    declaration.setError("Color must be a color literal");
                }
                break;
            case "background-color":
                if (!(declaration.expression instanceof ColorLiteral) && expression != ExpressionType.COLOR) {
                    declaration.setError("Background-color must be a color literal");
                }
                break;
            case "height":
                if (!(declaration.expression instanceof PixelLiteral) && expression != ExpressionType.PIXEL) {
                    declaration.setError("Height must be a pixel literal");
                }
                break;
            case "width":
                if (!(declaration.expression instanceof PixelLiteral) && expression != ExpressionType.PIXEL) {
                    declaration.setError("Width must be a pixel literal");;
                }
                break;
            default:
                declaration.setError("Unknown property");
        }

    }

    /*Controleer of de conditie bij een if-statement van het
    type boolean is (zowel bij een variabele-referentie als een boolean literal)
     */
    private void checkIfClause(IfClause ifClause) {
        // Check if expression
        if (ifClause.conditionalExpression instanceof VariableReference) {
            checkVariableReference((VariableReference) ifClause.conditionalExpression);
        } else if (ifClause.conditionalExpression instanceof BoolLiteral) {
            checkLiteral((BoolLiteral) ifClause.conditionalExpression);
        } else {
            ifClause.setError("If clause must be a variable reference or literal");
        }
        // then check body
        checkBody(ifClause.body);

        // then check else clause body
        if (ifClause.elseClause != null) {
            checkBody(ifClause.elseClause.body);
        }
    }

    private void checkLiteral(Literal literal) {

    }

    private void checkOperation(Operation operation) {
        // todo: check if operation is valid

        /*Controleer of er geen kleuren worden gebruikt in operaties (plus, min en keer).
         */

        /*Controleer of de operanden van de operaties plus en min van gelijk type zijn.
        Je mag geen pixels bij percentages optellen bijvoorbeeld.
        Controleer dat bij vermenigvuldigen minimaal een operand een scalaire waarde is.
        Zo mag 20% * 3 en 4 * 5 wel, maar mag 2px * 3px niet.	*/

        // Each literal child needs to be from the same class (Bool, Percentage, Pixel)
    }

    private void checkVariableReference(VariableReference variableReference) {

    }


}
