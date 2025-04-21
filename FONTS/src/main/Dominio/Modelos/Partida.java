package Dominio.Modelos;

import java.util.ArrayList;
import java.util.List;

import Dominio.Excepciones.PosicionVaciaTablero;

/**
 * Representa una partida de Scrabble con dos jugadores.
 * Gestiona turnos, fichas de cada jugador, puntuaciones, bolsa y tablero.
 */
public class Partida {

    /** Fichas del jugador 1 en mano. */
    private List<Ficha> fichasJugador1;
    /** Fichas del jugador 2 en mano. */
    private List<Ficha> fichasJugador2;
    /** Puntos acumulados del jugador 1. */
    private int puntosJugador1;
    /** Puntos acumulados del jugador 2. */
    private int puntosJugador2;
    /** Nombres de los jugadores. */
    private String jugador1, jugador2;
    /** Turno actual: {@code true} para jugador 1, {@code false} para jugador 2. */
    private boolean turnoJugador;
    /** Bolsa común de fichas para la partida. */
    private Bolsa bolsa;
    /** Tablero de juego. */
    private Tablero tablero;
    /** Contador de turnos jugados. */
    private int contadorTurno;
    /** Coordenadas de la palabra en curso en el tablero. */
    private List<Pair<Integer,Integer>> coordenadasPalabra;

    /**
     * Inicia una nueva partida.
     *
     * @param jugadores        Lista de dos nombres [jugador1, jugador2].
     * @param lineasArchivoBolsa Líneas de configuración para la bolsa.
     * @param seed             Semilla para barajar la bolsa (0 para aleatorio).
     */
    public Partida(List<String> jugadores, List<String> lineasArchivoBolsa, long seed) {
        this.fichasJugador1 = new ArrayList<>();
        this.fichasJugador2 = new ArrayList<>();
        this.puntosJugador1 = 0;
        this.puntosJugador2 = 0;
        this.jugador1 = jugadores.get(0);
        this.jugador2 = jugadores.get(1);
        // Inicializar bolsa
        if (seed != 0) this.bolsa = new Bolsa(lineasArchivoBolsa, seed);
        else this.bolsa = new Bolsa(lineasArchivoBolsa);
        // Inicializar tablero y estado
        this.tablero = new Tablero();
        this.contadorTurno = 0;
        this.turnoJugador = true;
        this.coordenadasPalabra = new ArrayList<>();
        // Repartir 7 fichas alternando turnos
        for (int i = 0; i < 14; i++) {
            setFicha(bolsa.sacarFicha());
            turnoJugador = !turnoJugador;
        }
    }

    /**
     * Obtiene el tablero de la partida.
     *
     * @return Tablero actual.
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Obtiene el contador de turnos jugados.
     *
     * @return Número de turnos.
     */
    public int getContadorTurno() {
        return contadorTurno;
    }

    /** Incrementa el contador de turno en uno. */
    public void aumentarContador() {
        contadorTurno++;
    }

    /**
     * Limpia las coordenadas de la palabra en curso.
     */
        /**
     * Obtiene las coordenadas de las fichas colocadas en la palabra en curso.
     *
     * @return Lista de pares (fila, columna) de la palabra actual.
     */
    public List<Pair<Integer,Integer>> getCoordenadasPalabras() {
        return coordenadasPalabra;
    }

    /**
     * Limpia las coordenadas de la palabra en curso.
     */
    public void coordenadasClear() {
        coordenadasPalabra.clear();
    }

    /**
     * Añade una ficha al tablero en la posición dada y la registra para el turno.
     * Si se trata de un comodín ('#'), se usa para colocar la letra indicada.
     *
     * @param letra Letra o comodín a colocar.
     * @param x     Fila en el tablero.
     * @param y     Columna en el tablero.
     */
    public void añadirFicha(String letra, int x, int y) {
        if (!tablero.getCelda(x, y).estaOcupada()) {
            coordenadasPalabra.add(Pair.createPair(x, y));
            Ficha ficha = getFichaString(letra);
            if (ficha == null && getFichaString("#") != null) {
                tablero.ponerComodin(letra, x, y);
                quitarFicha("#");
            } else if (ficha != null) {
                tablero.ponerFicha(ficha, x, y);
                quitarFicha(letra);
            }
        }
    }

    /**
     * Suma puntos al jugador cuyo turno es actual.
     *
     * @param pts Puntos a añadir.
     */
    public void addPuntos(int pts) {
        if (turnoJugador) puntosJugador1 += pts;
        else puntosJugador2 += pts;
    }

    /**
     * Quita la ficha del tablero en (x,y) y la devuelve a la mano del jugador.
     *
     * @param x Fila.
     * @param y Columna.
     * @return {@code true} si la ficha se retiró, {@code false} si estaba vacía.
     * @throws PosicionVaciaTablero Si no hay ficha en la posición.
     */
    public boolean quitarFichaTablero(int x, int y) throws PosicionVaciaTablero {
        Ficha f = tablero.quitarFicha(x, y);
        if (f != null) {
            coordenadasPalabra.remove(Pair.createPair(x, y));
            setFicha(f);
            return true;
        }
        return false;
    }

