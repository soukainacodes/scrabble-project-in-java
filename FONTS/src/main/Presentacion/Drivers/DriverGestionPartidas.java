package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Dominio.CtrlDominio;
import Dominio.Modelos.*;
import Dominio.Excepciones.*;

public class DriverGestionPartidas {
     private static final Scanner sc  = new Scanner(System.in);
    private static CtrlDominio cd;
    private static String modo, dificultad, idioma;
    

    public static void main(String[] args) throws PosicionOcupadaTablero, FichaIncorrecta,  PosicionVaciaTablero {
        modo = dificultad = idioma = "";
        cd = new CtrlDominio();
        while(true) menuPrincipal();
    }

    private static void menuPrincipal()  throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        clearScreen();
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Jugar");
        System.out.println("2. Juego de pruebas");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1":  subMenuGestionPartida();   break;
            case "2":     break;
            case "3": System.exit(0);
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void subMenuGestionPartida() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        clearScreen();
        System.out.println("\n===== GESTIÓN PARTIDAS =====");
        System.out.println("1. Nueva Partida");
        System.out.println("2. Seguir última partida");
        System.out.println("3. Cargar Partida");
         switch (sc.nextLine().trim()) {
            case "1":  subMenuCrearPartida();   break;
            case "2": cd.cargarUltimaPartida(); subMenuPartida(); break;
            case "3": subMenuCargarPartida(); break;
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void subMenuCargarPartida() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        clearScreen();
        System.out.println("\n===== GESTIÓN PARTIDAS =====");
        System.out.println("1. Volver Atrás");
        System.out.println("Inserte el nombre de la partida");
        String input =  sc.nextLine().trim();
        if(input.equals(1)){
            subMenuGestionPartida(); 
        } 
        else{
            cd.cargarPartida(input);
            subMenuPartida();
        }
    }

