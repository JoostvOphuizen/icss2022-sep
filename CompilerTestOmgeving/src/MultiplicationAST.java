public class MultiplicationAST extends ExpressionAST{
    private ExpressionAST left;
    private ExpressionAST right;

    public MultiplicationAST(ExpressionAST left, ExpressionAST right) {
        this.left = left;
        this.right = right;
    }

    public ExpressionAST getLeft() {
        return left;
    }

    public ExpressionAST getRight() {
        return right;
    }

    @Override
    int eval() {
        return left.eval() * right.eval();
    }
}
