package Dominio.Excepciones;

/**
 * Se lanza cuando no se encuentra un usuario con el nombre dado.
 */
public class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException() {
        super("Usuario no encontrado.");
    }

    public UsuarioNoEncontradoException(String nombre) {
        super(String.format("Usuario con nombre '%s' no encontrado.", nombre));
    }
}
