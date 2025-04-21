package Dominio.Excepciones;

/**
 * Se lanza cuando se intenta guardar una partida con un ID que ya existe.
 */
public class PartidaYaExistenteException extends Exception {
    public PartidaYaExistenteException() {
        super("Ya existe una partida con ese identificador.");
    }
    public PartidaYaExistenteException(String id) {
        super(String.format("Ya existe una partida con ID '%s'.", id));
    }
}
