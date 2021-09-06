package Evaluator.Treenode;

import java.util.StringJoiner;
 public class Literal extends ValueNode<String> {
    //Evaluator.Treenode.Literal ist ein Tupel aus Wahrheitswert und Aussagevariable


    public static final boolean NOT = false;
    //statt false,'A' kann man auch not,'A' schreiben


    public boolean value = true;
    //setze alle Aussagenvariablen standardmäßig positiv

    public String s;
    //Aussagevariable

    /***
     * Konstruktor für  Literale (not,'A'), (true,'A')
     * @param value Wahrheitswert
     * @param s Atom
     */
    private Literal(boolean value, String s){
        super(value,s);
        this.value = value;
        this.s = s;
    }

    /***
     * Konstruktor für positive Literale ('A') -> (true,'A'), da Wahrheitswert standardmäßig true
     * @param s Atom
     */
    Literal(String s){
        super(s);
        this.s = s;
    }


    /***
     * Konstruktur für ein leeres Evaluator.Treenode.Literal
     */
    public Literal() {
        super();

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

    @Override
    public String getNodeType() {
        return this.toString();
    }
}
