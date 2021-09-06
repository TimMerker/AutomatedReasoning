package Evaluator.Treenode;

public class True extends UnaryTreeNode {
    public True(TreeNode child) {
        super(child);
    }

    public String getNodeType() {
        return "True";
    }

    @Override
    public String toString() {
        return this.getChild() + " = 1";
    }
}
