// FONTS/src/test/testsUnitarios/NodoTest.java
package testsUnitarios;

import Dominio.Modelos.Nodo;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

public class NodoTest {

    @Test
    public void testConstructorsAndGetters() {
        // Nodo raíz
        Nodo raiz = new Nodo();
        assertNull("La letra de la raíz debe ser null", raiz.getLetra());
        assertNotNull("El mapa de hijos no debe ser null", raiz.getHijos());
        assertTrue("El mapa de hijos debe estar vacío", raiz.getHijos().isEmpty());
        assertFalse("Por defecto no debe ser palabra válida", raiz.esValida());

        // Nodo con letra
        Nodo nodoX = new Nodo("X");
        assertEquals("X", nodoX.getLetra());
        assertNotNull(nodoX.getHijos());
        assertTrue(nodoX.getHijos().isEmpty());
        assertFalse(nodoX.esValida());
    }

    @Test
    public void testEsValidaFlagViaReflection() throws Exception {
        Nodo n = new Nodo("A");
        assertFalse("Inicialmente esValida() debe ser false", n.esValida());

        // Accedemos y cambiamos el campo privado palabraValidaHastaAqui
        Field flag = Nodo.class.getDeclaredField("palabraValidaHastaAqui");
        flag.setAccessible(true);
        flag.setBoolean(n, true);

        assertTrue("Después de reflejar el cambio, esValida() debe ser true", n.esValida());
    }

    @Test
    public void testGetHijosAndMutability() {
        Nodo padre = new Nodo("P");
        Map<String,Nodo> hijos = padre.getHijos();
        assertTrue("Inicialmente no debe haber hijos", hijos.isEmpty());

        Nodo h1 = new Nodo("H");
        hijos.put("H", h1);
        assertEquals("Ahora debe contener un hijo bajo clave 'H'", 1, padre.getHijos().size());
        assertSame("El hijo obtenido debe ser el mismo objeto", h1, padre.getHijos().get("H"));
    }

    @Test
    public void testEqualsAndHashCodeWithSameChildReference() {
        Nodo n1 = new Nodo("L");
        Nodo hijo = new Nodo("C");
        n1.getHijos().put("C", hijo);

        // Creamos otro nodo idéntico usando la misma referencia de hijo
        Nodo n2 = new Nodo("L");
        n2.getHijos().put("C", hijo);

        assertTrue("n1 debe ser igual a n2", n1.equals(n2));
        assertTrue("n2 debe ser igual a n1", n2.equals(n1));
        assertEquals("hashCode debe coincidir cuando equals es true", n1.hashCode(), n2.hashCode());

        // Cambiamos la bandera de palabra válida en uno solo
        // (para asegurarnos de que ambos deben coincidir en esa bandera)
        // Aquí la dejamos en false por defecto: igualdad mantiene
        assertFalse("palabraValidaHastaAqui es false en ambos, sigue siendo igual", n1.esValida());
        assertEquals(n1, n2);
    }

    @Test
    public void testEqualsInequalityDifferentLetterOrChild() {
        Nodo base = new Nodo("X");
        Nodo igual = new Nodo("X");
        assertEquals(base, igual);

        Nodo distintoLetra = new Nodo("Y");
        assertFalse("Distinta letra debe romper equals", base.equals(distintoLetra));

        // Con hijo diferente (aunque misma clave)
        Nodo conHijo1 = new Nodo("Z");
        Nodo hA = new Nodo("A");
        conHijo1.getHijos().put("A", hA);
        Nodo conHijo2 = new Nodo("Z");
        Nodo hB = new Nodo("B");
        conHijo2.getHijos().put("A", hB);
        assertFalse("Distinto objeto hijo bajo misma clave debe romper equals",
                    conHijo1.equals(conHijo2));
    }

    @Test
    public void testEqualsSelfAndNullAndOtherClass() {
        Nodo n = new Nodo("Q");
        assertTrue("Un nodo debe ser igual a sí mismo", n.equals(n));
        assertFalse("Un nodo no debe ser igual a null", n.equals(null));
        assertFalse("Un nodo no debe ser igual a un objeto de otra clase", n.equals("algo"));
    }
}
