package Dominio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import Dominio.Excepciones.BolsaNoEncontradaException;
import Dominio.Excepciones.BolsaYaExistenteException;
import Dominio.Excepciones.ComandoInvalidoException;
import Dominio.Excepciones.DiccionarioNoEncontradoException;
import Dominio.Excepciones.DiccionarioYaExistenteException;
import Dominio.Excepciones.NoHayPartidaGuardadaException;
import Dominio.Excepciones.PalabraInvalidaException;
import Dominio.Excepciones.PartidaNoEncontradaException;
import Dominio.Excepciones.PartidaYaExistenteException;
import Dominio.Excepciones.PasswordInvalidaException;
import Dominio.Excepciones.PuntuacionInvalidaException;
import Dominio.Excepciones.RankingVacioException;
import Dominio.Excepciones.UsuarioNoEncontradoException;
import Dominio.Excepciones.UsuarioYaRegistradoException;
import Dominio.Modelos.Partida;
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
   
    /** Controlador de persistencia para acceso a datos (jugadores, diccionarios, partidas, etc.). */
    private final CtrlPersistencia ctrlPersistencia;
    /** Controlador de la sesión y datos del jugador activo. */
    private final CtrlJugador ctrlJugador;

    /** Controlador de la partida en curso. */
    private final CtrlPartida ctrlPartida;




    //PRUEBA DE OBSERVER!!!
    private final List<Observer> observers = new ArrayList<>();

    private final DataConverter dc;

    /**
     * Construye una instancia de CtrlDominio, inicializando los controladores
     * de persistencia, jugador y partida.
     */
    public CtrlDominio() {
        this.ctrlPersistencia = new CtrlPersistencia();
        this.ctrlJugador      = new CtrlJugador();
        this.ctrlPartida      = new CtrlPartida();
        this.dc = new DataConverter();
    }

    // ─── Gestión de Jugadores ───────────────────────────────────────────────────

    /**
     * Registra un nuevo jugador en el sistema y establece su sesión.
     *
     * @param nombre   nombre único de usuario.
     * @param password contraseña del usuario.
     * @throws UsuarioYaRegistradoException si el nombre ya existe en persistencia.
     */
    public void registrarJugador(String nombre, String password)
        throws UsuarioYaRegistradoException, UsuarioNoEncontradoException, PasswordInvalidaException {

        // Verificar si el jugador ya existe en persistencia
        if (ctrlPersistencia.existeJugador(nombre)) {
            throw new UsuarioYaRegistradoException(nombre);
        }

        // Registrar el jugador en persistencia
        ctrlPersistencia.addJugador(nombre, password);

        // Establecer la sesión del jugador
        ctrlJugador.setJugador(nombre, password);
    }
    
    /**
     * Inicia sesión con credenciales existentes.
     *
     * @param nombre   nombre de usuario registrado.
     * @param password contraseña asociada.
     * @throws UsuarioNoEncontradoException  si no existe el usuario.
     * @throws PasswordInvalidaException     si la contraseña no coincide.
     */
    public void iniciarSesion(String nombre, String password)
            throws UsuarioNoEncontradoException, PasswordInvalidaException {
       
        ctrlJugador.setJugador(ctrlPersistencia.getJugador(nombre), password);
    }

    /**
     * Cierra la sesión activa del jugador.
     */
    public void cerrarSesion() {
        ctrlJugador.clearSesion();
    }

    /**
     * Indica si hay un jugador con sesión iniciada.
     *
     * @return {@code true} si hay sesión activa, {@code false} en caso contrario.
     */
    public boolean haySesion() {
        return ctrlJugador.haySesion();
    }

    /**
     * Obtiene el nombre del jugador actualmente en sesión.
     *
     * @return nombre de usuario activo, o {@code null} si no hay sesión.
     */
    public String getUsuarioActual() {
        if(ctrlJugador.haySesion()){
             return ctrlJugador.getJugadorActual().getNombre();
        }
       return null;
    }

    /**
     * Recupera la puntuación acumulada del jugador activo.
     *
     * @return puntos del jugador, o 0 si no hay sesión.
     */
    public int getPuntosActual() {
        
        if(ctrlJugador.haySesion()){
             return ctrlJugador.getJugadorActual().getPuntos();
        }
       return 0;
    }

    /**
     * Cambia la contraseña del jugador activo y actualiza en persistencia.
     *
     * @param antigua contraseña actual para validar.
     * @param nueva   nueva contraseña a asignar.
     * @throws PasswordInvalidaException     si la antigua no es correcta.
     * @throws UsuarioNoEncontradoException  si no hay jugador activo.
     */
    public void cambiarPassword(String antigua, String nueva)
            throws PasswordInvalidaException, UsuarioNoEncontradoException {
            ctrlJugador.cambiarPassword(antigua, nueva);
     
    }

    /**
     * Elimina la cuenta del jugador activo y borra datos en persistencia.
     *
     * @param password contraseña para verificar identidad.
     * @throws PasswordInvalidaException     si la contraseña no coincide.
     * @throws UsuarioNoEncontradoException  si no hay jugador activo.
     */
    public void eliminarUsuario(String password)
            throws PasswordInvalidaException, UsuarioNoEncontradoException {
     
        if (ctrlJugador.haySesion()) {
            ctrlPersistencia.removeJugador(ctrlJugador.getJugadorActual().getNombre());
        }
    }

    // ─── Ranking ────────────────────────────────────────────────────────────────

    /**
     * Obtiene el ranking global de jugadores ordenado por puntuación descendente.
     *
     * @return lista de pares (nombre, puntuación) ordenada.
     * @throws RankingVacioException si no hay datos de ranking.
     */
    public List<Map.Entry<String,Integer>> obtenerRanking()
            throws RankingVacioException {
        return ctrlPersistencia.obtenerRanking();
    }

    /**
     * Obtiene la posición en ranking de un jugador dado.
     *
     * @param nombre nombre de usuario.
     * @return posición (1-based) en el ranking.
     * @throws UsuarioNoEncontradoException si el jugador no figura.
     */
    public int getPosition(String nombre)
            throws UsuarioNoEncontradoException {
        try {
            return ctrlPersistencia.getPosition(nombre);
        } catch (NoSuchElementException e) {
            throw new UsuarioNoEncontradoException(nombre);
        }
    }


    /**
     * Recupera la posición en ranking del usuario actualmente en sesión.
     *
     * @return posición (1-based), o -1 si no hay sesión o no figura.
     */
    public int getPosicionActual() {
        String nombre = getUsuarioActual();
        if (nombre == null) return -1;
        try {
            return getPosition(nombre);
        } catch (UsuarioNoEncontradoException e) {
            return -1;
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
     */
    public void iniciarPartida(int modo,
                               String n2,
                               String idDiccionario,
                               long seed,
                               boolean jugadorAlgoritmo)
            throws DiccionarioNoEncontradoException, BolsaNoEncontradaException,UsuarioNoEncontradoException, PasswordInvalidaException {
       // iniciarSesion(n2,p2);
        List<String> dic = ctrlPersistencia.getDiccionario(idDiccionario);
        List<String> bolsa = ctrlPersistencia.getBolsa(idDiccionario);
        ctrlPartida.crearPartida(modo, Arrays.asList(ctrlJugador.getJugadorActual().getNombre(),n2), dic, bolsa, seed, jugadorAlgoritmo);
    }

    /**
     * Gestiona una jugada de Scrabble delegando en CtrlPartida y actualiza puntos.
     *
     * @param modo   código de acción de Scrabble.
     * @param jugada parámetros de jugada.
     * @return valor de fin de turno o fin de partida.
     * @throws PuntuacionInvalidaException si la puntuación es inválida.
     * @throws ComandoInvalidoException    si el comando es malformado.
     * @throws PalabraInvalidaException    si la palabra no es válida.
     */
    public int jugarScrabble(int modo, String jugada)
            throws PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException {
        int fin = ctrlPartida.jugarScrabble(modo, jugada);
        ctrlJugador.actualizarPuntuacion(ctrlPartida.getPuntosJugador1());
        ctrlPersistencia.reportarPuntuacion(ctrlJugador.getJugadorActual().getNombre(), ctrlJugador.getJugadorActual().getPuntos());
        
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
     * @throws PartidaYaExistenteException si ya existe una partida con ese id.
     */
    public void guardarPartida(String id) throws PartidaYaExistenteException {
        ctrlPersistencia.guardarPartida(id, dc.partidaToStringList(ctrlPartida.guardarPartida(), id));
    }

    /**
     * Carga una partida guardada desde persistencia.
     *
     * @param id identificador de la partida.
     * @throws PartidaNoEncontradaException si no existe la partida.
     */
    public void cargarPartida(String id) throws PartidaNoEncontradaException {
      //  Partida p = ctrlPersistencia.cargarPartida(id);
      //  ctrlPartida.cargarPartida(dc.stringListToPartida(ctrlPersistencia.cargarPartida(id)));
    }

    /**
     * Carga la última partida guardada automáticamente.
     *
     * @throws NoHayPartidaGuardadaException si no hay partidas previas.
     */
    public void cargarUltimaPartida() throws NoHayPartidaGuardadaException {
        // Obtener los datos de la última partida desde persistencia
        List<String> datosUltimaPartida = ctrlPersistencia.cargarUltimaPartida();

        // Convertir los datos simples a una estructura manejable por CtrlPartida
        // ctrlPartida.cargarPartida(dc.stringListToPartida(datosUltimaPartida));
    }

    /**
     * Lista los identificadores de partidas guardadas.
     *
     * @return conjunto de nombres de partidas disponibles.
     */
    public Set<String> obtenerNombresPartidasGuardadas() {
        return ctrlPersistencia.getListaPartidas().keySet();
    }

    /**
     * Elimina una partida guardada del sistema.
     *
     * @param id identificador de la partida a eliminar.
     * @throws PartidaNoEncontradaException si no existe la partida.
     */
    public void eliminarPartidaGuardada(String id) throws PartidaNoEncontradaException {
        ctrlPersistencia.removePartida(id);
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
     * @param col columna de la celda.
     * @return abreviatura de bonificación (DL, TL, DP, TP) o espacios.
     */
    public String getBonusCelda(int fila, int col) {
        return ctrlPartida.obtenerTablero().getBonusCelda(fila, col);
    }
    

    // ─── Gestión de Diccionarios y Bolsas ───────────────────────────────────────

    /**
     * Lista los IDs de diccionarios cargados.
     *
     * @return conjunto de IDs disponibles.
     */
    public Set<String> obtenerIDsDiccionarios() {
        return ctrlPersistencia.getDiccionarioIDs();
    }

    /**
     * Lista los IDs de bolsas cargadas.
     *
     * @return conjunto de IDs disponibles.
     */
    public Set<String> obtenerIDsBolsas() {
        return ctrlPersistencia.getBolsaIDs();
    }

    /**
     * Crea un diccionario persistente con lista de palabras.
     *
     * @param id        identificador del idioma.
     * @param palabras  lista de palabras a almacenar.
     * @throws IOException si falla la escritura en disco.
     * @throws DiccionarioYaExistenteException si ya existe el ID.
     */
    public void crearDiccionario(String id, List<String> palabras)
            throws IOException, DiccionarioYaExistenteException {
        ctrlPersistencia.addDiccionario(id, palabras);
    }

    /**
     * Crea una bolsa persistente con configuración de fichas.
     *
     * @param id    identificador del idioma.
     * @param datos mapa de letra->[cantidad,puntuación].
     * @throws IOException si falla la escritura en disco.
     * @throws BolsaYaExistenteException si ya existe el ID.
     */
    public void crearBolsa(String id, Map<String,int[]> datos)
            throws IOException, BolsaYaExistenteException {
        ctrlPersistencia.addBolsa(id, datos);
    }

    /**
     * Elimina completamente un idioma (diccionario+bolsa) de persistencia.
     *
     * @param id identificador del idioma.
     * @throws IOException si falla la eliminación de archivos.
     * @throws DiccionarioNoEncontradoException si no existe el diccionario.
     * @throws BolsaNoEncontradaException       si no existe la bolsa.
     */
    public void eliminarIdiomaCompleto(String id)
            throws IOException, DiccionarioNoEncontradoException, BolsaNoEncontradaException {
        ctrlPersistencia.removeIdiomaCompleto(id);
    }

    /**
     * Recupera la lista de palabras de un diccionario.
     *
     * @param id identificador del diccionario.
     * @return lista de palabras.
     * @throws DiccionarioNoEncontradoException si no existe el ID.
     */
    public List<String> getDiccionario(String id) throws DiccionarioNoEncontradoException {
        try {
            return ctrlPersistencia.getDiccionario(id);
        } catch (DiccionarioNoEncontradoException e) {
            throw new DiccionarioNoEncontradoException();
        }
    }

    /**
     * Recupera una bolsa de fichas para un idioma.
     *
     * @param id identificador de la bolsa.
     * @return lista de líneas con configuración de fichas.
     * @throws BolsaNoEncontradaException si no existe el ID.
     */
    public List<String> getBolsa(String id) throws BolsaNoEncontradaException {
        try {
            return ctrlPersistencia.getBolsa(id);
        } catch (BolsaNoEncontradaException e) {
            throw new BolsaNoEncontradaException();
        }
    }
}
