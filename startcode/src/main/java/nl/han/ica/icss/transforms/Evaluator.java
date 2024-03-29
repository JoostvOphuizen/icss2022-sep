package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * This class evaluates the AST and replaces all variable references with their values
 * It also evaluates all operations and replaces them with their result
 *
 * @author Joost van Ophuizen
 * @version 1.0
 */
public class Evaluator implements Transform {

    private IHANLinkedList<HashMap<String, Literal>> variableValues;

    private final LinkedList<ASTNode> NodesToRemove = new LinkedList<>();
    private final IHANLinkedList<HashMap<String, Function>> functionValues = new HANLinkedList<>();

    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    Optimize optimise = new Optimize();

    @Override
    public void apply(AST ast) {
        variableValues = new HANLinkedList<>();
        Stylesheet stylesheet = ast.root;
        evaluateStyleSheet(stylesheet);

        // Remove all nodes that are in the NodesToRemove list, within the stylesheet scope
        for (ASTNode node : NodesToRemove) {
            stylesheet.removeChild(node);
        }

        // Optimise the stylesheet
        optimise.apply(ast);
    }

    /**
     * Gets the value of a variable from the variableValues list
     *
     * @param name The name of the variable
     * @return The value of the variable
     */
    private Literal getLiteralValueFromVariable(String name) {
        for (HashMap<String, Literal> map : variableValues) {
            if (map.containsKey(name)) {
                return map.get(name);
            }
        }
        return null;
    }

    /**
     * Evaluates a stylesheet and adds the correct body to the nodesToAdd list
     *
     * @param stylesheet The stylesheet to evaluate
     */
    private void evaluateStyleSheet(Stylesheet stylesheet) {
        variableValues.addFirst(new HashMap<>());
        for (ASTNode node : stylesheet.getChildren()) {
            if (node instanceof VariableAssignment) {
                evaluateVariableAssignment((VariableAssignment) node);
            } else if (node instanceof Stylerule) {
                evaluateStylerule((Stylerule) node);
            } else if (node instanceof Function) {
                evaluateFunction((Function) node);
            }
        }
        variableValues.removeFirst();
    }

    /**
     * Evaluates a function and adds the correct body to the nodesToAdd list
     *
     * @param function The function to evaluate
     */
    private void evaluateFunction(Function function) {
        functionValues.addFirst(new HashMap<>());
        evaluateNode(function, function::removeChild);
        functionValues.getFirst().put(function.name.name, function);
        NodesToRemove.add(function);
    }

    /**
     * Evaluates a function and adds the correct body to the nodesToAdd list
     *
     * @param function The function to evaluate
     */
    private LinkedList<ASTNode> evaluateBody(ArrayList<ASTNode> body, Consumer<ASTNode> removeChildAction) {
        variableValues.addFirst(new HashMap<>());
        LinkedList<ASTNode> nodesToAdd = new LinkedList<>();
        LinkedList<ASTNode> nodesToRemove = new LinkedList<>();
        for (ASTNode node : body) {
            if (node instanceof VariableAssignment) {
                nodesToRemove.addAll(evaluateVariableAssignment((VariableAssignment) node));
            } else if (node instanceof IfClause) {
                evaluateIfClause((IfClause) node, nodesToAdd, nodesToRemove);
            } else if (node instanceof Declaration) {
                evaluateDeclaration((Declaration) node);
            } else if (node instanceof FunctionCall) {
                evaluateFunctionCall(node, nodesToAdd, nodesToRemove);
            }
        }
        for (ASTNode node : nodesToRemove) {
            removeChildAction.accept(node);
        }
        variableValues.removeFirst();
        return nodesToAdd;
    }

