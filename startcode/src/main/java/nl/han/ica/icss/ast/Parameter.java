package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.types.ExpressionType;

public class Parameter extends Literal {

    public ParameterName name;

    public Parameter() {
        super();
    }

    public Parameter(ParameterName name, Expression value) {
        super();
        this.name = name;
    }

    @Override
    public String getNodeLabel() {
        return "Parameter (" + name.toString() + ")";
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if (child instanceof ParameterName) {
            name = (ParameterName) child;
        }
        return this;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public ASTNode removeChild(ASTNode child) {
        if (child instanceof ParameterName) {
            name = null;
        }
        return this;
    }

    @Override
    public Literal calculate() {
        return null;
    }

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.PARAMETER_REFERENCE;
    }
}
