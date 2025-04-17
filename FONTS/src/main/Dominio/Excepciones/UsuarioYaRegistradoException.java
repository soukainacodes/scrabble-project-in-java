// Dominio/Excepciones/UsuarioYaRegistradoException.java
package Dominio.Excepciones;
public class UsuarioYaRegistradoException extends Exception {
    public UsuarioYaRegistradoException(String nombre) {
        super("El usuario ya existe: " + nombre);
    }
}
