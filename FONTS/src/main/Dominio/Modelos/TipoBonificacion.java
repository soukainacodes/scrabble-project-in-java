package Dominio.Modelos;

public enum TipoBonificacion {
    NINGUNA(1),
    DOBLE_LETRA(2),
    TRIPLE_LETRA(3),
    DOBLE_PALABRA(2),
    TRIPLE_PALABRA(3);

    private final int multiplicador;

    private TipoBonificacion(int multiplicador) {
        this.multiplicador = multiplicador;
    }
    
    public int getMultiplicador() {
        return multiplicador;
    }
}
