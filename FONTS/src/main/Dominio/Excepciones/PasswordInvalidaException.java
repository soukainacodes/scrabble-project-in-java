package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se proporciona una contraseña incorrecta.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>El usuario introduce una contraseña que no coincide con la almacenada</li>
 *   <li>Se intenta realizar una operación protegida con credenciales incorrectas</li>
 * </ul>
 * 
 */
public class PasswordInvalidaException extends Exception {

    /**
     * Construye una nueva PasswordInvalidaException con un mensaje predeterminado.
     * El mensaje predeterminado indica que la contraseña es incorrecta.
     */
    public PasswordInvalidaException() {
        super("Contraseña incorrecta. Operación cancelada.");
    }
    
    /**
     * Construye una nueva PasswordInvalidaException con un mensaje personalizado.
     * El mensaje personalizado permite especificar detalles adicionales sobre el error.
     *
     * @param mensaje descripción específica del problema con la contraseña.
     */
    public PasswordInvalidaException(String mensaje) {
        super(mensaje);
    }
}