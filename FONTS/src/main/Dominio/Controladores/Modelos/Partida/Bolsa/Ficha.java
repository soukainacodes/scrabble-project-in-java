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
        //this.asignarPuntuacion(letra); // Asigna la puntuación automáticamente
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

    // Método para asignar la puntuación según la letra
    /*
    public void asignarPuntuacion(String letra) {
        letra = letra.toUpperCase();

        switch (letra) {
            case "A": case "E": case "O": case "S": case "I": case "U": case "N": case "L": case "R": case "T":
                this.puntuacion = 1;
                break;
            case "D": case "G":
                this.puntuacion = 2;
                break;
            case "B": case "C": case "M": case "P":
                this.puntuacion = 3;
                break;
            case "F": case "H": case "V": case "Y":
                this.puntuacion = 4;
                break;
            case "K":
                this.puntuacion = 5;
                break;
            case "J": case "X": case "Ñ":
                this.puntuacion = 8;
                break;
            case "Q": case "Z":
                this.puntuacion = 10;
                break;
            default:
                this.puntuacion = 0; // Si la letra no tiene puntuación asignada
                break;
        }
    }
    */

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
