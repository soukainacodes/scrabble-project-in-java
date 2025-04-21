// FONTS/src/test/testsUnitarios/PairTest.java
package testsUnitarios;

import Dominio.Modelos.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class PairTest {

    @Test
    public void testConstructorAndGetters() {
        Pair<String,Integer> p = new Pair<>("foo", 42);
        assertEquals("foo", p.getFirst());
        assertEquals(Integer.valueOf(42), p.getSecond());
    }

    @Test
    public void testCreatePairFactory() {
        Pair<Double,Character> p = Pair.createPair(3.14, 'π');
        assertEquals(Double.valueOf(3.14), p.getFirst());
        assertEquals(Character.valueOf('π'), p.getSecond());
    }

    @Test
    public void testSetters() {
        Pair<String,String> p = new Pair<>("a","b");
        p.setFirst("x");
        p.setSecond("y");
        assertEquals("x", p.getFirst());
        assertEquals("y", p.getSecond());
    }

    @Test
    public void testToString() {
        Pair<Integer,Integer> p = new Pair<>(1, 2);
        assertEquals("(1, 2)", p.toString());
    }

    @Test
    public void testEqualsAndReflexive() {
        Pair<String,String> p1 = new Pair<>("L", "R");
        // reflexive
        assertTrue(p1.equals(p1));
        // symmetric & transitive
        Pair<String,String> p2 = new Pair<>("L", "R");
        Pair<String,String> p3 = new Pair<>("L", "R");
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        assertTrue(p2.equals(p3));
        assertTrue(p1.equals(p3));
    }

    @Test
    public void testNotEqualsDifferentFirst() {
        Pair<String,String> p1 = new Pair<>("A", "B");
        Pair<String,String> p2 = new Pair<>("X", "B");
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testNotEqualsDifferentSecond() {
        Pair<String,String> p1 = new Pair<>("A", "B");
        Pair<String,String> p2 = new Pair<>("A", "Z");
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testNotEqualsNullOrDifferentType() {
        Pair<Integer,Integer> p = new Pair<>(1, 1);
        assertFalse(p.equals(null));
        assertFalse(p.equals("not a pair"));
    }
}
