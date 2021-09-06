package Evaluator.Treenode;

public class Falsum extends ValueNode<String> {
    private boolean value;

    public Falsum() {
        boolean value = false;
    }

    public String getNodeType() {
        return "Falsum";
    }

    @Override
    public String toString() { return "0";
    }
}
