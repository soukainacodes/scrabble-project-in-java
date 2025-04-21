package Dominio.Excepciones;

/**
 * Se lanza cuando se intenta añadir un diccionario con un ID ya existente.
 */
public class DiccionarioYaExistenteException extends Exception {
    public DiccionarioYaExistenteException() {
        super("El diccionario ya existe.");
    }

    public DiccionarioYaExistenteException(String id) {
        super(String.format("Ya existe un diccionario con ID '%s'.", id));
    }
}
