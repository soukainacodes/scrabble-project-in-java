package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Dominio.CtrlDominio;
import Dominio.Modelos.*;
import Dominio.Excepciones.UsuarioYaRegistradoException;
import Dominio.Excepciones.UsuarioNoEncontradoException;
import Dominio.Excepciones.*;
public class DriverPartidaAlgoritmo {

    // Scanner para entrada por teclado
    private static Scanner in = new Scanner(System.in);
    // Controlador de Dominio, que manejará la lógica de la partida
    private static CtrlDominio cd = new CtrlDominio();
    private static String nombreJugador;

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
                if (!linea.isEmpty()) {
                    lineasArchivo.add(linea);
                }
            }
        }
        return lineasArchivo;
    }

    private static void menuPartida() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
     //   clearScreen();
        System.out.println("\n--- Tablero Actual ---");
        System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
        System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());

        mostrarTablero();
        System.out.println("\n\n--- Fichas Disponibles ---");
        mostrarFichas();
        System.out.println("\n\n");
        System.out.println("1. Añadir ficha");
        System.out.println("2. Quitar ficha");
        System.out.println("3. Cambiar fichas");
        System.out.println("4. Pasar");
        System.out.println("5. Acabar turno");
        System.out.println("6. Salir");
        System.out.println("7. Algoritmo (luego borrar)");
        System.out.print("Opción: ");
        switch (in.nextLine().trim()) {
            case "1": //Añadir Ficha
                subMenuAnadir(); break;
            case "2": //Quitar Ficha
                subMenuQuitar(); break;
            case "3": //Cambiar Ficha
                subMenuCambiar(); break;
            case "4":
                cd.jugarScrabble(3, ""); 
                menuPartida();
            case "5": //Acabar turno
                int fin = cd.jugarScrabble(4, "");
                if(fin == 0) menuPartida();
                else if (fin == 1){
                    System.out.println("Jugador 1 gana");
                    break;
                }
                else {
                    System.out.println("Jugador 2 gana");
                    break;
                }
            case "6":
                subMenuSalir(); break;
            case "7":
                cd.jugarScrabble(5,"");
                menuPartida();
            default:
                System.out.println("Opción no válida.");
                menuPartida();
        }
    }

    private static void subMenuAnadir() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero{
      //  clearScreen();
        System.out.println("\n--- Tablero Actual ---");
        System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
        System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());

        mostrarTablero();
        System.out.println("\n\n--- Fichas Disponibles ---");
        mostrarFichas();
        System.out.println("\n\n");
        System.out.println("1. Volver atras:");
        System.out.println("Inserta la Ficha junto a la posición del tablero:");
        System.out.println("En caso de que sea un # añada despues de '#', la letra que quiera que substituya:");
        String input = in.nextLine().trim();
        if (input.equals("1")) {
            menuPartida(); 
        } else {
            cd.jugarScrabble(1, input);
            menuPartida();
            
        }
    }

    private static void subMenuQuitar() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        clearScreen();
        System.out.println("\n--- Tablero Actual ---");
        System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
        System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());

        mostrarTablero();
        System.out.println("\n\n--- Fichas Disponibles ---");
        mostrarFichas();
        System.out.println("\n\n");
        System.out.println("1. Volver atras:");
        System.out.println("Inserta la posición del tablero:");
        String input = in.nextLine().trim();
        if (input.equals("1")) {
            menuPartida();
        } else {
            cd.jugarScrabble(2, input);
            menuPartida();
        }
    }

    private static void subMenuCambiar() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero  {
   
        System.out.println("1. Volver atras:");
        System.out.println("Inserta las fichas a modificar:");
        String input = in.nextLine().trim();
        if (input.equals("1")) {
            menuPartida();
        } else {
            cd.cambiarFichas(input);
            menuPartida();
        }
    }

    private static void subMenuSalir() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        clearScreen();
        System.out.println("1. Abandonar partida");
        System.out.println("2. Guardar Partida");
        System.out.println("3. Volver atras:");
        switch (in.nextLine().trim()) {
            case "1":
                System.out.println("Jugador " + nombreJugador + " es Francés.");
                break;
            case "2":
                subMenuGuardar();
            case "3":
                menuPartida();
            default:
             System.out.println("Opción no válida.");
             subMenuSalir();
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
        String input = in.nextLine().trim();
        if (input.equals("1")) {
            menuPartida();  
        } else {
            cd.guardarPartida(input); 
        }
    }

    public static void main(String[] args) throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero{
        System.out.println("Driver de prueba de Partida contra el Algoritmo (CtrlDominio)");

        // Registro del jugador
        nombreJugador = "A";
        String contrasena = "A";
        int dificultad = 1;
        try {
            cd.registrarJugador(nombreJugador, contrasena);
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        Jugador jugador;
        try {
            jugador = cd.obtenerJugador(nombreJugador);
        } catch (UsuarioNoEncontradoException e) {
            System.err.println("Error al recuperar jugador: " + e.getMessage());
            return;
        }

        // Lectura de recursos y inicio de partida
        List<String> lineasArchivoDiccionario;
        List<String> lineasArchivoBolsa;
        try {
            lineasArchivoBolsa = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Catalan/letrasCAT.txt");
            lineasArchivoDiccionario = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Catalan/catalan.txt");
            cd.iniciarPartida(1, jugador.getNombre(), "", lineasArchivoDiccionario, lineasArchivoBolsa,0,dificultad);
        } catch (IOException e) {
            System.err.println("Error al cargar archivos: " + e.getMessage());
            return;
        }

        // Bucle principal del juego
        String input;
        menuPartida();
       
        System.out.println("Partida finalizada. ¡Gracias por jugar!");
        in.close();
    }

    // no comments
}
