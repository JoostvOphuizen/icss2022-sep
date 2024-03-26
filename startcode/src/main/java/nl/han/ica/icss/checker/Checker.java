package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Checker class is responsible for checking the AST for errors.
 *
 * @author Joost van Ophuizen
 * @version 1.0
 */
public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;
    private final HashMap<String, FunctionName> functionNames = new HashMap<>();

    private boolean PixelLiteral = false;
    private boolean PercentageLiteral = false;

    /**
     * This method checks the given AST for errors.
     *
     * @param ast The AST to check for errors.
     */
    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        checkStyleSheet(ast.root);
    }

    /**
     * This method checks the given stylesheet for errors.
     *
     * @param stylesheet The stylesheet to check for errors.
     */
    public void checkStyleSheet(Stylesheet stylesheet) {
        variableTypes.addFirst(new HashMap<>());
        for (ASTNode astNode : stylesheet.getChildren()) {
            if (astNode instanceof Stylerule) {
                checkStylerule((Stylerule) astNode);
            } else if (astNode instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) astNode);
            } else if (astNode instanceof Function) {
                checkFunction((Function) astNode);
            }
        }
        variableTypes.removeFirst();
    }

    /**
     * This method checks the given function for errors.
     *
     * @param function The function to check for errors.
     */
    private void checkFunction(Function function) {
        if (functionNames.containsKey(function.name.name)) {
            function.setError("Function name already exists");
        } else {
            functionNames.put(function.name.name, function.name);
        }
        ParameterList parameters = new ParameterList();
        for (ASTNode astNode : function.body) {
            if (astNode instanceof ParameterList) {
                parameters = (ParameterList) astNode;
            }
        }
        checkBody(function.body, parameters);
    }

    /**
     * This method checks the given variable assignment for errors.
     *
     * @param assignment The variable assignment to check for errors.
     */
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
            ExpressionType type = checkOperation((Operation) assignment.expression);
            variableTypes.getFirst().put(assignment.name.name, type);
        }
    }

    /**
     * This method checks the given style rule for errors.
     *
     * @param stylerule The style rule to check for errors.
     */
    private void checkStylerule(Stylerule stylerule) {
        checkBody(stylerule.body);
    }

    /**
     * This method checks the given body for errors.
     * A body can contain variable assignments, if-clauses, and declarations.
     *
     * @param body The body to check for errors.
     */
    private void checkBody(ArrayList<ASTNode> body, ParameterList parameters)  {
        variableTypes.addFirst(new HashMap<>());

        if (parameters != null) {
            for (Parameter parameter : parameters.parameters) {
                variableTypes.getFirst().put(parameter.name.name, ExpressionType.PARAMETER_REFERENCE);
            }
        }

        for (ASTNode astNode : body) {
            if (astNode instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) astNode);
            } else if (astNode instanceof IfClause) {
                checkIfClause((IfClause) astNode);
            } else if (astNode instanceof Declaration) {
                checkDeclaration((Declaration) astNode);
            } else if (astNode instanceof FunctionCall) {
                checkFunctionCall((FunctionCall) astNode);
            }
        }
        variableTypes.removeFirst();
    }

    /**
     * This method checks the given body for errors.
     * A body can contain variable assignments, if-clauses, and declarations.
     *
     * @param body The body to check for errors.
     */
    private void checkBody(ArrayList<ASTNode> body)  {
        checkBody(body, null);
    }

    /**
     * This method checks the given function call for errors.
     *
     * @param functionCall The function call to check for errors.
     */
    private void checkFunctionCall(FunctionCall functionCall) {
        if (!functionNames.containsKey(functionCall.getName().name)) {
            functionCall.setError("Function not found");
        }
    }

    /**
     * This method checks the given declaration for errors.
     *
     * @param declaration The declaration to check for errors.
     */
    private void checkDeclaration(Declaration declaration) {
        if (declaration.expression instanceof Operation) {
            checkOperation((Operation) declaration.expression);
            return;
        }

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

        checkDeclarationTypes(declaration, expression);
    }

    /**
     * This method checks the given declaration for errors based on the type of the declaration and the type of the expression.
     *
     * @param declaration The declaration to check for errors.
     * @param expression The type of the expression in the declaration.
     */
    private void checkDeclarationTypes(Declaration declaration, ExpressionType expression) {
        if (expression == ExpressionType.PARAMETER_REFERENCE) {
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

    /**
     * This method checks the given if-clause for errors.
     *
     * @param ifClause The if-clause to check for errors.
     */
    private void checkIfClause(IfClause ifClause) {
        // Check if expression
        if (!(ifClause.conditionalExpression instanceof VariableReference) && !(ifClause.conditionalExpression instanceof BoolLiteral)) {
            ifClause.setError("If clause must be a variable reference or literal");
        }

        // then check body
        checkBody(ifClause.body);

        // then check else clause body
        if (ifClause.elseClause != null) {
            checkBody(ifClause.elseClause.body);
        }
    }

    /**
     * This method checks the given operation for errors.
     *
     * @param operation The operation to check for errors.
     */
    private ExpressionType checkOperation(Operation operation) {
        checkOperationTree(operation);

        boolean tempPixelLiteral = PixelLiteral;
        boolean tempPercentageLiteral = PercentageLiteral;
        // reset literals
        PixelLiteral = false;
        PercentageLiteral = false;

        if (tempPixelLiteral) {
            return ExpressionType.PIXEL;
        } else if (tempPercentageLiteral) {
            return ExpressionType.PERCENTAGE;
        } else {
            return ExpressionType.SCALAR;
        }
    }

    /**
     * This recursive method checks the given operation and its sub operations for errors.
     *
     * @param operation The operation to check for errors.
     */
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

        UpdateDuplicateLiterals(operation, left, right);

        checkOperationCalculationRules(operation, left, right);
    }

    /**
     * This method checks the calculation rules for the given operation and its operands.
     * The rules are as follows:
     * - The left and right side of an addition or subtraction operation must be a pixel or percentage literal or another operation.
     * - If the left side of a multiplication operation is a scalar literal, the right side must be a pixel or percentage literal or another operation.
     * - If the right side of a multiplication operation is a scalar literal, the left side must be a pixel or percentage literal or another operation.
     * - A multiplication operation can't have multiple scalar literals as operands.
     * - A multiplication operation can't have multiple operands.
     * If any of these rules are violated, the operation will be marked as invalid.
     *
     * @param operation The operation to check the calculation rules for.
     * @param left The left operand of the operation.
     * @param right The right operand of the operation.
     */
    private void checkOperationCalculationRules(Operation operation, Expression left, Expression right) {
        if (operation instanceof AddOperation || operation instanceof SubtractOperation) {
            if (!isValidInOperation(left) || !isValidInOperation(right)) {
                operation.setError("In a addition or subtraction operation, there must be a pixel or percentage literal or another operation");
            }
        } else if (operation instanceof MultiplyOperation) {
            if (left instanceof ScalarLiteral) {
                if (right instanceof ScalarLiteral) {
                    operation.setError("Multiplication can't have multiple scalar literals as operands");
                }
                if (!isValidInOperation(right)) {
                    operation.setError("In a multiplication operation, there must be a pixel or percentage literal or another operation");
                }
            }
            if (right instanceof ScalarLiteral) {
                if (!isValidInOperation(left)) {
                    operation.setError("In a multiplication operation, there must be a pixel or percentage literal or another operation");
                }
            }
            if (isValidInOperation(left) && isValidInOperation(right)) {
                operation.setError("In a multiplication operation, there can't be multiple operands with literals");
            }
        }
    }

    /**
     * This method updates the PixelLiteral and PercentageLiteral variables based on the given operation and its operands.
     *
     * @param operation The operation to update the literals for.
     * @param left The left operand of the operation.
     * @param right The right operand of the operation.
     */
    private void UpdateDuplicateLiterals(Operation operation, Expression left, Expression right) {
        if (left instanceof PixelLiteral || right instanceof PixelLiteral) {
            PixelLiteral = true;
        }
        if (right instanceof PercentageLiteral || left instanceof PercentageLiteral) {
            PercentageLiteral = true;
        }
        if (PixelLiteral && PercentageLiteral) {
            operation.setError("Pixel and percentage literals can't be used in the same operation");
        }
    }

    /**
     * This method returns an expression from a variable reference.
     * If the variable reference is not found in the current scope, it will return null.
     * If the variable reference is found, it will return the expression type of the variable reference.
     * The values inside the expressions are not used within the checker, therefor the values are arbitrary.
     *
     * @param variableReference The variable reference to get the expression from.
     * @return The expression of the variable reference.
     */
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

    /**
     * This method checks if the given expression is valid within an operation.
     * Valid expressions are instances of PixelLiteral, PercentageLiteral, AddOperation, SubtractOperation, or MultiplyOperation.
     * This is because these expressions are always valid within an operation. With minor exceptions when using multiplication.
     *
     * @param expression The expression to be checked for validity within an operation.
     * @return A boolean value indicating whether the expression is valid within an operation.
     */
    private boolean isValidInOperation(Expression expression){
        return expression instanceof PixelLiteral
                || expression instanceof PercentageLiteral
                || expression instanceof AddOperation
                || expression instanceof SubtractOperation
                || expression instanceof MultiplyOperation;
    }

    private void checkOperand(Expression operand) {
        if (operand instanceof ColorLiteral) {
            operand.setError("Color literals cannot be used in operations");
        }
    }

}
