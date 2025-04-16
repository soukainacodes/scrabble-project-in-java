// Dominio/Modelos/Jugador.java
package Dominio.Modelos;

public class Jugador {
    private String nombre;
    private String password;
    private int puntos;      // la puntuación máxima alcanzada

    public Jugador(String nombre, String password) {
        this.nombre   = nombre;
        this.password = password;
        this.puntos   = 0;
    }

    public String getNombre()   { return nombre; }
    public String getPassword() { return password; }
    public int    getPuntos()   { return puntos; }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validarPassword(String pass) {
        return this.password.equals(pass);
    }
}
