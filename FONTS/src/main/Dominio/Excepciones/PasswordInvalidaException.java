// Dominio/Excepciones/PasswordInvalidaException.java
package Dominio.Excepciones;
public class PasswordInvalidaException extends Exception {
    public PasswordInvalidaException() {
        super("Contraseña incorrecta. Operación cancelada.\r\n" + //
                        "");
    }
}
