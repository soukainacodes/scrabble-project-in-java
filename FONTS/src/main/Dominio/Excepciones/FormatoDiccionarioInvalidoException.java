package Dominio.Excepciones;

public class FormatoDiccionarioInvalidoException extends Exception {

    public FormatoDiccionarioInvalidoException() {
        super("El formato del diccionario es inválido");
    }

}
