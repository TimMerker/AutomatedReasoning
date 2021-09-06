package Evaluator.Treenode;

public class Variable extends ValueNode<String> {

    public Variable(String value) {
        super(value);
    }

    public String getNodeType() {
        return "Variable";
    }

    @Override
    public String toString() {
        return getVariable();
    }
}