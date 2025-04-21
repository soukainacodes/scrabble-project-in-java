package Dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


import Dominio.Modelos.*;
import Dominio.Excepciones.*;

public class CtrlPartida {

    private Partida partidaActual;
    private Dawg dawg;
    private Validador validador;
    private boolean finTurno;
    private boolean isAlgoritmo;
    private Algoritmo algoritmo;
    private boolean jugadorAlgoritmo;

    // Constructor
    public CtrlPartida() {

        this.validador = new Validador();

    }

    public void crearPartida(int modo, List<String> players, List<String> lineasArchivo, List<String> lineasArchivoBolsa, long seed, boolean jugadorAlgoritmo) {
        this.dawg = new Dawg(lineasArchivoBolsa, lineasArchivo);

        this.partidaActual = new Partida(players, lineasArchivoBolsa, seed);
        this.finTurno = false;
        this.jugadorAlgoritmo = jugadorAlgoritmo;
        if (modo == 0) {
            this.isAlgoritmo = true;
            this.algoritmo = new Algoritmo();
            if (partidaActual.getTurnoJugador() == false) {
                //  partidaActual.addPuntos(jugarAlgoritmo());
                //   finTurno(true,false);
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


public int jugarScrabble(int opcion, String input) throws ComandoInvalidoException, PalabraInvalidaException {

    String[] parts = input.trim().split(" ");

    switch (opcion) {
        case 1: {
            if (parts.length < 3) {
                throw new ComandoInvalidoException("Uso esperado: <ficha> <x> <y>.");
            }

            String ficha = parts[0];
            if (ficha.matches("[0-7]")) {
                int num = Integer.parseInt(ficha);
                List<String> fichasJugador = partidaActual.obtenerFichas();
                if (num < 0 || num >= fichasJugador.size()) {
                    throw new ComandoInvalidoException("Índice de ficha fuera de rango: " + num);
                }
                ficha = fichasJugador.get(num);
            }

            int x, y;
            try {
                if (ficha.equals("#") && parts.length == 4) {
                    ficha = parts[1];
                    x = Integer.parseInt(parts[2]);
                    y = Integer.parseInt(parts[3]);
                } else {
                    x = Integer.parseInt(parts[1]);
                    y = Integer.parseInt(parts[2]);
                }
            } catch (NumberFormatException e) {
                throw new ComandoInvalidoException("Coordenadas inválidas: deben ser números.");
            }

            partidaActual.añadirFicha(ficha, x, y);
            break;
        }

        case 2: {
            if (parts.length != 2) {
                throw new ComandoInvalidoException("Uso esperado: <x> <y> para quitar ficha.");
            }
            try {
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                partidaActual.quitarFichaTablero(x, y);
            } catch (NumberFormatException e) {
                throw new ComandoInvalidoException("Coordenadas inválidas: deben ser números.");
            }
            break;
        }

        case 3: {
            for (Pair<Integer, Integer> p : partidaActual.getCoordenadasPalabras()) {
                partidaActual.quitarFichaTablero(p.getFirst(), p.getSecond());
            }
            return finTurno(true, true);
        }

        case 4:
            return finTurno(false, true);

        case 5: {
            List<Pair<Integer, Integer>> coords = partidaActual.getCoordenadasPalabras();
            for (Pair<Integer, Integer> p : coords) {
                partidaActual.quitarFichaTablero(p.getFirst(), p.getSecond());
            }

            List<String> fichas = new ArrayList<>(Arrays.asList(parts));
            List<String> letrasJugador = new ArrayList<>();
            for (Ficha f : partidaActual.getFichasJugador()) {
                letrasJugador.add(f.getLetra());
            }

            for (int i = fichas.size() - 1; i >= 0; --i) {
                String s = fichas.get(i);
                if (s.matches("[0-7]")) {
                    int idx = Integer.parseInt(s);
                    List<Ficha> mano = partidaActual.getFichasJugador();
                    if (idx < 0 || idx >= mano.size()) {
                        throw new ComandoInvalidoException("Índice de ficha inválido: " + s);
                    }
                    fichas.add(mano.get(idx).getLetra());
                    fichas.remove(i);
                }
            }

            for (String s : fichas) {
                if (!letrasJugador.contains(s)) {
                    throw new ComandoInvalidoException("No tienes la ficha: '" + s + "'");
                }
                partidaActual.quitarFicha(s);
            }

            partidaActual.recuperarFichas();
            return finTurno(true, true);
        }

        case 6:
            return finPartida(false);

        case 7:
            if (!jugadorAlgoritmo) {
                throw new ComandoInvalidoException("Jugador de algoritmo no está habilitado.");
            }

            int puntosAlgoritmo = jugarAlgoritmo();
            partidaActual.addPuntos(puntosAlgoritmo);
            if (puntosAlgoritmo == 0 && partidaActual.isBolsaEmpty()
                && partidaActual.getPuntosJugador2() > partidaActual.getPuntosJugador1()) {
                return finPartida(false);
            }
            return finTurno(true, true);

        default:
            throw new ComandoInvalidoException("Opción de jugada desconocida: " + opcion);
    }

    return 0;
}


    public int getPuntosJugador1() {
        return partidaActual.getPuntosJugador1();
    }

    public int getPuntosJugador2() {
        return partidaActual.getPuntosJugador2();
    }

    public int finTurno(boolean pasar, boolean algoritmo) throws PalabraInvalidaException {

        if (!pasar) {
            int puntos = validador.validarPalabra(partidaActual.getCoordenadasPalabras(), dawg, partidaActual.getTablero(), partidaActual.getContadorTurno());
            if (puntos > 0) {
                partidaActual.addPuntos(puntos);
            } else {
                return 0;
            }

        }

        partidaActual.bloquearCeldas();
        partidaActual.coordenadasClear();
        partidaActual.cambiarTurnoJugador();
        partidaActual.aumentarContador();
        if (!partidaActual.recuperarFichas() && partidaActual.getListSize() == 0) {
            return finPartida(false);
        }

        if (isAlgoritmo && algoritmo) {
            int puntosAlgoritmo = jugarAlgoritmo();
            partidaActual.addPuntos(puntosAlgoritmo);
            if (puntosAlgoritmo == 0 && partidaActual.isBolsaEmpty() && partidaActual.getPuntosJugador1() > partidaActual.getPuntosJugador2()) {
                return finPartida(false);
            }
            finTurno(true, false);
        }
        return 0;
    }

    public int finPartida(boolean abandono) {
        if (abandono) {
            if (partidaActual.getTurnoJugador()) {
                return 2;
            } else {
                return 1;
            }
        }
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

    private int jugarAlgoritmo() {
        Pair<List<Pair<String, Pair<Integer, Integer>>>, Integer> ss = algoritmo.find_all_words(partidaActual.getFichasJugador(), dawg, partidaActual.getTablero());
        List<Pair<String, Pair<Integer, Integer>>> s = ss.getFirst();
        for (Pair<String, Pair<Integer, Integer>> aa : s) {
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