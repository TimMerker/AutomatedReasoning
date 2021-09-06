package Evaluator.Treenode;

public class Or extends BinaryTreeNode {
    public Or(TreeNode left, TreeNode right) {
        super(left, right);
    }

    public String getNodeType() {
        return "Or";
    }

    @Override
    public String toString() {
        return "(" + getLeftChild() + " | " + getRightChild() + ")";
    }

}
