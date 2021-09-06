package Algorithmen;

import Evaluator.List.Klausel;
import Evaluator.List.Klauselmenge;
import Evaluator.List.Literal;
import Evaluator.List.Ordnung;
import main.Main;

import java.util.*;
import java.util.stream.Collectors;

public class Resolution {

    /***
     *  Nach einer globalen Variable werden die System.out.println eingeschaltet
     * @param erklaerungsmodus, wenn in der main methode auf true gesetzt, werden zusätzliche erklaerungen ausgegeben
     * @param s, die Erklaerung die an dieser Stelle ausgegeben werden kann
     */
    public void erklaerungAusgeben(boolean erklaerungsmodus, String s){
        if (erklaerungsmodus){
            System.out.println(s);
        }
    }


    /***
     * The ordered resolution is applied on clause set with a given ordering.
     * Returns whether a clause set is satisfiable or not.
     *
     *
     * @param ordnung, ordering for the clauses.
     * @param klauselmenge, clause set
     * @return boolean, false if the clause set is unsatisfiable, else true
     */
    public boolean geordneteResolution(Ordnung ordnung, Klauselmenge klauselmenge) {
        if (klauselmenge.toString().equals("[[0]]")){
            System.out.println("UNERFÜLLBAR: \nDa die leere Klausel hergeleitet werden konnte, " +
                    "ist die Klauselmenge unerfüllbar.");
            return false;
        }
        if (klauselmenge.toString().equals("[[1]]")){
            System.out.println("ERFÜLLBAR: \nDie Klauselmenge ist erfüllbar, da Verum hergeleitet werden konnte.");
            return true;
        }

        System.out.println("\nKlauselmenge nach der automatischen Redundanzelimination: " + klauselmenge);
        if (klauselmenge.size() == 0){
            System.out.println("UNERFÜLLBAR: \nDa die leere Klausel hergeleitet werden konnte, " +
                    "ist die Klauselmenge unerfüllbar.");
            System.out.println("Die Eingabe ist eine leere Klauselmenge. " + klauselmenge);
            return false;
        }
        if (ordnung == null){
            ordnung = erstelleLexikografischeOrdnung(klauselmenge);
        }
        ordnung.ordnen(klauselmenge);
        System.out.println("Nach dieser Ordnung wird die Klauselmenge sortiert: " + ordnung);
        erklaerungAusgeben(Main.erklaerungsmodus,"Das ist die sortierte Klauselmenge: "+ klauselmenge+"\n");

        for (int i = 0; i < klauselmenge.size(); i++) {
            Klausel k1 = klauselmenge.get(i);
            if (k1.size() == 0) {
                System.out.println("UNERFÜLLBAR: \nDa die leere Klausel hergeleitet werden konnte, " +
                        "ist die Klauselmenge unerfüllbar.");
                System.out.println(klauselmenge);
                return false;

            }
            faktorisierungsregel(k1);
            Literal l1 = k1.get(0);
            for (int j = i + 1; j < klauselmenge.size(); j++) {
                Klausel k2 = klauselmenge.get(j);
                Literal l2 = k2.get(0);
                if (!l1.s.equals(l2.s) || l1.value == Literal.NOT) {
                    break;
                    // 1.Geordnete Klauselmenge, falls sich die Literale unterscheiden, gibt es kein komplementäres mehr
                    // 2.Geordnete Klauselmenge, falls das erste maximale Literal negiert ist,
                    // gibt es nur noch negierte maximale Literale in der der restlichen klauselmenge
                } else {
                    if (l1.value == true && l2.value == false) {
                        Klausel merge = resolutionsregel(k1, k2);
                        ordnung.ordnen(merge);
                        faktorisierungsregel(merge);
                        if (!klauselmenge.n.contains(merge)) {
                            klauselmenge.n.add(merge);
                            erklaerungAusgeben(Main.erklaerungsmodus,k1 + " 1.Klausel für die Inferenz.");
                            erklaerungAusgeben(Main.erklaerungsmodus,k2 + " 2.Klausel für die Inferenz.");
                            erklaerungAusgeben(Main.erklaerungsmodus,merge + " Ergebnis der Resolution mit automatischer Faktorisierungsregel.");
                            ordnung.ordnen(klauselmenge);
                            i = -1;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("\nERFÜLLBAR: \nDie Klauselmenge ist erfüllbar, da keine weiteren Schritte möglich sind,\n" +
                "Falsum nicht hergeleitet werden konnte, und die geordnete Resolution vollständig ist.\n\n");
        System.out.println("Saturierte Klauselmenge: \n"+ klauselmenge + "\n");


        Klauselmenge withoutRedundancy;
        if (!Main.erklaerungsmodus) {
            withoutRedundancy = redundanzEliminationOhneKonsolenausgaben(klauselmenge, ordnung);
        }
        else{
            erklaerungAusgeben(Main.erklaerungsmodus,"Redundanzelimination in der saturierten Klauselmenge: ");
            withoutRedundancy = redundanzElimination(klauselmenge, ordnung);
        }
        if(withoutRedundancy.size() > 3){
            System.out.println("Saturierte Klauselmenge nach Redundanzelimination: \n");
            klauselmengeList(withoutRedundancy);
        }
        else{
            System.out.println("Saturierte Klauselmenge nach Redundanzelimination: \n" + withoutRedundancy+ "\n");
        }
        System.out.println("\nFolgendes Modell kann mit Hilfe der kanonischen Konstruktion erzeugt werden:");
        kanonischeModellgenerierung(withoutRedundancy, ordnung);
        return true;
    }

    /***
     * Applies the factorization rule on a clause.
     *
     * Requirement: The clause must be ordered!
     * Iteration over the literals of an entered clause,
     * while deleting duplicates.
     *
     * Since the clause is sorted,
     * it is sufficient to iterate once over the clause.
     * The previous literal is compared with the current literal.
     * If the previous literal equals the current literal,
     * the current literal is deleted.
     * @param klausel, ordered clause that may contain duplicates.
     */
    public void faktorisierungsregel(Klausel klausel) {
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


    /***
     * Applies the resolution rule on a clause.
     *
     * Requirement: The maximal literals of the two entered clauses,
     * have the same propositional variable,
     * but one is positive and the other negative.
     * Or else, an exception is thrown.
     *
     * If this is the case, we delete the maximal literals of the clauses,
     * and merge them into a new clause.
     *
     * The entered clauses are not modified during the method.
     *
     * @param klausel1, clause for the resolution rule,
     *                  first maximal literal applies the conditions mentioned above.
     * @param klausel2, clause for the resolution rule,
     *      *           first maximal literal applies the conditions mentioned above.
     *
     * @return Klausel, clause that does not contain the maximal literal of clause 1 and clause 2,
     *                  but contains all other literals appearing in clause 1 and clause 2.
     */
    public Klausel resolutionsregel(Klausel klausel1, Klausel klausel2) {
        Literal l1 = klausel1.l.peekFirst();
        Literal l2 = klausel2.l.peekFirst();
        assert l1 != null;
        assert l2 != null;
        if (l1.s.equals(l2.s) && l1.value != l2.value) {
            Klausel klausel1Clone = new Klausel(new LinkedList<>());
            klausel1Clone.addAll(klausel1);
            Klausel klausel2Clone = new Klausel(new LinkedList<>());
            klausel2Clone.addAll(klausel2);
            klausel1Clone.l.pollFirst();
            klausel2Clone.l.pollFirst();
            Klausel merge = new Klausel(new LinkedList<>());
            merge.addAll(klausel1Clone);
            merge.addAll(klausel2Clone);
            return merge;
        }
        throw new ArithmeticException("Resolutionsregel ist mit Klausel" + klausel1.toString() + "und" + klausel2.toString() + "nicht möglich.");

    }

    /***
     * Erstellt eine Lexikografische Ordnung.
     *
     * Wichtigkeit der Literale absteigend beginnend bei A. Ende bei Z. Bei 2 gleichen Anfangsbuchstaben, ist die
     * Wichtigkeit der Literale als Zahl absteigend. A1 < A2 < A3...
     *
     * Folgendes Ordnungsrelation gilt:
     * Bsp.: A < A0 < A1 < B3 < C4 < D1
     * @return ordnung, lexikografische Ordnung
     */
    public Ordnung erstelleLexikografischeOrdnung(Klauselmenge klauselmenge) {
        int maxSize = 0;
        int maxNummer = 0;
        for (int i = 0; i < klauselmenge.size(); i++) {
            Klausel klausel = klauselmenge.get(i);
            for (int j = 0; j < klausel.size(); j++) {
                if (klausel.get(j).size() >= maxSize) {
                    maxSize = klausel.get(j).size();
                    if (maxSize > 1) {
                        String indizes = klausel.get(j).s;
                        indizes = indizes.substring(1);
                        int literalNummer = Integer.parseInt(indizes);
                        if (literalNummer >= maxNummer) {
                            maxNummer = literalNummer;
                        }
                    }
                }
            }
        }

        if (maxSize <= 1) {
            LinkedList<String> alpha = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
            LinkedList<Literal> collect = alpha.stream().map(Literal::new).collect(Collectors.toCollection(LinkedList::new));
            return new Ordnung(collect);
        } else {
            LinkedList<String> alpha = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
            ListIterator<String> iterator = (ListIterator<String>) alpha.iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                int i = 0;
                while (i <= maxNummer){
                    String index = Integer.toString(i);
                    String neuesLiteral = s.concat(index);
                    iterator.add(neuesLiteral);
                    i++;
                }
            }
            LinkedList<Literal> collect = alpha.stream().map(Literal::new).collect(Collectors.toCollection(LinkedList::new));
            return new Ordnung(collect);
        }
    }



    /***
     * Tests whether a clause is redundant w.r.t. a clause set with a given ordering.
     *
     *
     *
     * Es wird überprüft ob, die Klauseln vor der eingegebenen Klausel in der Klauselmenge, die eingegebene Klausel implizieren. N1 und nicht (C)
     * 1. Imdem eine Kopie der Klauselmenge erstellt wird, die alle Klauseln enthält die kleiner als die eingegebene Klausel sind.
     * 2. Indem die negierte Klausel in die Kopie hinzugefügt wird und die geordnete Algorithmen.Resolution angewendet wird.
     *
     *
     * @param klausel, clause which is tested for redundancy.
     * @param klauselmenge, clause set.
     * @param ordnung, ordering.
     *
     * @return boolean,
     *  true -> the entered clause is redundant. geordneteResolution mit den Klauseln aus der Klauselmenge vor der Klausel und der negierten Klausel ist unerfüllbar.
     *       -> Also ist die eingegebene Klausel redundant.
     *
     *  false -> the entered clause is not redundant. geordneteResolution mit den Klauseln aus der Klauselmenge vor der Klausel und der negierten Klausel ist erfüllbar.
     *        -> Also ist die eingegebene Klausel nicht redundant.
     *
     */
    public boolean redundanztest(Klausel klausel, Klauselmenge klauselmenge, Ordnung ordnung) {
        LinkedList<Literal> alphabetisch = new LinkedList<>();
        if (ordnung == null){
            ordnung = erstelleLexikografischeOrdnung(klauselmenge);
        }
        Klauselmenge klauselmengeKlon = new Klauselmenge(new LinkedList<>());
        klauselmengeKlon.merge(klauselmenge);
        klauselmengeKlon.add(klausel);
        ordnung.ordnen(klauselmengeKlon);
        System.out.println("Das ist die auf Redundanz zu überprüfende Klausel: " + klausel);
        System.out.println("Das ist die eingegebene Klauselmenge: " +klauselmenge);
        System.out.println("Nach dieser Ordnung wird die Klauselmenge sortiert: " + ordnung);
        erklaerungAusgeben(Main.erklaerungsmodus,"Das ist die geordnete Klauselmenge mit der zu überprüfenden Klausel einsortiert: " + klauselmengeKlon);

        Klauselmenge kleinereKlauseln = new Klauselmenge(new LinkedList<>());


        for (int i = 0; i < klauselmengeKlon.size(); i++){
            if (klauselmengeKlon.get(i) != klausel){
                kleinereKlauseln.add(klauselmengeKlon.get(i));
            }
            else {
                break;
            }
        }

        Klauselmenge klauselmengeSmallerKlauselKlon = new Klauselmenge(new LinkedList<>());
        klauselmengeSmallerKlauselKlon.merge(kleinereKlauseln);

        erklaerungAusgeben(Main.erklaerungsmodus,"Das ist der Teil der Klauselmenge, der kleiner ist als die zu überprüfende Klausel: " + kleinereKlauseln);
        kleinereKlauseln.merge(klausel.negate());
        ordnung.ordnen(kleinereKlauseln);
        erklaerungAusgeben(Main.erklaerungsmodus,"Das ist der Teil der Klauselmenge, der kleiner ist als die zu überprüfende Klausel mit der negierten Klausel vereinigt und sortiert: " + kleinereKlauseln);


        if (!geordneteResolutionOhneKonsolenausgaben(ordnung, kleinereKlauseln)){
            if (ordnung.equals(new Ordnung(alphabetisch))) {
                System.out.println("Da keine Ordnung eingegeben wurde, erfolgte der Redundanztest nach der lexikographischen Ordnung");
            }
            System.out.println("\nDie eingegebene Klausel ist redundant in Bezug auf diesen Teil der Klauselmenge  " + klausel +", " + klauselmengeSmallerKlauselKlon);
            return true;
        }
        else{
            if (ordnung.equals(new Ordnung(alphabetisch))) {
                System.out.println("Da keine Ordnung eingegeben wurde, erfolgte der Redundanztest nach der lexikographischen Ordnung");
            }
            System.out.println("\nDie Klausel ist nicht redundant in Bezug auf die Klauselmenge: " + klauselmengeSmallerKlauselKlon);

            System.out.println("\nFolgendes Modell kann mit Hilfe der kanonischen Konstruktion erzeugt werden für C1,...,Cn & -Ci: ");
            kanonischeModellgenerierung(kleinereKlauseln, ordnung);
            return false;
        }
    }

    /***
     *  1.Entfernt alle redundanten Literale in jeder Klauusel mit Hilfe der Faktorisierungsregel.
     *  2.Entfernt alle redundanten Klauseln aus der Klauselmenge.
     *
     *  Für jede Klausel beginnend ab der 2.Klausel wird überprüft, ob die vorigen Klauseln die zu überprüfende Klauseln implizieren.
     *  Die zu überprüfende Klausel wird negiert und mit den vorigen Klauseln in eine leere Klauselmenge hinzugefügt. klonKlauselmengenTeil.
     *  Die geordnete Resolution wird verwendet, um zu testen ob klonKlauselmengenTeil erfüllbar ist.
     *  Falls klonKlauselmengenTeil erfüllbar ist, wird die Klausel nicht aus der Klauselmenge entfernt.
     *  Falls klonKlauselmengenTeil unerfüllbar ist, wird die Klausel auf der Klauselmenge entfernt.
     *
     * @param klauselmenge, deren redundante Klauseln entfernt werden.
     * @param ordnung, nach der die Klauselmenge sortiert ist.
     * @return Klauselmenge, von der klauselmenge abgeleitet, die keine redundanten Klauseln enthält.
     */

    public Klauselmenge redundanzElimination(Klauselmenge klauselmenge, Ordnung ordnung){

        ordnung.ordnen(klauselmenge);
        eliminateTrivialRedundancies(klauselmenge);
        if (klauselmenge.size() <= 1){
            System.out.println("Klauselmengen mit maximal 1 Klausel sind nicht redundant. " + klauselmenge);
            return klauselmenge;
        }
        System.out.println("Redundante Klauseln aus dieser Klauselmenge werden entfernt:  " + klauselmenge);
        System.out.println("Nach dieser Ordnung wird die Klauselmenge sortiert: " + ordnung +"\n");
        Klauselmenge kleinereKlauseln = new Klauselmenge(new LinkedList<>());
        boolean removed = false;
        for (int i = 0; i < klauselmenge.size(); i++){
            faktorisierungsregel(klauselmenge.get(i));
            if (!removed) {
                kleinereKlauseln.add(klauselmenge.get(i));
            }
            removed = false;
            if (i+1 < klauselmenge.size()){
                Klausel klausel = klauselmenge.get(i+1);
                Klauselmenge negierteKlausel = klausel.negate();

                Klauselmenge vereinigteKlauseln = new Klauselmenge(new LinkedList<>());
                vereinigteKlauseln.merge(kleinereKlauseln);
                vereinigteKlauseln.merge(negierteKlausel);

                if (!geordneteResolutionOhneKonsolenausgaben(ordnung, vereinigteKlauseln)){
                    erklaerungAusgeben(Main.erklaerungsmodus,klauselmenge.get(i+1) +"  folgt aus den kleineren Klauseln: "+ kleinereKlauseln.toString());
                    erklaerungAusgeben(Main.erklaerungsmodus,klauselmenge.get(i+1) + " ist redundant und wird entfernt.\n");
                    klauselmenge.n.remove(i+1);
                    removed = true;
                    i--;
                }
                else {
                    erklaerungAusgeben(Main.erklaerungsmodus,klauselmenge.get(i+1) +" folgt nicht aus den kleineren Klauseln: "+ kleinereKlauseln.toString());
                    erklaerungAusgeben(Main.erklaerungsmodus,klauselmenge.get(i+1) +" ist also nicht redundant und wird nicht entfernt.\n");
                }
            }
        }
        System.out.println("Das ist die bereinigte Klauselmenge. " + klauselmenge);
        return klauselmenge;
    }

    private void eliminateTrivialRedundancies(Klauselmenge klauselmenge) {
        for (int i = 0; i < klauselmenge.size();i++){
            faktorisierungsregel(klauselmenge.get(i));
        }
        for (int i = 0; i < klauselmenge.size();i++){
            for (int j = 1; j < klauselmenge.get(i).size();j++){
                String l1 = klauselmenge.get(i).get(j-1).s;
                String l2 = klauselmenge.get(i).get(j).s;
                if (l1.equals(l2)){
                    erklaerungAusgeben(Main.erklaerungsmodus, "\nDiese trivial redundante Klausel wird vorab entfernt: " + klauselmenge.get(i));
                    klauselmenge.n.remove(i);
                    if (klauselmenge.n.size() == 0){
                        Klausel verum = new Klausel(new LinkedList<>());
                        verum.add(new Literal("1"));
                        klauselmenge.n.add(verum);
                    }
                    else{
                        i--;
                    }
                    break;
                }
            }
        }
    }

    public Klauselmenge redundanzEliminationOhneKonsolenausgaben(Klauselmenge klauselmenge, Ordnung ordnung){
        ordnung.ordnen(klauselmenge);
        eliminateTrivialRedundancies(klauselmenge);

        if (klauselmenge.size() <= 1){
            return klauselmenge;
        }

        Klauselmenge klauselmengenTeil = new Klauselmenge(new LinkedList<>());
        boolean removed = false;
        for (int i = 0; i < klauselmenge.size(); i++){
            faktorisierungsregel(klauselmenge.get(i));
            if (!removed) {
                klauselmengenTeil.add(klauselmenge.get(i));
            }
            removed = false;
            if (i+1 < klauselmenge.size()){
                Klausel klausel = klauselmenge.get(i+1);
                Klauselmenge negierteKlausel = klausel.negate();

                Klauselmenge klonKlauselmengenTeil = new Klauselmenge(new LinkedList<>());
                klonKlauselmengenTeil.merge(klauselmengenTeil);
                klonKlauselmengenTeil.merge(negierteKlausel);

                if (!geordneteResolutionOhneKonsolenausgaben(ordnung, klonKlauselmengenTeil)){

                    klauselmenge.n.remove(i+1);
                    removed = true;
                    i--;
                }
            }
        }
        return klauselmenge;
    }

    public boolean geordneteResolutionOhneKonsolenausgaben(Ordnung ordnung, Klauselmenge klauselmenge) {

        if (klauselmenge.size() == 0){

            return false;
        }
        if (ordnung == null){
            ordnung = erstelleLexikografischeOrdnung(klauselmenge);
        }
        ordnung.ordnen(klauselmenge);


        for (int i = 0; i < klauselmenge.size(); i++) {
            Klausel k1 = klauselmenge.get(i);
            if (k1.size() == 0) {

                return false;

            }
            faktorisierungsregel(k1);
            Literal l1 = k1.get(0);
            for (int j = i + 1; j < klauselmenge.size(); j++) {
                Klausel k2 = klauselmenge.get(j);
                Literal l2 = k2.get(0);
                if (!l1.s.equals(l2.s) || l1.value == Literal.NOT) {
                    break;

                } else {
                    if (l1.value && l2.value == false) {
                        Klausel merge = resolutionsregel(k1, k2);
                        ordnung.ordnen(merge);
                        faktorisierungsregel(merge);
                        if (!klauselmenge.n.contains(merge)) {
                            klauselmenge.n.add(merge);
                            ordnung.ordnen(klauselmenge);
                            i = -1;
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }


    /***
     * Applies the construction of an interpretation
     * on a satisfiable clause with a given ordering.
     *
     * A model for a clause set is generated as a clause,
     * in which every literal must be true,
     * to make the clause set satisfiable.
     *
     *
     * Requirements: Clause set is saturated and ordered.
     * @param klauselmenge, clause set for which a model is constructed.
     * @param ordnung, ordering on literals.
     * @return LinkedList, list which contains a model
     *                      for the clause set.
     */
    public LinkedList<Literal> kanonischeModellgenerierung(Klauselmenge klauselmenge, Ordnung ordnung) {
        System.out.println("Ist die Klauselmenge saturiert? (Geben Sie J für Ja und N für Nein ein): ");
        Scanner in = new Scanner(System.in);
        String antwort = in.nextLine();
        if (antwort.equals("J") || antwort.equals("j") | antwort.equals("Ja") | antwort.equals("ja") | antwort.equals("Y") | antwort.equals("y") | antwort.equals("Yes") | antwort.equals("yes") ){
            System.out.println("Ein Modell wird mit der eingegeben Klauselmenge erzeugt.\n");
        }
        else if (antwort.equals("N") | antwort.equals("n") | antwort.equals("Nein") | antwort.equals("nein") | antwort.equals("No") | antwort.equals("no")){
            System.out.println("Die Klauselmenge wird vor der Modellgenerierung durch die geordnete Resolution saturiert.\n");
            geordneteResolutionOhneKonsolenausgaben(ordnung, klauselmenge);
        }
        else
            throw new IllegalArgumentException("Die Eingabe wurde nicht erkannt.");

        // Creates default model,
        // where every literal is set negative.
        ordnung.ordnen(klauselmenge);
        LinkedList<Literal> model = new LinkedList<Literal>();
        for (int i = 0; i < ordnung.size(); i++){
            if (i % 2 == 0){
                model.add(ordnung.get(i));
            }
        }
        erklaerungAusgeben(Main.erklaerungsmodus,"A ist die Menge der Literale die wahr sein müssen, um ein Modell für die Klausel zu erzeugen.");
        System.out.println("Initialisierung mit A = " + model.toString()+"\n");
        boolean nextKlausel = false;
        // Iterating over the clause of a clause set.
        for (int i = 0; i < klauselmenge.size(); i ++){
            nextKlausel = false;
            Klausel temp = klauselmenge.get(i);
            // Iterating over the literals of a clause.
            erklaerungAusgeben(Main.erklaerungsmodus, "Analysiere Klausel "+klauselmenge.get(i).toString()+":");
            for (int j = 0; j < temp.size(); j++){
                if (nextKlausel) {
                    break;
                }
                Literal literal = temp.get(j);

                // Compares the current literal of the clause
                // with every literal in the model.
                // If the literal of the clause is in the model,
                // then the clause is true under the current model,
                // and we can move to the next clause with
                // the break statement,
                // and the nextKlausel true statement.

                for (int k = 0; k < model.size(); k++){
                    Literal literal2 = model.get(k);
                    if (literal.equals(literal2)){
                        erklaerungAusgeben(Main.erklaerungsmodus,"Klausel "+ klauselmenge.get(i).toString() +" ist wahr in Modell: " + model.toString()+"\n");
                        nextKlausel = true;
                        break;
                    }
                }
            }
            // Model must be adapted
            // since clause is false in the current interpretation.
            // Model sets the maximal literal
            // of the current clause as true.
            // Clause [P,R] model [-Q,-P,-R].
            // -> new model [-Q,P,-R].
            if (!nextKlausel){
                Literal maxLiteral = new Literal();
                maxLiteral.value = false;
                maxLiteral.s = temp.get(0).s;
                for (int k = 0; k < model.size(); k++){
                    if (model.get(k).equals(maxLiteral)){
                        model.get(k).value = true;
                        erklaerungAusgeben(Main.erklaerungsmodus,"Klausel ist falsch bezüglich A.");
                        erklaerungAusgeben(Main.erklaerungsmodus, model.get(k) + " ist strikt maximal in Klausel "+ klauselmenge.get(i).toString() +". ");
                        erklaerungAusgeben(Main.erklaerungsmodus,"Setze "+ model.get(k) + " |= 1 in A." );
                        erklaerungAusgeben(Main.erklaerungsmodus,"A wird aktualisiert zu: A = "+ model.toString()+"\n");
                    }
                }
            }
        }
        StringJoiner sj = new StringJoiner(",", "{","} ");
        StringJoiner positiv = new StringJoiner(") = A(","A(",") = 1");
        StringJoiner negativ = new StringJoiner(") = A(","A(",") = 0");
        boolean notEmpty = false;
        boolean notEmpty2 = false;
        for (int i = 0; i < model.size();i++){
            sj.add(model.get(i).s);
            if (model.get(i).value){

                positiv.add(model.get(i).s);
                notEmpty = true;
            }
            else{
                negativ.add(model.get(i).s);
                notEmpty2 = true;
            }
        }
        String modell = null;
        if (notEmpty && !notEmpty2){
            modell = "A :" +sj.toString() + "-> {0,1}\n"+
                    "mit "+ positiv.toString();
        }
        if (!notEmpty && notEmpty2){
            modell = "A :" +sj.toString() + "-> {0,1}\n"+
                    "mit " + negativ.toString();
        }
        if (notEmpty && notEmpty2){
            modell = "A :" +sj.toString() + "-> {0,1}\n"+
                    "mit "+ positiv.toString() + " und " + negativ.toString();
        }
        if (!notEmpty && !notEmpty2){
            throw new IllegalStateException();
        }
        System.out.println(modell);
        return model;
    }

    public Klauselmenge filterGemeinsameKlauseln(Ordnung ordnungCommon, Klauselmenge klauselmenge) {
        Klauselmenge filteredKlauselmenge = new Klauselmenge(new LinkedList<>());
        for (int i = 0; i < klauselmenge.size();i++){
            boolean onlyCommon = true;
            for(int j = 0; j < klauselmenge.get(i).size();j++){
                boolean literalFound = false;
                for (int k = 0; k < ordnungCommon.size();k++){
                    if (klauselmenge.get(i).get(j).equals(ordnungCommon.get(k))){
                        literalFound = true;
                    }
                }
                onlyCommon &= literalFound;
                if (onlyCommon){
                    filteredKlauselmenge.add(klauselmenge.get(i));
                }
                onlyCommon = false;
            }
        }
        return filteredKlauselmenge;
    }

    public void klauselmengeList (Klauselmenge klauselmenge){
        System.out.print("[");
        for(int i = 0; i < klauselmenge.size();i++){
            Klausel klausel = klauselmenge.get(i);
            if(i == klauselmenge.size() -1){
                System.out.println(klausel.toString() + "]");
            }
            else{
                System.out.println(klausel.toString() + ";");
            }
        }
    }

    public void interpolant(Ordnung pureOrdnungA,Ordnung ordnungCommon, Ordnung pureOrdnungB, Klauselmenge klauselmengeA, Klauselmenge klauselmengeB) {
        Ordnung ordnungALL = new Ordnung(new LinkedList<>());
        ordnungALL.o.addAll(pureOrdnungA.o);
        ordnungALL.o.addAll(ordnungCommon.o);
        ordnungALL.o.addAll(pureOrdnungB.o);
        System.out.println("Folgende Ordnung wurde für die geordnete Resolution erstellt: " + ordnungALL);

        Ordnung ordnungA = new Ordnung(new LinkedList<>());
        ordnungA.o.addAll(pureOrdnungA.o);
        ordnungA.o.addAll(ordnungCommon.o);

        Klauselmenge mergedFormulas = new Klauselmenge(new LinkedList<>());
        mergedFormulas.addAll(klauselmengeA);
        mergedFormulas.addAll(klauselmengeB);

        if (!geordneteResolutionOhneKonsolenausgaben(ordnungALL, mergedFormulas)) {
            System.out.println("Klauselmenge A vereinigt mit Klauselmenge B ist unerfüllbar.\n" +
                    "Es existiert eine Interpolante.\n");
            geordneteResolutionOhneKonsolenausgaben(ordnungA, klauselmengeA);
            System.out.println("Das ist die saturierte Klauselmenge A: " + klauselmengeA);
            erklaerungAusgeben(Main.erklaerungsmodus, "Für die Interpolante werden nur Klauseln berücksichtigt, deren Variablen in Formel A und in Formel B auftauchen.");
            Klauselmenge interpolante = filterGemeinsameKlauseln(ordnungCommon, klauselmengeA);
            redundanzEliminationOhneKonsolenausgaben(interpolante, ordnungCommon);
            System.out.println("Das ist die Interpolante: I = " + interpolante);

        } else {
            System.out.println("Klauselmenge A vereinigt mit Klauselmenge B ist erfüllbar.\n" +
                    "ERROR: Es existiert keine Interpolante.\n");
        }
    }
}
