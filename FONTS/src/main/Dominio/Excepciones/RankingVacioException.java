package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta obtener un ranking pero está vacío.
 */
public class RankingVacioException extends Exception {

    /**
     * Construye una nueva RankingVacioException con un mensaje predeterminado.
     * El mensaje predeterminado indica que no hay jugadores con puntuación en el ranking.
     */
    public RankingVacioException() {
        super("No hay jugadores con puntuación en el ranking.");
    }
}
