

import java.io.*;
import java.util.List;
import java.util.Map;

import Dominio.Excepciones.*;
import Dominio.Modelos.*;

/**
 * Controlador del Ranking.
 * <p>
 * Esta clase se encarga de la interacción entre la capa de presentación y el modelo Ranking.
 * Gestiona la carga y el guardado del ranking en un fichero de texto, y delega operaciones básicas
 * (agregar, actualizar y mostrar) al modelo.
 * </p>
 */
public class CtrlRanking {
    
    /** Ruta del fichero donde se persiste el ranking */
    private static final String FILE_NAME = "ranking.txt";
    
    /** Modelo del ranking */
    private Ranking ranking;
    
    /**
     * Constructor de CtrlRanking.
     * <p>
     * Se carga el ranking desde el fichero de persistencia. Si el fichero no existe o ocurre algún
     * error al cargar, se instancia un nuevo Ranking.
     * </p>
     */
    public CtrlRanking() {
        ranking = new Ranking();
        cargarRanking();
    }
    
    /**
     * Agrega un jugador al ranking.
     * <p>
     * Invoca el método {@code agregarJugador} del modelo y posteriormente guarda el ranking.
     * </p>
     *
     * @param nombre     Nombre del jugador.
     * @param puntuacion Puntuación del jugador.
     */
    public void crearJugador(String nombre, int puntuacion) {
        ranking.agregarJugador(nombre, puntuacion);
        guardarRanking();
    }
    
    /**
     * Actualiza la puntuación de un jugador existente.
     * <p>
     * Se invoca el método {@code actualizarPuntuacion} del modelo. Si el jugador no se encuentra,
     * se lanza la excepción {@link JugadorNoEncontradoException} y se muestra el mensaje de error.
     * Luego de actualizar, se guarda el ranking.
     * </p>
     *
     * @param nombre     Nombre del jugador.
     * @param puntuacion Nueva puntuación del jugador.
     */
    public void modificarRanking(String nombre, int puntuacion) {
        try {
            ranking.actualizarPuntuacion(nombre, puntuacion);
            guardarRanking();
        } catch (JugadorNoEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Muestra en consola el ranking ordenado de mayor a menor puntuación.
     * <p>
     * Se obtiene la lista ordenada del modelo y se imprime cada entrada junto con su posición.
     * </p>
     */
    public void mostrarRanking() {
        List<Map.Entry<String, Integer>> listaOrdenada = ranking.obtenerRankingOrdenado();
        int pos = 1;
        for (Map.Entry<String, Integer> entry : listaOrdenada) {
            System.out.println(pos + ". " + entry.getKey() + " - " + entry.getValue());
            pos++;
        }
    }
    
    /**
     * Guarda el ranking en un fichero de texto.
     * <p>
     * Se escribe en el fichero especificado por {@code FILE_NAME} la lista ordenada del ranking,
     * utilizando el formato "posición. nombre - puntuación" en cada línea.
     * </p>
     */
    public void guardarRanking() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            List<Map.Entry<String, Integer>> listaOrdenada = ranking.obtenerRankingOrdenado();
            int pos = 1;
            for (Map.Entry<String, Integer> entry : listaOrdenada) {
                writer.write(pos + ". " + entry.getKey() + " - " + entry.getValue());
                writer.newLine();
                pos++;
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el ranking: " + e.getMessage());
        }
    }
    
    /**
     * Carga el ranking desde el fichero de texto.
     * <p>
     * Se lee el fichero especificado por {@code FILE_NAME}. Por cada línea válida se extrae el nombre
     * y la puntuación para agregarlos al modelo. Si el fichero no existe, se informa y se creará uno nuevo
     * al guardar.
     * </p>
     */
    public void cargarRanking() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No se encontró el fichero de ranking. Se creará uno nuevo al guardar.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // Se espera el formato: "posición. nombre - puntuación"
                String[] partes = line.split("\\. ", 2);
                if (partes.length < 2) continue;
                String datos = partes[1]; // "nombre - puntuación"
                String[] subPartes = datos.split(" - ");
                if (subPartes.length < 2) continue;
                String nombre = subPartes[0].trim();
                int puntuacion = Integer.parseInt(subPartes[1].trim());
                ranking.agregarJugador(nombre, puntuacion);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar el ranking: " + e.getMessage());
        }
    }
}
