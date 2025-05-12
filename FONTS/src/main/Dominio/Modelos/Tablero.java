package Dominio.Modelos;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el tablero de Scrabble de tamaño fijo 15x15.
 * Cada celda puede tener una bonificación de letra o palabra.
 */
public class Tablero {

    /** Tamaño fijo del tablero (número de filas y columnas). */
    private static final int TAMANO = 15;

    /** Matriz de celdas que conforman el tablero. */
    private Celda[][] celdas;

    /**
     * Construye un tablero inicializando todas las celdas con sus bonificaciones.
     */
    public Tablero() {
        this.celdas = new Celda[TAMANO][TAMANO];
        inicializarTablero();
    }

    /**
     * Crea y asigna cada celda del tablero con su correspondiente bonificación.
     */
    private void inicializarTablero() {
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                celdas[fila][columna] = new Celda(asignarBonificacion(fila, columna));
            }
        }
    }

    /**
     * Determina la bonificación de una posición según las reglas de Scrabble.
     *
     * @param fila    Índice de fila (0..14).
     * @param columna Índice de columna (0..14).
     * @return Tipo de bonificación de la celda.
     */
    private TipoBonificacion asignarBonificacion(int fila, int columna) {
        int[][] triplePalabra = {{0,0},{0,7},{0,14},{7,0},{7,14},{14,0},{14,7},{14,14}};
        int[][] doblePalabra = {{1,1},{2,2},{3,3},{4,4},{10,10},{11,11},{12,12},{13,13},
                                 {1,13},{2,12},{3,11},{4,10},{10,4},{11,3},{12,2},{13,1},{7,7}};
        int[][] tripleLetra  = {{1,5},{1,9},{5,1},{5,5},{5,9},{5,13},{9,1},{9,5},{9,9},{9,13},{13,5},{13,9}};
        int[][] dobleLetra   = {{0,3},{0,11},{2,6},{2,8},{3,0},{3,7},{3,14},{6,2},{6,6},{6,8},{6,12},
                                 {7,3},{7,11},{8,2},{8,6},{8,8},{8,12},{11,0},{11,7},{11,14},{12,6},{12,8},{14,3},{14,11}};
        for (int[] p : triplePalabra) if (p[0]==fila && p[1]==columna) return TipoBonificacion.TRIPLE_PALABRA;
        for (int[] p : doblePalabra)   if (p[0]==fila && p[1]==columna) return TipoBonificacion.DOBLE_PALABRA;
        for (int[] p : tripleLetra)    if (p[0]==fila && p[1]==columna) return TipoBonificacion.TRIPLE_LETRA;
        for (int[] p : dobleLetra)     if (p[0]==fila && p[1]==columna) return TipoBonificacion.DOBLE_LETRA;
        return TipoBonificacion.NINGUNA;
    }

    /**
     * Obtiene la celda en la posición indicada.
     *
     * @param fila    Índice de fila.
     * @param columna Índice de columna.
     * @return Objeto Celda, o null si está fuera de límites.
     */
    public Celda getCelda(int fila, int columna) {
        if (fila >= 0 && fila < TAMANO && columna >= 0 && columna < TAMANO) {
            return celdas[fila][columna];
        }
        return null;
    }

    /**
     * Obtiene la ficha en la celda dada.
     *
     * @param fila    Índice de fila.
     * @param columna Índice de columna.
     * @return Objeto Ficha o null si no hay ficha o posición inválida.
     */
    public Ficha getFicha(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        return celda != null ? celda.getFicha() : null;
    }

    /**
     * Coloca una ficha en la celda indicada.
     *
     * @param ficha   Ficha a colocar.
     * @param fila    Índice de fila.
     * @param columna Índice de columna.
     * @return true si la operación tuvo éxito, false si la celda no existe o está ocupada.
     */
    public boolean ponerFicha(Ficha ficha, int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        return celda != null && celda.colocarFicha(ficha);
    }

    /**
     * Coloca un comodín en la celda, representando la letra dada.
     *
     * @param letra   Letra representada por el comodín.
     * @param fila    Índice de fila.
     * @param columna Índice de columna.
     * @return true si se colocó correctamente, false en caso contrario.
     */
    public boolean ponerComodin(String letra, int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda == null) return false;
        Ficha f = new Ficha(letra, 0);
        return celda.colocarFicha(f);
    }

    /**
     * Quita y devuelve la ficha de la celda indicada.
     *
     * @param fila    Índice de fila.
     * @param columna Índice de columna.
     * @return Ficha retirada o null si la posición es inválida o vacía.
     */
    public Ficha quitarFicha(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        return celda != null ? celda.quitarFicha() : null;
    }

    /**
     * Bloquea la ficha en la celda para que no pueda ser retirada.
     *
     * @param fila    Índice de fila.
     * @param columna Índice de columna.
     */
    public void bloquearCelda(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) celda.bloquearFicha();
    }

    /**
     * Verifica si la posición es el centro del tablero (casilla de inicio).
     *
     * @param fila    Índice de fila.
     * @param columna Índice de columna.
     * @return true si es el centro (7,7), false en otro caso.
     */
    public boolean esCentroDelTablero(int fila, int columna) {
        return fila == TAMANO/2 && columna == TAMANO/2;
    }

   
    public List<String> toListString(){
        List<String> tableroList = new ArrayList<>();
        String celdaString = "";
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna= 0; columna < TAMANO; columna++) {
                if(getFicha(fila,columna) != null){
                    celdaString += getFicha(fila,columna).getLetra() + " " + 
                                     Integer.toString(getFicha(fila,columna).getPuntuacion()) + " " +
                                     Integer.toString(fila) + " " + 
                                     Integer.toString(columna);
                    tableroList.add(celdaString);
                    celdaString = ""; 
                }
            }
        }
        return tableroList;
    }

    /**
     * Devuelve la letra de la celda en la posición dada.
     *
     * @param fila    Índice de fila.
     * @param col     Índice de columna.
     * @return Letra de la ficha o null si no hay ficha o posición inválida.
     */
    public String getLetraCelda(int fila, int col) {
        Celda cel = celdas[fila][col];
        if (cel.estaOcupada()) {
            Ficha f = cel.getFicha();
            // si puntuación 0, devolvemos “#”, si no la propia letra
            return f.getPuntuacion() == 0 ? "#" : f.getLetra();
        }
        return null;
    }



    /**
     * Devuelve la bonificación de la celda en la posición dada.
     *
     * @param fila    Índice de fila.
     * @param col     Índice de columna.
     * @return Tipo de bonificación como String (DL, TL, DP, TP o US).
     */
    public String getBonusCelda(int fila, int col) {
        Celda cel = getCelda(fila, col);
        // si la bonificación ya fue usada y no es NINGUNA
        if (!cel.bonusDisponible() && cel.getBonificacion() != TipoBonificacion.NINGUNA) {
            return "US";
        }
        // según el tipo de bonificación
        return switch (cel.getBonificacion()) {
            case DOBLE_LETRA    -> "DL";
            case TRIPLE_LETRA   -> "TL";
            case DOBLE_PALABRA  -> "DP";
            case TRIPLE_PALABRA -> "TP";
            default             -> "  ";
        };
    }

}
