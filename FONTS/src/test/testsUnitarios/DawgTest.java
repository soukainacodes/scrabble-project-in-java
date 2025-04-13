package testsUnitarios;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import Dominio.Modelos.Dawg;
import Dominio.Modelos.Nodo;
import Dominio.Excepciones.PrefijoNoEncontradoException;

/**
 * Test unitario para la clase Dawg, usando únicamente mocks para las entradas.
 *  
 * Se crean mocks para las listas de cadenas que se suministran a:
 * - cargarFichasValidas(List<String> lineasArchivo)
 * - construirDesdeArchivo(List<String> lineasArchivo)
 *  
 * De este modo la prueba es independiente y se controla la entrada mediante mocks.
 */
@RunWith(MockitoJUnitRunner.class)
public class DawgTest {

    private Dawg dawg;
    
    // Creamos mocks para las listas de cadenas que Dawg recibirá
    @Mock
    private List<String> mockFichasValidas;
    
    @Mock
    private List<String> mockPalabras;
    
    @Before
    public void setUp() {
        dawg = new Dawg();
        // Configuramos el mock para las fichas válidas.
        // Por ejemplo, simulamos que las fichas válidas son: "A", "B", "C" y "AB".
        List<String> fichas = Arrays.asList("A", "B", "C", "AB");
        when(mockFichasValidas.iterator()).thenReturn(fichas.iterator());
        
        // Configuramos el mock para las palabras.
        // Por ejemplo, simulamos que el archivo de palabras contiene: "ABBA" y "ABBC".
        List<String> palabras = Arrays.asList("ABBA", "ABBC");
        when(mockPalabras.iterator()).thenReturn(palabras.iterator());
    }
    
    /**
     * Prueba la carga de fichas y la tokenización.
     * Por ejemplo, con las fichas simuladas, tokenizar "ABBA" debería producir ["AB", "B", "A"].
     */
    @Test
    public void testCargarFichasYTokenizacion() {
        dawg.cargarFichasValidas(mockFichasValidas);
        List<String> tokens = dawg.tokenizarPalabra("ABBA");
        
        // La lógica de tokenización:
        // - Longitud máxima = 2 (por "AB")
        // - Para "ABBA": se toma "AB" (porque "AB" está en las fichas válidas), luego se procesa "BA":
        //   de "BA": intenta 2 letras ("BA") que no están, luego 1 letra: "B" y después "A".
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
        
        // El método buscarPalabra recorre el grafo y, si la palabra se puede tokenizar, retorna true.
        assertTrue("La palabra 'ABBA' debe encontrarse en el DAWG", dawg.buscarPalabra("ABBA"));
        
        // Verificar que se puede obtener el último nodo para "ABBC" (retorna un Nodo no null).
        Nodo ultimoNodo = dawg.buscarUltimoNodo("ABBC");
        assertNotNull("El último nodo para 'ABBC' no debe ser null", ultimoNodo);
    }
    
    /**
     * Prueba la búsqueda de un prefijo existente.
     */
    @Test
    public void testBuscarPrefijoExistente() {
        dawg.cargarFichasValidas(mockFichasValidas);
        dawg.construirDesdeArchivo(mockPalabras);
        try {
            boolean existe = dawg.buscarPrefijo("ABB");
            assertTrue("El prefijo 'ABB' debe existir en el DAWG", existe);
        } catch (PrefijoNoEncontradoException e) {
            fail("El prefijo 'ABB' debería existir y no lanzar excepción");
        }
    }
    
    /**
     * Prueba la búsqueda de un prefijo no existente.
     * Se espera que se lance PrefijoNoEncontradoException para un prefijo inválido.
     */
    @Test
    public void testBuscarPrefijoNoExistente() {
        dawg.cargarFichasValidas(mockFichasValidas);
        dawg.construirDesdeArchivo(mockPalabras);
        try {
            dawg.buscarPrefijo("XYZ");
            fail("Se esperaba que 'XYZ' no se encuentre y se lance PrefijoNoEncontradoException");
        } catch (PrefijoNoEncontradoException e) {
            // Éxito: se lanzó la excepción esperada.
        }
    }
}
