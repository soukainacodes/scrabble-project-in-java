package Persistencia;

import java.io.*;
import java.util.List;
import java.util.Map;

import Dominio.Modelos.Ranking;

/**
 * Controlador de Persistencia para el Ranking.
 * <p>
 * Esta clase gestiona la carga y el guardado del ranking en un fichero de texto.
 * El formato de cada línea será: "nombre_de_jugador puntuacion_jugador".
 * </p>
 */
public class CtrlPersistencia {
    
    /** Ruta del fichero donde se persiste el ranking */
    private static final String FILE_NAME = "FONTS/src/main/Persistencia/Datos/ranking.txt";
    
    /**
     * Guarda el ranking en un fichero de texto.
     * <p>
     * Se recorre la lista ordenada del ranking y se escribe cada línea con el formato
     * "nombre_de_jugador puntuacion_jugador".
     * </p>
     *
     * @param ranking Instancia del ranking que se desea guardar.
     */
    public void guardarRanking(Ranking ranking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            List<Map.Entry<String, Integer>> listaOrdenada = ranking.obtenerRankingOrdenado();
            for (Map.Entry<String, Integer> entry : listaOrdenada) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
            // Crear un objeto File para poder obtener la ruta absoluta
            File file = new File(FILE_NAME);
            System.out.println("Guardando ranking en: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al guardar el ranking: " + e.getMessage());
        }
    }
    
    /**
     * Carga el ranking desde un fichero de texto.
     * <p>
     * Se espera que cada línea tenga el formato: "nombre_de_jugador puntuacion_jugador".
     * Se procesa cada línea dividiéndola por el espacio y se agrega la información al ranking.
     * </p>
     *
     * @param ranking Instancia del ranking en la que se agregará la información cargada.
     */
    public void cargarRanking(Ranking ranking) {
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
                // Se espera el formato: "nombre_de_jugador puntuacion_jugador"
                String[] tokens = line.split(" ");
                if (tokens.length < 2) continue;
                String nombre = tokens[0].trim();
                int puntuacion = Integer.parseInt(tokens[1].trim());
                ranking.agregarJugador(nombre, puntuacion);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar el ranking: " + e.getMessage());
        }
    }
}