    private static void subMenuCrearPartida() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. Elegir Modo: " + modo);
        System.out.println("2. Elegir Idioma: " + idioma);
        System.out.println("3. Elegir dificultad: " + dificultad);
        System.out.println("4. Finalizar");
        switch (sc.nextLine().trim()) {
            case "1":  subMenuElegirModo();   break;
            case "2": subMenuElegirIdioma();    break;
            case "3": subMenuElegirDificultad(); break;
            case "4": 
            if(!"".equals(modo) && !"".equals(idioma) && !"".equals(dificultad)){
                int modoPartida;
                int dificultadPartida;
                if(modo.equals("1 Jugador")) modoPartida = 0;
                else modoPartida = 1;
                
                if(dificultad.equals("Fácil")) dificultadPartida = 1;
                else if(dificultad.equals("Intermedio")) dificultadPartida = 2;
                else dificultadPartida = 3;

                 List<String> lineasArchivoDiccionario = null;
                List<String> lineasArchivoBolsa = null;
            try {
                 if(idioma.equals("Catalán")){
                    System.out.println("afasdfa");
                    lineasArchivoBolsa = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Catalan/letrasCAT.txt");
                    lineasArchivoDiccionario = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Catalan/catalan.txt");
                }
                else if(idioma.equals("Castellano")){
                    lineasArchivoBolsa = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/letrasCAST.txt");
                    lineasArchivoDiccionario = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/castellano.txt");
                }   
                else if(idioma.equals("Inglés")){
                    lineasArchivoBolsa = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Ingles/letrasENG.txt");
                    lineasArchivoDiccionario = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Ingles/english.txt");
                }
            cd.iniciarPartida(modoPartida,"Alex", "",lineasArchivoDiccionario, lineasArchivoBolsa,0,dificultadPartida);
            } catch (IOException e) {
                 System.err.println("Error al cargar archivos: " + e.getMessage());
            }
               
            subMenuPartida();
            break;
            }
            
            break;
            default:  System.out.println("Opción no válida."); 
        }
        }

    private static void subMenuElegirModo() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero{
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. 1 Jugador");
        System.out.println("2. 2 Jugadores");
        switch (sc.nextLine().trim()) {
            case "1": modo = "1 Jugador"; subMenuCrearPartida();   break;
            case "2": modo = "2 Jugadores"; subMenuCrearPartida();   break;
            default:  System.out.println("Opción no válida.");
        }
    }


     private static void subMenuElegirIdioma() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero{
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. Catalán");
        System.out.println("2. Castellano");
        System.out.println("2. Ingles");
        switch (sc.nextLine().trim()) {
            case "1": idioma = "Catalán"; subMenuCrearPartida();  break;
            case "2": idioma = "Castellano"; subMenuCrearPartida();   break;
            case "3": idioma = "Inglés"; subMenuCrearPartida();    break;
            default:  System.out.println("Opción no válida.");
        }
    }
    private static void subMenuElegirDificultad() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero{
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. Fácil");
        System.out.println("2. Intermedio");
        System.out.println("2. Díficil");
        switch (sc.nextLine().trim()) {
            case "1": dificultad = "Fácil"; subMenuCrearPartida();   break;
            case "2": dificultad = "Intermedio"; subMenuCrearPartida();   break;
            case "3": dificultad = "Díficil"; subMenuCrearPartida();   break;
            default:  System.out.println("Opción no válida.");
        }
    }

   

    private static void subMenuPartida() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        clearScreen();
        System.out.println("\n--- Tablero Actual ---");
        System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
        System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());

        mostrarTablero();
        System.out.println("\n\n--- Fichas Disponibles ---");
        mostrarFichas();
        System.out.println("\n\n");
        System.out.println("1. Abandonar partida");
        System.out.println("2. Guardar Partida");
        System.out.println("Opcion: ");
        switch (sc.nextLine().trim()) {
            case "1":
                System.out.println("Jugador " + " es Francés.");
                break;
            case "2":
                subMenuGuardar(); break;
                
            default:
             System.out.println("Opción no válida.");
        }
    }



    private static void subMenuGuardar() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero{
        clearScreen();
        System.out.println("\n--- Tablero Actual ---");
        System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
        System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());

        mostrarTablero();
        System.out.println("\n\n--- Fichas Disponibles ---");
        mostrarFichas();
        System.out.println("\n\n");
        System.out.println("1. Volver atras:");
        System.out.println("Nombre de la partida:");
        String input = sc.nextLine().trim();
        if (input.equals("1")) {
            subMenuPartida();  
        } else {
            cd.guardarPartida(input);
            System.out.println("1. Se ha guardado exitosamete");

        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void mostrarFichas() {
        System.out.println("Fichas actuales: " + cd.obtenerFichas());
    }

    private static void mostrarTablero() {
        Tablero tablero = cd.obtenerTablero();
        System.out.print("    ");
        for (int j = 0; j < 15; j++) {
            System.out.printf("%4d", j);
        }
        System.out.println();
        for (int i = 0; i < 15; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < 15; j++) {
                Celda celda = tablero.getCelda(i, j);
                String display = obtenerDisplayCelda(celda);
                System.out.printf("[%2s]", display);
            }
            System.out.println();
        }
    }

    private static String obtenerDisplayCelda(Celda celda) {
        if (celda.getFicha() != null) {
            return celda.getFicha().getLetra();
        } else if (!celda.bonusDisponible() && celda.getBonificacion() != TipoBonificacion.NINGUNA) {
            return "usada";
        } else {
            switch (celda.getBonificacion()) {
                case DOBLE_LETRA:
                    return "DL";
                case TRIPLE_LETRA:
                    return "TL";
                case DOBLE_PALABRA:
                    return "DP";
                case TRIPLE_PALABRA:
                    return "TP";
                default:
                    return "  ";
            }
        }
    }

    public static List<String> leerArchivo(String rutaArchivo) throws IOException {
        List<String> lineasArchivo = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    lineasArchivo.add(linea);
                }
            }
        }
        return lineasArchivo;
    }

}
