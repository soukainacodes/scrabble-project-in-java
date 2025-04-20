package Dominio;

import java.util.*;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
import Dominio.Modelos.Tablero;
import Dominio.Modelos.Celda;
import Dominio.Modelos.TipoBonificacion;
import Dominio.Excepciones.*;
import Persistencia.CtrlPersistencia;

/**
 * Coordina persistencia, sesión, ranking y partida.
 */
public class CtrlDominio {

    private final CtrlPersistencia ctrlPersistencia;
    private final CtrlJugador      ctrlJugador;
    private final CtrlPartida      ctrlPartida;

    public CtrlDominio() {
        this.ctrlPersistencia = new CtrlPersistencia();
        this.ctrlJugador      = new CtrlJugador();
        this.ctrlPartida      = new CtrlPartida();
    }

    // ─── Usuarios ────────────────────────────────────────────────

    public void registrarJugador(String nombre, String password)
            throws UsuarioYaRegistradoException {
        Jugador j = new Jugador(nombre, password);
        ctrlPersistencia.addJugador(j);
        ctrlJugador.setJugadorActual(j);
    }

    public void crearUsuario(String nombre, String password)
            throws UsuarioYaRegistradoException {
        registrarJugador(nombre, password);
    }

    public Jugador obtenerJugador(String nombre)
            throws UsuarioNoEncontradoException {
        Jugador j = ctrlPersistencia.getJugador(nombre);
        if (j == null) throw new UsuarioNoEncontradoException(nombre);
        return j;
    }

    public void iniciarSesion(String nombre, String password)
            throws UsuarioNoEncontradoException, AutenticacionException {
        Jugador j = ctrlPersistencia.getJugador(nombre);
        if (j == null) throw new UsuarioNoEncontradoException(nombre);
        if (!j.validarPassword(password)) throw new AutenticacionException();
        ctrlJugador.setJugadorActual(j);
    }

    public void cerrarSesion() {
        ctrlJugador.clearSesion();
    }

    public boolean haySesion() {
        return ctrlJugador.haySesion();
    }

    public String getUsuarioActual() {
        Jugador j = ctrlJugador.getJugadorActual();
        return j != null ? j.getNombre() : null;
    }

    public int getPuntosActual() {
        Jugador j = ctrlJugador.getJugadorActual();
        return j != null ? j.getPuntos() : 0;
    }

    public void cambiarPassword(String antigua, String nueva)
            throws PasswordInvalidaException {
        ctrlJugador.cambiarPassword(antigua, nueva);
        Jugador j = ctrlJugador.getJugadorActual();
        if (j != null) ctrlPersistencia.updateJugador(j);
    }

    public void eliminarUsuario(String password)
            throws PasswordInvalidaException {
        Jugador j = ctrlJugador.getJugadorActual();
        if (j == null) return;
        ctrlJugador.eliminarJugador(password);
        ctrlPersistencia.removeJugador(j.getNombre());
    }

    // ─── Ranking ────────────────────────────────────────────────

    public List<Map.Entry<String,Integer>> obtenerRanking() {
        return ctrlPersistencia.obtenerRanking();
    }

    public int getPosition(String nombre) throws UsuarioNoEncontradoException {
        try {
            return ctrlPersistencia.getPosition(nombre);
        } catch (NoSuchElementException e) {
            throw new UsuarioNoEncontradoException(nombre);
        }
    }

    public int getPosicionActual() {
        String nombre = getUsuarioActual();
        if (nombre == null) return -1;
        try {
            return getPosition(nombre);
        } catch (UsuarioNoEncontradoException e) {
            return -1;
        }
    }

    public int getPosicion() {
        return getPosicionActual();
    }

    // ─── Partida / Scrabble ────────────────────────────────────

