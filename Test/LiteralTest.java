package Test;

import Evaluator.List.Literal;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
public class LiteralTest {
    Literal l1 = new Literal("A");
    Literal l2 = new Literal(Literal.NOT, "A");
    Literal l3 = new Literal("P");
    Literal l4 = new Literal("B");
    Literal l5 = new Literal(Literal.NOT, "B");
    Literal l6 = new Literal("B");
    Literal l7 = new Literal("A");

    @Test
    public void literalTest(){
        assertEquals(true,l1.value);
        assertEquals(false,l2.value);
        assertEquals("A",l2.s);
        assertEquals(l7.s,l2.s);
        assertEquals(l5.value,l2.value);
        assertNotEquals(l5.s,l2.s);
    }


    @Test
    public void toStringTest(){
        assertEquals("A",l1.toString());
        assertEquals("-A",l2.toString());
        assertEquals("-B",l5.toString());
    }
    @Test
    public void equalsTest(){
        assertEquals(true,l2.equals(l2));
        assertEquals(false,l1.equals(l2));
        assertEquals(false,l2.equals(l1));
        assertEquals(false,l3.equals(l5));
        assertEquals(false,l6.equals(null));

    }
    @Test
    public void negateTest(){
        assertEquals(true,l1.equals(l2.negate(l2)));
        assertEquals(true,l2.equals(l1.negate(l1)));
        assertEquals("B",l5.negate(l5).toString());
        assertEquals("-B",l4.negate(l4).toString());
    }
}
