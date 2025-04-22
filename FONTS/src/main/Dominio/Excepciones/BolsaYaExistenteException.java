package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta añadir una "Bolsa" (bolsa) con un ID ya existente.
 * Esta excepción se utiliza para indicar que el sistema no puede crear una nueva "Bolsa"
 * porque ya existe otra con el mismo ID.
 */
public class BolsaYaExistenteException extends Exception {

    /**
     * Construye una nueva BolsaYaExistenteException con un mensaje predeterminado.
     * El mensaje predeterminado indica que la "Bolsa" ya existe.
     */
    public BolsaYaExistenteException() {
        super("La bolsa ya existe.");
    }

    /**
     * Construye una nueva BolsaYaExistenteException con un mensaje personalizado.
     * El mensaje personalizado incluye el ID de la "Bolsa" que ya existe.
     *
     * @param id el ID de la "Bolsa" que causó la excepción.
     */
    public BolsaYaExistenteException(String id) {
        super(String.format("Ya existe una bolsa con ID '%s'.", id));
    }
}
