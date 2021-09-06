package Processor.sixStepsProcessor;

import Evaluator.Treenode.TreeNode;

public class conversionToDNF {
    public static String process(TreeNode tree) {
        tree = firstStep.process(tree);
        tree = secondStep.process(tree);
        tree = thirdAndFourthStep.process(tree);
        tree = fifthStepDNF.process(tree);
        tree = sixthStep.process(tree);
        String dnf = tree.toString();
        dnf = removeBrackets(dnf);
        return dnf;
    }

    private static String removeBrackets(String dnf) {
        dnf = dnf.replaceAll("[\\p{Ps}\\p{Pe}]","");
        return dnf;
    }
}
