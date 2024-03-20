package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Evaluator implements Transform {

    private IHANLinkedList<HashMap<String, Literal>> variableValues;

    private final LinkedList<ASTNode> NodesToRemove = new LinkedList<>();

    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues = new HANLinkedList<>();
        Stylesheet stylesheet = ast.root;
        evaluateStyleSheet(stylesheet);

        for (ASTNode node : NodesToRemove) {
            stylesheet.removeChild(node);
        }
    }

    private Literal getLiteralValueFromVariable(String name) {
        for (HashMap<String, Literal> map : variableValues) {
            if (map.containsKey(name)) {
                return map.get(name);
            }
        }
        return null;
    }

    private void evaluateStyleSheet(Stylesheet stylesheet) {
        variableValues.addFirst(new HashMap<>());
        for (ASTNode node : stylesheet.getChildren()) {
            if (node instanceof VariableAssignment) {
                evaluateVariableAssignment((VariableAssignment) node);
            } else if (node instanceof Stylerule) {
                evaluateStylerule((Stylerule) node);
            }
        }
        variableValues.removeFirst();
    }

    private void evaluateBody(ArrayList<ASTNode> body) {
        variableValues.addFirst(new HashMap<>());
        for (ASTNode node : body) {
            if (node instanceof VariableAssignment) {
                evaluateVariableAssignment((VariableAssignment) node);
            } else if (node instanceof IfClause) {
                evaluateIfClause((IfClause) node);
            } else if (node instanceof Declaration) {
                evaluateDeclaration((Declaration) node);
            }
        }
        variableValues.removeFirst();
    }

    private void evaluateStylerule(Stylerule stylerule) {

    }

    private void evaluateIfClause(IfClause ifClause) {
        // todo
    }

    private boolean evaluateExpression(Expression conditionalExpression) {
        return false; //TODO
    }

    private void evaluateDeclaration(Declaration declaration) {
        if (declaration.expression instanceof Operation) {
            evaluateOperation((Operation) declaration.expression);
        }
    }

    private void evaluateOperation(Operation operation) {
        System.out.println(operation.calculate());
    }

    private void evaluateVariableAssignment(VariableAssignment assignment) {
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
            evaluateOperation((Operation) assignment.expression);
        }
        NodesToRemove.add(assignment);
    }


}
