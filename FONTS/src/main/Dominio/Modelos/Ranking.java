package Dominio.Modelos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import Dominio.Excepciones.*;

/**
 * Clase modelo Ranking.
 * <p>
 * Esta clase gestiona el ranking de jugadores mediante un HashMap donde se asocia
 * el nombre del jugador con su puntuación. Se proveen métodos para agregar, actualizar
 * y obtener el ranking ordenado (de mayor a menor), además de getters y setters para
 * el registro interno.
 * </p>
 */
public class Ranking {

    /** Registro de jugadores y sus puntuaciones */
    private HashMap<String, Integer> registro;

    /**
     * Constructor por defecto.
     * <p>
     * Inicializa la estructura interna {@code HashMap} para almacenar el ranking.
     * </p>
     */
    public Ranking() {
        this.registro = new HashMap<>();
    }

    /**
     * Agrega un jugador con su puntuación o actualiza la existente si la nueva puntuación es mayor.
     * <p>
     * Si el jugador no existe, se añade con la puntuación dada. Si existe, solo se actualiza si
     * la nueva puntuación es mayor que la actual.
     * </p>
     *
     * @param nombre     Nombre del jugador.
     * @param puntuacion Puntuación del jugador.
     */
    public void agregarJugador(String nombre, int puntuacion) {
        int actual = registro.getOrDefault(nombre, -1);
        if (puntuacion > actual) {
            registro.put(nombre, puntuacion);
        }
    }

    /**
     * Actualiza la puntuación de un jugador existente.
     * <p>
     * Si el jugador no se encuentra en el registro, se lanza una excepción
     * {@link JugadorNoEncontradoException}. Solo se actualiza si la nueva puntuación es mayor.
     * </p>
     *
     * @param nombre     Nombre del jugador.
     * @param puntuacion Nueva puntuación.
     * @throws JugadorNoEncontradoException Si el jugador no existe en el registro.
     */
    public void actualizarPuntuacion(String nombre, int puntuacion) throws JugadorNoEncontradoException {
        if (!registro.containsKey(nombre)) {
            throw new JugadorNoEncontradoException(nombre);
        }
        int actual = registro.get(nombre);
        if (puntuacion > actual) {
            registro.put(nombre, puntuacion);
        }
    }

    /**
     * Obtiene la puntuación de un jugador.
     *
     * @param nombre Nombre del jugador.
     * @return Puntuación del jugador o {@code null} si no existe.
     */
    public Integer getPuntuacion(String nombre) {
        return registro.get(nombre);
    }

    /**
     * Comprueba si el registro contiene al jugador.
     *
     * @param nombre Nombre del jugador.
     * @return {@code true} si el jugador se encuentra en el registro; {@code false} en caso contrario.
     */
    public boolean contieneJugador(String nombre) {
        return registro.containsKey(nombre);
    }

    /**
     * Obtiene una lista del ranking ordenado de mayor a menor puntuación,
     * incluyendo únicamente a aquellos con puntuación superior a 0.
     *
     * @return Lista filtrada y ordenada de entradas (nombre, puntuación).
     */
    public List<Entry<String, Integer>> obtenerRankingOrdenado() {
        return registro.entrySet().stream()
                // Filtrar sólo puntuaciones mayores que cero
                .filter(e -> e.getValue() > 0)
                // Ordenar de mayor a menor
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }
    /**
     * Retorna una copia del registro interno.
     * <p>
     * Se devuelve una copia para preservar el encapsulamiento del registro original.
     * </p>
     *
     * @return Copia del {@code HashMap} con los datos del ranking.
     */
    public HashMap<String, Integer> getRegistro() {
        return new HashMap<>(registro);
    }

    /**
     * Establece el registro de jugadores con los datos proporcionados.
     * <p>
     * Este método permite recargar la estructura de datos con un nuevo conjunto de información.
     * </p>
     *
     * @param datos {@code HashMap} con los datos a asignar.
     */
    public void setRegistro(HashMap<String, Integer> datos) {
        this.registro = datos;
    }
}
