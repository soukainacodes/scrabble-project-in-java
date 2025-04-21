package Dominio.Excepciones;

/**
 * Se lanza cuando se intenta obtener un ranking pero está vacío.
 */
public class RankingVacioException extends Exception {
    public RankingVacioException() {
        super("No hay jugadores con puntuación en el ranking.");
    }
}
