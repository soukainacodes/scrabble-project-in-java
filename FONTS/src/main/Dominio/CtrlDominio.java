package Dominio;

import java.io.IOException;
import java.util.LinkedHashMap;
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

    
    public void iniciarSesion(String nombre, String password) 
            throws UsuarioNoEncontradoException, PasswordInvalidaException
    {
        // First check if user exists
        if (!ctrlPersistencia.existeJugador(nombre)) {
            throw new UsuarioNoEncontradoException(nombre);
        }
        
        // Then check if there's already a session
        if (ctrlJugador.haySesion()) {
            throw new IllegalStateException("Ya hay una sesión activa");
        }
        
        // Finally validate password
        if (!ctrlPersistencia.verificarContrasena(nombre, password)) {
            throw new PasswordInvalidaException();
        }
        
        // If all checks pass, set the active user
        ctrlJugador.setJugadorActual(nombre);
    }


    public void cerrarSesion() {
        ctrlJugador.clearSesion();
    }

    
    public boolean haySesion() {
        return ctrlJugador.haySesion();
    }

    
    public String getUsuarioActual() {
        if (ctrlJugador.haySesion())  return ctrlJugador.getJugadorActual();
        
        return null;
    }

    
    public int getPuntosActual() throws UsuarioNoEncontradoException {
        if (ctrlJugador.haySesion()) {
            return ctrlPersistencia.obtenerPuntuacion(ctrlJugador.getJugadorActual());
        }
        return 0;
    }

    
    public void cambiarPassword(String antigua, String nueva) throws UsuarioNoEncontradoException{
        if (ctrlJugador.haySesion()) 
            ctrlPersistencia.actualizarContrasena(ctrlJugador.getJugadorActual(), antigua, nueva);
    }
    

    public void cambiarNombre(String nuevoNombre, String password) throws UsuarioNoEncontradoException, IOException, UsuarioYaRegistradoException {
        if (ctrlJugador.haySesion() && ctrlPersistencia.verificarContrasena(ctrlJugador.getJugadorActual(), password)) {
            ctrlPersistencia.actualizarNombre(ctrlJugador.getJugadorActual(), nuevoNombre);
            ctrlJugador.setJugadorActual(nuevoNombre);
        }
    }

    public void eliminarUsuario(String password) 
        throws UsuarioNoEncontradoException, IOException   {
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

    
    public void iniciarPartida(
            String id,
            String n2,
            String idDiccionario,
            long seed,
            boolean jugadorAlgoritmo)
            throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, UsuarioNoEncontradoException,
            PasswordInvalidaException, IOException {

        List<String> dic = ctrlPersistencia.obtenerDiccionario(idDiccionario);
        List<String> bolsa = ctrlPersistencia.obtenerBolsa(idDiccionario);

        if (n2 == null || n2.isEmpty()) {
            ctrlJugador.setSegundoJugador("propAI");
            ctrlPartida.crearPartida(0, id, dic, bolsa, seed, jugadorAlgoritmo);

        }
        else 
            ctrlPartida.crearPartida(1, id, dic, bolsa, seed, jugadorAlgoritmo);
        
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
     * @throws PartidaYaExistenteException 
     */
    public int jugarScrabble(int modo, String id)
            throws PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException,
            UsuarioNoEncontradoException, PartidaYaExistenteException {

        int fin = ctrlPartida.jugarScrabble(modo, id);
        
        

        ctrlPersistencia.guardarPartida(
            ctrlJugador.getJugadorActual(), 
            ctrlJugador.getSegundoJugador(), 
            ctrlPartida.getId(), 
            dc.partidaToStringList(
                ctrlPartida.getPartida(), 
                ctrlJugador.getJugadorActual(), 
                ctrlJugador.getSegundoJugador(), 
                ctrlPartida.getId()));


        // Actualiza la puntuación del jugador activo y openente
        if (fin != 0) {
            System.out.println("Fin de partida es " + fin);
            System.out.println("Puntos jugador 2: " + ctrlPartida.getPuntosJugador2());
            ctrlPersistencia.actualizarPuntuacion(ctrlJugador.getJugadorActual(),ctrlPartida.getPuntosJugador1());
            ctrlPersistencia.actualizarPuntuacion(ctrlJugador.getSegundoJugador(),ctrlPartida.getPuntosJugador2());
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
     * Guarda la partida actual en persistencia con un identificador.
     *
     * @param id nombre de la partida a crear.
     * @throws PartidaYaExistenteException  si ya existe una partida con ese id.
     * @throws UsuarioNoEncontradoException
     */
    public void salirPartida(String id) throws UsuarioNoEncontradoException {
        ctrlPersistencia.guardarPartida(ctrlJugador.getJugadorActual(), ctrlJugador.getSegundoJugador(), id,
                dc.partidaToStringList(ctrlPartida.getPartida(), ctrlJugador.getJugadorActual(), ctrlJugador.getSegundoJugador(), id));
        ctrlPersistencia.guardarPartida(ctrlJugador.getSegundoJugador(), ctrlJugador.getSegundoJugador(), id,
                dc.partidaToStringList(ctrlPartida.getPartida(), ctrlJugador.getJugadorActual(), ctrlJugador.getSegundoJugador(), id));

    }

    /**
     * Guarda la partida actual en persistencia con un identificador.
     *
     * @param id nombre de la partida a crear.
     * @throws PartidaYaExistenteException  si ya existe una partida con ese id.
     * @throws UsuarioNoEncontradoException
     */
    public void guardarPartida(String id) throws UsuarioNoEncontradoException {
        ctrlPersistencia.guardarPartida(ctrlJugador.getJugadorActual(), ctrlJugador.getSegundoJugador(), id, dc.partidaToStringList(ctrlPartida.getPartida(), ctrlJugador.getJugadorActual(), ctrlJugador.getSegundoJugador(), id));
    }

    /**
     * Carga una partida guardada desde persistencia.
     *
     * @param id identificador de la partida.
     * @throws PartidaNoEncontradaException si no existe la partida.
     * @throws IOException 
     */
    public void cargarPartida(String id) throws PartidaNoEncontradaException, IOException {
        ctrlPartida.setPartida(dc.stringListToPartida(ctrlPersistencia.cargarPartida(id)), ctrlPersistencia.obtenerDiccionario(ctrlPersistencia.obtenerRecursoPartida(id)), ctrlPersistencia.obtenerBolsa(ctrlPersistencia.obtenerRecursoPartida(id)));
    }

    /**
     * Carga la última partida guardada automáticamente.
     *
     * @throws NoHayPartidaGuardadaException si no hay partidas previas.
     * @throws PartidaNoEncontradaException
     * @throws UsuarioNoEncontradoException
     * @throws IOException 
     */
        public void cargarUltimaPartida() 
            throws NoHayPartidaGuardadaException, PartidaNoEncontradaException, UsuarioNoEncontradoException, IOException {
        // Obtener los datos de la última partida desde persistencia
        String ultimaPartida = ctrlPersistencia.obtenerUltimaPartida(getUsuarioActual());
        
        if (ultimaPartida == null) {
            throw new NoHayPartidaGuardadaException();
        }
        
        List<String> datosUltimaPartida = ctrlPersistencia.cargarPartida(ultimaPartida);
        ctrlPartida.setPartida(dc.stringListToPartida(datosUltimaPartida), 
            ctrlPersistencia.obtenerDiccionario(ctrlPersistencia.obtenerRecursoPartida(ultimaPartida)), 
            ctrlPersistencia.obtenerBolsa(ctrlPersistencia.obtenerRecursoPartida(ultimaPartida)));
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


    public void crearRecurso(String id , List<String> diccionario, Map<String, int[]> bolsa)
            throws IOException, RecursoExistenteException, FormatoDiccionarioInvalidoException, FormatoBolsaInvalidoException {
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
     * @throws IOException                      si falla la modificación de archivos.
     * @throws BolsaNoEncontradaException       si no existe la bolsa.
     * @throws DiccionarioNoEncontradoException 
     * @throws FormatoDiccionarioInvalidoException 
     * @throws FormatoBolsaInvalidoException 
     */
    public void modificarRecurso(String id, List<String> diccionario, Map<String, int[]> bolsa)
            throws IOException, RecursoNoExistenteException,
            BolsaNoEncontradaException, DiccionarioNoEncontradoException, FormatoDiccionarioInvalidoException, FormatoBolsaInvalidoException {
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
     * @throws FormatoBolsaInvalidoException 
     */
    public Map<String, int[]> obtenerBolsa(String id) throws BolsaNoEncontradaException, IOException {
        // Get the raw list of strings from persistence
        List<String> bolsaList = ctrlPersistencia.obtenerBolsa(id);
        
        // Convert to a map format
        Map<String, int[]> bolsaMap = new LinkedHashMap<>();
        for (String linea : bolsaList) {
            String[] partes = linea.split("\\s+");
            if (partes.length >= 3) {
                bolsaMap.put(partes[0], new int[] {
                    Integer.parseInt(partes[1]),
                    Integer.parseInt(partes[2])
                });
            }
        }
        
        return bolsaMap;
    }

    /**
     * Valida que un diccionario cumpla con el formato requerido:
     * - Cada línea contiene una palabra
     * - Todas las palabras están en mayúsculas
     * - No contiene acentos
     * - Está ordenado alfabéticamente
     * 
     * @param diccionario Lista de palabras a validar
     * @throws FormatoDiccionarioInvalidoException si el diccionario no cumple con algún requisito
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


    void validarFormatoBolsa(Map<String, int[]> bolsa)
        throws FormatoBolsaInvalidoException {
        if (bolsa == null || bolsa.isEmpty()) {
            throw new FormatoBolsaInvalidoException();
        }
        
        for (Map.Entry<String, int[]> entry : bolsa.entrySet()) {
            String letra = entry.getKey();
            int[] valores = entry.getValue();
            
            // Verifica si la letra es nula o vacía
            if (letra == null || letra.trim().isEmpty()) {
                throw new FormatoBolsaInvalidoException();
            }
            
            // Verifica si la letra está en mayúsculas
            if (!letra.equals(letra.toUpperCase())) {
                throw new FormatoBolsaInvalidoException();
            }
            
            // Verifica si los valores son válidos
            if (valores == null || valores.length != 2 || valores[0] < 0 || valores[1] < 0) {
                throw new FormatoBolsaInvalidoException();
            }
        }
    }




}
