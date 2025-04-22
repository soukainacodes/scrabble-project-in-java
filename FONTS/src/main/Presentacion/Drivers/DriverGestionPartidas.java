package Presentacion.Drivers;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;
import java.util.*;

/**
 * Driver para gestionar y probar la funcionalidad de partidas.
 * Permite realizar operaciones como:
 * <ul>
 *   <li>Crear nuevas partidas.</li>
 *   <li>Cargar y continuar partidas guardadas.</li>
 *   <li>Gestionar partidas guardadas (listar, cargar, eliminar).</li>
 *   <li>Ejecutar pruebas automáticas de creación, guardado y eliminación de partidas.</li>
 * </ul>
 */


 /**
 * Constructor por defecto para la clase DriverGestionPartidas.
 * Inicializa los valores necesarios para gestionar las partidas.
 */
public class DriverGestionPartidas {
    private static final Scanner sc = new Scanner(System.in);
    private static final CtrlDominio cd = new CtrlDominio();

    private static final String MODO1 = "1 Jugador";
    private static final String MODO2 = "2 Jugadores";

    private static String modo          = "";
    private static String idDiccionario = "";
    private static String nombre1       = "";
    private static String nombre2       = "";

    /**
     * Punto de entrada principal del driver.
     * Muestra un menú principal para interactuar con las funcionalidades.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        while (true) {
            try {
                menuPrincipal();
            } catch (Exception e) {
                System.err.println("¡Error!: " + e.getMessage());
                pause();
            }
        }
    }

    /**
     * Muestra el menú principal y gestiona las opciones seleccionadas por el usuario.
     *
     * @throws PartidaYaExistenteException si ya existe una partida con el mismo ID
     * @throws PartidaNoEncontradaException si no se encuentra la partida
     * @throws NoHayPartidaGuardadaException si no hay una partida guardada reciente
     */
    private static void menuPrincipal() throws PartidaYaExistenteException, PartidaNoEncontradaException, NoHayPartidaGuardadaException {
        clearScreen();
        System.out.println("=== MENÚ PRINCIPAL ===");
        System.out.println("1) Nueva Partida");
        System.out.println("2) Seguir última partida");
        System.out.println("3) Cargar Partida");
        System.out.println("4) Gestión partidas guardadas");
        System.out.println("5) Juego de pruebas");
        System.out.println("6) Salir");
        System.out.print("Opción: ");

        switch (sc.nextLine().trim()) {
            case "1": subMenuCrearPartida();       break;
            case "2":
                cd.cargarUltimaPartida();
                subMenuPartida();
                break;
            case "3": subMenuCargarPartida();      break;
            case "4": subMenuGestionGuardadas();   break;
            case "5": juegoPruebas();              break;
            case "6": System.exit(0);
            default:
                System.out.println("Opción no válida.");
                pause();
        }
    }

    /**
     * Muestra un submenú para cargar una partida guardada.
     *
     * @throws PartidaNoEncontradaException si no se encuentra la partida
     * @throws PartidaYaExistenteException 
     */
    private static void subMenuCargarPartida() throws PartidaNoEncontradaException, PartidaYaExistenteException {
        clearScreen();
        System.out.println("=== CARGAR PARTIDA ===");
        List<String> partidas = new ArrayList<>(cd.obtenerNombresPartidasGuardadas());
        if (partidas.isEmpty()) {
            System.out.println("No hay partidas guardadas.");
            pause();
            return;
        }
        for (int i = 0; i < partidas.size(); i++) {
            System.out.printf("%2d) %s%n", i + 1, partidas.get(i));
        }
        System.out.print("Elige número o escribe el ID: ");
        String in = sc.nextLine().trim();
        String id;
        if (in.matches("\\d+")) {
            int idx = Integer.parseInt(in) - 1;
            if (idx < 0 || idx >= partidas.size()) {
                System.out.println("Selección fuera de rango.");
                pause();
                return;
            }
            id = partidas.get(idx);
        } else {
            id = in;
            if (!partidas.contains(id)) {
                System.out.println("ID no válido.");
                pause();
                return;
            }
        }
        cd.cargarPartida(id);
        System.out.println("Partida cargada.");
        pause();
        subMenuPartida();
    }

