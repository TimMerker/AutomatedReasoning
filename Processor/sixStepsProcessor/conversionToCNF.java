package Processor.sixStepsProcessor;

import Evaluator.List.Klausel;
import Evaluator.List.Klauselmenge;
import Evaluator.List.KlauselmengeEvaluator;
import Evaluator.List.Literal;
import Evaluator.Treenode.TreeNode;

import java.util.Collections;
import java.util.Iterator;

public class conversionToCNF {
    public static String process(TreeNode tree) {
        TreeNode result;
        result = firstStep.process(tree);
        result = secondStep.process(result);
        result = thirdAndFourthStep.process(result);
        result = fifthStepCNF.process(result);
        result = sixthStep.process(result);
        String cnf = result.toString();
        cnf = removeBrackets(cnf);
        cnf = subsumtionRules(cnf);
        return cnf;
    }

    /***
     * Entfernt Klauseln nach den Subsumtionsregeln.
     *
     * Input: (B | -A) & (-A | B) & (-A) & (-A) & (B | -A) & (B) & (B | -A) & (B | -A)
     * Output: (-A) & (B)
     * @param cnf
     * @return
     */
    private static String subsumtionRules(String cnf) {

        KlauselmengeEvaluator eval = new KlauselmengeEvaluator();
        Klauselmenge klauselmenge = eval.evaluateFormula(cnf);
        removeDuplicateLiterals(klauselmenge);
        sortBySize(klauselmenge);

        for (int i = 0; i < klauselmenge.size();i++){

            Klausel klausel1 = klauselmenge.get(i);
            for (int j = i + 1; j < klauselmenge.size();j++){
                Klausel klausel2 = klauselmenge.get(j);
                boolean subsumption = true;
                for(int k = 0; k < klausel1.size();k++){
                    boolean contains = false;
                    for(int l = 0; l < klausel2.size();l++){
                        if (klausel1.get(k).equals(klausel2.get(l))){
                            contains = true;
                        }
                    }
                    if (!contains){
                        subsumption = false;
                    }
                }
                if (subsumption){
                    klauselmenge.n.remove(j);
                    i--;
                    break;
                }
            }
        }
        return klauselmenge.toCnf();
    }

    private static void sortBySize(Klauselmenge klauselmenge) {
        for (int i = 0; i < klauselmenge.size();i++){
            for (int j = i + 1; j< klauselmenge.size();j++)
            if (klauselmenge.get(j).size() < klauselmenge.get(i).size()){
                Collections.swap(klauselmenge.n,j,i);
            }
        }
    }

    private static void removeDuplicateLiterals(Klauselmenge klauselmenge) {
        //Faktorisierungsregel auf alle Klauseln der Klauselmenge
        for (int i = 0; i < klauselmenge.size();i++){
            Klausel klausel = klauselmenge.get(i);
            Iterator<Literal> itr = klausel.iterator();

            if (itr.hasNext()) {
                Literal previous = itr.next();
                while (itr.hasNext()) {
                    Literal current = itr.next();
                    if (previous.equals(current)) {
                        itr.remove();
                    } else {
                        previous = current;
                    }


                }
            }
        }
    }


    /***
     * Removes brackets, duplicates and the obvious redundant clauses like (-A | A) (1).
     * @param cnf
     * @return
     */
    public static String removeBrackets(String cnf) {
        cnf = cnf.replaceAll("[\\p{Ps}\\p{Pe}]","");
        cnf = cnf.replaceAll("\\s","");
        String[] s = cnf.split("&");
        String[] filtered = new String[s.length];
        for (int i = 0; i < s.length;i++){
            String temp = filterDuplicates(s[i]);
            filtered[i] = removeRedundant(temp);
        }


        StringBuilder str = new StringBuilder();
        for (int i = 0; i < filtered.length; i++) {
            if (filtered[i].equals("")){
                continue;
            }
            str.append('(');
            str.append(filtered[i]);

            if (i != s.length -1 ){
                str.append(") & ");
            }
            else{
                str.append(")");
            }
        }
        String result = str.toString();
        if (result.endsWith(" & ")){
            result = result.substring(0,result.length()-3);
        }
        if(result.length() == 0){
            return "1";
        }
        else{
            return result;
        }
    }

    private static String removeRedundant(String temp) {
        String copy = temp;
        copy = copy.replaceAll("\\s","");
        String[] literals = copy.split("\\|");
        boolean complementary = false;
        for (int i = 0; i < literals.length; i++){
            for (int j = i + 1; j < literals.length;j++) {
                if (literals[i].equals("-" + literals[j]) || literals[j].equals("-" + literals[i])) {
                    complementary = true;
                    break;
                }
            }
        }
        if (complementary){
            return "";
        }
        else{
            return temp;
        }
    }

    /***
     * Removes duplicates and falsum from a clause.
     * Input: A | B | -D | 0 | A
     * Output: B | -D | A
     * @param s, clause as String A | B | -D | 0
     */


    private static String filterDuplicates(String s) {
        String copy = s;
        copy = copy.replaceAll("\\s","");
        String[] literals = copy.split("\\|");
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < literals.length; i++){
            boolean duplicate = false;
            for (int j = i + 1; j < literals.length;j++) {
                if (literals[i].equals(literals[j])) {
                    duplicate = true;
                }
            }
            if(!duplicate){
                str.append(literals[i]);
                if (i != literals.length -1){
                    str.append(" | ");
                }
            }
        }
        return str.toString();
    }
    /*
    /***
     * Returns true is a clause contains Verum.
     * @param s
     * @return
     */

/*
    private static boolean clauseContainsVerum(String s) {
        String copy = s;
        copy = copy.replaceAll("\\s","");
        String[] literals = copy.split("\\|");
        for (int i = 0; i < literals.length;i++){
            if (literals[i].length() == 1){
                if (literals[i].equals("1")){
                    return true;
                }
            }
        }
        return false;
    }
    */


}
