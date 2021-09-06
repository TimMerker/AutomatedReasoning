package Processor.sixStepsProcessor;

import Evaluator.Treenode.*;

public class firstStep {

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
        throw new IllegalArgumentException("Das ist kein korrekter unärer Operator: "+ node.toString());
    }

    private static TreeNode processBinary(BinaryTreeNode node) {
        if (node instanceof And){
            return new And(
                    processNode(node.getLeftChild()),
                    processNode(node.getRightChild())
            );
        }
        if (node instanceof Or){
            return new Or(
                    processNode(node.getLeftChild()),
                    processNode(node.getRightChild())
            );
        }
        if (node instanceof Implication){
            return new Implication(
                    processNode(node.getLeftChild()),
                    processNode(node.getRightChild())
            );
        }
        if (node instanceof Equivalence){
            return new And(
                    new Implication(
                            processNode(node.getLeftChild()),
                            processNode(node.getRightChild())),

                    new Implication(
                            processNode(node.getRightChild()),
                            processNode(node.getLeftChild()))
            );
        }
        throw new IllegalArgumentException("Das ist kein korrekter Binäroperator: " + node.toString());
    }

}
