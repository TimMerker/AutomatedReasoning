package Test;

import Evaluator.List.Klausel;
import Evaluator.List.Literal;
import org.junit.Test;

import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class KlauselTest {
    Literal l1 = new Literal("A");
    Literal l2 = new Literal(Literal.NOT, "A");
    Literal l3 = new Literal("P");
    Literal l4 = new Literal("B");
    Literal l5 = new Literal(Literal.NOT, "B");
    Literal l6 = new Literal("B");
    Literal l7 = new Literal("A");


    @Test
    public void klauselTest(){
        Klausel k1 = new Klausel(new LinkedList<>());
        Klausel k2 = new Klausel(new LinkedList<>());
        assertEquals(k1,k2);
        assertEquals(0,k1.size());

        k1.add(l1);
        k1.add(l2);
        k1.add(l3);
        k2.add(l3);
        k2.add(l2);
        k2.add(l1);
        assertNotEquals(k1,k2);

        Klausel k3 = new Klausel(new LinkedList<>());
        k3.add(l1);
        k3.add(l2);
        k3.add(l3);
        assertEquals(k1,k3);
        assertEquals(k1.size(),k3.size());

    }
    @Test
    public void toStringTest(){
        Klausel k1 = new Klausel(new LinkedList<>());
        Klausel k2 = new Klausel(new LinkedList<>());
        k1.add(l1);
        k1.add(l2);
        k1.add(l3);
        assertEquals("[A,-A,P]",k1.toString());
        assertEquals("[]",k2.toString());

        k2.addAll(k1);
        assertEquals("[A,-A,P]",k2.toString());

        k2.addAll(k1);
        assertEquals("[A,-A,P,A,-A,P]",k2.toString());

    }
}
