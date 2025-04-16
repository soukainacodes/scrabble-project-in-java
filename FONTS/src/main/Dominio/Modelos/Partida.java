package Dominio.Modelos;

import java.util.ArrayList;
import java.util.List;

public class Partida {

    // Atributos de la clase
    private List<Ficha> fichasJugador1;  // Lista de fichas del Jugador 1
    private List<Ficha> fichasJugador2;  // Lista de fichas del Jugador 2
    private int puntosJugador1;          // Puntos acumulados por el Jugador 1
    private int puntosJugador2;          // Puntos acumulados por el Jugador 2
    private boolean turnoJugador;       // Indica el turno actual (true: Jugador 1, false: Jugador 2)
    private Bolsa bolsa;
    private Tablero tablero;

    // Constructor
    public Partida(List<String> s, Tablero tablero, Bolsa bolsa) {
        fichasJugador1 = new ArrayList<>(); // Inicializa la lista de fichas del Jugador 1
        fichasJugador2 = new ArrayList<>(); // Inicializa la lista de fichas del Jugador 2
        turnoJugador = false;               // Inicializa el turno (por defecto, Jugador 2 comienza)
        puntosJugador1 = 0;                 // Inicializa los puntos del Jugador 1 en 0
        puntosJugador2 = 0;                 // Inicializa los puntos del Jugador 2 en 0
        this.bolsa = bolsa;
        this.tablero = tablero;
        for (int i = 0; i < 14; i++) {
            setFicha(bolsa.sacarFichaAleatoria());
            turnoJugador = !turnoJugador;
        }
        turnoJugador = true;
    }

    // Método setFicha
    public void setFicha(Ficha ficha) {
        if (turnoJugador) {
            fichasJugador1.add(ficha); // Añade la ficha a la lista del Jugador 1 si es su turno
        } else {
            fichasJugador2.add(ficha); // Añade la ficha a la lista del Jugador 2 si es su turno
        }
    }

    // Método getListSize
    public int getListSize() {
        if (turnoJugador) {
            return fichasJugador1.size(); // Devuelve el tamaño de la lista de fichas del Jugador 1
        } else {
            return fichasJugador2.size(); // Devuelve el tamaño de la lista de fichas del Jugador 2
        }
    }

    public void recuperarFichas() {
        for (int i = fichasJugador1.size(); i < 7; i++) {
            fichasJugador1.add(bolsa.sacarFichaAleatoria());
        }
        for (int i = fichasJugador2.size(); i < 7; i++) {
            fichasJugador2.add(bolsa.sacarFichaAleatoria());
        }
    }

    // Método cambiarTurnoJugador
    public void cambiarTurnoJugador() {
        turnoJugador = !turnoJugador; // Cambia el turno entre Jugador 1 y Jugador 2
    }

    // Método getFicha
    public Ficha getFicha(int n) {
        if (turnoJugador) {
            return fichasJugador1.get(n); // Remueve y devuelve la ficha en la posición n de la lista del Jugador 1
        } else {
            return fichasJugador2.get(n); // Remueve y devuelve la ficha en la posición n de la lista del Jugador 2
        }
    }

    public Ficha getFichaString(String s) {

        if (turnoJugador) {
            for (int i = 0; i <  fichasJugador1.size(); ++i) {
                if (fichasJugador1.get(i).getLetra().equals(s)) {
                    System.out.println(i);
                    return fichasJugador1.get(i);
                }
            }
        } else {
            for (int i = 0; i <  fichasJugador2.size(); ++i) {
                if (fichasJugador2.get(i).getLetra().equals(s)) {
                    return fichasJugador1.get(i);
                }
            }
        }
        return null;
    }

    public void quitarFicha(String s) {

        if (turnoJugador) {
            for (int i = 0; i < fichasJugador1.size(); ++i) {
                if (fichasJugador1.get(i).getLetra().equals(s)) {
                    fichasJugador1.remove(i);
                    return;
                }
            }
        } else {
            for (int i = 0; i < fichasJugador2.size(); ++i) {
                if (fichasJugador2.get(i).getLetra().equals(s)) {
                    fichasJugador1.remove(i);
                    return;
                }
            }
        }
    }

    // Método obtenerFichas
    public List<String> obtenerFichas() {
        List<String> s = new ArrayList<>(); // Lista para almacenar las letras de las fichas
        if (turnoJugador) {
            for (Ficha f : fichasJugador1) {
                s.add(f.getLetra()); // Añade las letras de las fichas del Jugador 1 a la lista
            }
        } else {
            for (Ficha f : fichasJugador2) {
                s.add(f.getLetra()); // Añade las letras de las fichas del Jugador 2 a la lista
            }
        }
        return s; // Devuelve la lista de letras de las fichas
    }

    // Método getTurnoJugador
    public boolean getTurnoJugador() {
        return turnoJugador; // Devuelve el turno actual (true: Jugador 1, false: Jugador 2)
    }

    // Método setTurnoJugador
    public void setTurnoJugador(boolean turno) {
        turnoJugador = turno; // Establece el turno del jugador (true: Jugador 1, false: Jugador 2)
    }

    // Método setPuntos
    public void setPuntos(int n) {
        if (turnoJugador) {
            puntosJugador1 = n; // Establece los puntos del Jugador 1 si es su turno
        } else {
            puntosJugador2 = n; // Establece los puntos del Jugador 2 si es su turno
        }
    }

    // Método getPuntosJugador1
    public int getPuntosJugador1() {
        return puntosJugador1; // Devuelve los puntos acumulados por el Jugador 1
    }

    // Método getPuntosJugador2
    public int getPuntosJugador2() {
        return puntosJugador2; // Devuelve los puntos acumulados por el Jugador 2
    }

    public List<Ficha> getFichasJugador2() {
        return fichasJugador2;
    }

}
