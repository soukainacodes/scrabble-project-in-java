package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se proporciona una contraseña incorrecta.
 * Esta excepción se utiliza para indicar que la operación no puede continuar
 * debido a una contraseña inválida.
 */
public class PasswordInvalidaException extends Exception {

    /**
     * Construye una nueva PasswordInvalidaException con un mensaje predeterminado.
     * El mensaje predeterminado indica que la contraseña es incorrecta.
     */
    public PasswordInvalidaException() {
        super("Contraseña incorrecta. Operación cancelada.");
    }
}
