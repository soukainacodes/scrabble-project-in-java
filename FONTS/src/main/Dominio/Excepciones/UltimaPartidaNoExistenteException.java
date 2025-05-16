package Dominio.Excepciones;

public class UltimaPartidaNoExistenteException extends Exception {
    /**
     * Excepción lanzada cuando se intenta acceder a la última partida guardada y no existe.
     * Esta excepción se utiliza para indicar que el sistema no puede recuperar la última partida
     * porque no hay ninguna partida guardada.
     */
    public UltimaPartidaNoExistenteException() {
        super("No existe ninguna última partida guardada.");
    }

    
}
