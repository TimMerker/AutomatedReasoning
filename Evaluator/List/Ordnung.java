package Evaluator.List;

import Evaluator.Comparable;

import java.util.*;

public class Ordnung implements Comparable<Literal>, Iterable<Literal> {
    public static final boolean NOT = false;
    /***
     * The ordering is a linked list of literals, with some constraints.
     */
    public LinkedList<Literal> o;

    /***
     * Constructor for an ordering.
     * Literals are sorted in a descending order.
     *
     * @param o, linked list of positive literals, without duplicates.
     */
    public Ordnung(LinkedList<Literal> o){
        if (enthaeltDuplikate(o))
            throw new IllegalArgumentException("Ein Literal kommt " +
                    "in der Ordnung doppelt vor");
        this.o = o;
        this.addNegativeLiterale();
    }

    public static Ordnung pureOrdnung(Klauselmenge klauselmenge1, Klauselmenge klauselmenge2) {
        Ordnung pureOrdnung = new Ordnung(new LinkedList<>());
        for (int i = 0; i < klauselmenge1.size();i++){
            for (int j = 0; j < klauselmenge1.get(i).size();j++){
                boolean pure = true;
                for (int k = 0; k < klauselmenge2.size();k++){
                    for (int l = 0; l < klauselmenge2.get(k).size();l++){
                        String l1 = klauselmenge1.get(i).get(j).s;
                        String l2 = klauselmenge2.get(k).get(l).s;
                        if (l1.equals(l2)){
                            pure = false;
                        }
                    }
                }
                if (pure){
                    pureOrdnung.add(new Literal (true, klauselmenge1.get(i).get(j).s));
                }
            }
        }
        return pureOrdnung;
    }

    public static Ordnung commonOrdnung(Klauselmenge klauselmenge1, Klauselmenge klauselmenge2) {
        Ordnung commonOrdnung = new Ordnung(new LinkedList<>());
        for (int i = 0; i < klauselmenge1.size();i++){
            for (int j = 0; j < klauselmenge1.get(i).size();j++){
                boolean common = false;
                for (int k = 0; k < klauselmenge2.size();k++){
                    for (int l = 0; l < klauselmenge2.get(k).size();l++){
                        String l1 = klauselmenge1.get(i).get(j).s;
                        String l2 = klauselmenge2.get(k).get(l).s;
                        if (l1.equals(l2)){
                            common = true;
                        }
                    }
                }
                if (common){
                    commonOrdnung.add(new Literal (true, klauselmenge1.get(i).get(j).s));
                }
            }
        }
        return commonOrdnung;
    }


    /***
     * Fügt ein Literal in die Ordnungsliste hinzu.
     *
     * Vorr.: User darf nur positive Literale eingeben -> Ansonsten Exception
     * Fügt ein positives Literal in die Ordnungsliste hinzu.
     * Erstellt automatisch das negative Literal und fügt dieses vor das positive Literal
     * @param l
     */
    public void add(Literal l){
        Literal ln = new Literal(NOT, l.s);
        if (l.value == false){
            throw new IllegalArgumentException("Der Nutzer darf nur positive Literale in die Ordnung einfügen");
        }
        if (!o.contains(l)) {
            o.add(ln);
            if (!o.contains(l)) {
                o.add(l);
            }
        }
    }


    /***
     * Überprüft ob die Ordnung noch ein Literal nach dem eingegebenem Literal enthält.
     *
     * @param l
     * @return boolean Wert = ob das eingegebene Literal das letzte in der Klausel ist.
     */
    private boolean hasNext(Literal l){
        if (this.o.size() == 0){
            return false;
        }
        else{
            return o.getLast() != l;
        }
    }


    /***
     * Gibt den index des nächsten Literals aus dieser Ordnungsliste zurück
     * @param literal
     * @return int index
     */
    private int getNext(Literal literal){
        if (hasNext(literal)){
            return o.indexOf(literal)+1;
        }
        throw new IndexOutOfBoundsException();
    }


    /***
     * Es wird ein Literal genommen und mit jedem anderem Literal verglichen.
     * Falls zwei Literale gleich sind wird true zurückgegeben
     * @param l, Listen von Literalen als Ordnung
     * @return boolean, true wenn die Ordnung Duplikate enthält, sonst false
     */
    private boolean enthaeltDuplikate(LinkedList<Literal> l){

        for (int i = 0; i < l.size(); i++) {
            for (int j = i + 1; j < l.size(); j++){
                if (l.get(i) == l.get(j)){
                    return true;
                }
            }
        }
        return false;

    }

    /***
     * Ordnung als String wird als Aufzählung von Literalen als String zurückgegeben.
     *
     * Für z.B. Konsolenausgabe: Wird in der systemoutprint methode verwendet um den output zu formen,
     * statt die speicheradresse zurückzugeben.
     * Stringbuilder um alle Literale durch, zu trennen und die Ordnung in [] zu setzen
     * @return Ordnung als String
     */
    public String toString(){
        StringJoiner sj = new StringJoiner(",","[","]");
        for (Literal l: o) {
            sj.add(l.toString());
        }
        return sj.toString();
    }


    /***
     * Fügt für jedes Literal das in dieser Ordnung vorkommt ein negatives Literal davor hinzu.
     *
     * Es wird über die Ordnungsliste iteriert.
     * Das gegenwärtige Literal kopiert und danach negiert.
     * Die Kopie wird nach dem gegenwärtigen Listel hinzugefügt.
     */

    private void addNegativeLiterale() {

        ListIterator<Literal> iterator = (ListIterator<Literal>) this.iterator();
        while (iterator.hasNext()) {
            Literal l = iterator.next();
            l.value = NOT;
            Literal ln = new Literal(true, l.s);
            if (!this.o.contains(ln)) {
                iterator.add(ln);
            }
        }
    }


