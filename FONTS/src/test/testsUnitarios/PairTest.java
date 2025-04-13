package testsUnitarios;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import Dominio.Modelos.Pair;

/**
 * Test unitario para la clase Pair usando únicamente mocks.
 *
 * Se define en este test una interfaz auxiliar "Element" para poder crear mocks
 * y utilizarlos como parámetros genéricos del Pair. Así se puede testear:
 * - Los getters y setters.
 * - El método toString (usando stubs para toString de los mocks).
 * - El comportamiento de equals, comprobando que dos Pair con los mismos elementos son iguales y
 *   dos Pair con elementos distintos no lo son.
 */
@RunWith(MockitoJUnitRunner.class)
public class PairTest {

    /**
     * Interfaz auxiliar para generar mocks que serán usados como parámetros
     * en la clase Pair.
     */
    public interface Element {}

    // Mocks para los elementos (first y second)
    @Mock
    private Element firstMock;
    @Mock
    private Element secondMock;
    @Mock
    private Element newFirstMock;
    @Mock
    private Element newSecondMock;

    // Instancia de Pair que se testeará.
    private Pair<Element, Element> pair;

    @Before
    public void setUp() {
        // Se utiliza el método estático createPair para crear una nueva pareja con los mocks.
        pair = Pair.createPair(firstMock, secondMock);
    }

    /**
     * Test de los getters: se verifica que se obtienen los mismos objetos inyectados.
     */
    @Test
    public void testGetters() {
        assertSame("El primer elemento debe ser firstMock", firstMock, pair.getFirst());
        assertSame("El segundo elemento debe ser secondMock", secondMock, pair.getSecond());
    }

    /**
     * Test de los setters: se verifica que se actualizan correctamente los elementos.
     */
    @Test
    public void testSetters() {
        pair.setFirst(newFirstMock);
        pair.setSecond(newSecondMock);
        assertSame("El primer elemento debe actualizarse a newFirstMock", newFirstMock, pair.getFirst());
        assertSame("El segundo elemento debe actualizarse a newSecondMock", newSecondMock, pair.getSecond());
    }

    /**
     * Test de toString:
     * Se stubmean los métodos toString de los mocks para valores predecibles,
     * y se verifica que Pair genera el string en el formato "(first, second)".
     */
    @Test
    public void testToString() {
        when(firstMock.toString()).thenReturn("first");
        when(secondMock.toString()).thenReturn("second");
        Pair<Element, Element> pair2 = Pair.createPair(firstMock, secondMock);
        assertEquals("El toString debe formar '(first, second)'", "(first, second)", pair2.toString());
    }

    /**
     * Test de equals:
     * Verifica que dos Pair construidos con los mismos objetos (mismos mocks) sean iguales.
     */
    @Test
    public void testEqualsSame() {
        Pair<Element, Element> pair2 = Pair.createPair(firstMock, secondMock);
        assertTrue("Dos Pair creados con los mismos elementos deben ser iguales", pair.equals(pair2));
        assertTrue("La igualdad debe ser simétrica", pair2.equals(pair));
    }

    /**
     * Test de equals:
     * Verifica que dos Pair construidos con elementos distintos no sean iguales.
     */
    @Test
    public void testEqualsDifferent() {
        Pair<Element, Element> pair2 = Pair.createPair(newFirstMock, newSecondMock);
        assertFalse("Dos Pair con elementos diferentes no deben ser iguales", pair.equals(pair2));
        assertFalse("La igualdad debe ser simétrica", pair2.equals(pair));
    }
}
