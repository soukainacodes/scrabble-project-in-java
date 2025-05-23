package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta cargar la última partida guardada pero no existe ninguna.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>El usuario intenta continuar una partida pero no hay ninguna guardada</li>
 *   <li>Se solicita información sobre la última partida y esta no existe</li>
 *   <li>El archivo de partida guardada ha sido eliminado o está corrupto</li>
 * </ul>
 * 
 */
public class NoHayPartidaGuardadaException extends Exception {

    /**
     * Construye una nueva NoHayPartidaGuardadaException con un mensaje predeterminado.
     * El mensaje predeterminado indica que no hay partidas guardadas recientemente.
     */
    public NoHayPartidaGuardadaException() {
        super("No hay ninguna partida guardada recientemente.");
    }

    /**
     * Constructor que permite personalizar el mensaje de error.
     * 
     * @param mensaje Descripción detallada del problema encontrado al intentar
     *                cargar una partida. Este mensaje debe proporcionar información
     *                específica sobre por qué no se pudo encontrar la partida guardada.
     */
    public NoHayPartidaGuardadaException(String mensaje) {
        super(mensaje);
    }
}