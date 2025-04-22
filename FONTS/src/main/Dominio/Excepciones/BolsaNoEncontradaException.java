package Dominio.Excepciones;

/**
 * Excepción lanzada cuando no se encuentra una "Bolsa" (bolsa) solicitada.
 * Esta excepción se utiliza para indicar que el sistema no pudo localizar
 * el recurso de "Bolsa" requerido.
 */
public class BolsaNoEncontradaException extends Exception {
    private static final String DEFAULT_MSG = "Bolsa no encontrada.";

    /**
     * Construye una nueva BolsaNoEncontradaException con un mensaje predeterminado.
     */
    public BolsaNoEncontradaException() {
        super(DEFAULT_MSG);
    }

    /**
     * Construye una nueva BolsaNoEncontradaException con un mensaje personalizado.
     *
     * @param message el mensaje detallado que describe el error.
     */
    public BolsaNoEncontradaException(String message) {
        super(message);
    }
}
