package Dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
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

    public void crearPartida(int modo, List<String> players, List<String> lineasArchivo, List<String> lineasArchivoBolsa) {
        this.dawg = new Dawg(lineasArchivoBolsa, lineasArchivo);
    
        this.partidaActual = new Partida(players, lineasArchivoBolsa);
        this.finTurno = false;

        if (modo == 1) {
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

    public void jugarScrabble(int opcion, String input) throws PosicionOcupadaTablero, PosicionVaciaTablero, FichaIncorrecta {

        String[] parts = input.split(" ");

        switch (opcion) {
            case 1: {
                //Poner ficha en el tablero
                String ficha = parts[0];
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
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
            
                reset("");
                finTurno(true, true);
                break;
            case 4: // Fin turno
                finTurno(false, true);
                break;
            default:
               // int puntosTotales = jugarAlgoritmo();
               // partidaActual.addPuntos(puntosTotales);
               // System.out.println("Validador " + puntosTotales);
               // finTurno(true, false);
                break;
        }

    }

    public void reset(String letras) throws PosicionVaciaTablero, PosicionOcupadaTablero , FichaIncorrecta {

        if (partidaActual.getCoordenadasPalabras().size() != 0) {
            for (Pair<Integer, Integer> p : partidaActual.getCoordenadasPalabras()) {
                partidaActual.quitarFichaTablero(p.getFirst(), p.getSecond());
            }
        }
       
        ArrayList<String> fichas = new ArrayList<>(Arrays.asList(letras.split(" ")));

        for(int i=0;i<fichas.size();++i){
            if(fichas.get(i).matches("[0-7]")){
                int num = Integer.parseInt(fichas.get(i));
                fichas.add(partidaActual.getFichasJugador().get(num).getLetra());
                fichas.remove(i);
            }
        }
        System.out.println(fichas);
        if (fichas.size() != 0) {
            for (String s : fichas) {
                partidaActual.quitarFicha(s);
            }
        }

        partidaActual.recuperarFichas();
        finTurno(true, true);
    }

    public int getPuntosJugador1() {
        return partidaActual.getPuntosJugador1();
    }

    public int getPuntosJugador2() {
        return partidaActual.getPuntosJugador2();
    }

    public void finTurno(boolean pasar, boolean algoritmo) throws PosicionOcupadaTablero, FichaIncorrecta {

        if (!pasar) {
            int puntos = validador.validarPalabra(partidaActual.getCoordenadasPalabras(), dawg, partidaActual.getTablero(), partidaActual.getContadorTurno());
            if (puntos > 0) {
                partidaActual.addPuntos(puntos);
            } else {
                System.out.println("Palabra incorrecta!!");
                return;

            }
        }

        partidaActual.bloquearCeldas();
        partidaActual.coordenadasClear();
        partidaActual.cambiarTurnoJugador();
        partidaActual.aumentarContador();
        if (!partidaActual.recuperarFichas() && partidaActual.getListSize() == 0) {
            finPartida();
        }

        if (isAlgoritmo && algoritmo) {
            partidaActual.addPuntos(jugarAlgoritmo());
            finTurno(true, false);
        }

    }

    public void finPartida() {
        partidaActual.cambiarTurnoJugador();
        for (Ficha ficha : partidaActual.getFichasJugador()) {
            partidaActual.addPuntos(-ficha.getPuntuacion());
        }
        System.out.println("Fin Partida");
        if (partidaActual.getPuntosJugador1() > partidaActual.getPuntosJugador2()) {
            System.out.println("Ganador Jugador1!!");
        } else {
            System.out.println("Ganador Jugador2!!");
        }
    }

    private int jugarAlgoritmo() throws PosicionOcupadaTablero, FichaIncorrecta {
        Pair<List<Pair<Ficha, Pair<Integer, Integer>>>, Integer> ss = algoritmo.find_all_words(partidaActual.getFichasJugador(), dawg, partidaActual.getTablero());
        List<Pair<Ficha, Pair<Integer, Integer>>> s = ss.getFirst();
        System.out.print(partidaActual.obtenerFichas());
        for (Pair<Ficha, Pair<Integer, Integer>> aa : s) {

            partidaActual.añadirFicha(aa.getFirst().getLetra(), aa.getSecond().getFirst(), aa.getSecond().getSecond());

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
