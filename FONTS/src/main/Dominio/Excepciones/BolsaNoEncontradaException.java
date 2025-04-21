// src/Dominio/Excepciones/BolsaNoEncontradaException.java
package Dominio.Excepciones;

public class BolsaNoEncontradaException extends Exception {
    private static final String DEFAULT_MSG = "Bolsa no encontrada.";

    public BolsaNoEncontradaException() {
        super(DEFAULT_MSG);
    }

    public BolsaNoEncontradaException(String message) {
        super(message);
    }
}
