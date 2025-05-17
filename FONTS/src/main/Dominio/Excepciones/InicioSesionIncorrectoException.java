package Dominio.Excepciones;

public class InicioSesionIncorrectoException extends Exception {
    /**
     * Constructor de la excepción InicioSesionIncorrectoException.
     * Se lanza cuando el inicio de sesión es incorrecto.
     */
    public InicioSesionIncorrectoException() {
        super("Inicio de sesión incorrecto.");
    }

    /**
     * Constructor de la excepción InicioSesionIncorrectoException con un mensaje personalizado.
     *
     * @param mensaje Mensaje personalizado para la excepción.
     */
    public InicioSesionIncorrectoException(String mensaje) {
        super(mensaje);
    }
    
}
