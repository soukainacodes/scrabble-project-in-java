package Presentacion.Drivers;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;
import java.util.*;

/**
 * Driver para gestionar y probar la funcionalidad de juego de Scrabble.
 * Permite crear partidas, ejecutar juegos de prueba y simular partidas entre jugadores o IAs.
 */
public class DriverGestionJuego {

    private static final Scanner in = new Scanner(System.in);
    private static final CtrlDominio cd = new CtrlDominio();
    private static final String nombreJugador = "A";
    private static final String contrasena = "A";

    private static String modo = "";
    private static String idioma = "";

     /**
     * Constructor por defecto para la clase DriverGestionJuego.
     * Inicializa los valores necesarios para gestionar el juego.
     */
    private DriverGestionJuego() {
        // Constructor vacío
    }

    /**
     * Punto de entrada principal del driver.
     * Muestra el menú principal para jugar o ejecutar juegos de prueba.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        iniciarSesionAutomatica();

        while (true) {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Jugar");
            System.out.println("2. Juegos de Prueba");
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
                    pause();
            }
        }
    }

    /**
     * Inicia sesión automática con un usuario de prueba.
     * Si el usuario ya existe, ignora la excepción.
     */
    private static void iniciarSesionAutomatica() {
        try {
            cd.registrarJugador(nombreJugador, contrasena);
        } catch (UsuarioYaRegistradoException ignored) {}

        try {
            cd.iniciarSesion(nombreJugador, contrasena);
        } catch (Exception e) {
            System.err.println("Error al iniciar sesión automática: " + e.getMessage());
            pause();
        }
    }

