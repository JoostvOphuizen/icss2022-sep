package nl.han.ica.icss.ast;

public abstract class Expression extends ASTNode {

    public abstract Literal calculate();
}
