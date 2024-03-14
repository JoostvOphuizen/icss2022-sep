public class NumberAST extends ExpressionAST{
    private int value;

    public NumberAST(int value) {
        this.value = value;
    }

    @Override
    int eval() {
        return value;
    }
}
