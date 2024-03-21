package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Stylerule;

public class Generator {

	public String generate(AST ast) {
		return generateStylesheet(ast);
	}

	/**
	 * Generate a string representation of the given AST
	 *
	 * @param ast The AST to generate a string representation of
	 * @return A string representation of the given AST
	 */
	private String generateStylesheet(AST ast) {
        return generateStylerules(ast);
	}

	/**
	 * Generate a string representation of all stylerules in the given AST
	 *
	 * @param ast The AST to generate a string representation of
	 * @return A string representation of all stylerules in the given AST
	 */
	private String generateStylerules(AST ast) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < ast.root.body.size(); i++) {
			if (ast.root.body.get(i) instanceof Stylerule) {
				result.append(generateStylerule((Stylerule) ast.root.body.get(i)));
			}
		}

		return result.toString();
	}

	/**
	 * Generate a string representation of a stylerule
	 *
	 * @param stylerule The stylerule to generate a string representation of
	 * @return A string representation of the given stylerule
	 */
	private String generateStylerule(Stylerule stylerule) {
		StringBuilder result = new StringBuilder();

		result.append(stylerule.selectors.get(0).toString()).append(" {\n");
		for (int i = 0; i < stylerule.body.size(); i++) {
			result.append("  ").append(stylerule.body.get(i).toString()).append("\n");
		}
		result.append("}\n");

		return result.toString();
	}

	
}
