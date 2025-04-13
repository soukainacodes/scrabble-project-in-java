package testsUnitarios;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import Dominio.Modelos.Ranking;
import Dominio.Excepciones.JugadorNoEncontradoException;

public class RankingTest {

    private Ranking ranking;
    
    // Usaremos un mock para simular el HashMap interno del Ranking.
    @Mock
    private HashMap<String, Integer> mockRegistro;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ranking = new Ranking();
        // Inyectamos el mock del registro interno.
        ranking.setRegistro(mockRegistro);
    }
    
    /**
     * Test para agregar un jugador nuevo:
     * Si el jugador no existe (getOrDefault retorna -1), se debe llamar a put con la puntuación.
     */
    @Test
    public void testAgregarJugador_Nuevo() {
        when(mockRegistro.getOrDefault("Alice", -1)).thenReturn(-1);
        
        ranking.agregarJugador("Alice", 100);
        
        verify(mockRegistro, times(1)).put("Alice", 100);
    }
    
    /**
     * Test para actualizar el jugador si la nueva puntuación es mayor:
     * Se simula que el jugador ya existe con una puntuación baja.
     */
    @Test
    public void testAgregarJugador_Update() {
        when(mockRegistro.getOrDefault("Alice", -1)).thenReturn(50);
        
        ranking.agregarJugador("Alice", 100);
        
        verify(mockRegistro, times(1)).put("Alice", 100);
    }
    
    /**
     * Test para que no se actualice si la nueva puntuación es menor o igual:
     * Se simula que el jugador tiene una puntuación más alta que la enviada.
     */
    @Test
    public void testAgregarJugador_NoUpdate() {
        when(mockRegistro.getOrDefault("Alice", -1)).thenReturn(150);
        
        ranking.agregarJugador("Alice", 100);
        
        verify(mockRegistro, never()).put(anyString(), anyInt());
    }
    
    /**
     * Test para actualizar la puntuación de un jugador existente correctamente.
     */
    @Test
    public void testActualizarPuntuacion_Success() throws JugadorNoEncontradoException {
        // Configuramos para que el jugador exista y tenga una puntuación actual de 50.
        when(mockRegistro.containsKey("Alice")).thenReturn(true);
        when(mockRegistro.get("Alice")).thenReturn(50);
        
        ranking.actualizarPuntuacion("Alice", 80);
        
        verify(mockRegistro, times(1)).put("Alice", 80);
    }
    
    /**
     * Test para que actualizar la puntuación lance excepción si el jugador no existe.
     */
    @Test(expected = JugadorNoEncontradoException.class)
    public void testActualizarPuntuacion_PlayerNotFound() throws JugadorNoEncontradoException {
        when(mockRegistro.containsKey("Alice")).thenReturn(false);
        
        ranking.actualizarPuntuacion("Alice", 80);
    }
    
    /**
     * Test para obtener la puntuación de un jugador.
     */
    @Test
    public void testGetPuntuacion() {
        when(mockRegistro.get("Alice")).thenReturn(90);
        
        Integer puntuacion = ranking.getPuntuacion("Alice");
        assertNotNull("La puntuación no debe ser null", puntuacion);
        assertEquals("La puntuación de Alice debe ser 90", 90, puntuacion.intValue());
    }
    
    /**
     * Test para verificar el método contieneJugador.
     */
    @Test
    public void testContieneJugador() {
        when(mockRegistro.containsKey("Alice")).thenReturn(true);
        
        boolean contiene = ranking.contieneJugador("Alice");
        assertTrue("El registro debe contener a Alice", contiene);
    }
    
    /**
     * Test para obtener el ranking ordenado.
     * Se crean mocks para los Map.Entry y se los devuelve en un conjunto real.
     * Se verifica que la lista resultante esté ordenada (descendentemente) por puntuación.
     */
    @Test
    public void testObtenerRankingOrdenado() {
        // Creamos dos mocks para Map.Entry.
        @SuppressWarnings("unchecked")
        Map.Entry<String, Integer> entry1 = (Map.Entry<String, Integer>) mock(Map.Entry.class);
        @SuppressWarnings("unchecked")
        Map.Entry<String, Integer> entry2 = (Map.Entry<String, Integer>) mock(Map.Entry.class);
        
        when(entry1.getKey()).thenReturn("Bob");
        when(entry1.getValue()).thenReturn(200);
        
        when(entry2.getKey()).thenReturn("Alice");
        when(entry2.getValue()).thenReturn(100);
        
        // Usamos un conjunto real que contenga los mocks de las entradas.
        Set<Map.Entry<String, Integer>> entrySet = new HashSet<>();
        entrySet.add(entry1);
        entrySet.add(entry2);
        
        when(mockRegistro.entrySet()).thenReturn(entrySet);
        
        List<Entry<String, Integer>> rankingOrdenado = ranking.obtenerRankingOrdenado();
        assertNotNull("El ranking ordenado no debe ser null", rankingOrdenado);
        assertEquals("Se esperan 2 entradas en el ranking ordenado", 2, rankingOrdenado.size());
        // Dado que se ordena en forma descendente por valor, se espera que la primera sea "Bob" (200) y la segunda "Alice" (100).
        assertEquals("La primera entrada debe tener clave 'Bob'", "Bob", rankingOrdenado.get(0).getKey());
        assertEquals("La primera entrada debe tener valor 200", 200, rankingOrdenado.get(0).getValue().intValue());
    }
    
    /**
     * Test para obtener el registro interno.
     * Se configura el mock del registro para que, al construir una copia (new HashMap<>(mockRegistro)),
     * se simule que el registro tiene una sola entrada.
     */
    @Test
    public void testGetRegistro() {
        // Creamos un mock para Map.Entry y lo colocamos en un conjunto real.
        @SuppressWarnings("unchecked")
        Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) mock(Map.Entry.class);
        when(entry.getKey()).thenReturn("Alice");
        when(entry.getValue()).thenReturn(100);
        
        Set<Map.Entry<String, Integer>> entrySet = new HashSet<>();
        entrySet.add(entry);
        
        // Stubear entrySet() del mockRegistro para simular que tiene una entrada.
        when(mockRegistro.entrySet()).thenReturn(entrySet);
        // También stubear el size() para que retorne 1.
        when(mockRegistro.size()).thenReturn(1);
        
        HashMap<String, Integer> copia = ranking.getRegistro();
        // Verificamos que la copia no sea la misma instancia.
        assertNotSame("La copia del registro no debe ser la misma instancia que el registro interno", mockRegistro, copia);
        // Verificamos que la copia tenga tamaño 1 (según lo configurado en el mock).
        assertEquals("La copia del registro debe tener tamaño 1", 1, copia.size());
    }
    
    /**
     * Test para setRegistro: se verifica que luego de establecer un nuevo registro real, getRegistro lo refleja.
     */
    @Test
    public void testSetRegistro() {
        HashMap<String, Integer> nuevoRegistro = new HashMap<>();
        nuevoRegistro.put("Alice", 100);
        ranking.setRegistro(nuevoRegistro);
        HashMap<String, Integer> registroObtenido = ranking.getRegistro();
        assertEquals("El registro obtenido debe tener 1 entrada", 1, registroObtenido.size());
        assertEquals("La puntuación de Alice debe ser 100", Integer.valueOf(100), registroObtenido.get("Alice"));
    }
}
