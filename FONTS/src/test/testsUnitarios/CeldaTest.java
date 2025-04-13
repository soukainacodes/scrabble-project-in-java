package testsUnitarios;

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
 * Test unitario para la clase Celda.
 * Se usan mocks para Ficha y se emplea una instancia real para TipoBonificacion
 * (ya que es final o un enum y no se recomienda mockearlo).
 */
@RunWith(MockitoJUnitRunner.class)
public class CeldaTest {

    private Celda celda;
    
    // Se crea un mock para Ficha
    @Mock
    private Ficha fichaMock;
    
    // En lugar de mockear TipoBonificacion, se usa una instancia real.
    // Suponemos que, por ejemplo, TipoBonificacion.DOBLE_PALABRA tiene getMultiplicador() = 3.
    // Ajusta este valor según la implementación real.
    private TipoBonificacion bonificacionReal = TipoBonificacion.DOBLE_PALABRA;

    @Before
    public void setUp() {
        // Se inicializa la Celda con la instancia real de TipoBonificacion.
        celda = new Celda(bonificacionReal);
    }

    /**
     * Verifica que la celda esté inicialmente vacía.
     */
    @Test
    public void testCeldaInicialmenteVacia() {
        assertFalse("La celda no debe estar ocupada al inicio", celda.estaOcupada());
        assertNull("La ficha de la celda debe ser null al inicio", celda.getFicha());
    }
    
    /**
     * Verifica que se pueda colocar una ficha en la celda.
     */
    @Test
    public void testColocarFichaExitosa() {
        boolean colocada = celda.colocarFicha(fichaMock);
        assertTrue("Se debe poder colocar la ficha en una celda libre", colocada);
        assertTrue("La celda debe quedar ocupada", celda.estaOcupada());
        assertSame("La ficha colocada debe ser la misma que se pasó", fichaMock, celda.getFicha());
    }
    
    /**
     * Verifica que no se pueda colocar otra ficha si la celda ya está ocupada.
     */
    @Test
    public void testColocarFichaCuandoOcupada() {
        celda.colocarFicha(fichaMock);
        Ficha otraFichaMock = mock(Ficha.class);
        boolean resultado = celda.colocarFicha(otraFichaMock);
        assertFalse("No se debe poder colocar una segunda ficha en la misma celda", resultado);
        assertSame("La ficha en la celda debe ser la primera añadida", fichaMock, celda.getFicha());
    }
    
    /**
     * Verifica que se puede quitar la ficha de la celda.
     */
    @Test
    public void testQuitarFicha() {
        celda.colocarFicha(fichaMock);
        Ficha fichaQuitada = celda.quitarFicha();
        assertNotNull("Se debe quitar la ficha cuando la celda no está bloqueada", fichaQuitada);
        assertSame("La ficha quitada debe ser la misma que se colocó", fichaMock, fichaQuitada);
        assertFalse("Después de quitar la ficha, la celda no debe estar ocupada", celda.estaOcupada());
    }
    
    /**
     * Verifica que al bloquear la celda se marque como bloqueada y la bonificación quede usada.
     */
    @Test
    public void testBloquearFicha() {
        celda.colocarFicha(fichaMock);
        celda.bloquearFicha();
        assertTrue("La celda debe estar bloqueada después de bloquearla", celda.estaBloqueada());
        assertFalse("La bonificación debe marcarse como usada al bloquear la celda", celda.bonusDisponible());
    }
    
    /**
     * Verifica el comportamiento de obtener el multiplicador.
     * Mientras la bonificación esté disponible, se debe retornar el multiplicador real;
     * una vez bloqueada la celda, se retorna 1.
     */
    @Test
    public void testObtenerMultiplicador() {
        // Antes de colocar ficha, la bonificación está disponible.
        int expectedMultiplicador = bonificacionReal.getMultiplicador();
        assertEquals("El multiplicador debe ser el de la bonificación mientras esté disponible",
                     expectedMultiplicador, celda.obtenerMultiplicador());
        
        // Colocar ficha y bloquear la celda (lo que marca la bonificación como usada).
        celda.colocarFicha(fichaMock);
        celda.bloquearFicha();
        
        // Una vez usada la bonificación, se debe retornar 1.
        assertEquals("El multiplicador debe ser 1 una vez que la bonificación ha sido usada",
                     1, celda.obtenerMultiplicador());
    }
}
