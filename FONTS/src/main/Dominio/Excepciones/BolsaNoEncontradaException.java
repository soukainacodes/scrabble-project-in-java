package Dominio.Excepciones;

/**
 * Excepción lanzada cuando no se encuentra una "Bolsa" (conjunto de fichas) solicitada.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta acceder a una bolsa de fichas que no existe en el sistema</li>
 *   <li>Se intenta cargar una bolsa cuyo identificador no está registrado</li>
 *   <li>La bolsa ha sido eliminada o el archivo está corrompido</li>
 * </ul>
 * 
 */
public class BolsaNoEncontradaException extends Exception {
    private static final String DEFAULT_MSG = "Bolsa no encontrada.";

    /**
     * Construye una nueva BolsaNoEncontradaException con un mensaje predeterminado.
     */
    public BolsaNoEncontradaException() {
        super(DEFAULT_MSG);
    }

    /**
     * Construye una nueva BolsaNoEncontradaException con un mensaje personalizado.
     *
     * @param message el mensaje detallado que describe el error.
     */
    public BolsaNoEncontradaException(String message) {
        super(message);
    }
}