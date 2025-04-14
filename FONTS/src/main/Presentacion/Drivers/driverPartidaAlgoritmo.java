package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Dominio.Modelos.*;
import Dominio.*;


public class driverPartidaAlgoritmo {
        private static Scanner in = null;
    private CtrlDominio cd;

    public static void main(String[] args) {
        in = new Scanner(System.in);
        CtrlDominio cd = new CtrlDominio();
        System.out.println("Driver de prueba de Partida con 2 Jugadores");

        //Crear 2 jugadores
        
        cd.registrarJugador("j1","1234");
        
        Jugador j1 = cd.obtenerJugador("j1");
       
        //Crear partida
        List<String> lineasArchivo; //Información para el diccionario (DAWG)
        List<String> lineasArchivoBolsa; //Información para la bolsa
        //Leer las fichas y el diccionario.
        try{ 
             lineasArchivoBolsa = leerArchivo("../FONTS/src/Recursos/Castellano/letrasCAST.txt"); 
             lineasArchivo = leerArchivo("../FONTS/src/Recursos/Castellano/castellano.txt");
             cd.iniciarPartida(1, j1.getNombre(),  "", lineasArchivo, lineasArchivoBolsa);
        }
        catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
        
        //Jugar
        cd.mostrarTablero();
        cd.mostrarFichas();
        String input = in.nextLine();
        while(input != "salir"){
            
           cd.jugarScrabble(input);
           
           cd.mostrarTablero();
           cd.mostrarFichas();

           input = in.nextLine();
        }
    }

    public static List<String> leerArchivo(String rutaArchivo) throws IOException {
    List<String> lineasArchivo = new ArrayList<>();
    
    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            // Saltamos líneas vacías o comentarios
            if (!linea.isEmpty() && !linea.startsWith("#")) {
                lineasArchivo.add(linea);
            }
        }
    }
    
    return lineasArchivo;
}
}
