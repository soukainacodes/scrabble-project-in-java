package Dominio.Modelos;

/**
 * Representa un jugador del juego, con nombre, contraseña y puntuación máxima alcanzada.
 */
public class Jugador {

    /** Nombre único del jugador. */
    private String nombre;

    /** Contraseña para autenticar al jugador. */
    private String password;

    /** Puntuación máxima alcanzada por el jugador. */
    private int puntos;

    /**
     * Construye un nuevo jugador con nombre y contraseña, inicializando la puntuación a cero.
     *
     * @param nombre   Nombre del jugador.
     * @param password Contraseña del jugador.
     */
    public Jugador(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
        this.puntos = 0;
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
     * Obtiene la contraseña del jugador.
     *
     * @return Contraseña del jugador.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene la puntuación máxima alcanzada por el jugador.
     *
     * @return Puntos máximos.
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Establece la nueva puntuación máxima del jugador.
     *
     * @param puntos Puntuación a asignar.
     */
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    /**
     * Cambia la contraseña del jugador.
     *
     * @param password Nueva contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Valida si la cadena proporcionada coincide con la contraseña actual.
     *
     * @param pass Contraseña a validar.
     * @return {@code true} si es correcta, {@code false} en caso contrario.
     */
    public boolean validarPassword(String pass) {
        return this.password != null && this.password.equals(pass);
    }

    /**
     * Devuelve una representación en cadena del jugador (solo nombre y puntos).
     *
     * @return Texto con nombre y puntuación.
     */
    @Override
    public String toString() {
        return String.format("Jugador{name='%s', puntos=%d}", nombre, puntos);
    }

    /**
     * Compara igualdad basándose en el nombre del jugador.
     *
     * @param obj Objeto a comparar.
     * @return {@code true} si es otro Jugador con mismo nombre.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Jugador)) return false;
        Jugador other = (Jugador) obj;
        return nombre.equals(other.nombre);
    }

    /**
     * Genera un código hash basado en el nombre del jugador.
     *
     * @return Código hash.
     */
    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }
}
