package Dominio;

import java.io.*;
import java.util.*;

import Dominio.Excepciones.*;
import Dominio.Modelos.*;

public class CtrlDominio {

   private CtrlJugador ctrlJugador;

   private CtrlPartida ctrlPartida;
   // private CtrlRanking ctrlRanking;

   public CtrlDominio() {

      this.ctrlJugador = new CtrlJugador();
      // this.Ranking = new Ranking();
      this.ctrlPartida = new CtrlPartida();
   }

   public void crearUsuari(String nombre, String password) {
      ctrlJugador.crearJugador(nombre, password);
   }

   public void iniciarPartida(int modo, String nombre1, String nombre2, List<String> lineasArchivo,
         List<String> lineasArchivoBolsa) {
      List<String> players = new ArrayList<>();
      if (nombre1 != "")
         players.add(nombre1);
      if (nombre2 != "")
         players.add(nombre2);
      ctrlPartida.crearPartida(modo, players, lineasArchivo, lineasArchivoBolsa);
   }

   public void jugarScrabble(String input) {
      ctrlPartida.jugarScrabble(input);
   }

   public void registrarJugador(String nombre, String password) {
      ctrlJugador.crearJugador(nombre, password);
   }

   public Jugador obtenerJugador(String nombre) {
      return ctrlJugador.getJugador(nombre);
   }

   public Tablero obtenerTablero() {
      return ctrlPartida.obtenerTablero();
   }

   public void mostrarFichas() {
      ctrlPartida.mostrarFichas();
   }

}