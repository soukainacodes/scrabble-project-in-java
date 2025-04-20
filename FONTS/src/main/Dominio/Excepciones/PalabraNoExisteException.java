// FONTS/src/main/Dominio/Excepciones/PalabraNoExisteException.java
package Dominio.Excepciones;
public class PalabraNoExisteException extends Exception {
    public PalabraNoExisteException(String palabra) {
        super("La palabra no existe en el diccionario: " + palabra);
    }
}