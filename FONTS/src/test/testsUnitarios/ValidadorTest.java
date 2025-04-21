// FONTS/src/test/testsUnitarios/ValidadorTest.java
package testsUnitarios;

import Dominio.Modelos.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ValidadorTest {

    @Mock private Tablero tablero;
    @Mock private Dawg diccionario;
    @Mock private Celda celda;
    @Mock private Ficha ficha;

    private Validador validador;

    @Before
    public void setUp() {
        validador = new Validador();
        // stubs por defecto para no encontrar nada ni en el tablero ni en el diccionario
        when(tablero.getCelda(anyInt(), anyInt())).thenReturn(null);
        when(tablero.getFicha(anyInt(), anyInt())).thenReturn(null);
        when(tablero.esCentroDelTablero(anyInt(), anyInt())).thenReturn(false);
        when(diccionario.buscarPalabra(anyString())).thenReturn(false);
        when(diccionario.tokenizarPalabra(anyString()))
            .thenReturn(new ArrayList<String>() {{ add(""); }});
    }

    @Test
    public void sinCoordenadas_devuelve0() {
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        int pts = validador.validarPalabra(coords, diccionario, tablero, 5);
        assertEquals(0, pts);
    }

    @Test
    public void unSoloTile_primerTurno_devuelve0() {
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(7,7));
        int pts = validador.validarPalabra(coords, diccionario, tablero, 0);
        assertEquals(0, pts);
    }

    @Test
    public void unSoloTile_segundoTurno_sumaAmbasDirecciones() {
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(4,4));

        when(tablero.getCelda(4,4)).thenReturn(celda);
        when(tablero.getFicha(4,4)).thenReturn(ficha);
        when(ficha.getLetra()).thenReturn("B");
        when(ficha.getPuntuacion()).thenReturn(2);
        when(celda.estaBloqueada()).thenReturn(false);
        when(celda.isDobleTripleLetra()).thenReturn(false);
        when(celda.isDobleTriplePalabra()).thenReturn(false);
        when(diccionario.buscarPalabra("B")).thenReturn(true);

        int pts = validador.validarPalabra(coords, diccionario, tablero, 1);
        assertEquals(4, pts); // 2 + 2
    }

    @Test
    public void noEnLinea_devuelve0() {
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(1,2));
        coords.add(new Pair<>(2,3));
        int pts = validador.validarPalabra(coords, diccionario, tablero, 1);
        assertEquals(0, pts);
    }

    @Test
    public void primerTurno_sinCentro_devuelve0() {
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(5,5));
        coords.add(new Pair<>(5,6));
        int pts = validador.validarPalabra(coords, diccionario, tablero, 0);
        assertEquals(0, pts);
    }

    @Test
    public void palabraPrincipalInvalida_devuelve0() {
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(3,3));
        coords.add(new Pair<>(3,4));

        when(tablero.getCelda(3,3)).thenReturn(celda);
        when(tablero.getFicha(3,3)).thenReturn(ficha);
        when(tablero.getCelda(3,4)).thenReturn(celda);
        when(tablero.getFicha(3,4)).thenReturn(ficha);
        when(ficha.getLetra()).thenReturn("Z");
        when(ficha.getPuntuacion()).thenReturn(10);
        when(celda.estaBloqueada()).thenReturn(false);
        when(celda.isDobleTripleLetra()).thenReturn(false);
        when(celda.isDobleTriplePalabra()).thenReturn(false);

        when(diccionario.buscarPalabra("ZZ")).thenReturn(false);
        when(diccionario.tokenizarPalabra("ZZ"))
            .thenReturn(new ArrayList<String>() {{ add(""); }});

        int pts = validador.validarPalabra(coords, diccionario, tablero, 2);
        assertEquals(0, pts);
    }

    @Test
    public void perpendicularInvalida_devuelve0() {
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(6,6));
        coords.add(new Pair<>(6,7));

        when(tablero.getCelda(6,6)).thenReturn(celda);
        when(tablero.getFicha(6,6)).thenReturn(ficha);
        when(tablero.getCelda(6,7)).thenReturn(celda);
        when(tablero.getFicha(6,7)).thenReturn(ficha);
        when(ficha.getLetra()).thenReturn("A");
        when(ficha.getPuntuacion()).thenReturn(1);
        when(celda.estaBloqueada()).thenReturn(false);
        when(celda.isDobleTripleLetra()).thenReturn(false);
        when(celda.isDobleTriplePalabra()).thenReturn(false);

        // palabra principal válida
        when(diccionario.buscarPalabra("AA")).thenReturn(true);
        // perpendicular inválida
        when(diccionario.buscarPalabra("A")).thenReturn(false);
        when(diccionario.tokenizarPalabra("A"))
            .thenReturn(new ArrayList<String>() {{ add(""); }});

        int pts = validador.validarPalabra(coords, diccionario, tablero, 1);
        assertEquals(0, pts);
    }

    @Test
    public void bingo_suma50() {
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(7,7));
        coords.add(new Pair<>(8,7));
        coords.add(new Pair<>(9,7));
        coords.add(new Pair<>(10,7));
        coords.add(new Pair<>(11,7));
        coords.add(new Pair<>(12,7));
        coords.add(new Pair<>(13,7));

        when(tablero.esCentroDelTablero(7,7)).thenReturn(true);
        for (Pair<Integer,Integer> p : coords) {
            when(tablero.getCelda(p.getFirst(), p.getSecond())).thenReturn(celda);
            when(tablero.getFicha(p.getFirst(), p.getSecond())).thenReturn(ficha);
        }
        when(ficha.getLetra()).thenReturn("M");
        when(ficha.getPuntuacion()).thenReturn(1);
        when(celda.estaBloqueada()).thenReturn(false);
        when(celda.isDobleTripleLetra()).thenReturn(false);
        when(celda.isDobleTriplePalabra()).thenReturn(false);

        // palabra principal "MMMMMMM" válida
        when(diccionario.buscarPalabra("MMMMMMM")).thenReturn(true);
        // perpendiculares válidas ("M")
        when(diccionario.buscarPalabra("M")).thenReturn(true);

        int pts = validador.validarPalabra(coords, diccionario, tablero, 0);
        // 7 puntos palabra principal + 7 puntos (1 por cada perpendicular) + 50 de bingo = 64
        assertEquals(64, pts);
    }

}
