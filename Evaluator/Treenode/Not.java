package Evaluator.Treenode;

public class Not extends UnaryTreeNode {
    public Not(TreeNode value) {
        super(value);
    }
    public String getNodeType(){
        return "-";
    }
}
