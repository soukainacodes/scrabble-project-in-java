package Dominio.Excepciones;

/**
 * Excepción lanzada cuando una jugada forma una palabra inválida según las reglas del juego.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>La palabra formada no existe en el diccionario oficial del juego</li>
 *   <li>La palabra tiene caracteres no permitidos o inválidos</li>
 *   <li>La disposición de las fichas no cumple con las reglas de colocación</li>
 *   <li>La palabra no tiene la longitud mínima requerida</li>
 * </ul>
 * 
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