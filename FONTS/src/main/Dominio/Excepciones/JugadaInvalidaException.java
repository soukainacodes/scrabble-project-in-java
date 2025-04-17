// Dominio/Excepciones/JugadaInvalidaException.java
package Dominio.Excepciones;
public class JugadaInvalidaException extends Exception {
    public JugadaInvalidaException(String jugada) {
        super("Jugada inválida: " + jugada);
    }
}
