// Dominio/Excepciones/AutenticacionException.java
package Dominio.Excepciones;
public class AutenticacionException extends Exception {
    public AutenticacionException() {
        super("Usuario o contraseña incorrectos. Inténtalo de nuevo.\r\n" + //
                        "");
    }
}
