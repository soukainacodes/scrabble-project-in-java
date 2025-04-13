package Dominio.Excepciones;
public class PrefijoNoEncontradoException extends Exception {
    public PrefijoNoEncontradoException(String prefijo) {
        super("El prefijo no existe en el DAWG: " + prefijo);
    }
}
