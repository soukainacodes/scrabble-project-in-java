// Dominio/Excepciones/SesionNoIniciadaException.java
package Dominio.Excepciones;
public class SesionNoIniciadaException extends Exception {
    public SesionNoIniciadaException() {
        super("No hay ninguna sesión iniciada.");
    }
}
