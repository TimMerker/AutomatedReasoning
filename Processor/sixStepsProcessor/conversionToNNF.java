package Processor.sixStepsProcessor;

import Evaluator.Treenode.TreeNode;

public class conversionToNNF {
    public static TreeNode process(TreeNode tree) {
        tree = firstStep.process(tree);
        tree = secondStep.process(tree);
        tree = thirdAndFourthStep.process(tree);
        return tree;
    }


}
