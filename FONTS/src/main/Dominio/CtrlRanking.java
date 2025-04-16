// Dominio/CtrlRanking.java
package Dominio;

import java.util.*;
import java.util.stream.*;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Ranking;

public class CtrlRanking {
    private Ranking rankingModel;
    private CtrlJugador ctrlJugador;

    public CtrlRanking(CtrlJugador ctrlJugador) {
        this.ctrlJugador  = ctrlJugador;
        this.rankingModel = new Ranking();
        // Cargo en memoria las puntuaciones actuales
        for (Jugador j : ctrlJugador.getJugadores().values()) {
            rankingModel.agregarJugador(j.getNombre(), j.getPuntos());
        }
    }

    /** Devuelve lista ordenada desc. por puntos */
    public List<Map.Entry<String, Integer>> getRankingOrdenado() {
        return rankingModel.obtenerRankingOrdenado();
    }

    /** Posición 1‑based de un jugador en el ranking, o -1 si no aparece */
    public int getPosition(String nombre) {
        List<Map.Entry<String, Integer>> lista = getRankingOrdenado();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getKey().equals(nombre)) return i+1;
        }
        return -1;
    }

    /**
     * Después de una partida, registra la nueva puntuación:
     *  - en el ranking en memoria,
     *  - y en el jugador si mejora su máximo.
     */
    public void reportarPuntuacion(String nombre, int puntos) {
        rankingModel.agregarJugador(nombre, puntos);
        ctrlJugador.actualizarPuntuacion(nombre, puntos);
    }
}
