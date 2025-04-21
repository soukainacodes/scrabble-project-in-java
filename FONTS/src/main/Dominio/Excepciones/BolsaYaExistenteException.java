package Dominio.Excepciones;

/**
 * Se lanza cuando se intenta añadir una bolsa con un ID ya existente.
 */
public class BolsaYaExistenteException extends Exception {
    public BolsaYaExistenteException() {
        super("La bolsa ya existe.");
    }

    public BolsaYaExistenteException(String id) {
        super(String.format("Ya existe una bolsa con ID '%s'.", id));
    }
}
