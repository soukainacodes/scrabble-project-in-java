// Dominio/Excepciones/UsuarioNoEncontradoException.java
package Dominio.Excepciones;
public class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException(String nombre) {
        super("Usuario no encontrado: " + nombre);
    }
}
