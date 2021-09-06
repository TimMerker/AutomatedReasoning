package Test;

import Algorithmen.Resolution;
import Evaluator.List.Klausel;
import Evaluator.List.Klauselmenge;
import Evaluator.List.Literal;
import Evaluator.List.Ordnung;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResolutionTest {
    Literal l1 = new Literal("A");
    Literal l2 = new Literal(Literal.NOT, "A");
    Literal l3 = new Literal("P");
    Literal l4 = new Literal("B");
    Literal l5 = new Literal(Literal.NOT, "B");
    Literal l6 = new Literal("B");
    Literal l7 = new Literal("A");
    @Test
    public void faktorisierungsregelTest() {
        Ordnung o3 = new Ordnung(new LinkedList<>());
        o3.add(l1);
        o3.add(l3);
        o3.add(l4);

        Klausel k8 = new Klausel(new LinkedList<>());
        k8.add(l1);
        k8.add(l2);
        k8.add(l1);
        k8.add(l1);
        k8.add(l2);
        k8.add(new Literal(Literal.NOT, l2.s));

        Resolution R = new Resolution();
        System.out.println("Das ist die Klausel vor der Faktorisierung" + k8.toString());
        o3.ordnen(k8);
        R.faktorisierungsregel(k8);
        System.out.println("Das ist die Klausel nach der Faktorisierung" + k8.toString());
        assertEquals("[-A,A]",k8.toString());

        Klausel k9 = new Klausel(new LinkedList<>());
        System.out.println("Das ist die Klausel vor der Faktorisierung" + k9.toString());
        o3.ordnen(k9);
        R.faktorisierungsregel(k9);
        System.out.println("Das ist die Klausel nach der Faktorisierung" + k9.toString());
        assertEquals("[]",k9.toString());
    }

    @Test
    public void resolutionsregelTest(){
        Klausel k10 = new Klausel(new LinkedList<>());
        k10.add(l1);
        k10.add(l4);
        k10.add(l3);

        Klausel k11 = new Klausel(new LinkedList<>());
        k11.add(new Literal(Literal.NOT,l1.s));
        k10.add(l4);
        k10.add(l3);

        Ordnung o3 = new Ordnung(new LinkedList<>());
        o3.add(l1);
        o3.add(l3);
        o3.add(l4);

        Resolution r = new Resolution();

        o3.ordnen(k10);
        o3.ordnen(k11);

        System.out.println("Das sind die Klauseln vor der Resolutionsregel" + k10.toString() +"und"+ k11.toString());
        Klausel result1 = r.resolutionsregel(k10,k11);
        System.out.println("Das sind die Klauseln nach der Resolutionsregel " + k10.toString() +" und "+ k11.toString() + " Ergebnis: " + result1.toString());

        assertEquals("[P,P,B,B]",result1.toString());

    }
    
    @Test
    public void geordneteResolutionTest1(){
        Klausel k1 = new Klausel(new LinkedList<Literal>());
        k1.add(l1);
        k1.add(l2);
        k1.add(l3);
        Klausel k2 = new Klausel(new LinkedList<Literal>());
        k2.add(l1);
        k2.add(l2);
        Klausel k3 = new Klausel(new LinkedList<Literal>());
        k3.add(l3);
        Klausel k4 = new Klausel(new LinkedList<Literal>());
        Klausel k5 = new Klausel(new LinkedList<>());
        k5.add(l4);
        k5.add(l3);
        k5.add(l1);
        Klausel k6 = new Klausel(new LinkedList<>());
        k6.add(l4);
        k6.add(l4);
        k6.add(new Literal(Literal.NOT,l4.s));
        k6.add(l3);
        k6.add(new Literal(Literal.NOT,l3.s));
        k6.add(new Literal(Literal.NOT,l3.s));
        k6.add(l1);
        Klausel k7 = new Klausel(new LinkedList<>());
        k7.add(new Literal(Literal.NOT,l3.s));
        k7.add(new Literal(Literal.NOT,l1.s));
        k7.add(new Literal(Literal.NOT,l4.s));

        Ordnung o1 = new Ordnung(new LinkedList<>());
        o1.add(l1);
        o1.add(l3);
        o1.add(l4);
        
        Klauselmenge n4 = new Klauselmenge(new LinkedList<>());
        n4.add(k1);
        n4.add(k2);
        n4.add(k3);
        n4.add(k5);
        n4.add(k5);
        n4.add(k6);
        n4.add(k7);

        Resolution r = new Resolution();

        r.geordneteResolution(o1,n4);
    }
    @Test
    //Tests funktionieren nicht weil bei ERfüllbnarkeit für die Modellgenerierung eine Nutzereingabe erwartet wird,
    //ob die Klauselmenge saturiert ist.
    public void geordneteResolutionTest2(){
        Literal l1 = new Literal("R");
        Literal l2 = new Literal("Q");
        Literal l3 = new Literal("P");
        Literal l4 = new Literal(Literal.NOT, "R");
        Literal l5 = new Literal(Literal.NOT,"Q");
        Literal l6 = new Literal(Literal.NOT,"P");

        Ordnung ordnung = new Ordnung(new LinkedList<>());
        ordnung.add(l1);
        ordnung.add(l2);
        ordnung.add(l3);

        Klausel k1 = new Klausel(new LinkedList<>());
        k1.add(l2);
        k1.add(l3);

        Klausel k2 = new Klausel(new LinkedList<>());
        k2.add(l5);

        Klausel k3 = new Klausel(new LinkedList<>());
        k3.add(l1);
        k3.add(l6);
        k3.add(l2);

        Klausel k4 = new Klausel(new LinkedList<>());
        k4.add(l6);
        k4.add(l4);

        Klauselmenge n1 = new Klauselmenge(new LinkedList<Klausel>());
        n1.add(k3);
        n1.add(k1);
        n1.add(k2);
        n1.add(k4);

        Resolution r1 = new Resolution();

        r1.geordneteResolution(ordnung, n1);
    }
    @Test
    public void geordneteResolutionTest3(){
        Literal l1 = new Literal("Q12");
        Literal l2 = new Literal("R4");
        Literal l3 = new Literal("P");
        Literal l4 = new Literal("U");
        Literal l5 = new Literal(Literal.NOT,"Q12");
        Literal l6 = new Literal(Literal.NOT,"R4");
        Literal l7 = new Literal(Literal.NOT,"P");
        Literal l8 = new Literal(Literal.NOT,"U");
        Literal l9 = new Literal("S");
        Literal l10 = new Literal(Literal.NOT,"S");

        Klausel k1 = new Klausel(new LinkedList<>());
        k1.add(l5);
        k1.add(l2);
        k1.add(l7);
        k1.add(l8);

        Klausel k2 = new Klausel(new LinkedList<>());
        k2.add(l5);
        k2.add(l7);
        k2.add(l9);

        Klausel k3 = new Klausel(new LinkedList<>());
        k3.add(l3);
        k3.add(l5);

        Klausel k4 = new Klausel(new LinkedList<>());
        k4.add(l10);
        k4.add(l6);

        Klausel k5 = new Klausel(new LinkedList<>());
        k5.add(l1);

        Klausel k6 = new Klausel(new LinkedList<>());
        k6.add(l2);
        k6.add(l4);

        Klauselmenge n1 = new Klauselmenge(new LinkedList<>());
        n1.add(k6);
        n1.add(k4);
        n1.add(k1);
        n1.add(k2);
        n1.add(k3);
        n1.add(k5);

        Resolution r1 = new Resolution();
        r1.geordneteResolution(null,n1);

    }
    @Test
    public void geordneteResolutionTest4(){
        Literal l1 = new Literal("P");
        Literal l2 = new Literal("Q");
        Literal l3 = new Literal("R");
        Literal l4 = new Literal("S");
        Literal l5 = new Literal("U");
        Literal l6 = new Literal(Literal.NOT,"P");
        Literal l7 = new Literal(Literal.NOT,"Q");
        Literal l8 = new Literal(Literal.NOT,"R");
        Literal l9 = new Literal(Literal.NOT,"S");
        Literal l10 = new Literal(Literal.NOT,"U");

        Ordnung ordnung = new Ordnung(new LinkedList<>());
        ordnung.add(l1);
        ordnung.add(l2);
        ordnung.add(l3);
        ordnung.add(l4);
        ordnung.add(l5);

        Klausel k1 = new Klausel(new LinkedList<>());
        k1.add(l10);
        k1.add(l3);
        k1.add(l7);
        k1.add(l6);

        Klausel k2 = new Klausel(new LinkedList<>());
        k2.add(l4);
        k2.add(l7);
        k2.add(l6);

        Klausel k3 = new Klausel(new LinkedList<>());
        k3.add(l7);
        k3.add(l1);

        Klausel k4 = new Klausel(new LinkedList<>());
        k4.add(l8);
        k4.add(l9);

        Klausel k5 = new Klausel(new LinkedList<>());
        k5.add(l2);

        Klausel k6 = new Klausel(new LinkedList<>());
        k6.add(l5);
        k6.add(l3);

        Klauselmenge n1 = new Klauselmenge(new LinkedList<>());
        n1.add(k5);
        n1.add(k4);
        n1.add(k2);
        n1.add(k3);
        n1.add(k1);
        n1.add(k6);

        Resolution r1 = new Resolution();
        r1.geordneteResolution(ordnung, n1);
    }
    @Test
    public void geordneteResolutionTest5(){
        Literal l1 = new Literal("S");
        Literal l2 = new Literal("P");
        Literal l3 = new Literal("Q");
        Literal l4 = new Literal("R");
        Literal l5 = new Literal(Literal.NOT,"S");
        Literal l6 = new Literal(Literal.NOT,"P");
        Literal l7 = new Literal(Literal.NOT,"Q");
        Literal l8 = new Literal(Literal.NOT,"R");

        Ordnung ordnung = new Ordnung(new LinkedList<>());
        ordnung.add(l1);
        ordnung.add(l2);
        ordnung.add(l3);
        ordnung.add(l4);

        Klausel k1 = new Klausel(new LinkedList<>());
        k1.add(l7);
        k1.add(l6);

        Klausel k2 = new Klausel(new LinkedList<>());
        k2.add(l4);
        k2.add(l2);

        Klausel k3 = new Klausel(new LinkedList<>());
        k3.add(l3);
        k3.add(l1);

        Klausel k4 = new Klausel(new LinkedList<>());
        k4.add(l7);
        k4.add(l5);

        Klauselmenge n1 = new Klauselmenge(new LinkedList<>());
        n1.add(k4);
        n1.add(k3);
        n1.add(k1);
        n1.add(k2);

        Resolution r1 = new Resolution();
        r1.geordneteResolution(ordnung,n1);

    }
    @Test
    public void geordneteResolutionTest6(){
        Klauselmenge n1 = new Klauselmenge(new LinkedList<>());
        Resolution r1 = new Resolution();
        r1.geordneteResolution(null,n1);
    }

    @Test
    public void redundanzUeberpruefungTest1(){
        Literal l1 = new Literal("P");
        Literal l2 = new Literal("S");
        Literal l3 = new Literal("Q");
        Literal l4 = new Literal("R");
        Literal l5 = new Literal(Literal.NOT,"P");
        Literal l6 = new Literal(Literal.NOT,"S");
        Literal l7 = new Literal(Literal.NOT,"Q");
        Literal l8 = new Literal(Literal.NOT,"R");

        Ordnung ordnung = new Ordnung(new LinkedList<>());
        ordnung.add(l1);
        ordnung.add(l2);
        ordnung.add(l3);
        ordnung.add(l4);

        Klausel k1 = new Klausel(new LinkedList<>());
        k1.add(l1);
        k1.add(l6);

        Klausel k2 = new Klausel(new LinkedList<>());
        k2.add(l7);
        k2.add(l1);

        Klausel k3 = new Klausel(new LinkedList<>());
        k3.add(l4);
        k3.add(l5);

        Klausel k4 = new Klausel(new LinkedList<>());
        k4.add(l3);
        k4.add(l6);

        Klauselmenge n1 = new Klauselmenge(new LinkedList<>());
        n1.add(k2);
        n1.add(k3);
        n1.add(k4);

        Resolution r1 = new Resolution();
        r1.redundanztest(k1,n1,ordnung);





    }
    @Test
    public void redundanzUeberpruefungTest2(){
        Literal l1 = new Literal("P");
        Literal l2 = new Literal("S");
        Literal l3 = new Literal("Q");
        Literal l4 = new Literal("R");
        Literal l5 = new Literal(Literal.NOT,"P");
        Literal l6 = new Literal(Literal.NOT,"S");
        Literal l7 = new Literal(Literal.NOT,"Q");
        Literal l8 = new Literal(Literal.NOT,"R");

        Ordnung ordnung = new Ordnung(new LinkedList<>());
        ordnung.add(l1);
        ordnung.add(l2);
        ordnung.add(l3);
        ordnung.add(l4);



        Klausel k2 = new Klausel(new LinkedList<>());
        k2.add(l7);
        k2.add(l1);

        Klausel k3 = new Klausel(new LinkedList<>());
        k3.add(l4);
        k3.add(l5);

        Klausel k4 = new Klausel(new LinkedList<>());
        k4.add(l3);
        k4.add(l6);

        Klauselmenge n1 = new Klauselmenge(new LinkedList<>());
        n1.add(k2);
        n1.add(k3);
        n1.add(k4);


        Klausel k5 = new Klausel(new LinkedList<>());
        k5.add(l7);
        k5.add(l4);

        Resolution r1 = new Resolution();
        r1.redundanztest(k5,n1,ordnung);



    }

    /***
     * Die Redundanzeliminierungstests beziehen sich auf die Eingaben bei den geordneteResolutionTests.
     */
    @Test
    public void redundanzEliminierungTest1(){
        Klausel k1 = new Klausel(new LinkedList<Literal>());
        k1.add(l1);
        k1.add(l2);
        k1.add(l3);
        Klausel k2 = new Klausel(new LinkedList<Literal>());
        k2.add(l1);
        k2.add(l2);
        Klausel k3 = new Klausel(new LinkedList<Literal>());
        k3.add(l3);
        Klausel k4 = new Klausel(new LinkedList<Literal>());
        Klausel k5 = new Klausel(new LinkedList<>());
        k5.add(l4);
        k5.add(l3);
        k5.add(l1);
        Klausel k6 = new Klausel(new LinkedList<>());
        k6.add(l4);
        k6.add(l4);
        k6.add(new Literal(Literal.NOT,l4.s));
        k6.add(l3);
        k6.add(new Literal(Literal.NOT,l3.s));
        k6.add(new Literal(Literal.NOT,l3.s));
        k6.add(l1);
        Klausel k7 = new Klausel(new LinkedList<>());
        k7.add(new Literal(Literal.NOT,l3.s));
        k7.add(new Literal(Literal.NOT,l1.s));
        k7.add(new Literal(Literal.NOT,l4.s));

        Ordnung o1 = new Ordnung(new LinkedList<>());
        o1.add(l1);
        o1.add(l3);
        o1.add(l4);

        Klauselmenge n4 = new Klauselmenge(new LinkedList<>());
        n4.add(k1);
        n4.add(k2);
        n4.add(k3);
        n4.add(k5);
        n4.add(k5);
        n4.add(k6);
        n4.add(k7);

        Resolution r1 = new Resolution();


        r1.redundanzElimination(n4,o1);
    }
    @Test
    public void redundanzEliminierungTest2(){
        Literal l1 = new Literal("R");
        Literal l2 = new Literal("Q");
        Literal l3 = new Literal("P");
        Literal l4 = new Literal(Literal.NOT, "R");
        Literal l5 = new Literal(Literal.NOT,"Q");
        Literal l6 = new Literal(Literal.NOT,"P");

        Ordnung ordnung = new Ordnung(new LinkedList<>());
        ordnung.add(l1);
        ordnung.add(l2);
        ordnung.add(l3);

        Klausel k1 = new Klausel(new LinkedList<>());
        k1.add(l2);
        k1.add(l3);

        Klausel k2 = new Klausel(new LinkedList<>());
        k2.add(l5);

        Klausel k3 = new Klausel(new LinkedList<>());
        k3.add(l1);
        k3.add(l6);
        k3.add(l2);

        Klausel k4 = new Klausel(new LinkedList<>());
        k4.add(l6);
        k4.add(l4);

        Klauselmenge n1 = new Klauselmenge(new LinkedList<Klausel>());
        n1.add(k3);
        n1.add(k1);
        n1.add(k2);
        n1.add(k4);

        Resolution r1 = new Resolution();
        r1.redundanzElimination(n1,ordnung);
    }

}
