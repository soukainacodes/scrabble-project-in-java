package Dominio.Excepciones;

/**
 * Se lanza cuando se intenta acceder o eliminar una partida que no existe.
 */
public class PartidaNoEncontradaException extends Exception {
    public PartidaNoEncontradaException() {
        super("La partida no existe.");
    }

    public PartidaNoEncontradaException(String id) {
        super(String.format("No existe ninguna partida con ID '%s'.", id));
    }
}
