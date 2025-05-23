package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta asignar una puntuación inválida en el juego.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta asignar una puntuación negativa a un jugador</li>
 * </ul>
 *
 */
public class PuntuacionInvalidaException extends Exception {

    /**
     * Construye una nueva PuntuacionInvalidaException con un mensaje predeterminado.
     * El mensaje predeterminado indica que la puntuación no puede ser negativa.
     */
    public PuntuacionInvalidaException() {
        super("La puntuación no puede ser negativa.");
    }

    /**
     * Construye una nueva PuntuacionInvalidaException con un mensaje personalizado.
     * El mensaje personalizado incluye el valor de la puntuación inválida.
     *
     * @param valor el valor de la puntuación que causó la excepción.
     */
    public PuntuacionInvalidaException(int valor) {
        super("Puntuación inválida: " + valor + ". No puede ser negativa.");
    }
}