// Dominio/Excepciones/SesionNoIniciadaException.java
package Dominio.Excepciones;
public class SesionNoIniciadaException extends Exception {
    public SesionNoIniciadaException() {
        super("Error de sesión. Vuelve a intentarlo más tarde.\r\n" + //
                        "");
    }
}
