package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta registrar un usuario que ya existe en el sistema.
 */
public class UsuarioYaRegistradoException extends Exception {

    /**
     * Construye una nueva excepción UsuarioYaRegistradoException con un mensaje personalizado.
     *
     * @param nombre el nombre del usuario que ya está registrado.
     */
    public UsuarioYaRegistradoException(String nombre) {
        super("El usuario ya existe: " + nombre);
    }
}
