package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta añadir un diccionario con un ID ya existente.
 * Esta excepción se utiliza para indicar que el sistema no puede crear un nuevo
 * diccionario porque ya existe otro con el mismo ID.
 */
public class DiccionarioYaExistenteException extends Exception {

    /**
     * Construye una nueva DiccionarioYaExistenteException con un mensaje predeterminado.
     * El mensaje predeterminado indica que el diccionario ya existe.
     */
    public DiccionarioYaExistenteException() {
        super("El diccionario ya existe.");
    }

    /**
     * Construye una nueva DiccionarioYaExistenteException con un mensaje personalizado.
     * El mensaje personalizado incluye el ID del diccionario que ya existe.
     *
     * @param id el ID del diccionario que causó la excepción.
     */
    public DiccionarioYaExistenteException(String id) {
        super(String.format("Ya existe un diccionario con ID '%s'.", id));
    }
}
