package Presentacion.Drivers;

import java.util.*;
import java.io.*;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;

public class DriverPartidaCon2Jugadores {

    private static final Scanner in = new Scanner(System.in);
    private static final CtrlDominio cd = new CtrlDominio();
    private static String jugador1, jugador2;
    private static String turnoActual;
    private static final int MODO_PARTIDA = 1;

    public static void main(String[] args) throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        bienvenida();
        crearYObtenerJugadores();
        cargarYConstruirPartida();
        turnoActual = jugador1;
        buclePrincipal();
    }

    private static void bienvenida() {
        System.out.println("==========================================");
        System.out.println("   DRIVER INTERACTIVO: PARTIDA SCRABBLE   ");
        System.out.println("==========================================\n");
    }

    private static void crearYObtenerJugadores() {
        System.out.print("Nombre jugador 1: ");
        jugador1 = in.nextLine().trim();
        System.out.print("Nombre jugador 2: ");
        jugador2 = in.nextLine().trim();
        String pwd = "1234";

        try {
            cd.registrarJugador(jugador1, pwd);
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("(*) " + e.getMessage());
        }
        try {
            cd.registrarJugador(jugador2, pwd);
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("(*) " + e.getMessage());
        }

        System.out.println("\nJugadores listos: " + jugador1 + " vs " + jugador2 + "\n");
    }

    private static void cargarYConstruirPartida() {
        try {
            List<String> lineasBolsa = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/letrasCAST.txt");
            List<String> lineasDicc  = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/castellano.txt");
            cd.iniciarPartida(MODO_PARTIDA, jugador1, jugador2, lineasDicc, lineasBolsa, 0L, -1);
        } catch (IOException e) {
            System.err.println("Error al leer ficheros: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void buclePrincipal() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = in.nextLine().trim();
            switch (opcion) {
                case "1": mostrarTablero();   break;
                case "2": mostrarFichas();    break;
                case "3": jugarTurno();       break;
                case "4": cambiarTurno();     break;
                case "5": salir = true; System.out.println("¡Hasta luego!"); break;
                default:  System.out.println("Opción no válida.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n------ Menú de opciones (Turno: " + turnoActual + ") ------");
        System.out.println("1) Ver tablero");
        System.out.println("2) Ver fichas de " + turnoActual);
        System.out.println("3) Jugar palabra");
        System.out.println("4) Cambiar turno");
        System.out.println("5) Salir");
        System.out.print("Selecciona una opción: ");
    }

    private static void mostrarTablero() {
        System.out.println("\n    Tablero actual:");
        int N = cd.getTableroDimension();
        System.out.print("    ");
        for (int x = 0; x < N; x++) System.out.printf("%4d", x);
        System.out.println();
        for (int y = 0; y < N; y++) {
            System.out.printf("%2d: ", y);
            for (int x = 0; x < N; x++) {
                String letra = cd.getLetraCelda(y, x);
                String bono  = cd.getBonusCelda(y, x);
                String d     = letra != null ? letra : bono;
                System.out.printf("[%2s]", d);
            }
            System.out.println();
        }
    }

    private static void mostrarFichas() {
        System.out.println("\nFichas de " + turnoActual + ":");
        System.out.println("  " + cd.obtenerFichas());
    }

    private static void jugarTurno() throws PosicionOcupadaTablero, FichaIncorrecta, PosicionVaciaTablero {
        System.out.print("\nIntroduce palabra (o 'cancelar'): ");
        String palabra = in.nextLine().trim();
        if ("cancelar".equalsIgnoreCase(palabra)) {
            System.out.println("Turno de " + turnoActual + " cancelado.");
            return;
        }
        cd.jugarScrabble(MODO_PARTIDA, palabra);
        System.out.println("Palabra jugada: " + palabra);
        mostrarTablero();
        mostrarFichas();
    }

    private static void cambiarTurno() {
        turnoActual = turnoActual.equals(jugador1) ? jugador2 : jugador1;
        System.out.println("----- Ahora juega: " + turnoActual + " -----");
    }

    private static List<String> leerArchivo(String ruta) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String l;
            while ((l = br.readLine()) != null) {
                l = l.trim();
                if (!l.isEmpty()) lines.add(l);
            }
        }
        return lines;
    }
}
