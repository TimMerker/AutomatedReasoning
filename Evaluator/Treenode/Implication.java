package Evaluator.Treenode;

public class Implication extends BinaryTreeNode {
    public Implication(TreeNode left, TreeNode right) {
        super(left, right);
    }

    public String getNodeType() {
        return "Implies";
    }

    @Override
    public String toString() {
        return "(" + getLeftChild() + " -> " + getRightChild() + ")";
    }
}

