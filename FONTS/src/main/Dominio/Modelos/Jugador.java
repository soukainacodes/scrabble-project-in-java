package Dominio.Modelos;

/**
 * Representa un jugador del juego, con nombre, contraseña y puntuación máxima alcanzada.
 */
public class Jugador {

    /** Nombre único del jugador. */
    private String nombre;

    
    /**
     * Construye un nuevo jugador con nombre y contraseña, inicializando la puntuación a cero.
     *
     * @param nombre   Nombre del jugador.
     */
    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return Nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nombre Nuevo nombre del jugador.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

 
}
