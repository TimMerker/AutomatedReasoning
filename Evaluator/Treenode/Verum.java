package Evaluator.Treenode;

public class Verum extends ValueNode<String> {
    private boolean value;

    public Verum() {
        boolean value = true;
    }

    public String getNodeType() {
        return "Verum";
    }

    @Override
    public String toString() { return "1";
    }
}
