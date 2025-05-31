package Persistencia;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import Dominio.Excepciones.*;

import Persistencia.Gestores.Utilidades.Utils;

// Importaciones específicas para los gestores
import Persistencia.Gestores.GestorDePartidas;
import Persistencia.Gestores.GestorDeRanking;
import Persistencia.Gestores.GestorDeRecursos;
import Persistencia.Gestores.GestorDeUsuarios;

/**
 * Controlador de persistencia de la aplicación. Gestiona:
 * <ul>
 * <li>Usuarios y su archivo de datos.</li>
 * <li>Ranking de jugadores.</li>
 * <li>Partidas guardadas en memoria.</li>
 * <li>Diccionarios y bolsas de fichas desde recursos.</li>
 * </ul>
 */
public class CtrlPersistencia {

    /**
     * Rutas de los directorios de datos
     */
    private static final String JUGADORES = "FONTS/src/main/Persistencia/Datos/Jugadores/";
    private static final String PARTIDAS = "FONTS/src/main/Persistencia/Datos/Partidas/";
    private static final String RECURSOS = "FONTS/src/main/Persistencia/Datos/Recursos/";
    
    /**
     * Gestores para los diferentes subsistemas
     */
    private final GestorDeUsuarios gestorUsuarios;
    private final GestorDeRanking gestorRanking;
    private final GestorDeRecursos gestorRecursos;
    private final GestorDePartidas gestorPartidas;

    /**
     * Constructor del controlador de persistencia.
     * Inicializa todos los gestores y asegura la existencia de los directorios de datos.
     */
    public CtrlPersistencia() {
        // Crear directorios si no existen
        crearDirectorios();
        
        // Inicializar gestores
        gestorUsuarios = new GestorDeUsuarios();
        gestorRanking = new GestorDeRanking();
        gestorRecursos = new GestorDeRecursos();
        gestorPartidas = new GestorDePartidas(gestorUsuarios, gestorRecursos);
    }
    
    /**
     * Crea los directorios de datos si no existen
     */
    private void crearDirectorios() {
        new File(JUGADORES).mkdirs();
        new File(PARTIDAS).mkdirs();
        new File(RECURSOS).mkdirs();
    }

    // ─── Jugadores ─────────────────────────────────────────────────────────────

    /**
     * Verifica si un jugador existe en el sistema.
     * 
     * @param username Nombre de usuario del jugador
     * @return true si el jugador existe, false en caso contrario
     */
    public boolean existeJugador(String username) {
        return gestorUsuarios.existeJugador(username);
    }

    /**
     * Añade un nuevo jugador al sistema.
     * 
     * @param username Nombre de usuario del jugador
     * @param password Contraseña del jugador
     * @throws IOException                  Si ocurre un error al acceder a los archivos
     * @throws UsuarioYaRegistradoException Si el jugador ya existe
     */
    public void anadirJugador(String username, String password) throws IOException, UsuarioYaRegistradoException {
        gestorUsuarios.anadirJugador(username, password);
    }

    /**
     * Elimina un jugador del sistema.
     * 
     * @param username Nombre de usuario del jugador
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws IOException                  Si ocurre un error al acceder a los archivos
     */
    public void eliminarJugador(String username) throws UsuarioNoEncontradoException, IOException {
        gestorUsuarios.eliminarJugador(username);
    }

    /**
     * Verifica la contraseña de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @param password Contraseña a verificar
     * @return true si la contraseña es correcta, false en caso contrario
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public boolean verificarContrasena(String username, String password) throws UsuarioNoEncontradoException {
        if (!gestorUsuarios.verificarContrasena(username, password)){
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        return gestorUsuarios.verificarContrasena(username, password);
    }

    /**
     * Actualiza el nombre de un jugador.
     * 
     * @param username    Nombre de usuario del jugador
     * @param newUsername Nuevo nombre de usuario
     * @throws UsuarioNoEncontradoException  Si el jugador no existe
     * @throws UsuarioYaRegistradoException  Si el nuevo nombre ya está en uso
     */
    public void actualizarNombre(String username, String newUsername)
            throws UsuarioNoEncontradoException, UsuarioYaRegistradoException {
        gestorUsuarios.actualizarNombre(username, newUsername);
    }

    /**
     * Actualiza la contraseña de un jugador.
     * 
     * @param username   Nombre de usuario del jugador
     * @param currentPass Contraseña actual
     * @param newPass    Nueva contraseña
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public void actualizarContrasena(String username, String currentPass, String newPass)
            throws UsuarioNoEncontradoException {
        gestorUsuarios.actualizarContrasena(username, currentPass, newPass);
    }

    /**
     * Actualiza la puntuación máxima de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @param pts      Nueva puntuación máxima
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws PuntuacionInvalidaException  Si la puntuación es negativa
     */
    public void actualizarPuntuacion(String username, int pts)
            throws UsuarioNoEncontradoException, PuntuacionInvalidaException {
        gestorUsuarios.actualizarPuntuacion(username, pts);
    }

