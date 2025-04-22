package Dominio.Excepciones;

/**
 * Excepción lanzada cuando una jugada forma una palabra inválida según las reglas del juego.
 */
public class PalabraInvalidaException extends Exception {

    /**
     * Construye una nueva PalabraInvalidaException con un mensaje predeterminado.
     * El mensaje predeterminado indica que la palabra no es válida.
     */
    public PalabraInvalidaException() {
        super("La palabra no es válida.");
    }

    /**
     * Construye una nueva PalabraInvalidaException con un mensaje personalizado.
     * El mensaje personalizado incluye la razón por la cual la palabra es inválida.
     *
     * @param razon la razón específica por la cual la palabra es inválida.
     */
    public PalabraInvalidaException(String razon) {
        super("Palabra inválida: " + razon);
    }
}
