package Dominio.Excepciones;

/**
 * Excepción que se lanza cuando se produce un error durante el inicio de sesión.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>El nombre de usuario introducido no existe en el sistema</li>
 *   <li>La contraseña proporcionada no coincide con la almacenada</li>
 * </ul>
 * 
 */
public class InicioSesionIncorrectoException extends Exception {
    /**
     * Constructor predeterminado que inicializa la excepción con un mensaje estándar.
     * Este constructor se utiliza cuando no se necesita especificar detalles adicionales
     * sobre el error de inicio de sesión.
     */
    public InicioSesionIncorrectoException() {
        super("Inicio de sesión incorrecto.");
    }

    /**
     * Constructor que permite personalizar el mensaje de error.
     * 
     * @param mensaje Descripción detallada del problema encontrado durante el inicio 
     *                de sesión. Este mensaje debe proporcionar información específica
     *                sobre qué aspecto de la autenticación ha fallado.
     */
    public InicioSesionIncorrectoException(String mensaje) { 
        super(mensaje);
    }
    
}