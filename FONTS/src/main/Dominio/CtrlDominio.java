package Dominio;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import Dominio.Excepciones.*;
import Dominio.Modelos.Celda;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
import Dominio.Modelos.TipoBonificacion;
import Persistencia.CtrlPersistencia;

/**
 * Coordina persistencia, sesión, ranking y partida.
 */
public class CtrlDominio {

    private final CtrlPersistencia ctrlPersistencia;
    private final CtrlJugador    ctrlJugador;
    private final CtrlPartida    ctrlPartida;

    public CtrlDominio() {
        this.ctrlPersistencia = new CtrlPersistencia();
        this.ctrlJugador      = new CtrlJugador();
        this.ctrlPartida      = new CtrlPartida();
    }

    // ─── Usuarios ────────────────────────────────────────────────────────────────

    public void registrarJugador(String nombre, String password)
            throws UsuarioYaRegistradoException {
        Jugador j = new Jugador(nombre, password);
        ctrlPersistencia.addJugador(j);
        ctrlJugador.setJugadorActual(j);
    }

 

    public void iniciarSesion(String nombre, String password)
            throws UsuarioNoEncontradoException, PasswordInvalidaException {
        Jugador j = ctrlPersistencia.getJugador(nombre);
        if (j == null) {
            throw new UsuarioNoEncontradoException(nombre);
        }
        if (!j.validarPassword(password)) {
            throw new PasswordInvalidaException();
        }
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
        return (j != null) ? j.getNombre() : null;
    }

    public int getPuntosActual() {
        Jugador j = ctrlJugador.getJugadorActual();
        return (j != null) ? j.getPuntos() : 0;
    }

    public void cambiarPassword(String antigua, String nueva)
            throws PasswordInvalidaException, UsuarioNoEncontradoException {
        ctrlJugador.cambiarPassword(antigua, nueva);
        Jugador j = ctrlJugador.getJugadorActual();
        if (j != null) {
            ctrlPersistencia.updateJugador(j);
        }
    }

    public void eliminarUsuario(String password)
            throws PasswordInvalidaException, UsuarioNoEncontradoException {
        Jugador j = ctrlJugador.getJugadorActual();
        if (j != null) {
            ctrlJugador.eliminarJugador(password);
            ctrlPersistencia.removeJugador(j.getNombre());
        }
    }

    // ─── Ranking ────────────────────────────────────────────────────────────────

    public List<Map.Entry<String,Integer>> obtenerRanking() throws RankingVacioException {
        return ctrlPersistencia.obtenerRanking();
    }

    public int getPosition(String nombre)
            throws UsuarioNoEncontradoException {
        try {
            return ctrlPersistencia.getPosition(nombre);
        } catch (NoSuchElementException e) {
            throw new UsuarioNoEncontradoException(nombre);
        }
    }

    /**
     * Para el driver de usuarios (mismo que getPosicionActual).
     */
    public int getPosicion() {
        return getPosicionActual();
    }

    public int getPosicionActual() {
        String nombre = getUsuarioActual();
        if (nombre == null) {
            return -1;
        }
        try {
            return getPosition(nombre);
        } catch (UsuarioNoEncontradoException e) {
            return -1;
        }
    }

    // ─── Partida / Scrabble ──────────────────────────────────────────────────────

public void iniciarPartida(int modo,
            String n1,
            String n2,
            String idDiccionario,
            long seed,
            boolean jugadorAlgoritmo) throws DiccionarioNoEncontradoException, BolsaNoEncontradaException {
        try {
            List<String> dic = ctrlPersistencia.getDiccionario(idDiccionario);
            List<String> bolsa = ctrlPersistencia.getBolsa(idDiccionario);

            ctrlPartida.crearPartida(
                modo,
                Arrays.asList(n1, n2),
                dic,
                bolsa,
                seed,
                jugadorAlgoritmo
            );
        }
        catch (DiccionarioNoEncontradoException e) {
            // relanzamos con mensaje más contextualizado, si queremos
            throw new DiccionarioNoEncontradoException();
        }
        catch (BolsaNoEncontradaException e) {
            throw new BolsaNoEncontradaException();
        }
    }

