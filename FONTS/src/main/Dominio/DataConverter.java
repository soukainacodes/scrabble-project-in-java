package Dominio;

import java.util.ArrayList;
import java.util.List;

import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
class DataConverter {

   // convierte un Jugador a lista de Strings
public List<String> jugadorToStringList(Jugador jugador) {
    List<String>  s = new ArrayList<>();
    s.add(jugador.getNombre());
    s.add(jugador.getPassword());
    s.add(Integer.toString(jugador.getPuntos()));
    return s;
}

// convierte una Partida a lista de Strings
public List<String> partidaToStringList(Partida partida, String nombrePartida) { 
    List<String>  s = new ArrayList<>();
    s.add(nombrePartida);
    s.add(Integer.toString(partida.getContadorTurno()));
    s.add(partida.getTurnoJugador() ? "1" : "0");
    s.add(partida.getJugador1());
    s.add(partida.getJugador2());
    s.add(Integer.toString(partida.getPuntosJugador1()));
    s.add(Integer.toString(partida.getPuntosJugador2()));
    String fichas = "";
    if(!partida.getTurnoJugador()) partida.cambiarTurnoJugador();  
    for (String f : partida.obtenerFichas()) fichas += f + " ";  
    s.add(fichas);
    fichas = "";
    partida.cambiarTurnoJugador();
    for (String f : partida.obtenerFichas()) fichas += f + " ";  
    s.add(fichas); 
   List<String> bolsa = partida.getBolsa().toListString();
    for(String fichaBolsa : bolsa){
        s.add(fichaBolsa);
    } 
    s.add("");
     List<String> tablero = partida.getTablero().toListString();
     for(String fichaTablero: tablero){
        s.add(fichaTablero);
    } 
    
   
    return s;
}

     

// reconstruye un Jugador a partir de una lista de Strings
public Jugador stringListToJugador(List<String> s) {
    Jugador j = new Jugador(s.get(0),s.get(1));
    j.setPuntos(Integer.parseInt(s.get(2)));
    return j;
 }

// reconstruye una Partida a partir de una lista de Strings
//public Partida stringListToPartida(List<String> strings) {

   // Partida partida = new Partida();
    //partida.setIdPartida(strings.get(0));
 //  partida.setContadorTurno();
  // partida.setTurnoJugador(turno);
   // partida.setJugador1();
   // partida.setJugador2();
  //  partida.setPuntosJugador1();
  //  partida.setPuntosJugador2();
  //  partida.setFichasJugador1();
  //  partida.setFichasJugador2();

 //   partida.setBolsa();
  //  partida.setTablero();
    

    

   // return partida;
//}

}
