package Dominio;

import java.util.List;
import java.util.Map;

import Dominio.Excepciones.*;
import Dominio.Modelos.*;
import Persistencia.*;

/**
 * Controlador del Ranking.
 * <p>
 * Esta clase se encarga de la lógica de negocio del ranking (agregar, actualizar
 * jugadores) y delega la persistencia a CtrlPersistencia.
 * </p>
 */
public class CtrlRanking {
    
    /** Modelo del ranking */
    private Ranking ranking;
    
    /** Controlador de persistencia para la carga/guardado en fichero */
    private CtrlPersistencia persistencia;
    
    /**
     * Constructor de CtrlRanking.
     * <p>
     * Se instancia el ranking y se invoca el controlador de persistencia para cargar
     * el ranking desde el fichero. Si no existe, se carga un ranking vacío.
     * </p>
     */
    public CtrlRanking() {
        ranking = new Ranking();
        persistencia = new CtrlPersistencia();
        persistencia.cargarRanking(ranking);
    }
    
    /**
     * Agrega un jugador al ranking.
     *
     * @param nombre     Nombre del jugador.
     * @param puntuacion Puntuación del jugador.
     */
    public void crearJugador(String nombre, int puntuacion) {
        ranking.agregarJugador(nombre, puntuacion);
        persistencia.guardarRanking(ranking);
    }
    
    /**
     * Actualiza la puntuación de un jugador existente.
     *
     * @param nombre     Nombre del jugador.
     * @param puntuacion Nueva puntuación del jugador.
     */
    public void modificarRanking(String nombre, int puntuacion) {
        try {
            ranking.actualizarPuntuacion(nombre, puntuacion);
            persistencia.guardarRanking(ranking);
        } catch (JugadorNoEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Devuelve el ranking ordenado de mayor a menor puntuación.
     *
     * @return Lista ordenada con entradas del ranking.
     */
    public List<Map.Entry<String, Integer>> getRankingOrdenado() {
        return ranking.obtenerRankingOrdenado();
    }
}
