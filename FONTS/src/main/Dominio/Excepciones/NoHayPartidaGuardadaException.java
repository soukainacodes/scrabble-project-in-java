package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta cargar la última partida guardada pero no existe ninguna.
 */
public class NoHayPartidaGuardadaException extends Exception {

    /**
     * Construye una nueva NoHayPartidaGuardadaException con un mensaje predeterminado.
     * El mensaje predeterminado indica que no hay partidas guardadas recientemente.
     */
    public NoHayPartidaGuardadaException() {
        super("No hay ninguna partida guardada recientemente.");
    }

}
