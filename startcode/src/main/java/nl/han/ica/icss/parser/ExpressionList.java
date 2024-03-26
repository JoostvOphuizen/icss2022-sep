package nl.han.ica.icss.parser;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;

import java.util.ArrayList;

public class ExpressionList extends ASTNode {

    ArrayList<Expression> expressions = new ArrayList<>();

    public ExpressionList() { }

    public ExpressionList(ArrayList<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String getNodeLabel() {
        return "ExpressionList";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.addAll(expressions);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof Expression) {
            expressions.add((Expression) child);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExpressionList that = (ExpressionList) o;
        return expressions.equals(that.expressions);
    }

    @Override
    public int hashCode() {
        return expressions.hashCode();
    }

    @Override
    public ASTNode removeChild(ASTNode child) {
        if (child instanceof Expression) {
            expressions.remove(child);
        }
        return this;
    }

    @Override
    public String toString() {
        return expressions.toString();
    }

}
