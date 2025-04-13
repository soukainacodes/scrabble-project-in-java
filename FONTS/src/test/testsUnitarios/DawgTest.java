package testsUnitarios;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import Dominio.Modelos.Dawg;
import Dominio.Modelos.Nodo;
import Dominio.Excepciones.PrefijoNoEncontradoException;

/**
 * Test unitario para la clase Dawg utilizando únicamente mocks para las entradas.
 * 
 * Se crean mocks para las listas de cadenas que se usan en:
 * - cargarFichasValidas(List<String> lineasArchivo)
 * - construirDesdeArchivo(List<String> lineasArchivo)
 * 
 * Para evitar que el objeto Dawg quede con atributos internos nulos, se crea el mock parcial
 * usando useConstructor() para que se invoque el constructor real.
 */
public class DawgTest {

    // Creamos un mock parcial de Dawg que llame a los métodos reales y ejecute el constructor.
    private Dawg dawg;

    // Mocks para las listas de entrada.
    @Mock
    private List<String> mockFichasValidas;

    @Mock
    private List<String> mockPalabras;

    @Before
    public void setUp() {
        // Inicializa los mocks de este test.
        MockitoAnnotations.initMocks(this);
        
        // Importante: usar useConstructor() para que se ejecute el constructor real de Dawg.
        dawg = Mockito.mock(Dawg.class, 
                Mockito.withSettings().useConstructor().defaultAnswer(Mockito.CALLS_REAL_METHODS));

        // Configuramos el mock para la lista de fichas válidas:
        // Simulamos que las fichas válidas son: "A", "B", "C" y "AB".
        List<String> fichas = Arrays.asList("A", "B", "C", "AB");
        when(mockFichasValidas.iterator()).thenReturn(fichas.iterator());

        // Configuramos el mock para la lista de palabras:
        // Simulamos que el "archivo" de palabras contiene: "ABBA" y "ABBC".
        List<String> palabras = Arrays.asList("ABBA", "ABBC");
        when(mockPalabras.iterator()).thenReturn(palabras.iterator());
    }
    
    /**
     * Prueba la carga de fichas y la tokenización de una palabra.
     * Con las fichas simuladas se espera que tokenizar "ABBA" retorne ["AB", "B", "A"].
     */
    @Test
    public void testCargarFichasYTokenizacion() {
        dawg.cargarFichasValidas(mockFichasValidas);
        List<String> tokens = dawg.tokenizarPalabra("ABBA");

        assertNotNull("La lista de tokens no debe ser null", tokens);
        assertEquals("Se esperan 3 tokens al tokenizar 'ABBA'", 3, tokens.size());
        assertEquals("El primer token debe ser 'AB'", "AB", tokens.get(0));
        assertEquals("El segundo token debe ser 'B'", "B", tokens.get(1));
        assertEquals("El tercer token debe ser 'A'", "A", tokens.get(2));
    }

    /**
     * Prueba la construcción del DAWG y la búsqueda de palabras.
     */
    @Test
    public void testConstruirYBuscarPalabra() {
        dawg.cargarFichasValidas(mockFichasValidas);
        dawg.construirDesdeArchivo(mockPalabras);

        // Se espera que buscar "ABBA" retorne true.
        assertTrue("La palabra 'ABBA' debe encontrarse en el DAWG", dawg.buscarPalabra("ABBA"));

        // Se verifica que se pueda obtener el último nodo para "ABBC".
        Nodo ultimoNodo = dawg.buscarUltimoNodo("ABBC");
        assertNotNull("El último nodo para 'ABBC' no debe ser null", ultimoNodo);
    }

    /**
     * Prueba la búsqueda de un prefijo existente en el DAWG.
     */
    @Test
    public void testBuscarPrefijoExistente() {
        dawg.cargarFichasValidas(mockFichasValidas);
        dawg.construirDesdeArchivo(mockPalabras);

        try {
            boolean existe = dawg.buscarPrefijo("ABB");
            assertTrue("El prefijo 'ABB' debe existir en el DAWG", existe);
        } catch (PrefijoNoEncontradoException ex) {
            fail("El prefijo 'ABB' debería existir y no lanzar excepción");
        }
    }

    

}
