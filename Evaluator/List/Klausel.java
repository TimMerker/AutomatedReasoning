package Evaluator.List;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringJoiner;

/***
 * implements Iterable damit man mit foreach über Klausel iterieren kann
 */
public class Klausel implements Iterable<Literal>  {
    /***
     * Linked List of literals.
     */
    public LinkedList<Literal> l;

    /***
     * Constructor for a clause.
     * @param L, linked list of literals.
     */
    public Klausel(LinkedList<Literal> L) {
        this.l = L;
    }


    /**
     * Literale können mit add in die Klausel (i.e. Literalenliste) hinzugefügt werden
     *
     * @param literal
     */
    public void add(Literal literal) {
        l.add(literal);
    }

    /**
     * Alle Literale einer eingegebenen Klausel werden in diese Klausel hinzugefügt
     *
     * @param klausel
     */
    public void addAll(Klausel klausel) {
        l.addAll(klausel.l);
    }

    /**
     * Überprüft ob die Klausel noch ein Literal nach dem eingegeben Literal enthält
     *
     * @param literal
     * @return
     */
    public boolean hasNext(Literal literal) {
        return l.getLast() != literal;
    }


    /**
     * Gibt den Index des nächsten Literals eines gegebenen Literals aus dieser Klausel zurück
     *
     * @param literal
     * @return index des nächsten Literals
     */
    public int getNext(Literal literal) {
        if (hasNext(literal)) {
            return l.indexOf(literal) + 1;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Gibt das Literal mit index i aus dieser Klausel zurück
     *
     * @param index
     * @return Literal mit index i aus der Klausel
     */
    public Literal get(int index) {
        int j = 0;
        Iterator iterator = this.iterator();
        while (iterator.hasNext() && j < index) {

            iterator.next();
            j++;
        }
        return (Literal) iterator.next();
    }

    /***
     * Nimmt diese Klausel und negiert diese.
     *
     * Es entstehen neue Klauseln die jeweils 1 Literal enthalten.
     * Die Literale sind negiert.
     * @return Klauselmenge aus negierten Klauseln
     */
    public Klauselmenge negate() {
        Klauselmenge n = new Klauselmenge(new LinkedList<>());
        for (Literal l : this.l) {
            Literal l1 = l.negate(l);
            Klausel neueKlausel = new Klausel(new LinkedList<>());
            neueKlausel.add(l1);
            n.add(neueKlausel);
        }
        return n;
    }


    /***
     * Klausel als String wird als Aufzählung von Literalen als String zurückgegeben.
     *
     * Stringbuilder um alle Literale miteinander durch , zu trennen und in [] zu setzen
     * Wird in der systemoutprint methode verwendet um den output zu formen statt die speicheradresse zurückzugeben
     *
     * @return Klausel als String
     */
    public String toString() {
        StringJoiner sj = new StringJoiner(",", "[", "]");
        for (Literal l : l) {
            sj.add(l.toString());
        }
        return sj.toString();
    }


    @Override
    public Iterator<Literal> iterator() {
        return getLiterals();
    }

    private Iterator<Literal> getLiterals() {
        return l.iterator();
    }


    /***
     * Überprüft ob eine eingegebene Klausel gleich dieser Klausel ist.
     *
     * Indem überprüft wird
     * 1. ob beide Klauseln leer sind -> true
     * 2. ob die Klauseln eine unterschiedliche Länge haben -> false
     * 3. ob die beiden Klauseln die gleich Literale in der gleichen Reihenfolge enthalten
     * @param obj
     * @return boolean wert = ob dieses Objekt gleich dem eingegebenem ist.
     */

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Klausel)) {
            return false;
        }
        Klausel comparedKlausel = (Klausel) obj;
        if (this.size() == 0 && comparedKlausel.size() == 0) {
            return true;
        }
        if (this.size() != comparedKlausel.size()) {
            return false;
        } else {
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).equals(comparedKlausel.get(i))) {
                    return false;
                }
            }
            return true;
        }

    }

    /***
     * Gibt die größe der Klausel zurück, = Anzahl der Literale
     *
     * @return int, Anzahl der Literale
     */
    public int size() {
        return l.size();
    }
}
