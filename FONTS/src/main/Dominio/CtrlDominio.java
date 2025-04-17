package Dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Dominio.Modelos.Jugador;      // <-- necesario para los drivers que lo usan
import Dominio.Modelos.Partida;
import Dominio.Modelos.Tablero;
import Persistencia.CtrlPersistencia;

public class CtrlDominio {

    private final CtrlPersistencia ctrlPersistencia;
    private final CtrlJugador      ctrlJugador;
    private final CtrlRanking      ctrlRanking;
    private final CtrlPartida      ctrlPartida;

    // Para la sesión de DriverAplicacion
    private String usuarioActual = null;

    public CtrlDominio() {
        ctrlPersistencia = new CtrlPersistencia();
        ctrlJugador      = new CtrlJugador();
        ctrlRanking      = new CtrlRanking(ctrlJugador);
        ctrlPartida      = new CtrlPartida();
    }

    // ————— Métodos “legacy” para otros Drivers —————————————

    /** DriverPartida... y DriverPartidaCon2Jugadores usan esto */
    public void registrarJugador(String nombre, String password) {
        ctrlJugador.crearJugador(nombre, password);
    }

    /** Para recuperar al Jugador desde otros Drivers */
    public Jugador obtenerJugador(String nombre) {
        return ctrlJugador.getJugador(nombre);
    }

    // ————— Registro/Autenticación/Perfil —————————————

    public boolean crearUsuario(String nombre, String password) {
        return ctrlJugador.crearJugador(nombre, password);
    }

    public boolean iniciarSesion(String nombre, String password) {
        boolean ok = ctrlJugador.iniciarSesion(nombre, password) != null;
        if (ok) usuarioActual = nombre;
        return ok;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public boolean haySesion() {
        return usuarioActual != null;
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public int getPuntosActual() {
        return ctrlJugador.getJugador(usuarioActual).getPuntos();
    }

    public int getPosicionActual() {
        return ctrlRanking.getPosition(usuarioActual);
    }

    public boolean cambiarPassword(String antigua, String nueva) {
        return ctrlJugador.cambiarPassword(usuarioActual, antigua, nueva);
    }

    public boolean eliminarUsuario(String password) {
        boolean ok = ctrlJugador.eliminarJugador(usuarioActual, password);
        if (ok) cerrarSesion();
        return ok;
    }

    // ————— Ranking —————————————

    public List<Map.Entry<String,Integer>> obtenerRanking() {
        return ctrlRanking.getRankingOrdenado();
    }

    public int getPosition(String nombre) {
        return ctrlRanking.getPosition(nombre);
    }

    // ————— Partida/Scrabble —————————————

    /** Firma clásica (2 jugadores) */
    public void iniciarPartida(int modo,
                               String nombre1,
                               String nombre2,
                               List<String> lineasArchivo,
                               List<String> lineasArchivoBolsa) {
        List<String> jugadores = new ArrayList<>();
        jugadores.add(nombre1);
        jugadores.add(nombre2);
        ctrlPartida.crearPartida(modo, jugadores, lineasArchivo, lineasArchivoBolsa);
    }

    /** Firma genérica */
    public void iniciarPartida(int modo,
                               List<String> jugadores,
                               List<String> lineasArchivo,
                               List<String> lineasArchivoBolsa) {
        ctrlPartida.crearPartida(modo, jugadores, lineasArchivo, lineasArchivoBolsa);
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

    public void cargarPartida(Partida partida) { /* … */ }
    public void guardarPartida(String nombre)   { /* … */ }
}
