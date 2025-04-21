// FONTS/src/test/testsUnitarios/BolsaTest.java
package testsUnitarios;

import Dominio.Modelos.Bolsa;
import Dominio.Modelos.Ficha;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BolsaTest {

    private Bolsa emptyBolsa;
    private Bolsa seededBolsa;

    @Before
    public void setUp() {
        // Bolsa vacía a partir de lista vacía
        emptyBolsa = new Bolsa(Collections.emptyList());
        // Bolsa con datos determinísticamente barajada usando semilla
        List<String> datos = List.of(
            "A 1 10",   // 1 ficha 'A' de puntuación 10
            "B 2 5"     // 2 fichas 'B' de puntuación 5
        );
        // Con semilla fija, el orden tras shuffle es siempre el mismo
        seededBolsa = new Bolsa(datos, 42L);
    }

    @Test
    public void testEmptyBolsa() {
        // Vacía → isEmpty == true y sacar devuelve null
        assertTrue("Debe estar vacía tras crearse con lista vacía", emptyBolsa.isEmpty());
        assertNull("sacarFicha() en bolsa vacía debe retornar null", emptyBolsa.sacarFicha());
        assertNull("sacarFichaAleatoria() en bolsa vacía debe retornar null", emptyBolsa.sacarFichaAleatoria());
    }

    @Test
    public void testSacarFichaSequencialmente() {
        // Bolsa con 3 fichas: 1 'A' + 2 'B'
        assertFalse("No debe estar vacía al iniciar", seededBolsa.isEmpty());

        // Sacamos en modo FIFO (índice 0)
        Ficha f1 = seededBolsa.sacarFicha();  assertNotNull("Primera ficha no debe ser null", f1);
        Ficha f2 = seededBolsa.sacarFicha();  assertNotNull("Segunda ficha no debe ser null", f2);
        Ficha f3 = seededBolsa.sacarFicha();  assertNotNull("Tercera ficha no debe ser null", f3);

        // Ahora ya no quedan fichas
        assertTrue("Tras sacar tres fichas debe estar vacía", seededBolsa.isEmpty());
        assertNull("Ya no hay fichas", seededBolsa.sacarFicha());
    }

    @Test
    public void testSacarFichaAleatoria() {
        // Creamos otra bolsa con la misma semilla para test de aleatoriedad
        List<String> datos = List.of("A 1 10", "B 2 5");
        Bolsa b = new Bolsa(datos, 123L);

        // Tamaño inicial esperado: 3 fichas
        int originalSize = 3;
        assertFalse("No debe estar vacía al iniciar", b.isEmpty());

        // Sacar una al azar
        Ficha removed = b.sacarFichaAleatoria();
        assertNotNull("Debe retornar una ficha al azar", removed);

        // A partir de aquí, calculamos cuántas quedan sacándolas todas
        int restante = 0;
        Ficha siguiente;
        while ((siguiente = b.sacarFicha()) != null) {
            restante++;
        }
        assertEquals(
            "Tras sacar 1 aleatoria y luego sacar todas las restantes,"
            + " debe quedar originalSize-1 = 2",
            originalSize - 1, restante
        );
    }
}
