public class Celda {
    private Ficha ficha;
    private TipoBonificacion bonificacion;
    private boolean bloqueada;
    private boolean bonusUsada; // Indica si la bonificación ya fue aplicada

    public Celda(TipoBonificacion bonificacion) {
        this.bonificacion = bonificacion;
        this.ficha = null;
        this.bloqueada = false;
        this.bonusUsada = false;
    }

    // Retorna true si ya hay una ficha en la celda.
    public boolean estaOcupada() {
        return ficha != null;
    }

    public TipoBonificacion getBonificacion() {
        return bonificacion;
    }
    public boolean isDobleTriplePalabra(){
        return (getBonificacion() == TipoBonificacion.DOBLE_PALABRA 
        || getBonificacion() == TipoBonificacion.TRIPLE_PALABRA);
    }

    public boolean isDobleTripleLetra(){
        return (getBonificacion() == TipoBonificacion.DOBLE_LETRA
        || getBonificacion() == TipoBonificacion.TRIPLE_LETRA);
    }
    // Devuelve la ficha que se encuentre en la celda (o null si no hay ninguna).
    public Ficha getFicha() {
        return ficha;
    }

    // Coloca una ficha en la celda si ésta está libre y no bloqueada.
    // Si la celda tiene bonificación disponible, se marca como usada.
    public boolean colocarFicha(Ficha ficha) {
        if (!estaOcupada() && !bloqueada) {
            this.ficha = ficha;
            
            return true;
        }
        return false; // No se puede colocar si ya hay ficha o la celda está bloqueada.
    }

    // Quita la ficha de la celda (si no está bloqueada).
    // La bonificación permanece usada, incluso si se quita la ficha.
    public Ficha quitarFicha() {
        if (this.ficha != null && !this.bloqueada) {
            Ficha fichaQuitada = this.ficha;
            this.ficha = null;
            return fichaQuitada;
        }
        return null; // Si la celda está vacía o bloqueada, devuelve null.
    }
    
    // Bloquea la celda si ya hay una ficha colocada.
    public void bloquearFicha() {
        if (estaOcupada()) {
            // Una vez que se coloca la ficha, la bonificación se aplica (si no se había usado ya)
            if (!bonusUsada) {
                bonusUsada = true;
            }
            this.bloqueada = true;
        }
    }
    
    // Indica si la celda está bloqueada.
    public boolean estaBloqueada() {
        return bloqueada;
    }

    // Retorna true si la bonificación aún no se ha usado.
    public boolean bonusDisponible() {
        return !bonusUsada;
    }

    // Devuelve el multiplicador de la bonificación si está disponible; de lo contrario, retorna 1.
    // Se asume que TipoBonificacion tiene un método getMultiplicador().
    public int obtenerMultiplicador() {
        return bonusDisponible() ? bonificacion.getMultiplicador() : 1;
    }
}
