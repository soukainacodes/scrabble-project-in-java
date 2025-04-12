import java.io.*;
import java.util.*;

public class CtrlJugador {
  
    private Map<String,Jugador> jugadores;

    // Constructor
    public CtrlJugador() {
        this.jugadores= new HashMap<>(); // Inicializa el mapa vacío
    }

    // Método para crear un nuevo jugador
    public boolean crearJugador(String nombre, String password) {
       
        if (existeJugador(nombre)) return false; // El jugador ya existe
        Jugador nuevoJugador = new Jugador(nombre, password);
        jugadores.put(nombre,nuevoJugador);
        
        return true;
    }

    
    // Método para iniciar sesión de un jugador
    public boolean iniciarSesion(String nombre, String password) {
        Jugador jugador = getJugador(nombre);
        if (jugador != null && jugador.getPassword() == password) return true; 
        return false; 
    }


    // Método para verificar si un jugador está registrado
    public boolean existeJugador(String nombre) {
        return jugadores.containsKey(nombre);
    }

    // Método para obtener un jugador por su nombre
    public Jugador getJugador(String nombre) {
        return jugadores.get(nombre);
    }

    // Método para listar todos los jugadores registrados
    /*public void listarJugadores() {
        if (conjuntoDeJugadoresRegistrados.isEmpty()) {
            System.out.println("No hay jugadores registrados.");
        } else {
            System.out.println("Jugadores registrados:");
            for (Jugador jugador : conjuntoDeJugadoresRegistrados.values()) {
                System.out.println("Nombre: " + jugador.getNombre() + ", Puntos: " + jugador.getPuntos());
            }
        }
    }*/
}
