package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta obtener un ranking pero está vacío.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta mostrar un ranking de jugadores pero no hay ninguno registrado</li>
 * </ul>
 *
 */
public class RankingVacioException extends Exception {

    /**
     * Construye una nueva RankingVacioException con un mensaje predeterminado.
     * El mensaje predeterminado indica que no hay jugadores con puntuación en el ranking.
     */
    public RankingVacioException() {
        super("No hay jugadores con puntuación en el ranking.");
    }
    
    /**
     * Construye una nueva RankingVacioException con un mensaje personalizado.
     * Permite especificar detalles adicionales sobre por qué el ranking está vacío.
     *
     * @param mensaje descripción específica de por qué el ranking está vacío.
     */
    public RankingVacioException(String mensaje) {
        super(mensaje);
    }
}