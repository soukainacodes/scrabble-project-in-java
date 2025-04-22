package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta guardar una partida con un ID que ya existe.
 */
public class PartidaYaExistenteException extends Exception {

    /**
     * Construye una nueva PartidaYaExistenteException con un mensaje predeterminado.
     * El mensaje predeterminado indica que ya existe una partida con el mismo identificador.
     */
    public PartidaYaExistenteException() {
        super("Ya existe una partida con ese identificador.");
    }

    /**
     * Construye una nueva PartidaYaExistenteException con un mensaje personalizado.
     * El mensaje personalizado incluye el ID de la partida que ya existe.
     *
     * @param id el ID de la partida que causó la excepción.
     */
    public PartidaYaExistenteException(String id) {
        super(String.format("Ya existe una partida con ID '%s'.", id));
    }
}
