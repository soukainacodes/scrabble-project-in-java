package Dominio;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Painter;

import Dominio.Modelos.*;

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

    public void crearPartida(int modo, List<String> players, List<String> lineasArchivo, List<String> lineasArchivoBolsa) {
        this.dawg = new Dawg();
        dawg.cargarFichasValidas(lineasArchivoBolsa);
        dawg.construirDesdeArchivo(lineasArchivo);
     
        this.partidaActual = new Partida(players, lineasArchivoBolsa);
        this.finTurno = false;

        if (modo == 1) {
            this.isAlgoritmo = true;
            this.algoritmo = new Algoritmo();
            if (partidaActual.getTurnoJugador() == false) {

                partidaActual.addPuntos(jugarAlgoritmo());
                partidaActual.bloquearCeldas();
                partidaActual.cambiarTurnoJugador();
                partidaActual.coordenadasClear();
                partidaActual.recuperarFichas();    
                partidaActual.aumentarContador();
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

    public void jugarScrabble(String input) {

        String[] parts = input.split(" ");

        if (parts[0].contains("set")) {

            String ficha = parts[1];
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[3]);
            partidaActual.añadirFicha(ficha, x, y);

        } else if (parts[0].contains("get")) {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            partidaActual.quitarFichaTablero(x, y);
        } else if (parts[0].contains("reset")) {

        } else if (parts[0].contains("pasar")) {
            for( Pair <Integer,Integer> p : partidaActual.getCoordenadasPalabras()){
                partidaActual.quitarFichaTablero(p.getFirst(),p.getSecond());
              }
            finTurno(false); 
        } else if (parts[0].contains("fin")) {

            finTurno(true);
        } else {
            jugarAlgoritmo();
            finTurno(true);
        }

    }

    public int getPuntosJugador1() {
        return partidaActual.getPuntosJugador1();
    }

    public int getPuntosJugador2() {
        return partidaActual.getPuntosJugador2();
    }

    public void finTurno(boolean pasar) {
        if(!pasar){
            partidaActual.cambiarTurnoJugador();
            partidaActual.aumentarContador();

        }
        int puntos = validador.validarPalabra(partidaActual.getCoordenadasPalabras(), dawg, partidaActual.getTablero(), partidaActual.getContadorTurno());
    
        if (puntos > 0) {
            
            partidaActual.addPuntos(puntos);
            partidaActual.bloquearCeldas();
            partidaActual.coordenadasClear();
            partidaActual.cambiarTurnoJugador();
            partidaActual.aumentarContador();
            if (isAlgoritmo) {
                int puntosAlgoritmo = jugarAlgoritmo();
                if(puntosAlgoritmo == 0) jugarScrabble("pasar");
                partidaActual.addPuntos(puntosAlgoritmo);
                partidaActual.bloquearCeldas();
                partidaActual.aumentarContador();
            }

            if (!partidaActual.recuperarFichas()) {
                if (partidaActual.getListSize() == 0) {
                    finPartida();
                }
            }
            partidaActual.coordenadasClear();
            partidaActual.cambiarTurnoJugador();
        } else {
            System.out.println("Palabra incorrecta");
        }

    }

    public void finPartida() {
        partidaActual.cambiarTurnoJugador();
        for (Ficha ficha : partidaActual.getFichasJugador()) {
            partidaActual.addPuntos(-ficha.getPuntuacion());
        }
        System.out.println("Fin Partida");
        if(partidaActual.getPuntosJugador1() > partidaActual.getPuntosJugador2()) System.out.println("Ganador Jugador1!!");
        else System.out.println("Ganador Jugador2!!");
    }

    private int jugarAlgoritmo() {
        Pair<List<Pair<Ficha, Pair<Integer, Integer>>>, Integer> ss = algoritmo.find_all_words(partidaActual.getFichasJugador(), dawg, partidaActual.getTablero());
        List<Pair<Ficha, Pair<Integer, Integer>>> s = ss.getFirst();
        System.out.print(partidaActual.obtenerFichas());
        for (Pair<Ficha , Pair<Integer, Integer>> aa : s) {
         //   System.out.println(aa.getFirst().getLetra());

            partidaActual.añadirFicha(aa.getFirst().getLetra(), aa.getSecond().getFirst(), aa.getSecond().getSecond());

        }
        if(s.size() == 0){
            return 0;
        }
        return ss.getSecond();
    }

    public Tablero obtenerTablero() {
        return partidaActual.getTablero();
    }

}
