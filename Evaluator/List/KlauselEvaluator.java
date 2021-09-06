package Evaluator.List;

import java.util.LinkedList;

public class KlauselEvaluator {
    /***
     * Liest einen String ein und gibt die Datenstruktur Klausel zurück.
     *
     * Der String muss die Form [A,B,C] haben.
     *
     * @param formel3
     * @return Klausel, sodass die Algorithmen angewendet werden können.
     */
    public Klausel evaluate(String formel3) {
        Klausel k1 = new Klausel(new LinkedList<>());
        formel3 = formel3.replaceAll("\\s", "");
        formel3 = formel3.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        String[] s = formel3.split(",");

        for (int i = 0; i < s.length; i++) {
            Literal l1 = new Literal();

            String stringLiteral = s[i];
            if (stringLiteral.startsWith("-")) {
                l1.value = Literal.NOT;
                stringLiteral = stringLiteral.substring(1);
                l1.s = stringLiteral;
            } else {
                l1.value = true;
                l1.s = stringLiteral;
            }
            k1.add(l1);
        }
        return k1;
    }
}
