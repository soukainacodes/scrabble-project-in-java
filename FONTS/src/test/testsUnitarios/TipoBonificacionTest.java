package testsUnitarios;

import static org.junit.Assert.*;
import org.junit.Test;
import Dominio.Modelos.TipoBonificacion;

/**
 * Test unitario para el enum TipoBonificacion.
 * Se comprueba que cada constante retorne el multiplicador esperado.
 */
public class TipoBonificacionTest {

    @Test
    public void testGetMultiplicador() {
        assertEquals("NINGUNA debe tener multiplicador 1", 1, TipoBonificacion.NINGUNA.getMultiplicador());
        assertEquals("DOBLE_LETRA debe tener multiplicador 2", 2, TipoBonificacion.DOBLE_LETRA.getMultiplicador());
        assertEquals("TRIPLE_LETRA debe tener multiplicador 3", 3, TipoBonificacion.TRIPLE_LETRA.getMultiplicador());
        assertEquals("DOBLE_PALABRA debe tener multiplicador 2", 2, TipoBonificacion.DOBLE_PALABRA.getMultiplicador());
        assertEquals("TRIPLE_PALABRA debe tener multiplicador 3", 3, TipoBonificacion.TRIPLE_PALABRA.getMultiplicador());
    }
}
