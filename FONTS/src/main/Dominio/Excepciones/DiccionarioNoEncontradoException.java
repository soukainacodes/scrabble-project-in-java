// src/Dominio/Excepciones/DiccionarioNoEncontradoException.java
package Dominio.Excepciones;

public class DiccionarioNoEncontradoException extends Exception {
    private static final String DEFAULT_MSG = "Diccionario no encontrado.";

    public DiccionarioNoEncontradoException() {
        super(DEFAULT_MSG + " Por favor, verifique el nombre del diccionario.");
    }

    public DiccionarioNoEncontradoException(String message) {
        super(message);
    }
}
