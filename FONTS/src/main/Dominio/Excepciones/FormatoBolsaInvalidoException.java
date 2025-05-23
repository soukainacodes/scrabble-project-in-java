package Dominio.Excepciones;

/**
 * Excepción que se lanza cuando se detecta un formato inválido en la bolsa de fichas.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>La estructura de la bolsa no cumple con el formato esperado</li>
 * </ul>
 * 
 * 
 */
public class FormatoBolsaInvalidoException extends Exception {

    /**
     * Constructor predeterminado que inicializa la excepción con un mensaje estándar.
     * Este constructor se utiliza cuando no se necesita especificar detalles adicionales
     * sobre el error de formato.
     */
    public FormatoBolsaInvalidoException() {
        super("El formato de la bolsa es inválido");
    }

    /**
     * Constructor que permite personalizar el mensaje de error.
     * 
     * @param mensaje Descripción detallada del problema encontrado en el formato
     *                de la bolsa. Este mensaje debe proporcionar información específica
     *                sobre qué aspecto del formato es incorrecto.
     */
    public FormatoBolsaInvalidoException(String mensaje) {
        super(mensaje);
    }
}