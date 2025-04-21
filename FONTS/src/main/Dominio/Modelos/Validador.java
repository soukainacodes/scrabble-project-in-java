package Dominio.Modelos;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de validar las jugadas hechas en el tablero según las reglas del
 * juego de Scrabble.
 * <p>
 * Verifica alineación, conexión con otras fichas, uso del centro en el primer
 * turno, y validez de palabras en el diccionario DAWG. También calcula la
 * puntuación total de la jugada, incluyendo palabras perpendiculares formadas
 * al colocar nuevas fichas.
 */
public class Validador {

    private int contadorTurno;
    private Tablero tablero;

    private Dawg diccionario;
    private boolean hayBloqueada;

    /**
     * Constructor por defecto del validador. Inicializa una instancia sin
     * configuración inicial explícita.
     */
    public Validador() {

    }

    /**
     * Valida una jugada en el tablero comprobando si las fichas colocadas
     * forman una palabra válida según el diccionario y las reglas del juego
     * (alineación, conexión, uso del centro en el primer turno, etc.).
     *
     * También calcula la puntuación total de la palabra principal y las
     * palabras perpendiculares que se formen.
     *
     * @param coordenadasPalabra Lista de coordenadas (fila, columna) donde se
     * han colocado las nuevas fichas.
     * @param diccionario Estructura DAWG con las palabras válidas.
     * @param tablero Estado actual del tablero de juego.
     * @param contadorTurno Número de turno actual (comienza en 0).
     * @return Puntuación total de la jugada si es válida, o {@code 0} si la
     * jugada es inválida.
     */
    public int validarPalabra(List<Pair<Integer, Integer>> coordenadasPalabra, Dawg diccionario, Tablero tablero, int contadorTurno) {
        this.hayBloqueada = false;
        // If no new tiles were placed
        this.tablero = tablero;
        this.diccionario = diccionario;

        if (coordenadasPalabra.isEmpty()) {

            return 0;
        }

        int puntosTotales = 0;

        // Special case: only one tile placed
        if (coordenadasPalabra.size() == 1) {

            if (contadorTurno == 0) {
                return 0;
            }

            Pair<Integer, Integer> coord = coordenadasPalabra.get(0);

            puntosTotales += recorrerDireccion(coord.getFirst(), coord.getSecond(), false);
            puntosTotales += recorrerDireccion(coord.getFirst(), coord.getSecond(), true);

            return puntosTotales;
        }

        // Check if tiles are in a straight line
        boolean isHorizontalLine = true;
        boolean isVerticalLine = true;
        boolean touchesCenterTile = false;

        int refX = coordenadasPalabra.get(0).getFirst();
        int refY = coordenadasPalabra.get(0).getSecond();

        for (Pair<Integer, Integer> p : coordenadasPalabra) {
            if (p.getFirst() != refX) {
                isHorizontalLine = false;
            }
            if (p.getSecond() != refY) {
                isVerticalLine = false;
            }
            if (tablero.esCentroDelTablero(p.getFirst(), p.getSecond())) {
                touchesCenterTile = true;
            }
        }

        // Validate placement rules
        if (!isHorizontalLine && !isVerticalLine) {
            return 0; // Tiles must be in a straight line
        }

        if (contadorTurno == 0 && !touchesCenterTile) {
            return 0; // First turn must include center tile
        }

        // Calculate main word and points
        // Get the main word based on line direction
        if (isHorizontalLine) {
            puntosTotales += recorrerDireccion(coordenadasPalabra.get(0).getFirst(), coordenadasPalabra.get(0).getSecond(), false);
    
            if (puntosTotales < 0) {
                return 0;
            }

        } else { // isVerticalLine
            puntosTotales += recorrerDireccion(coordenadasPalabra.get(0).getFirst(), coordenadasPalabra.get(0).getSecond(), true);
            if (puntosTotales < 0) {
                return 0;
            }
        }

        // Validate that at least one tile is connected to an existing word (if not first turn)
        int puntos;
        // Check for perpendicular words formed by each placed tile
        for (Pair<Integer, Integer> p : coordenadasPalabra) {

            if (isHorizontalLine) {
                puntos = recorrerDireccion(p.getFirst(), p.getSecond(), true);

                if (puntos < 0) {
                    return 0;
                } else {
                    puntosTotales += puntos;
                }
            } else {

                puntos = recorrerDireccion(p.getFirst(), p.getSecond(), false);

                if (puntos < 0) {
                    return 0;
                } else {
                    puntosTotales += puntos;
                }
            }
        }

        if (!hayBloqueada && contadorTurno > 0) {

            return 0;
        }

        if (coordenadasPalabra.size() == 7) {
            puntosTotales += 50;
        }
        return puntosTotales;
    }

    /**
     * Recorre una dirección específica desde una posición dada (horizontal o
     * vertical), construyendo una palabra y calculando su puntuación teniendo
     * en cuenta bonificaciones de celda.
     *
     * También determina si alguna de las fichas colocadas es nueva (bloqueada),
     * lo cual es necesario para validar la jugada.
     *
     * @param xx Fila inicial desde donde comienza el recorrido.
     * @param yy Columna inicial desde donde comienza el recorrido.
     * @param vertical {@code true} para recorrer verticalmente, {@code false}
     * para horizontalmente.
     * @return Puntuación de la palabra construida si es válida, {@code 0} si
     * solo se trata de una letra, o {@code -1} si la palabra no es válida.
     */
    private int recorrerDireccion(int xx, int yy, boolean vertical) {
        int puntosLinea = 0;
        StringBuilder palabra = new StringBuilder();
        StringBuilder palabra2 = new StringBuilder();
        int x = xx, y = yy;
        int puntosFicha = 0;
        int dx;
        int dy;
        int bonificador = 0;

        if (vertical) {
            dx = 1;
            dy = 0;
        } else {
            dx = 0;
            dy = 1;
        }
        // Recorre en ambas direcciones
        for (int dir = -1; dir <= 1; dir += 2) {
            x = xx;
            y = yy;

            while (tablero.getCelda(x, y) != null && tablero.getFicha(x, y) != null) {
                String letra = tablero.getFicha(x, y).getLetra();
                if (dir == 1 || (x != xx || y != yy)) {
                    if (dir == 1) {
                        palabra.append(letra);
                    } else {
                        palabra2.insert(0, letra);
                    }
                    puntosFicha = tablero.getFicha(x, y).getPuntuacion();
                    if (tablero.getCelda(x, y).estaBloqueada()) {
                        hayBloqueada = true;
                    } else {

    
                        if (tablero.getCelda(x, y).isDobleTripleLetra()) {
                            puntosFicha *= tablero.getCelda(x, y).getBonificacion().getMultiplicador();
                        }
                        if (tablero.getCelda(x, y).isDobleTriplePalabra()) {

                            bonificador += tablero.getCelda(x, y).getBonificacion().getMultiplicador();
                        }
                    }

                    puntosLinea += puntosFicha;
                }

                x += dir * dx;
                y += dir * dy;
            }
        }
        if (bonificador > 0) {

            puntosLinea *= bonificador;
        }

        String palabraFinal = palabra2.toString() + palabra.toString();
        List<String> s = diccionario.tokenizarPalabra(palabraFinal);
        if (diccionario.buscarPalabra(palabraFinal)) {
            return puntosLinea;
        } else if (s.size() == 1) {
            return 0;
        }

        return -1;

    }

}
