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

    private boolean PixelLiteral = false;
    private boolean PercentageLiteral = false;

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
        // reset literals
        PixelLiteral = false;
        PercentageLiteral = false;
        // Create postfix
//        ArrayList<Expression> postfix = new ArrayList<>();
//        createPostfix(operation, postfix);

//        // if there is a pixelLiteral and a percentageLiteral in the postfix, return error
//        boolean pixelLiteral = false;
//        boolean percentageLiteral = false;
//        for (Expression expression : postfix) {
//            if (expression instanceof PixelLiteral) {
//                if (percentageLiteral) {
//                    operation.setError("Pixel and percentage literals can't be used in the same operation");
//                    return;
//                }
//                pixelLiteral = true;
//            } else if (expression instanceof PercentageLiteral) {
//                if (pixelLiteral) {
//                    operation.setError("Pixel and percentage literals can't be used in the same operation");
//                    return;
//                }
//                percentageLiteral = true;
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
            checkOperationTree((Operation) operation.lhs);
        } else {
            checkOperand(operation.lhs);
        }

        // Check right child
        if (operation.rhs instanceof Operation) {
            checkOperationTree((Operation) operation.rhs);
        } else {
            checkOperand(operation.rhs);
        }

        Expression left = operation.lhs;
        Expression right = operation.rhs;

        if (left instanceof VariableReference) {
            left = getExpressionFromVariableReference((VariableReference) left);
        }
        if (right instanceof VariableReference) {
            right = getExpressionFromVariableReference((VariableReference) right);
        }

        if (left instanceof PixelLiteral || right instanceof PixelLiteral) {
            PixelLiteral = true;
        }
        if (right instanceof PercentageLiteral || left instanceof PercentageLiteral) {
            PercentageLiteral = true;
        }
        if (PixelLiteral && PercentageLiteral) {
            operation.setError("Pixel and percentage literals can't be used in the same operation");
        }

        // traverse the tree and check if the operation is valid
        // if operation is addition or subtraction, left should be (PixelLiteral || percentageLiteral) || Addition/subtraction || multiplication
        // then do same for right
        // if operation is multiplication, left if (scalar) then right should be (PixelLiteral || percentageLiteral) || Addition/subtraction || multiplication else return;
        if (operation instanceof AddOperation || operation instanceof SubtractOperation) {
            if (isValidInOperation(left)) {
                operation.setError("Addition or subtraction must be a pixel or percentage literal or another operation");
            }
            if (isValidInOperation(right)) {
                operation.setError("Addition or subtraction must be a pixel or percentage literal or another operation");
            }
        } else if (operation instanceof MultiplyOperation) {
            if (left instanceof ScalarLiteral) {
                if (isValidInOperation(right)) {
                    operation.setError("Multiplication must be a pixel or percentage literal or another operation");
                } else {
                    operation.setError("Multiplication can't have multiple scalar literals as operands");
                }
            } else if (!(right instanceof ScalarLiteral)) {
                operation.setError("Multiplication can't have multiple operands");
            }
        }
    }

    private Expression getExpressionFromVariableReference(VariableReference variableReference) {
        for (HashMap<String, ExpressionType> scope : variableTypes) {
            if (scope.containsKey(variableReference.name)) {
                ExpressionType type = scope.get(variableReference.name);
                if (type == null) {
                    variableReference.setError("Variable not found in this scope.");
                    return null;
                }
                switch (type) {
                    case BOOL:
                        return new BoolLiteral("BoolLiteral");
                    case COLOR:
                        return new ColorLiteral("#000000");
                    case PIXEL:
                        return new PixelLiteral("10px");
                    case PERCENTAGE:
                        return new PercentageLiteral("10%");
                    case SCALAR:
                        return new ScalarLiteral(1);
                }
            }
        }
        return null;
    }

    private boolean isValidInOperation(Expression expression){
        if (expression instanceof PixelLiteral || expression instanceof PercentageLiteral || expression instanceof AddOperation || expression instanceof SubtractOperation || expression instanceof MultiplyOperation) {
            return false;
        }
        return true;
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
