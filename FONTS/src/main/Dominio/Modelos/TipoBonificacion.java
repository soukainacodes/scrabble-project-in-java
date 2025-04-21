package Dominio.Modelos;

/**
 * Enumeración de los tipos de bonificación disponibles en el tablero de Scrabble.
 * Cada tipo define un multiplicador aplicado a la puntuación de letra o palabra.
 */
public enum TipoBonificacion {

    /** Sin bonificación (multiplicador 1). */
    NINGUNA(1),

    /** Bonificación de doble letra (multiplicador 2). */
    DOBLE_LETRA(2),

    /** Bonificación de triple letra (multiplicador 3). */
    TRIPLE_LETRA(3),

    /** Bonificación de doble palabra (multiplicador 2). */
    DOBLE_PALABRA(2),

    /** Bonificación de triple palabra (multiplicador 3). */
    TRIPLE_PALABRA(3);

    /** Multiplicador asociado al tipo de bonificación. */
    private final int multiplicador;

    /**
     * Constructor interno de la enumeración.
     *
     * @param multiplicador Valor para multiplicar la puntuación.
     */
    private TipoBonificacion(int multiplicador) {
        this.multiplicador = multiplicador;
    }

    /**
     * Obtiene el multiplicador de la bonificación.
     *
     * @return Entero con el valor del multiplicador.
     */
    public int getMultiplicador() {
        return multiplicador;
    }
}
