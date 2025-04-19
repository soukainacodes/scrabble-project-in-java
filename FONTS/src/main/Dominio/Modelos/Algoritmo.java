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

public class Algoritmo {

    private int puntosFinal;
    private List<String> fichass;
    private List<Ficha> f;
    private Dawg diccionario;
    private Tablero tablero;
    private List<Pair<Ficha, Pair<Integer, Integer>>> resultadoFinal;
    private boolean vertical;

    public Algoritmo() {

    }

    private boolean isEmpty(Pair<Integer, Integer> pos) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        return (tablero.getCelda(x, y) != null && !tablero.getCelda(x, y).estaOcupada());
    }

    private boolean isFilled(Pair<Integer, Integer> pos) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        return (tablero.getCelda(x, y) != null && tablero.getCelda(x, y).estaOcupada());
    }

    private boolean isDentroTablero(Pair<Integer, Integer> pos) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        return (tablero.getCelda(x, y) != null);
    }

    private ArrayList<Pair<Integer, Integer>> find_anchors() {
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

    private Pair<Integer, Integer> before(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();
        if (!vertical) {
            return Pair.createPair(row, col - 1);
        } else {
            return Pair.createPair(row - 1, col);
        }
    }

    private Pair<Integer, Integer> after(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();
        if (!vertical) {
            return Pair.createPair(row, col + 1);
        } else {
            return Pair.createPair(row + 1, col);
        }

    }

    private Pair<Integer, Integer> up(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();

        return Pair.createPair(row - 1, col);
    }

    private Pair<Integer, Integer> down(Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();
        return Pair.createPair(row + 1, col);
    }

    private void ponerFicha(String s, Pair<Integer, Integer> pos) {
        int row = pos.getFirst();
        int col = pos.getSecond();
        for (Ficha ficha : f) {
            if (ficha.getLetra().equals(s)) {
                resultadoFinal.add(Pair.createPair(ficha, pos));
                break;
            }
        }
    }

    private void palabra_parcial(String palabra, Pair<Integer, Integer> last_pos, int puntos) {

        Pair<Integer, Integer> play_pos = last_pos;

        int bonusPalabra = 0;
        int puntosPalabra = 0;
        List<String> palabraTokenizada = diccionario.tokenizarPalabra(palabra);
        int lenght = palabraTokenizada.size() - 1;
        while (lenght >= 0) {

            puntosPalabra += getFichaPuntuacion(last_pos);
         
            int p = 0;
            for (Ficha ficha : f) {
                if (ficha.getLetra().contains(palabraTokenizada.get(lenght))) {
                    
                    p = ficha.getPuntuacion();
                    if (tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).isDobleTripleLetra()) {
                        p *= tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).getBonificacion().getMultiplicador();
                    }
                    puntosPalabra += p;
                    if (tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).isDobleTriplePalabra()) {
                        bonusPalabra += tablero.getCelda(last_pos.getFirst(), last_pos.getSecond()).getBonificacion().getMultiplicador();
                    }

                    break;
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
            System.out.println(palabra);
            resultadoFinal.clear();

          
            while (lenght >= 0) {
                ponerFicha(palabraTokenizada.get(lenght), play_pos);
                lenght--;

                play_pos = before(play_pos);
            }
        }
    }

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

                puntosRecorrerDireccion += getFichaPuntuacion(p);

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
            // System.out.println("he estado: " + palabraCompleta + " " + puntosRecorrerDireccion );
            return puntosRecorrerDireccion;
        } else {
            return -1;
        }

    }

    private int getFichaPuntuacion(Pair<Integer, Integer> pos) {
        int x = pos.getFirst();
        int y = pos.getSecond();
        if (tablero.getFicha(x, y) != null) {

            int p = tablero.getFicha(x, y).getPuntuacion();
            return p;
        }
        return 0;
    }

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
                            fichass.remove(s);

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
                    fichass.remove(s);
                    before_part(partial_word + s, nodo_actual.getHijos().get(s), pos, limit - 1, puntos);
                    fichass.add(s);
                }
            }

        }

    }

    public Pair<List<Pair<Ficha, Pair<Integer, Integer>>>, Integer> find_all_words(List<Ficha> fichas, Dawg diccionario, Tablero tablero) {

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
          ArrayList<Pair<Integer, Integer>> anchors = new ArrayList<>();
          anchors = find_anchors();
           if (anchors.size() == 0) {
                anchors.add(Pair.createPair(7, 7));
            }
        for (int i = 0; i < 2; ++i) {
            if (i == 0) {
                vertical = false;
            } else {
                vertical = true;
            }

          
           
            for (Pair<Integer, Integer> pos : anchors) {
                int puntos = 0;
                int longitud = 0;
                if (isFilled(before(pos))) {

                    Pair<Integer, Integer> scan_pos = before(pos);
                    puntos = getFichaPuntuacion(scan_pos);

                    String partial_word = getFicha(scan_pos);

                    while (isFilled(before(scan_pos))) {

                        scan_pos = before(scan_pos);
                        puntos += getFichaPuntuacion(scan_pos);
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

        }

        return Pair.createPair(resultadoFinal, puntosFinal);

    }
}
