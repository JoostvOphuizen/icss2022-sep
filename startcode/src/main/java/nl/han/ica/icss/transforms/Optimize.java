package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.Stylesheet;

/**
 * Optimises the AST
 *
 * - Removes declarations that are not used, such as double declarations
 */
public class Optimize {

    public void apply(AST ast) {
        applyStylesheet(ast);
    }

    /**
     * Find the stylesheet in the AST
     *
     * @param ast The AST to find the stylesheet in
     */
    private void applyStylesheet(AST ast) {
        applyStyleRules(ast.root);
    }

    /**
     * Find the style rules in the stylesheet
     *
     * @param stylesheet The stylesheet to find the style rules in
     */
    private void applyStyleRules(Stylesheet stylesheet) {
        for (int i = 0; i < stylesheet.body.size(); i++) {
            if (stylesheet.body.get(i) instanceof Stylerule) {
                optimiseStyleRule((Stylerule) stylesheet.body.get(i));
            }
        }

    }

    /**
     * Optimise the style rule
     *
     * @param stylerule The style rule to optimize
     */
    private void optimiseStyleRule(Stylerule stylerule) {
        removeUnusedDeclarations(stylerule);
    }

    /**
     * Remove unused declarations from the style rule
     * Example: p { width: 100px; width: 200px; } -[will be optimized to]-> p { width: 200px; }
     *
     * @param stylerule The style rule to remove unused declarations from
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
