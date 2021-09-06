package Evaluator.List;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringJoiner;

public class Klauselmenge implements Iterable<Klausel> {

    /***
     * A clause set is a linked list of clauses.
     */
    public LinkedList<Klausel> n;

    /***
     * Constructor for a clause set.
     * @param n,
     */
    public Klauselmenge(LinkedList<Klausel> n){
        this.n = n;
    }

    /***
     * Klauseln können mit add in die Evaluator.List.Klauselmenge (i.e. Klauselliste) hinzugefügt werden
     * @param k
     */
    public void add(Klausel k){
        n.add(k);
    }

    /***
     * Überprüft ob die eingegebene Evaluator.List.Klausel nicht die letzte Evaluator.List.Klausel in der Evaluator.List.Klauselmenge ist
     * @param k
     * @return boolean k ist nicht das letzte
     */
    public boolean hasNext(Klausel k){
        if (n.getLast() != k) {
            return true;
        }
        return false;
    }

    /***
     * Evaluator.List.Klauselmenge als String wird als Aufzählung von Klauseln als String zurückgegeben.
     *
     * Stringbuilder um alle Klauseln miteinander durch , zu trennen und in [] zu setzen
     * Wird in der systemoutprint methode verwendet um den output zu formen statt die speicheradresse zurückzugeben
     * @return String
     */
    public String toString(){
        StringJoiner sj = new StringJoiner(";","[","]");
        for (Klausel k: n) {
            sj.add(k.toString());

        }
        return sj.toString();
    }

    /***
     * Fügt alle Klauseln aus der eingegebenen Evaluator.List.Klauselmenge in diese Evaluator.List.Klauselmenge hinzu
     * @param klauselmenge
     */
    public void addAll(Klauselmenge klauselmenge){
        n.addAll(klauselmenge.n);
    }


    /***
     * Nimmt eine Evaluator.List.Klauselmenge und fügt alle Klauseln in diese Evaluator.List.Klauselmenge hinzu.
     * Fügt zwei Klauselmengen zusammen.
     * @param klauselmenge
     */
    public void merge(Klauselmenge klauselmenge){
        this.addAll(klauselmenge);

    }


    @Override
    public Iterator<Klausel> iterator() {
        return getKlausel();
    }

    public Iterator<Klausel> getKlausel() {
        return n.iterator();
    }

    /***
     *  Gibt die Größe der Evaluator.List.Klauselmenge zurück. = Anzahl der Klauseln
     * @return die Größe
     */
    public int size() {
        return n.size();
    }


    /**
     * Gibt die Evaluator.List.Klausel mit index i aus dieser Evaluator.List.Klauselmenge zurück
     * @param index
     * @return Evaluator.List.Klausel mit index i aus dieser Evaluator.List.Klauselmenge
     */
    public Klausel get(int index) {
        int j = 0;
        Iterator<Klausel> iterator = this.iterator();
        while (iterator.hasNext() && j < index){

            iterator.next();
            j++;
        }
        return (Klausel) iterator.next();
    }

    /***
     * Gibt die Größe der größten Evaluator.List.Klausel aus dieser Evaluator.List.Klauselmenge zurück
     * @return die Größe der größten Evaluator.List.Klausel
     */
    public int getMaxKlauselSize() {
        int max = 0;
        for (Klausel klausel: this) {
            if (klausel.size() > max){
                max = klausel.size();
            }
        }
        return max;
    }

    public String toCnf() {
        String klauselmenge = this.toString();
        klauselmenge = klauselmenge.replaceAll(";"," & ");
        klauselmenge = klauselmenge.replaceAll(","," | ");
        klauselmenge = klauselmenge.replaceAll("\\[","(");
        klauselmenge = klauselmenge.replaceAll("\\]",")");
        return klauselmenge;
    }
}
