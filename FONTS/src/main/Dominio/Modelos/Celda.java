package Dominio.Modelos;

/**
 * Representa una celda individual del tablero de Scrabble.
 * <p>
 * Cada celda puede contener una ficha, una bonificación de letra o palabra,
 * y mantiene el estado de si la ficha ha sido bloqueada y la bonificación
 * aplicada.
 * </p>
 */
public class Celda {

    /** Ficha actualmente en la celda (null si está vacía). */
    private Ficha ficha;

    /** Tipo de bonificación de la celda (puede ser ninguna). */
    private TipoBonificacion bonificacion;

    /** Indica si la ficha ha sido bloqueada (no se puede retirar ni recolocar). */
    private boolean bloqueada;

    /** Indica si la bonificación de la celda ya fue usada. */
    private boolean bonusUsada;

    /**
     * Construye una celda con la bonificación indicada.
     * La celda inicia vacía, no bloqueada y con bonificación disponible.
     *
     * @param bonificacion Bonificación de letra o palabra asignada a la celda.
     */
    public Celda(TipoBonificacion bonificacion) {
        this.bonificacion = bonificacion;
        this.ficha = null;
        this.bloqueada = false;
        this.bonusUsada = false;
    }

    /**
     * Indica si la celda contiene una ficha.
     *
     * @return {@code true} si hay una ficha en la celda, {@code false} si está
     *         vacía.
     */
    public boolean estaOcupada() {
        return this.ficha != null;
    }

    /**
     * Recupera la bonificación asignada a esta celda.
     *
     * @return Objeto {@link TipoBonificacion} de la celda.
     */
    public TipoBonificacion getBonificacion() {
        return this.bonificacion;
    }

    /**
     * Comprueba si la bonificación corresponde a palabra doble o triple.
     *
     * @return {@code true} si es doble o triple palabra, {@code false} en otro
     *         caso.
     */
    public boolean isDobleTriplePalabra() {
        return bonificacion == TipoBonificacion.DOBLE_PALABRA
                || bonificacion == TipoBonificacion.TRIPLE_PALABRA;
    }

    /**
     * Comprueba si la bonificación corresponde a letra doble o triple.
     *
     * @return {@code true} si es doble o triple letra, {@code false} en otro caso.
     */
    public boolean isDobleTripleLetra() {
        return bonificacion == TipoBonificacion.DOBLE_LETRA
                || bonificacion == TipoBonificacion.TRIPLE_LETRA;
    }

    /**
     * Devuelve la ficha en la celda.
     *
     * @return {@link Ficha} actual, o {@code null} si la celda está vacía.
     */
    public Ficha getFicha() {
        return this.ficha;
    }

    /**
     * Coloca una ficha en la celda si está libre y no bloqueada.
     *
     * @param ficha Ficha a colocar.
     * @return {@code true} si la ficha se colocó con éxito, {@code false} en caso
     *         contrario.
     */
    public boolean colocarFicha(Ficha ficha) {
        if (!estaOcupada() && !bloqueada) {
            this.ficha = ficha;
            return true;
        }
        return false;
    }

    /**
     * Quita y retorna la ficha de la celda si no está bloqueada.
     * La bonificación permanece usada si ya fue aplicada.
     *
     * @return Ficha retirada, o {@code null} si no se pudo retirar.
     */
    public Ficha quitarFicha() {
        if (this.ficha != null && !bloqueada) {
            Ficha temp = this.ficha;
            this.ficha = null;
            return temp;
        }
        return null;
    }

    /**
     * Bloquea la ficha en la celda para evitar su retirada.
     * Si la bonificación está disponible, la marca como usada.
     */
    public void bloquearFicha() {
        if (estaOcupada()) {
            if (!bonusUsada) {
                bonusUsada = true;
            }
            this.bloqueada = true;
        }
    }

    /**
     * Indica si la ficha en la celda está bloqueada.
     *
     * @return {@code true} si bloqueada, {@code false} en caso contrario.
     */
    public boolean estaBloqueada() {
        return this.bloqueada;
    }

    /**
     * Indica si la bonificación aún no ha sido utilizada.
     *
     * @return {@code true} si disponible, {@code false} si ya fue usada.
     */
    public boolean bonusDisponible() {
        return !this.bonusUsada;
    }

}