package nl.han.ica.icss.ast;

import nl.han.ica.icss.parser.ExpressionList;

import java.util.ArrayList;

public class FunctionCall extends ASTNode {

    public FunctionName name;
    public ExpressionList parameters;

    public FunctionCall() { }

    public FunctionCall(FunctionName name) {
        this.name = name;
    }

    @Override
    public String getNodeLabel() {
        return "FunctionCall";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.add(name);
        children.add(parameters);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof FunctionName) {
            name = (FunctionName) child;
        } else if (child instanceof ExpressionList) {
            parameters = (ExpressionList) child;
        }
        return this;
    }

    public FunctionName getName() {
        return name;
    }
}
