package nl.han.ica.icss.ast;

import java.util.ArrayList;

public class Function extends ASTNode{

    public PropertyName name;
    public ArrayList<ASTNode> body = new ArrayList<>();

    public Function() { }

    public Function(PropertyName name) {
        this.name = name;
    }

    public Function(PropertyName name, ArrayList<ASTNode> body) {
        this.name = name;
        this.body = body;
    }

    @Override
    public String getNodeLabel() {
        return "Function";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.add(name);
        children.addAll(body);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof PropertyName) {
            name = (PropertyName) child;
        } else {
            body.add(child);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Function function = (Function) o;
        return name.equals(function.name) &&
                body.equals(function.body);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + body.hashCode();
    }

    @Override
    public ASTNode removeChild(ASTNode child) {
        if (child instanceof PropertyName) {
            name = null;
        } else {
            body.remove(child);
        }
        return this;
    }


}
