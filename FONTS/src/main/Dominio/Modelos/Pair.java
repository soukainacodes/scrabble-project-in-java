package Dominio.Modelos;

import java.util.Objects;

/**
 * Representa un par de valores genéricos (first, second).
 *
 * @param <F> Tipo del primer valor.
 * @param <S> Tipo del segundo valor.
 */
public class Pair<F, S> {

    /** Primer elemento del par. */
    private F first;
    /** Segundo elemento del par. */
    private S second;

    /**
     * Construye un par con los valores indicados.
     *
     * @param first  Primer valor.
     * @param second Segundo valor.
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Fábrica estática para crear un par sin necesidad de especificar tipos.
     *
     * @param <F>    Tipo del primer valor.
     * @param <S>    Tipo del segundo valor.
     * @param first  Primer valor.
     * @param second Segundo valor.
     * @return Nuevo objeto Pair con los valores proporcionados.
     */
    public static <F, S> Pair<F, S> createPair(F first, S second) {
        return new Pair<>(first, second);
    }

    /**
     * Establece un nuevo primer valor.
     *
     * @param first Nuevo primer valor.
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * Establece un nuevo segundo valor.
     *
     * @param second Nuevo segundo valor.
     */
    public void setSecond(S second) {
        this.second = second;
    }

    /**
     * Obtiene el primer valor.
     *
     * @return Primer elemento del par.
     */
    public F getFirst() {
        return first;
    }

    /**
     * Obtiene el segundo valor.
     *
     * @return Segundo elemento del par.
     */
    public S getSecond() {
        return second;
    }

    /**
     * Representación en cadena del par, en formato (first, second).
     *
     * @return Cadena que representa el par.
     */
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    /**
     * Compara igualdad basándose en la igualdad de ambos elementos.
     *
     * @param obj Objeto a comparar.
     * @return {@code true} si ambos pares contienen elementos iguales.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pair)) return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(first, other.first)
            && Objects.equals(second, other.second);
    }

    /**
     * Genera un código hash consistente con equals.
     *
     * @return Código hash basado en los dos elementos.
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
