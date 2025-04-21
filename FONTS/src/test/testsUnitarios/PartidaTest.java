// FONTS/src/test/testsUnitarios/PartidaTest.java
package testsUnitarios;

import Dominio.Modelos.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartidaTest {

    private Partida partida;

    @Mock private Bolsa   mockBolsa;
    @Mock private Tablero mockTablero;
    @Mock private Celda   mockCelda;
    @Mock private Ficha   mockFicha;
    @Mock private Ficha   mockComodin;

    private static void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Before
    public void setUp() throws Exception {
        partida = new Partida(List.of("J1","J2"), List.of(), 0L);
        setField(partida, "bolsa",              mockBolsa);
        setField(partida, "tablero",            mockTablero);
        setField(partida, "fichasJugador1",     new ArrayList<Ficha>());
        setField(partida, "fichasJugador2",     new ArrayList<Ficha>());
        setField(partida, "coordenadasPalabra", new ArrayList<Pair<Integer,Integer>>());
        setField(partida, "turnoJugador",       true);
        setField(partida, "puntosJugador1",     0);
        setField(partida, "puntosJugador2",     0);
        setField(partida, "contadorTurno",      0);
    }

    @Test
    public void testAumentarContador() {
        assertEquals(0, partida.getContadorTurno());
        partida.aumentarContador();
        assertEquals(1, partida.getContadorTurno());
    }

    @Test
    public void testCoordenadasAddClear() {
        assertTrue(partida.getCoordenadasPalabras().isEmpty());
        partida.getCoordenadasPalabras().add(Pair.createPair(2,3));
        partida.coordenadasClear();
        assertTrue(partida.getCoordenadasPalabras().isEmpty());
    }

    @Test
    public void testAñadirFicha_Normal() throws Exception {
        when(mockFicha.getLetra()).thenReturn("X");
        List<Ficha> rack = new ArrayList<>();
        rack.add(mockFicha);
        setField(partida, "fichasJugador1", rack);

        when(mockTablero.getCelda(1,1)).thenReturn(mockCelda);
        when(mockCelda.estaOcupada()).thenReturn(false);

        partida.añadirFicha("X", 1, 1);

        assertEquals(1, partida.getCoordenadasPalabras().size());
        assertEquals(Pair.createPair(1,1), partida.getCoordenadasPalabras().get(0));
        verify(mockTablero).ponerFicha(mockFicha, 1, 1);
        assertFalse(rack.contains(mockFicha));
    }

    @Test
    public void testAñadirFicha_Comodín() throws Exception {
        when(mockComodin.getLetra()).thenReturn("#");
        List<Ficha> rack = new ArrayList<>();
        rack.add(mockComodin);
        setField(partida, "fichasJugador1", rack);

        when(mockTablero.getCelda(2,2)).thenReturn(mockCelda);
        when(mockCelda.estaOcupada()).thenReturn(false);

        partida.añadirFicha("Z", 2, 2);

        verify(mockTablero).ponerComodin("Z", 2, 2);
        assertFalse(rack.contains(mockComodin));
    }

    @Test
    public void testAddPuntosAndGetPuntos() {
        partida.addPuntos(5);
        assertEquals(5, partida.getPuntosJugador1());
        partida.setTurnoJugador(false);
        partida.addPuntos(3);
        assertEquals(3, partida.getPuntosJugador2());
    }

    @Test
    public void testSetFichaGetListSizeGetFicha() {
        assertEquals(0, partida.getListSize());
        Ficha f = mock(Ficha.class);
        partida.setFicha(f);
        assertEquals(1, partida.getListSize());
        assertSame(f, partida.getFicha(0));
    }

    @Test
    public void testRecuperarFichas() throws Exception {
        when(mockBolsa.isEmpty()).thenReturn(false,false,true);
        when(mockBolsa.sacarFicha()).thenReturn(mockFicha);
        setField(partida,"fichasJugador1", new ArrayList<Ficha>());
        boolean resultado = partida.recuperarFichas();
        verify(mockBolsa, times(2)).sacarFicha();
        assertFalse(resultado);

        reset(mockBolsa);
        when(mockBolsa.isEmpty()).thenReturn(false);
        when(mockBolsa.sacarFicha()).thenReturn(mockFicha);
        setField(partida,"fichasJugador1", new ArrayList<Ficha>());
        resultado = partida.recuperarFichas();
        verify(mockBolsa, atLeast(7)).sacarFicha();
        assertTrue(resultado);
    }

    @Test
    public void testQuitarFichaTablero() throws Exception {
        Pair<Integer,Integer> coord = Pair.createPair(3,3);
        List<Pair<Integer,Integer>> coords = new ArrayList<>();
        coords.add(coord);
        setField(partida,"coordenadasPalabra", coords);

        when(mockTablero.quitarFicha(3,3)).thenReturn(mockFicha);
        assertTrue(partida.quitarFichaTablero(3,3));
        verify(mockTablero).quitarFicha(3,3);
        assertTrue(partida.getCoordenadasPalabras().isEmpty());

        when(mockTablero.quitarFicha(4,4)).thenReturn(null);
        assertFalse(partida.quitarFichaTablero(4,4));
    }

    @Test
    public void testCambiarTurnoYIsBolsaEmptyYGetTurno() {
        assertTrue(partida.getTurnoJugador());
        partida.cambiarTurnoJugador();
        assertFalse(partida.getTurnoJugador());
        when(mockBolsa.isEmpty()).thenReturn(true);
        assertTrue(partida.isBolsaEmpty());
    }

    @Test
    public void testObtenerFichas() throws Exception {
        when(mockFicha.getLetra()).thenReturn("W");
        List<Ficha> rack = new ArrayList<>();
        rack.add(mockFicha);
        setField(partida,"fichasJugador1", rack);
        List<String> letras = partida.obtenerFichas();
        assertEquals(1, letras.size());
        assertEquals("W", letras.get(0));
    }

    @Test
    public void testBloquearCeldas() throws Exception {
        Pair<Integer,Integer> p1 = Pair.createPair(1,2);
        List<Pair<Integer,Integer>> cps = new ArrayList<>();
        cps.add(p1);
        setField(partida,"coordenadasPalabra", cps);
        partida.bloquearCeldas();
        verify(mockTablero).bloquearCelda(1,2);
    }

    @Test
    public void testSetPuntosYGetters() {
        partida.setPuntos(10);
        assertEquals(10, partida.getPuntosJugador1());
        partida.setTurnoJugador(false);
        partida.setPuntos(7);
        assertEquals(7, partida.getPuntosJugador2());
    }
}
