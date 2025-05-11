import org.json.JSONObject;

import Dominio.Excepciones.PuntuacionInvalidaException;
import Dominio.Excepciones.UsuarioNoEncontradoException;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class Persistencia {

    private String ultimaPartida;

    public boolean existeJugador(String username) {
        File userDir = new File("FONTS/src/main/Persistencia/Datos/Jugadores/" + username);
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
        jugadorJson.put("username", username);
        jugadorJson.put("password", password);
        jugadorJson.put("maxpoints", 0);

        // Escribir el JSON en el archivo
        try (BufferedWriter writer = Files.newBufferedWriter(userFile, StandardCharsets.UTF_8)) {
            writer.write(jugadorJson.toString(4));
        }
    }

    public void eliminarJugador(String username) throws UsuarioNoEncontradoException, IOException {
        File userDir = new File("FONTS/src/main/Persistencia/Datos/Jugadores/" + username);

        if (!userDir.exists() || !userDir.isDirectory()) {
            throw new UsuarioNoEncontradoException(username);
        }

        File[] archivos = userDir.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (!archivo.delete()) {
                    throw new IOException("No se pudo eliminar el archivo: " + archivo.getName());
                }
            }
        }

        if (!userDir.delete()) {
            throw new IOException("No se pudo eliminar la carpeta del jugador: " + username);
        }
    }

    public void actualizarContrasena(String username, String newPass) throws UsuarioNoEncontradoException {
        Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");

        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);
            jugadorJson.put("password", newPass);

            Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    public void actualizarPuntuacion(String username, int pts)
            throws UsuarioNoEncontradoException, PuntuacionInvalidaException {
        if (pts < 0) {
            throw new PuntuacionInvalidaException(pts);
        }

        int maxpointsActual = getPuntuacion(username);

        if (pts > maxpointsActual) {
            Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");

            try {
                String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
                JSONObject jugadorJson = new JSONObject(contenido);
                jugadorJson.put("maxpoints", pts);

                Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
            }
        }
    }

    public int getPuntuacion(String username) throws UsuarioNoEncontradoException {
        Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");

        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);
            return jugadorJson.getInt("maxpoints");
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    public List<Map.Entry<String, Integer>> generarRanking() throws IOException {
        Path jugadoresDir = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores");
        List<Map.Entry<String, Integer>> ranking = new ArrayList<>();

        if (Files.exists(jugadoresDir) && Files.isDirectory(jugadoresDir)) {
            try (Stream<Path> paths = Files.list(jugadoresDir)) {
                paths.filter(Files::isDirectory).forEach(dir -> {
                    Path userFile = dir.resolve(dir.getFileName() + ".json");
                    if (Files.exists(userFile)) {
                        try {
                            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
                            JSONObject jugadorJson = new JSONObject(contenido);
                            String username = jugadorJson.getString("username");
                            int maxpoints = jugadorJson.getInt("maxpoints");
                            ranking.add(Map.entry(username, maxpoints));
                        } catch (IOException e) {
                            System.err.println("Error al procesar el archivo de jugador: " + userFile);
                        }
                    }
                });
            }
        }

        ranking.sort((e1, e2) -> {
            int cmp = Integer.compare(e2.getValue(), e1.getValue());
            if (cmp == 0) {
                return e1.getKey().compareTo(e2.getKey());
            }
            return cmp;
        });

        return ranking;
    }

    public int obtenerPosicion(String name) throws UsuarioNoEncontradoException, IOException {
        List<Map.Entry<String, Integer>> ranking = generarRanking();

        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getKey().equals(name)) {
                return i + 1;
            }
        }

        throw new UsuarioNoEncontradoException("El jugador no fue encontrado en el ranking: " + name);
    }

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

    // Función auxiliar para convertir las fichas de un jugador de JSONArray a
    //
    // String
    private static String parseFichasToString(JSONArray fichas) {
        StringBuilder fichasStr = new StringBuilder();
        for (int i = 0; i < fichas.length(); i++) {
            fichasStr.append(fichas.getString(i)).append(" ");
        }
        return fichasStr.toString().trim();
    }

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
        String rutaArchivo = nombreArchivo;

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
        String rutaArchivo = "partida_" + id + ".json";
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
            throw new PartidaNoEncontradaException(ultimaPartida);
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
        File partidaFile = new File("partida_" + id + ".json");
        if (partidaFile.delete()) {
            System.out.println("Partida eliminada: " + partidaFile.getPath());
        } else {
            System.out.println("No se pudo eliminar la partida.");
        }
    }

    public List<String> listarPartidasNoAcabadas() throws PartidaNoEncontradaException {
        List<String> partidasNoAcabadas = new ArrayList<>();
        File dir = new File("/mnt/c/Users/souka/Desktop/PROP_Entrega1_Vinent_Mahboub");

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

// Closing brace for the Persistencia class
}
