package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;

public class SubtractOperation extends Operation {

    @Override
    public String getNodeLabel() {
        return "Subtract";
    }

    @Override
    public Literal calculate() {
        Expression calculatedLhs = lhs.calculate();
        Expression calculatedRhs = rhs.calculate();

        if (calculatedLhs instanceof PixelLiteral && calculatedRhs instanceof PixelLiteral) {
            return new PixelLiteral(((PixelLiteral) calculatedRhs).value - ((PixelLiteral) calculatedLhs).value);
        }
        if (calculatedLhs instanceof PercentageLiteral && calculatedRhs instanceof PercentageLiteral) {
            return new PercentageLiteral(((PercentageLiteral) calculatedRhs).value - ((PercentageLiteral) calculatedLhs).value);
        }
        if (calculatedLhs instanceof ScalarLiteral && calculatedRhs instanceof ScalarLiteral) {
            return new ScalarLiteral(((ScalarLiteral) calculatedRhs).value - ((ScalarLiteral) calculatedLhs).value);
        }
        return null;
    }
}