    /**
     * Muestra el submenú para crear una nueva partida.
     * Permite elegir modo, idioma y finalizar la creación.
     */
    private static void subMenuCrearPartida() {
        while (true) {
            System.out.println("\n===== CREAR PARTIDA =====");
            System.out.println("1. Modo: " + modo);
            System.out.println("2. Idioma: " + idioma);
            System.out.println("3. Finalizar");
            System.out.print("Opción: ");

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
                        pause();
                    } else {
                        if (modo.equals("2 Jugadores")) {
                            try {
                                cd.registrarJugador("Invitado", "Invitado");
                            } catch (UsuarioYaRegistradoException ignored) {}
                        }
                        iniciarPartida();
                        return;
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
                    pause();
            }
        }
    }

    /**
     * Inicia una nueva partida con los parámetros seleccionados.
     * Si ocurre un error, lo muestra por pantalla.
     */
    private static void iniciarPartida() {
        try {
            cd.iniciarPartida(modo.equals("1 Jugador") ? 0 : 1, nombreJugador,
                    modo.equals("1 Jugador") ? "" : "Invitado", idioma, 123L, false);
            menuPartida();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pause();
        }
    }

    /**
     * Permite al usuario elegir el modo de juego (1 o 2 jugadores).
     */
    private static void elegirModo() {
        System.out.println("\n1. 1 Jugador\n2. 2 Jugadores");
        switch (in.nextLine().trim()) {
            case "1": modo = "1 Jugador"; break;
            case "2": modo = "2 Jugadores"; break;
            default: System.out.println("Opción no válida."); pause();
        }
    }

    /**
     * Permite al usuario elegir el idioma (diccionario) para la partida.
     */
    private static void elegirIdioma() {
        List<String> idiomas = new ArrayList<>(cd.obtenerIDsDiccionarios());
        for (int i = 0; i < idiomas.size(); i++) {
            System.out.println((i+1) + ". " + idiomas.get(i));
        }
        try {
            int opcion = Integer.parseInt(in.nextLine().trim());
            idioma = idiomas.get(opcion - 1);
        } catch (Exception e) {
            System.out.println("Opción no válida."); pause();
        }
    }

    /**
     * Muestra el menú de la partida en curso y gestiona las acciones del usuario.
     */
    private static void menuPartida() {
        while (true) {
            System.out.println("\n--- Estado de la partida ---");
            System.out.println(" Puntos Jugador 1: " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2: " + cd.getPuntosJugador2());
            mostrarTablero();
            mostrarFichas();

            System.out.println("\n1. Añadir ficha");
            System.out.println("2. Quitar ficha");
            System.out.println("3. Cambiar fichas");
            System.out.println("4. Pasar turno");
            System.out.println("5. Acabar turno");
            System.out.println("6. Salir");
            System.out.print("Opción: ");

            try {
                int resultado = gestionarOpcionesPartida(in.nextLine().trim());
                if (resultado != 0) {
                    finalPartida(resultado);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause();
            }
        }
    }

    /**
     * Gestiona las opciones seleccionadas por el usuario durante la partida.
     *
     * @param opt opción seleccionada por el usuario.
     * @return un entero indicando el estado final de la partida (0 si sigue).
     * @throws Exception si ocurre un error en la acción seleccionada.
     */
    private static int gestionarOpcionesPartida(String opt) throws Exception {
        switch (opt) {
            case "1": System.out.print("Ficha y posición: "); cd.jugarScrabble(1, in.nextLine()); break;
            case "2": System.out.print("Posición: "); cd.jugarScrabble(2, in.nextLine()); break;
            case "3": System.out.print("Fichas a cambiar: "); cd.jugarScrabble(5, in.nextLine()); break;
            case "4": return cd.jugarScrabble(3, "");
            case "5": return cd.jugarScrabble(4, "");
            case "6": subMenuSalir(); return 2;
            default: System.out.println("Opción no válida."); pause();
        }
        return 0;
    }

    /**
     * Muestra el submenú para salir de la partida, permitiendo abandonar o guardar.
     *
     * @throws Exception si ocurre un error al abandonar o guardar la partida.
     */
    private static void subMenuSalir() throws Exception {
        System.out.println("1. Abandonar partida\n2. Guardar partida");
        switch (in.nextLine().trim()) {
            case "1": int num = cd.jugarScrabble(6, ""); System.out.println("Jugador " + num + " abandona."); break;
            case "2": System.out.print("Nombre para guardar: "); cd.guardarPartida(in.nextLine()); break;
        }
    }

    /**
     * Muestra el resultado final de la partida.
     *
     * @param fin indica el jugador que ha ganado o el motivo de finalización.
     */
    private static void finalPartida(int fin) {
        System.out.println("Jugador " + fin + " gana");
        pause();
    }

    /**
     * Limpia la pantalla de la consola.
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Muestra las fichas actuales del jugador.
     */
    private static void mostrarFichas() {
        System.out.println("Fichas actuales: " + cd.obtenerFichas());
    }

    /**
     * Pausa la ejecución hasta que el usuario presione ENTER.
     */
    private static void pause() {
        System.out.println("\nPulsa ENTER para continuar...");
        in.nextLine();
    }

    /**
     * Muestra el tablero actual de la partida.
     */
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

    /**
     * Muestra el estado actual de la partida (puntos, tablero y fichas).
     */
    private static void mostrarEstado() {
        System.out.println("\n--- Estado de la partida ---");
        System.out.println(" Puntos J1: " + cd.getPuntosJugador1());
        System.out.println(" Puntos J2: " + cd.getPuntosJugador2());
        mostrarTablero();
        System.out.println("\n--- Fichas ---");
        mostrarFichas();
    }

    /**
     * Muestra el menú de juegos de prueba y ejecuta el seleccionado.
     */
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

    /**
     * Ejecuta el juego de pruebas 1: 1 jugador vs IA en catalán.
     */
    private static void juegoPruebas1() {
        // Limpiar pantalla e iniciar juego de prueba 1
        clearScreen();
        System.out.println("=== Ejecutando Juego de Pruebas 1 ===\n");
        System.out.println("Modo: 1 Jugador vs IA");
        System.out.println("Idioma: Catalán");
        String usuario = nombreJugador;  // Nombre del jugador humano

        try {
            // 1. Crear partida 1 jugador vs IA en catalán
            System.out.println("\nCreando partida...");
            cd.iniciarPartida(0, usuario, "", "Catalan", 123L, false);
            System.out.println("Partida creada correctamente.");

            // Mostrar estado inicial del tablero y fichas
            System.out.println("\n--- Estado inicial de la partida ---");
            System.out.println(" Puntos " + usuario + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();

            // 2. Añadir una ficha y mostrar tablero
            System.out.println("\nAñadiendo ficha 'T' en posición (7,7)...");
            try {
                cd.jugarScrabble(1, "T 7 7");
                System.out.println("Ficha 'T' añadida en (7,7).");
            } catch (Exception e) {
                System.out.println("Error al añadir ficha: " + e.getMessage());
            }
            mostrarTablero();

            // 3. Quitar la ficha añadida y mostrar tablero
            System.out.println("\nQuitando ficha de (7,7)...");
            try {
                cd.jugarScrabble(2, "7 7");
                System.out.println("Ficha removida de (7,7).");
            } catch (Exception e) {
                System.out.println("Error al quitar ficha: " + e.getMessage());
            }
            mostrarTablero();

            // 4. Colocar varias fichas para formar una palabra y finalizar el turno
            System.out.println("\nColocando fichas para formar la palabra \"REGLANT\"...");
            try {
                cd.jugarScrabble(1, "N 7 6");
                cd.jugarScrabble(1, "A 7 5");
                cd.jugarScrabble(1, "L 7 4");
                cd.jugarScrabble(1, "G 7 3");
                cd.jugarScrabble(1, "E 7 2");
                cd.jugarScrabble(1, "R 7 1");
                cd.jugarScrabble(1, "T 7 7");  // Colocar la 'T' finalmente en el centro
                System.out.println("Palabra formada: REGLANT");
            } catch (Exception e) {
                System.out.println("Error al colocar fichas: " + e.getMessage());
            }
            System.out.println("Finalizando turno del jugador...");
            try {
                cd.jugarScrabble(4, "");  // Acabar turno y puntuar palabra
            } catch (Exception e) {
                System.out.println("Error al finalizar turno: " + e.getMessage());
            }

            // Mostrar estado tras finalizar turno (puntuación actual y tablero, IA juega automáticamente)
            System.out.println("\n--- Estado tras turno del Jugador 1 ---");
            System.out.println(" Puntos " + usuario + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            mostrarTablero();

            // 5. Pasar turno y permitir que la IA juegue
            System.out.println("\nPasando turno del jugador...");
            try {
                cd.jugarScrabble(3, "");  // Pasar turno (sin colocar ficha)
                System.out.println("Turno pasado. (Ahora juega la IA).");
            } catch (Exception e) {
                System.out.println("Error al pasar turno: " + e.getMessage());
            }
            // Mostrar estado tras turno de la IA
            System.out.println("\n--- Estado tras turno de la IA ---");
            System.out.println(" Puntos " + usuario + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2 (IA): " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();

            // 6. Cambiar fichas del jugador (ejemplo: R, I, E, E, S) y mostrar nuevas fichas
            System.out.println("\nCambiando fichas [R, I, E, E, S] del jugador...");
            try {
                cd.jugarScrabble(5, "R I E E S");
                System.out.println("Fichas cambiadas correctamente.");
            } catch (Exception e) {
                System.out.println("Error al cambiar fichas: " + e.getMessage());
            }
            System.out.println("\n--- Fichas tras el cambio ---");
            mostrarFichas();

            // 7. Guardar la partida de prueba
            System.out.println("\nGuardando partida de prueba...");
            try {
                cd.guardarPartida("PRUEBA_1Jugador_Catalan");
                System.out.println("La partida se ha guardado correctamente.");
            } catch (Exception e) {
                System.out.println("Error al guardar la partida: " + e.getMessage());
            }
        } catch (Exception e) {
            // Captura de cualquier excepción no esperada durante el flujo de la prueba
            System.out.println("** Error en Juego de Pruebas 1: " + e.getMessage() + " **");
        }
        pause();

    }

    /**
     * Ejecuta el juego de pruebas 2: 2 jugadores humanos en inglés.
     */
    private static void juegoPruebas2() {
        // Limpiar pantalla e iniciar juego de prueba 2
        clearScreen();
        System.out.println("=== Ejecutando Juego de Pruebas 2 ===\n");
        System.out.println("Modo: 2 Jugadores (Jugador vs Jugador)");
        System.out.println("Idioma: Inglés");
        List<String> usuarios = Arrays.asList("Jordi", "Maria");
        String pwd = "123456";

        try {
            // Registrar usuarios de prueba (si no existen) e iniciar sesión
            for (String user : usuarios) {
                try {
                    cd.registrarJugador(user, pwd);
                } catch (UsuarioYaRegistradoException e) {
                    System.out.println("Aviso: " + e.getMessage());
                }
            }
            // Iniciar partida 2 jugadores en inglés
            System.out.println("\nCreando partida para " + usuarios.get(0) + " y " + usuarios.get(1) + "...");
            cd.iniciarPartida(1, usuarios.get(0), usuarios.get(1), "Ingles", 123L, false);
            System.out.println("Partida creada correctamente.");

            // Mostrar estado inicial
            System.out.println("\n--- Estado inicial de la partida ---");
            System.out.println(" Puntos " + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos " + usuarios.get(1) + ": " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();

            // Turno 1: Jugador 1 coloca una palabra
            System.out.println("\nTurno de " + usuarios.get(0) + " (Jugador 1)...");
            System.out.println(usuarios.get(0) + " añade las fichas para formar \"HANDLES\"...");
            try {
                cd.jugarScrabble(1, "H 7 3");
                cd.jugarScrabble(1, "A 7 4");
                cd.jugarScrabble(1, "N 7 5");
                cd.jugarScrabble(1, "D 7 6");
                cd.jugarScrabble(1, "L 7 7");
                cd.jugarScrabble(1, "E 7 8");
                cd.jugarScrabble(1, "S 7 9");
            } catch (Exception e) {
                System.out.println("Error al añadir fichas: " + e.getMessage());
            }
            System.out.println("Finalizando turno de " + usuarios.get(0) + "...");
            try {
                cd.jugarScrabble(4, "");
            } catch (Exception e) {
                System.out.println("Error al finalizar turno: " + e.getMessage());
            }

            // Mostrar estado tras turno 1
            System.out.println("\n--- Estado tras turno de " + usuarios.get(0) + " ---");
            System.out.println(" Puntos " + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos " + usuarios.get(1) + ": " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();

            // Turno 2: Jugador 2 coloca una palabra
            System.out.println("\nTurno de " + usuarios.get(1) + " (Jugador 2)...");
            System.out.println(usuarios.get(1) + " añade las fichas para formar \"WIENIE\"...");
            try {
                cd.jugarScrabble(1, "W 6 8");
                cd.jugarScrabble(1, "I 6 9");
                cd.jugarScrabble(1, "E 6 10");
                cd.jugarScrabble(1, "N 6 11");
                cd.jugarScrabble(1, "I 6 12");
                cd.jugarScrabble(1, "E 6 13");
            } catch (Exception e) {
                System.out.println("Error al añadir fichas: " + e.getMessage());
            }
            System.out.println("Finalizando turno de " + usuarios.get(1) + "...");
            try {
                cd.jugarScrabble(4, "");
            } catch (Exception e) {
                System.out.println("Error al finalizar turno: " + e.getMessage());
            }

            // Mostrar estado tras turno 2
            System.out.println("\n--- Estado tras turno de " + usuarios.get(1) + " ---");
            System.out.println(" Puntos " + usuarios.get(0) + ": " + cd.getPuntosJugador1());
            System.out.println(" Puntos " + usuarios.get(1) + ": " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();

            // Turno 3: Jugador 1 abandona la partida
            System.out.println("\nTurno de " + usuarios.get(0) + " (Jugador 1)...");
            System.out.println(usuarios.get(0) + " decide abandonar la partida.");
            int fin;
            try {
                fin = cd.jugarScrabble(6, "");  // Jugador 1 (actual) abandona
            } catch (Exception e) {
                // Capturar error si ocurre al abandonar
                System.out.println("Error al abandonar la partida: " + e.getMessage());
                fin = 2;  // asumir abandono jugador1 si excepción no específica
            }

            // Mostrar puntuación final y resultado de la partida
            if (fin != 0) {
                System.out.println("\n--- Puntuación final ---");
                System.out.println(" Puntos " + usuarios.get(0) + ": " + cd.getPuntosJugador1());
                System.out.println(" Puntos " + usuarios.get(1) + ": " + cd.getPuntosJugador2());
                if (fin == 2) {
                    System.out.println("Jugador 1 (" + usuarios.get(0) + ") abandona la partida.");
                    System.out.println("¡Jugador 2 (" + usuarios.get(1) + ") gana la partida!");
                } else if (fin == 1) {
                    System.out.println("Jugador 2 (" + usuarios.get(1) + ") abandona la partida.");
                    System.out.println("¡Jugador 1 (" + usuarios.get(0) + ") gana la partida!");
                }
            }
        } catch (Exception e) {
            System.out.println("** Error en Juego de Pruebas 2: " + e.getMessage() + " **");
        }
        pause();
    }

    /**
     * Ejecuta el juego de pruebas 3: IA vs IA en castellano.
     */
    private static void juegoPruebas3() {
        // Limpiar pantalla e iniciar juego de prueba 3
        clearScreen();
        System.out.println("=== Ejecutando Juego de Pruebas 3 ===\n");
        System.out.println("Modo: IA vs IA (dos inteligencias artificiales)");
        System.out.println("Idioma: Castellano");
        String usuario = nombreJugador;  // Se usará como referencia a Jugador1 si es necesario
        String nombreIA1 = "GLaDOS";
        String nombreIA2 = "Skynet";

        try {
            // Crear partida en modo 1 Jugador vs IA pero activando simulación IA vs IA
            System.out.println("\nCreando partida IA vs IA...");
            cd.iniciarPartida(0, usuario, "", "CastellanoCorto", 123L, true);
            System.out.println("Partida creada correctamente. (Jugadores IA: " + nombreIA1 + " vs " + nombreIA2 + ")");

            // Simular varios turnos automáticos de las IAs
            int turnosMax = 10;
            int fin = 0;
            int turnoActual = 1;
            while (turnoActual <= turnosMax && fin == 0) {
                System.out.println("\n--- Turno " + turnoActual + " ---");
                System.out.println(" Puntos " + nombreIA1 + ": " + cd.getPuntosJugador1());
                System.out.println(" Puntos " + nombreIA2 + ": " + cd.getPuntosJugador2());
                mostrarTablero();
                System.out.println("\n--- Fichas Disponibles (Jugador " + ((turnoActual % 2 == 1) ? "1" : "2") + ") ---");
                mostrarFichas();
                try {
                    fin = cd.jugarScrabble(7, "");  // Ejecutar jugada automática de la IA
                } catch (Exception e) {
                    System.out.println("Error durante jugada automática: " + e.getMessage());
                    fin = 1;  // terminar bucle por error, asumiendo abandono de Jugador 2
                }
                turnoActual++;
            }

            // Verificar cómo terminó la partida (por fin de juego o alcance de turnosMax)
            if (fin != 0) {
                System.out.println("\n--- Puntuación Final ---");
                System.out.println(" Puntos " + nombreIA1 + ": " + cd.getPuntosJugador1());
                System.out.println(" Puntos " + nombreIA2 + ": " + cd.getPuntosJugador2());
                if (fin == 2) {
                    System.out.println("Jugador 1 (" + nombreIA1 + ") abandona la partida.");
                    System.out.println("¡Jugador 2 (" + nombreIA2 + ") gana la partida!");
                } else if (fin == 1) {
                    System.out.println("Jugador 2 (" + nombreIA2 + ") abandona la partida.");
                    System.out.println("¡Jugador 1 (" + nombreIA1 + ") gana la partida!");
                } else if (fin == 3) {
                    System.out.println("La partida ha finalizado (sin fichas o sin movimientos posibles).");
                    // En este caso podría determinarse el ganador por puntos, si aplica.
                }
            } else {
                System.out.println("\nFin de la simulación de prueba tras " + turnosMax + " turnos.");
                System.out.println("Puntos " + nombreIA1 + ": " + cd.getPuntosJugador1()
                        + " | Puntos " + nombreIA2 + ": " + cd.getPuntosJugador2());
            }
        } catch (Exception e) {
            System.out.println("** Error en Juego de Pruebas 3: " + e.getMessage() + " **");
        }
        pause();
    }
}
