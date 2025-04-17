// Dominio/Excepciones/PasswordInvalidaException.java
package Dominio.Excepciones;
public class PasswordInvalidaException extends Exception {
    public PasswordInvalidaException() {
        super("La contraseña actual no coincide.");
    }
}
