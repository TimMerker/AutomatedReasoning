package Processor.Polarity;

import Evaluator.Treenode.*;
import Processor.sixStepsProcessor.firstStep;
import Processor.sixStepsProcessor.secondStep;
import main.Main;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class PolarityProcessor {
    static LinkedList<String> result = new LinkedList<String>();
    static PolarityList polarity = new PolarityList();
    static TreeNode subformula;
    static LinkedList<Integer> index = new LinkedList<>();
    static int counter = 0;

    public static String process(TreeNode node, TreeNode subformula) {
        PolarityProcessor.subformula = subformula;
        node = firstStep.process(node);
        node = secondStep.process(node);
        processNode(node);
        return "Polarity List: " + resultToString() + "Polarity of the subformula: "
                + index.stream().map(x -> "[" + result.get(x) + "]").collect(Collectors.joining("; \n "));
    }

    private static void findSubformula(TreeNode node) {
        if (subformula.toString().equals(node.toString())) {
            PolarityProcessor.index.add(counter);
        }
        counter++;
    }

    private static String resultToString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            s.append("[");
            s.append(result.get(i));
            s.append("];\n ");
        }
        return s.toString();
    }

    private static TreeNode processNode(TreeNode node) {
        return processPositive(node, new LinkedList<>());
    }

    private static TreeNode processPositive(TreeNode node, LinkedList<Integer> l) {
        findSubformula(node);
        if (node instanceof ValueNode) {
            result.add(node.toString() + ", +]" + printAdress(" Address: [" + l.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]"));
            return node;
        }
        if (node instanceof Not) {
            String name = polarity.inventName();
            result.add(name + ", " + node.toString() + ", +]" + printAdress(" Address: [" + l.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]"));
            return new Not(processNegative(((Not) node).getChild(), clone(l, 1)));
        }
        if (node instanceof BinaryTreeNode) {
            BinaryTreeNode binary = (BinaryTreeNode) node;
            if (node instanceof And) {
                String name = polarity.inventName();
                result.add(name + ", " + node.toString() + ", +]" + printAdress(" Address: [" + l.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]"));
                return new And(processPositive(binary.getLeftChild(), clone(l, 1)),
                        processPositive(binary.getRightChild(), clone(l, 2)));
            }
            if (node instanceof Or) {
                String name = polarity.inventName();
                result.add(name + ", " + node.toString() + ", +]" + printAdress(" Address: [" + l.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]"));
                return new Or(processPositive(binary.getLeftChild(), clone(l, 1)),
                        processPositive(binary.getRightChild(), clone(l, 2)));
            }
        }
        throw new IllegalArgumentException("ProcessPositive failed.");

    }

    private static LinkedList<Integer> clone(LinkedList<Integer> l, int i) {
        LinkedList<Integer> clone = new LinkedList<Integer>(l);
        clone.add(i);
        return clone;
    }


    private static TreeNode processNegative(TreeNode node, LinkedList<Integer> l) {
        findSubformula(node);
        if (node instanceof ValueNode) {
            result.add(node.toString() + ", -]" + printAdress(" Address: [" + l.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]"));
            return node;
        }
        if (node instanceof Not) {
            String name = polarity.inventName();
            result.add(name + ", " + node.toString() + ", -]" + printAdress(" Address: [" + l.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]"));
            return new Not(processPositive(((Not) node).getChild(), clone(l, 1)));
        }
        if (node instanceof BinaryTreeNode) {
            BinaryTreeNode binary = (BinaryTreeNode) node;
            if (node instanceof And) {
                String name = polarity.inventName();
                result.add(name + ", " + node.toString() + ", -]" + printAdress(" Address: [" + l.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]"));
                return new And(processNegative(binary.getLeftChild(), clone(l, 1)),
                        processNegative(binary.getRightChild(), clone(l, 2)));
            }
            if (node instanceof Or) {
                String name = polarity.inventName();
                result.add(name + ", " + node.toString() + ", -]" + printAdress(" Address: [" + l.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]"));
                return new Or(processNegative(binary.getLeftChild(), clone(l, 1)),
                        processNegative(binary.getRightChild(), clone(l, 2)));
            }
        }
        throw new IllegalArgumentException("ProcessPositive failed.");
    }

    public static String printAdress(String s){
        if (Main.erklaerungsmodus){
            return s;
        }
        else{
            return "";
        }
    }
}

