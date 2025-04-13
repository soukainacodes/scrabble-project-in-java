package Dominio.Excepciones;
public class NodoNoEncontradoException extends Exception {
    public NodoNoEncontradoException(String palabra) {
        super("No se encontró el nodo correspondiente a: " + palabra);
    }
}
