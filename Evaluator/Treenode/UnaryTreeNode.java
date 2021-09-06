package Evaluator.Treenode;

public abstract class UnaryTreeNode implements TreeNode {

    private TreeNode child;

    UnaryTreeNode(TreeNode child) {
        this.child = child;
    }
    public TreeNode getChild(){return child;}

    void setChild(TreeNode child) {this.child = child;}

    @Override
    public String toString(){return getNodeType() + getChild();}
}
