package Dominio;

import Dominio.Excepciones.*;
import Persistencia.CtrlPersistencia;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Controlador principal del dominio del juego Scrabble.
 * <p>
 * Esta clase gestiona la lógica del juego, incluyendo la gestión de jugadores,
 * partidas, recursos (diccionarios y bolsas), y la interacción con la capa de
 * persistencia.
 * </p>
 */
public class CtrlDominio {

    /**
     * Controlador de persistencia para acceso a datos (jugadores, partidas y
     * recursos).
     */
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

    // ──────────────────────────────────────────────────────────────────────────
    // ───────────────────────────── Jugador y sesión ───────────────────────────
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Registra un nuevo jugador en el sistema.
     *
     * @param nombre   nombre del jugador.
     * @param password contraseña del jugador.
     * @throws IOException                  si falla la escritura en disco.
     * @throws UsuarioYaRegistradoException si el usuario ya existe.
     */
    public void registrarJugador(String nombre, String password) throws IOException, UsuarioYaRegistradoException {
        ctrlPersistencia.anadirJugador(nombre, password);
    }

    /**
     * Inicia sesión de un jugador en el sistema.
     *
     * @param nombre   nombre del jugador.
     * @param password contraseña del jugador.
     * @throws UsuarioNoEncontradoException   si el jugador no existe.
     * @throws PasswordInvalidaException      si la contraseña es incorrecta.
     * @throws InicioSesionIncorrectoException si se intenta iniciar sesión como
     *                                        'propAI'.
     */
    public void iniciarSesion(String nombre, String password)
            throws UsuarioNoEncontradoException, PasswordInvalidaException, InicioSesionIncorrectoException {

        if ("propAI".equals(nombre)) {
            throw new InicioSesionIncorrectoException("No se puede iniciar sesión como 'propAI'");
        }

        if (!ctrlPersistencia.existeJugador(nombre)) {
            throw new UsuarioNoEncontradoException(nombre);
        }

        if (ctrlJugador.haySesion()) {
            throw new IllegalStateException("Ya hay una sesión activa");
        }
        if (!ctrlPersistencia.verificarContrasena(nombre, password)) {
            throw new PasswordInvalidaException();
        }

        // If all checks pass, set the active user
        ctrlJugador.setJugadorActual(nombre);
    }

    /**
     * Cierra la sesión del jugador activo.
     * <p>
     * Esta operación no requiere parámetros y no devuelve ningún valor.
     * </p>
     */
    public void cerrarSesion() {
        ctrlJugador.clearSesion();
        ctrlPartida.clearPartida();
    }

    /**
     * Verifica si hay una sesión activa.
     *
     * @return true si hay sesión activa, false en caso contrario.
     */
    public boolean haySesion() {
        return ctrlJugador.haySesion();
    }

    /**
     * Recupera el nombre del jugador activo.
     *
     * @return nombre del jugador activo o null si no hay sesión.
     */
    public String getUsuarioActual() {
        if (ctrlJugador.haySesion())
            return ctrlJugador.getJugadorActual();

        return null;
    }

    /**
     * Recupera el nombre del segundo jugador.
     *
     * @return nombre del segundo jugador o null si no hay sesión.
     */
    public String getSegundoJugador() {
        if (ctrlJugador.haySesion())
            return ctrlJugador.getSegundoJugador();

        return null;
    }

   

   
    /**
     * Recupera los puntos actuales del jugador activo.
     * 
     * @return puntos actuales del jugador activo.
     * @exception UsuarioNoEncontradoException si el jugador no existe.
     */
    public int getPuntosActual() throws UsuarioNoEncontradoException {
        if (ctrlJugador.haySesion()) {
            return ctrlPersistencia.obtenerPuntuacion(ctrlJugador.getJugadorActual());
        }
        return 0;
    }

