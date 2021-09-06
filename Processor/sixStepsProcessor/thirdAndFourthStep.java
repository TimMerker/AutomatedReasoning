package Processor.sixStepsProcessor;

import Evaluator.Treenode.*;

public class thirdAndFourthStep {
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
            if (node.getChild() instanceof And){
                return new Or(
                        processNode(new Not(((And) node.getChild()).getLeftChild())),
                        processNode(new Not(((And) node.getChild()).getRightChild()))
                );
            }
            if (node.getChild() instanceof Or){
                return new And(
                        processNode(new Not(((Or) node.getChild()).getLeftChild())),
                        processNode(new Not(((Or) node.getChild()).getRightChild()))
                );
            }
            if (node.getChild() instanceof Not){
                //TODO Abklären mit doppelter Negation seperat als Schritt oder so lassen
                return processNode(((Not) node.getChild()).getChild());
            }
            if (node.getChild() instanceof ValueNode) {
                return new Not(processNode(node.getChild()));
            }
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
        if (node instanceof Or){
            return new Or(
                    processNode(node.getLeftChild()),
                    processNode(node.getRightChild())
            );
        }
        throw new IllegalArgumentException("Das ist kein korrekter Binäroperator für Step 3 + Step 4: " + node.toString());
    }
}