    /**
     * Actualiza la última partida guardada de un jugador.
     * 
     * @param username  Nombre de usuario del jugador
     * @param idPartida ID de la partida a guardar
     * @throws UsuarioNoEncontradoException      Si el jugador no existe
     * @throws UltimaPartidaNoExistenteException Si no hay partida guardada
     */
    public void actualizarUltimaPartida(String username, String idPartida)
            throws UsuarioNoEncontradoException, UltimaPartidaNoExistenteException {
        gestorUsuarios.actualizarUltimaPartida(username, idPartida);
    }

    /**
     * Obtiene la puntuación máxima de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @return Puntuación máxima del jugador
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public int obtenerPuntuacion(String username) throws UsuarioNoEncontradoException {
        return gestorUsuarios.obtenerPuntuacion(username);
    }

    /**
     * Obtiene la última partida guardada de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @return ID de la última partida guardada
     * @throws UsuarioNoEncontradoException      Si el jugador no existe
     * @throws UltimaPartidaNoExistenteException Si no hay partida guardada
     */
    public String obtenerUltimaPartida(String username)
            throws UsuarioNoEncontradoException, UltimaPartidaNoExistenteException {
        return gestorUsuarios.obtenerUltimaPartida(username);
    }

    // ------------------------------ RANKING ----------------------------------

    /**
     * Genera un ranking de jugadores basado en sus puntuaciones.
     * 
     * @return Lista de pares (nombre, puntuación) ordenada por puntuación
     * @throws IOException Si ocurre un error al acceder a los archivos
     */
    public List<Map.Entry<String, Integer>> generarRanking() throws IOException {
        return gestorRanking.generarRanking();
    }

    /**
     * Obtiene la posición de un jugador en el ranking.
     * 
     * @param name Nombre del jugador
     * @return Posición del jugador en el ranking
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws IOException                  Si ocurre un error al acceder a los archivos
     */
    public int obtenerPosicion(String name) throws UsuarioNoEncontradoException, IOException {
        return gestorRanking.obtenerPosicion(name);
    }

    // ------------------------------ PARTIDAS ----------------------------------

    /**
     * Comprueba si una partida existe en el sistema.
     * 
     * @param id ID de la partida
     * @return true si la partida existe, false en caso contrario
     */
    public boolean existePartida(String id) {
        return gestorPartidas.existePartida(id);
    }

    /**
     * Verifica si una partida está acabada.
     * 
     * @param id ID de la partida
     * @return true si la partida está acabada, false en caso contrario
     */
    public boolean esPartidaAcabada(String id) {
        return gestorPartidas.esPartidaAcabada(id);
    }

    /**
     * Obtiene el recurso de una partida.
     * 
     * @param id ID de la partida
     * @return Recurso de la partida
     */
    public String obtenerRecursoPartida(String id) {
        return gestorPartidas.obtenerRecursoPartida(id);
    }

    /**
     * Guarda una partida en el sistema.
     * 
     * @param username        Nombre de usuario del jugador
     * @param segundoJugador  Nombre de usuario del segundo jugador
     * @param id              ID de la partida
     * @param partida         Lista de datos de la partida
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public void guardarPartida(String username, String segundoJugador, String id, List<String> partida)
            throws UsuarioNoEncontradoException {
        gestorPartidas.guardarPartida(username, segundoJugador, id, partida);
    }

    /**
     * Carga una partida del sistema.
     * 
     * @param id ID de la partida a cargar
     * @return Lista de datos de la partida
     * @throws PartidaNoEncontradaException Si la partida no existe
     */
    public List<String> cargarPartida(String id) throws PartidaNoEncontradaException {
        return gestorPartidas.cargarPartida(id);
    }

    /**
     * Elimina una partida del sistema.
     * 
     * @param id ID de la partida a eliminar
     * @throws PartidaNoEncontradaException Si la partida no existe
     */
    public void eliminarPartida(String id) throws PartidaNoEncontradaException {
        gestorPartidas.eliminarPartida(id);
    }

    /**
     * Lista todas las partidas no acabadas del jugador actual.
     * 
     * @param jugadorActual Nombre de usuario del jugador actual
     * @return Lista de IDs de partidas no acabadas
     */
    public List<String> listarPartidasNoAcabadas(String jugadorActual) {
        return gestorPartidas.listarPartidasNoAcabadas(jugadorActual);
    }

    /**
     * Obtiene el jugador actual de una partida.
     * 
     * @param id ID de la partida
     * @return Nombre del jugador actual
     * @throws PartidaNoEncontradaException Si la partida no existe
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public String obtenerJugadorActual(String id) throws PartidaNoEncontradaException, UsuarioNoEncontradoException {
        return gestorPartidas.obtenerJugadorActual(id);
    }

    /**
     * Obtiene el segundo jugador de una partida.
     * 
     * @param id ID de la partida
     * @return Nombre del segundo jugador
     * @throws PartidaNoEncontradaException Si la partida no existe
     * @throws UsuarioNoEncontradoException  Si el usuario no existe
     */
    public String obtenerSegundoJugador(String id) throws PartidaNoEncontradaException, UsuarioNoEncontradoException {
        return gestorPartidas.obtenerSegundoJugador(id);
    }

