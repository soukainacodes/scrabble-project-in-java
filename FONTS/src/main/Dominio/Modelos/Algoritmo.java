package Dominio.Modelos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.Painter;
import javax.swing.plaf.basic.BasicTreeUI;

import jdk.swing.interop.DragSourceContextWrapper;

/**
 * Se encarga de buscar y construir jugadas posibles en el juego de Scrabble.
 * <p>
 * Utiliza las fichas del jugador, el tablero y un diccionario DAWG para formar palabras válidas
 * que respeten las reglas del juego. Calcula la puntuación de cada jugada y selecciona la mejor.
 */

public class Algoritmo {

    private int puntosFinal;
    private List<String> fichass;
    private List<Ficha> f;
    private Dawg diccionario;
    private Tablero tablero;
    private List<Pair<String, Pair<Integer, Integer>>> resultadoFinal;
    private boolean vertical;

    /**
     * Constructor por defecto del algoritmo. Inicializa la instancia sin
     * realizar acciones adicionales.
     */
    public Algoritmo() {

    }

    /**
     * Comprueba si una celda en una posición determinada del tablero está
     * vacía.
     *
     * @param pos Par de coordenadas (fila, columna) a verificar.
     * @return {@code true} si la celda existe y no está ocupada, {@code false}
     * en caso contrario.
     */
    public boolean isEmpty(Pair<Integer, Integer> pos) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        return (tablero.getCelda(x, y) != null && !tablero.getCelda(x, y).estaOcupada());
    }

    /**
     * Comprueba si una celda en una posición determinada del tablero está
     * ocupada.
     *
     * @param pos Par de coordenadas (fila, columna) a verificar.
     * @return {@code true} si la celda existe y está ocupada, {@code false} en
     * caso contrario.
     */
    public boolean isFilled(Pair<Integer, Integer> pos) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        return (tablero.getCelda(x, y) != null && tablero.getCelda(x, y).estaOcupada());
    }

    /**
     * Verifica si una posición se encuentra dentro de los límites del tablero.
     *
     * @param pos Par de coordenadas (fila, columna) a comprobar.
     * @return {@code true} si la celda en la posición indicada existe,
     * {@code false} en caso contrario.
     */
    public boolean isDentroTablero(Pair<Integer, Integer> pos) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        return (tablero.getCelda(x, y) != null);
    }

    /**
     * Busca todas las posiciones del tablero que pueden actuar como anclas, es
     * decir, celdas vacías adyacentes a al menos una celda ocupada.
     *
     * @return Lista de posiciones (pares fila, columna) que son válidas como
     * anclas.
     */
    public ArrayList<Pair<Integer, Integer>> find_anchors() {
        ArrayList<Pair<Integer, Integer>> anchors = new ArrayList<>();
        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 15; j++) {
                Pair<Integer, Integer> pos = Pair.createPair(i, j);
                boolean empty = isEmpty(pos);
                boolean vecinoOcupado = (isFilled(before(pos)) || isFilled(after(pos)) || isFilled(up(pos)) || isFilled(down(pos)));

                if (empty && vecinoOcupado) {

                    anchors.add(pos);
                }
            }
        }
        return anchors;
    }

    /**
     * Calcula la posición anterior a la dada, dependiendo de la orientación
     * actual (horizontal o vertical).
     *
     * @param pos Posición de referencia.
     * @return Posición anterior según la orientación del algoritmo.
     */
    private Pair<Integer, Integer> before(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();
        if (!vertical) {
            return Pair.createPair(row, col - 1);
        } else {
            return Pair.createPair(row - 1, col);
        }
    }

    /**
     * Calcula la posición siguiente a la dada, dependiendo de la orientación
     * actual (horizontal o vertical).
     *
     * @param pos Posición de referencia.
     * @return Posición siguiente según la orientación del algoritmo.
     */
    private Pair<Integer, Integer> after(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();
        if (!vertical) {
            return Pair.createPair(row, col + 1);
        } else {
            return Pair.createPair(row + 1, col);
        }

    }

    /**
     * Obtiene la posición directamente arriba de la posición dada.
     *
     * @param pos Posición de referencia.
     * @return Par de coordenadas correspondiente a la celda superior.
     */
    private Pair<Integer, Integer> up(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();

        return Pair.createPair(row - 1, col);
    }

    /**
     * Obtiene la posición directamente debajo de la posición dada.
     *
     * @param pos Posición de referencia.
     * @return Par de coordenadas correspondiente a la celda inferior.
     */
    private Pair<Integer, Integer> down(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();
        return Pair.createPair(row + 1, col);
    }

    /**
     * Añade una ficha representada por una cadena en una posición específica al
     * resultado final.
     *
     * @param s Letra o símbolo que representa la ficha.
     * @param pos Posición en la que se colocará la ficha.
     */
    private void ponerFicha(String s, Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();

        resultadoFinal.add(Pair.createPair(s, pos));

    }

    /**
     * Calcula la puntuación de una palabra parcial colocada en el tablero,
     * comenzando desde una posición dada y yendo hacia atrás, teniendo en
     * cuenta las bonificaciones de letra y palabra, así como el uso completo de
     * las fichas.
     *
     * Si la puntuación total obtenida es mayor que la mejor registrada hasta el
     * momento, actualiza el resultado final.
     *
     * @param palabra Palabra a evaluar.
     * @param last_pos Posición final de la palabra (última letra colocada).
     * @param puntos Puntuación acumulada antes de colocar esta palabra.
     */
    private void palabra_parcial(String palabra, Pair<Integer, Integer> last_pos, int puntos) {

        Pair<Integer, Integer> play_pos = last_pos;

        int bonusPalabra = 0;
        int puntosPalabra = 0;
        List<String> palabraTokenizada = diccionario.tokenizarPalabra(palabra);
        int lenght = palabraTokenizada.size() - 1;
        while (lenght >= 0) {

            if (tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).estaBloqueada()) {
                puntosPalabra += getFichaPuntuacion(last_pos);
            } else {
                int p;
                for (Ficha ficha : f) {
                    if (ficha.getLetra().equals(palabraTokenizada.get(lenght))) {

                        p = ficha.getPuntuacion();
                        if (tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).isDobleTripleLetra()) {
                            p *= tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).getBonificacion().getMultiplicador();
                        }

                        puntosPalabra += p;
                        break;
                    }

                }
                if (tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).isDobleTriplePalabra() && tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).bonusDisponible()) {
                    bonusPalabra += tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).getBonificacion().getMultiplicador();
                }
            }

            last_pos = before(last_pos);
            lenght--;
        }

        if (bonusPalabra != 0) {
            puntosPalabra *= bonusPalabra;
        }

        puntos += puntosPalabra;
        if (fichass.size() == 0) {
            puntos += 50;
        }

        lenght = palabraTokenizada.size() - 1;
        if (puntos > puntosFinal) {
            puntosFinal = puntos;

            resultadoFinal.clear();

            while (lenght >= 0) {
                ponerFicha(palabraTokenizada.get(lenght), play_pos);
                lenght--;

                play_pos = before(play_pos);
            }
        }
    }

    /**
     * Recorre una dirección horizontal o vertical desde una posición dada para
     * formar una palabra completa con la letra especificada, evaluando si la
     * palabra resultante es válida y calculando su puntuación.
     *
     * Se consideran las bonificaciones de letra y palabra, así como los puntos
     * base de las fichas ya colocadas en el tablero.
     *
     * @param pos Posición donde se colocará la nueva ficha.
     * @param direccion {@code true} para recorrer en dirección horizontal,
     * {@code false} para vertical.
     * @param s Letra que se quiere colocar en la posición indicada.
     * @return Puntuación total de la palabra si es válida, o {@code -1} si la
     * palabra no existe en el diccionario.
     */
    private int recorrerDireccion(Pair<Integer, Integer> pos, boolean direccion, String s) {
        StringBuilder palabra = new StringBuilder();
        StringBuilder palabra2 = new StringBuilder();
        int xx = pos.getFirst();
        int yy = pos.getSecond();
        int x = xx, y = yy;
        int puntosFicha = 0;
        int dx, dy;
        int puntosRecorrerDireccion = 0;

        // Determine direction of traversal
        if (direccion) {
            dx = 1; // horizontal
            dy = 0;
        } else {
            dx = 0; // vertical
            dy = 1;
        }

        // Recorre en ambas direcciones
        for (int dir = -1; dir <= 1; dir += 2) {
            x = xx;
            y = yy;
            StringBuilder currentPalabra = (dir == -1) ? palabra2 : palabra;

            while (true) {
                // Adjust coordinates before checking
                x += dir * dx;
                y += dir * dy;

                // Check if cell is out of bounds or empty
                if (x < 0 || y < 0
                        || tablero.getCelda(x, y) == null
                        || tablero.getFicha(x, y) == null) {
                    break;
                }

                String letra = tablero.getFicha(x, y).getLetra();
                Pair<Integer, Integer> p = Pair.createPair(x, y);
                if (tablero.getCelda(p.getFirst(), p.getSecond()).estaBloqueada()) {
                    puntosRecorrerDireccion += getFichaPuntuacion(p);
                }

                // Append letter to appropriate StringBuilder
                if (dir == 1) {
                    currentPalabra.append(letra);
                } else {
                    currentPalabra.insert(0, letra);
                }

            }
        }

        String palabraCompleta = palabra2.toString() + s + palabra.toString();
        boolean esPalabra = diccionario.buscarPalabra(palabraCompleta);
        if (esPalabra || palabraCompleta.length() == 1) {

            if (esPalabra) {
                Pair<Integer, Integer> p = Pair.createPair(xx, yy);
                int pp = 0;
                for (Ficha ficha : f) {
                    if (ficha.getLetra().equals(s)) {
                        pp = ficha.getPuntuacion();
                        if (tablero.getCelda(xx, yy).isDobleTripleLetra()) {
                            pp *= tablero.getCelda(xx, yy).getBonificacion().getMultiplicador();
                        }
                        break;
                    }
                }
                puntosRecorrerDireccion += pp;

                if (tablero.getCelda(xx, yy).isDobleTriplePalabra()) {
                    puntosRecorrerDireccion *= tablero.getCelda(xx, yy).getBonificacion().getMultiplicador();
                }
            }

            return puntosRecorrerDireccion;
        } else {
            return -1;
        }

    }

    /**
     * Obtiene la puntuación de la ficha colocada en una posición determinada
     * del tablero.
     *
     * @param pos Posición (fila, columna) de la ficha.
     * @return Puntuación de la ficha si existe, o {@code 0} si no hay ninguna
     * ficha en esa celda.
     */
    private int getFichaPuntuacion(Pair<Integer, Integer> pos) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        if (tablero.getFicha(x, y) != null) {

            int p = tablero.getFicha(x, y).getPuntuacion();
            return p;
        }
        return 0;
    }

    /**
     * Extiende una palabra parcialmente construida hacia adelante desde una
     * posición dada, explorando todas las posibles letras que pueden formar una
     * palabra válida desde el nodo actual del DAWG.
     *
     * Si se alcanza una palabra válida en una celda vacía después del ancla,
     * evalúa la puntuación correspondiente.
     *
     * @param palabraParcial Palabra construida hasta el momento.
     * @param nodo_actual Nodo actual en el DAWG que representa el prefijo
     * construido.
     * @param next_pos Siguiente posición en el tablero a explorar.
     * @param anchor_filled Indica si se ha pasado por una celda ancla válida.
     * @param puntos Puntuación acumulada hasta el momento.
     */
    private void extend_after(String palabraParcial, Nodo nodo_actual, Pair<Integer, Integer> next_pos, boolean anchor_filled, int puntos) {
        if (!isFilled(next_pos) && nodo_actual.esValida() && anchor_filled) {
            palabra_parcial(palabraParcial, before(next_pos), puntos);
        }
        if (isDentroTablero(next_pos)) {
            if (isEmpty(next_pos)) {
                for (String s : nodo_actual.getHijos().keySet()) {

                    if (fichass.contains("#") || fichass.contains(s)) {
                        int b = recorrerDireccion(next_pos, !vertical, s);
                        if (b != -1) {
                            if (fichass.contains(s)) {
                                fichass.remove(s);
                            } else {
                                fichass.remove("#");
                            }

                            extend_after(palabraParcial + s, nodo_actual.getHijos().get(s), after(next_pos), true, puntos + b);

                            fichass.add(s);
                        }
                    }

                }
            } else {
                String existing_letter = getFicha(next_pos);

                if (nodo_actual.getHijos().containsKey(existing_letter)) {

                    extend_after(palabraParcial + existing_letter, nodo_actual.getHijos().get(existing_letter), after(next_pos), true, puntos);

                }
            }
        }
    }

    private String getFicha(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();

        return tablero.getFicha(row, col).getLetra();

    }

    private void before_part(String partial_word, Nodo nodo_actual, Pair<Integer, Integer> pos, int limit, int puntos) {

        extend_after(partial_word, nodo_actual, pos, false, puntos);
        if (limit > 0) {
            for (String s : nodo_actual.getHijos().keySet()) {
                if (fichass.contains("#") || fichass.contains(s)) {
                    if (fichass.contains(s)) {
                        fichass.remove(s);
                    } else {
                        fichass.remove("#");
                    }
                    before_part(partial_word + s, nodo_actual.getHijos().get(s), pos, limit - 1, puntos);
                    fichass.add(s);
                }
            }

        }

    }

    /**
     * Encuentra la mejor jugada posible en el tablero utilizando las fichas
     * disponibles del jugador, evaluando todas las combinaciones válidas que se
     * pueden formar desde las posiciones ancla.
     *
     * Utiliza un DAWG para validar palabras y recorre el tablero tanto
     * horizontal como verticalmente para construir palabras completas, tomando
     * en cuenta las bonificaciones de celda.
     *
     * @param fichas Lista de fichas disponibles del jugador.
     * @param diccionario Estructura DAWG que contiene todas las palabras
     * válidas.
     * @param tablero Estado actual del tablero de juego.
     * @return Par compuesto por la lista de jugadas (letra y posición) que
     * forman la mejor palabra encontrada y su puntuación total.
     */
    public Pair<List<Pair<String, Pair<Integer, Integer>>>, Integer> find_all_words(List<Ficha> fichas, Dawg diccionario, Tablero tablero) {

        this.resultadoFinal = new ArrayList<>();
        this.tablero = tablero;
        this.fichass = new ArrayList<>();
        this.f = fichas;
        for (Ficha f : fichas) {
            this.fichass.add(f.getLetra());
        }

        this.diccionario = diccionario;
        //fichas del jugador
        puntosFinal = 0;
        vertical = false;
        ArrayList<Pair<Integer, Integer>> anchors = find_anchors();
        if (anchors.size() == 0) {
            anchors.add(Pair.createPair(7, 7));
        }

        for (Pair<Integer, Integer> pos : anchors) {

            for (int i = 0; i < 2; ++i) {
                if (i == 0) {
                    vertical = false;
                } else {
                    vertical = true;
                }
                int puntos = 0;
                int longitud = 0;
                if (isFilled(before(pos))) {

                    Pair<Integer, Integer> scan_pos = before(pos);

                    String partial_word = getFicha(scan_pos);

                    while (isFilled(before(scan_pos))) {

                        scan_pos = before(scan_pos);

                        partial_word = getFicha(scan_pos) + partial_word;
                    }

                    Nodo pw_node = diccionario.buscarUltimoNodo(partial_word);

                    if (pw_node != null) {

                        extend_after(partial_word, pw_node, pos, false, puntos);

                    }
                } else {
                    int limit = 0;
                    Pair<Integer, Integer> scan_pos = pos;

                    while (isEmpty(before(scan_pos)) && !anchors.contains(before(scan_pos))) {
                        limit++;
                        scan_pos = before(scan_pos);
                    }
                    before_part("", diccionario.getRaiz(), pos, limit, 0);
                }

            }
            if (resultadoFinal.size() != 0) {
                return Pair.createPair(resultadoFinal, puntosFinal);
            }
        }
        return Pair.createPair(resultadoFinal, puntosFinal);
    }
}
