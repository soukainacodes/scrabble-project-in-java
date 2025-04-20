// FONTS/src/main/Dominio/Excepciones/PartidaNoEncontradaException.java
package Dominio.Excepciones;
public class PartidaNoEncontradaException extends Exception {
    public PartidaNoEncontradaException(String id) {
        super("Partida no encontrada: " + id);
    }
}