    /***
     * Ordnet die Literale einer Klausel nach dieser Ordnung entsprechend (inplace, Selectionsort)
     * Absteigend
     * @param klausel
     */
    public void ordnen(Klausel klausel){
        testLiteraleInOrdnung(klausel);
        for (int i = 0; i < klausel.size(); i++){
            Literal kliteral1 = klausel.get(i);
            for (int j = i + 1; j < klausel.size();j++){
                Literal kliteral2 = klausel.get(j);
                int position1 = o.indexOf(kliteral1);
                int position2 = o.indexOf(kliteral2);
                if (greaterThan(o.get(position2),o.get(position1))) {
                    Collections.swap(klausel.l,i,j);
                    kliteral1 = klausel.get(i);
                }

            }
        }
    }
    /***
     * Ordnet eine Klauselmenge und ihre Klauseln nach dieser Ordnung.
     *
     * 1.Ordnet die Literale jeder Klausel aus einer Klauselmenge nach dieser Ordnung entsprechend (Absteigend)
     * 2.Ordnet die Klauseln einer Klauselmenge nach dieser Ordnung entsprechend anhand des maximalen Literals (Aufsteigend)
     * Bei Gleichheit wird das nächste Literal betrachtet, wobei kein Literal < irgendein Literal*
     *
     * Sortieren der Klauseln nach Insertionsort.
     *
     * @param klauselmenge
     */

    public void ordnen(Klauselmenge klauselmenge){
        for (Klausel klausel:klauselmenge) {
            ordnen(klausel);
        }


        int n = klauselmenge.size();
        for(int i = 1;i < n;i++){
            int j = i - 1;
            Klausel klausel1 = klauselmenge.get(j);
            Klausel klausel2 = klauselmenge.get(j+1);
            while(j >= 0 && kleinereKlausel(klausel2,klausel1)){
                Collections.swap(klauselmenge.n,j,j+1);
                j--;
                klausel1 = klauselmenge.get(j);
                klausel2 = klauselmenge.get(j+1);
            }
        }
    }


    /**
     * Prüft ob jedes Literal in der Klausel in dieser Ordnungsliste enthalten ist
     * @param klausel
     */
    private void testLiteraleInOrdnung(Klausel klausel) {
        for (Literal literal: klausel) {
            if (!this.o.contains(literal)){
                throw new NoSuchElementException("Die Klausel enthält ein Literal, " +literal.toString() +
                        " dass nicht in der Ordnung enthalten ist");
            }
        }
    }
    /**
     * Vergleicht 2 Klausel und gibt true zurück, wenn die erste Klausel durch Vergleich der Literale
     * mit den Literalen der zweiten Klausel anhand der Ordnungsliste kleiner ist.
     *
     * Notiz: Leere Klauseln sind die kleinsten Klauseln.
     * Falls ein Literal l1 < l2 und wir zwei Klauseln [1,...k,l1] [1,...,k,l2] haben, ist die erste Kleiner
     * Falls 2 Klauseln identisch sind bis zu einem [1,...,k],[1,...,k,...,n] ist die erste Klausel kleiner
     *
     * @param klausel1
     * @param klausel2
     * @return Boolean Wert = ob klausel1 kleiner als klausel2 ist
     */
    public boolean kleinereKlausel(Klausel klausel1, Klausel klausel2) {

        if (klausel1.size() == 0) {
            return true;
        } else if (klausel2.size() == 0) {
            return false;
        }


        int minSize = 0;
        boolean klausel1Smaller = false;
        if (klausel1.size() < klausel2.size()) {
            minSize = klausel1.size();
            klausel1Smaller = true;
        } else {
            minSize = klausel2.size();
        }
        for (int i = 0; i < minSize; i++) {
            if (greaterThan(klausel1.get(i), klausel2.get(i))) {
                return false;
            }
            if (greaterThan(klausel2.get(i), klausel1.get(i))) {
                return true;
            }
        }
        return klausel1Smaller;
    }
    /***
     * Überprüft ob eine eingegebene Ordnung gleich dieser Ordnung ist.
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
        if (!(obj instanceof Ordnung)) {
            return false;
        }
        Ordnung comparedOrdnung = (Ordnung) obj;
        if (this.size() == 0 && comparedOrdnung.size() == 0) {
            return true;
        }
        if (this.size() != comparedOrdnung.size()) {
            return false;
        } else {
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).equals(comparedOrdnung.get(i))) {
                    return false;
                }
            }
            return true;
        }

    }

    /**
     * Gibt das Literal mit index i aus dieser Ordnung zurück
     *
     * @param index
     * @return Literal mit index i aus der Ordnung
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
     * Gibt die größe der Ordnung zurück, = Anzahl der Literale
     *
     * @return int, Anzahl der Literale
     */
    public int size() {
        return o.size();
    }

    /***
     * Legt die Literale über die bei der Ordnung iteriert werden soll fest
     * @return Iterator<Literal>
     */
    @Override
    public Iterator<Literal> iterator() {
        return getLiterals();
    }

    private Iterator<Literal> getLiterals() {
        return o.iterator();
    }

    /**
     * Vergleicht 2 Literale und gibt true zurück, wenn das erste Literal in diesr Ordnungsliste weiter vorne steht.
     *
     * -> also einen kleineren Index hat
     * Je weiter vorne desto wichtiger.
     *
     * @param literal1
     * @param literal2
     * @return Boolean Wert = ob literal 1 größer ist als literal 2
     */
    @Override
    public boolean greaterThan(Literal literal1, Literal literal2) {
        return o.indexOf(literal1) < o.indexOf(literal2);
    }


}
