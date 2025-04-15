package Dominio.Modelos;

public class Tablero {
    private static final int TAMANO = 15;
    private Celda[][] celdas;

    public Tablero() {
        this.celdas = new Celda[TAMANO][TAMANO];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                celdas[i][j] = new Celda(asignarBonificacion(i, j));
            }
        }
    }

    

    private TipoBonificacion asignarBonificacion(int fila, int columna) {
        int[][] triplePalabra = {{0, 0}, {0, 7}, {0, 14}, {7, 0}, {7, 14}, {14, 0}, {14, 7}, {14, 14}};
        int[][] doblePalabra = {
            {1, 1}, {2, 2}, {3, 3}, {4, 4},
            {10, 10}, {11, 11}, {12, 12}, {13, 13},
            {1, 13}, {2, 12}, {3, 11}, {4, 10},
            {10, 4}, {11, 3}, {12, 2}, {13, 1},
            {7, 7}
        };
        int[][] tripleLetra = {
            {1, 5}, {1, 9}, {5, 1}, {5, 5},
            {5, 9}, {5, 13}, {9, 1}, {9, 5},
            {9, 9}, {9, 13}, {13, 5}, {13, 9}
        };
        int[][] dobleLetra = {
            {0, 3}, {0, 11}, {2, 6}, {2, 8},
            {3, 0}, {3, 7}, {3, 14}, {6, 2},
            {6, 6}, {6, 8}, {6, 12}, {7, 3},
            {7, 11}, {8, 2}, {8, 6}, {8, 8},
            {8, 12}, {11, 0}, {11, 7}, {11, 14},
            {12, 6}, {12, 8}, {14, 3}, {14, 11}
        };
        
        for (int[] pos : triplePalabra) {
            if (pos[0] == fila && pos[1] == columna) return TipoBonificacion.TRIPLE_PALABRA;
        }
        for (int[] pos : doblePalabra) {
            if (pos[0] == fila && pos[1] == columna) return TipoBonificacion.DOBLE_PALABRA;
        }
        for (int[] pos : tripleLetra) {
            if (pos[0] == fila && pos[1] == columna) return TipoBonificacion.TRIPLE_LETRA;
        }
        for (int[] pos : dobleLetra) {
            if (pos[0] == fila && pos[1] == columna) return TipoBonificacion.DOBLE_LETRA;
        }
        return TipoBonificacion.NINGUNA;
    }

    public Celda getCelda(int fila, int columna) {
        if (fila >= 0 && fila < TAMANO && columna >= 0 && columna < TAMANO) {
            return celdas[fila][columna];
        } else {
           // System.out.println("Posición fuera de los límites del tablero.");
            return null;
        }
    }
    
    // Método para obtener la ficha en una posición dada
    public Ficha getFicha(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            return celda.getFicha();
        }
        return null;
    }

    // Método para colocar una ficha en una celda dada
    public boolean ponerFicha(Ficha ficha, int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            return celda.colocarFicha(ficha);
        }
        return false;
    }

    // Método para quitar una ficha de una celda dada
    public Ficha quitarFicha(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            return celda.quitarFicha();
        }
        return null;
    }

    // Método para bloquear la celda en una posición dada
    public void bloquearCelda(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.bloquearFicha();
        }
    }

    // Método para ver si una posición es el centro del tablero o no: bool
    public boolean esCentroDelTablero(int fila, int columna) {
        return fila == 7 && columna == 7;
    }
        
}
