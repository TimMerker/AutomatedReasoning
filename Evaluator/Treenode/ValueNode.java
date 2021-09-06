package Evaluator.Treenode;

public abstract class ValueNode<T> implements TreeNode {
    private boolean value;
    private T variable;

    public ValueNode() {
    }
    ValueNode(T variable) {
        this.variable = variable;
    }

    public ValueNode(boolean value, T variable) {
        this.value = value;
        this.variable = variable;
    }

    boolean getValue() {
        return value;
    }

    T getVariable(){
        return variable;
    }
    public void setValue(boolean value) {
        this.value = value;
    }
    public void setVariable(T variable){
        this.variable = variable;
    }

    @Override
    public String toString() {
        return getNodeType() + "(" + getValue() + ")";
    }
}

