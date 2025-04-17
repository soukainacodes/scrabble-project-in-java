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

public class DriverPartidaAlgoritmo {

    // Scanner para entrada por teclado
    private static Scanner in = new Scanner(System.in);
    // Controlador de Dominio, que manejará la lógica de la partida
    private static CtrlDominio cd = new CtrlDominio();

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
                case DOBLE_LETRA:   return "DL";
                case TRIPLE_LETRA:  return "TL";
                case DOBLE_PALABRA: return "DP";
                case TRIPLE_PALABRA:return "TP";
                default:            return "  ";
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

    public static void main(String[] args) {
        System.out.println("Driver de prueba de Partida contra el Algoritmo (CtrlDominio)");

        // Registro del jugador
        String nombreJugador = "A";
        String contrasena    = "A";
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
            lineasArchivoBolsa     = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/letrasCAST.txt");
            lineasArchivoDiccionario = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/castellano.txt");
            cd.iniciarPartida(1, jugador.getNombre(), "", lineasArchivoDiccionario, lineasArchivoBolsa);
        } catch (IOException e) {
            System.err.println("Error al cargar archivos: " + e.getMessage());
            return;
        }

        // Bucle principal del juego
        String input;
        do {
            clearScreen();
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            System.out.println("\n Fichas IA: " + cd.obtenerFichasAlgoritmo() + "\n");
            mostrarTablero();
            System.out.println("\n\n--- Fichas Disponibles ---");
            mostrarFichas();
            input = in.nextLine().trim();
            if (!input.equalsIgnoreCase("salir") && !input.isEmpty()) {
                if (input.equalsIgnoreCase("guardar")) {
                    String nombre = in.nextLine().trim();
                    cd.guardarPartida(nombre);
                } else {
                    cd.jugarScrabble(input);
                }
            }
        } while (!input.equalsIgnoreCase("salir"));

        System.out.println("Partida finalizada. ¡Gracias por jugar!");
        in.close();
    }
}
