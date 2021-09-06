package Test;

import Evaluator.List.Klauselmenge;
import Evaluator.List.KlauselmengeEvaluator;
import Evaluator.Treenode.TreeEvaluator;
import Evaluator.Treenode.TreeNode;
import Processor.sixStepsProcessor.conversionToCNF;
import org.junit.Test;

public class KNFTest {
    @Test
    public void KNFTest01(){
        String formel = "(A | -B) & C <-> -C";
        TreeEvaluator eval = new TreeEvaluator();
        TreeNode tree = eval.evaluate(formel);
        String cnf;
        cnf = conversionToCNF.process(tree);
        String result = cnf.toString();
        KlauselmengeEvaluator eval2 = new KlauselmengeEvaluator();
        Klauselmenge klauselmenge = eval2.evaluateFormula(result);
        System.out.println("The translated formula is: "+ klauselmenge);
        System.out.println("This is equivalent with the CNF:  " +
                "C ∧ ¬A ∧ B from https://www.erpelstolz.at/cgi-bin/cgi-logik");
    }
    @Test
    public void KNFTest02(){
        String formel = "(A | -A | 0) & (A | A) & (B | 1 | 0) & (1) & (0) & (B | -B)";
        TreeEvaluator eval = new TreeEvaluator();
        TreeNode tree = eval.evaluate(formel);
        String cnf;
        cnf = conversionToCNF.process(tree);
        String result = cnf;
        KlauselmengeEvaluator eval2 = new KlauselmengeEvaluator();
        Klauselmenge klauselmenge = eval2.evaluateFormula(result);
        System.out.println("The translated formula is: "+ klauselmenge);
        System.out.println("This is equivalent with the CNF:  " +
                "0");
    }
    @Test
    public void KNFTest03(){
        String formel = "(A | -A | 0) & (A | A) & (B | 1 | 0) & (1)  & (B | -B)";
        TreeEvaluator eval = new TreeEvaluator();
        TreeNode tree = eval.evaluate(formel);
        String cnf;
        cnf = conversionToCNF.process(tree);
        String result = cnf.toString();
        KlauselmengeEvaluator eval2 = new KlauselmengeEvaluator();
        Klauselmenge klauselmenge = eval2.evaluateFormula(result);
        System.out.println("The translated formula is: "+ klauselmenge);
        System.out.println("This is equivalent with the CNF:  " +
                "(A)");
    }

}
