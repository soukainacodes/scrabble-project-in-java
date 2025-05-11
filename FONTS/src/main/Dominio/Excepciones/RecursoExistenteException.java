package Dominio.Excepciones;

public class RecursoExistenteException extends Exception {
    /**
     * Excepción lanzada cuando se intenta añadir un recurso que ya existe.
     * Esta excepción se utiliza para indicar que el sistema no puede crear un nuevo recurso
     * porque ya existe otro con el mismo ID.
     */
    public RecursoExistenteException() {
        super("El recurso ya existe.");
    }

    /**
     * Construye una nueva RecursoExistenteException con un mensaje personalizado.
     * El mensaje personalizado incluye el ID del recurso que ya existe.
     *
     * @param id el ID del recurso que causó la excepción.
     */
    public RecursoExistenteException(String id) {
        super(String.format("Ya existe un recurso con ID '%s'.", id));
    }
    
}