    /**
     * Evaluates a function call and adds the correct body to the nodesToAdd list
     *
     * @param node          The node to evaluate
     * @param body          The list to add the nodes to
     * @param nodesToRemove The list to remove the nodes from
     */
    private void evaluateFunctionCall(ASTNode node, LinkedList<ASTNode> body, LinkedList<ASTNode> nodesToRemove) {
        FunctionCall functionCall = (FunctionCall) node;
        Function function = null;
        for (HashMap<String, Function> map : functionValues) {
            if (map.containsKey(functionCall.name.name)) {
                function = map.get(functionCall.name.name);
                break;
            }
        }

        if (function == null) {
            return;
        }
        body.addAll(function.body);
        nodesToRemove.add(functionCall);
    }

    /**
     * Evaluates a node and adds the correct body to the nodesToAdd list
     *
     * @param astNode          The node to evaluate
     * @param removeChildAction The action to remove a child from the node
     */
    private void evaluateNode(ASTNode astNode, Consumer<ASTNode> removeChildAction) {
        LinkedList<ASTNode> nodesToAdd = evaluateBody(astNode.getChildren(), removeChildAction);

        for (ASTNode node : nodesToAdd) {
            astNode.addChild(node);
        }

        // Check if the astNode has an if-clause
        // Use a temporary variable to prevent ConcurrentModificationException
        boolean hasIfClause = false;
        for (ASTNode node : astNode.getChildren()) {
            if (node instanceof IfClause) {
                hasIfClause = true;
                break;
            }
        }
        if (hasIfClause){
            // If the astNode has an if-clause, evaluate the body again
            evaluateNode(astNode, removeChildAction);
        } else {
            // If the astNode doesn't have an if-clause, evaluate the body one last time
            LinkedList<ASTNode> nodesToAdd2 = evaluateBody(astNode.getChildren(), removeChildAction);
            for (ASTNode node : nodesToAdd2) {
                astNode.addChild(node);
            }
        }
    }

    /**
     * Evaluates a stylerule and adds the correct body to the nodesToAdd list
     *
     * @param stylerule The stylerule to evaluate
     */
    private void evaluateStylerule(Stylerule stylerule) {
        evaluateNode(stylerule, stylerule::removeChild);
    }

    /**
     * Evaluates an if-clause and adds the correct body to the nodesToAdd list
     *
     * @param ifClause     The if-clause to evaluate
     * @param nodesToAdd   The list to add the nodes to
     * @param nodesToRemove The list to remove the nodes from
     */
    private void evaluateIfClause(IfClause ifClause, LinkedList<ASTNode> nodesToAdd, LinkedList<ASTNode> nodesToRemove) {
        replaceVariableReferenceWithLiteral(ifClause, ifClause.conditionalExpression);
        LinkedList<ASTNode> tempNodesToAdd = new LinkedList<>();
        if (evaluateBooleanExpression(ifClause.conditionalExpression)){
            tempNodesToAdd.addAll(ifClause.body);
        } else if (ifClause.elseClause != null) {
            tempNodesToAdd.addAll(ifClause.elseClause.body);
        }
        nodesToRemove.add(ifClause);
        if (ifClause.elseClause != null){
            nodesToRemove.add(ifClause.elseClause);
        }
        nodesToAdd.addAll(tempNodesToAdd);
    }

    /**
     * Evaluates a boolean expression and returns the result
     *
     * @param conditionalExpression The expression to evaluate
     * @return The result of the expression
     */
    private boolean evaluateBooleanExpression(Expression conditionalExpression) {
        if (conditionalExpression instanceof BoolLiteral) {
            return ((BoolLiteral) conditionalExpression).value;
        }
        return false;
    }

    /**
     * Evaluates a declaration and replaces the expression with a literal if possible
     *
     * @param declaration The declaration to evaluate
     */
    private void evaluateDeclaration(Declaration declaration) {
        if (declaration.expression instanceof Operation) {
            declaration.expression = evaluateOperation((Operation) declaration.expression);
        }
        if (declaration.expression instanceof VariableReference) {
            Literal literal = getLiteralValueFromVariable(((VariableReference) declaration.expression).name);
            if (literal != null) {
                declaration.expression = literal;
            }
        }
    }

