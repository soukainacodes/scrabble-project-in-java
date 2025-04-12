package main.Dominio.Controladores.Excepciones;

public class PalabraNoEncontradaException extends Exception {
    public PalabraNoEncontradaException(String palabra) {
        super("La palabra no fue encontrada en el DAWG: " + palabra);
    }
}
