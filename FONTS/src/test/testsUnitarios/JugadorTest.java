package testsUnitarios;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Dominio.Modelos.Jugador;

/**
 * Test unitario para la clase Jugador.
 * Se comprueba el correcto funcionamiento del constructor, los getters,
 * el setter de puntos y el método ValidarPassword.
 *
 * Dado que Jugador es una clase simple sin dependencias externas,
 * no se utilizan mocks en este test.
 */
public class JugadorTest {

    private Jugador jugador;

    @Before
    public void setUp() {
        // Se crea una instancia de Jugador con nombre "Alice" y contraseña "secret".
        jugador = new Jugador("Alice", "secret");
    }

    @Test
    public void testConstructor() {
        // Verifica que el constructor inicializa correctamente los atributos.
        assertEquals("Alice", jugador.getNombre());
        assertEquals("secret", jugador.getPassword());
        assertEquals("Los puntos iniciales deben ser 0", 0, jugador.getPuntos());
    }

    @Test
    public void testSetPuntos() {
        // Actualiza los puntos del jugador y se verifican.
        jugador.setPuntos(200);
        assertEquals("Los puntos deben actualizarse a 200", 200, jugador.getPuntos());
    }

    @Test
    public void testValidarPassword() {
        // Verifica que la validación de la contraseña funcione correctamente.
        assertTrue("La contraseña correcta debe validar", jugador.ValidarPassword("secret"));
        assertFalse("Una contraseña incorrecta no debe validar", jugador.ValidarPassword("wrong"));
    }
}
