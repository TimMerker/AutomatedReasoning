package Processor.sixStepsProcessor;


import Evaluator.Treenode.*;

public class fifthStepDNF {
    public static TreeNode process(TreeNode node) {return processNode(node); }

    private static TreeNode processNode(TreeNode node) {
        if (node instanceof BinaryTreeNode)
            return processBinary((BinaryTreeNode) node);
        if (node instanceof UnaryTreeNode) {
            return processUnary((UnaryTreeNode) node);
        }
        if (node instanceof ValueNode){
            return node;
        }
        throw new IllegalArgumentException();
    }

    private static TreeNode processUnary(UnaryTreeNode node) { //--((A|B)&(C|D))
        if (node instanceof Not){
            return new Not(processNode(node.getChild()));

        }
        throw new IllegalArgumentException();
    }

    private static TreeNode processBinary(BinaryTreeNode node) {
        if (node instanceof And) {
            TreeNode processedLeftChild = processNode(node.getLeftChild());
            TreeNode processedRightChild = processNode(node.getRightChild());

            if (processedLeftChild instanceof Or) {
                BinaryTreeNode binary = (BinaryTreeNode) processedLeftChild;
                return new Or(
                        processNode(new And(
                                binary.getLeftChild(),
                                processedRightChild)),

                        processNode(new And(
                                binary.getRightChild(),
                                processedRightChild))
                );
            }
            else if (processedRightChild instanceof Or) {
                BinaryTreeNode binary = (BinaryTreeNode) node.getRightChild();
                return new Or(
                        processNode(new And(
                                processedLeftChild,
                                binary.getLeftChild())),

                        processNode(new And(
                                processedLeftChild,
                                binary.getRightChild()))
                );
            }
            else {
                return new And(
                        processNode(node.getLeftChild()),
                        processNode(node.getRightChild())
                );
            }
        }
        if (node instanceof Or){
            return new Or(
                    processNode(node.getLeftChild()),
                    processNode(node.getRightChild())
            );
        }
        throw new IllegalArgumentException("Das ist kein korrekter Binäroperator für Step 3: " + node.toString());
    }
}
