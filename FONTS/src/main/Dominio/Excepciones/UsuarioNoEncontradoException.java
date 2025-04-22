package Dominio.Excepciones;

/**
 * Excepción lanzada cuando no se encuentra un usuario con el nombre dado.
 */
public class UsuarioNoEncontradoException extends Exception {

    /**
     * Construye una nueva UsuarioNoEncontradoException con un mensaje predeterminado.
     * El mensaje predeterminado indica que el usuario no fue encontrado.
     */
    public UsuarioNoEncontradoException() {
        super("Usuario no encontrado.");
    }

    /**
     * Construye una nueva UsuarioNoEncontradoException con un mensaje personalizado.
     * El mensaje personalizado incluye el nombre del usuario que no fue encontrado.
     *
     * @param nombre el nombre del usuario que causó la excepción.
     */
    public UsuarioNoEncontradoException(String nombre) {
        super(String.format("Usuario con nombre '%s' no encontrado.", nombre));
    }
}
