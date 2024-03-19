package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Evaluator implements Transform {

    private IHANLinkedList<HashMap<String, Literal>> variableValues;

    private LinkedList<ASTNode> NodesToRemove = new LinkedList<>();

    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues = new HANLinkedList<>();
        Stylesheet stylesheet = (Stylesheet) ast.root;
        evaluateStyleSheet(stylesheet);

        for (ASTNode node : NodesToRemove) {
            stylesheet.removeChild(node);
        }
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
        // if ifclause is false remove it
        if (ifClause.conditionalExpression instanceof BoolLiteral) {
            if (!((BoolLiteral) ifClause.conditionalExpression).value) {
                if (ifClause.elseClause != null) {
                    evaluateBody(ifClause.elseClause.body);
                }
                NodesToRemove.add(ifClause);
            }
        } else if (evaluateExpression(ifClause.conditionalExpression)) {
            evaluateBody(ifClause.body);
        } else if (ifClause.elseClause != null) {
            evaluateBody(ifClause.elseClause.body);
        }
    }

    private boolean evaluateExpression(Expression conditionalExpression) {
        return false; //TODO
    }

    private void evaluateDeclaration(Declaration declaration) {

    }

    private void evaluateVariableAssignment(VariableAssignment variableAssignment) {
        NodesToRemove.add(variableAssignment);
    }


}
