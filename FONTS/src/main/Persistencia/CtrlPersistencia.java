package Persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Dominio.Excepciones.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.json.JSONObject;
import org.json.JSONArray;

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
     * Ruta de los recursos, partidas y jugadores.
     */
    private static final String RECURSOS = "FONTS/src/main/Persistencia/Datos/Recursos/";
    private static final String PARTIDAS = "FONTS/src/main/Persistencia/Datos/Partidas/";
    private static final String JUGADORES = "FONTS/src/main/Persistencia/Datos/Jugadores/";



    /**
     * Constructor de la clase CtrlPersistencia.
    */
    public CtrlPersistencia() {

    }

    // ─── Jugadores ─────────────────────────────────────────────────────────────

    /**
     * Comprueba si un jugador existe en el sistema.
     * @param username Nombre de usuario del jugador
     * @return true si el jugador existe, false en caso contrario
     */
    public boolean existeJugador(String username) {
        // Define la ruta del directorio del jugador
        File userDir = new File(JUGADORES + username);
        // Comprueba si la carpeta del jugador existe
        return userDir.exists() && userDir.isDirectory();
    }


    /**
     * Registra un nuevo jugador en el sistema.
     * 
     * @param username Nombre de usuario único del jugador
     * @param password Contraseña del jugador
     * @throws IOException Si ocurre algún error durante la creación del archivo
     * @throws UsuarioYaRegistradoException Si el nombre de usuario ya existe
     */
    public void anadirJugador(String username, String password) throws IOException, UsuarioYaRegistradoException {
        // Verificar si el jugador ya existe
        if (existeJugador(username)) {
            throw new UsuarioYaRegistradoException(username);
        }
    
        // Crear el directorio del jugador
        Path userDir = Paths.get(JUGADORES + username);
        Files.createDirectories(userDir);
    
        // Crear el archivo JSON del jugador
        Path userFile = userDir.resolve(username + ".json");
        
        // Preparar los datos del jugador en formato JSON
        JSONObject jugadorJson = new JSONObject();
        jugadorJson.put("nombre", username);      // Usado en la interfaz
        jugadorJson.put("password", password);    // Para autenticación
        jugadorJson.put("maxpuntos", 0);          // Puntuación inicial
        jugadorJson.put("ultimaPartidaGuardada", JSONObject.NULL);  // Sin partida guardada
        
        // Escribir el archivo JSON
        try (BufferedWriter writer = Files.newBufferedWriter(userFile, StandardCharsets.UTF_8)) {
            writer.write(jugadorJson.toString(4)); // 4 espacios para formato legible
        }
    }
    


    /**
     * Elimina un jugador del sistema y todos sus archivos asociados.
     * 
     * @param username Nombre de usuario del jugador a eliminar
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws IOException Si hay algún error al eliminar archivos
     */
    public void eliminarJugador(String username) throws UsuarioNoEncontradoException, IOException {
        // Usa el método existente para verificar si el jugador existe
        if (!existeJugador(username)) {
            throw new UsuarioNoEncontradoException(username);
        }
        
        // Define la ruta de la carpeta del jugador
        File userDir = new File(JUGADORES + username);
    
        // Elimina todos los archivos dentro de la carpeta
        File[] archivos = userDir.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (!archivo.delete()) {
                    throw new IOException("No se pudo eliminar el archivo: " + archivo.getName());
                }
            }
        }
    
        // Elimina la carpeta del jugador
        if (!userDir.delete()) {
            throw new IOException("No se pudo eliminar la carpeta del jugador: " + username);
        }
    }

    

    /**
     * Verifica si la contraseña de un jugador es correcta.
     * 
     * @param username Nombre de usuario del jugador
     * @param password Contraseña a verificar
     * @return true si la contraseña es correcta, false en caso contrario
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public boolean verificarContrasena(String username, String password) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");

        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Verificar la contraseña
            return jugadorJson.getString("password").equals(password);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    /**
     * Actualiza la contraseña de un jugador si la contraseña actual es correcta.
     * 
     * @param username Nombre de usuario del jugador
     * @param currentPass Contraseña actual del jugador
     * @param newPass Nueva contraseña del jugador
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws IllegalArgumentException Si la contraseña actual es incorrecta
     */
    public void actualizarContrasena(String username, String currentPass, String newPass) 
            throws UsuarioNoEncontradoException {
        // Verificar la contraseña actual
        if (!verificarContrasena(username, currentPass)) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");
        
        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);
    
            // Actualizar la contraseña
            jugadorJson.put("password", newPass);
    
            // Escribir el archivo JSON actualizado
            Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    /**
     * Actualiza la puntuación máxima de un jugador.
     * @param username Nombre de usuario del jugador
     * @param pts Nueva puntuación máxima
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws PuntuacionInvalidaException Si la puntuación es invalida
     */
    public void actualizarPuntuacion(String username, int pts)
            throws UsuarioNoEncontradoException, PuntuacionInvalidaException {
        // Verificar que la puntuación no sea negativa
        if (pts < 0) {
            throw new PuntuacionInvalidaException(pts);
        }
    
        // Obtener la puntuación actual
        int maxpuntosActual = obtenerPuntuacion(username);
    
        // Actualizar la puntuación solo si el nuevo valor es mayor
        if (pts > maxpuntosActual) {
            // Define la ruta del archivo JSON del jugador
            Path userFile = Paths.get(JUGADORES + username, username + ".json");
    
            try {
                // Leer el contenido del archivo JSON
                String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
                JSONObject jugadorJson = new JSONObject(contenido);
    
                // Actualizar la puntuación
                jugadorJson.put("maxpuntos", pts);
    
                // Escribir el archivo JSON actualizado
                Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
            }
        }
    }
  

    /**
     * Actualiza la última partida guardada de un jugador.
     * @param username Nombre de usuario del jugador
     * @param idPartida ID de la partida guardada
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public void actualizarUltimaPartida(String username, String idPartida) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");

        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Actualizar la última partida guardada
            jugadorJson.put("ultimaPartidaGuardada", idPartida);

            // Escribir el archivo JSON actualizado
            Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    public int obtenerPuntuacion(String username) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");


        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Obtener la puntuación
            return jugadorJson.getInt("maxpuntos");
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    /**
     * Obtiene la última partida guardada de un jugador.
     * @param username Nombre de usuario del jugador
     * @return ID de la última partida guardada
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public String obtenerUltimaPartida(String username) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");

        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Obtener la última partida guardada
            return jugadorJson.getString("ultimaPartidaGuardada");
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    // ------------------------------ RANKING ----------------------------------

    /**
     * Genera un ranking de jugadores basado en sus puntuaciones.
     * @return Lista de entradas (nombre, puntuación) ordenadas por puntuación
     * @throws IOException Si ocurre un error al acceder a los archivos
     */
    public List<Map.Entry<String, Integer>> generarRanking() throws IOException {
        // Ruta base donde están los directorios de los jugadores
        Path jugadoresDir = Paths.get(JUGADORES);

        // Lista para almacenar los pares (nombre, puntuación)
        List<Map.Entry<String, Integer>> ranking = new ArrayList<>();

        // Verificar si el directorio de jugadores existe
        if (Files.exists(jugadoresDir) && Files.isDirectory(jugadoresDir)) {
            // Recorrer los directorios de los jugadores
            try (Stream<Path> paths = Files.list(jugadoresDir)) {
                paths.filter(Files::isDirectory).forEach(dir -> {
                    // Obtener el archivo JSON del jugador
                    Path userFile = dir.resolve(dir.getFileName() + ".json");
                    if (Files.exists(userFile)) {
                        try {
                            // Leer el contenido del archivo JSON
                            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
                            JSONObject jugadorJson = new JSONObject(contenido);

                            if (jugadorJson.getInt("maxpuntos") > 0) {
                                String username = jugadorJson.getString("nombre");
                                int maxpuntos = jugadorJson.getInt("maxpuntos");
                                // Añadir al ranking
                                ranking.add(Map.entry(username, maxpuntos));
                            }
                        } catch (IOException e) {
                            System.err.println("Error al procesar el archivo de jugador: " + userFile);
                        }
                    }
                });
            }
        }

        // Ordenar el ranking por puntuación descendente y luego por nombre ascendente
        ranking.sort((e1, e2) -> {
            int cmp = Integer.compare(e2.getValue(), e1.getValue()); // Ordenar por puntuación (descendente)
            if (cmp == 0) {
                return e1.getKey().compareTo(e2.getKey()); // Si hay empate, ordenar por nombre (ascendente)
            }
            return cmp;
        });

        return ranking;
    }

    /**
     * Obtiene la posición de un jugador en el ranking.
     * @param name Nombre del jugador
     * @return Posición del jugador en el ranking
     * @throws UsuarioNoEncontradoException Si el jugador no se encuentra en el ranking
     * @throws IOException Si ocurre un error al acceder a los archivos
     */
    public int obtenerPosicion(String name) throws UsuarioNoEncontradoException, IOException {
        // Generar el ranking
        List<Map.Entry<String, Integer>> ranking = generarRanking();

        // Recorrer el ranking para encontrar la posición del jugador
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getKey().equals(name)) {
                return i + 1; // Las posiciones empiezan en 1
            }
        }

        // Si no se encuentra el jugador, lanzar una excepción
        throw new UsuarioNoEncontradoException("El jugador no fue encontrado en el ranking: " + name);
    }

    // ------------------------------ PARTIDAS ----------------------------------
    
    /**
     * Verifica si una partida existe en el sistema.
     * @param id ID de la partida
     * @throws PartidaNoEncontradaException Si la partida no existe 
     * @return
     */
    public boolean existePartida(String id) {
        // Verifica si el archivo de la partida existe
        File partidaFile = new File(PARTIDAS + "partida_" + id + ".json");
        return partidaFile.exists();
    }


    /**
     * Verifica si una partida está acabada.
     * @param id ID de la partida
     * @return true si la partida está acabada, false en caso contrario
     * @throws PartidaNoEncontradaException Si la partida no existe o no se puede leer
     */
    public boolean esPartidaAcabada(String id) {
        try {
            // Verifica si la partida existe
            if (!existePartida(id)) {
                return false;
            }
            
            // Leemos el archivo y parseamos directamente el JSON
            String content = Files.readString(Paths.get(PARTIDAS + "partida_" + id + ".json"), 
                                             StandardCharsets.UTF_8);
            JSONObject partidaJson = new JSONObject(content);
            
            // El campo se guarda como un entero (0 o 1)
            int acabadaValue = partidaJson.getInt("partida_acabada");
            return acabadaValue == 1;
            
        } catch (IOException e) {
            System.err.println("[Persistencia] Error al leer la partida: " + e.getMessage());
            return false;
        }
    }


    public String obtenerRecursoPartida(String id)
    {
        // Verifica si la partida existe
        if (!existePartida(id)) {
            return null;
        }

        // Carga el archivo de la partida
        String rutaArchivo = PARTIDAS + "partida_" + id + ".json";
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
            JSONObject partidaJson = new JSONObject(contenido);

            System.out.println("El recurso de la partida es: " + partidaJson.getString("recurso"));
            return partidaJson.getString("recurso");
        } catch (IOException e) {
            System.err.println("[Persistencia] Error al cargar el recurso de la partida: " + e.getMessage());
            return null;
        }
    }

    /**
     * Guarda una partida en el sistema.
     * @param username Nombre de usuario del jugador
     * @param id ID de la partida
     * @param partida Datos de la partida
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public void guardarPartida(String username, String segundoJugador, String id, List<String> partida) throws UsuarioNoEncontradoException {
        String nombreArchivo = "partida_" + id + ".json";
        String rutaArchivo = PARTIDAS + nombreArchivo;

        // Actualizar la última partida
        actualizarUltimaPartida(username, id);
        actualizarUltimaPartida(segundoJugador, id);

        // Convertir la lista de datos a JSON
        String jsonPartida = partidaListToJson(partida);

        // Escribir el JSON en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(jsonPartida);
            System.out.println("Partida guardada exitosamente en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("[Persistencia] Error al guardar la partida: " + e.getMessage());
        }
    }

    
    public List<String> cargarPartida(String id) throws PartidaNoEncontradaException {
        // Verifica si la partida existe
        if (!existePartida(id)) {
            throw new PartidaNoEncontradaException(id);
        }

        // Carga el archivo de la partida
        String rutaArchivo = PARTIDAS + "partida_" + id + ".json";
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
            return jsonToPartidaList(contenido);
        } catch (IOException e) {
            throw new PartidaNoEncontradaException(id);
        }
    }

    public void eliminarPartida(String id) throws PartidaNoEncontradaException {
        // Verifica si la partida existe
        if (!existePartida(id)) {
            throw new PartidaNoEncontradaException(id);
        }

        // Elimina el archivo de la partida
        File partidaFile = new File(PARTIDAS + "partida_" + id + ".json");
        if (partidaFile.delete()) {
            System.out.println("Partida eliminada: " + partidaFile.getPath());
        } else {
            System.out.println("No se pudo eliminar la partida.");
        }
    }

        public List<String> listarPartidasNoAcabadas(String jugadorActual) {
        List<String> partidasNoAcabadas = new ArrayList<>();
        File dir = new File(PARTIDAS);
    
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.getName().endsWith(".json")) {
                    String id = file.getName().replace("partida_", "").replace(".json", "");
                    
                    try {
                        // Verificamos si la partida no está acabada
                        if (!esPartidaAcabada(id)) {
                            // Leer el archivo JSON para verificar si el jugador actual es jugador_1
                            String content = Files.readString(Paths.get(PARTIDAS + file.getName()), 
                                                             StandardCharsets.UTF_8);
                            JSONObject partidaJson = new JSONObject(content);
                            
                            // Verificar si el jugador actual es el jugador_1
                            if (partidaJson.getString("jugador_1").equals(jugadorActual)) {
                                partidasNoAcabadas.add(id);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("[Persistencia] Error al procesar partida " + id + ": " + e.getMessage());
                    }
                }
            }
        }
        return partidasNoAcabadas; // Retorna la lista de partidas no acabadas
    }

    // ─── Diccionarios y Bolsas ───────────────────────────────────────────────

    public boolean existeRecurso(String id) {
        // Verifica si el directorio del idioma existe
        File dir = new File(RECURSOS + id);
        return dir.exists() && dir.isDirectory();
    }

    public List<String> listarRecursos() {
        List<String> idiomas = new ArrayList<>();
        File dir = new File(RECURSOS);
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles(File::isDirectory))) {
                idiomas.add(file.getName());
            }
        }
        return idiomas;
    }

    
    public List<String> obtenerDiccionario(String id) throws IOException {
        List<String> palabras = new ArrayList<>();
        File diccionarioFile = new File(RECURSOS + id, id + "_diccionario.txt");
        if (diccionarioFile.exists() && diccionarioFile.isFile()) {
            palabras = leerArchivoTexto(diccionarioFile.getPath());
        } else {
            throw new IOException("No se encontró el diccionario para el idioma: " + id);
        }
        return palabras;
    }

    public List<String> obtenerBolsa(String id) throws IOException {
        List<String> bolsa = new ArrayList<>();
        File bolsaFile = new File(RECURSOS + id, id + "_bolsa.txt");
        if (bolsaFile.exists() && bolsaFile.isFile()) {
            bolsa = leerArchivoTexto(bolsaFile.getPath());
        } else {
            throw new IOException("No se encontró la bolsa para el idioma: " + id);
        }
        return bolsa;
    }

    public void crearRecurso(String id, List<String> palabras, Map<String, int[]> bolsaData)
            throws IOException, RecursoExistenteException {
        // Verifica si el recurso ya existe
        if (existeRecurso(id)) {
            throw new RecursoExistenteException(id);
        }

        // Crea el directorio del recurso si no existe
        File dir = new File(RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear el directorio: " + dir.getPath());
        }

        // Crea el archivo del diccionario
        crearDiccionario(id, palabras);

        // Crea el archivo de la bolsa
        crearBolsa(id, bolsaData);
    }

    public void crearDiccionario(String id, List<String> palabras) throws IOException, RecursoExistenteException {
       

        // Crea el directorio del recurso si no existe
        File dir = new File(RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear el directorio: " + dir.getPath());
        }

        // Crea el archivo del diccionario
        File diccionarioFile = new File(dir, id + "_diccionario.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(diccionarioFile))) {
            for (String palabra : palabras) {
                writer.write(palabra);
                writer.newLine();
            }
        }
    }

    public void crearBolsa(String id, Map<String, int[]> bolsaData) throws IOException, RecursoExistenteException {
        // Crea el archivo de la bolsa
        File bolsaFile = new File(RECURSOS + id, id + "_bolsa.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bolsaFile))) {
            for (Map.Entry<String, int[]> entry : bolsaData.entrySet()) {
                String ficha = entry.getKey();
                int[] valores = entry.getValue();
                writer.write(ficha + " " + valores[0] + " " + valores[1]);
                writer.newLine();
            }
        }
    }

    public void eliminarRecurso(String id)
            throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, IOException {
        // Define la ruta de la carpeta del recurso
        File recursoDir = new File(RECURSOS + id);

        // Verifica si la carpeta existe
        if (!recursoDir.exists() || !recursoDir.isDirectory()) {
            throw new IOException("El recurso no existe: " + id);
        }

        // Verifica si el diccionario existe
        File diccionarioFile = new File(recursoDir, id + "_diccionario.txt");
        if (!diccionarioFile.exists()) {
            throw new DiccionarioNoEncontradoException("No se encontró el diccionario para el idioma: " + id);
        }

        // Verifica si la bolsa existe
        File bolsaFile = new File(recursoDir, id + "_bolsa.txt");
        if (!bolsaFile.exists()) {
            throw new BolsaNoEncontradaException("No se encontró la bolsa para el idioma: " + id);
        }

        // Elimina los archivos del recurso
        if (!diccionarioFile.delete()) {
            throw new IOException("No se pudo eliminar el diccionario: " + diccionarioFile.getName());
        }
        if (!bolsaFile.delete()) {
            throw new IOException("No se pudo eliminar la bolsa: " + bolsaFile.getName());
        }

        // Elimina la carpeta del recurso
        if (!recursoDir.delete()) {
            throw new IOException("No se pudo eliminar la carpeta del recurso: " + id);
        }
    }

    // -------------------------------- UTILIDADES E/S----------------------------------
    
    private List<String> leerArchivoTexto(String ruta) throws IOException {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            for (String l; (l = br.readLine()) != null;) {
                l = l.trim();
                if (!l.isEmpty()) {
                    out.add(l);
                }
            }
        }
        return out;
    }

    // -------------------------------- UTILIDADES JSON----------------------------------

    public static String partidaListToJson(List<String> partidaData) {
        // Usamos un JSONObject directamente ya que el orden no importa
        JSONObject partidaJson = new JSONObject();

        // 0. Partida acabada
        partidaJson.put("partida_acabada", partidaData.get(0));

        // 1. nombre_partida
        partidaJson.put("nombre_partida", partidaData.get(1));

        // 2. numero_turnos
        partidaJson.put("numero_turnos", Integer.parseInt(partidaData.get(2)));

        // 3. turno_jugador
        partidaJson.put("turno_jugador", Integer.parseInt(partidaData.get(3)));

        // 4. jugador_1
        partidaJson.put("jugador_1", partidaData.get(4));

        // 5. jugador_2
        partidaJson.put("jugador_2", partidaData.get(5));

        // 6. puntos_jugador_1
        partidaJson.put("puntos_jugador_1", Integer.parseInt(partidaData.get(6)));

        // 7. puntos_jugador_2
        partidaJson.put("puntos_jugador_2", Integer.parseInt(partidaData.get(7)));

        // 8. fichas_jugador_1
        JSONArray fichasJugador1 = parseFichas(partidaData.get(8));
        partidaJson.put("fichas_jugador_1", fichasJugador1);

        // 9. fichas_jugador_2
        JSONArray fichasJugador2 = parseFichas(partidaData.get(9));
        partidaJson.put("fichas_jugador_2", fichasJugador2);

        // 10. fichas_restantes
        int numFichasRestantes = Integer.parseInt(partidaData.get(10));
        JSONArray fichasRestantes = new JSONArray();
        for (int i = 11; i < 11 + numFichasRestantes; i++) {
            fichasRestantes.put(partidaData.get(i)); // Añadimos la ficha como una cadena
        }
        partidaJson.put("bolsa", fichasRestantes);

        // 11. posiciones_tablero
        int numFichasUsadas = Integer.parseInt(partidaData.get(11 + numFichasRestantes));
        JSONArray posicionesTablero = new JSONArray();
        for (int i = 11 + numFichasRestantes + 1; i < partidaData.size(); i++) {
            posicionesTablero.put(partidaData.get(i)); // Añadimos la posición como una cadena
        }
        partidaJson.put("posiciones_tablero", posicionesTablero);

        // 12. recurso
        partidaJson.put("recurso", partidaData.get(partidaData.size() - 1)); // Último elemento es el recurso

        // Retornamos el JSON como una cadena con formato bonito
        return partidaJson.toString(2); // Formateo bonito del JSON
    }

    public static List<String> jsonToPartidaList(String partidaJson) {
        // Inicializamos la lista que contendrá los datos de la partida
        List<String> partidaData = new ArrayList<>();

        // Convertimos el JSON a un JSONObject
        JSONObject partida = new JSONObject(partidaJson);

        // Extraemos los valores en el orden exacto que hemos establecido
        partidaData.add(String.valueOf(partida.getInt("partida_acabada"))); // 0. partida_acabada
        partidaData.add(partida.getString("nombre_partida")); // 1. nombre_partida
        partidaData.add(String.valueOf(partida.getInt("numero_turnos"))); // 2. numero_turnos
        partidaData.add(String.valueOf(partida.getInt("turno_jugador"))); // 3. turno_jugador
        partidaData.add(partida.getString("jugador_1")); // 4. jugador_1
        partidaData.add(partida.getString("jugador_2")); // 5. jugador_2
        partidaData.add(String.valueOf(partida.getInt("puntos_jugador_1"))); // 6. puntos_jugador_1
        partidaData.add(String.valueOf(partida.getInt("puntos_jugador_2"))); // 7. puntos_jugador_2

        // 8. fichas_jugador_1
        JSONArray fichasJugador1 = partida.getJSONArray("fichas_jugador_1");
        partidaData.add(parseFichasToString(fichasJugador1));

        // 9. fichas_jugador_2
        JSONArray fichasJugador2 = partida.getJSONArray("fichas_jugador_2");
        partidaData.add(parseFichasToString(fichasJugador2));

        // 10. fichas_restantes
        JSONArray fichasRestantes = partida.getJSONArray("bolsa");
        partidaData.add(String.valueOf(fichasRestantes.length())); // Añadimos el número de fichas restantes
        for (int i = 0; i < fichasRestantes.length(); i++) {
            partidaData.add(fichasRestantes.getString(i)); // Añadimos cada ficha restante
        }

        // 11. posiciones_tablero
        JSONArray posicionesTablero = partida.getJSONArray("posiciones_tablero");
        partidaData.add(String.valueOf(posicionesTablero.length())); // Añadimos el número de fichas restantes
        for (int i = 0; i < posicionesTablero.length(); i++) {
            partidaData.add(posicionesTablero.getString(i)); // Añadimos cada posición del tablero
        }

        // 12. recurso
        partidaData.add(partida.getString("recurso")); // Último elemento es el recurso
        return partidaData;
    }

    private static JSONArray parseFichas(String fichaData) {
        JSONArray fichas = new JSONArray();
        String[] fichaArray = fichaData.split(" ");
        for (int i = 0; i < fichaArray.length; i += 2) {
            fichas.put(fichaArray[i] + " " + fichaArray[i + 1]); // Guardamos la ficha como una cadena
        }
        return fichas;
    }

    private static String parseFichasToString(JSONArray fichas) {
        StringBuilder fichasStr = new StringBuilder();
        for (int i = 0; i < fichas.length(); i++) {
            fichasStr.append(fichas.getString(i)).append(" ");
        }
        return fichasStr.toString().trim();
    }

}
