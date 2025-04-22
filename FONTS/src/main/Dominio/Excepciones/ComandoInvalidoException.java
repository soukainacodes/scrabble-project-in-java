package Dominio.Excepciones;

public class ComandoInvalidoException extends Exception {
    public ComandoInvalidoException() {
        super("Comando inválido.");
    }

    public ComandoInvalidoException(String detalle) {
        super("Comando inválido: " + detalle);
    }
}
