package main.Dominio.Controladores.Excepciones;

public class JugadorNoEncontradoException extends Exception {
    public JugadorNoEncontradoException(String nombre) {
        super("El jugador no se encuentra en el ranking: " + nombre);
    }
}
