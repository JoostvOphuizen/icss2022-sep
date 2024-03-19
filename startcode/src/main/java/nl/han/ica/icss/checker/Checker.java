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
                declaration.setError("Variable not found in this scope.");
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
        checkOperationTree(operation);

//        ArrayList<Expression> postfix = new ArrayList<>();
//        createPostfix(operation, postfix);
//
//        // check if contains MultiplyOperation
//        if (postfix.stream().anyMatch(expression -> expression instanceof MultiplyOperation)) {
//            // can only contain one Operand, example: list {Procent, Scalar, scalar, pixel} not valid because it contains 2 different operands
//            long countScalar = postfix.stream().filter(expression -> expression instanceof ScalarLiteral).count();
//            long countOperations = postfix.stream().filter(expression -> expression instanceof Operation).count();
//            if (countScalar != (long) postfix.size() -countOperations -1){
//                operation.setError("Multiply operation should have a maximum of one operand.");
//            }
//        }

    }

    private void createPostfix(Operation operation, ArrayList<Expression> postfix) {
        if (operation.rhs instanceof Operation) {
            createPostfix((Operation) operation.rhs, postfix);
        } else {
            postfix.add(operation.rhs);
        }
        if (operation.lhs instanceof Operation) {
            createPostfix((Operation) operation.lhs, postfix);
        } else {
            postfix.add(operation.lhs);
        }
        postfix.add(operation);
    }

    private void checkOperationTree(Operation operation) {
        // Check left child
        if (operation.lhs instanceof Operation) {
            checkOperation((Operation) operation.lhs);
        } else {
            checkOperand(operation.lhs);
        }

        // Check right child
        if (operation.rhs instanceof Operation) {
            checkOperation((Operation) operation.rhs);
        } else {
            checkOperand(operation.rhs);
        }

        // Check operation
        ExpressionType lhs = getExpressionType(operation.lhs);
        ExpressionType rhs = getExpressionType(operation.rhs);

        if (lhs == ExpressionType.COLOR || rhs == ExpressionType.COLOR) {
            operation.setError("Color literals cannot be used in operations");
            return;
        }
        if (operation instanceof AddOperation || operation instanceof SubtractOperation) {
            if (lhs != rhs) {
                if (lhs == null || rhs == null) {
                    return;
                }
                operation.setError("Operation should be of the same type.");
            }
        }
        if (operation instanceof MultiplyOperation) {
            if (lhs != ExpressionType.SCALAR && rhs != ExpressionType.SCALAR) {
                operation.setError("Multiply operation should have a scalar as one of the operands.");
            }
        }
    }

    private void checkOperand(Expression operand) {
        if (operand instanceof ColorLiteral) {
            operand.setError("Color literals cannot be used in operations");
        }
    }

    // Only call this method if it's certain that the expression is not an operation
    private ExpressionType getExpressionType(Expression expression) {
        if (expression instanceof BoolLiteral) {
            return ExpressionType.BOOL;
        } else if (expression instanceof ColorLiteral) {
            return ExpressionType.COLOR;
        } else if (expression instanceof PixelLiteral) {
            return ExpressionType.PIXEL;
        } else if (expression instanceof PercentageLiteral) {
            return ExpressionType.PERCENTAGE;
        } else if (expression instanceof ScalarLiteral) {
            return ExpressionType.SCALAR;
        } else if (expression instanceof VariableReference) {
            for (HashMap<String, ExpressionType> scope : variableTypes) {
                if (scope.containsKey(((VariableReference) expression).name)) {
                    ExpressionType type = scope.get(((VariableReference) expression).name);
                    if (type == null) {
                        expression.setError("Variable not found in this scope.");
                        return null;
                    }
                    return type;
                }
            }
        }
        return null;
    }

    private void checkColorLiteralInOperation(Operation operation) {

    }

    private void checkVariableReference(VariableReference variableReference) {

    }


}
