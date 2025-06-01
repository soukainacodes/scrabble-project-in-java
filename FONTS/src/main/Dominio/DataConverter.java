package Dominio;

import Dominio.Modelos.Ficha;
import Dominio.Modelos.Partida;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de convertir una partida a una lista de Strings y viceversa.
 * 
 * Esta clase es responsable de serializar y deserializar la información de una
 * partida
 * en un formato de lista de cadenas, lo que facilita su almacenamiento y
 * recuperación.
 */
class DataConverter {

    /**
     * Convierte una partida a una lista de Strings.
     * 
     * @param partida        La partida a convertir.
     * @param jugadorActual  El nombre del jugador actual.
     * @param segundoJugador El nombre del segundo jugador.
     * @param nombrePartida  El nombre de la partida.
     * @return Una lista de Strings que representa la partida.
     */
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
        boolean turno1 = false;
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
        if (!turno1) {
            partida.cambiarTurnoJugador();
        }

        // Añadir al final el id del recurso usado (bolsa + diccionario)
        s.add(partida.getRecursoPartida());

        return s;
    }

    /**
     * Convierte una lista de Strings a una partida.
     * 
     * @param strings La lista de Strings que representa la partida.
     * @return La partida convertida.
     */
    public Partida stringListToPartida(List<String> strings) {
        List<String> jugadores = new ArrayList<>();
        jugadores.add(strings.get(4));
        jugadores.add(strings.get(5));

        int bolsaStartIndex = 11;
        int bolsaSize = Integer.parseInt(strings.get(10));

        List<String> bolsa = strings.subList(bolsaStartIndex, bolsaStartIndex + bolsaSize);

        int tableroStartIndex = bolsaStartIndex + bolsaSize + 1; 
        int tableroSize = Integer.parseInt(strings.get(bolsaStartIndex + bolsaSize));

     
        List<String> tablero = new ArrayList<>();
        for (int i = 0; i < tableroSize; i++) {
            String position = strings.get(tableroStartIndex + i);
            if (position.equals("null")) {
                tablero.add(null); 
            } else {
                tablero.add(position);
            }
        }
       
        Partida partida = new Partida(strings.get(1), bolsa, tablero);
      

        partida.setContadorTurno(Integer.parseInt(strings.get(2)));
       
        partida.setTurnoJugador(strings.get(3).equals("1"));

        partida.setPuntosJugador1(Integer.parseInt(strings.get(6)));
        partida.setPuntosJugador2(Integer.parseInt(strings.get(7)));

       

        partida.setTurnoJugador(true); 
        String[] fichasString = strings.get(8).trim().split(" ");
        List<Ficha> fichas = new ArrayList<>();
        for (int contadorFicha = 0; contadorFicha < fichasString.length; contadorFicha += 2) {
            Ficha ficha = new Ficha(fichasString[contadorFicha], Integer.parseInt(fichasString[contadorFicha + 1]));
            fichas.add(ficha);
        }
        
        partida.setFichas(fichas);

        partida.setTurnoJugador(false); 
        fichasString = strings.get(9).trim().split(" ");
        fichas = new ArrayList<>();
        for (int contadorFicha = 0; contadorFicha < fichasString.length; contadorFicha += 2) {
            Ficha ficha = new Ficha(fichasString[contadorFicha], Integer.parseInt(fichasString[contadorFicha + 1]));
            fichas.add(ficha);
        }
        
        partida.setFichas(fichas);

        partida.setTurnoJugador(strings.get(3).equals("1"));
       

        partida.setRecursoPartida(strings.get(strings.size() - 1));
       

        return partida;
    }
}
