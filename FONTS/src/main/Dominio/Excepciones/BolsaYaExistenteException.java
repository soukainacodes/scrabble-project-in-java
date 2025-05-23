package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta añadir una "Bolsa" (conjunto de fichas) con un ID ya existente.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta crear una nueva bolsa de fichas con un identificador ya registrado</li>
 *   <li>Se intenta importar una bolsa con un nombre que colisiona con una existente</li>
 * </ul>
 * 
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