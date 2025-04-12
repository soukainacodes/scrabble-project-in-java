package main.Dominio.Controladores.Excepciones;

public class PalabraInvalidaException extends Exception {
    public PalabraInvalidaException(String palabra) {
        super("La palabra no fue encontrada en el DAWG: " + palabra);
    }
}
