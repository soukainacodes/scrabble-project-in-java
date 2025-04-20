package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;
;

public class DriverPartidaAlgoritmo {

    private static final Scanner in = new Scanner(System.in);
    private static final CtrlDominio cd = new CtrlDominio();
    private static final String nombreJugador = "A";
    private static final String contrasena    = "A";
    private static final int    dificultad    = 1;

    public static void main(String[] args) {
        System.out.println("Driver de prueba de Partida contra el Algoritmo (CtrlDominio)");

        // Registro del jugador (si ya existe, lo ignoramos)
        try {
            cd.registrarJugador(nombreJugador, contrasena);
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        // Lectura de recursos e inicio de partida
        List<String> dict, bolsa;
        try {
            dict  = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Catalan/catalan.txt");
            bolsa = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Catalan/letrasCAT.txt");
            // modo = 1 (2 jugadores: humano vs IA), jugador 1 = nombreJugador, jugador 2 = IA
            cd.iniciarPartida(1, nombreJugador, "", dict, bolsa, 0L, dificultad);
        } catch (IOException e) {
            System.err.println("Error al cargar archivos: " + e.getMessage());
            return;
        }

        // Bucle de menú principal de la partida
        try {
            menuPartida();
        } catch (Exception e) {
            System.err.println("Error en partida: " + e.getMessage());
        }

        System.out.println("Partida finalizada. ¡Gracias por jugar!");
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void mostrarFichas() {
        System.out.println("Fichas actuales: " + cd.obtenerFichas());
    }

    private static void mostrarTablero() {
        int N = cd.getTableroDimension();
        System.out.print("    ");
        for (int j = 0; j < N; j++) System.out.printf("%4d", j);
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < N; j++) {
                String letra = cd.getLetraCelda(i, j);
                String bono  = cd.getBonusCelda(i, j);
                String disp  = letra != null ? letra : bono;
                System.out.printf("[%2s]", disp);
            }
            System.out.println();
        }
    }

    private static void mostrarEstado() {
        System.out.println("\n--- Estado de la partida ---");
        System.out.println(" Puntos J1: " + cd.getPuntosJugador1());
        System.out.println(" Puntos J2: " + cd.getPuntosJugador2());
        mostrarTablero();
        System.out.println("\n--- Fichas ---");
        mostrarFichas();
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

      

    private static void menuPartida()
            throws PosicionOcupadaTablero,
                   FichaIncorrecta,
                   PosicionVaciaTablero {
      //  clearScreen();
        System.out.println("\n--- Tablero Actual ---");
        System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
        System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
        mostrarTablero();
        System.out.println("\n--- Fichas Disponibles ---");
        mostrarFichas();

        System.out.println("\n1. Añadir ficha");
        System.out.println("2. Quitar ficha");
        System.out.println("3. Cambiar fichas");
        System.out.println("4. Pasar turno");
        System.out.println("5. Acabar turno");
        System.out.println("6. Salir");
        System.out.println("7. Algoritmo (luego borrar)");
        System.out.print("Opción: ");

        String opt = in.nextLine().trim();
        switch (opt) {
            case "1": subMenuAnadir();      break;
            case "2": subMenuQuitar();      break;
            case "3": subMenuCambiar();     break;
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
                break;
            default:
                System.out.println("Opción no válida.");
                menuPartida();
        }
    }

    private static void subMenuAnadir() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
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
    } 

    private static void subMenuQuitar()
            throws PosicionOcupadaTablero,
                   FichaIncorrecta,
                   PosicionVaciaTablero {
        clearScreen(); mostrarEstado();
        System.out.println("Inserta la posición a quitar (o '1' para atrás):");
        String input = in.nextLine().trim();
        if (!input.equals("1")) {
            cd.jugarScrabble(2, input);
        }
        menuPartida();
    }

    private static void subMenuCambiar()
            throws PosicionVaciaTablero,
                   PosicionOcupadaTablero,
                   FichaIncorrecta {
        clearScreen(); mostrarEstado();
        System.out.println("Inserta las fichas a cambiar (o '1' para atrás):");
        String input = in.nextLine().trim();
        if (!input.equals("1")) {
            cd.cambiarFichas(input);
        }
        menuPartida();
    }

    private static void subMenuSalir() {
        System.out.println("1. Abandonar partida");
        System.out.println("2. Guardar Partida");
        System.out.println("3. Volver atrás");
        System.out.print("Opción: ");
        String opt = in.nextLine().trim();
        switch (opt) {
            case "1":
                System.out.println("Jugador " + nombreJugador + " abandona.");
                break;
            case "2":
                System.out.print("Nombre para guardar: ");
                String n = in.nextLine().trim();
                if (!n.isEmpty()) cd.guardarPartida(n);
                break;
            case "3":
                try { menuPartida(); } catch (Exception ignored) {}
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

 
}
