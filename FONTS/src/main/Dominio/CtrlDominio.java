package Dominio;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import Dominio.Excepciones.*;
import Persistencia.CtrlPersistencia;

/**
 * Controlador principal del dominio que coordina la lógica de negocio,
 * incluyendo gestión de usuarios, ranking y manejo de partidas de Scrabble.
 * <p>
 * Este controlador orquesta llamadas a los subcontroladores de jugador
 * ({@link CtrlJugador}) y partida ({@link CtrlPartida}), así como a la capa
 * de persistencia ({@link CtrlPersistencia}).
 */
public class CtrlDominio {

    /** Controlador de persistencia para acceso a datos (jugadores, partidas y recursos). */
    private final CtrlPersistencia ctrlPersistencia;
    
    /** Controlador de la sesión del jugador activo. */
    private final CtrlJugador ctrlJugador;

    /** Controlador de la partida en curso. */
    private final CtrlPartida ctrlPartida;

    private final DataConverter dc;

    /**
     * Construye una instancia de CtrlDominio, inicializando los controladores
     * de persistencia, jugador y partida.
     */
    public CtrlDominio() {
        this.ctrlPersistencia = new CtrlPersistencia();
        this.ctrlJugador = new CtrlJugador();
        this.ctrlPartida = new CtrlPartida();
        this.dc = new DataConverter();
    }

    // ─── Gestión de Jugadores ───────────────────────────────────────────────────

    public void registrarJugador(String nombre, String password) throws IOException, UsuarioYaRegistradoException {
        ctrlPersistencia.anadirJugador(nombre, password);
    }

    
    public void iniciarSesion(String nombre, String password) throws UsuarioNoEncontradoException
    {
        if (ctrlPersistencia.existeJugador(nombre) && !ctrlJugador.haySesion() && ctrlPersistencia.verificarContrasena(nombre, password))
        {
            ctrlJugador.setJugadorActual(nombre);;
        }
    }


    public void cerrarSesion() {
        ctrlJugador.clearSesion();
    }

    
    public boolean haySesion() {
        return ctrlJugador.haySesion();
    }

    
    public String getUsuarioActual() {
        if (ctrlJugador.haySesion())  return ctrlJugador.getJugadorActual().getNombre();
        
        return null;
    }

    
    public int getPuntosActual() throws UsuarioNoEncontradoException {

        if (ctrlJugador.haySesion()) {
            return ctrlPersistencia.obtenerPuntuacion(ctrlJugador.getJugadorActual().getNombre());
        }
        return 0;
    }

    
    public void cambiarPassword(String antigua, String nueva) throws UsuarioNoEncontradoException{
        if (ctrlJugador.haySesion()) 
            ctrlPersistencia.actualizarContrasena(ctrlJugador.getJugadorActual().getNombre(), nueva);
        
    }
    

    public void eliminarUsuario(String password) throws UsuarioNoEncontradoException, IOException   {
        if (ctrlJugador.haySesion() && ctrlPersistencia.verificarContrasena(ctrlJugador.getJugadorActual().getNombre(), password)) {
            ctrlPersistencia.eliminarJugador(ctrlJugador.getJugadorActual().getNombre());
            ctrlJugador.clearSesion();
        }
    }

    // ─── Ranking ────────────────────────────────────────────────────────────────

    /**
     * Obtiene el ranking global de jugadores ordenado por puntuación descendente.
     *
     * @return lista de pares (nombre, puntuación) ordenada.
     * @throws RankingVacioException si no hay datos de ranking.
     * @throws IOException
     */
    public List<Map.Entry<String, Integer>> obtenerRanking()
            throws RankingVacioException, IOException {
        return ctrlPersistencia.generarRanking();
    }

    /**
     * Obtiene la posición en ranking de un jugador dado.
     *
     * @param nombre nombre de usuario.
     * @return posición (1-based) en el ranking.
     * @throws UsuarioNoEncontradoException si el jugador no figura.
     * @throws IOException
     */
    public int obtenerPosicion(String nombre)
            throws UsuarioNoEncontradoException, IOException {
        try {
            return ctrlPersistencia.obtenerPosicion(nombre);
        } catch (NoSuchElementException e) {
            throw new UsuarioNoEncontradoException(nombre);
        }
    }

   

    // ─── Partida / Scrabble ──────────────────────────────────────────────────────

