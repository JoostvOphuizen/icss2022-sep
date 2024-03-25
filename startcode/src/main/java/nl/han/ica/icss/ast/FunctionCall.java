package nl.han.ica.icss.ast;

import java.util.ArrayList;

public class FunctionCall extends ASTNode {

    public FunctionName name;

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
        return children;
    }

    public FunctionName getName() {
        return name;
    }
}
