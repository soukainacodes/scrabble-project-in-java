package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Dominio.CtrlDominio;
import Dominio.Modelos.Tablero;
import Dominio.Modelos.Celda;
import Dominio.Modelos.TipoBonificacion;
import Dominio.Excepciones.FichaIncorrecta;
import Dominio.Excepciones.PosicionOcupadaTablero;
import Dominio.Excepciones.PosicionVaciaTablero;

public class DriverGestionPartidas {
    private static final Scanner sc = new Scanner(System.in);
    private static final CtrlDominio cd = new CtrlDominio();
    private static String modo = "", dificultad = "", idioma = "";

    public static void main(String[] args) {
        while (true) {
            try {
                menuPrincipal();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                sc.nextLine();
            }
        }
    }

    private static void menuPrincipal() throws Exception {
        clearScreen();
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Jugar");
        System.out.println("2. Juego de pruebas");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1": subMenuGestionPartida(); break;
            case "2": juegoPruebas();         break;
            case "3": System.exit(0);
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void juegoPruebas() {
        clearScreen();
        System.out.println("=== Ejecutando juego de pruebas de partidas ===\n");
        boolean errores = false;
        List<String> modos = Arrays.asList("1 Jugador", "2 Jugadores");
        List<String> idiomas = Arrays.asList("Catalán", "Castellano", "Inglés");
        List<String> dificultades = Arrays.asList("Fácil", "Intermedio", "Difícil");

        for (String m : modos) {
            for (String idio : idiomas) {
                for (String dif : dificultades) {
                    try {
                        System.out.println(
                          String.format("--- Prueba: modo=%s, idioma=%s, dificultad=%s ---", m, idio, dif)
                        );
                        modo = m; idioma = idio; dificultad = dif;

                        // 1) Iniciar partida de prueba
                        cd.iniciarPartida(
                            m.equals("1 Jugador") ? 0 : 1,
                            "PruebaUser", "PruebaUser",
                            leerArchivo(obtenerPathDiccionario(idio)),
                            leerArchivo(obtenerPathBolsa(idio)),
                            0L,
                            dif.equals("Fácil")    ? 1 :
                            dif.equals("Intermedio")? 2 : 3
                        );

                        // 2) Guardar partida
                        String saveName = "test_" + m.replace(" ", "") + "_" + idio + "_" + dif;
                        cd.guardarPartida(saveName);
                        System.out.println("- Guardada como: " + saveName);

                        // 3) Listar partidas guardadas
                        List<String> partidas = new ArrayList<>(cd.obtenerNombresPartidasGuardadas());
                        System.out.println("Partidas disponibles: " + partidas);

                        // 4) Cargar esa partida
                        cd.cargarPartida(saveName);
                        System.out.println("- Cargada: " + saveName);

                        // 5) Cargar la última partida
                        cd.cargarUltimaPartida();
                        System.out.println("- Última partida recargada.");

                        // 6) Eliminar de persistencia
                        cd.eliminarPartidaGuardada(saveName);
                        System.out.println("- Partida eliminada de la persistencia.");

                        System.out.println("Prueba completada con éxito.\n");
                    } catch (Exception e) {
                        errores = true;
                        System.err.println(
                          String.format("Error en prueba [%s, %s, %s]: %s\n",
                                        modo, idioma, dificultad, e.getMessage())
                        );
                    }
                }
            }
        }
        System.out.println("---------------------------------------------");
        System.out.println(
            !errores
            ? "Todas las pruebas se ejecutaron correctamente."
            : "Algunas pruebas fallaron. Revisar errores."
        );
        System.out.println("---------------------------------------------");
    }

    private static void subMenuGestionPartida() throws Exception {
        clearScreen();
        System.out.println("\n===== GESTIÓN PARTIDAS =====");
        System.out.println("1. Nueva Partida");
        System.out.println("2. Seguir última partida");
        System.out.println("3. Cargar Partida");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1":
                subMenuCrearPartida();
                break;
            case "2":
                cd.cargarUltimaPartida();
                subMenuPartida();
                break;
            case "3":
                subMenuCargarPartida();
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void subMenuCargarPartida() {
        clearScreen();
        System.out.println("\n===== CARGAR PARTIDA =====");
        Set<String> nombresSet = cd.obtenerNombresPartidasGuardadas();
        if (nombresSet.isEmpty()) {
            System.out.println("No hay partidas guardadas.");
            return;
        }
        List<String> nombres = new ArrayList<>(nombresSet);
        System.out.println("Partidas disponibles:");
        for (int i = 0; i < nombres.size(); i++) {
            System.out.printf(" %d. %s\n", i + 1, nombres.get(i));
        }
        System.out.print("Elige número o nombre ('0' para volver): ");
        String input = sc.nextLine().trim();
        if ("0".equals(input)) return;
        String elegido = input.matches("\\d+")
                       ? nombres.get(Integer.parseInt(input) - 1)
                       : input;
        cd.cargarPartida(elegido);
        subMenuPartida();
    }

    private static void subMenuCrearPartida() throws Exception {
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. Elegir Modo:       " + modo);
        System.out.println("2. Elegir Idioma:     " + idioma);
        System.out.println("3. Elegir Dificultad: " + dificultad);
        System.out.println("4. Finalizar");
        switch (sc.nextLine().trim()) {
            case "1":
                subMenuElegirModo();
                break;
            case "2":
                subMenuElegirIdioma();
                break;
            case "3":
                subMenuElegirDificultad();
                break;
            case "4":
                if (!modo.isEmpty() && !idioma.isEmpty() && !dificultad.isEmpty()) {
                    cd.iniciarPartida(
                        modo.equals("1 Jugador") ? 0 : 1,
                        "Alex", "",
                        leerArchivo(obtenerPathDiccionario(idioma)),
                        leerArchivo(obtenerPathBolsa(idioma)),
                        0L,
                        dificultad.equals("Fácil")    ? 1 :
                        dificultad.equals("Intermedio")? 2 : 3
                    );
                    subMenuPartida();
                } else {
                    System.out.println("Completa todos los parámetros antes de finalizar.");
                }
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void subMenuElegirModo() throws Exception {
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. 1 Jugador");
        System.out.println("2. 2 Jugadores");
        String opt = sc.nextLine().trim();
        if ("1".equals(opt)) modo = "1 Jugador";
        else if ("2".equals(opt)) modo = "2 Jugadores";
        else System.out.println("Opción no válida.");
        subMenuCrearPartida();
    }

    private static void subMenuElegirIdioma() throws Exception {
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. Catalán");
        System.out.println("2. Castellano");
        System.out.println("3. Inglés");
        String opt = sc.nextLine().trim();
        if ("1".equals(opt)) idioma = "Catalán";
        else if ("2".equals(opt)) idioma = "Castellano";
        else if ("3".equals(opt)) idioma = "Inglés";
        else System.out.println("Opción no válida.");
        subMenuCrearPartida();
    }

    private static void subMenuElegirDificultad() throws Exception {
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. Fácil");
        System.out.println("2. Intermedio");
        System.out.println("3. Difícil");
        String opt = sc.nextLine().trim();
        if ("1".equals(opt)) dificultad = "Fácil";
        else if ("2".equals(opt)) dificultad = "Intermedio";
        else if ("3".equals(opt)) dificultad = "Difícil";
        else System.out.println("Opción no válida.");
        subMenuCrearPartida();
    }

    private static void subMenuPartida() {
        try {
            clearScreen();
            System.out.println("\n--- TABLERO ACTUAL ---");
            System.out.println(" Puntos J1: " + cd.getPuntosJugador1());
            System.out.println(" Puntos J2: " + cd.getPuntosJugador2());
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            System.out.println(cd.obtenerFichas());
            System.out.println("\n1. Abandonar partida");
            System.out.println("2. Guardar Partida");
            System.out.print("Opción: ");
            String opt = sc.nextLine().trim();
            if ("1".equals(opt)) {
                cd.cargarUltimaPartida();
            } else if ("2".equals(opt)) {
                subMenuGuardar();
            } else {
                System.out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void subMenuGuardar() {
        try {
            clearScreen();
            System.out.print("Nombre de la partida para guardar: ");
            String name = sc.nextLine().trim();
            if (!name.isEmpty()) {
                cd.guardarPartida(name);
                System.out.println("Partida guardada como: " + name);
            } else {
                System.out.println("Nombre inválido.");
            }
        } catch (Exception e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    private static void mostrarTablero() {
        Tablero t = cd.obtenerTablero();
        System.out.print("    ");
        for (int j = 0; j < 15; j++) System.out.printf("%4d", j);
        System.out.println();
        for (int i = 0; i < 15; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < 15; j++) {
                Celda cel = t.getCelda(i, j);
                String disp;
                if (cel.getFicha() != null) {
                    disp = cel.getFicha().getLetra();
                } else if (!cel.bonusDisponible() && cel.getBonificacion() != TipoBonificacion.NINGUNA) {
                    disp = "US";
                } else {
                    disp = switch (cel.getBonificacion()) {
                        case DOBLE_LETRA    -> "DL";
                        case TRIPLE_LETRA   -> "TL";
                        case DOBLE_PALABRA  -> "DP";
                        case TRIPLE_PALABRA -> "TP";
                        default              -> "  ";
                    };
                }
                System.out.printf("[%2s]", disp);
            }
            System.out.println();
        }
    }

    private static String obtenerPathDiccionario(String idio) {
        return "./FONTS/src/main/Recursos/Idiomas/"
             + switch (idio) {
                 case "Catalán"    -> "Catalan";
                 case "Castellano" -> "Castellano";
                 case "Inglés"     -> "Ingles";
                 default            -> "";
               }
             + "/"
             + switch (idio) {
                 case "Catalán"   -> "catalan.txt";
                 case "Castellano"-> "castellano.txt";
                 case "Inglés"    -> "english.txt";
                 default           -> "";
               };
    }

    private static String obtenerPathBolsa(String idio) {
        return "./FONTS/src/main/Recursos/Idiomas/"
             + switch (idio) {
                 case "Catalán"    -> "Catalan";
                 case "Castellano" -> "Castellano";
                 case "Inglés"     -> "Ingles";
                 default            -> "";
               }
             + "/"
             + switch (idio) {
                 case "Catalán"   -> "letrasCAT.txt";
                 case "Castellano"-> "letrasCAST.txt";
                 case "Inglés"    -> "letrasENG.txt";
                 default           -> "";
               };
    }

    private static List<String> leerArchivo(String ruta) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    lineas.add(linea);
                }
            }
        }
        return lineas;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
