// Dominio/Excepciones/AutenticacionException.java
package Dominio.Excepciones;
public class AutenticacionException extends Exception {
    public AutenticacionException() {
        super("Credenciales inválidas.");
    }
}
