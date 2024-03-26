package nl.han.ica.icss.ast;

import java.util.ArrayList;

public class ParameterList extends ASTNode {

    public ArrayList<Parameter> parameters = new ArrayList<>();

    public ParameterList() { }

    public ParameterList(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getNodeLabel() {
        return "ParameterList";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.addAll(parameters);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof Parameter) {
            parameters.add((Parameter) child);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ParameterList that = (ParameterList) o;
        return parameters.equals(that.parameters);
    }

    @Override
    public int hashCode() {
        return parameters.hashCode();
    }

    @Override
    public ASTNode removeChild(ASTNode child) {
        if (child instanceof Parameter) {
            parameters.remove(child);
        }
        return this;
    }
}
