package Processor.Polarity;

import Evaluator.Treenode.*;
import main.Main;

public class StructurePreservingCNFProcessor {
    static PolarityList polarity = new PolarityList();


    public static String process(TreeNode node) {
        if (node instanceof ValueNode || (node instanceof Not && ((Not) node).getChild() instanceof ValueNode)){
            return node.toString();
        }
        processNode(node, polarity.inventName());
        deleteSelfEquivalences(polarity);
        erklaerungAusgeben(Main.erklaerungsmodus,"Polarity transformation:   " + polarity.polarity.toString());
        return polarity.toString();
    }

    private static TreeNode processNode(TreeNode node, String name) {
        initialize(name);
        initializeClause(name);
        if (node instanceof Not) {
            if (((Not) node).getChild() instanceof ValueNode){
                substituteLiterals(node,polarity,name);
                polarity.append(" <-> " + node.toString() + ")");
                return ((Not) node).getChild();
            }
            else {
                //return processNegative(node);
            }
        }
        if (node instanceof BinaryTreeNode) {
            return processPositive(node);
        }
        if (node instanceof ValueNode) {
            substituteLiterals(node,polarity,name);
            polarity.append(" <-> " + node.toString() + ")");
            return node;
        }
        throw new IllegalArgumentException();
    }

    private static TreeNode processPositive(TreeNode node) {
        polarity.append(" -> ");
        if (node instanceof BinaryTreeNode) {
            BinaryTreeNode binary = (BinaryTreeNode) node;
            if (node instanceof And) {
                String subName1 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName1 : " + subName1 + " binary.getLeftChild: " + binary.getLeftChild());
                String subName2 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName2 : " + subName2 + " binary.getRightChild: " + binary.getRightChild());
                polarity.append("(" + subName1 + " & " + subName2 + "))");
                return new And(
                        processNode(binary.getLeftChild(), subName1),
                        processNode(binary.getRightChild(), subName2)
                );
            }
            if (node instanceof Or) {
                String subName1 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName1 : " + subName1 + " binary.getLeftChild: " + binary.getLeftChild());
                String subName2 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName2 : " + subName2 + " binary.getRightChild: " + binary.getRightChild());
                polarity.append("(" + subName1 + " | " + subName2 + "))");
                return new Or(
                        processNode(binary.getLeftChild(), subName1),
                        processNode(binary.getRightChild(), subName2)
                );
            }
            if (node instanceof Implication) {
                String subName1 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName1 : " + subName1 + " binary.getLeftChild: " + binary.getLeftChild());
                String subName2 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName2 : " + subName2 + " binary.getRightChild: " + binary.getRightChild());
                polarity.append("(" + subName1 + " -> " + subName2 + "))");
                return new Implication(
                        processNode(binary.getLeftChild(), subName1),
                        processNode(binary.getRightChild(), subName2)
                );
            }
            if (node instanceof Equivalence) {
                String subName1 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName1 : " + subName1 + " binary.getLeftChild: " + binary.getLeftChild());
                String subName2 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName2 : " + subName2 + " binary.getRightChild: " + binary.getRightChild());
                polarity.append("(" + subName1 + " <-> " + subName2 + "))");
                return new Equivalence(
                        processNode(binary.getLeftChild(), subName1),
                        processNode(binary.getRightChild(), subName2)
                );
            }
        }
        if (node instanceof UnaryTreeNode) {
            if (node instanceof Not) {
                TreeNode child = ((Not) node).getChild();
                if (child instanceof Literal) {
                    polarity.append("-" + child.toString());
                    return child;
                }
                //return new Not(processNegative(child));
            }
        }
        throw new IllegalArgumentException("Fail " + node.toString());
    }


    /*private static TreeNode processNegative(TreeNode node) {
        polarity.append(" <- ");
        if (node instanceof ValueNode) {
            String literal = node.toString();
            polarity.append("-" + literal + "");
            return node;
        }
        if (node instanceof BinaryTreeNode) {
            BinaryTreeNode binary = (BinaryTreeNode) node;
            if (node instanceof And) {
                String subName1 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName1 : " + subName1 + " binary.getLeftChild: " + binary.getLeftChild());
                String subName2 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName2 : " + subName2 + " binary.getRightChild: " + binary.getRightChild());
                polarity.append("(" + subName1 + " & " + subName2 + "))");
                return new And(
                        processNode(binary.getLeftChild(), subName1),
                        processNode(binary.getRightChild(), subName2)
                );

            }
            if (node instanceof Or) {
                String subName1 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName1 : " + subName1 + " binary.getLeftChild: " + binary.getLeftChild());
                String subName2 = polarity.inventName();
                erklaerungAusgeben(Main.erklaerungsmodus,"subName2 : " + subName2 + " binary.getRightChild: " + binary.getRightChild());
                polarity.append("(" + subName1 + " | " + subName2 + "))");
                return new Or(
                        processNode(binary.getLeftChild(), subName1),
                        processNode(binary.getRightChild(), subName2)
                );
            }
        }
        if (node instanceof UnaryTreeNode) {
            if (node instanceof Not) {
                TreeNode child = ((Not) node).getChild();
                if (child instanceof Literal) {
                    polarity.append("-" + child.toString() + ")");
                    return child;
                }
                return new Not(processPositive(child));
            }
        }
        throw new IllegalArgumentException("Fail neg" + node.toString());
    }
*/
    private static void deleteSelfEquivalences(PolarityList polarity) {
        String[] cuts = polarity.toString().split("&");
        polarity.polarity.delete(0,polarity.polarity.length());
        for (int i = 0; i <= cuts.length-1;i++){
            if (!cuts[i].contains("<->")){
                polarity.append(cuts[i]);
                if (i < cuts.length-1){
                    polarity.append("&");
                }
            }
        }
        if (polarity.polarity.toString().endsWith("&")){
            polarity.polarity.delete(polarity.polarity.length()-1,polarity.polarity.length());
            String result = polarity.polarity.toString().trim();
            polarity.polarity.delete(0,polarity.polarity.length());
            polarity.polarity.append(result);
        }
    }

    private static void substituteLiterals(TreeNode node, PolarityList polarity, String name) {
        String substitutedLeft = polarity.toString().replace("(" + name, "(" + node.toString());
        polarity.polarity.delete(0,polarity.polarity.length());
        String substitutedRight = substitutedLeft.replace(name + ")",  node.toString()+ ")");
        polarity.polarity.append(substitutedRight);
    }

    public static void erklaerungAusgeben(boolean erklaerungsmodus, String s){
        if (erklaerungsmodus){
            System.out.println(s);
        }
    }

    private static void initializeClause(String name) {
        if (polarity.counter >= 2) {
            polarity.append("\n                               & " + "(" + name);
        }
    }

    private static void initialize(String name) {
        if (polarity.counter == 1) {
            polarity.append(name + " & " + "(" + name);
        }
    }
}
