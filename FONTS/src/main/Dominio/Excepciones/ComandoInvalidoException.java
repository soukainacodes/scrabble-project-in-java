package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se detecta un comando inválido.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se proporciona un comando que no existe en el sistema</li>
 *   <li>La sintaxis del comando es incorrecta o incompleta</li>
 *   <li>Se intenta ejecutar un comando en un contexto no permitido</li>
 *   <li>Los parámetros del comando no cumplen con los requisitos esperados</li>
 * </ul>
 * 
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