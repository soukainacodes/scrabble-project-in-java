package Dominio.Modelos;

/**
 * Representa una ficha del juego de Scrabble, con letra y puntuación asociada.
 */
public class Ficha {

    /** Letra que representa la ficha. */
    private String letra;

    /** Puntuación que otorga la ficha al colocarse. */
    private int puntuacion;

    /**
     * Construye una ficha con la letra y puntuación indicadas.
     *
     * @param letra      Cadena con la letra (o símbolo) de la ficha.
     * @param puntuacion Valor numérico de la puntuación asociada.
     */
    public Ficha(String letra, int puntuacion) {
        this.letra = letra;
        this.puntuacion = puntuacion;
    }   

    /**
     * Obtiene la letra de la ficha.
     *
     * @return Cadena con la letra o símbolo de la ficha.
     */
    public String getLetra() {
        return letra;
    }

    /**
     * Obtiene la puntuación de la ficha.
     *
     * @return Entero con los puntos que vale la ficha.
     */
    public int getPuntuacion() {
        return puntuacion;
    }


}
