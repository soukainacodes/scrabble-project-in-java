package Dominio.Modelos;

/**
 * Representa una ficha del juego de Scrabble, con letra y puntuación asociada.
 */
public class Ficha {

    /** Identificador único de la ficha (opcional). */
    private int id;

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

    /**
     * Devuelve una representación en cadena de la ficha.
     *
     * @return Texto con letra y puntuación, por ejemplo "A(1)".
     */
    @Override
    public String toString() {
        return letra + "(" + puntuacion + ")";
    }

    /**
     * Compara igualdad basándose en letra y puntuación.
     *
     * @param obj Objeto a comparar.
     * @return {@code true} si es otra Ficha con misma letra y puntuación.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ficha)) return false;
        Ficha otra = (Ficha) obj;
        return puntuacion == otra.puntuacion && letra.equals(otra.letra);
    }

    /**
     * Genera un código hash basado en letra y puntuación.
     *
     * @return Código hash para uso en colecciones.
     */
    @Override
    public int hashCode() {
        int result = letra != null ? letra.hashCode() : 0;
        result = 31 * result + puntuacion;
        return result;
    }
}
