package Dominio.Modelos;

import java.util.ArrayList;
import java.util.List;

public class Validador {

    private Partida partida;

    private Bolsa bolsa;
    private boolean finTurno;
    private boolean prueba;
    private int ejex, ejey;
    private int contadorTurno;
    private Tablero tablero;
    private int puntosLinea;
    private boolean doblePalabra;

    private boolean hayBloqueada;

    public Validador() {
        //Deberia recibir el DAWG como parametro

        this.puntosLinea = 0;
    }

    public int validarPalabra(List<Pair<Integer, Integer>> coordenadasPalabra, Dawg diccionario, Tablero tablero, int contadorTurno) {
        // If no new tiles were placed
        this.tablero = tablero;

        if (coordenadasPalabra.isEmpty()) {
            return 0;
        }

        List<String> palabras = new ArrayList<>();

        // Special case: only one tile placed
        if (coordenadasPalabra.size() == 1) {

            if (contadorTurno == 0) {

                return 0; // First turn must have more than one tile
            }

            // Check both horizontal and vertical words formed with this single tile
            Pair<Integer, Integer> coord = coordenadasPalabra.get(0);
            int puntosTotal = 0;

            String horizontalWord = recorrerDireccion(coord.getFirst(), coord.getSecond(), false);
            if (diccionario.buscarPalabra(horizontalWord)) {
                palabras.add(horizontalWord);
                puntosTotal += puntosLinea;
            }

            String verticalWord = recorrerDireccion(coord.getFirst(), coord.getSecond(), true);
            if (diccionario.buscarPalabra(verticalWord)) {
                palabras.add(verticalWord);
                puntosTotal += puntosLinea;
            }

            return palabras.isEmpty() ? 0 : puntosTotal;
        }

        // Reset modifiers for scoring
        doblePalabra = false;

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
        hayBloqueada = false;
        int puntosTotales = 0;

        // Get the main word based on line direction
        String mainWord;
        if (isHorizontalLine) {
            mainWord = recorrerDireccion(coordenadasPalabra.get(0).getFirst(),
                    coordenadasPalabra.get(0).getSecond(), false);
                System.out.println("MainH: " + mainWord);
            if (!diccionario.buscarPalabra(mainWord)) {
                System.out.println("No en diccionario");
                return 0;
            }
            palabras.add(mainWord);
            puntosTotales += puntosLinea;
        } else { // isVerticalLine
            mainWord = recorrerDireccion(coordenadasPalabra.get(0).getFirst(),
                    coordenadasPalabra.get(0).getSecond(), true);
                System.out.println("MainV: " + mainWord);
            if (!diccionario.buscarPalabra(mainWord)) {
                System.out.println("No en diccionario");
                return 0;
            }
            palabras.add(mainWord);
            puntosTotales += puntosLinea;
        }

        // Validate that at least one tile is connected to an existing word (if not first turn)
        if (!hayBloqueada && contadorTurno > 0) {
            System.out.println("No hay bloqueada");
            return 0;
        }

        // Check for perpendicular words formed by each placed tile
        for (Pair<Integer, Integer> p : coordenadasPalabra) {
            String crossWord;

            if (isHorizontalLine) {
                crossWord = recorrerDireccion(p.getFirst(), p.getSecond(), true);
            } else {
                crossWord = recorrerDireccion(p.getFirst(), p.getSecond(), false);
            }

            // Only consider words with length > 1 (single letters aren't words)
            if (crossWord.length() > 1 && diccionario.buscarPalabra(crossWord)) {
                System.out.println("Cross: " + crossWord);
                palabras.add(crossWord);
                puntosTotales += puntosLinea;
            }
        }

        return puntosTotales;
    }

    private String recorrerDireccion(int xx, int yy, boolean vertical) {
        this.puntosLinea = 0;
        StringBuilder palabra = new StringBuilder();
        StringBuilder palabra2 = new StringBuilder();
        int x = xx, y = yy;
        int puntosFicha = 0;
        int dx;
        int dy;
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
                    }

                    if (tablero.getCelda(x, y).isDobleTripleLetra()) {
                        puntosFicha *= tablero.getCelda(x, y).getBonificacion().getMultiplicador();
                    }
                    if (tablero.getCelda(x, y).isDobleTriplePalabra()) {
                        doblePalabra = true;
                    }
                    puntosLinea += puntosFicha;
                }

                x += dir * dx;
                y += dir * dy;
            }
        }
        System.out.println(palabra2.toString() + palabra.toString());
        return palabra2.toString() + palabra.toString();

    }

}
