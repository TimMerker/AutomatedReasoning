package Evaluator.Treenode;

public class Equivalence extends BinaryTreeNode {
    public Equivalence(TreeNode left, TreeNode right) {super(left, right);}

    @Override
    public String getNodeType() {
        return "Equiv";
    }

    @Override
    public String toString() {
        return "(" + getLeftChild() + " <-> " + getRightChild() + ")";
    }
}
