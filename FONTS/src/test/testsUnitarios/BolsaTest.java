package testsUnitarios;

import Dominio.Modelos.Bolsa;
import Dominio.Modelos.Ficha;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test unitario para la clase Bolsa usando únicamente mocks para Ficha.
 * Se prueban los métodos de añadir y sacar fichas, garantizando que la prueba se
 * ejecute de forma independiente del comportamiento real de la clase Ficha.
 */
@RunWith(MockitoJUnitRunner.class)
public class BolsaTest {

    private Bolsa bolsa;

    // Este mock se inicializa automáticamente para poder reutilizarlo en los tests.
    @Mock
    private Ficha fichaMock;

    /**
     * Se utiliza una lista vacía para inicializar Bolsa, de modo que el constructor
     * no cree fichas reales a partir de líneas de archivo. Así, se aisla el test de
     * cualquier comportamiento no controlado por nosotros.
     */
    @Before
    public void setUp() {
        bolsa = new Bolsa(Collections.emptyList());
    }

    /**
     * Verifica que al añadir una ficha (mock) a la bolsa se incremente el tamaño
     * y que el objeto añadido es exactamente el mock pasado.
     */
    @Test
    public void testAñadirFicha() {
        int initialSize = bolsa.getConjuntoDeFichas().size();
        bolsa.añadirFicha(fichaMock);
        assertEquals("El tamaño debe incrementarse en 1 tras añadir una ficha",
                     initialSize + 1, bolsa.getConjuntoDeFichas().size());
        assertSame("El objeto añadido debe ser el mismo que el mock inyectado",
                   fichaMock, bolsa.getConjuntoDeFichas().get(initialSize));
    }

    /**
     * Verifica que el método sacarFichaAleatoria retira una ficha de la bolsa cuando
     * ésta no está vacía. Para ello, se crean mocks adicionales in situ para simular
     * diferentes fichas.
     */
    @Test
    public void testSacarFichaAleatoria_NoEmpty() {
        // Se crean mocks adicionales de Ficha para el test.
        Ficha ficha1 = mock(Ficha.class);
        Ficha ficha2 = mock(Ficha.class);
        
        bolsa.añadirFicha(ficha1);
        bolsa.añadirFicha(ficha2);
        
        int sizeBefore = bolsa.getConjuntoDeFichas().size();
        Ficha removed = bolsa.sacarFichaAleatoria();
        
        assertNotNull("Debiera retornar una ficha no nula al sacar de una bolsa no vacía", removed);
        assertEquals("El tamaño de la bolsa debe disminuir en 1 tras sacar una ficha",
                     sizeBefore - 1, bolsa.getConjuntoDeFichas().size());
    }
    
    /**
     * Verifica que intentar sacar una ficha de una bolsa vacía retorne null.
     */
    @Test
    public void testSacarFichaAleatoria_Empty() {
        // Se crea una nueva instancia de Bolsa con lista vacía para garantizar que no hay fichas.
        Bolsa emptyBolsa = new Bolsa(Collections.emptyList());
        Ficha removed = emptyBolsa.sacarFichaAleatoria();
        assertNull("Sacar una ficha de una bolsa vacía debería retornar null", removed);
    }
}