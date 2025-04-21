package Dominio.Modelos;

import java.util.List;

import Dominio.Excepciones.PalabraInvalidaException;

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


public int validarPalabra(
    List<Pair<Integer, Integer>> coordenadasPalabra,
    Dawg diccionario,
    Tablero tablero,
    int contadorTurno
) throws PalabraInvalidaException {

    this.hayBloqueada = false;
    this.tablero = tablero;
    this.diccionario = diccionario;

    if (coordenadasPalabra.isEmpty()) {
        throw new PalabraInvalidaException();
    }

    int puntosTotales = 0;

    if (coordenadasPalabra.size() == 1) {
        if (contadorTurno == 0) throw new PalabraInvalidaException();

        Pair<Integer, Integer> coord = coordenadasPalabra.get(0);
        puntosTotales += recorrerDireccion(coord.getFirst(), coord.getSecond(), false);
        puntosTotales += recorrerDireccion(coord.getFirst(), coord.getSecond(), true);
        return puntosTotales;
    }

    boolean isHorizontalLine = true;
    boolean isVerticalLine = true;
    boolean touchesCenterTile = false;

    int refX = coordenadasPalabra.get(0).getFirst();
    int refY = coordenadasPalabra.get(0).getSecond();

    for (Pair<Integer, Integer> p : coordenadasPalabra) {
        if (p.getFirst() != refX) isHorizontalLine = false;
        if (p.getSecond() != refY) isVerticalLine = false;
        if (tablero.esCentroDelTablero(p.getFirst(), p.getSecond())) touchesCenterTile = true;
    }

    if (!isHorizontalLine && !isVerticalLine) throw new PalabraInvalidaException();
    if (contadorTurno == 0 && !touchesCenterTile) throw new PalabraInvalidaException();

    int puntosPrincipales = isHorizontalLine
        ? recorrerDireccion(refX, refY, false)
        : recorrerDireccion(refX, refY, true);

    if (puntosPrincipales < 0) throw new PalabraInvalidaException();
    puntosTotales += puntosPrincipales;

    for (Pair<Integer, Integer> p : coordenadasPalabra) {
        int puntos = isHorizontalLine
            ? recorrerDireccion(p.getFirst(), p.getSecond(), true)
            : recorrerDireccion(p.getFirst(), p.getSecond(), false);

        if (puntos < 0) throw new PalabraInvalidaException();
        puntosTotales += puntos;
    }

    if (!hayBloqueada && contadorTurno > 0) {
        throw new PalabraInvalidaException();
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
