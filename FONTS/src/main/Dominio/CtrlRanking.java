// Dominio/CtrlRanking.java
package Dominio;

import java.util.*;
import Dominio.Modelos.Ranking;
import Dominio.Excepciones.UsuarioNoEncontradoException;

/**
 * Gestiona el ranking en memoria.
 * Solo CtrlDominio lo inicializa y persiste.
 */
public class CtrlRanking {
    private final Ranking rankingModel = new Ranking();

    /** Carga los puntos iniciales (invocado solo desde CtrlDominio). */
    public void cargarInicial(Map<String,Integer> puntosPorJugador) {
        puntosPorJugador.forEach(rankingModel::agregarJugador);
    }

    /** Lista ordenada desc. por puntos. */
    public List<Map.Entry<String,Integer>> getRankingOrdenado() {
        return rankingModel.obtenerRankingOrdenado();
    }

    /**
     * Posición 1‑based de un jugador, lanza si no existe.
     */
    public int getPosition(String nombre)
            throws UsuarioNoEncontradoException {
        var lista = rankingModel.obtenerRankingOrdenado();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getKey().equals(nombre))
                return i + 1;
        }
        throw new UsuarioNoEncontradoException(nombre);
    }

    /**
     * Registra (o actualiza) la puntuación en memoria.
     * No persiste: CtrlDominio se encarga de guardar.
     */
    public void reportarPuntuacion(String nombre, int puntos) {
        rankingModel.agregarJugador(nombre, puntos);
    }
}
