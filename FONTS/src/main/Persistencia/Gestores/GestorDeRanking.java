package Persistencia.Gestores;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.json.JSONObject;
import Persistencia.Gestores.Utilidades.Utils;

import Dominio.Excepciones.UsuarioNoEncontradoException;

/**
 * Clase GestorDeRanking.
 * Esta clase se encarga de gestionar el ranking de jugadores basado en sus puntuaciones.
 * Permite generar un ranking y obtener la posición de un jugador específico.
 */
public class GestorDeRanking {


    /**
     * Ruta donde se encuentran los archivos de los jugadores.
     */
    private static final String JUGADORES = "FONTS/src/main/Persistencia/Datos/Jugadores/";

    /**
     * Constructor de la clase GestorDeRanking.
     * Inicializa el gestor de ranking.
    */
    public GestorDeRanking() {
        // Constructor vacío
    }


    /**
     * Genera un ranking de jugadores basado en sus puntuaciones.
     * 
     * @return Lista de pares (nombre, puntuación) ordenada por puntuación
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
     * 
     * @param name Nombre del jugador
     * @return Posición del jugador en el ranking
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws IOException                  Si ocurre un error al acceder a los archivos
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
    
}
