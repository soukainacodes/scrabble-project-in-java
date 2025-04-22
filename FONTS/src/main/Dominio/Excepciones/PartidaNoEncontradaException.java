package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta acceder o eliminar una partida que no existe.
 */
public class PartidaNoEncontradaException extends Exception {

    /**
     * Construye una nueva PartidaNoEncontradaException con un mensaje predeterminado.
     * El mensaje predeterminado indica que la partida no existe.
     */
    public PartidaNoEncontradaException() {
        super("La partida no existe.");
    }

    /**
     * Construye una nueva PartidaNoEncontradaException con un mensaje personalizado.
     * El mensaje personalizado incluye el ID de la partida que no se encontró.
     *
     * @param id el ID de la partida que causó la excepción.
     */
    public PartidaNoEncontradaException(String id) {
        super(String.format("No existe ninguna partida con ID '%s'.", id));
    }
}