    // ─── Diccionarios y Bolsas ───────────────────────────────────────────────

    /**
     * Verifica si un recurso existe en el sistema.
     * 
     * @param id ID del recurso
     * @return true si el recurso existe, false en caso contrario
     */
    public boolean existeRecurso(String id) {
        return gestorRecursos.existeRecurso(id);
    }

    /**
     * Lista todos los recursos disponibles en el sistema.
     * 
     * @return Lista de nombres de recursos
     */
    public List<String> listarRecursos() {
        return gestorRecursos.listarRecursos();
    }

    /**
     * Obtiene el diccionario de un recurso.
     * 
     * @param id ID del recurso
     * @return Lista de palabras del diccionario
     * @throws IOException Si ocurre un error de E/S
     */
    public List<String> obtenerDiccionario(String id) throws IOException {
        return gestorRecursos.obtenerDiccionario(id);
    }

    /**
     * Obtiene la bolsa de un recurso.
     * 
     * @param id ID del recurso
     * @return Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException Si ocurre un error de E/S
     */
    public List<String> obtenerBolsa(String id) throws IOException {
        return gestorRecursos.obtenerBolsa(id);
    }

    /**
     * Crea un nuevo recurso en el sistema.
     * 
     * @param id       Identificador único del recurso
     * @param palabras Lista de palabras para el diccionario
     * @param bolsa    Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException               Si ocurre un error de E/S
     * @throws RecursoExistenteException Si el recurso ya existe
     */
    public void crearRecurso(String id, List<String> palabras, List<String> bolsa)
            throws IOException, RecursoExistenteException {
        gestorRecursos.crearRecurso(id, palabras, bolsa);
    }

    /**
     * Crea un nuevo archivo de diccionario para un recurso.
     * 
     * @param id       Identificador del recurso
     * @param palabras Lista de palabras para el diccionario
     * @throws IOException               Si ocurre un error al escribir el archivo
     * @throws RecursoExistenteException Si el recurso ya existe
     */
    public void crearDiccionario(String id, List<String> palabras) throws IOException, RecursoExistenteException {
        gestorRecursos.crearDiccionario(id, palabras);
    }

    /**
     * Crea un nuevo archivo de bolsa para un recurso.
     * 
     * @param id    Identificador del recurso
     * @param bolsa Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void crearBolsa(String id, List<String> bolsa) throws IOException {
        gestorRecursos.crearBolsa(id, bolsa);
    }

    /**
     * Modifica un recurso existente en el sistema.
     * 
     * @param id       Identificador único del recurso
     * @param palabras Lista de palabras para el diccionario
     * @param bolsaData    Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException               Si ocurre un error de E/S
     * @throws RecursoNoExistenteException Si el recurso no existe
     */
    public void modificarRecurso(String id, List<String> palabras, List<String> bolsaData)
            throws IOException, RecursoNoExistenteException {
        gestorRecursos.modificarRecurso(id, palabras, bolsaData);
    }

    /**
     * Modifica el archivo de diccionario para un recurso.
     * 
     * @param id       Identificador del recurso
     * @param palabras Lista de palabras para el diccionario
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void modificarDiccionario(String id, List<String> palabras) throws IOException {
        gestorRecursos.modificarDiccionario(id, palabras);
    }

    /**
     * Modifica el archivo de bolsa para un recurso.
     * 
     * @param id    Identificador del recurso
     * @param bolsa Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void modificarBolsa(String id, List<String> bolsa) throws IOException {
        gestorRecursos.modificarBolsa(id, bolsa);
    }

    /**
     * Elimina un recurso del sistema.
     * 
     * @param id Identificador del recurso
     * @throws DiccionarioNoEncontradoException Si el diccionario no se encuentra
     * @throws BolsaNoEncontradaException       Si la bolsa no se encuentra
     * @throws IOException                      Si ocurre un error de E/S
     */
    public void eliminarRecurso(String id)
            throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, IOException {
        gestorRecursos.eliminarRecurso(id);
    }

    // ------------------------------ IMAGENES ----------------------------------

    /**
     * Guarda la imagen de perfil de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @param image    Imagen a guardar
     */
    public void guardarImagenPerfil(String username, BufferedImage image) {
        gestorUsuarios.guardarImagenPerfil(username, image);
    }

    /**
     * Obtiene la imagen de perfil de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @return La imagen de perfil, o null si no existe
     */
    public BufferedImage obtenerImagenPerfil(String username) {
        return gestorUsuarios.obtenerImagenPerfil(username);
    }

    /**
     * Elimina la imagen de perfil de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     */
    public void eliminarImagenPerfil(String username) {
        gestorUsuarios.eliminarImagenPerfil(username);
    }

   
}