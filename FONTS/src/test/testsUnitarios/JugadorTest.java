// FONTS/src/test/testsUnitarios/JugadorTest.java
package testsUnitarios;

import Dominio.Modelos.Jugador;
import org.junit.Test;
import static org.junit.Assert.*;

public class JugadorTest {

    @Test
    public void testConstructorAndGetters() {
        Jugador j = new Jugador("SOUKAINA", "PASS123");
        assertEquals("El nombre debe ser el pasado al constructor", "SOUKAINA", j.getNombre());
        assertEquals("La password debe ser la pasada al constructor", "PASS123", j.getPassword());
        assertEquals("Los puntos iniciales deben ser 0", 0, j.getPuntos());
    }

    @Test
    public void testSetPassword() {
        Jugador j = new Jugador("ALEX", "VIEJO");
        j.setPassword("NUEVO");
        assertEquals("La password debe actualizarse correctamente", "NUEVO", j.getPassword());
    }

    @Test
    public void testSetPuntos() {
        Jugador j = new Jugador("ATLI", "CONSTRASENYA");
        j.setPuntos(42);
        assertEquals("Los puntos deben actualizarse correctamente", 42, j.getPuntos());
        j.setPuntos(100);
        assertEquals("Los puntos deben reflejar el último seteo", 100, j.getPuntos());
    }

    @Test
    public void testValidarPassword() {
        Jugador j = new Jugador("SELMA", "1234");
        assertTrue("Debe validar la contraseña correcta", j.validarPassword("1234"));
        assertFalse("No debe validar una contraseña incorrecta", j.validarPassword("4321"));
    }
}
