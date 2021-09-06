package Evaluator.List;

import java.util.LinkedList;

public class OrdnungEvaluator {
    /***
     * Nimmt einen String (Ordnung) und erstellt die dazugehörige Datenstruktur Ordnung.
     *
     * Der String sollte die Form [A,B,C] haben. A,B,C ist auch möglich.
     *
     * @param formel2, die eingegebene Ordnung als String
     * @return die zu dem String erstellte Datenstruktur Ordnung
     */
    public Ordnung evaluate(String formel2) {

        Ordnung o1 = new Ordnung(new LinkedList<>());

        formel2 = formel2.replaceAll("\\s", "");
        formel2 = formel2.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        String[] s = formel2.split(",");
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
            o1.add(l1);
        }
        return o1;
    }
}
