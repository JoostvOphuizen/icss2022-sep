package nl.han.ica.icss.ast;

public class FunctionName extends PropertyName {

    public FunctionName(String text) {
        super(text);
    }

    @Override
    public String getNodeLabel() {
        return "FunctionName: (" + name + ")";
    }
}
