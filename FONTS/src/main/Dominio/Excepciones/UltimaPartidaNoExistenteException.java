package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta acceder a la última partida guardada y no existe.
 *
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta cargar la última partida pero no hay ninguna en el registro</li>
 * </ul>
 *
 */
public class UltimaPartidaNoExistenteException extends Exception {
    
    /**
     * Construye una nueva UltimaPartidaNoExistenteException con un mensaje predeterminado.
     * El mensaje predeterminado indica que no existe ninguna última partida guardada.
     */
    public UltimaPartidaNoExistenteException() {
        super("No existe ninguna última partida guardada.");
    }

    /**
     * Construye una nueva UltimaPartidaNoExistenteException con un mensaje personalizado.
     * El mensaje personalizado permite proporcionar detalles adicionales sobre el error.
     *
     * @param mensaje descripción específica del problema al intentar acceder a la última partida.
     */
    public UltimaPartidaNoExistenteException(String mensaje) {
        super(mensaje);
    }
    
}