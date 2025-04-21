// FONTS/src/test/testsUnitarios/FichaTest.java
package testsUnitarios;

import Dominio.Modelos.Ficha;
import org.junit.Test;
import static org.junit.Assert.*;

public class FichaTest {

    @Test
    public void testGetters() {
        // Creamos una ficha con letra "X" y puntuación 8
        Ficha ficha = new Ficha("X", 8);

        // Comprobamos que getLetra() devuelve lo esperado
        assertEquals("La letra debe ser la pasada al constructor", "X", ficha.getLetra());

        // Comprobamos que getPuntuacion() devuelve lo esperado
        assertEquals("La puntuación debe ser la pasada al constructor", 8, ficha.getPuntuacion());
    }

    @Test
    public void testVariasInstancias() {
        // Otra ficha con valores distintos
        Ficha fichaA = new Ficha("A", 1);
        Ficha fichaB = new Ficha("B", 3);

        assertEquals("A", fichaA.getLetra());
        assertEquals(1, fichaA.getPuntuacion());

        assertEquals("B", fichaB.getLetra());
        assertEquals(3, fichaB.getPuntuacion());
    }
}
