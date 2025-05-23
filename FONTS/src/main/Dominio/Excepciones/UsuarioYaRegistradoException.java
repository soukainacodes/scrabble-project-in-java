package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta registrar un usuario que ya existe en el sistema.
 *
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta crear una cuenta con un nombre de usuario que ya está registrado</li>
 * </ul>
 * 
 */
public class UsuarioYaRegistradoException extends Exception {

    /**
     * Construye una nueva UsuarioYaRegistradoException con un mensaje predeterminado.
     * El mensaje predeterminado indica que ya existe un usuario con ese nombre.
     */
    public UsuarioYaRegistradoException() {
        super("El usuario ya está registrado en el sistema.");
    }

    /**
     * Construye una nueva excepción UsuarioYaRegistradoException con un mensaje personalizado.
     *
     * @param nombre el nombre del usuario que ya está registrado.
     */
    public UsuarioYaRegistradoException(String nombre) {
        super("El usuario ya existe: " + nombre);
    }
}