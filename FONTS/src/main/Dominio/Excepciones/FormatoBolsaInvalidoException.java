package Dominio.Excepciones;

public class FormatoBolsaInvalidoException extends Exception {

    public FormatoBolsaInvalidoException() {
        super("El formato de la bolsa es inválido");
    }

    public FormatoBolsaInvalidoException(String mensaje) {
        super(mensaje);
    }

}
