package testsUnitarios;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import Dominio.Modelos.Validador;
import Dominio.Modelos.Dawg;
import Dominio.Modelos.Tablero;
import Dominio.Modelos.Celda;
import Dominio.Modelos.Ficha;
import Dominio.Modelos.Pair;
import Dominio.Excepciones.PalabraInvalidaException;

/**
 * Test unitario independiente para la clase Validador.
 * Se usan mocks para Dawg y Tablero, evitando dependencias externas.
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidadorTest {

    @InjectMocks
    private Validador validador;

    @Mock
    private Dawg diccionario;

    @Mock
    private Tablero tablero;

    @Mock
    private Celda c00, c01, c02;

    @Mock
    private Ficha f00, f01, f02;

    @Before
    public void setUp() {
        validador = new Validador();
    }

    /**
     * Una sola letra en el primer turno (contadorTurno=0) debe lanzar excepción.
     */
    @Test(expected = PalabraInvalidaException.class)
    public void validarPalabra_SingleTileFirstTurn_ThrowsException() throws PalabraInvalidaException {
        List<Pair<Integer, Integer>> coords = Arrays.asList(new Pair<>(7, 7));
        validador.validarPalabra(coords, diccionario, tablero, 0);
    }

    /**
     * Jugada horizontal válida en primer turno sobre el centro: "CAT" => 3+1+1 = 5 puntos.
     */
    @Test
    public void validarPalabra_HorizontalWord_ReturnsCorrectScore() throws PalabraInvalidaException {
        List<Pair<Integer, Integer>> coords = Arrays.asList(
            new Pair<>(7, 7),
            new Pair<>(7, 8),
            new Pair<>(7, 9)
        );
        // Centro en primer turno
        when(tablero.esCentroDelTablero(7, 7)).thenReturn(true);
        // fuera de coords, null
        when(tablero.getCelda(anyInt(), anyInt())).thenReturn(null);
        when(tablero.getFicha(anyInt(), anyInt())).thenReturn(null);
        // stub en coords
        when(tablero.getCelda(7, 7)).thenReturn(c00);
        when(tablero.getCelda(7, 8)).thenReturn(c01);
        when(tablero.getCelda(7, 9)).thenReturn(c02);
        when(tablero.getFicha(7, 7)).thenReturn(f00);
        when(tablero.getFicha(7, 8)).thenReturn(f01);
        when(tablero.getFicha(7, 9)).thenReturn(f02);
        // configuración de celdas
        when(c00.estaBloqueada()).thenReturn(false);
        when(c01.estaBloqueada()).thenReturn(false);
        when(c02.estaBloqueada()).thenReturn(false);
        when(c00.isDobleTripleLetra()).thenReturn(false);
        when(c01.isDobleTripleLetra()).thenReturn(false);
        when(c02.isDobleTripleLetra()).thenReturn(false);
        when(c00.isDobleTriplePalabra()).thenReturn(false);
        when(c01.isDobleTriplePalabra()).thenReturn(false);
        when(c02.isDobleTriplePalabra()).thenReturn(false);
        // Letras y puntuaciones: C=3, A=1, T=1
        when(f00.getLetra()).thenReturn("C");
        when(f01.getLetra()).thenReturn("A");
        when(f02.getLetra()).thenReturn("T");
        when(f00.getPuntuacion()).thenReturn(3);
        when(f01.getPuntuacion()).thenReturn(1);
        when(f02.getPuntuacion()).thenReturn(1);
        // fallback tokenización para subpalabras de un carácter
        when(diccionario.tokenizarPalabra(anyString())).thenAnswer(invocation -> {
            String palabra = invocation.getArgument(0);
            return Collections.singletonList(palabra);
        });
        // Diccionario reconoce "CAT"
        when(diccionario.buscarPalabra("CAT")).thenReturn(true);
        when(diccionario.tokenizarPalabra("CAT")).thenReturn(Collections.singletonList("CAT"));

        int score = validador.validarPalabra(coords, diccionario, tablero, 0);
        assertEquals(5, score);
    }

    /**
     * Palabra no válida según el diccionario debe lanzar excepción.
     */
    @Test(expected = PalabraInvalidaException.class)
    public void validarPalabra_InvalidWord_ThrowsException() throws PalabraInvalidaException {
        List<Pair<Integer, Integer>> coords = Arrays.asList(
            new Pair<>(7, 7),
            new Pair<>(7, 8),
            new Pair<>(7, 9)
        );
        when(tablero.esCentroDelTablero(7, 7)).thenReturn(true);
        when(tablero.getCelda(anyInt(), anyInt())).thenReturn(null);
        when(tablero.getFicha(anyInt(), anyInt())).thenReturn(null);
        when(tablero.getCelda(7, 7)).thenReturn(c00);
        when(tablero.getCelda(7, 8)).thenReturn(c00);
        when(tablero.getCelda(7, 9)).thenReturn(c00);
        when(tablero.getFicha(7, 7)).thenReturn(f00);
        when(tablero.getFicha(7, 8)).thenReturn(f00);
        when(tablero.getFicha(7, 9)).thenReturn(f00);
        when(c00.estaBloqueada()).thenReturn(false);
        when(c00.isDobleTripleLetra()).thenReturn(false);
        when(c00.isDobleTriplePalabra()).thenReturn(false);
        when(f00.getLetra()).thenReturn("X");
        when(f00.getPuntuacion()).thenReturn(1);
        // invalid dictionary
        when(diccionario.buscarPalabra(anyString())).thenReturn(false);
        when(diccionario.tokenizarPalabra(anyString())).thenReturn(Arrays.asList("X", "Y"));

        validador.validarPalabra(coords, diccionario, tablero, 0);
    }
}
