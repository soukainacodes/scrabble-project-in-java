package Dominio.Modelos;

public class Ficha {
    // Atributos
    private int id;
    private String letra;
    private int puntuacion;

    // Constructora
    public Ficha(String letra, int puntuacion) {
       
        this.letra = letra;
        this.puntuacion = puntuacion;
    }

    // Getters
    public String getLetra() {
        return letra; //Devuelve la letra asociada a la Ficha
    }

    public int getPuntuacion() {
        return puntuacion; //Devuelve la puntuacion asociada a la Ficha
    }

}
