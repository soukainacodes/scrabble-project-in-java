// Fichero: FONTS/src/main/Dominio/Excepciones/PalabraInvalidException.java
package Dominio.Excepciones;

public class PalabraInvalidException extends Exception {
    public PalabraInvalidException(String palabra) {
        super("Palabra inválida: " + palabra);
    }
}
