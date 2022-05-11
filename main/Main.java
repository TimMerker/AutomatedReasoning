package main;

import Algorithmen.Resolution;
import Evaluator.List.*;
import Evaluator.Treenode.TreeEvaluator;
import Evaluator.Treenode.TreeNode;
import Processor.Polarity.PolarityProcessor;
import Processor.Polarity.StructurePreservingCNFProcessor;
import Processor.sixStepsProcessor.*;

import java.util.LinkedList;
import java.util.Scanner;

import static Processor.sixStepsProcessor.conversionToCNF.removeBrackets;

public class Main {

    public static boolean erklaerungsmodus;

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);

        System.out.println("TOOLKIT FOR AUTOMATED REASONING AND INTERPOLATION WITH ORDERED RESOLUTION");
        System.out.println();
        System.out.println("This toolkit provides various tests for propositional logic.\n\n" +
                "Propositional formulas or sets of clauses can be entered.\n\n" +
                "The input for propositional formulas must use the following syntax: \n\n" +
                "The following representation applies to the operators:\n& (conjunktion),\n| (disjunktion),\n-> (implication),\n<-> (equivalence),\n- (negation),\n( (left parenthesis),\n) (right parenthesis)\n\n" +
                "Atoms are defined by \n a capital letter and possibly an index::   A1 (Atom A1)\n\n" +
                "For the clauses, literals are separated by a comma and enclosed in square brackets:\n   [A1,-A2,A3] (Clause with literals A1,-A2,A3)\n\n" +
                "For the clause sets, clauses are separated by a semicolon and placed in square brackets:\n    [[A1,-A2,A3];[-A1,A2,A3];[-A1,-A2]]\n\n");


        System.out.println();
        System.out.print("Please choose whether you want to use the explanation mode or the result mode:\n\n" +
                ("(Enter E for explanation mode and R for result mode):"));
        String modusEingabe = in.nextLine();

        boolean quit = false;
        while (!quit) {
                if (!(modusEingabe.equals("E")) && !(modusEingabe.equals("e")) && !(modusEingabe.equals("R")) && !(modusEingabe.equals("r")))
                    throw new IllegalArgumentException();

            erklaerungsmodus = modusEingabe.equals("E") || modusEingabe.equals("e");

                System.out.println();



                System.out.print("Please indicate which method you would like to use \n" +
                        "1: Ordered resolution as a satisfiability test with model generation \n" +
                        "2: Redundancy test/redundancy elimination \n" +
                        "3: Transformation of a formula to CNF/DNF/NNF  \n" +
                        "4: Determining the polarity of a subformula of a given formula  \n" +
                        "5: Calculation of the interpolant of two different unsatisfiable formulas\n" +
                        "6: Change mode\n" +
                        "7: Exit program\n");


                String test = in.nextLine();

                if (!(test.equals("1")) && !(test.equals("2")) && !(test.equals("3")) && !(test.equals("4")) && !(test.equals("5")) && !(test.equals("6")) && !(test.equals("7"))) {
                    throw new IllegalArgumentException();
                }
                System.out.println();

                if (test.equals("1")) {
                    System.out.print("Please select the form of entry\n (F: for propositional formula or C: for set of clauses): \n");
                    String form = in.nextLine();
                    if (form.equals("F") || form.equals("f")) {
                        System.out.print("Please enter a propositional formula \n to test satisfiability: \n");
                        String formelInput = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formelInput);
                        String cnf = conversionToCNF.process(tree);
                        System.out.println("Formula in KNF:" + cnf + "\n");
                        if (cnf.equals("0")) {
                            System.out.println("UNSATISFIABLE: We couldn't derive the empty clause " +
                                    "therefore the formula is unsatisfiable.");
                            return;
                        }
                        if (cnf.equals("1")) {
                            System.out.println("SATISFIABLE: The set of clauses is satisfiable, since Verum could be derived.");
                            return;
                        }
                        KlauselmengeEvaluator eval2 = new KlauselmengeEvaluator();
                        Klauselmenge klauselmenge = eval2.evaluateFormula(cnf);
                        System.out.println(klauselmenge.toString() + "\n");
                        System.out.print("Please enter an order: ");
                        String ordnungInput = in.nextLine();
                        OrdnungEvaluator eval3 = new OrdnungEvaluator();
                        Ordnung ordnung = eval3.evaluate(ordnungInput);
                        Resolution resolution = new Resolution();
                        Klauselmenge withoutRedundancy = resolution.redundanzEliminationOhneKonsolenausgaben(klauselmenge, ordnung);
                        resolution.geordneteResolution(ordnung, withoutRedundancy);

                    } else if (form.equals("C") || form.equals("c")) {
                        System.out.print("Please enter a clause set: ");
                        String formel = in.nextLine();
                        KlauselmengeEvaluator eval = new KlauselmengeEvaluator();
                        Klauselmenge klauselmenge = eval.evaluateSet(formel);
                        System.out.print("Please enter an order: ");
                        String formel2 = in.nextLine();
                        OrdnungEvaluator eval2 = new OrdnungEvaluator();
                        Ordnung ordnung = eval2.evaluate(formel2);
                        Resolution resolution = new Resolution();
                        Klauselmenge withoutRedundancy = resolution.redundanzEliminationOhneKonsolenausgaben(klauselmenge, ordnung);
                        resolution.geordneteResolution(ordnung, withoutRedundancy);
                    } else {
                        throw new IllegalArgumentException();
                    }
                }

                if (test.equals("2")) {
                    System.out.println("1:  Redundancy test of a clause with respect to a set of clauses \n" + "2:  Redundancy elimination in a clause set");
                    String input = in.nextLine();
                    if (!(input.equals("1")) && !(input.equals("2"))) {
                        throw new IllegalArgumentException();
                    }
                    if (input.equals("1")) {
                        System.out.print("Please enter a clause set: ");
                        String formel = in.nextLine();
                        KlauselmengeEvaluator eval = new KlauselmengeEvaluator();
                        Klauselmenge klauselmenge = eval.evaluateSet(formel);
                        System.out.print("Please enter an order: ");
                        String formel2 = in.nextLine();
                        OrdnungEvaluator eval2 = new OrdnungEvaluator();
                        Ordnung ordnung = eval2.evaluate(formel2);
                        System.out.println("Please enter the clause to be examined for redundancy: ");
                        String formel3 = in.nextLine();
                        KlauselEvaluator eval3 = new KlauselEvaluator();
                        Klausel klausel = eval3.evaluate(formel3);
                        Resolution resolution = new Resolution();
                        resolution.redundanztest(klausel, klauselmenge, ordnung);
                    }
                    if (input.equals("2")) {
                        System.out.print("Please enter a clause set: ");
                        String formel = in.nextLine();
                        KlauselmengeEvaluator eval = new KlauselmengeEvaluator();
                        Klauselmenge klauselmenge = eval.evaluateSet(formel);
                        System.out.print("Please enter an order: ");
                        String formel2 = in.nextLine();
                        OrdnungEvaluator eval2 = new OrdnungEvaluator();
                        Ordnung ordnung = eval2.evaluate(formel2);
                        Resolution resolution = new Resolution();
                        resolution.redundanzElimination(klauselmenge, ordnung);
                    }

                }


                if (test.equals("3")) {
                    System.out.println("1: Transformation of a formula in NNF using equivalences \n" + "2: Transformation of a formula in CNF using equivalences \n" + "3: Transformation of a formula in DNF using equivalences \n"
                            + "4: Transformation of a formula in KNF using structure preserving transformations\n");
                    String input = in.nextLine();
                    if (!(input.equals("1")) && !(input.equals("2")) && !(input.equals("3")) && !(input.equals("4"))) {
                        throw new IllegalArgumentException();
                    }
                    if (input.equals("1")) {
                        System.out.print("Please enter a propositional formula: ");
                        String formel = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formel);
                        String result;
                        result = conversionToNNF.process(tree).toString();
                        System.out.println("The translated formula is: ");
                        System.out.println(result);
                    }
                    if (input.equals("2")) {
                        System.out.print("Please enter a propositional formula: ");
                        String formel = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formel);
                        String result;
                        result = conversionToCNF.process(tree);
                        System.out.println("The translated formula is: " + result);
                    }

                    if (input.equals("3")) {
                        System.out.print("Please enter a propositional formula: ");
                        String formel = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formel);
                        String result;
                        result = conversionToDNF.process(tree);
                        System.out.println("The translated formula is: ");
                        System.out.println(result);
                    }
                    if (input.equals("4")) {
                        System.out.print("Please enter a propositional formula: ");
                        String formel = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formel);
                        TreeNode nnf = conversionToNNF.process(tree);
                        System.out.println("Formula in NNF: " + nnf.toString());
                        String result;
                        result = StructurePreservingCNFProcessor.process(nnf);
                        tree = eval.evaluate(result);
                        tree = secondStep.process(tree);
                        tree = fifthStepCNF.process(tree);
                        tree = sixthStep.process(tree);
                        String cnf = tree.toString();
                        cnf = removeBrackets(cnf);
                        System.out.println("The translated formula is: " + cnf);
                    }
                }


                if (test.equals("4")) {
                    System.out.print("Please enter a propositional formula: ");
                    String formel = in.nextLine();
                    TreeEvaluator eval = new TreeEvaluator();
                    TreeNode tree = eval.evaluate(formel);
                    System.out.print("Please enter a subformula of the given propositional formula: ");
                    String sub = in.nextLine();
                    TreeNode subformula = eval.evaluate(sub);
                    String result;
                    result = PolarityProcessor.process(tree, subformula);
                    System.out.println("The translated formula is: " + result);
                }

                if (test.equals("5")) {
                    System.out.print("Please enter the first propositional formula A in clause set notation:");
                    String formel = in.nextLine();
                    KlauselmengeEvaluator eval1 = new KlauselmengeEvaluator();
                    Klauselmenge klauselmenge1 = eval1.evaluateSet(formel);
                    System.out.print("Please enter the second propositional formula B in clause set notation: ");
                    String formel2 = in.nextLine();
                    KlauselmengeEvaluator eval2 = new KlauselmengeEvaluator();
                    Klauselmenge klauselmenge2 = eval2.evaluateSet(formel2);

                    System.out.print("Should the order be created automatically? (Enter Y for yes or N for no): ");
                    String automatisiert = in.nextLine();

                    if (!(automatisiert.equals("Y")) && !(automatisiert.equals("y")) && !(automatisiert.equals("N")) && !(automatisiert.equals("n")))
                        throw new IllegalArgumentException();

                    Ordnung pureOrdnungA = new Ordnung(new LinkedList<>());
                    Ordnung ordnungCommon = new Ordnung(new LinkedList<>());
                    Ordnung pureOrdnungB = new Ordnung(new LinkedList<>());

                    if (automatisiert.equals("Y") || automatisiert.equals("y")) {
                        pureOrdnungA = Ordnung.pureOrdnung(klauselmenge1, klauselmenge2);
                        ordnungCommon = Ordnung.commonOrdnung(klauselmenge1, klauselmenge2);
                        pureOrdnungB = Ordnung.pureOrdnung(klauselmenge2, klauselmenge1);
                    }
                    if (automatisiert.equals("N") || automatisiert.equals("n")) {
                        System.out.println("Please enter the variables that appear in formula A AND NOT in formula B: ");
                        String ordnung = in.nextLine();
                        OrdnungEvaluator eval3 = new OrdnungEvaluator();
                        pureOrdnungA = eval3.evaluate(ordnung);
                        System.out.println("Please enter the variables that appear in formula A AND formula B: ");
                        String ordnung2 = in.nextLine();
                        OrdnungEvaluator eval4 = new OrdnungEvaluator();
                        ordnungCommon = eval4.evaluate(ordnung2);
                        System.out.println("Enter the variables that do NOT appear in formula A BUT appear in formula B: ");
                        String ordnung3 = in.nextLine();
                        OrdnungEvaluator eval5 = new OrdnungEvaluator();
                        pureOrdnungB = eval5.evaluate(ordnung3);
                    }
                    Resolution resolution = new Resolution();
                    resolution.interpolant(pureOrdnungA,ordnungCommon,pureOrdnungB,klauselmenge1,klauselmenge2);



                }
                if (test.equals("6")) {
                    System.out.println("Returning to main menu......\n");
                    System.out.println("You can switch modes now.\n");
                    System.out.print("Please choose whether you want to use the explanation mode or the result mode:\n\n" +
                            ("(Enter E for explanation mode and R for result mode): "));
                    modusEingabe = in.nextLine();
                    if (!(modusEingabe.equals("E")) && !(modusEingabe.equals("e")) && !(modusEingabe.equals("R")) && !(modusEingabe.equals("r")))
                        throw new IllegalArgumentException();
                    erklaerungsmodus = modusEingabe.equals("E") || modusEingabe.equals("e");
                    System.out.println();
                }
                if (test.equals("7")) {
                    System.out.println("Quitting...\n");
                    System.out.println("Application closed.\n");
                    System.out.println("Thank you and have a nice day!\n");
                    quit = true;
                }
            }

    }

    private static void erklaerungsmodus(boolean erklaerungsmodus, String s) {
        if (erklaerungsmodus){
            System.out.println(s);
        }
    }
}
