import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;

public class ExpressionReader implements ExpressionsListener {

    private Stack<String> output = new Stack<>();
    private Stack<String> operators = new Stack<>();

    @Override
    public void enterExpr(ExpressionsParser.ExprContext ctx) {
        //System.out.println("enterExpr: " + ctx.getText());
    }

    @Override
    public void exitExpr(ExpressionsParser.ExprContext ctx) {
//        while (!operators.isEmpty()) {
//            output.push(operators.pop());
//        }
//        System.out.println("exitExpr: " + output);
    }

    @Override
    public void enterTerm(ExpressionsParser.TermContext ctx) {

    }

    @Override
    public void exitTerm(ExpressionsParser.TermContext ctx) {
        //System.out.println("exitTerm: " + ctx.getText());
    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {
//        String token = terminalNode.getText();
//        if (token.matches("\\d+")) {
//            output.push(token);
//        } else {
//            while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
//                output.push(operators.pop());
//            }
//            operators.push(token);
//        }
    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }

    private int precedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }

    public Stack<String> getPostfix() {
        return output;
    }

    public int calculate(Stack<String> stack) {
        Stack<Integer> resultStack = new Stack<>();

        for (String token : stack) {
            if (token.matches("\\d+")) {
                resultStack.push(Integer.parseInt(token));
                continue;
            }

            if (resultStack.size() < 2) {
                throw new IllegalStateException("Insufficient operands for operator: " + token);
            }
            int operand2 = resultStack.pop();
            int operand1 = resultStack.pop();
            switch (token) {
                case "+":
                    resultStack.push(operand1 + operand2);
                    break;
                case "-":
                    resultStack.push(operand1 - operand2);
                    break;
                case "*":
                    resultStack.push(operand1 * operand2);
                    break;
                case "/":
                    resultStack.push(operand1 / operand2);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + token);
            }
        }

        if (resultStack.size() != 1) {
            throw new IllegalStateException("Invalid expression");
        }

        return resultStack.pop();
    }

    public Stack<String> getStack() {
        return output;
    }
}