    /**
     * Inicia una nueva partida de Scrabble con datos persistidos.
     *
     * @param modo             0=jugador vs IA, 1=dos jugadores.
     * @param n1               nombre jugador 1 (activo).
     * @param n2               nombre jugador 2 o IA.
     * @param idDiccionario    ID de diccionario/bolsa.
     * @param seed             semilla para aleatoriedad.
     * @param jugadorAlgoritmo {@code true} si jugador 2 es IA.
     * @throws DiccionarioNoEncontradoException si no existe el diccionario.
     * @throws BolsaNoEncontradaException       si no existe la bolsa.
     * @throws IOException
     */
    public void iniciarPartida(int modo,
            String n2,
            String idDiccionario,
            long seed,
            boolean jugadorAlgoritmo)
            throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, UsuarioNoEncontradoException,
            PasswordInvalidaException, IOException {

        List<String> dic = ctrlPersistencia.obtenerDiccionario(idDiccionario);
        List<String> bolsa = ctrlPersistencia.obtenerBolsa(idDiccionario);

        if (modo == 0) {
            ctrlJugador.setSegundoJugador("propAI");
            n2 = "propAI";
            ctrlPartida.crearPartida(modo, Arrays.asList(ctrlJugador.getJugadorActual().getNombre(), n2), dic, bolsa, seed,
                jugadorAlgoritmo);

        }
        else 
        {
            ctrlPartida.crearPartida(modo, Arrays.asList(ctrlJugador.getJugadorActual().getNombre(), n2), dic, bolsa, seed,
                jugadorAlgoritmo);
        }
        

    }

    /**
     * Gestiona una jugada de Scrabble delegando en CtrlPartida y actualiza puntos.
     *
     * @param modo   código de acción de Scrabble.
     * @param jugada parámetros de jugada.
     * @return valor de fin de turno o fin de partida.
     * @throws PuntuacionInvalidaException  si la puntuación es inválida.
     * @throws ComandoInvalidoException     si el comando es malformado.
     * @throws PalabraInvalidaException     si la palabra no es válida.
     * @throws UsuarioNoEncontradoException
     */
    public int jugarScrabble(int modo, String jugada)
            throws PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException,
            UsuarioNoEncontradoException {

        int fin = ctrlPartida.jugarScrabble(modo, jugada);
        
        // Actualiza la puntuación del jugador activo y openente
        ctrlPersistencia.actualizarPuntuacion(ctrlJugador.getJugadorActual().getNombre(),ctrlPartida.getPuntosJugador1());
        ctrlPersistencia.actualizarPuntuacion(ctrlJugador.getSegundoJugador().getNombre(),ctrlPartida.getPuntosJugador2());

        return fin;
    }

    /**
     * Obtiene las fichas del jugador activo en la partida.
     *
     * @return lista de cadenas de fichas.
     */
    public List<String> obtenerFichas() {
        return ctrlPartida.obtenerFichas();
    }

    /**
     * Recupera la puntuación del jugador 1 en la partida.
     *
     * @return puntos acumulados.
     */
    public int getPuntosJugador1() {
        return ctrlPartida.getPuntosJugador1();
    }

    /**
     * Recupera la puntuación del jugador 2 o IA en la partida.
     *
     * @return puntos acumulados.
     */
    public int getPuntosJugador2() {
        return ctrlPartida.getPuntosJugador2();
    }

    /**
     * Guarda la partida actual en persistencia con un identificador.
     *
     * @param id nombre de la partida a crear.
     * @throws PartidaYaExistenteException  si ya existe una partida con ese id.
     * @throws UsuarioNoEncontradoException
     */
    public void guardarPartida(String id) throws PartidaYaExistenteException, UsuarioNoEncontradoException {
        ctrlPersistencia.guardarPartida(ctrlJugador.getJugadorActual().getNombre(), id,
                dc.partidaToStringList(ctrlPartida.guardarPartida(), id));
        ctrlPersistencia.guardarPartida(ctrlJugador.getSegundoJugador().getNombre(), id,
                dc.partidaToStringList(ctrlPartida.guardarPartida(), id));

    }

    /**
     * Carga una partida guardada desde persistencia.
     *
     * @param id identificador de la partida.
     * @throws PartidaNoEncontradaException si no existe la partida.
     */
    public void cargarPartida(String id) throws PartidaNoEncontradaException {
        // Partida p = ctrlPersistencia.cargarPartida(id);
        ctrlPartida.cargarPartida(dc.stringListToPartida(ctrlPersistencia.cargarPartida(id)));
    }

    /**
     * Carga la última partida guardada automáticamente.
     *
     * @throws NoHayPartidaGuardadaException si no hay partidas previas.
     * @throws PartidaNoEncontradaException
     * @throws UsuarioNoEncontradoException
     */
    public void cargarUltimaPartida()
            throws NoHayPartidaGuardadaException, PartidaNoEncontradaException, UsuarioNoEncontradoException {
        // Obtener los datos de la última partida desde persistencia
        String ultimaPartida = ctrlPersistencia.obtenerUltimaPartida(getUsuarioActual());
        List<String> datosUltimaPartida = ctrlPersistencia.cargarPartida(ultimaPartida);
        ctrlPartida.cargarPartida(dc.stringListToPartida(datosUltimaPartida));
    }

