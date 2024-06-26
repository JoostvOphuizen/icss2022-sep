package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;

public class AddOperation extends Operation {

    @Override
    public String getNodeLabel() {
        return "Add";
    }

    @Override
    public Literal calculate() {
        Expression calculatedLhs = lhs.calculate();
        Expression calculatedRhs = rhs.calculate();

        if (calculatedLhs instanceof PixelLiteral && calculatedRhs instanceof PixelLiteral) {
            return new PixelLiteral(((PixelLiteral) calculatedLhs).value + ((PixelLiteral) calculatedRhs).value);
        }
        if (calculatedLhs instanceof PercentageLiteral && calculatedRhs instanceof PercentageLiteral) {
            return new PercentageLiteral(((PercentageLiteral) calculatedLhs).value + ((PercentageLiteral) calculatedRhs).value);
        }
        if (calculatedLhs instanceof ScalarLiteral && calculatedRhs instanceof ScalarLiteral) {
            return new ScalarLiteral(((ScalarLiteral) calculatedLhs).value + ((ScalarLiteral) calculatedRhs).value);
        }
        return null;
    }
}
