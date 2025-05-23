package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta acceder a un recurso que no existe.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta cargar un recurso con un identificador que no existe en el sistema</li>
 *   <li>Se intenta utilizar un recurso que ha sido eliminado</li>
 * </ul>
 * 
 */
public class RecursoNoExistenteException extends Exception {
    /**
     * Construye una nueva RecursoNoExistenteException con un mensaje predeterminado.
     * El mensaje predeterminado indica que el recurso no existe.
     */
    public RecursoNoExistenteException() {
        super("El recurso no existe.");
    }

    /**
     * Construye una nueva RecursoNoExistenteException con un mensaje personalizado.
     * El mensaje personalizado incluye el ID del recurso que no existe.
     *
     * @param id el ID del recurso que causó la excepción.
     */
    public RecursoNoExistenteException(String id) {
        super(String.format("No existe un recurso con ID '%s'.", id));
    }
    
}