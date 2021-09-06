package Evaluator.Treenode;

public abstract class BinaryTreeNode implements TreeNode {
    private TreeNode leftChild;
    private TreeNode rightChild;

    BinaryTreeNode(TreeNode leftChild, TreeNode rightChild){
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public TreeNode getLeftChild(){return leftChild;}

    void setLeftChild(TreeNode leftChild){this.leftChild = leftChild;}

    public TreeNode getRightChild(){return rightChild;}

    void setRightChild(TreeNode leftChild){this.rightChild = leftChild;}

   @Override
    public String toString(){
        return getNodeType() +"(" + getLeftChild() + "," + getRightChild() + ")";
   }
}
