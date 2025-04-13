package testsUnitarios;

import Dominio.Modelos.Tablero;
import Dominio.Modelos.Celda;
import Dominio.Modelos.Ficha;
import Dominio.Modelos.TipoBonificacion;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test unitario para la clase Tablero.
 * Se utilizan mocks para Ficha (para aislar las pruebas de la dependencia) y se 
 * verifica la funcionalidad integrada del Tablero (que maneja celdas, colocación, 
 * retiro de fichas, bloqueo y detección del centro).
 */
@RunWith(MockitoJUnitRunner.class)
public class TableroTest {

    private Tablero tablero;

    // Se declara un mock para Ficha para usar en los tests de colocación y retirada.
    @Mock
    private Ficha fichaMock;

    @Before
    public void setUp() {
        // Se instancia el tablero, que crea internamente un arreglo de celdas de 15x15.
        tablero = new Tablero();
    }

    /**
     * Verifica que se pueda obtener una celda con coordenadas válidas y que para
     * coordenadas inválidas retorne null.
     */
    @Test
    public void testGetCelda() {
        // Coordenadas válidas
        Celda celda = tablero.getCelda(0, 0);
        assertNotNull("La celda en (0,0) debería existir", celda);
        
        celda = tablero.getCelda(14, 14);
        assertNotNull("La celda en (14,14) debería existir", celda);
        
        // Coordenadas inválidas
        assertNull("La celda en (-1,0) debe ser null", tablero.getCelda(-1, 0));
        assertNull("La celda en (15,15) debe ser null", tablero.getCelda(15, 15));
        assertNull("La celda en (0,15) debe ser null", tablero.getCelda(0, 15));
    }

    /**
     * Verifica que inicialmente no haya ficha en ninguna celda.
     */
    @Test
    public void testGetFicha_InitiallyNull() {
        // Se prueba en una posición arbitraria, por ejemplo (7,7)
        assertNull("La ficha en (7,7) debe ser null al inicio", tablero.getFicha(7, 7));
    }

    /**
     * Verifica que se pueda colocar una ficha en el tablero y que luego se pueda obtener.
     */
    @Test
    public void testPonerFicha() {
        // Coloca la ficha en una posición conocida, por ejemplo (5,5)
        boolean placed = tablero.ponerFicha(fichaMock, 5, 5);
        assertTrue("Debe ser posible poner una ficha en (5,5)", placed);
        // Se obtiene la ficha y se verifica que es la misma
        assertSame("La ficha obtenida en (5,5) debe ser la misma que se colocó", fichaMock, tablero.getFicha(5, 5));
    }

    /**
     * Verifica que se pueda retirar (quitar) una ficha de una celda.
     */
    @Test
    public void testQuitarFicha() {
        // Coloca una ficha en (3,3)
        tablero.ponerFicha(fichaMock, 3, 3);
        // Retira la ficha
        Ficha removed = tablero.quitarFicha(3, 3);
        assertNotNull("La ficha retirada de (3,3) no debe ser null", removed);
        assertSame("La ficha retirada debe ser la misma que se colocó", fichaMock, removed);
        // Después de retirarla, la celda debe quedar sin ficha
        assertNull("Después de quitar la ficha, (3,3) debe quedar sin ficha", tablero.getFicha(3, 3));
    }
    
    /**
     * Verifica que se bloquee correctamente una celda cuando se coloca una ficha y se llama a bloquear.
     */
    @Test
    public void testBloquearCelda() {
        // Coloca una ficha en la celda (2,2)
        tablero.ponerFicha(fichaMock, 2, 2);
        // Se bloquea la celda
        tablero.bloquearCelda(2, 2);
        // Se obtiene la celda para comprobar su estado
        Celda celda = tablero.getCelda(2, 2);
        assertTrue("La celda en (2,2) debe estar bloqueada tras llamar a bloquearCelda", celda.estaBloqueada());
        assertFalse("El bonus de la celda en (2,2) no debe estar disponible después de bloquear", celda.bonusDisponible());
    }

    /**
     * Verifica que se identifique correctamente el centro del tablero.
     */
    @Test
    public void testEsCentroDelTablero() {
        assertTrue("La posición (7,7) debe ser el centro del tablero", tablero.esCentroDelTablero(7, 7));
        assertFalse("La posición (0,0) no debe ser el centro del tablero", tablero.esCentroDelTablero(0, 0));
        assertFalse("La posición (7,8) no debe ser el centro del tablero", tablero.esCentroDelTablero(7, 8));
    }
}
