package Dominio.Excepciones;

/**
 * Se lanza cuando se intenta asignar una puntuación inválida (negativa).
 */
public class PuntuacionInvalidaException extends Exception {
    public PuntuacionInvalidaException() {
        super("La puntuación no puede ser negativa.");
    }

    public PuntuacionInvalidaException(int valor) {
        super("Puntuación inválida: " + valor + ". No puede ser negativa.");
    }
}
