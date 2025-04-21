package testsUnitarios;

import Dominio.Modelos.Algoritmo;
import Dominio.Modelos.Pair;
import Dominio.Modelos.Tablero;
import Dominio.Modelos.Celda;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AlgoritmoTest {

    @Mock private Tablero tablero;
    @Mock private Celda celdaEmpty;
    @Mock private Celda celdaFilled;

    private Algoritmo algoritmo;

    @Before
    public void setUp() throws Exception {
        algoritmo = new Algoritmo();

        // Inyectamos el mock 'tablero' en el campo privado de Algoritmo
        Field fld = Algoritmo.class.getDeclaredField("tablero");
        fld.setAccessible(true);
        fld.set(algoritmo, tablero);

        // Por defecto, todas las celdas existen y NO están ocupadas
        when(tablero.getCelda(anyInt(), anyInt())).thenReturn(celdaEmpty);
        when(celdaEmpty.estaOcupada()).thenReturn(false);

        // Para (7,7) devolvemos celdaFilled (ocupada)
        when(tablero.getCelda(eq(7), eq(7))).thenReturn(celdaFilled);
        when(celdaFilled.estaOcupada()).thenReturn(true);
    }

    @Test
    public void isEmpty_retornaTrueSoloSiCeldaExisteYNoOcupada() {
        // (1,1) existe y no ocupada → true
        assertTrue(algoritmo.isEmpty(Pair.createPair(1,1)));

        // si getCelda devuelve null → false
        when(tablero.getCelda(2,2)).thenReturn(null);
        assertFalse(algoritmo.isEmpty(Pair.createPair(2,2)));

        // (7,7) stubbed como ocupada → false
        assertFalse(algoritmo.isEmpty(Pair.createPair(7,7)));
    }

    @Test
    public void isFilled_retornaTrueSoloSiCeldaExisteYOcupada() {
        // (7,7) devuelve celdaFilled.estaOcupada=true → true
        assertTrue(algoritmo.isFilled(Pair.createPair(7,7)));

        // (1,1) celdaEmpty.estaOcupada=false → false
        assertFalse(algoritmo.isFilled(Pair.createPair(1,1)));

        // getCelda null → false
        when(tablero.getCelda(4,4)).thenReturn(null);
        assertFalse(algoritmo.isFilled(Pair.createPair(4,4)));
    }

    @Test
    public void isDentroTablero_retornaTrueSoloSiCeldaNoNull() {
        // celdaEmpty != null → true
        assertTrue(algoritmo.isDentroTablero(Pair.createPair(5,5)));

        // getCelda null → false
        when(tablero.getCelda(6,6)).thenReturn(null);
        assertFalse(algoritmo.isDentroTablero(Pair.createPair(6,6)));
    }

    @Test
    public void findAnchors_sinCasillasOcupadas_retornaListaVacia() {
        // Simula TODO el tablero vacío (getCelda==null)
        when(tablero.getCelda(anyInt(), anyInt())).thenReturn(null);

        List<Pair<Integer,Integer>> anchors = algoritmo.find_anchors();
        assertNotNull(anchors);
        assertTrue(anchors.isEmpty());
    }

    @Test
    public void findAnchors_unTileEnCentro_retornaVecinos() {
        // Por defecto en setUp: (7,7) está ocupada, resto no.
        List<Pair<Integer,Integer>> anchors = algoritmo.find_anchors();

        // Debe devolver exactamente los 4 vecinos de (7,7)
        assertEquals(4, anchors.size());
        assertTrue(anchors.contains(Pair.createPair(7,6)));
        assertTrue(anchors.contains(Pair.createPair(7,8)));
        assertTrue(anchors.contains(Pair.createPair(6,7)));
        assertTrue(anchors.contains(Pair.createPair(8,7)));
    }
}
