package Dominio.Excepciones;

/**
 * Excepción que se lanza cuando se detecta un formato inválido en el diccionario.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>La estructura del diccionario no cumple con el formato esperado</li>
 *   <li>Las palabras contenidas en el diccionario tienen un formato incorrecto</li>
 *   <li>El archivo del diccionario está corrupto o no puede ser leído correctamente</li>
 * </ul>
 * 
 */
public class FormatoDiccionarioInvalidoException extends Exception {

    /**
     * Constructor predeterminado que inicializa la excepción con un mensaje estándar.
     * Este constructor se utiliza cuando no se necesita especificar detalles adicionales
     * sobre el error de formato en el diccionario.
     */
    public FormatoDiccionarioInvalidoException() {
        super("El formato del diccionario es inválido");
    }

    /**
     * Constructor que permite personalizar el mensaje de error.
     * 
     * @param mensaje Descripción detallada del problema encontrado en el formato
     *                del diccionario. Este mensaje debe proporcionar información específica
     *                sobre qué aspecto del formato es incorrecto.
     */
    public FormatoDiccionarioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
