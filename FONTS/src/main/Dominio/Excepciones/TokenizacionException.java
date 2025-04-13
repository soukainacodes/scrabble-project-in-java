package Dominio.Excepciones;
public class TokenizacionException extends Exception {
    public TokenizacionException(String secuenciaInvalida) {
        super("No se puede tokenizar la secuencia: " + secuenciaInvalida);
    }
}
