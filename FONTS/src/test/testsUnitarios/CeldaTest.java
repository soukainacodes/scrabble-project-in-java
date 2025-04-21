// FONTS/src/test/testsUnitarios/CeldaMockTest.java
package testsUnitarios;

import Dominio.Modelos.Celda;
import Dominio.Modelos.Ficha;
import Dominio.Modelos.TipoBonificacion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CeldaTest {

    @Mock
    private Ficha fichaMock;

    @Before
    public void setUp() {
        // Inicializa @Mock
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testColocarYObtenerConMock() {
        Celda celda = new Celda(TipoBonificacion.NINGUNA);
        // La mock no tiene lógica, pero Celda sólo guarda la referencia
        assertTrue("Debe permitir colocar el mock", celda.colocarFicha(fichaMock));
        assertTrue("Ahora debe estar ocupada", celda.estaOcupada());
        assertSame("getFicha() debe devolver el mismo mock", fichaMock, celda.getFicha());
    }

    @Test
    public void testQuitarCuandoNoBloqueadaConMock() {
        Celda celda = new Celda(TipoBonificacion.NINGUNA);
        celda.colocarFicha(fichaMock);

        Ficha salida = celda.quitarFicha();
        assertSame("quitarFicha() devuelve el mock", fichaMock, salida);
        assertFalse("Tras quitar, no debe quedar ocupada", celda.estaOcupada());
    }

    @Test
    public void testNoQuitarSiBloqueadaConMock() {
        Celda celda = new Celda(TipoBonificacion.NINGUNA);
        celda.colocarFicha(fichaMock);
        celda.bloquearFicha();

        assertNull("quitarFicha() devuelve null si está bloqueada", celda.quitarFicha());
        assertTrue("Sigue ocupada tras intento de quitar", celda.estaOcupada());
    }

    @Test
    public void testNoColocarSiYaOcupadaConMock() {
        Celda celda = new Celda(TipoBonificacion.NINGUNA);
        assertTrue(celda.colocarFicha(fichaMock));
        assertFalse("No debe aceptar un segundo colocarFicha()", celda.colocarFicha(mock(Ficha.class)));
    }

    @Test
    public void testMultiplicadorConMock() {
        Celda celda = new Celda(TipoBonificacion.DOBLE_LETRA);
        // Antes de colocar ni bloquear, multiplica
        assertEquals("Multiplicador inicial de DL",
                     TipoBonificacion.DOBLE_LETRA.getMultiplicador(),
                     celda.obtenerMultiplicador());
        celda.colocarFicha(fichaMock);
        celda.bloquearFicha();
        // Tras usar el bonus, multiplica 1
        assertEquals("Tras usar bonus debe ser 1", 1, celda.obtenerMultiplicador());
    }
}
