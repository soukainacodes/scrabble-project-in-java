package testsUnitarios;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import Dominio.Modelos.Nodo;

/**
 * Test unitario para la clase Nodo utilizando únicamente mocks para los nodos hijos.
 * Se verifican los métodos equals, hashCode y toString.
 */
public class NodoTest {

    // Usamos mocks para los nodos hijos.
    private Nodo hijoMock;

    @Before
    public void setUp() {
        // Creamos un mock de Nodo que servirá como hijo.
        hijoMock = mock(Nodo.class);
    }

    /**
     * Verifica que un nodo es igual a sí mismo.
     */
    @Test
    public void testEqualsSameInstance() {
        Nodo nodo = new Nodo("A");
        assertTrue("Un nodo debe ser igual a sí mismo", nodo.equals(nodo));
    }

    /**
     * Verifica que dos nodos con la misma letra y el mismo mapa de hijos (por referencia)
     * se consideren iguales.
     */
    @Test
    public void testEqualsIdenticalNodes() {
        Nodo nodo1 = new Nodo("A");
        Nodo nodo2 = new Nodo("A");
        
        // Agregamos el mismo hijo mock en ambos nodos con la misma clave.
        nodo1.getHijos().put("child", hijoMock);
        nodo2.getHijos().put("child", hijoMock);
        
        // Ambos nodos fueron creados con el mismo valor por defecto de palabraValidaHastaAqui (false)
        assertTrue("Nodos con la misma letra y mismos hijos deben ser iguales", nodo1.equals(nodo2));
        assertEquals("Los códigos hash de nodos iguales deben coincidir", nodo1.hashCode(), nodo2.hashCode());
    }

    /**
     * Verifica que dos nodos con letras diferentes no sean iguales.
     */
    @Test
    public void testEqualsDifferentLetter() {
        Nodo nodo1 = new Nodo("A");
        Nodo nodo2 = new Nodo("B");
        assertFalse("Nodos con letras diferentes no deben ser iguales", nodo1.equals(nodo2));
    }

    /**
     * Verifica que dos nodos con hijos distintos (diferente instancia para una misma clave) no sean iguales.
     */
    @Test
    public void testEqualsDifferentChildren() {
        Nodo nodo1 = new Nodo("A");
        Nodo nodo2 = new Nodo("A");
        
        // En nodo1, agregamos el hijoMock.
        nodo1.getHijos().put("child", hijoMock);
        // En nodo2, agregamos otro mock para el hijo (diferente instancia).
        Nodo otroHijoMock = mock(Nodo.class);
        nodo2.getHijos().put("child", otroHijoMock);
        
        assertFalse("Nodos con hijos diferentes no deben ser iguales", nodo1.equals(nodo2));
    }

    /**
     * Verifica que el método hashCode sea consistente con equals.
     */
    @Test
    public void testHashCodeConsistency() {
        Nodo nodo1 = new Nodo("A");
        Nodo nodo2 = new Nodo("A");

        nodo1.getHijos().put("child", hijoMock);
        nodo2.getHijos().put("child", hijoMock);

        assertTrue("Nodos iguales deben tener el mismo hashCode", nodo1.hashCode() == nodo2.hashCode());
    }

    /**
     * Verifica que el método toString retorne una cadena que contenga la letra y las claves de sus hijos.
     */
    @Test
    public void testToStringFormat() {
        Nodo nodo = new Nodo("A");
        nodo.getHijos().put("child", hijoMock);
        String str = nodo.toString();
        assertNotNull("El toString no debe retornar null", str);
        assertTrue("El toString debe contener la letra 'A'", str.contains("A"));
        // Verificamos que se incluya la clave "child"
        assertTrue("El toString debe incluir la clave 'child'", str.contains("child"));
    }
}
