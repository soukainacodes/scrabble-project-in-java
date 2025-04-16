package Dominio.Modelos;

public class Ficha {
    // Atributos
    private int id;
    private String letra;
    private int puntuacion;

    // Constructora
    public Ficha(int id, String letra, int puntuacion) {
        this.id = id;
        this.letra = letra;
        this.puntuacion = puntuacion;
    }

    // Getters
    public int getId() {
        return id; //Devuelve el ID de la Ficha
    }

    public String getLetra() {
        return letra; //Devuelve la letra asociada a la Ficha
    }

    public int getPuntuacion() {
        return puntuacion; //Devuelve la puntuacion asociada a la Ficha
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLetra(String letra) {
        this.letra = letra;
        //this.asignarPuntuacion(letra);
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
        //this.asignarPuntuacion(letra);
    }


    // Método toString para depurar los datos de una ficha como String
    @Override
    public String toString() {
        return "Ficha{" +
                "id=" + id +
                ", letra='" + letra + "'" +
                ", puntuacion=" + puntuacion +
                '}';
    }
}
