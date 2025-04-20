// FONTS/src/main/Dominio/Excepciones/BonusNoDisponibleException.java
package Dominio.Excepciones;
public class BonusNoDisponibleException extends Exception {
    public BonusNoDisponibleException(int fila, int col) {
        super("Bonus ya usado en la casilla: (" + fila + "," + col + ")");
    }
}
