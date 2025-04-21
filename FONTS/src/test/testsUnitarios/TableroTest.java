// FONTS/src/test/testsUnitarios/TableroTest.java
package testsUnitarios;

import Dominio.Modelos.Tablero;
import Dominio.Modelos.Celda;
import Dominio.Modelos.Ficha;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test unitario para la clase Tablero.
 * Se usa un mock de Ficha para no depender de su implementación real.
 */
@RunWith(MockitoJUnitRunner.class)
public class TableroTest {

    private Tablero tablero;

    @Mock
    private Ficha fichaMock;

    @Before
    public void setUp() {
        // Crear un tablero 15×15
        tablero = new Tablero();
    }

    @Test
    public void testGetCelda_ValidaEInválida() {
        // Coordenadas válidas
        assertNotNull("La celda (0,0) debe existir", tablero.getCelda(0, 0));
        assertNotNull("La celda (14,14) debe existir", tablero.getCelda(14, 14));
        // Coordenadas inválidas
        assertNull("Celda (-1,0) debe ser null", tablero.getCelda(-1, 0));
        assertNull("Celda (15,15) debe ser null", tablero.getCelda(15, 15));
        assertNull("Celda (0,15) debe ser null", tablero.getCelda(0, 15));
    }

    @Test
    public void testGetFicha_InicialmenteNull() {
        assertNull("Ficha en (7,7) debe ser null al inicio", tablero.getFicha(7, 7));
    }

    @Test
    public void testPonerYObtenerFicha() {
        boolean placed = tablero.ponerFicha(fichaMock, 5, 5);
        assertTrue("Debe poderse poner ficha en (5,5)", placed);
        assertSame("La ficha obtenida debe ser la misma que se colocó", fichaMock, tablero.getFicha(5, 5));
    }

    @Test
    public void testQuitarFicha() {
        tablero.ponerFicha(fichaMock, 3, 3);
        Ficha removed = tablero.quitarFicha(3, 3);
        assertSame("La ficha retirada debe ser la misma que se colocó", fichaMock, removed);
        assertNull("Tras retirar, la celda (3,3) debe quedar vacía", tablero.getFicha(3, 3));
    }

    @Test
    public void testBloquearCelda_Y_bonusDisponible() {
        // Colocar ficha y luego bloquear
        tablero.ponerFicha(fichaMock, 2, 2);
        tablero.bloquearCelda(2, 2);

        Celda c = tablero.getCelda(2, 2);
        assertTrue("Celda debe estar bloqueada tras bloquearCelda", c.estaBloqueada());
        // bonusDisponible() debe devolver false tras bloqueo
        assertFalse("Bonus ya no debe estar disponible después de bloquear", c.bonusDisponible());
    }

    @Test
    public void testEsCentroDelTablero() {
        assertTrue("La posición (7,7) es el centro", tablero.esCentroDelTablero(7, 7));
        assertFalse("La posición (0,0) no es el centro", tablero.esCentroDelTablero(0, 0));
        assertFalse("La posición (7,8) no es el centro", tablero.esCentroDelTablero(7, 8));
    }
}
