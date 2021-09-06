package Evaluator.Treenode;


public class And extends BinaryTreeNode {
    public And(TreeNode left, TreeNode right) {
        super(left,right);
    }
    public String getNodeType(){return "And";}

    @Override
    public String toString() {
        return "(" + getLeftChild() + " & " + getRightChild() + ")";
    }
}
