import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws IOException {
        // Setup pipeline
        CharStream input = CharStreams.fromStream(new FileInputStream("C:\\dev\\icss2022-sep\\CompilerTestOmgeving\\src\\example.cfg"));
        ExpressionsLexer lexer = new ExpressionsLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionsParser parser = new ExpressionsParser(tokens);

        // Get tree
        ExpressionsParser.ExprContext tree = parser.expr();
        System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        ExpressionReader reader = new ExpressionReader();
        walker.walk(reader, tree);

//        Stack<String> stack = reader.getStack();
//        int result = reader.calculate(stack);
//        System.out.println("Result: " + result);
    }
}
