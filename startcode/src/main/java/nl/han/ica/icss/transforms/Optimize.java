package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.Stylesheet;

/**
 * This class is responsible for optimizing the AST (Abstract Syntax Tree).
 * It removes unused declarations such as double declarations.
 */
public class Optimize {

    /**
     * This method applies the optimization process to the given AST.
     *
     * @param ast The AST to be optimized.
     */
    public void apply(AST ast) {
        applyStylesheet(ast);
    }

    /**
     * This method finds the stylesheet in the given AST and applies the optimization process to it.
     *
     * @param ast The AST where the stylesheet is to be found.
     */
    private void applyStylesheet(AST ast) {
        applyStyleRules(ast.root);
    }

    /**
     * This method finds the style rules in the given stylesheet and applies the optimization process to them.
     *
     * @param stylesheet The stylesheet where the style rules are to be found.
     */
    private void applyStyleRules(Stylesheet stylesheet) {
        for (int i = 0; i < stylesheet.body.size(); i++) {
            if (stylesheet.body.get(i) instanceof Stylerule) {
                optimiseStyleRule((Stylerule) stylesheet.body.get(i));
            }
        }
    }

    /**
     * This method optimizes the given style rule.
     *
     * @param stylerule The style rule to be optimized.
     */
    private void optimiseStyleRule(Stylerule stylerule) {
        removeUnusedDeclarations(stylerule);
    }

    /**
     * This method removes unused declarations from the given style rule.
     * For example, if a style rule is "p { width: 100px; width: 200px; }", it will be optimized to "p { width: 200px; }".
     *
     * @param stylerule The style rule from which unused declarations are to be removed.
     */
    private void removeUnusedDeclarations(Stylerule stylerule) {
        for (int i = 0; i < stylerule.body.size(); i++) {
            if (!(stylerule.body.get(i) instanceof Declaration)) {
                continue;
            }
            Declaration declaration = (Declaration) stylerule.body.get(i);
            for (int j = i + 1; j < stylerule.body.size(); j++) {
                if (!(stylerule.body.get(j) instanceof Declaration)) {
                    continue;
                }
                Declaration otherDeclaration = (Declaration) stylerule.body.get(j);
                if (declaration.property.name.equals(otherDeclaration.property.name)) {
                    stylerule.body.remove(i);
                    i--;
                    break;
                }
            }
        }
    }
}