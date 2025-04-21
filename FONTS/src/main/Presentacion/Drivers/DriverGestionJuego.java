package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.w3c.dom.CDATASection;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;

;

public class DriverGestionJuego {

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
            throws PartidaYaExistenteException, UsuarioNoEncontradoException, PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException {
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
                menuJuegoPruebas();
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
                            123L,
                            true);
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

    private static void subMenuAnadir() throws PartidaYaExistenteException, UsuarioNoEncontradoException, PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException {
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
            throws  PartidaYaExistenteException, UsuarioNoEncontradoException, PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException {
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
            throws PartidaYaExistenteException, UsuarioNoEncontradoException, PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException {
        clearScreen();
        mostrarEstado();
        System.out.println("Inserta las fichas a cambiar (o '1' para atrás):");
        String input = in.nextLine().trim();
        if (!input.equals("1")) {
            cd.jugarScrabble(5, input);
        }
        menuPartida();
    }

    private static void subMenuSalir() throws PartidaYaExistenteException, UsuarioNoEncontradoException, PuntuacionInvalidaException, ComandoInvalidoException, PalabraInvalidaException {
        System.out.println("1. Abandonar partida");
        System.out.println("2. Guardar Partida");
        System.out.println("3. Volver atrás");
        System.out.print("Opción: ");
        String opt = in.nextLine().trim();
        switch (opt) {
            case "1":
                int num = cd.jugarScrabble(6, "");
                System.out.println("Jugador " + num + " abandona.");
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

    private static void menuJuegoPruebas() {
        System.out.println("1. Juego de Pruebas (Solo / Jugador VS IA) ");
        System.out.println("2. Juego de Pruebas (Duo / 2 Jugadores)");
        System.out.println("3. Juego de Pruebas (IA vs IA)");
        String opt = in.nextLine().trim();
        switch (opt) {
            case "1":
                juegoPruebas1();
                break;
            case "2":
                juegoPruebas2();
                break;
            case "3":
                juegoPruebas3();
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void juegoPruebas1() {
        // Limpia pantalla
        System.out.print("\033[H\033[2J");
        System.out.println("=== Ejecutando juego de pruebas 1 ===\n");
        List<String> usuarios = Arrays.asList("Jordi");
        String pwd = "123456";
        boolean errores = false;
        System.out.println("Juego de pruebas de Partida: ");
        System.out.println("Modo: 0 Judador vs Algoritmo ");
        System.out.println("Idioma: Catalán ");
        try {

            System.out.println("-> Creando partida...");
            //Modo 1 Jugador
            cd.iniciarPartida(0, usuarios.get(0), "", "Catalan", 123L, false);
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n\n--- Fichas Disponibles ---\n");
            mostrarFichas();

            System.out.println("-> Añadir ficha: " + "T");
            cd.jugarScrabble(1, "T 7 7");
            mostrarTablero();

            System.out.println("-> Quitar ficha: " + "T");;
            cd.jugarScrabble(2, "7 7");
            mostrarTablero();

            System.out.println("-> Añadiendo ficha...");
            cd.jugarScrabble(1, "N 7 6");
            System.out.println("-> Añadir ficha: " + "N");
            cd.jugarScrabble(1, "A 7 5");
            System.out.println("-> Añadir ficha: " + "A");
            cd.jugarScrabble(1, "L 7 4");
            System.out.println("-> Añadir ficha: " + "L");
            cd.jugarScrabble(1, "G 7 3");
            System.out.println("-> Añadir ficha: " + "G");
            cd.jugarScrabble(1, "E 7 2");
            System.out.println("-> Añadir ficha: " + "E");
            cd.jugarScrabble(1, "R 7 1");
            System.out.println("-> Añadir ficha: " + "R");

            System.out.println("-> Finalizando Turno. Resultado:" + "REGLAN\n");
            cd.jugarScrabble(4, "");

            mostrarTablero();

            System.out.println("\n\n-> Palabra incorrecta. 'REGLAN' no esta en el diccionario");

            System.out.println("-> Añadir ficha: " + "T");
            cd.jugarScrabble(1, "0 7 7");

            System.out.println("-> Finalizando Turno. Resultado:" + "REGLANT");
            cd.jugarScrabble(4, "");
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            System.out.println("-> Turno del algoritmo...\n");
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n\n--- Fichas Disponibles ---\n");
            mostrarFichas();

            System.out.println("-> Pasar turno..");
            cd.jugarScrabble(3, "");
            System.out.println("-> Turno del algoritmo...\n");
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n\n--- Fichas Disponibles ---\n");
            mostrarFichas();

            System.out.println("-> Cambiar fichas. R, I, E, E, S");
            cd.jugarScrabble(5, "0 3 E I 5");
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n\n--- Fichas Disponibles ---\n");
            mostrarFichas();


            System.out.println("-> Guardar partida.");
            cd.guardarPartida("PRUEBA_1Jugador_Catalan");
            System.out.println("-> Se ha guardado la partida correctamente..");

        } catch (Exception e) {
        }

    }

    private static void juegoPruebas2() {
        // Limpia pantalla
        System.out.print("\033[H\033[2J");
        System.out.println("=== Ejecutando juego de pruebas 2 ===\n");
        List<String> usuarios = Arrays.asList("Jordi", "Maria");
        String pwd = "123456";
        boolean errores = false;
        System.out.println("Juego de pruebas de Partida: ");
        System.out.println("Modo: 1 Judador vs Jugador ");
        System.out.println("Idioma: Catalán ");
        try {

            System.out.println("-> Creando partida...");
            //Modo 1 Jugador
            cd.iniciarPartida(1, usuarios.get(0), usuarios.get(1), "Ingles", 123L, false);
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos" + usuarios.get(1) + ": " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();

            System.out.println("-> Turno de " + usuarios.get(0));
            System.out.println("-> Añadiendo fichas...");
            cd.jugarScrabble(1, "H 7 3");
            cd.jugarScrabble(1, "A 7 4");
            cd.jugarScrabble(1, "N 7 5");
            cd.jugarScrabble(1, "D 7 6");
            cd.jugarScrabble(1, "L 7 7");
            cd.jugarScrabble(1, "E 7 8");
            cd.jugarScrabble(1, "S 7 9");
            System.out.println("-> Finalizando Turno...");
            cd.jugarScrabble(4, "");

            System.out.println("-> Turno de " + usuarios.get(1) + "\n");
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos" + usuarios.get(1) + ": " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();

            System.out.println("-> Añadiendo fichas...");

            cd.jugarScrabble(1, "W 6 8");
            cd.jugarScrabble(1, "I 6 9");
            cd.jugarScrabble(1, "E 6 10");
            cd.jugarScrabble(1, "N 6 11");
            cd.jugarScrabble(1, "I 6 12");
            cd.jugarScrabble(1, "E 6 13");

            System.out.println("-> Finalizando Turno...");
            cd.jugarScrabble(4, "");

            System.out.println("-> Turno de " + usuarios.get(0) + "\n");
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos" + usuarios.get(1) + ": " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();

            int fin = cd.jugarScrabble(6, "");
            if (fin != 0) {
                System.out.println("\n--- Puntuación final ---");
                System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
                System.out.println(" Puntos" + usuarios.get(1) + ": " + cd.getPuntosJugador2());
                if (fin == 2) {
                    System.out.println("-> Jugador 1 abandona..");
                    System.out.println("-> Gana Jugador 2.");
                } else if (fin == 1) {
                    System.out.println("-> Jugador 2 abandona..");
                    System.out.println("-> Gana Jugador 1.");
                }
            }

        } catch (Exception e) {
        }

    }

    private static void juegoPruebas3() {
        System.out.print("\033[H\033[2J");
        System.out.println("=== Ejecutando juego de pruebas 3 ===\n");
        List<String> usuarios = Arrays.asList("Jordi", "Maria");
        String pwd = "123456";
        boolean errores = false;
        System.out.println("Juego de pruebas de Partida: ");
        System.out.println("Modo: 1 Judador vs Jugador ");
        System.out.println("Idioma: Catalán ");

        try {

            System.out.println("-> Creando partida...");
            //Modo 1 Jugador
            cd.iniciarPartida(0, usuarios.get(0), "", "CastellanoCorto", 123L, true);

            for (int i = 0; i < 10; ++i) {
                System.out.println("\n--- Tablero Actual ---");
                System.out.println(" Puntos GLaDOS: " + cd.getPuntosJugador1());
                System.out.println(" Puntos Skynet: " + cd.getPuntosJugador2());
                mostrarTablero();
                System.out.println("\n--- Fichas Disponibles ---");
                mostrarFichas();
                int fin = cd.jugarScrabble(7, "");
                if (fin != 0) {
                    System.out.println("\n--- Puntuación final ---");
                    System.out.println(" Puntos" + usuarios.get(0) + ": " + cd.getPuntosJugador1());
                    System.out.println(" Puntos Skynet: " + cd.getPuntosJugador2());
                    if (fin == 2) {
                        System.out.println("-> Jugador 1 abandona..");
                        System.out.println("-> Gana Jugador 2.");
                    } else if (fin == 1) {
                        System.out.println("-> Jugador 2 abandona..");
                        System.out.println("-> Gana Jugador 1.");
                    }
                }
            }
        } catch (Exception e) {
        }
    }

}
