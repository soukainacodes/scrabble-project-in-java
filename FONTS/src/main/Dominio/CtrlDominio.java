package Dominio;

import java.util.*;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
import Dominio.Modelos.Tablero;
import Dominio.Excepciones.*;
import Persistencia.CtrlPersistencia;

public class CtrlDominio {

    private final CtrlPersistencia ctrlPersistencia;
    private final CtrlJugador      ctrlJugador;
    private final CtrlRanking      ctrlRanking;
    private final CtrlPartida      ctrlPartida;

    private String usuarioActual = null;

    public CtrlDominio() {
        ctrlPersistencia = new CtrlPersistencia();
        ctrlJugador      = new CtrlJugador();
        ctrlRanking      = new CtrlRanking(ctrlJugador);
        ctrlPartida      = new CtrlPartida();
    }

    // ─── Gestión de usuarios ────────────────────────────────────────

    public void registrarJugador(String nombre, String password)
            throws UsuarioYaRegistradoException {
        if (!ctrlJugador.crearJugador(nombre, password))
            throw new UsuarioYaRegistradoException(nombre);
    }

    public Jugador obtenerJugador(String nombre)
            throws UsuarioNoEncontradoException {
        Jugador j = ctrlJugador.getJugador(nombre);
        if (j == null) throw new UsuarioNoEncontradoException(nombre);
        return j;
    }

    public void crearUsuario(String nombre, String password)
            throws UsuarioYaRegistradoException {
        registrarJugador(nombre, password);
    }

    public void iniciarSesion(String nombre, String password)
            throws AutenticacionException {
        Jugador j = ctrlJugador.iniciarSesion(nombre, password);
        if (j == null) throw new AutenticacionException();
        usuarioActual = nombre;
    }

    public void cerrarSesion()
            throws SesionNoIniciadaException {
        if (usuarioActual == null) throw new SesionNoIniciadaException();
        usuarioActual = null;
    }

    public boolean haySesion() {
        return usuarioActual != null;
    }

    public String getUsuarioActual()
            throws SesionNoIniciadaException {
        if (usuarioActual == null) throw new SesionNoIniciadaException();
        return usuarioActual;
    }

    public int getPuntosActual()
            throws SesionNoIniciadaException {
        String u = getUsuarioActual();
        return ctrlJugador.getJugador(u).getPuntos();
    }

    public int getPosicionActual()
            throws SesionNoIniciadaException {
        String u = getUsuarioActual();
        return ctrlRanking.getPosition(u);
    }

    public void cambiarPassword(String antigua, String nueva)
            throws SesionNoIniciadaException, PasswordInvalidaException {
        String u = getUsuarioActual();
        if (!ctrlJugador.cambiarPassword(u, antigua, nueva))
            throw new PasswordInvalidaException();
    }

    public void eliminarUsuario(String password)
            throws SesionNoIniciadaException, PasswordInvalidaException {
        String u = getUsuarioActual();
        if (!ctrlJugador.eliminarJugador(u, password))
            throw new PasswordInvalidaException();
        usuarioActual = null;
    }

    // ─── Ranking ───────────────────────────────────────────────────────

    public List<Map.Entry<String,Integer>> obtenerRanking() {
        return ctrlRanking.getRankingOrdenado();
    }

    public int getPosition(String nombre)
            throws UsuarioNoEncontradoException {
        int pos = ctrlRanking.getPosition(nombre);
        if (pos < 0) throw new UsuarioNoEncontradoException(nombre);
        return pos;
    }

    // ─── Partida / Scrabble ───────────────────────────────────────────

    public void iniciarPartida(int modo,
                               String nombre1,
                               String nombre2,
                               List<String> lineasDicc,
                               List<String> lineasBolsa) {
        List<String> jugadores = Arrays.asList(nombre1, nombre2);
        ctrlPartida.crearPartida(modo, jugadores, lineasDicc, lineasBolsa);
    }

    public void iniciarPartida(int modo,
                               List<String> jugadores,
                               List<String> lineasDicc,
                               List<String> lineasBolsa) {
        ctrlPartida.crearPartida(modo, jugadores, lineasDicc, lineasBolsa);
    }

    public void jugarScrabble(String jugada) {
        ctrlPartida.jugarScrabble(jugada);
    }

    public Tablero obtenerTablero() {
        return ctrlPartida.obtenerTablero();
    }

    public List<String> obtenerFichas() {
        return ctrlPartida.obtenerFichas();
    }

    public List<String> obtenerFichasAlgoritmo() {
        return ctrlPartida.obtenerFichasAlgoritmo();
    }

    public int getPuntosJugador1() {
        return ctrlPartida.getPuntosJugador1();
    }

    public int getPuntosJugador2() {
        return ctrlPartida.getPuntosJugador2();
    }

    public void cargarPartida(Partida partida) { /*…*/ }
    public void guardarPartida(String nombre)    { /*…*/ }
}
