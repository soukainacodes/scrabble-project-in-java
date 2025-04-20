// FONTS/src/main/Dominio/Excepciones/TurnoNoValidoException.java
package Dominio.Excepciones;
public class TurnoNoValidoException extends Exception {
    public TurnoNoValidoException(String jugador) {
        super("No es el turno de: " + jugador);
    }
}
