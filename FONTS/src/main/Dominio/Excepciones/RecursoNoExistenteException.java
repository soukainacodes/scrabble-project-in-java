package Dominio.Excepciones;

public class RecursoNoExistenteException extends Exception {
    /**
     * Excepción lanzada cuando se intenta acceder a un recurso que no existe.
     * Esta excepción se utiliza para indicar que el sistema no puede encontrar un recurso
     * con el ID proporcionado.
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
