package Test;

import Evaluator.List.Klausel;
import Evaluator.List.Klauselmenge;
import Evaluator.List.Literal;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KlauselmengeTest {
    Literal l1 = new Literal("A");
    Literal l2 = new Literal(Literal.NOT, "A");
    Literal l3 = new Literal("P");
    Literal l4 = new Literal("B");
    Literal l5 = new Literal(Literal.NOT, "B");
    Literal l6 = new Literal("B");
    Literal l7 = new Literal("A");
    Klausel k1 = new Klausel(new LinkedList<>());
    Klausel k2 = new Klausel(new LinkedList<>());

    @Test
    public void toStringTest() {

        Klausel k0 = new Klausel(new LinkedList<Literal>());
        Klauselmenge N0 = new Klauselmenge(new LinkedList<>());
        Assert.assertEquals("[]",N0.toString());

        Klausel k1 = new Klausel(new LinkedList<Literal>());
        k1.add(l1);
        k1.add(l2);
        k1.add(l3);

        Klausel k2 = new Klausel(new LinkedList<Literal>());

        Klausel k3 = new Klausel(new LinkedList<Literal>());
        k2.add(l1);
        k2.add(l2);

        Klausel k4 = new Klausel(new LinkedList<Literal>());
        k3.add(l3);

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


        Klauselmenge N1 = new Klauselmenge(new LinkedList<>());
        N1.add(k1);
        N1.add(k2);
        N1.add(k3);
        N1.add(k4);
        N1.add(k5);
        N1.add(k6);
        assertEquals("[[A,-A,P];[A,-A];[P];[];[B,P,A];[B,B,-B,P,-P,-P,A]]", N1.toString());

        N1.merge(N0);
        //N0 ist die leere Klauselmenge
        assertEquals("[[A,-A,P];[A,-A];[P];[];[B,P,A];[B,B,-B,P,-P,-P,A]]",N1.toString());

        Klauselmenge negate = k5.negate();
        N0.merge(negate);
        assertEquals("[[-B];[-P];[-A]]",N0.toString());

        N1.merge(negate);
        assertEquals("[[A,-A,P];[A,-A];[P];[];[B,P,A];[B,B,-B,P,-P,-P,A];[-B];[-P];[-A]]",N1.toString());

    }

}
