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

        System.out.println("TOOLKIT FÜR DIE AUSSAGENLOGIK MIT GEORDNETER RESOLUTION");
        System.out.println();
        System.out.println("Dieses Toolkit bietet verschiedene Tests für die Aussagenlogik.\n\n" +
                "Es können aussagenlogische Formeln oder Klauselmengen eingegeben werden.\n\n" +
                "Die Eingabe für aussagenlogische Formeln muss folgende Syntax verwenden: \n\n" +
                "Für die Operatoren gilt folgende Darstellung:\n& (Konjunktion),\n| (Disjunktion),\n-> (Implikation),\n<-> (Äquivalenz),\n- (Negation),\n( (öffnende Klammer),\n) (schließende Klammer)\n\n" +
                "Für die Aussagenvariablen gilt eine Schreibweise bestehend aus\n einem Großbuchstaben und ggf. einem Index:   A1 (Aussagenvariable A1)\n\n" +
                "Für die Klauseln werden Literale durch ein Komma getrennt und in eckige Klammern gestzt:\n   [A1,-A2,A3] (Klausel mit den Literalen A1,-A2,A3)\n\n" +
                "Für die Klauselmengen werden Klauseln durch ein Semikolon getrennt und in eckige Klammern gesetzt:\n    [[A1,-A2,A3];[-A1,A2,A3];[-A1,-A2]]\n\n");


        System.out.println();
        System.out.print("Bitte wählen Sie ob Sie den erweiterten Erklärungsmodus anwenden möchten oder nur die wichtigten Ergebnisse sehen wollen:\n\n" +
                ("(Geben Sie E für Erklärungsmodus und R für Ergebnismodus ein): "));
        String modusEingabe = in.nextLine();

        boolean quit = false;
        while (!quit) {
                if (!(modusEingabe.equals("E")) && !(modusEingabe.equals("e")) && !(modusEingabe.equals("R")) && !(modusEingabe.equals("r")))
                    throw new IllegalArgumentException();

                if (modusEingabe.equals("E") || modusEingabe.equals("e")) {
                    erklaerungsmodus = true;
                } else {
                    erklaerungsmodus = false;
                }

                System.out.println();



                System.out.print("Bitte geben Sie an, welches Verfahren Sie anwenden möchten \n" +
                        "1: Geordnete Resolution als Erfüllbarkeitstest mit Modellgenerierung \n" +
                        "2: Redundanztest/Redundanzelimination \n" +
                        "3: Transformation einer Formel in KNF/DNF/NNF  \n" +
                        "4: Bestimmung der Polarität einer Subformel einer gegebenen Formel  \n" +
                        "5: Berechnung der Interpolante von zwei verschiedenen unerfüllbaren Formeln\n" +
                        "6: Modus wechseln\n" +
                        "7: Programm verlassen\n");


                String test = in.nextLine();

                if (!(test.equals("1")) && !(test.equals("2")) && !(test.equals("3")) && !(test.equals("4")) && !(test.equals("5")) && !(test.equals("6")) && !(test.equals("7"))) {
                    throw new IllegalArgumentException();
                }
                System.out.println();

                if (test.equals("1")) {
                    System.out.print("Bitte wählen Sie die Form der Eingabe\n (F: für aussagenlogische Formel oder K: für Klauselmenge ein): \n");
                    String form = in.nextLine();
                    if (form.equals("F") || form.equals("f")) {
                        System.out.print("Bitte geben Sie eine aussagenlogische Formel für den \nErfüllbarkeitstest ein: \n");
                        String formelInput = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formelInput);
                        String cnf = conversionToCNF.process(tree);
                        System.out.println("Formel in KNF:" + cnf + "\n");
                        if (cnf.equals("0")) {
                            System.out.println("UNERFÜLLBAR: Da die leere Klausel hergeleitet werden konnte, " +
                                    "ist die Klauselmenge unerfüllbar.");
                            return;
                        }
                        if (cnf.equals("1")) {
                            System.out.println("ERFÜLLBAR: Die Klauselmenge ist erfüllbar, da Verum hergeleitet werden konnte.");
                            return;
                        }
                        KlauselmengeEvaluator eval2 = new KlauselmengeEvaluator();
                        Klauselmenge klauselmenge = eval2.evaluateFormula(cnf);
                        System.out.println(klauselmenge.toString() + "\n");
                        System.out.print("Bitte geben Sie eine Ordnung ein: ");
                        String ordnungInput = in.nextLine();
                        OrdnungEvaluator eval3 = new OrdnungEvaluator();
                        Ordnung ordnung = eval3.evaluate(ordnungInput);
                        Resolution resolution = new Resolution();
                        Klauselmenge withoutRedundancy = resolution.redundanzEliminationOhneKonsolenausgaben(klauselmenge, ordnung);
                        resolution.geordneteResolution(ordnung, withoutRedundancy);

                    } else if (form.equals("K") || form.equals("k")) {
                        System.out.print("Bitte geben Sie eine Klauselmenge ein: ");
                        String formel = in.nextLine();
                        KlauselmengeEvaluator eval = new KlauselmengeEvaluator();
                        Klauselmenge klauselmenge = eval.evaluateSet(formel);
                        System.out.print("Bitte geben Sie eine Ordnung ein: ");
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
                    System.out.println("1:  Redundanztest einer Klausel bzgl. einer Klauselmenge \n" + "2:  Redundanzelimination in einer Klauselmenge");
                    String input = in.nextLine();
                    if (!(input.equals("1")) && !(input.equals("2"))) {
                        throw new IllegalArgumentException();
                    }
                    if (input.equals("1")) {
                        System.out.print("Bitte geben Sie eine Klauselmenge ein: ");
                        String formel = in.nextLine();
                        KlauselmengeEvaluator eval = new KlauselmengeEvaluator();
                        Klauselmenge klauselmenge = eval.evaluateSet(formel);
                        System.out.print("Bitte geben Sie eine Ordnung ein: ");
                        String formel2 = in.nextLine();
                        OrdnungEvaluator eval2 = new OrdnungEvaluator();
                        Ordnung ordnung = eval2.evaluate(formel2);
                        System.out.println("Bitte geben Sie die auf Redundanz zu untersuchende Klausel ein: ");
                        String formel3 = in.nextLine();
                        KlauselEvaluator eval3 = new KlauselEvaluator();
                        Klausel klausel = eval3.evaluate(formel3);
                        Resolution resolution = new Resolution();
                        resolution.redundanztest(klausel, klauselmenge, ordnung);
                    }
                    if (input.equals("2")) {
                        System.out.print("Bitte geben Sie eine Klauselmenge ein: ");
                        String formel = in.nextLine();
                        KlauselmengeEvaluator eval = new KlauselmengeEvaluator();
                        Klauselmenge klauselmenge = eval.evaluateSet(formel);
                        System.out.print("Bitte geben Sie eine Ordnung ein: ");
                        String formel2 = in.nextLine();
                        OrdnungEvaluator eval2 = new OrdnungEvaluator();
                        Ordnung ordnung = eval2.evaluate(formel2);
                        Resolution resolution = new Resolution();
                        resolution.redundanzElimination(klauselmenge, ordnung);
                    }

                }


                if (test.equals("3")) {
                    System.out.println("1: Transformation einer Formel in NNF durch Äquivalenzumformungen\n" + "2: Transformation einer Formel in KNF durch Äquivalenzumformungen\n" + "3: Transformation einer Formel in DNF durch Äquivalenzumformungen \n"
                            + "4: Transformation einer Formel in KNF durch strukturerhaltende Umformungen\n");
                    String input = in.nextLine();
                    if (!(input.equals("1")) && !(input.equals("2")) && !(input.equals("3")) && !(input.equals("4"))) {
                        throw new IllegalArgumentException();
                    }
                    if (input.equals("1")) {
                        System.out.print("Bitte geben Sie eine aussagenlogische Formel ein: ");
                        String formel = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formel);
                        String result;
                        result = conversionToNNF.process(tree).toString();
                        System.out.println("The translated formula is: ");
                        System.out.println(result);
                    }
                    if (input.equals("2")) {
                        System.out.print("Bitte geben Sie eine aussagenlogische Formel ein: ");
                        String formel = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formel);
                        String result;
                        result = conversionToCNF.process(tree);
                        System.out.println("The translated formula is: " + result);
                    }

                    if (input.equals("3")) {
                        System.out.print("Bitte geben Sie eine aussagenlogische Formel ein: ");
                        String formel = in.nextLine();
                        TreeEvaluator eval = new TreeEvaluator();
                        TreeNode tree = eval.evaluate(formel);
                        String result;
                        result = conversionToDNF.process(tree);
                        System.out.println("The translated formula is: ");
                        System.out.println(result);
                    }
                    if (input.equals("4")) {
                        System.out.print("Bitte geben Sie eine aussagenlogische Formel ein: ");
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
                    System.out.print("Bitte geben Sie eine aussagenlogische Formel ein: ");
                    String formel = in.nextLine();
                    TreeEvaluator eval = new TreeEvaluator();
                    TreeNode tree = eval.evaluate(formel);
                    System.out.print("Bitte geben Sie eine Subformel in Bezug auf die aussagenlogische Formel ein: ");
                    String sub = in.nextLine();
                    TreeNode subformula = eval.evaluate(sub);
                    String result;
                    result = PolarityProcessor.process(tree, subformula);
                    System.out.println("The translated formula is: " + result);
                }

                if (test.equals("5")) {
                    System.out.print("Bitte geben Sie die erste Formel A in Klauselmengennotation ein: ");
                    String formel = in.nextLine();
                    KlauselmengeEvaluator eval1 = new KlauselmengeEvaluator();
                    Klauselmenge klauselmenge1 = eval1.evaluateSet(formel);
                    System.out.print("Bitte geben Sie die zweite Formel B in Klauselmengennotation ein: ");
                    String formel2 = in.nextLine();
                    KlauselmengeEvaluator eval2 = new KlauselmengeEvaluator();
                    Klauselmenge klauselmenge2 = eval2.evaluateSet(formel2);

                    System.out.print("Soll die Ordnung automatisch erstellt werden? (Geben Sie J für ja oder N für nein ein): ");
                    String automatisiert = in.nextLine();

                    if (!(automatisiert.equals("J")) && !(automatisiert.equals("j")) && !(automatisiert.equals("N")) && !(automatisiert.equals("n")))
                        throw new IllegalArgumentException();

                    Ordnung pureOrdnungA = new Ordnung(new LinkedList<>());
                    Ordnung ordnungCommon = new Ordnung(new LinkedList<>());
                    Ordnung pureOrdnungB = new Ordnung(new LinkedList<>());

                    if (automatisiert.equals("J") || automatisiert.equals("j")) {
                        pureOrdnungA = Ordnung.pureOrdnung(klauselmenge1, klauselmenge2);
                        ordnungCommon = Ordnung.commonOrdnung(klauselmenge1, klauselmenge2);
                        pureOrdnungB = Ordnung.pureOrdnung(klauselmenge2, klauselmenge1);
                    }
                    if (automatisiert.equals("N") || automatisiert.equals("n")) {
                        System.out.println("Geben Sie die Variablen an die in Formel A UND NICHT in Formel B auftauchen: ");
                        String ordnung = in.nextLine();
                        OrdnungEvaluator eval3 = new OrdnungEvaluator();
                        pureOrdnungA = eval3.evaluate(ordnung);
                        System.out.println("Geben Sie die Variablen an die in Formel A UND Formel B auftauchen: ");
                        String ordnung2 = in.nextLine();
                        OrdnungEvaluator eval4 = new OrdnungEvaluator();
                        ordnungCommon = eval4.evaluate(ordnung2);
                        System.out.println("Geben Sie die Variablen an die NICHT in Formel A ABER in Formel B auftauchen: ");
                        String ordnung3 = in.nextLine();
                        OrdnungEvaluator eval5 = new OrdnungEvaluator();
                        pureOrdnungB = eval5.evaluate(ordnung3);
                    }
                    Resolution resolution = new Resolution();
                    resolution.interpolant(pureOrdnungA,ordnungCommon,pureOrdnungB,klauselmenge1,klauselmenge2);



                }
                if (test.equals("6")) {
                    System.out.println("Gehe zurück zum Hauptmenü...\n");
                    System.out.println("Sie können den Modus jetzt wechseln.\n");
                    System.out.print("Bitte wählen Sie ob Sie den erweiterten Erklärungsmodus anwenden möchten oder nur die wichtigten Ergebnisse sehen wollen:\n\n" +
                            ("(Geben Sie E für Erklärungsmodus und R für Ergebnismodus ein): "));
                    modusEingabe = in.nextLine();
                    if (!(modusEingabe.equals("E")) && !(modusEingabe.equals("e")) && !(modusEingabe.equals("R")) && !(modusEingabe.equals("r")))
                        throw new IllegalArgumentException();
                    if (modusEingabe.equals("E") || modusEingabe.equals("e")) {
                        erklaerungsmodus = true;
                    } else {
                        erklaerungsmodus = false;
                    }
                    System.out.println();
                }
                if (test.equals("7")) {
                    System.out.println("Beende die Applikation...\n");
                    System.out.println("Applikation geschlossen.\n");
                    System.out.println("Vielen Dank und noch einen schönen Tag!\n");
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
