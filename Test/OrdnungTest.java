package Test;

import Evaluator.List.Klausel;
import Evaluator.List.Klauselmenge;
import Evaluator.List.Literal;
import Evaluator.List.Ordnung;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OrdnungTest {
    Literal l1 = new Literal("A");
    Literal l2 = new Literal(Literal.NOT, "A");
    Literal l3 = new Literal("P");
    Literal l4 = new Literal("B");
    Literal l5 = new Literal(Literal.NOT, "B");
    Literal l6 = new Literal("B");
    Literal l7 = new Literal("A");




    ExpectedException addNegative = ExpectedException.none();

    @Test
    public void OrdnungTest(){
        Ordnung o1 = new Ordnung(new LinkedList<>());
        o1.add(l1);
        o1.add(l3);
        o1.add(l4);
        assertEquals(6,o1.size());

        Ordnung o2 = new Ordnung((new LinkedList<>()));
        o2.add(l1);
        o2.add(l4);
        o2.add(l3);
        assertNotEquals(o1,o2);

        Ordnung o3 = new Ordnung((new LinkedList<>()));
        o3.add(l1);
        o3.add(l4);
        o3.add(l3);
        assertEquals(o2,o3);

        Ordnung o4 = new Ordnung(new LinkedList<>());
        o4.add(l1);
        o4.add(l1);
        o4.add(l3);
        o4.add(l4);
        o4.add(l4);
        assertEquals(o1,o4);


    }
    @Test
    public void addOrdnung(){
        Ordnung o1 = new Ordnung(new LinkedList<>());
        try{
            o1.add(l2);
            fail();
        } catch (IllegalArgumentException ignored){
        }


        o1.add(l1);
        o1.add(l1);

        LinkedList<Literal> l = new LinkedList<>();
        l.add(l1);
        /*TODO  Erstellen einer Ordnung mit einer fertigen Liste funktioniert nicht, wirft nullpointer Exception by hasNext*/
        Ordnung o2 = new Ordnung(l);

        System.out.println(o2.toString());
        //assertEquals(o1,o2);
    }
    @Test
    public void toStringTest(){
        Ordnung o1 = new Ordnung(new LinkedList<>());
        o1.add(l1);
        o1.add(l3);
        o1.add(l4);
        assertEquals("[-A,A,-P,P,-B,B]",o1.toString());

        Ordnung o2 = new Ordnung((new LinkedList<>()));
        o2.add(l1);
        o2.add(l4);
        o2.add(l3);
        assertEquals("[-A,A,-B,B,-P,P]",o2.toString());
    }


    @Test
    public void ordnenKlauselmengeTest(){
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

        Ordnung o1 = new Ordnung(new LinkedList<>());
        o1.add(l1);
        o1.add(l3);
        o1.add(l4);
        assertEquals("[-A,A,-P,P,-B,B]",o1.toString());

        Klauselmenge n1 = new Klauselmenge(new LinkedList<>());
        n1.add(k1);
        n1.add(k2);
        n1.add(k3);
        n1.add(k4);
        assertEquals("[[A,-A,P];[A,-A];[P];[]]",n1.toString());

        o1.ordnen(n1);
        assertEquals("[[];[P];[-A,A];[-A,A,P]]",n1.toString());


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

        Klauselmenge n3 = new Klauselmenge(new LinkedList<>());
        n3.add(k1);
        n3.add(k2);
        n3.add(k3);
        n3.add(k4);
        n3.add(k5);
        n3.add(k6);
        n3.add(k7);
        System.out.println("Das ist die ungeordnete Klauselmenge" + n3.toString());
        System.out.println("Das ist die Ordnung"+ o1.toString());
        o1.ordnen(n3);
        System.out.println("Das ist die geordnete Klauselmenge" + n3.toString());
        assertEquals("[[];[P];[A,P,B];[A,-P,-P,P,-B,B,B];[-A,-P,-B];[-A,A];[-A,A,P]]",n3.toString());


        //Test mit Literal in einer Klauselmenge, dass nicht in der Ordnung enthalten ist failed beim ordnen
        // erfolgreich mit Fehlermeldung
        k7.add(new Literal(Literal.NOT,"X"));
        try{
            o1.ordnen(n3);
        }
        catch (NoSuchElementException ignored){
        }


    }
}
