package Processor.sixStepsProcessor;

import Evaluator.Treenode.*;

public class sixthStep {
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
            if (node.getChild() instanceof Verum){
                return new Falsum();
            }
            if (node.getChild() instanceof Falsum){
                return new Verum();
            }
            else {
                return new Not(processNode(node.getChild()));
            }
        }
        throw new IllegalArgumentException();
    }

    private static TreeNode processBinary(BinaryTreeNode node) {
        if (node instanceof And) {
            TreeNode processedLeftChild = processNode(node.getLeftChild());
            TreeNode processedRightChild = processNode(node.getRightChild());
            if (processedLeftChild instanceof ValueNode) {
                if (processedLeftChild instanceof Verum) {
                    return processedRightChild;
                }
                if (processedLeftChild instanceof Falsum) {
                    return new Falsum();
                }
            }
            else if (processedRightChild instanceof ValueNode) {
                if (processedRightChild instanceof Verum) {
                    return processedLeftChild;
                }
                if (processedRightChild instanceof Falsum) {
                    return new Falsum();
                }
            }
            if (processedLeftChild instanceof Literal && processedRightChild instanceof Literal) {
                return new And(
                        processNode(processedLeftChild),
                        processNode(processedRightChild)
                );
            }
            else{
                return new And(
                        processNode(processedLeftChild),
                        processNode(processedRightChild)
                );
            }


        } else if (node instanceof Or) {
            TreeNode processedLeftChild = processNode(node.getLeftChild());
            TreeNode processedRightChild = processNode(node.getRightChild());
            if (processedLeftChild instanceof ValueNode) {
                if (processedLeftChild instanceof Verum) {
                    return new Verum();
                }
                if (processedLeftChild instanceof Falsum) {
                    return processedRightChild;
                }
            }
            if (processedRightChild instanceof ValueNode) {
                if (processedRightChild instanceof Verum) {
                    return new Verum();
                }
                if (processedRightChild instanceof Falsum) {
                    return processedLeftChild;
                }
            }
            if (processedLeftChild instanceof Literal && processedRightChild instanceof Literal) {
                return new Or(
                        processNode(processedLeftChild),
                        processNode(processedRightChild)
                );
            }
            else{
                return new Or(
                        processNode(processedLeftChild),
                        processNode(processedRightChild)
                );
            }
        }
        throw new IllegalArgumentException();
    }
}
