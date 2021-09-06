package Evaluator.List;

import java.util.LinkedList;

public class KlauselmengeEvaluator {
    /***
     * Liest einen String (Formel in CNF) ein und gibt die Datenstruktur Klauselmenge zurück.
     *
     * Der String sollte die Form [[A,B];[-A,C];[C]] haben.
     * Die Form ohne eckigen Klammern ist auch möglich. A,B;-A,C;C
     *
     * Die eckigen Klammern und die Leerzeichen werden für die weitere Verarbeitung durch Regex entfernt.
     * @param formel, in CNF. klassische Klauselmenge
     * @return Klauselmenge, sodass die Algorithmen angewendet werden können.
     */
    public Klauselmenge evaluateSet(String formel) {

        Klauselmenge klauselmenge = new Klauselmenge(new LinkedList<>());
        formel = formel.replaceAll("\\s","");
        formel = formel.replaceAll("[\\p{Ps}\\p{Pe}]","");
        String[] s = formel.split(";");

        for (int i = 0; i < s.length; i++){
            Klausel k1 = new Klausel(new LinkedList<>());
            String stringKlausel = s[i];
            String[] literale = stringKlausel.split(",");

            for (int j = 0; j < literale.length; j++){
                Literal l1 = new Literal();

                String stringLiteral = literale[j];
                if (stringLiteral.startsWith("-")){
                    l1.value = Literal.NOT;
                    stringLiteral = stringLiteral.substring(1);
                    l1.s = stringLiteral;
                }
                else{
                    l1.value = true;
                    l1.s = stringLiteral;
                }
                k1.add(l1);
            }
            klauselmenge.add(k1);
        }
        return klauselmenge;

    }

    /***
     * Liest einen String (Formel in CNF) ein und gibt die Datenstruktur Klauselmenge zurück.
     *
     * Der String muss die Form ((((-A | -C) | -C) & ((B | -C) | -C)) & ((C | (A | -B)) & (C | C))) haben.
     * @param cnfString in CNF, noch nicht als Klauselmenge.
     * @return Klauselmenge, sodass die Algorithmen angewendet werden können.
     */
    public Klauselmenge evaluateFormula(String cnfString) {
        Klauselmenge klauselmenge = new Klauselmenge(new LinkedList<>());
        cnfString = cnfString.replaceAll("\\s","");
        cnfString = cnfString.replaceAll("[\\p{Ps}\\p{Pe}]","");
        String[] s = cnfString.split("&");
        for (int i = 0; i < s.length; i++){
            Klausel k1 = new Klausel(new LinkedList<>());
            String stringKlausel = s[i];
            String[] literale = stringKlausel.split("\\|");

            for (int j = 0; j < literale.length; j++){
                Literal l1 = new Literal();

                String stringLiteral = literale[j];
                if (stringLiteral.startsWith("-")){
                    l1.value = Literal.NOT;
                    stringLiteral = stringLiteral.substring(1);
                    l1.s = stringLiteral;
                }
                else{
                    l1.value = true;
                    l1.s = stringLiteral;
                }
                k1.add(l1);
            }
            klauselmenge.add(k1);
        }
        return klauselmenge;

    }
}
