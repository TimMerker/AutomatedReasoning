package Processor.sixStepsProcessor;

import Evaluator.Treenode.*;

public class fifthStepCNF {
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

    private static TreeNode processUnary(UnaryTreeNode node) {
        if (node instanceof Not){
            return new Not(processNode(node.getChild()));

        }
        throw new IllegalArgumentException();
    }

    private static TreeNode processBinary(BinaryTreeNode node) {
        if (node instanceof And){
            return new And(
                    processNode(node.getLeftChild()),
                    processNode(node.getRightChild())
            );
        }
        if (node instanceof Or) {
            TreeNode processedLeftChild = processNode(node.getLeftChild());
            TreeNode processedRightChild = processNode(node.getRightChild());

            if (processedLeftChild instanceof And) {
                BinaryTreeNode binary = (BinaryTreeNode) processedLeftChild;
                return new And(
                        processNode(new Or(
                                binary.getLeftChild(),
                                processedRightChild)),

                        processNode(new Or(
                                binary.getRightChild(),
                                processedRightChild))
                );
            }
            else if (processedRightChild instanceof And) {
                BinaryTreeNode binary = (BinaryTreeNode) node.getRightChild();
                return new And(
                        processNode(new Or(
                                processedLeftChild,
                                binary.getLeftChild())),

                        processNode(new Or(
                                processedLeftChild,
                                binary.getRightChild()))
                );
            }
            else {
                return new Or(
                        processNode(node.getLeftChild()),
                        processNode(node.getRightChild())
                );
            }
        }
        throw new IllegalArgumentException("Das ist kein korrekter Binäroperator für Step 3: " + node.toString());
    }
}
