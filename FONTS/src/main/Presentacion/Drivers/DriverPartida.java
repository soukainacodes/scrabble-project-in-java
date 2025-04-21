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
    private static final String contrasena = "A";
    private static int fin = 0;
    private static String modo = "", idioma = "";

    public static void main(String[] args) {
        System.out.println("Driver de prueba de Partida contra el Algoritmo (CtrlDominio)");

        // Registro del jugador (si ya existe, lo ignoramos)
        try {
            cd.registrarJugador(nombreJugador, contrasena);
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        // Bucle de menú principal de la partida
        try {
            menuPrincipal();
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
        for (int j = 0; j < N; j++) {
            System.out.printf("%4d", j);
        }
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < N; j++) {
                String letra = cd.getLetraCelda(i, j);
                String bono = cd.getBonusCelda(i, j);
                String disp = letra != null ? letra : bono;
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
            case "1": //Añadir Ficha
                subMenuAnadir();
                break;
            case "2": //Quitar Ficha
                subMenuQuitar();
                break;
            case "3": //Cambiar Fichas
                subMenuCambiar();
                break;
            case "4": //Pasar Turno
                fin = cd.jugarScrabble(3, "");
                if (fin == 0) {
                    menuPartida();
                } else {
                    finalPartida(fin);
                    break;
                }
            case "5": //Acabar turno
                fin = cd.jugarScrabble(4, "");

                if (fin == 0) {
                    menuPartida();
                } else {
                    finalPartida(fin);
                    break;
                }
            case "6":
                subMenuSalir();
                break;
            case "7":
                fin = cd.jugarScrabble(7, "");
                if (fin == 0) {
                    menuPartida();
                } else {
                    finalPartida(fin);
                    break;
                }
                break;
            default:
                System.out.println("Opción no válida.");
                menuPartida();
        }
    }

    private static void finalPartida(int fin) {
        if (fin == 1) {
            System.out.println("Jugador 1 gana");

        } else {
            System.out.println("Jugador 2 gana");

        }
    }

    private static void menuPrincipal() throws Exception {
        clearScreen();
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Jugar");
        System.out.println("2. Juego de pruebas");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
        switch (in.nextLine().trim()) {
            case "1":
                subMenuCrearPartida();
                break;
            case "2":
                  juegoPruebas();
                break;
            case "3":
                System.exit(0);
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void subMenuCrearPartida() throws Exception {
       // clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. Modo:       " + modo);
        System.out.println("2. Idioma:     " + idioma);
        System.out.println("3. Finalizar");
        switch (in.nextLine().trim()) {
            case "1":
                elegirModo();
                break;
            case "2":
                elegirIdioma();
                break;
            case "3":
                if (modo.isEmpty() || idioma.isEmpty()) {
                    System.out.println("Completa todos los parámetros.");
                } else {
                    cd.iniciarPartida(
                            modo.equals("1 Jugador") ? 0 : 1,
                            "PruebaUser", "PruebaUser",
                            idioma,
                            123L);
                    menuPartida();
                }
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void elegirModo() throws Exception {
        clearScreen();
        System.out.println("\n1. 1 Jugador\n2. 2 Jugadores");
        String o = in.nextLine().trim();
        if (o.equals("1")) {
            modo = "1 Jugador";
        } else if (o.equals("2")) {
            modo = "2 Jugadores";
        } else {
            System.out.println("Opción no válida.");
        }
        subMenuCrearPartida();
    }

    private static void elegirIdioma() throws Exception {
        clearScreen();
        int i = 1;
        for (String s : cd.obtenerIDsDiccionarios()) {
            System.out.println(i + ". " + s);
            i++;
        }

        String entrada = in.nextLine().trim();
        i = 1;
        for (String s : cd.obtenerIDsDiccionarios()) {
            if (entrada.equals(String.valueOf(i))) {
                idioma = s;
            }
            i++;

        }

        subMenuCrearPartida();
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
        String input = in.nextLine().trim();
        if (!input.equals("1")) {
            cd.jugarScrabble(1, input);
        }
        menuPartida();

    }

    private static void subMenuQuitar()
            throws PosicionOcupadaTablero,
            FichaIncorrecta,
            PosicionVaciaTablero {
        clearScreen();
        mostrarEstado();
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
        clearScreen();
        mostrarEstado();
        System.out.println("Inserta las fichas a cambiar (o '1' para atrás):");
        String input = in.nextLine().trim();
        if (!input.equals("1")) {
            cd.jugarScrabble(5, input);
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
                if (!n.isEmpty()) {
                    cd.guardarPartida(n);
                }
                break;
            case "3":
                try {
                    menuPartida();
                } catch (Exception ignored) {
                }
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void juegoPruebas() {
        // Limpia pantalla
        System.out.print("\033[H\033[2J");
        System.out.println("=== Ejecutando juego de pruebas ===\n");
        List<String> usuarios = Arrays.asList("Jordi");
        String pwd = "123456";
        boolean errores = false;
        System.out.println("Juego de pruebas de Partida: ");
        System.out.println("Modo: 1 Judador vs Algoritmo ");
        System.out.println("Idioma: Catalán ");
        try {

            int modo = 0; // 1 Jugador
            int idioma = 0;
            System.out.println("-> Creando partida...");
            cd.iniciarPartida(0, usuarios.get(0), "", "Catalan", 123L);
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();
        } catch (Exception e) {
        }

    }

}
