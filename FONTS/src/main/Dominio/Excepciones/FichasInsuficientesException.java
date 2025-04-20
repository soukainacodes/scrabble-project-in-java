// FONTS/src/main/Dominio/Excepciones/FichasInsuficientesException.java
package Dominio.Excepciones;
public class FichasInsuficientesException extends Exception {
    public FichasInsuficientesException(int pedidas, int disponibles) {
        super("Fichas insuficientes: pedidas=" + pedidas + ", disponibles=" + disponibles);
    }
}
