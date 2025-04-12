public class Jugador {

    // Atributos de la clase
    private String nombre;      // Almacena el nombre del jugador
    private String password;    // Almacena la contraseña del jugador
    private int puntos;         // Almacena los puntos acumulados por el jugador

    // Constructor
    public Jugador(String nombre, String password) {
        this.nombre = nombre;    // Inicializa el nombre del jugador
        this.password = password; // Inicializa la contraseña del jugador
        this.puntos = 0;          // Inicializa los puntos del jugador en 0
    }

    // Método getNombre
    public String getNombre() {
        return this.nombre; // Devuelve el nombre del jugador
    }

    public String getPassword() {
        return this.password; // Devuelve el nombre del jugador
    }
    // Método getPuntos
    public int getPuntos() {
        return this.puntos; // Devuelve los puntos actuales del jugador
    }

    // Método setPuntos
    public void setPuntos(int puntos) {
        this.puntos = puntos; // Establece o actualiza los puntos del jugador
    }

    // Método ValidarPassword (versión corregida)
    public boolean ValidarPassword(String pass) {
        return this.password.equals(pass); // Valida si el parametro es la contraseña del jugadpr
    }

        /*
    public boolean ValidarPassword(String pass) {
        return this.password == pass;
    }
    */
}