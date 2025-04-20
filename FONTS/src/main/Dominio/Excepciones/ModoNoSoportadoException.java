// FONTS/src/main/Dominio/Excepciones/ModoNoSoportadoException.java
package Dominio.Excepciones;

public class ModoNoSoportadoException extends Exception {
    public ModoNoSoportadoException(int modo) {
        super("Modo de juego no soportado: " + modo);
    }
}
