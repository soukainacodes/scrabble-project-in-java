package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se detecta un comando inválido.
 * Esta excepción se utiliza para indicar que el comando proporcionado
 * no es válido o no puede ser procesado por el sistema.
 */
public class ComandoInvalidoException extends Exception {

    /**
     * Construye una nueva ComandoInvalidoException con un mensaje predeterminado.
     * El mensaje predeterminado indica que el comando es inválido.
     */
    public ComandoInvalidoException() {
        super("Comando inválido.");
    }

    /**
     * Construye una nueva ComandoInvalidoException con un mensaje personalizado.
     * El mensaje personalizado incluye detalles adicionales sobre el comando inválido.
     *
     * @param detalle información adicional sobre el comando inválido.
     */
    public ComandoInvalidoException(String detalle) {
        super("Comando inválido: " + detalle);
    }
}
