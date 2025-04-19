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

    private boolean doblePalabra;
    private Dawg diccionario;
    private boolean hayBloqueada;

    public Validador() {
        //Deberia recibir el DAWG como parametro

    }

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
           // System.out.println("Linea horizontal: " + puntosTotales);
            if (puntosTotales < 0) {
                return 0;
            }

        } else { // isVerticalLine
            puntosTotales += recorrerDireccion(coordenadasPalabra.get(0).getFirst(), coordenadasPalabra.get(0).getSecond(), true);
           // System.out.println("Linea vertical: " + puntosTotales);
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
                 
                if(puntos < 0) return 0;
                else puntosTotales += puntos;
            } else {
                 
                puntos = recorrerDireccion(p.getFirst(), p.getSecond(), false);
                
                if(puntos < 0) return 0;
                else puntosTotales += puntos;
            }
        }

         if (!hayBloqueada && contadorTurno > 0) {
         
            return 0;
        }

        if(coordenadasPalabra.size() == 7) puntosTotales +=50;
        System.out.println("Validador " + puntosTotales);
        return puntosTotales;
    }

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

                        System.out.println(letra);
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

        if (diccionario.buscarPalabra(palabraFinal)) {
            return puntosLinea;
        }
        else if(palabraFinal.length() == 1) return 0;
        
        return -1;

    }

}
