package Evaluator.Treenode;

public class False extends UnaryTreeNode {
    public False(TreeNode child) {
        super(child);
    }

    public String getNodeType() {
        return "False";
    }

    @Override
    public String toString() {
        return this.getChild() + " = 0";
    }
}
