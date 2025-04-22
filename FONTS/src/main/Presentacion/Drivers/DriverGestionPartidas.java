package Presentacion.Drivers;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;
import java.util.*;

public class DriverGestionPartidas {
    private static final Scanner sc = new Scanner(System.in);
    private static final CtrlDominio cd = new CtrlDominio();

    private static final String MODO1 = "1 Jugador";
    private static final String MODO2 = "2 Jugadores";

    private static String modo          = "";
    private static String idDiccionario = "";
    private static String nombre1       = "";
    private static String nombre2       = "";

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

    private static void menuPrincipal() throws PartidaYaExistenteException, PartidaNoEncontradaException, NoHayPartidaGuardadaException {
        clearScreen();
        System.out.println("=== MENÚ PRINCIPAL ===");
        System.out.println("1) Nueva Partida");
        System.out.println("2) Seguir última partida");
        System.out.println("3) Cargar Partida");
        System.out.println("4) Gestión partidas guardadas");      // NUEVO
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
            case "4": subMenuGestionGuardadas();   break;  // NUEVO
            case "5": juegoPruebas();              break;
            case "6": System.exit(0);
            default:
                System.out.println("Opción no válida.");
                pause();
        }
    }

    /** NUEVO: menú para listar, cargar o eliminar partidas guardadas 
     * @throws PartidaYaExistenteException 
     * @throws PartidaNoEncontradaException */
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

        // Pregunta acción
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

    // --- resto de submenus sin cambios salvo ajustar retornos cuando convenga ---

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
                        return; // vuelve al principal tras jugar
                    }
                    break;
                case "5": return;
                default:
                    System.out.println("Opción no válida.");
                    pause();
            }
        }
    }

    private static boolean datosCompletos() {
        if (modo.isEmpty() || idDiccionario.isEmpty() || nombre1.isBlank()) return false;
        if (modo.equals(MODO2) && nombre2.isBlank()) return false;
        return true;
    }

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
            // Si todo va bien, seguimos a la subinterfaz de juego
            subMenuPartida();
        }
        catch (DiccionarioNoEncontradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        catch (BolsaNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    
    private static void elegirModo() {
        clearScreen();
        System.out.println("=== SELECCIONAR MODO ===");
        System.out.println("1) " + MODO1);
        System.out.println("2) " + MODO2);
        System.out.print("Opción: ");
        String o = sc.nextLine().trim();
        if (o.equals("1"))      modo = MODO1;
        else if (o.equals("2")) modo = MODO2;
        else {
            System.out.println("Opción no válida.");
            pause();
        }
    }

    private static void elegirDiccionario() {
        clearScreen();
        System.out.println("=== SELECCIONAR DICCIONARIO ===");
        List<String> ids = new ArrayList<>(cd.obtenerIDsDiccionarios());
        if (ids.isEmpty()) {
            System.out.println("No hay diccionarios disponibles.");
            pause();
            return;
        }
        for (int i = 0; i < ids.size(); i++) {
            System.out.printf("%2d) %s%n", i + 1, ids.get(i));
        }
        System.out.print("Elige n.º o ID: ");
        String in = sc.nextLine().trim();
        if (in.matches("\\d+")) {
            int idx = Integer.parseInt(in) - 1;
            if (idx >= 0 && idx < ids.size()) {
                idDiccionario = ids.get(idx);
                return;
            }
        } else if (ids.contains(in)) {
            idDiccionario = in;
            return;
        }
        System.out.println("Selección no válida.");
        pause();
    }

    private static void introducirNombres() {
        clearScreen();
        System.out.print("Nombre Jugador 1: ");
        nombre1 = sc.nextLine().trim();
        if (modo.equals(MODO2)) {
            System.out.print("Nombre Jugador 2: ");
            nombre2 = sc.nextLine().trim();
        }
    }

    private static void subMenuCargarPartida() throws PartidaYaExistenteException, PartidaNoEncontradaException {
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
        System.out.print("Elige n.º o ID (0 = volver): ");
        String in = sc.nextLine().trim();
        if (in.equals("0")) return;
        String id = in.matches("\\d+")
                    ? partidas.get(Integer.parseInt(in) - 1)
                    : in;
        cd.cargarPartida(id);
        subMenuPartida();
    }

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

    private static void pause() {
        System.out.println("\nPulsa ENTER para continuar...");
        sc.nextLine();
    }

    private static void clearScreen() {
        // ANSI
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
