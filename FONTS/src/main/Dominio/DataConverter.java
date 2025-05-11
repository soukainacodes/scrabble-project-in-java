package Dominio;

import java.util.ArrayList;
import java.util.List;

import Dominio.Modelos.Ficha;
import Dominio.Modelos.Partida;

class DataConverter {



// convierte una Partida a lista de Strings
    public List<String> partidaToStringList(Partida partida, String nombrePartida) {
        List<String> s = new ArrayList<>();
        s.add(nombrePartida);
        s.add(Integer.toString(partida.getContadorTurno()));
        s.add(partida.getTurnoJugador() ? "1" : "0");
        s.add(partida.getJugador1());
        s.add(partida.getJugador2());
        s.add(Integer.toString(partida.getPuntosJugador1()));
        s.add(Integer.toString(partida.getPuntosJugador2()));
        String fichas = "";
        if (!partida.getTurnoJugador()) {
            partida.cambiarTurnoJugador();
        }
        for (String f : partida.obtenerFichas()) {
            fichas += f + " ";
        }
        s.add(fichas);
        fichas = "";
        partida.cambiarTurnoJugador();
        for (String f : partida.obtenerFichas()) {
            fichas += f + " ";
        }
        s.add(fichas);
        List<String> bolsa = partida.getBolsa().toListString();
        s.add(Integer.toString(bolsa.size()));
        for (String fichaBolsa : bolsa) {
            s.add(fichaBolsa);
        }
        
        List<String> tablero = partida.getTablero().toListString();
        s.add(Integer.toString(tablero.size()));
        for (String fichaTablero : tablero) {
            s.add(fichaTablero);
        }

        return s;
    }


// reconstruye una Partida a partir de una lista de Strings
    public Partida stringListToPartida(List<String> strings) {
        List<String> jugadores = new ArrayList<>();
        jugadores.add(strings.get(3));
        jugadores.add(strings.get(4));

        List<String> bolsa = strings.subList(11, 11 + Integer.parseInt(strings.get(10)));
        List<String> tablero = strings.subList(Integer.parseInt(strings.get(10)), 11 + Integer.parseInt(strings.get(10)));
        Partida partida = new Partida(strings.get(1), jugadores, bolsa, tablero);

        partida.setContadorTurno(Integer.parseInt(strings.get(1)));
        partida.setTurnoJugador(true);

        partida.setPuntos(Integer.parseInt(strings.get(5)));
        String[] fichasString = strings.get(7).trim().split(" ");
        List<Ficha> fichas = new ArrayList<>();
        for (int contadorFicha = 0; contadorFicha < fichasString.length; contadorFicha += 2) {
            Ficha ficha = new Ficha(fichasString[contadorFicha], Integer.parseInt(fichasString[contadorFicha + 1]));
            fichas.add(ficha);
        }

        partida.setFichas(fichas);
        partida.cambiarTurnoJugador();
        partida.setPuntos(Integer.parseInt(strings.get(6)));


        fichasString = strings.get(8).trim().split(" ");
        fichas = new ArrayList<>();
        for (int contadorFicha = 0; contadorFicha < fichasString.length; contadorFicha += 2) {
            Ficha ficha = new Ficha(fichasString[contadorFicha], Integer.parseInt(fichasString[contadorFicha + 1]));
            fichas.add(ficha);
        }

         return partida;
    }

}
