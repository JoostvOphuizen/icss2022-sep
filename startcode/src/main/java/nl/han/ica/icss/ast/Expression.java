package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.types.ExpressionType;

public abstract class Expression extends ASTNode {

    public abstract Literal calculate();

    public ExpressionType getExpressionType() {
        return ExpressionType.UNDEFINED;
    }
}
