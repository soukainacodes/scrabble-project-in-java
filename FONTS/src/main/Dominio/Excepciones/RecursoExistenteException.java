package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta añadir un recurso que ya existe.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta crear un nuevo recurso con un identificador ya registrado</li>
 *   <li>Se intenta importar un recurso con un nombre que colisiona con uno existente</li>
 * </ul>
 * 
 */
public class RecursoExistenteException extends Exception {
    /**
     * Construye una nueva RecursoExistenteException con un mensaje predeterminado.
     * El mensaje predeterminado indica que el recurso ya existe.
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