// Dominio/CtrlDominio.java
package Dominio;

import java.util.*;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
import Dominio.Modelos.Tablero;
import Dominio.Excepciones.*;
import Persistencia.CtrlPersistencia;

/**
 * Coordina persistencia, sesión, ranking y partida.
 */
public class CtrlDominio {

    private final CtrlPersistencia ctrlPersistencia;
    private final CtrlJugador      ctrlJugador;
    private final CtrlRanking      ctrlRanking;
    private final CtrlPartida      ctrlPartida;

    public CtrlDominio() {
        this.ctrlPersistencia = new CtrlPersistencia();
        this.ctrlJugador      = new CtrlJugador();
        this.ctrlRanking      = new CtrlRanking();

        // Inicializar ranking con los usuarios ya persistidos
        Map<String,Integer> inicial = new HashMap<>();
        for (Jugador j : ctrlPersistencia.getAllUsuarios().values()) {
            inicial.put(j.getNombre(), j.getPuntos());
        }
        ctrlRanking.cargarInicial(inicial);

        this.ctrlPartida      = new CtrlPartida();
    }

    // ─── Usuarios ────────────────────────────────────────────────

    /** Registra y deja autenticado */
    public void registrarJugador(String nombre, String password)
            throws UsuarioYaRegistradoException {
        Jugador j = new Jugador(nombre, password);
        ctrlPersistencia.addJugador(j);
        ctrlJugador.setJugadorActual(j);
        ctrlRanking.reportarPuntuacion(nombre, 0);
    }

    /** Alias de registrarJugador */
    public void crearUsuario(String nombre, String password)
            throws UsuarioYaRegistradoException {
        registrarJugador(nombre, password);
    }

    /** Recupera cualquier jugador */
    public Jugador obtenerJugador(String nombre)
            throws UsuarioNoEncontradoException {
        Jugador j = ctrlPersistencia.getJugador(nombre);
        if (j == null) throw new UsuarioNoEncontradoException(nombre);
        return j;
    }

    /** Inicia sesión con un jugador existente */
    public void iniciarSesion(String nombre, String password)
            throws UsuarioNoEncontradoException, AutenticacionException {
        Jugador j = ctrlPersistencia.getJugador(nombre);
        if (j == null) throw new UsuarioNoEncontradoException(nombre);
        if (!j.validarPassword(password)) throw new AutenticacionException();
        ctrlJugador.setJugadorActual(j);
    }

    /** Cierra la sesión actual */
    public void cerrarSesion() {
        ctrlJugador.clearSesion();
    }

    /** ¿Hay usuario autenticado? */
    public boolean haySesion() {
        return ctrlJugador.haySesion();
    }

    /** Nombre del usuario activo (o null) */
    public String getUsuarioActual() {
        Jugador j = ctrlJugador.getJugadorActual();
        return j != null ? j.getNombre() : null;
    }

    /** Puntos del usuario activo (o 0) */
    public int getPuntosActual() {
        Jugador j = ctrlJugador.getJugadorActual();
        return j != null ? j.getPuntos() : 0;
    }

    /** Cambia la contraseña y persiste */
    public void cambiarPassword(String antigua, String nueva)
            throws PasswordInvalidaException {
        ctrlJugador.cambiarPassword(antigua, nueva);
        Jugador j = ctrlJugador.getJugadorActual();
        if (j != null) ctrlPersistencia.updateJugador(j);
    }

    /** Elimina al usuario activo y lo borra de la persistencia */
    public void eliminarUsuario(String password)
            throws PasswordInvalidaException {
        Jugador j = ctrlJugador.getJugadorActual();
        if (j == null) return;
        ctrlJugador.eliminarJugador(password);
        ctrlPersistencia.removeJugador(j.getNombre());
    }

    // ─── Ranking ────────────────────────────────────────────────

    public List<Map.Entry<String,Integer>> obtenerRanking() {
        return ctrlRanking.getRankingOrdenado();
    }

    /** Posición del usuario activo, -1 si no hay sesión o no está en ranking */
    public int getPosicionActual() {
        String nombre = getUsuarioActual();
        if (nombre == null) return -1;
        try {
            return ctrlRanking.getPosition(nombre);
        } catch (UsuarioNoEncontradoException e) {
            return -1;
        }
    }

    /** Posición de cualquier jugador */
    public int getPosition(String nombre)
            throws UsuarioNoEncontradoException {
        return ctrlRanking.getPosition(nombre);
    }

    // ─── Partida / Scrabble ────────────────────────────────────

    public void iniciarPartida(int modo,
                               String n1,
                               String n2,
                               List<String> lineasDicc,
                               List<String> lineasBolsa) {
        ctrlPartida.crearPartida(modo, Arrays.asList(n1, n2), lineasDicc, lineasBolsa);
    }

    public void jugarScrabble(int modo, String jugada) {
        String nombre = getUsuarioActual();
        ctrlPartida.jugarScrabble(modo, jugada);

        // Actualizar puntos en memoria y en disco
        ctrlJugador.actualizarPuntuacion(ctrlPartida.getPuntosJugador1());
        Jugador j = ctrlJugador.getJugadorActual();
        if (j != null) ctrlPersistencia.updateJugador(j);

        // Actualizar ranking en memoria
        ctrlRanking.reportarPuntuacion(nombre, ctrlPartida.getPuntosJugador1());
    }

    public Tablero obtenerTablero() {
        return ctrlPartida.obtenerTablero();
    }

    public List<String> obtenerFichas() {
        return ctrlPartida.obtenerFichas();
    }

    public int getPuntosJugador1() {
        return ctrlPartida.getPuntosJugador1();
    }

    public int getPuntosJugador2() {
        return ctrlPartida.getPuntosJugador2();
    }

    public void guardarPartida(String id) {
        ctrlPersistencia.guardarPartida(id, ctrlPartida.guardarPartida());
    }

    public void cargarPartida(String id) {
        ctrlPartida.cargarPartida(ctrlPersistencia.cargarPartida(id));
    }
}