    /**
     * Evaluates an operation and returns the result
     *
     * @param operation The operation to evaluate
     * @return The result of the operation
     */
    private Literal evaluateOperation(Operation operation) {
        // replace all variable references with their values
        recursiveThroughOperationTree(operation);

        return operation.calculate();
    }

    /**
     * Recursively goes through the operation tree and replaces all variable references with their values
     *
     * @param operation The operation to go through
     */
    private void recursiveThroughOperationTree(Operation operation) {
        // Check left child
        if (operation.lhs instanceof Operation) {
            recursiveThroughOperationTree((Operation) operation.lhs);
        } else if (operation.lhs instanceof VariableReference) {
            replaceVariableReferenceWithLiteral(operation, operation.lhs);
        }
        // Check right child
        if (operation.rhs instanceof Operation) {
            recursiveThroughOperationTree((Operation) operation.rhs);
        } else if (operation.rhs instanceof VariableReference) {
            replaceVariableReferenceWithLiteral(operation, operation.rhs);
        }
    }

    /**
     * Replaces a variable reference with a literal if the variable is found in the variableValues list
     *
     * @param operation   The operation to replace the variable reference in
     * @param expression  The expression to replace
     */
    private void replaceVariableReferenceWithLiteral(Operation operation, Expression expression) {
        if (expression instanceof VariableReference) {
            Literal literal = getLiteralValueFromVariable(((VariableReference) expression).name);
            if (literal != null) {
                if (operation.lhs == expression) {
                    operation.lhs = literal;
                } else if (operation.rhs == expression) {
                    operation.rhs = literal;
                }
            }
        }
    }

    /**
     * Replaces a variable reference with a literal if the variable is found in the variableValues list
     *
     * @param operation   The operation to replace the variable reference in
     * @param expression  The expression to replace
     */
    private void replaceVariableReferenceWithLiteral(IfClause operation, Expression expression) {
        if (expression instanceof VariableReference) {
            Literal literal = getLiteralValueFromVariable(((VariableReference) expression).name);
            if (literal != null) {
                if (operation.conditionalExpression == expression) {
                    operation.conditionalExpression = literal;
                }
            }
        }
    }

    /**
     * Evaluates a variable assignment and adds the value to the variableValues list
     *
     * @param assignment The variable assignment to evaluate
     */
    private LinkedList<ASTNode> evaluateVariableAssignment(VariableAssignment assignment) {
        LinkedList<ASTNode> nodesToRemove = new LinkedList<>();
        if (assignment.expression instanceof BoolLiteral){
            variableValues.getFirst().put(assignment.name.name, new BoolLiteral(((BoolLiteral) assignment.expression).value));
        } else if (assignment.expression instanceof ColorLiteral){
            variableValues.getFirst().put(assignment.name.name, new ColorLiteral(((ColorLiteral) assignment.expression).value));
        } else if (assignment.expression instanceof PixelLiteral){
            variableValues.getFirst().put(assignment.name.name, new PixelLiteral(((PixelLiteral) assignment.expression).value));
        } else if (assignment.expression instanceof PercentageLiteral){
            variableValues.getFirst().put(assignment.name.name, new PercentageLiteral(((PercentageLiteral) assignment.expression).value));
        } else if (assignment.expression instanceof ScalarLiteral){
            variableValues.getFirst().put(assignment.name.name, new ScalarLiteral(((ScalarLiteral) assignment.expression).value));
        } else if (assignment.expression instanceof VariableReference){
            if (!variableValues.getFirst().containsKey(((VariableReference) assignment.expression).name)){
                assignment.setError("Variable not found");
            } else {
                variableValues.getFirst().put(assignment.name.name, variableValues.getFirst().get(((VariableReference) assignment.expression).name));
            }
        } else if (assignment.expression instanceof Operation){
            Literal temp = evaluateOperation((Operation) assignment.expression);
            variableValues.getFirst().put(assignment.name.name, temp);
        }
        nodesToRemove.add(assignment);
        NodesToRemove.addAll(nodesToRemove);
        return nodesToRemove;
    }


}