    /**
     * Inicia sesión del segundo jugador.
     *
     * @param segundoJugador nombre del segundo jugador.
     * @param password       contraseña del segundo jugador.
     * @throws UsuarioNoEncontradoException si el jugador no existe.
     * @throws PasswordInvalidaException    si la contraseña es incorrecta.
     * @throws InicioSesionIncorrectoException si se intenta iniciar sesión como el mismo jugador.
     */
    public void iniciarSesionSegundoJugador(String segundoJugador, String password)
            throws UsuarioNoEncontradoException, PasswordInvalidaException, InicioSesionIncorrectoException {

        if(segundoJugador.equals(ctrlJugador.getJugadorActual())) {
            throw new InicioSesionIncorrectoException("No se puede iniciar sesión como el mismo jugador");
        }
        
        if (!ctrlPersistencia.existeJugador(segundoJugador)) {
            throw new UsuarioNoEncontradoException(segundoJugador);
        }

        if (!ctrlPersistencia.verificarContrasena(segundoJugador, password)) {
            throw new PasswordInvalidaException();
        }
        // Si todos los chequeos pasan, se establece el segundo jugador
        ctrlJugador.setSegundoJugador(segundoJugador);
    }

    /**
     * Cambia la contraseña del jugador activo.
     *
     * @param antigua contraseña actual.
     * @param nueva   nueva contraseña.
     * @throws UsuarioNoEncontradoException si el jugador no existe.
     */
    public void cambiarPassword(String antigua, String nueva) throws UsuarioNoEncontradoException {
        if (ctrlJugador.haySesion())
            ctrlPersistencia.actualizarContrasena(ctrlJugador.getJugadorActual(), antigua, nueva);
    }

