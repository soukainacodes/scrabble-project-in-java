package Dominio.Excepciones;

/**
 * Se lanza cuando una jugada forma una palabra inválida según las reglas del juego.
 */
public class PalabraInvalidaException extends Exception {
    public PalabraInvalidaException() {
        super("La palabra no es válida.");
    }

    public PalabraInvalidaException(String razon) {
        super("Palabra inválida: " + razon);
    }
}
