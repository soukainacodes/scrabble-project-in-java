// Dominio/Excepciones/PartidaNoIniciadaException.java
package Dominio.Excepciones;
public class PartidaNoIniciadaException extends Exception {
    public PartidaNoIniciadaException() {
        super("No hay ninguna partida en curso.");
    }
}