    /**
     * Lista los identificadores de partidas guardadas.
     *
     * @return conjunto de nombres de partidas disponibles.
     */
    public List<String> obtenerNombresPartidasGuardadas() {
        return ctrlPersistencia.listarPartidasNoAcabadas();
    }

    /**
     * Elimina una partida guardada del sistema.
     *
     * @param id identificador de la partida a eliminar.
     * @throws PartidaNoEncontradaException si no existe la partida.
     */
    public void eliminarPartidaGuardada(String id) throws PartidaNoEncontradaException {
        ctrlPersistencia.eliminarPartida(id);
    }

    // ─── Auxiliares de Presentación ────────────────────────────────────────────

    /**
     * Dimensión fija del tablero de Scrabble.
     *
     * @return valor 15 (tamaño estándar).
     */
    public int getTableroDimension() {
        return 15;
    }

    /**
     * Obtiene la letra o marcador de una celda en la presentación.
     *
     * @param fila índice de fila.
     * @param col  índice de columna.
     * @return cadena con letra o null si vacía.
     */
    public String getLetraCelda(int fila, int col) {
        return ctrlPartida.obtenerTablero().getLetraCelda(fila, col);
    }

    /**
     * Obtiene el código de bonificación de celda para presentación.
     *
     * @param fila fila de la celda.
     * @param col  columna de la celda.
     * @return abreviatura de bonificación (DL, TL, DP, TP) o espacios.
     */
    public String getBonusCelda(int fila, int col) {
        return ctrlPartida.obtenerTablero().getBonusCelda(fila, col);
    }

    // ─── Gestión de Diccionarios y Bolsas ───────────────────────────────────────

    /**
     * Recupera la lista de recursos (diccionarios y bolsas) disponibles.
     *
     * @return lista de identificadores de recursos.
     */
    public List<String> obtenerRecursos() {
        return ctrlPersistencia.listarRecursos();
    }

    /**
     * Crea un diccionario persistente con lista de palabras.
     *
     * @param id       identificador del idioma.
     * @param palabras lista de palabras a almacenar.
     * @throws IOException                     si falla la escritura en disco.
     * @throws DiccionarioYaExistenteException si ya existe el ID.
     * @throws RecursoExistenteException
     */
    public void crearDiccionario(String id, List<String> palabras)
            throws IOException, DiccionarioYaExistenteException, RecursoExistenteException {
        ctrlPersistencia.crearDiccionario(id, palabras);
    }

    /**
     * Crea una bolsa persistente con configuración de fichas.
     *
     * @param id    identificador del idioma.
     * @param datos mapa de letra->[cantidad,puntuación].
     * @throws IOException               si falla la escritura en disco.
     * @throws BolsaYaExistenteException si ya existe el ID.
     * @throws RecursoExistenteException
     */
    public void crearBolsa(String id, Map<String, int[]> datos)
            throws IOException, RecursoExistenteException {
        ctrlPersistencia.crearBolsa(id, datos);
    }

    /**
     * Elimina completamente un idioma (diccionario+bolsa) de persistencia.
     *
     * @param id identificador del idioma.
     * @throws IOException                      si falla la eliminación de archivos.
     * @throws BolsaNoEncontradaException       si no existe la bolsa.
     * @throws DiccionarioNoEncontradoException 
     */
    public void eliminarRecurso(String id)
            throws IOException, RecursoNoExistenteException,
            BolsaNoEncontradaException, DiccionarioNoEncontradoException {
        ctrlPersistencia.eliminarRecurso(id);
    }

    /**
     * Recupera la lista de palabras de un diccionario.
     *
     * @param id identificador del diccionario.
     * @return lista de palabras.
     * @throws DiccionarioNoEncontradoException si no existe el ID.
     * @throws IOException
     */
    public List<String> obtenerDiccionario(String id) throws DiccionarioNoEncontradoException, IOException {
        return ctrlPersistencia.obtenerDiccionario(id);
    }

    /**
     * Recupera una bolsa de fichas para un idioma.
     *
     * @param id identificador de la bolsa.
     * @return lista de líneas con configuración de fichas.
     * @throws BolsaNoEncontradaException si no existe el ID.
     * @throws IOException
     */
    public List<String> obtenerBolsa(String id) throws BolsaNoEncontradaException, IOException {
        return ctrlPersistencia.obtenerBolsa(id);
    }
}
