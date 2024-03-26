package nl.han.ica.icss.ast;

public class ParameterName extends ASTNode {

    public String name;

    public ParameterName(String name) {
        this.name = name;
    }

    @Override
    public String getNodeLabel() {
        return "ParameterName";
    }

    @Override
    public String toString() {
        return name;
    }
}
