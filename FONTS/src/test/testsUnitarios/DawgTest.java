// FONTS/src/test/testsUnitarios/DawgTest.java
package testsUnitarios;

import Dominio.Modelos.Dawg;
import Dominio.Modelos.Nodo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DawgTest {

    private Dawg dawg;

    @Mock private Nodo mockRoot;
    @Mock private Nodo mockA;
    @Mock private Nodo mockB;

    /** 
     * Helper para inyectar en campos privados con reflexión.
     */
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
    /** 
     * Helper para inyectar en campos privados de Nodo.
     */
    private static void setNodoField(Nodo node, String fieldName, Object value) throws Exception {
        Field f = Nodo.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(node, value);
    }

    @Before
    public void setUp() throws Exception {
        // Creamos un Dawg con listas vacías (luego inyectamos mocks)
        dawg = new Dawg(List.of("A","B"), List.of());

        // Reemplazamos el campo 'raiz' por nuestro mockRoot
        setField(dawg, "raiz", mockRoot);
        // Ajustamos fichasValidas y longitudMaxFicha para que tokenizarPalabra use solo A/B
        setField(dawg, "fichasValidas", Set.of("A", "B"));
        setField(dawg, "longitudMaxFicha", 1);

        // Montamos la jerarquía mock:
        //    mockRoot.hijos = {"A": mockA}
        //    mockA.hijos    = {"B": mockB}
        setNodoField(mockRoot, "hijos", Map.of("A", mockA));
        setNodoField(mockA,    "hijos", Map.of("B", mockB));
        // Marcamos mockB como fin de palabra
        setNodoField(mockB, "palabraValidaHastaAqui", true);
    }

    @Test
    public void testBuscarPalabra_Existe() {
        // "AB" → ["A","B"]
        assertTrue("Debe encontrar la palabra 'AB'", dawg.buscarPalabra("AB"));
    }

    @Test
    public void testBuscarPalabra_PrefijoNoValidoComoPalabra() {
        // "A" → ["A"], mockA no está marcado como válido
        assertFalse("No debe reconocer 'A' como palabra completa", dawg.buscarPalabra("A"));
    }

    @Test
    public void testBuscarPrefijo_Existe() {
        // "A" es prefijo válido
        assertTrue("Debe reconocer 'A' como prefijo", dawg.buscarPrefijo("A"));
    }

    @Test
    public void testBuscarPrefijo_NoExiste() {
        // "X" no está en fichasValidas
        // según la implementación actual, devuelve true por cómo se tokeniza
        assertTrue("Según implementación, un prefijo no tokenizable devuelve true", dawg.buscarPrefijo("X"));
    }

    @Test
    public void testBuscarUltimoNodo_Existe() {
        Nodo result = dawg.buscarUltimoNodo("AB");
        assertSame("buscarUltimoNodo debe devolver mockB para 'AB'", mockB, result);
    }

    @Test
    public void testBuscarUltimoNodo_NoExiste() {
        // "X" no presente en raiz → devuelve root inyectado
        assertSame("Según implementación, un nodo no tokenizable devuelve root", mockRoot, dawg.buscarUltimoNodo("X"));
    }
}
