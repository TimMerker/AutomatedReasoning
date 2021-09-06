package Evaluator.List;

import java.util.StringJoiner;

public class Literal {
    /***
     * Literal is a tuple of a boolean value, and a propositional variable.
     */

    public static final boolean NOT = false;

    /**
     * Initailly set all the values of the literals to positive (true).
     */
    public boolean value = true;

    /**
     * Propositional variable.
     */
    public String s;


    /***
     * Konstruktor für  Literale (not,'A'), (true,'A')
     * @param value Wahrheitswert
     * @param s Atom
     */
    public Literal(boolean value, String s){
        this.value = value;
        this.s = s;
    }

    /***
     * Constructor for a literal
     * @param s Atom
     */
    public Literal(String s){
        this.s = s;
    }


    /***
     * Constructor for a literal.
     */
    public Literal() {
    }

    /***
     * Negiert ein eingegebenes Evaluator.Treenode.Literal indem der Wahrheitswert umgedreht wird,
     * und gibt das negierte Ltieral zurück
     *
     * @param l Evaluator.Treenode.Literal, dessen Wahrheitswert gedreht wird
     * @return Evaluator.Treenode.Literal negiert(l)
     */
    public Literal negate(Literal l){
        Literal l1 = new Literal(!l.value,l.s);
        return l1;
    }

    /***
     *  Gibt die Anzahl der Zeichen eines Literals zurück
     * @param
     * @return int, Zeichenlänge des Literals  (die Länge des Wortes)
     */
    public int size(){
        return this.s.length();
    }

    /***
     * Überprüft ob dieses Evaluator.Treenode.Literal gleich dem eingegebenem Objekt ist.
     *
     * Das Obejekt wird zum Evaluator.Treenode.Literal umgewandelt und es wird überprüft, ob:
     * 1.Der Wahrheitswert von diesem Evaluator.Treenode.Literal mit dem Wahrheitswert des Objektes gleich ist und
     * 2.das Atom von diesem Evaluator.Treenode.Literal mit dem Atom des Objektes gleich ist.
     * Falls die Bedingungen erfüllt sind, wird true zurückgegeben, sonst false.
     *
     * @param obj
     * @return boolean Wert = Ob dieses Evaluator.Treenode.Literal gleich dem Objekt ist
     */
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Literal)){
            return false;
        }
        Literal comparedliteral = (Literal) obj;
        if(this.value == comparedliteral.value && this.s.equals(comparedliteral.s)){
            return true;
        }
        else{
            return false;
        }
    }

    /***
     *
     * Wichtig für Konsolenausgabe: setze jedes Evaluator.Treenode.Literal in Klammern und schiebe bei Bedarf ein - davor
     * false(A) -> -A
     * true(P) -> P
     * @return Evaluator.Treenode.Literal als String
     */
    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("", "","");
        sj.add(s);
        if (value == false) {
            return "-" + sj.toString();
        }
        return sj.toString();
    }

}
