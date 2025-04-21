package Dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.naming.spi.DirStateFactory;
import javax.swing.Painter;

import Dominio.Modelos.*;
import Dominio.Excepciones.*;

public class CtrlPartida {

    private Partida partidaActual;
    private Dawg dawg;
    private Validador validador;
    private boolean finTurno;

    private Tablero tablero;
    private Bolsa bolsa;
    private boolean isAlgoritmo;
    private Algoritmo algoritmo;

    // Constructor
    public CtrlPartida() {

        this.validador = new Validador();

        //Dar fichas para jugador 1
        //Dar fichas para jugador 2
    }

    public void crearPartida(int modo, List<String> players, List<String> lineasArchivo, List<String> lineasArchivoBolsa, long seed) {
        this.dawg = new Dawg(lineasArchivoBolsa, lineasArchivo);

        this.partidaActual = new Partida(players, lineasArchivoBolsa, seed);
        this.finTurno = false;

        if (modo == 0) {
            this.isAlgoritmo = true;
            this.algoritmo = new Algoritmo();
            if (partidaActual.getTurnoJugador() == false) {
                //partidaActual.addPuntos(jugarAlgoritmo());
                //finTurno(true,false);
            }
        } else {
            this.isAlgoritmo = false;
        }

    }

    public List<String> obtenerFichas() {
        return partidaActual.obtenerFichas();
    }

    public void cargarPartida(Partida partida) {
        this.partidaActual = partida;
    }

    public Partida guardarPartida() {
        return partidaActual;
    }

    public int jugarScrabble(int opcion, String input) throws PosicionOcupadaTablero, PosicionVaciaTablero, FichaIncorrecta {

        String[] parts = input.split(" ");

        switch (opcion) {
            case 1: {
                //Poner ficha en el tablero
                String ficha = parts[0];
                System.out.println(parts[0]);
                if (ficha.matches("[0-7]")) {
                    int num = Integer.parseInt(ficha);
                    ficha = partidaActual.obtenerFichas().get(num);
                }
                int x;
                int y;

                if (ficha.matches("#") && parts.length == 4) {
                    ficha = parts[1];
                    x = Integer.parseInt(parts[2]);
                    y = Integer.parseInt(parts[3]);
                } else {
                    x = Integer.parseInt(parts[1]);
                    y = Integer.parseInt(parts[2]);
                }
                partidaActual.añadirFicha(ficha, x, y);
                break;
            }
            case 2: {
                //Quitar ficha del tablero
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                partidaActual.quitarFichaTablero(x, y);
                break;
            }
            case 3: // Pasar turno
                if (partidaActual.getCoordenadasPalabras().size() != 0) {
                    for (Pair<Integer, Integer> p : partidaActual.getCoordenadasPalabras()) {
                        partidaActual.quitarFichaTablero(p.getFirst(), p.getSecond());
                    }
                }
                //Torna pasar + algoritmo
                return finTurno(true, true);
            case 4: // Fin turno

                return finTurno(false, true);
            case 5: // cambiar fichas
                if (partidaActual.getCoordenadasPalabras().size() != 0) {
                    for (Pair<Integer, Integer> p : partidaActual.getCoordenadasPalabras()) {
                        partidaActual.quitarFichaTablero(p.getFirst(), p.getSecond());
                    }
                }
                List<String> fichas = new ArrayList<>(Arrays.asList(parts));
                for (int i = 0; i < fichas.size(); ++i) {
                    if (fichas.get(i).matches("[0-7]")) {
                        int num = Integer.parseInt(fichas.get(i));
                        fichas.add(partidaActual.getFichasJugador().get(num).getLetra());
                        fichas.remove(i);
                    }
                }

                partidaActual.recuperarFichas();
                return finTurno(true, true);
            case 7:
                System.out.println("Jugador: " + partidaActual.getTurnoJugador()); 
                int puntosTotales = jugarAlgoritmo();
                partidaActual.addPuntos(puntosTotales);
                //System.out.println("Validador " + puntosTotales);
                return finTurno(false, false);

        }
        return 0;

    }

    public int getPuntosJugador1() {
        return partidaActual.getPuntosJugador1();
    }

    public int getPuntosJugador2() {
        return partidaActual.getPuntosJugador2();
    }



    public int finTurno(boolean pasar, boolean algoritmo) throws PosicionOcupadaTablero, FichaIncorrecta {

        if (!pasar) {
            int puntos = validador.validarPalabra(partidaActual.getCoordenadasPalabras(), dawg, partidaActual.getTablero(), partidaActual.getContadorTurno());
            if (puntos > 0) {
                System.out.println("Puntos Validador:" + puntos);
                //partidaActual.addPuntos(puntos);
            } else {
                System.out.println("Palabra incorrecta!!");
                return 0;

            }
        }

        partidaActual.bloquearCeldas();
        partidaActual.coordenadasClear();
        partidaActual.cambiarTurnoJugador();
        partidaActual.aumentarContador();
        if (!partidaActual.recuperarFichas() && partidaActual.getListSize() == 0) {
            return finPartida();
        }

        if (isAlgoritmo && algoritmo) {
            int puntosAlgoritmo = jugarAlgoritmo();
            partidaActual.addPuntos(puntosAlgoritmo);
            if(puntosAlgoritmo == 0 && partidaActual.isBolsaEmpty() && partidaActual.getPuntosJugador1() > partidaActual.getPuntosJugador2()){
                return finPartida();
            }
            finTurno(true, false);
        }
        return 0;
    }



    public int finPartida() {
        partidaActual.cambiarTurnoJugador();
        for (Ficha ficha : partidaActual.getFichasJugador()) {
            partidaActual.addPuntos(-ficha.getPuntuacion());
        }

        if (partidaActual.getPuntosJugador1() > partidaActual.getPuntosJugador2()) {
            return 1;
        } else {
            return 2;
        }
    }

    private int jugarAlgoritmo() throws PosicionOcupadaTablero, FichaIncorrecta {
        Pair<List<Pair<String, Pair<Integer, Integer>>>, Integer> ss = algoritmo.find_all_words(partidaActual.getFichasJugador(), dawg, partidaActual.getTablero());
        List<Pair<String, Pair<Integer, Integer>>> s = ss.getFirst();
        System.out.print(partidaActual.obtenerFichas());
        for (Pair<String, Pair<Integer, Integer>> aa : s) {
            System.out.println("Letra algo: " + aa.getFirst());
            partidaActual.añadirFicha(aa.getFirst(), aa.getSecond().getFirst(), aa.getSecond().getSecond());

        }
        if (s.size() == 0) {
            return 0;
        }
        return ss.getSecond();
    }

    public Tablero obtenerTablero() {
        return partidaActual.getTablero();
    }

}