    /**
     * Muestra un submenú para gestionar partidas guardadas (listar, cargar, eliminar).
     *
     * @throws PartidaYaExistenteException si ya existe una partida con el mismo ID
     * @throws PartidaNoEncontradaException si no se encuentra la partida
     */
    private static void subMenuGestionGuardadas() throws PartidaYaExistenteException, PartidaNoEncontradaException {
        clearScreen();
        System.out.println("=== GESTIÓN PARTIDAS GUARDADAS ===");
        List<String> partidas = new ArrayList<>(cd.obtenerNombresPartidasGuardadas());
        if (partidas.isEmpty()) {
            System.out.println("No hay partidas guardadas.");
            pause();
            return;
        }
        for (int i = 0; i < partidas.size(); i++) {
            System.out.printf("%2d) %s%n", i + 1, partidas.get(i));
        }
        System.out.println("0) Volver");
        System.out.print("Elige número para cargar/eliminar: ");
        String in = sc.nextLine().trim();
        if (in.equals("0")) return;

        String id;
        if (in.matches("\\d+")) {
            int idx = Integer.parseInt(in) - 1;
            if (idx < 0 || idx >= partidas.size()) {
                System.out.println("Selección fuera de rango.");
                pause();
                return;
            }
            id = partidas.get(idx);
        } else {
            id = in;
            if (!partidas.contains(id)) {
                System.out.println("ID no válido.");
                pause();
                return;
            }
        }

        System.out.printf("Has elegido \"%s\". ¿Qué deseas hacer?%n", id);
        System.out.println("1) Cargar");
        System.out.println("2) Eliminar");
        System.out.println("0) Volver");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1":
                cd.cargarPartida(id);
                System.out.println("Partida cargada.");
                pause();
                subMenuPartida();
                break;
            case "2":
                cd.eliminarPartidaGuardada(id);
                System.out.println("Partida eliminada.");
                pause();
                break;
            default:
                // 0 o cualquier otra: volver
        }
    }

    /**
     * Muestra un submenú para crear una nueva partida.
     *
     * @throws PartidaYaExistenteException si ya existe una partida con el mismo ID
     */
    private static void subMenuCrearPartida() throws PartidaYaExistenteException {
        while (true) {
            clearScreen();
            System.out.println("=== CREAR NUEVA PARTIDA ===");
            System.out.printf("Modo        : %s%n", modo.isEmpty()          ? "<no seleccionado>" : modo);
            System.out.printf("Diccionario : %s%n", idDiccionario.isEmpty() ? "<no seleccionado>" : idDiccionario);
            System.out.printf("Jugador 1   : %s%n", nombre1.isEmpty()      ? "<no asignado>"     : nombre1);
            if (modo.equals(MODO2)) {
                System.out.printf("Jugador 2   : %s%n", nombre2.isEmpty()  ? "<no asignado>" : nombre2);
            } else {
                System.out.printf("Jugador 2   : <IA>%n");
            }
            System.out.println("1) Elegir modo");
            System.out.println("2) Elegir diccionario");
            System.out.println("3) Introducir nombres");
            System.out.println("4) Finalizar y jugar");
            System.out.println("5) Volver");
            System.out.print("Opción: ");

            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1": elegirModo();              break;
                case "2": elegirDiccionario();       break;
                case "3": introducirNombres();       break;
                case "4":
                    if (!datosCompletos()) {
                        System.out.println("¡Faltan datos! Completa modo, diccionario y nombres.");
                        pause();
                    } else {
                        iniciarYJugar();
                        return;
                    }
                    break;
                case "5": return;
                default:
                    System.out.println("Opción no válida.");
                    pause();
            }
        }
    }

    /**
     * Comprueba si los datos necesarios para iniciar una partida están completos.
     *
     * @return true si los datos están completos, false en caso contrario
     */
    private static boolean datosCompletos() {
        if (modo.isEmpty() || idDiccionario.isEmpty() || nombre1.isBlank()) return false;
        if (modo.equals(MODO2) && nombre2.isBlank()) return false;
        return true;
    }

    /**
     * Inicia una nueva partida con los datos proporcionados y muestra el submenú de partida.
     *
     * @throws PartidaYaExistenteException si ya existe una partida con el mismo ID
     */
    private static void iniciarYJugar() throws PartidaYaExistenteException {
        long seed = new Random().nextLong();
        int modoInt = modo.equals(MODO1) ? 0 : 1;

        try {
            cd.iniciarPartida(
                modoInt,
                nombre1,
                modoInt == 1 ? nombre2 : "",
                idDiccionario,
                seed,
                false
            );
            subMenuPartida();
        } catch (DiccionarioNoEncontradoException | BolsaNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Muestra un submenú para gestionar la partida actual.
     *
     * @throws PartidaYaExistenteException si ya existe una partida con el mismo ID
     */
    private static void subMenuPartida() throws PartidaYaExistenteException {
        while (true) {
            clearScreen();
            System.out.println("=== PARTIDA ACTUAL ===");
            imprimirTablero();
            System.out.println("\nFichas: " + cd.obtenerFichas());
            System.out.println("1) Abandonar (volver)");
            System.out.println("2) Guardar partida");
            System.out.println("3) Continuar jugada");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1": return;
                case "2":
                    System.out.print("Nombre para guardar: ");
                    String save = sc.nextLine().trim();
                    if (!save.isEmpty()) {
                        cd.guardarPartida(save);
                        System.out.println("Guardada como «" + save + "»");
                    } else {
                        System.out.println("Nombre inválido.");
                    }
                    pause();
                    break;
                case "3":
                    System.out.println("[Aquí iría el menú de jugadas]");
                    pause();
                    break;
                default:
                    System.out.println("Opción no válida.");
                    pause();
            }
        }
    }

    /**
     * Imprime el tablero actual de la partida.
     */
    private static void imprimirTablero() {
        int N = cd.getTableroDimension();
        System.out.print("    ");
        for (int j = 0; j < N; j++) System.out.printf("%3d", j);
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

    /**
     * Ejecuta un conjunto de pruebas automáticas para verificar la funcionalidad
     * de creación, guardado y eliminación de partidas.
     */
    private static void juegoPruebas() {
        clearScreen();
        System.out.println("=== JUEGO DE PRUEBAS ===");
        boolean falloGlobal = false;

        List<String> modos = List.of(MODO1, MODO2);
        List<String> dicts = new ArrayList<>(cd.obtenerIDsDiccionarios());
        if (dicts.isEmpty()) {
            System.out.println("No hay diccionarios para probar.");
            pause();
            return;
        }

        for (String m : modos) {
            for (String d : dicts) {
                System.out.printf("%n→ Prueba [Modo=%s, Dic=%s]%n", m, d);
                boolean falloCaso = false;
                String saveId = "PRUEBA_" + m.replace(" ", "") + "_" + d;
                int modoInt = m.equals(MODO1) ? 0 : 1;
                long seed  = new Random().nextLong();

                try {
                    System.out.print("  • Crear partida... ");
                    cd.iniciarPartida(modoInt, "Test1", modoInt==1?"Test2":"", d, seed, false);
                    System.out.println("OK");

                    System.out.print("  • Guardar partida \"" + saveId + "\"... ");
                    cd.guardarPartida(saveId);
                    System.out.println("OK");

                    System.out.print("  • Listar partidas guardadas... ");
                    List<String> list = new ArrayList<>(cd.obtenerNombresPartidasGuardadas());
                    System.out.println(list.contains(saveId) ? "OK" : "FALLÓ");
                    if (!list.contains(saveId)) falloCaso = true;

                    System.out.print("  • Cargar partida... ");
                    cd.cargarPartida(saveId);
                    System.out.println("OK");

                    System.out.print("  • Eliminar partida... ");
                    cd.eliminarPartidaGuardada(saveId);
                    System.out.println("OK");

                    System.out.print("  • Listar tras eliminación... ");
                    list = new ArrayList<>(cd.obtenerNombresPartidasGuardadas());
                    System.out.println(!list.contains(saveId) ? "OK" : "FALLÓ");
                    if (list.contains(saveId)) falloCaso = true;

                } catch (Exception ex) {
                    System.out.println("ERROR → " + ex.getMessage());
                    falloCaso = true;
                }

                System.out.println("  Resultado: " + (falloCaso ? "FALLÓ" : "OK"));
                if (falloCaso) falloGlobal = true;
            }
        }

        System.out.println("\n" + (falloGlobal
            ? "ALGUNAS PRUEBAS FALLARON."
            : "TODAS LAS PRUEBAS PASARON CORRECTAMENTE."));
        pause();
    }

    /**
     * Permite al usuario elegir el modo de juego.
     */
    private static void elegirModo() {
        clearScreen();
        System.out.println("=== ELEGIR MODO ===");
        System.out.println("1) " + MODO1);
        System.out.println("2) " + MODO2);
        System.out.print("Opción: ");
        String opt = sc.nextLine().trim();
        switch (opt) {
            case "1": modo = MODO1; break;
            case "2": modo = MODO2; break;
            default:
                System.out.println("Opción no válida.");
                pause();
        }
    }

    /**
     * Permite al usuario elegir un diccionario.
     */
    private static void elegirDiccionario() {
        clearScreen();
        System.out.println("=== ELEGIR DICCIONARIO ===");
        List<String> diccionarios = new ArrayList<>(cd.obtenerIDsDiccionarios());
        if (diccionarios.isEmpty()) {
            System.out.println("No hay diccionarios disponibles.");
            pause();
            return;
        }
        for (int i = 0; i < diccionarios.size(); i++) {
            System.out.printf("%2d) %s%n", i + 1, diccionarios.get(i));
        }
        System.out.print("Elige número o escribe el ID: ");
        String in = sc.nextLine().trim();
        if (in.matches("\\d+")) {
            int idx = Integer.parseInt(in) - 1;
            if (idx >= 0 && idx < diccionarios.size()) {
                idDiccionario = diccionarios.get(idx);
            } else {
                System.out.println("Selección fuera de rango.");
                pause();
            }
        } else if (diccionarios.contains(in)) {
            idDiccionario = in;
        } else {
            System.out.println("ID no válido.");
            pause();
        }
    }

    /**
     * Pausa la ejecución hasta que el usuario presione ENTER.
     */
    private static void pause() {
        System.out.println("\nPulsa ENTER para continuar...");
        sc.nextLine();
    }

    /**
     * Permite al usuario introducir los nombres de los jugadores.
     */
    private static void introducirNombres() {
        clearScreen();
        System.out.println("=== INTRODUCIR NOMBRES ===");
        System.out.print("Nombre del Jugador 1: ");
        nombre1 = sc.nextLine().trim();
        if (modo.equals(MODO2)) {
            System.out.print("Nombre del Jugador 2: ");
            nombre2 = sc.nextLine().trim();
        }
    }

    /**
     * Limpia la pantalla de la consola.
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
