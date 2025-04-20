// FONTS/src/main/Dominio/Excepciones/CasillaVaciaException.java
package Dominio.Excepciones;
public class CasillaVaciaException extends Exception {
    public CasillaVaciaException(int fila, int col) {
        super("No hay ficha en la casilla: (" + fila + "," + col + ")");
    }
}

