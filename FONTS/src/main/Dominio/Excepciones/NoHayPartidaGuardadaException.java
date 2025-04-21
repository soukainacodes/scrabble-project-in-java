package Dominio.Excepciones;

/**
 * Se lanza cuando se intenta cargar la última partida guardada pero no existe ninguna.
 */
public class NoHayPartidaGuardadaException extends Exception {
    public NoHayPartidaGuardadaException() {
        super("No hay ninguna partida guardada recientemente.");
    }
}
