package Dominio.Excepciones;
public class FichaIncorrecta extends Exception {
    public FichaIncorrecta() {
        super("Usuario o contraseña incorrectos. Inténtalo de nuevo.\r\n" + //
                        "");
    }
}