    /**
     * Añade una ficha a la mano del jugador actual.
     *
     * @param ficha Ficha a añadir.
     */
    public void setFicha(Ficha ficha) {
        if (turnoJugador) fichasJugador1.add(ficha);
        else fichasJugador2.add(ficha);
    }

    /**
     * Obtiene el número de fichas en mano del jugador actual.
     *
     * @return Tamaño de la lista de fichas.
     */
    public int getListSize() {
        return turnoJugador ? fichasJugador1.size() : fichasJugador2.size();
    }

    /**
     * Rellena las manos de ambos jugadores hasta 7 fichas desde la bolsa.
     *
     * @return {@code true} si se pudieron reponer todas las fichas, {@code false} si la bolsa se vació.
     */
    public boolean recuperarFichas() {
        boolean ok = true;
        while (fichasJugador1.size() < 7 && !bolsa.isEmpty()) fichasJugador1.add(bolsa.sacarFicha());
        while (fichasJugador2.size() < 7 && !bolsa.isEmpty()) fichasJugador2.add(bolsa.sacarFicha());
        if (fichasJugador1.size() < 7 || fichasJugador2.size() < 7) ok = false;
        return ok;
    }

    /** Alterna el turno entre los jugadores. */
    public void cambiarTurnoJugador() {
        turnoJugador = !turnoJugador;
    }

    /** Establece el turno actual. */
    public void setTurnoJugador(boolean turno) {
        turnoJugador = turno;
    }

    /**
     * Obtiene la ficha en índice {@code n} de la mano del jugador actual.
     *
     * @param n Índice de la ficha.
     * @return Objeto {@link Ficha} en la mano.
     */
    public Ficha getFicha(int n) {
        return turnoJugador ? fichasJugador1.get(n) : fichasJugador2.get(n);
    }

    /**
     * Busca una ficha por letra en la mano actual.
     *
     * @param s Letra de la ficha a buscar.
     * @return La ficha si existe, {@code null} en caso contrario.
     */
    public Ficha getFichaString(String s) {
        List<Ficha> mano = turnoJugador ? fichasJugador1 : fichasJugador2;
        for (Ficha f : mano) if (f.getLetra().equals(s)) return f;
        return null;
    }

    /**
     * Elimina la primera ocurrencia de la ficha con letra {@code s} de la mano actual.
     *
     * @param s Letra de la ficha a quitar.
     */
    public void quitarFicha(String s) {
        List<Ficha> mano = turnoJugador ? fichasJugador1 : fichasJugador2;
        for (int i = 0; i < mano.size(); i++) {
            if (mano.get(i).getLetra().equals(s)) {
                mano.remove(i);
                return;
            }
        }
    }

    /**
     * Verifica si la bolsa está vacía.
     *
     * @return {@code true} si no quedan fichas.
     */
    public boolean isBolsaEmpty() {
        return bolsa.isEmpty();
    }

    /**
     * Obtiene las letras de las fichas en mano del jugador actual.
     *
     * @return Lista de cadenas con las letras.
     */
    public List<String> obtenerFichas() {
        List<String> letras = new ArrayList<>();
        List<Ficha> mano = turnoJugador ? fichasJugador1 : fichasJugador2;
        for (Ficha f : mano) letras.add(f.getLetra());
        return letras;
    }

    /**
     * Obtiene el turno actual.
     *
     * @return {@code true} si es turno de jugador1, {@code false} de jugador2.
     */
    public boolean getTurnoJugador() {
        return turnoJugador;
    }

    /**
     * Bloquea en el tablero las celdas donde se colocaron fichas este turno.
     */
    public void bloquearCeldas() {
        for (Pair<Integer,Integer> p : coordenadasPalabra) 
            tablero.bloquearCelda(p.getFirst(), p.getSecond());
    }

    /**
     * Establece los puntos del jugador actual.
     *
     * @param puntos Nueva puntuación a asignar.
     */
    public void setPuntos(int puntos) {
        if (turnoJugador) puntosJugador1 = puntos;
        else puntosJugador2 = puntos;
    }

    /**
     * Obtiene la puntuación de jugador1.
     *
     * @return Puntos acumulados jugador1.
     */
    public int getPuntosJugador1() {
        return puntosJugador1;
    }

    /**
     * Obtiene la puntuación de jugador2.
     *
     * @return Puntos acumulados jugador2.
     */
    public int getPuntosJugador2() {
        return puntosJugador2;
    }

    /**
     * Obtiene la lista de fichas (objetos) del jugador actual.
     *
     * @return Lista de {@link Ficha}.
     */
    public List<Ficha> getFichasJugador() {
        return turnoJugador ? fichasJugador1 : fichasJugador2;
    }
}
