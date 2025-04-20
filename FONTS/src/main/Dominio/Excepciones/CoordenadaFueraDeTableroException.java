 // FONTS/src/main/Dominio/Excepciones/CoordenadaFueraDeTableroException.java
package Dominio.Excepciones;
public class CoordenadaFueraDeTableroException extends Exception {
    public CoordenadaFueraDeTableroException(int fila, int col) {
        super("Coordenada fuera de rango: (" + fila + "," + col + ")");
    }
}
 