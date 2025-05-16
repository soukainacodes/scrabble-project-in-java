package Dominio;

import java.util.ArrayList;
import java.util.List;

import Dominio.Modelos.Ficha;
import Dominio.Modelos.Partida;

class DataConverter {

    // convierte una Partida a lista de Strings
    public List<String> partidaToStringList(Partida partida, String jugadorActual, String segundoJugador,
            String nombrePartida) {
        List<String> s = new ArrayList<>();
        s.add(Integer.toString(partida.getPartidaAcabada()));
        s.add(nombrePartida);
        s.add(Integer.toString(partida.getContadorTurno()));
        s.add(partida.getTurnoJugador() ? "1" : "0");
        s.add(jugadorActual);
        s.add(segundoJugador);
        s.add(Integer.toString(partida.getPuntosJugador1()));
        s.add(Integer.toString(partida.getPuntosJugador2()));
        String fichas = "";
        boolean turno1  =false;
        if (!partida.getTurnoJugador()) {
            turno1 = true;
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
        if(!turno1){
            partida.cambiarTurnoJugador();
        }

        // Añadir al final el id del recurso usado (bolsa + diccionario)
        s.add(partida.getRecursoPartida());

        return s;
    }

        // reconstruye una Partida a partir de una lista de Strings
    public Partida stringListToPartida(List<String> strings) {
        List<String> jugadores = new ArrayList<>();
        jugadores.add(strings.get(4));
        jugadores.add(strings.get(5));
    
        // Calculate indices properly
        int bolsaStartIndex = 11;
        int bolsaSize = Integer.parseInt(strings.get(10));
    
        // Get the bag tiles
        List<String> bolsa = strings.subList(bolsaStartIndex, bolsaStartIndex + bolsaSize);
    
        // Calculate where the board positions start
        int tableroStartIndex = bolsaStartIndex + bolsaSize + 1; // +1 for the tablero size field
        int tableroSize = Integer.parseInt(strings.get(bolsaStartIndex + bolsaSize));
    
        // Get the board positions
        List<String> tablero = new ArrayList<>();
        for (int i = 0; i < tableroSize; i++) {
            String position = strings.get(tableroStartIndex + i);
            if (position.equals("null")) {
                tablero.add(null); // Convert the "null" string back to a real null
            } else {
                tablero.add(position);
            }
        }
        System.out.println("llega a index 0");
        Partida partida = new Partida(strings.get(1), bolsa, tablero);
        // Set the counter and turn from saved values
        System.out.println("llega a index 1");

        partida.setContadorTurno(Integer.parseInt(strings.get(2)));
        System.out.println("llega a index 2");
        partida.setTurnoJugador(strings.get(3).equals("1"));
        
        // Set player points directly instead of using setPuntos
        partida.setPuntosJugador1(Integer.parseInt(strings.get(6)));
        partida.setPuntosJugador2(Integer.parseInt(strings.get(7)));

        System.out.println("llega a index 6 y 7");
        
        // Load player 1's tiles
        partida.setTurnoJugador(true); // Set to player 1 for loading tiles
        String[] fichasString = strings.get(8).trim().split(" ");
        List<Ficha> fichas = new ArrayList<>();
        for (int contadorFicha = 0; contadorFicha < fichasString.length; contadorFicha += 2) {
            Ficha ficha = new Ficha(fichasString[contadorFicha], Integer.parseInt(fichasString[contadorFicha + 1]));
            fichas.add(ficha);
        }
        System.out.println("llega a index 8");
        partida.setFichas(fichas);
        
        // Load player 2's tiles
        partida.setTurnoJugador(false); // Set to player 2 for loading tiles
        fichasString = strings.get(9).trim().split(" ");
        fichas = new ArrayList<>();
        for (int contadorFicha = 0; contadorFicha < fichasString.length; contadorFicha += 2) {
            Ficha ficha = new Ficha(fichasString[contadorFicha], Integer.parseInt(fichasString[contadorFicha + 1]));
            fichas.add(ficha);
        }
        System.out.println("llega a index 9");
        partida.setFichas(fichas);
        
        // Restore the correct turn
        partida.setTurnoJugador(strings.get(3).equals("1"));
        System.out.println("llega a index 3");

        // Que recurso usa la partida
        // El recurso es el último elemento de la lista
        partida.setRecursoPartida(strings.get(strings.size() - 1));
        System.out.println("llega a index recurso");
        
        return partida;
    }
}