    /**
     * Cambia el nombre del jugador activo.
     *
     * @param nuevoNombre nuevo nombre de usuario.
     * @param password    contraseña actual.
     * @throws UsuarioNoEncontradoException si el jugador no existe.
     * @throws IOException                  si falla la escritura en disco.
     * @throws UsuarioYaRegistradoException si el nuevo nombre ya está en uso.
     * @throws PasswordInvalidaException      si la contraseña es incorrecta.
     */
    public void cambiarNombre(String nuevoNombre, String password)
            throws UsuarioNoEncontradoException, IOException, UsuarioYaRegistradoException, PasswordInvalidaException {
        if (ctrlJugador.haySesion()) {
            if (!ctrlPersistencia.verificarContrasena(ctrlJugador.getJugadorActual(), password)) {
                throw new PasswordInvalidaException();
            }
            ctrlPersistencia.actualizarNombre(ctrlJugador.getJugadorActual(), nuevoNombre);
            ctrlJugador.setJugadorActual(nuevoNombre);
        }
    }
    /**
     * Elimina el usuario activo del sistema.
     * 
     * @param password contraseña del usuario.
     * @throws UsuarioNoEncontradoException si el jugador no existe.
     * @throws IOException                si falla la lectura en disco.
     */
    public void eliminarUsuario(String password)
            throws UsuarioNoEncontradoException, IOException {
        if (ctrlJugador.haySesion() && ctrlPersistencia.verificarContrasena(ctrlJugador.getJugadorActual(), password)) {
            ctrlPersistencia.eliminarJugador(ctrlJugador.getJugadorActual());
            ctrlJugador.clearSesion();
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ───────────────────────────── Ranking ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Obtiene el ranking global de jugadores ordenado por puntuación descendente.
     *
     * @return lista de pares (nombre, puntuación) ordenada.
     * @throws RankingVacioException si no hay datos de ranking.
     * @throws IOException           si falla la lectura de archivos.
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
     * @throws IOException              si falla la lectura de archivos.
     */
    public int obtenerPosicion(String nombre)
            throws UsuarioNoEncontradoException, IOException {
        try {
            return ctrlPersistencia.obtenerPosicion(nombre);
        } catch (NoSuchElementException e) {
            throw new UsuarioNoEncontradoException(nombre);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ───────────────────────────── Partida ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Inicia una nueva partida.
     *
     * @param id             identificador de la partida.
     * @param segundoJugador nombre del segundo jugador (puede ser null).
     * @param idDiccionario  identificador del diccionario.
     * @param seed           semilla para el generador de números aleatorios.
     * @param jugadorAlgoritmo indica si el segundo jugador es un algoritmo.
     * @throws DiccionarioNoEncontradoException si no existe el diccionario.
     * @throws BolsaNoEncontradaException      si no existe la bolsa.
     * @throws UsuarioNoEncontradoException    si el usuario no existe.
     * @throws PasswordInvalidaException       si la contraseña es incorrecta.
     * @throws IOException                     si falla la escritura en disco.
     * @throws UltimaPartidaNoExistenteException si no hay una última partida guardada.
     * @throws PartidaYaExistenteException   si ya existe una partida con ese ID.
     */
    public void iniciarPartida(
            String id,
            String segundoJugador,
            String idDiccionario,
            long seed,
            boolean jugadorAlgoritmo)
            throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, UsuarioNoEncontradoException,
            PasswordInvalidaException, IOException, UltimaPartidaNoExistenteException, PartidaYaExistenteException {

        if (ctrlPersistencia.existePartida(id)) {
            throw new PartidaYaExistenteException(id);
        }

        List<String> dic = ctrlPersistencia.obtenerDiccionario(idDiccionario);
        List<String> bolsa = ctrlPersistencia.obtenerBolsa(idDiccionario);

        int modo;
        if (segundoJugador == null || segundoJugador.isEmpty()) {
            ctrlJugador.setSegundoJugador("propAI");
            modo = 0;
        } else {
            modo = 1;
            // Tranquilo, el segundo jugador no se setea aquí pero
            // iniciarSesionSegundoJugado
        }
        ctrlPartida.crearPartida(modo, id, dic, bolsa, seed, jugadorAlgoritmo);
        ctrlPartida.setRecursoPartida(idDiccionario, dic, bolsa);
        ctrlPersistencia.guardarPartida(
                ctrlJugador.getJugadorActual(),
                ctrlJugador.getSegundoJugador(),
                ctrlPartida.getId(),
                dc.partidaToStringList(
                        ctrlPartida.getPartida(),
                        ctrlJugador.getJugadorActual(),
                        ctrlJugador.getSegundoJugador(),
                        ctrlPartida.getId()));

        ctrlPersistencia.actualizarUltimaPartida(ctrlJugador.getJugadorActual(), ctrlPartida.getId());
        ctrlPersistencia.actualizarUltimaPartida(ctrlJugador.getSegundoJugador(), ctrlPartida.getId());
    }

    /**
     * Realiza un turno en la partida actual.
     *
     * @param modo modo de juego (0: IA, 1: humano).
     * @param id   identificador de la partida.
     * @return 0 si el turno ha terminado, 1 si el jugador ha ganado, -1 si el
     *         jugador ha perdido.
     * @throws PuntuacionInvalidaException    si la puntuación es inválida.
     * @throws ComandoInvalidoException       si el comando es inválido.
     * @throws PalabraInvalidaException       si la palabra es inválida.
     * @throws UsuarioNoEncontradoException   si el usuario no existe.
     * @throws PartidaYaExistenteException    si ya existe una partida con ese id.
     * @throws UltimaPartidaNoExistenteException si no hay una última partida guardada.
     */
    public int jugarScrabble(int modo, String id)
            throws PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException,
            UsuarioNoEncontradoException, PartidaYaExistenteException, UltimaPartidaNoExistenteException {

        int fin = ctrlPartida.jugarScrabble(modo, id);
    if(modo != 1 && modo != 2 && modo != 7){ 
        System.out.println("Fin de turno: " + fin);

        // Actualiza la puntuación del jugador activo y openente
        if (fin != 0) {

            System.out.println("Entrando en el if de fin");
            ctrlPersistencia.actualizarUltimaPartida(ctrlJugador.getJugadorActual(), null);
            System.out.println("El jugador actual es: " + ctrlJugador.getJugadorActual());
            System.out.println("El segundo jugador es: " + ctrlJugador.getSegundoJugador());
            ctrlPersistencia.actualizarUltimaPartida(ctrlJugador.getSegundoJugador(), null);
            
        } else {
            System.out.println("Entrando en el else de fin");
            ctrlPersistencia.actualizarUltimaPartida(ctrlJugador.getJugadorActual(), ctrlPartida.getId());
            ctrlPersistencia.actualizarUltimaPartida(ctrlJugador.getSegundoJugador(), ctrlPartida.getId());
            
        }

        ctrlPersistencia.guardarPartida(
                ctrlJugador.getJugadorActual(),
                ctrlJugador.getSegundoJugador(),
                ctrlPartida.getId(),
                dc.partidaToStringList(
                        ctrlPartida.getPartida(),
                        ctrlJugador.getJugadorActual(),
                        ctrlJugador.getSegundoJugador(),
                        ctrlPartida.getId()));

        ctrlPersistencia.actualizarPuntuacion(ctrlJugador.getJugadorActual(), ctrlPartida.getPuntosJugador1());
        ctrlPersistencia.actualizarPuntuacion(ctrlJugador.getSegundoJugador(), ctrlPartida.getPuntosJugador2());
      if(fin != 0)  salirPartida();
    }
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
     * Finaliza la partida actual y guarda los datos en persistencia.       
     * @throws UsuarioNoEncontradoException si el jugador no existe.
     */
    public void salirPartida() throws UsuarioNoEncontradoException {
        ctrlPersistencia.guardarPartida(ctrlJugador.getJugadorActual(), ctrlJugador.getSegundoJugador(), ctrlPartida.getId(),
                dc.partidaToStringList(ctrlPartida.getPartida(), ctrlJugador.getJugadorActual(),
                        ctrlJugador.getSegundoJugador(), ctrlPartida.getId()));
        ctrlPersistencia.guardarPartida(ctrlJugador.getSegundoJugador(), ctrlJugador.getSegundoJugador(), ctrlPartida.getId(),
                dc.partidaToStringList(ctrlPartida.getPartida(), ctrlJugador.getJugadorActual(),
                        ctrlJugador.getSegundoJugador(), ctrlPartida.getId()));
        ctrlJugador.setSegundoJugador(null);
    }



    /**
     * Carga una partida guardada desde persistencia.
     *
     * @param id identificador de la partida.
     * @throws PartidaNoEncontradaException si no existe la partida.
     * @throws IOException                  si falla la lectura de archivos.
     * @throws UsuarioNoEncontradoException si el jugador no existe.
     */
    public void cargarPartida(String id)
            throws PartidaNoEncontradaException, IOException, UsuarioNoEncontradoException {
        if (!ctrlPersistencia.existePartida(id)) {
            throw new PartidaNoEncontradaException(id);
        }

        ctrlPartida.setPartida(dc.stringListToPartida(ctrlPersistencia.cargarPartida(id)),
                ctrlPersistencia.obtenerDiccionario(ctrlPersistencia.obtenerRecursoPartida(id)),
                ctrlPersistencia.obtenerBolsa(ctrlPersistencia.obtenerRecursoPartida(id)));
        ctrlJugador.setJugadorActual(ctrlPersistencia.obtenerJugadorActual(id));
        ctrlJugador.setSegundoJugador(ctrlPersistencia.obtenerSegundoJugador(id));
        if ("propAI".equals(ctrlJugador.getSegundoJugador())) {
            ctrlPartida.activarAlgoritmo();
        }
    }

    /**
     * Carga la última partida guardada automáticamente.
     *
     * @throws NoHayPartidaGuardadaException     si no hay partidas previas.
     * @throws PartidaNoEncontradaException   si no existe la partida.
     * @throws UsuarioNoEncontradoException   si el jugador no existe.
     * @throws IOException                     si falla la lectura de archivos.
     * @throws UltimaPartidaNoExistenteException si no hay una última partida guardada.
     */
    public void cargarUltimaPartida()
            throws NoHayPartidaGuardadaException, PartidaNoEncontradaException, UsuarioNoEncontradoException,
            IOException, UltimaPartidaNoExistenteException {
        // Obtener los datos de la última partida desde persistencia
        String ultimaPartida;
        ultimaPartida = ctrlPersistencia.obtenerUltimaPartida(getUsuarioActual());
        if (ultimaPartida == null || ultimaPartida.isEmpty()) {
            throw new NoHayPartidaGuardadaException();
        }

        System.out.println("ID de la última partida: " + ultimaPartida);

        List<String> datosUltimaPartida = ctrlPersistencia.cargarPartida(ultimaPartida);
        System.out.println("Datos de la última partida: " + datosUltimaPartida);
        ctrlPartida.setPartida(dc.stringListToPartida(datosUltimaPartida),
                ctrlPersistencia.obtenerDiccionario(ctrlPersistencia.obtenerRecursoPartida(ultimaPartida)),
                ctrlPersistencia.obtenerBolsa(ctrlPersistencia.obtenerRecursoPartida(ultimaPartida)));
        ctrlJugador.setSegundoJugador(datosUltimaPartida.get(5));
    }

    /**
     * Lista los identificadores de partidas guardadas.
     *
     * @return conjunto de nombres de partidas disponibles.
     */
    public List<String> obtenerNombresPartidasGuardadas() {
        return ctrlPersistencia.listarPartidasNoAcabadas(ctrlJugador.getJugadorActual());
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

    /**
     * Recupera la imagen del tablero actual.
     * @return imagen del tablero.
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
     * Obtiene el valor de una celda en la presentación.
     *
     * @param fila índice de fila.
     * @param col  índice de columna.
     * @return valor de la celda o -1 si vacía.
     */
    public int getPuntuacionCelda(int fila, int col) {
        return ctrlPartida.obtenerTablero().getPuntuacionCelda(fila, col);
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

    // ────────────────────────────────────────────────────────────────────────
    // ───────────────────────────── Recursos ─────────────────────────────────
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Recupera la lista de recursos (diccionarios y bolsas) disponibles.
     *
     * @return lista de identificadores de recursos.
     */
    public List<String> obtenerRecursos() {
        return ctrlPersistencia.listarRecursos();
    }

    /**
     * Crea un nuevo recurso (diccionario+bolsa) en persistencia.
     *
     * @param id        identificador del idioma.
     * @param diccionario lista de palabras del diccionario.
     * @param bolsa      lista de letras y frecuencias de la bolsa.
     * @throws IOException                         si falla la escritura en disco.
     * @throws RecursoExistenteException           si el recurso ya existe.
     * @throws FormatoDiccionarioInvalidoException si el formato del diccionario es
     *                                             inválido.
     * @throws FormatoBolsaInvalidoException       si el formato de la bolsa es
     *                                             inválido.
     */
    public void crearRecurso(String id, List<String> diccionario, List<String> bolsa)
            throws IOException, RecursoExistenteException, FormatoDiccionarioInvalidoException,
            FormatoBolsaInvalidoException {
        if (ctrlPersistencia.existeRecurso(id)) {
            throw new RecursoExistenteException(id);
        }

        validarFormatoBolsa(bolsa);
        validarFormatoDiccionario(diccionario);
        ctrlPersistencia.crearRecurso(id, diccionario, bolsa);

    }

    /**
     * Modifica un recurso existente (diccionario+bolsa) en persistencia.
     *
     * @param id identificador del idioma.
     * @param diccionario lista de palabras del diccionario.
     * @param bolsa      lista de letras y frecuencias de la bolsa.
     * @throws IOException                         si falla la modificación de archivos.
     * @throws BolsaNoEncontradaException          si no existe la bolsa.
     * @throws DiccionarioNoEncontradoException    si no existe el diccionario.
     * @throws FormatoDiccionarioInvalidoException si el formato del diccionario es invalido.
     * @throws FormatoBolsaInvalidoException      si el formato de la bolsa es invalido.
     * @throws RecursoNoExistenteException         si el recurso no existe.
     */
    public void modificarRecurso(String id, List<String> diccionario, List<String> bolsa)
            throws IOException, RecursoNoExistenteException,
            BolsaNoEncontradaException, DiccionarioNoEncontradoException, FormatoDiccionarioInvalidoException,
            FormatoBolsaInvalidoException {
        validarFormatoBolsa(bolsa);
        validarFormatoDiccionario(diccionario);
        ctrlPersistencia.modificarRecurso(id, diccionario, bolsa);
    }

    /**
     * Elimina completamente un idioma (diccionario+bolsa) de persistencia.
     *
     * @param id identificador del idioma.
     * @throws IOException                      si falla la eliminación de archivos.
     * @throws BolsaNoEncontradaException       si no existe la bolsa.
     * @throws DiccionarioNoEncontradoException si no existe el diccionario.
     */
    public void eliminarRecurso(String id)
            throws IOException,
            BolsaNoEncontradaException, DiccionarioNoEncontradoException {
        ctrlPersistencia.eliminarRecurso(id);
    }

    /**
     * Recupera la lista de palabras de un diccionario.
     *
     * @param id identificador del diccionario.
     * @return lista de palabras.
     * @throws DiccionarioNoEncontradoException si no existe el ID.
     * @throws IOException               si hay problemas de lectura.
     */
    public List<String> obtenerDiccionario(String id) throws DiccionarioNoEncontradoException, IOException {
        return ctrlPersistencia.obtenerDiccionario(id);
    }

    /**
     * Recupera una bolsa de fichas para un idioma.
     *
     * @param id identificador de la bolsa.
     * @return lista de líneas con configuración de fichas en formato "LETRA FRECUENCIA PUNTOS".
     * @throws BolsaNoEncontradaException si no existe el ID.
     * @throws IOException                si hay problemas de lectura.
     */
    public List<String> obtenerBolsa(String id) throws BolsaNoEncontradaException, IOException {
        // Devolver directamente la lista de strings de la capa de persistencia
        return ctrlPersistencia.obtenerBolsa(id);
    }

    /**
     * Valida que un diccionario cumpla con el formato requerido:
     * - Cada línea contiene una palabra
     * - Todas las palabras están en mayúsculas
     * - No contiene acentos
     * - Está ordenado alfabéticamente
     * 
     * @param diccionario Lista de palabras a validar
     * @throws FormatoDiccionarioInvalidoException si el diccionario no cumple con
     *                                             algún requisito
     */
    public void validarFormatoDiccionario(List<String> diccionario)
            throws FormatoDiccionarioInvalidoException {
        if (diccionario == null || diccionario.isEmpty()) {
            throw new FormatoDiccionarioInvalidoException();
        }

        String palabraAnterior = null;

        for (String palabra : diccionario) {
            // Verifica si la palabra es nula o vacía
            if (palabra == null || palabra.trim().isEmpty()) {
                throw new FormatoDiccionarioInvalidoException();
            }

            // Verifica si la palabra está en mayúsculas
            if (!palabra.equals(palabra.toUpperCase())) {
                throw new FormatoDiccionarioInvalidoException();
            }

            // Verifica si la palabra contiene solo caracteres válidos (sin acentos)
            if (!palabra.matches("^[A-Z]*$")) {
                throw new FormatoDiccionarioInvalidoException();
            }

            // Verifica orden alfabético
            if (palabraAnterior != null && palabra.compareTo(palabraAnterior) <= 0) {
                throw new FormatoDiccionarioInvalidoException();
            }

            palabraAnterior = palabra;
        }
    }

    /**
     * Valida que una bolsa de letras en formato de lista de cadenas cumpla con el
     * formato requerido.
     * Cada cadena debe tener el formato "LETRA FRECUENCIA PUNTOS"
     * 
     * @param bolsa Lista de strings que representa la bolsa de letras
     * @throws FormatoBolsaInvalidoException si el formato de la bolsa no es válido
     */
    void validarFormatoBolsa(List<String> bolsa) throws FormatoBolsaInvalidoException {
        if (bolsa == null || bolsa.isEmpty()) {
            throw new FormatoBolsaInvalidoException();
        }

        for (String linea : bolsa) {
            // Verifica si la línea es nula o vacía
            if (linea == null || linea.trim().isEmpty()) {
                throw new FormatoBolsaInvalidoException();
            }

            // Divide la línea por espacios
            String[] partes = linea.split("\\s+");

            // Verifica que haya al menos 3 partes (letra, frecuencia, puntos)
            if (partes.length < 3) {
                throw new FormatoBolsaInvalidoException();
            }

            String letra = partes[0];

            // Verifica si la letra está en mayúsculas
            if (!letra.equals(letra.toUpperCase())) {
                throw new FormatoBolsaInvalidoException();
            }

            // Intenta analizar la frecuencia y los puntos como enteros
            try {
                int frecuencia = Integer.parseInt(partes[1]);
                int puntos = Integer.parseInt(partes[2]);

                // Verifica que frecuencia y puntos sean no negativos
                if (frecuencia < 0 || puntos < 0) {
                    throw new FormatoBolsaInvalidoException();
                }
            } catch (NumberFormatException e) {
                throw new FormatoBolsaInvalidoException();
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ───────────────────────────── Imagenes ───────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────
    

    /**
     * Guarda la imagen de perfil del jugador activo.
     * 
     * @param image imagen a guardar.
     */
    public void saveProfileImage(BufferedImage image) {
        ctrlPersistencia.guardarImagenPerfil(ctrlJugador.getJugadorActual(), image);
    }

    /**
     * Recupera la imagen de perfil del jugador activo.
     * 
     * @param username nombre del jugador.
     * @return imagen del perfil o null si no existe.
     */
    public BufferedImage getProfileImage(String username) {
        return ctrlPersistencia.obtenerImagenPerfil(username);
    }

    /**
     * Elimina la imagen de perfil del jugador activo.
     *
     * @param username nombre del jugador.
     */
    public void deleteProfileImage(String username) {
        ctrlPersistencia.eliminarImagenPerfil(username);
    }

}
