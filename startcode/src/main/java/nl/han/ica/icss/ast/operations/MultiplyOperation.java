package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;

public class MultiplyOperation extends Operation {

    @Override
    public String getNodeLabel() {
        return "Multiply";
    }

    @Override
    public Literal calculate() {
        Literal calculatedLhs = lhs.calculate();
        Literal calculatedRhs = rhs.calculate();

        if ((calculatedLhs instanceof PixelLiteral || calculatedLhs instanceof ScalarLiteral) && (calculatedRhs instanceof PixelLiteral || calculatedRhs instanceof ScalarLiteral)) {
            return new PixelLiteral(calculatedLhs.getValue() * calculatedRhs.getValue());
        }
        if ((calculatedLhs instanceof PercentageLiteral || calculatedLhs instanceof ScalarLiteral) && (calculatedRhs instanceof PercentageLiteral || calculatedRhs instanceof ScalarLiteral)) {
            return new PercentageLiteral(calculatedLhs.getValue() * calculatedRhs.calculate().getValue());
        }
        return null;
    }
}