    public void iniciarPartida(int modo,
                               String n1,
                               String n2,
                               List<String> lineasDicc,
                               List<String> lineasBolsa,
                               long seed,
                               int dificultad) {
        ctrlPartida.crearPartida(
            modo,
            Arrays.asList(n1, n2),
            lineasDicc,
            lineasBolsa,
            seed,
            dificultad
        );
    }

<<<<<<< HEAD
    /**
     * Ejecuta una jugada y actualiza puntuación
     * tanto en memoria como en persistencia.
     */
    public int jugarScrabble(int modo, String jugada)
=======
    public void jugarScrabble(int modo, String jugada)
>>>>>>> 4b1bb601cf70b8e03cb7ddba2c1e33b580d3ab3f
            throws PosicionOcupadaTablero,
                   PosicionVaciaTablero,
                   FichaIncorrecta {
       int fin = ctrlPartida.jugarScrabble(modo, jugada);
        // Actualizar puntos en jugador y persistencia
        ctrlJugador.actualizarPuntuacion(ctrlPartida.getPuntosJugador1());
        Jugador j = ctrlJugador.getJugadorActual();
        if (j != null) {
            ctrlPersistencia.reportarPuntuacion(j.getNombre(), j.getPuntos());
        }
        return fin; 
    }

<<<<<<< HEAD

=======
    /** Obtiene el tablero completo */
>>>>>>> 4b1bb601cf70b8e03cb7ddba2c1e33b580d3ab3f
    public Tablero obtenerTablero() {
        return ctrlPartida.obtenerTablero();
    }

    /** Obtiene la lista de fichas actuales */
    public List<String> obtenerFichas() {
        return ctrlPartida.obtenerFichas();
    }

    /** Puntos del jugador 1 en la partida actual */
    public int getPuntosJugador1() {
        return ctrlPartida.getPuntosJugador1();
    }

    /** Puntos del jugador 2 (IA) en la partida actual */
    public int getPuntosJugador2() {
        return ctrlPartida.getPuntosJugador2();
    }

    /** Intercambia/restablece fichas en la partida (si tu lógica lo permite) */
    public void cambiarFichas(String fichas)
            throws PosicionVaciaTablero, PosicionOcupadaTablero, FichaIncorrecta {
        ctrlPartida.reset(fichas);
    }

    public void guardarPartida(String id) {
        Partida p = ctrlPartida.guardarPartida();
        ctrlPersistencia.guardarPartida(id, p);
    }

    public void cargarPartida(String id) {
        Partida p = ctrlPersistencia.cargarPartida(id);
        ctrlPartida.cargarPartida(p);
    }

    public void cargarUltimaPartida() {
        Partida p = ctrlPersistencia.cargarUltimaPartida();
        ctrlPartida.cargarPartida(p);
    }

    // ─── Gestión de partidas guardadas ────────────────────────

    public Set<String> obtenerNombresPartidasGuardadas() {
        return ctrlPersistencia.getListaPartidas().keySet();
    }

    public void eliminarPartidaGuardada(String id) {
        ctrlPersistencia.removePartida(id);
    }

    // ─── Métodos auxiliares (opcional) ────────────────────────

    /** Dimensión fija del tablero (15×15) */
    public int getTableroDimension() {
        return 15;
    }

    /**
     * Letra en celda (fila,col) o null si está vacía.
     * Para no exponer Celda en el driver.
     */
    public String getLetraCelda(int fila, int col) {
        Celda cel = ctrlPartida.obtenerTablero().getCelda(fila, col);
        return cel.getFicha() != null ? cel.getFicha().getLetra() : null;
    }

    /**
     * Código de bonificación en celda (fila,col):
     * "US" si estaba y ya se usó,
     * "DL","TL","DP","TP" según el tipo,
     * "  " si no hay bonus.
     */
    public String getBonusCelda(int fila, int col) {
        Celda cel = ctrlPartida.obtenerTablero().getCelda(fila, col);
        if (!cel.bonusDisponible() &&
            cel.getBonificacion() != TipoBonificacion.NINGUNA) {
            return "US";
        }
        return switch (cel.getBonificacion()) {
            case DOBLE_LETRA   -> "DL";
            case TRIPLE_LETRA  -> "TL";
            case DOBLE_PALABRA -> "DP";
            case TRIPLE_PALABRA-> "TP";
            default             -> "  ";
        };
    }
}
