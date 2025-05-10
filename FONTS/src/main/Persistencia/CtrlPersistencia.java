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
import Dominio.Excepciones.PartidaNoEncontradaException;
import Dominio.Excepciones.PartidaYaExistenteException;
import Dominio.Excepciones.UsuarioYaRegistradoException;

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
     * Ruta al archivo de datos de usuarios.
     */
    private static final String FILE_USUARIOS = "FONTS/src/main/Persistencia/Datos/usuarios.txt";
    /**
     * Ruta base de recursos de diccionarios y bolsas.
     */
    private static final String BASE_RECURSOS = "FONTS/src/main/Recursos/Idiomas";
    private static final String PARTIDAS = "FONTS/src/main/Persistencia/Datos/Partidas/";
    private static final Path FILE_JUGADORES = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores/jugadores.json");

    
    /**
     * Identificador de la última partida guardada.
     */
    private String ultimaPartida;

    /**
     * Map de diccionario de palabras por ID de idioma.
     */
    private final Map<String, List<String>> diccionarios;
    /**
     * Map de líneas de bolsa por ID de idioma.
     */
    private final Map<String, List<String>> bolsas;

    /**
     * Construye el controlador, cargando usuarios desde disco, inicializando
     * ranking y recargando recursos de diccionarios y bolsas.
     */
    public CtrlPersistencia() {
        
        this.diccionarios = new HashMap<>();
        this.bolsas = new HashMap<>();
        this.ultimaPartida = null;
        cargarRecursosDesdeDisco();
    }

    // ─── Jugadores ─────────────────────────────────────────────────────────────

    public boolean existeJugador(String username) {
        // Define la ruta del directorio del jugador
        File userDir = new File("FONTS/src/main/Persistencia/Datos/Jugadores/" + username);
        
        // Comprueba si la carpeta del jugador existe
        return userDir.exists() && userDir.isDirectory();
    }

    public void anadirJugador(String username, String password) throws IOException, UsuarioYaRegistradoException {
        if (existeJugador(username)) {
            throw new UsuarioYaRegistradoException(username);
        }

        // Crear el directorio del jugador y sus padres si no existen
        Path userDir = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username);
        Files.createDirectories(userDir);

        // Crear el archivo JSON del jugador
        Path userFile = userDir.resolve(username + ".json");
        JSONObject jugadorJson = new JSONObject();
        jugadorJson.put("nombre", username);
        jugadorJson.put("password", password);
        jugadorJson.put("maxpuntos", 0);
        jugadorJson.put("ultimaPartidaGuardada", JSONObject.NULL);

        // Escribir el JSON en el archivo
        try (BufferedWriter writer = Files.newBufferedWriter(userFile, StandardCharsets.UTF_8)) {
            writer.write(jugadorJson.toString(4));
        }
    }

    public void eliminarJugador(String username) throws UsuarioNoEncontradoException, IOException {
        // Define la ruta de la carpeta del jugador
        File userDir = new File("FONTS/src/main/Persistencia/Datos/Jugadores/" + username);
    
        // Verifica si la carpeta existe
        if (!userDir.exists() || !userDir.isDirectory()) {
            throw new UsuarioNoEncontradoException(username);
        }
    
        // Elimina la carpeta y su contenido
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

    public void actualizarContrasena(String username, String newPass) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");
    
        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }
    
        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);
    
            // Actualizar la contraseña
            jugadorJson.put("password", newPass);
    
            // Escribir el archivo JSON actualizado
            Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    public void actualizarPuntuacion(String username, int pts) throws UsuarioNoEncontradoException, PuntuacionInvalidaException {
        // Verificar que la puntuación no sea negativa
        if (pts < 0) {
            throw new PuntuacionInvalidaException(pts);
        }
    
        // Obtener la puntuación actual utilizando getPuntuacion
        int maxpointsActual = getPuntuacion(username);
    
        // Actualizar la puntuación solo si el nuevo valor es mayor
        if (pts > maxpointsActual) {
            // Define la ruta del archivo JSON del jugador
            Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");
    
            try {
                // Leer el contenido del archivo JSON
                String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
                JSONObject jugadorJson = new JSONObject(contenido);
    
                // Actualizar la puntuación
                jugadorJson.put("maxpoints", pts);
    
                // Escribir el archivo JSON actualizado
                Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
            }
        }
    }

    public void actualizarUltimaPartida(String username, String idPartida) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");
    
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
            Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    public int getPuntuacion(String username) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");
    
        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }
    
        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);
    
            // Obtener la puntuación
            return jugadorJson.getInt("maxpoints");
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    //------------------------------  RANKING  ----------------------------------

    /**
     * Carga los usuarios desde el archivo de texto y devuelve un mapa de
     * jugadores.
     *
     * @return mapa de jugadores con nombre como clave y [password, puntos] como
     *         valor
     */
    public List<Map.Entry<String, Integer>> generarRanking() throws IOException {
        // Ruta base donde están los directorios de los jugadores
        Path jugadoresDir = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores");
    
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
    
                            // Obtener el nombre y la puntuación del jugador
                            String username = jugadorJson.getString("username");
                            int maxpoints = jugadorJson.getInt("maxpoints");
    
                            // Añadir al ranking
                            ranking.add(Map.entry(username, maxpoints));
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
     *
     * @param name el nombre del jugador
     * @return la posición del jugador en el ranking (1-indexed)
     * @throws UsuarioNoEncontradoException si el jugador no está en el ranking
     * @throws IOException si ocurre un error al generar el ranking
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

    //------------------------------  PARTIDAS  ----------------------------------
    public boolean existePartida(String id) {
        // Verifica si el archivo de la partida existe
        File partidaFile = new File(PARTIDAS + "partida_" + id + ".json");
        return partidaFile.exists();
    }

    public boolean esPartidaAcabada(String id) {
    try {
        // Leemos el archivo y parseamos directamente el JSON
        String content = new String(Files.readAllBytes(Paths.get(PARTIDAS + "partida_" + id + ".json")));
        return new JSONObject(content).getBoolean("partida_acabada");
    } catch (Exception e) {
        System.err.println("[Persistencia] Error al leer la partida: " + e.getMessage());
        return false;
    }
}

    public void guardarPartida(String id, List<String> partida) throws PartidaYaExistenteException {
        String nombreArchivo = "partida_" + id + ".json";
        String rutaArchivo = PARTIDAS + nombreArchivo;

        // Actualizar la última partida
        ultimaPartida = id;

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

    public List<String> cargarUltimaPartida() throws PartidaNoEncontradaException {
        // Verifica si hay una última partida guardada
        if (ultimaPartida == null) {
            throw new PartidaNoEncontradaException("No hay ninguna partida guardada.");
        }

        // Carga la última partida
        return cargarPartida(ultimaPartida);
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

    public List<String> listarPartidasNoAcabadas() {
        List<String> partidasNoAcabadas = new ArrayList<>();
        File dir = new File(PARTIDAS);
        
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.getName().endsWith(".json")) {
                    String id = file.getName().replace("partida_", "").replace(".json", "");
                    // Verificamos si la partida no está acabada
                    if (!esPartidaAcabada(id)) {
                        partidasNoAcabadas.add(id); // Si no está acabada, la agregamos a la lista
                    }
                }
            }
        }
        return partidasNoAcabadas; // Retorna la lista de partidas no acabadas
}

    /**
     * Elimina la partida con el ID dado.
     *
     * @param id identificador de la partida
     * @throws PartidaNoEncontradaException si no existe el ID
     */
    public void removePartida(String id) throws PartidaNoEncontradaException {
        String rutaArchivo = PARTIDAS + "partida_" + id + ".txt";
        File archivoPartida = new File(rutaArchivo);

        // Verificar si el archivo existe
        if (!archivoPartida.exists()) {
            throw new PartidaNoEncontradaException(id);
        }

        // Eliminar el archivo
        if (!archivoPartida.delete()) {
            throw new PartidaNoEncontradaException("No se pudo eliminar la partida con ID: " + id);
        }

        // Actualizar la última partida si corresponde
        if (id.equals(ultimaPartida)) {
            ultimaPartida = null;
        }
    }

    // ─── Diccionarios y Bolsas ───────────────────────────────────────────────
    /**
     * Carga desde disco todos los recursos de idiomas disponibles.
     */
    private void cargarRecursosDesdeDisco() {
        File base = new File(BASE_RECURSOS);
        if (!base.isDirectory()) {
            return;
        }
        for (File dir : Objects.requireNonNull(base.listFiles(File::isDirectory))) {
            String id = dir.getName();
            try {
                File fd = new File(dir, id + "_diccionario.txt");
                if (fd.exists()) {
                    diccionarios.put(id, leerArchivoTexto(fd.getPath()));
                }
                File fb = new File(dir, id + "_bolsa.txt");
                if (fb.exists()) {
                    bolsas.put(id, leerArchivoTexto(fb.getPath()));
                }
            } catch (IOException ignored) {
                // Ignorar recursos corruptos
            }
        }
    }

    /**
     * Obtiene los IDs de todos los diccionarios cargados.
     *
     * @return conjunto de IDs de diccionarios
     */
    public Set<String> getDiccionarioIDs() {
        System.out.println(diccionarios.keySet());
        return diccionarios.keySet();
    }

    /**
     * Obtiene los IDs de todas las bolsas cargadas.
     *
     * @return conjunto de IDs de bolsas
     */
    public Set<String> getBolsaIDs() {
        return bolsas.keySet();
    }

    /**
     * Recupera un diccionario en memoria por su ID.
     *
     * @param id identificador del diccionario
     * @return lista de palabras (inmutable)
     * @throws DiccionarioNoEncontradoException si no existe ese ID
     */
    public List<String> getDiccionario(String id)
            throws DiccionarioNoEncontradoException {
        List<String> datos = diccionarios.get(id);
        if (datos == null) {
            throw new DiccionarioNoEncontradoException(
                    String.format("Diccionario '%s' no encontrado.", id));
        }
        return Collections.unmodifiableList(datos);
    }

    /**
     * Recupera una bolsa en memoria por su ID.
     *
     * @param id identificador de la bolsa
     * @return lista de líneas de definición de bolsa (inmutable)
     * @throws BolsaNoEncontradaException si no existe ese ID
     */
    public List<String> getBolsa(String id) throws BolsaNoEncontradaException {
        List<String> datos = bolsas.get(id);
        if (datos == null) {
            throw new BolsaNoEncontradaException(
                    String.format("Bolsa '%s' no encontrada.", id));
        }
        return Collections.unmodifiableList(datos);
    }

    /**
     * Añade un nuevo diccionario y lo persiste en disco.
     *
     * @param id       identificador del idioma
     * @param palabras lista de palabras a guardar
     * @throws DiccionarioYaExistenteException si el ID ya existe
     * @throws IOException                     si hay error de E/S
     */
    public void addDiccionario(String id, List<String> palabras)
            throws DiccionarioYaExistenteException, IOException {
        if (diccionarios.containsKey(id)) {
            throw new DiccionarioYaExistenteException(id);
        }
        File dir = new File(BASE_RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + dir.getPath());
        }
        File f = new File(dir, id + "_diccionario.txt");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (String p : palabras) {
                w.write(p + "\n");
            }
        }
        diccionarios.put(id, new ArrayList<>(palabras));
    }

    /**
     * Añade una nueva bolsa de fichas y la persiste en disco.
     *
     * @param id        identificador del idioma
     * @param bolsaData mapa letra -> [cantidad, valor]
     * @throws BolsaYaExistenteException si el ID ya existe
     * @throws IOException               si hay error de E/S
     */
    public void addBolsa(String id, Map<String, int[]> bolsaData)
            throws BolsaYaExistenteException, IOException {
        if (bolsas.containsKey(id)) {
            throw new BolsaYaExistenteException(id);
        }
        File dir = new File(BASE_RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear carpeta: " + dir.getPath());
        }
        File f = new File(dir, id + "_bolsa.txt");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (var e : bolsaData.entrySet()) {
                w.write(e.getKey() + " " + e.getValue()[0] + " " + e.getValue()[1] + "\n");
            }
        }
        bolsas.put(id, new ArrayList<>(leerArchivoTexto(f.getPath())));
    }

    /**
     * Elimina completamente un idioma (diccionario y bolsa) de memoria y disco.
     *
     * @param id identificador del idioma
     * @throws DiccionarioNoEncontradoException si el diccionario no existe
     * @throws BolsaNoEncontradaException       si la bolsa no existe
     * @throws IOException                      si ocurre error de E/S al borrar
     */
    public void removeIdiomaCompleto(String id)
            throws DiccionarioNoEncontradoException,
            BolsaNoEncontradaException,
            IOException {
        if (!diccionarios.containsKey(id)) {
            throw new DiccionarioNoEncontradoException(
                    String.format("No existe ningún diccionario con ID '%s'.", id));
        }
        if (!bolsas.containsKey(id)) {
            throw new BolsaNoEncontradaException(
                    String.format("No existe ninguna bolsa con ID '%s'.", id));
        }
        diccionarios.remove(id);
        bolsas.remove(id);
        File dir = new File(BASE_RECURSOS, id);
        if (dir.exists()) {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                if (!f.delete()) {
                    throw new IOException("No se pudo borrar el archivo: " + f.getPath());
                }
            }
            if (!dir.delete()) {
                throw new IOException("No se pudo borrar la carpeta: " + dir.getPath());
            }
        }
    }

    // ─── Helpers de E/S ─────────────────────────────────────────────────────
    /**
     * Lee todas las líneas no vacías de un archivo de texto.
     *
     * @param ruta ruta del archivo
     * @return lista de líneas leídas
     * @throws IOException si hay error de lectura
     */
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

        return partidaData;
    }

// Función para parsear las fichas de un jugador como cadenas "T 1", "Q 5".
    private static JSONArray parseFichas(String fichaData) {
        JSONArray fichas = new JSONArray();
        String[] fichaArray = fichaData.split(" ");
        for (int i = 0; i < fichaArray.length; i += 2) {
            fichas.put(fichaArray[i] + " " + fichaArray[i + 1]); // Guardamos la ficha como una cadena
        }
        return fichas;
    }

    // Función auxiliar para convertir las fichas de un jugador de JSONArray a String
    private static String parseFichasToString(JSONArray fichas) {
        StringBuilder fichasStr = new StringBuilder();
        for (int i = 0; i < fichas.length(); i++) {
            fichasStr.append(fichas.getString(i)).append(" ");
        }
        return fichasStr.toString().trim();
    }




}

