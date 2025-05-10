import org.json.JSONObject;

import org.json.JSONArray;

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

    // Función auxiliar para convertir las fichas de un jugador de JSONArray a String
    private static String parseFichasToString(JSONArray fichas) {
        StringBuilder fichasStr = new StringBuilder();
        for (int i = 0; i < fichas.length(); i++) {
            fichasStr.append(fichas.getString(i)).append(" ");
        }
        return fichasStr.toString().trim();
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

    public static void main(String[] args) {
            Persistencia persistencia = new Persistencia();
            Path partidaTxtPath = Paths.get("partida_definitiva.txt");
            Path partidaJsonPath = Paths.get("partida_definitiva.json");
            Path testPartidaTxtPath = Paths.get("test_partida.txt"); // Path for the new .txt file
    
            try {
                System.out.println("=== Test de Conversión de partida_definitiva.txt a JSON y guardado ===\n");
    
                if (Files.exists(partidaTxtPath)) {
                    // Read all lines from the .txt file into a List<String>
                    List<String> partidaData = Files.readAllLines(partidaTxtPath, StandardCharsets.UTF_8);
    
                    if (partidaData.isEmpty()) {
                        System.err.println("El archivo partida_definitiva.txt está vacío.");
                        return;
                    }
    
                    System.out.println("Contenido de partida_definitiva.txt (línea por línea):");
                    for (int i = 0; i < partidaData.size(); i++) {
                        System.out.println("Línea " + i + ": " + partidaData.get(i));
                    }
                    System.out.println("\n--- Fin del contenido del archivo ---\n");
    
                    // Convert the List<String> to a JSON string
                    String jsonOutput = Persistencia.partidaListToJson(partidaData);
    
                    System.out.println("JSON generado a partir de partida_definitiva.txt:");
                    System.out.println(jsonOutput);
    
                    // Guardar el JSON en un nuevo archivo
                    try (BufferedWriter writer = Files.newBufferedWriter(partidaJsonPath, StandardCharsets.UTF_8)) {
                        writer.write(jsonOutput);
                        System.out.println("\nJSON guardado exitosamente en: " + partidaJsonPath.toAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("Error al guardar el archivo JSON: " + e.getMessage());
                        e.printStackTrace();
                        return; // Salir si no se pudo guardar el JSON
                    }
    
                    // === Nueva sección: Convertir JSON de vuelta a TXT y guardar ===
                    System.out.println("\n=== Test de Conversión de " + partidaJsonPath.getFileName() + " a TXT y guardado ===\n");
                    try {
                        // Leer el contenido del archivo JSON generado
                        String jsonInput = Files.readString(partidaJsonPath, StandardCharsets.UTF_8);
    
                        // Convertir el JSON string a List<String>
                        List<String> partidaDataFromJson = Persistencia.jsonToPartidaList(jsonInput);
    
                        System.out.println("Contenido de " + testPartidaTxtPath.getFileName() + " (generado desde JSON):");
                        for (int i = 0; i < partidaDataFromJson.size(); i++) {
                            System.out.println("Línea " + i + ": " + partidaDataFromJson.get(i));
                        }
    
                        // Guardar la List<String> en test_partida.txt
                        try (BufferedWriter writer = Files.newBufferedWriter(testPartidaTxtPath, StandardCharsets.UTF_8)) {
                            for (String line : partidaDataFromJson) {
                                writer.write(line);
                                writer.newLine();
                            }
                            System.out.println("\nArchivo " + testPartidaTxtPath.getFileName() + " guardado exitosamente en: " + testPartidaTxtPath.toAbsolutePath());
                        } catch (IOException e) {
                            System.err.println("Error al guardar el archivo " + testPartidaTxtPath.getFileName() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
    
                    } catch (IOException e) {
                        System.err.println("Error de E/S al leer el archivo " + partidaJsonPath.getFileName() + ": " + e.getMessage());
                        e.printStackTrace();
                    } catch (org.json.JSONException e) {
                        System.err.println("Error al parsear el JSON desde " + partidaJsonPath.getFileName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                    // === Fin de la nueva sección ===
    
                } else {
                    System.err.println("Error: El archivo partida_definitiva.txt no se encontró en "
                            + partidaTxtPath.toAbsolutePath());
                }
    
            } catch (IOException e) {
                System.err.println("Error de E/S al leer el archivo partida_definitiva.txt: " + e.getMessage());
                e.printStackTrace();
            } catch (org.json.JSONException e) {
                System.err.println("Error al generar el JSON: " + e.getMessage());
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                System.err.println(
                        "Error: El formato de partida_definitiva.txt no coincide con el esperado por partidaListToJson. "
                                + e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.err.println("Error: No se pudo convertir un número en partida_definitiva.txt. " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
}














// Clases de excepciones
class UsuarioYaRegistradoException extends Exception {
    public UsuarioYaRegistradoException(String username) {
        super("El usuario ya está registrado: " + username);
    }
}

class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException(String username) {
        super("El usuario no fue encontrado: " + username);
    }
}

class PuntuacionInvalidaException extends Exception {
    public PuntuacionInvalidaException(int puntuacion) {
        super("Puntuación inválida: " + puntuacion);
    }
}

class PartidaYaExistenteException extends Exception   {
    public PartidaYaExistenteException(String id) {
        super(String.format("Ya existe una partida con ID '%s'.", id));
    }
}