    public int jugarScrabble(int modo, String jugada)
            throws   PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException {
        int fin = ctrlPartida.jugarScrabble(modo, jugada);
        ctrlJugador.actualizarPuntuacion(ctrlPartida.getPuntosJugador1());
        Jugador j = ctrlJugador.getJugadorActual();
        if (j != null) {
            ctrlPersistencia.reportarPuntuacion(j.getNombre(), j.getPuntos());
        }
        return fin;         
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

    public void guardarPartida(String id) throws PartidaYaExistenteException {
        Partida p = ctrlPartida.guardarPartida();
        ctrlPersistencia.guardarPartida(id, p);
    }

    public void cargarPartida(String id) throws PartidaNoEncontradaException {
        Partida p = ctrlPersistencia.cargarPartida(id);
        ctrlPartida.cargarPartida(p);
    }

    public void cargarUltimaPartida() throws NoHayPartidaGuardadaException {
        Partida p = ctrlPersistencia.cargarUltimaPartida();
        ctrlPartida.cargarPartida(p);
    }

    public Set<String> obtenerNombresPartidasGuardadas() {
        return ctrlPersistencia.getListaPartidas().keySet();
    }

    public void eliminarPartidaGuardada(String id) throws PartidaNoEncontradaException {
        ctrlPersistencia.removePartida(id);
    }

    // ─── Helpers para driver ────────────────────────────────────────────────────

    public int getTableroDimension() {
        return 15;
    }

    public String getLetraCelda(int fila, int col) {
        Celda cel = ctrlPartida.obtenerTablero().getCelda(fila, col);
        return cel.getFicha() != null
                ? (cel.getFicha().getPuntuacion() == 0
                   ? "#" : cel.getFicha().getLetra())
                : null;
    }

    public String getBonusCelda(int fila, int col) {
        Celda cel = ctrlPartida.obtenerTablero().getCelda(fila, col);
        if (!cel.bonusDisponible()
                && cel.getBonificacion() != TipoBonificacion.NINGUNA) {
            return "US";
        }
        return switch (cel.getBonificacion()) {
            case DOBLE_LETRA   -> "DL";
            case TRIPLE_LETRA  -> "TL";
            case DOBLE_PALABRA -> "DP";
            case TRIPLE_PALABRA-> "TP";
            default            -> "  ";
        };
    }

    // ─── Diccionarios y Bolsas ──────────────────────────────────────────────────

    public Set<String> obtenerIDsDiccionarios() {
        return ctrlPersistencia.getDiccionarioIDs();
    }

    public Set<String> obtenerIDsBolsas() {
        return ctrlPersistencia.getBolsaIDs();
    }

    public void crearDiccionario(String id, List<String> palabras)
            throws IOException, DiccionarioYaExistenteException {
        ctrlPersistencia.addDiccionario(id, palabras);
    }

    public void crearBolsa(String id, Map<String,int[]> datos)
            throws IOException, BolsaYaExistenteException {
        ctrlPersistencia.addBolsa(id, datos);
    }

    public void eliminarIdiomaCompleto(String id) throws IOException, DiccionarioNoEncontradoException, BolsaNoEncontradaException {
        ctrlPersistencia.removeIdiomaCompleto(id);
    }

    public List<String> getDiccionario(String id) throws DiccionarioNoEncontradoException {
        try {
            return ctrlPersistencia.getDiccionario(id);
        }
        catch (DiccionarioNoEncontradoException e) {
            throw new DiccionarioNoEncontradoException();
        }
    }

    /**
     * Recupera una bolsa; traduce la excepción de persistencia
     * a la de dominio si el ID no existe.
     * @throws BolsaNoEncontradaException 
     */
    public List<String> getBolsa(String id) throws BolsaNoEncontradaException {
        try {
            return ctrlPersistencia.getBolsa(id);
        }
        catch (BolsaNoEncontradaException e) {
            throw new BolsaNoEncontradaException();
        }
    }

}