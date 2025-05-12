package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;

/**
 * Driver de consola para la aplicación Scrabble.
 * Usa exclusivamente el controlador CtrlDominio para toda la lógica.
 */
public class Driver {
    private static final Scanner sc = new Scanner(System.in);
    private static final CtrlDominio ctrl = new CtrlDominio();

    public static void main(String[] args) {
        while (true) {
            try {
                if (!ctrl.haySesion())
                    menuPrincipal();
                else
                    menuUsuario();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void menuPrincipal() throws UsuarioNoEncontradoException, PasswordInvalidaException, IOException {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1":
                login();
                break;
            case "2":
                registro();
                break;
            case "3":
                System.exit(0);
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void menuUsuario() throws IOException, PartidaNoEncontradaException, UsuarioYaRegistradoException,
            UsuarioNoEncontradoException, PasswordInvalidaException, DiccionarioNoEncontradoException,
            BolsaNoEncontradaException {
        System.out.println("\n===== MENÚ USUARIO =====");
        System.out.println("1. Gestión de Cuenta");
        System.out.println("2. Gestión de Diccionarios y Bolsas");
        System.out.println("3. Gestión de Partidas");
        System.out.println("4. Cerrar Sesión");
        System.out.println("5. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1":
                subMenuCuenta();
                break;
            case "2":
                subMenuRecursos();
                break;
            case "3":
                subMenuPartidas();
                break;
            case "4":
                ctrl.cerrarSesion();
                System.out.println("Sesión cerrada.");
                break;
            case "5":
                System.exit(0);
            default:
                System.out.println("Opción no válida.");
        }
    }

    // --- Cuenta ----------------------------------------------------------------
    private static void subMenuCuenta() throws UsuarioNoEncontradoException, IOException {
        while (true) {
            String u = ctrl.getUsuarioActual();
            int pts = ctrl.getPuntosActual();
            int pos;
            try {
                pos = ctrl.obtenerPosicion(u);
            } catch (Exception e) {
                pos = 0;
            }
            System.out.println("\n--- TU CUENTA ---");
            System.out.println("Usuario: " + u);
            System.out.println("Puntos: " + pts);
            System.out.println(pos > 0 ? "Posición: " + pos : "Posición: Sin clasificar");
            
            System.out.println("1. Cambiar contraseña");
            System.out.println("2. Ver ranking");
            System.out.println("3. Eliminar perfil");
            System.out.println("4. Volver");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1":
                    cambiarPassword();
                    break;
                case "2":
                    try {
                        verRanking();
                    } catch (RankingVacioException e) {
                        System.out.println("Ranking vacío.");
                    }
                    break;
                case "3":
                    borrarCuenta();
                    return;
                case "4":
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void cambiarPassword() throws UsuarioNoEncontradoException {
        System.out.print("Antigua: ");
        String oldp = sc.nextLine().trim();
        System.out.print("Nueva:   ");
        String newp = sc.nextLine().trim();
        ctrl.cambiarPassword(oldp, newp);
        System.out.println("Contraseña actualizada.");
    }

    private static void verRanking() throws RankingVacioException, IOException {
        System.out.println("\n--- RANKING GLOBAL ---");
        var list = ctrl.obtenerRanking();
        for (int i = 0; i < list.size(); i++) {
            var e = list.get(i);
            System.out.printf("%2d. %-15s %4d%n", i + 1, e.getKey(), e.getValue());
        }
    }

    private static void borrarCuenta() throws IOException {
        System.out.print("Confirmar contraseña para eliminar (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s"))
            return;
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        try {
            ctrl.eliminarUsuario(p);
            System.out.println("Cuenta eliminada.");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void login() throws IOException {
        System.out.print("Usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        try {
            ctrl.iniciarSesion(u, p);
            System.out.println("Bienvenido, " + u + "!");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void registro() {
        System.out.print("Nuevo usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        try {
            ctrl.registrarJugador(u, p);
            System.out.println("Usuario '" + u + "' registrado.");
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O ERROR: " + e.getMessage());
        }
    }

    // --- Recursos --------------------------------------------------------------
    private static void subMenuRecursos() {
        while (true) {
            System.out.println("\n=== RECURSOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Añadir");
            System.out.println("3. Eliminar");
            System.out.println("4. Volver");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1":
                    listarRecursos();
                    break;
                case "2":
                    addRecursos();
                    break;
                case "3":
                    delRecursos();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void listarRecursos() {
        List<String> ids = ctrl.obtenerRecursos();
        if (ids.isEmpty()) {
            System.out.println("Ningún recurso.");
            return;
        }
        System.out.println("Recursos:");
        for (String id : ids)
            System.out.println(" - " + id);
    }

    private static void addRecursos() {
        System.out.print("ID recurso: ");
        String id = sc.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("ERROR: El ID del recurso no puede estar vacío.");
            return;
        }

        try {
            // Solicitar el diccionario
            System.out.println("Para el diccionario:");
            System.out.println("1. Introducir ruta de fichero");
            System.out.println("2. Introducir palabras manualmente");
            System.out.print("Opción: ");
            String opcionDic = sc.nextLine().trim();

            List<String> palabras = new ArrayList<>();
            if (opcionDic.equals("1")) {
                System.out.print("Fichero diccionario (ruta): ");
                String rutaDiccionario = sc.nextLine().trim();
                palabras = leeTexto(rutaDiccionario);
            } else if (opcionDic.equals("2")) {
                System.out.println("Introduce palabras (línea vacía para terminar):");
                String palabra;
                while (!(palabra = sc.nextLine().trim()).isEmpty()) {
                    palabras.add(palabra);
                }
            } else {
                System.out.println("Opción no válida, usando fichero.");
                System.out.print("Fichero diccionario (ruta): ");
                String rutaDiccionario = sc.nextLine().trim();
                palabras = leeTexto(rutaDiccionario);
            }

            // Solicitar la bolsa
            System.out.println("Para la bolsa:");
            System.out.println("1. Introducir ruta de fichero");
            System.out.println("2. Introducir valores manualmente");
            System.out.print("Opción: ");
            String opcionBolsa = sc.nextLine().trim();

            Map<String, int[]> bolsa = new LinkedHashMap<>();
            if (opcionBolsa.equals("1")) {
                System.out.print("Fichero bolsa (ruta): ");
                String rutaBolsa = sc.nextLine().trim();
                bolsa = leeBolsa(rutaBolsa);
            } else if (opcionBolsa.equals("2")) {
                System.out.println("Introduce fichas en formato 'letra cantidad valor' (línea vacía para terminar):");
                String linea;
                while (!(linea = sc.nextLine().trim()).isEmpty()) {
                    String[] partes = linea.split("\\s+");
                    if (partes.length >= 3) {
                        try {
                            bolsa.put(partes[0], new int[] {
                                    Integer.parseInt(partes[1]),
                                    Integer.parseInt(partes[2])
                            });
                        } catch (NumberFormatException e) {
                            System.out.println("Formato incorrecto. Use: letra cantidad valor");
                        }
                    } else {
                        System.out.println("Formato incorrecto. Use: letra cantidad valor");
                    }
                }
            } else {
                System.out.println("Opción no válida, usando fichero.");
                System.out.print("Fichero bolsa (ruta): ");
                String rutaBolsa = sc.nextLine().trim();
                bolsa = leeBolsa(rutaBolsa);
            }

            // Crear el recurso
            ctrl.crearRecurso(id, palabras, bolsa);
            System.out.println("Recurso '" + id + "' creado.");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void delRecursos() {
        System.out.print("ID a eliminar: ");
        String id = sc.nextLine().trim();
        try {
            ctrl.eliminarRecurso(id);
            System.out.println("Recurso '" + id + "' eliminado.");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static List<String> leeTexto(String ruta) throws IOException {
        var res = new ArrayList<String>();
        try (var br = new BufferedReader(new FileReader(ruta))) {
            String l;
            while ((l = br.readLine()) != null)
                if (!l.isBlank())
                    res.add(l.trim());
        }
        return res;
    }

    private static Map<String, int[]> leeBolsa(String ruta) throws IOException {
        var map = new LinkedHashMap<String, int[]>();
        try (var br = new BufferedReader(new FileReader(ruta))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.isBlank())
                    continue;
                var t = l.trim().split("\\s+");
                map.put(t[0], new int[] { Integer.parseInt(t[1]), Integer.parseInt(t[2]) });
            }
        }
        return map;
    }

    // --- Partidas --------------------------------------------------------------

    private static void subMenuPartidas() throws UsuarioYaRegistradoException, UsuarioNoEncontradoException,
            PasswordInvalidaException, IOException, DiccionarioNoEncontradoException, BolsaNoEncontradaException {
        while (true) {
            System.out.println("\n=== PARTIDAS ===");
            System.out.println("1. Crear");
            System.out.println("2. Cargar última");
            System.out.println("3. Cargar guardada");
            System.out.println("4. Eliminar guardada");
            System.out.println("5. Volver");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1":
                    crearPartida();
                    break;
                case "2":
                    cargarUltima();
                    break;
                case "3":
                    cargarGuardada();
                    break;
                case "4":
                    eliminarGuardada();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void crearPartida() throws UsuarioYaRegistradoException, UsuarioNoEncontradoException,
            PasswordInvalidaException, IOException, DiccionarioNoEncontradoException, BolsaNoEncontradaException {
        System.out.print("ID partida: ");
        String pid = sc.nextLine().trim();
        System.out.print("ID diccionario: ");
        String did = sc.nextLine().trim();
        System.out.print("Modo (0=1 jugador, 1=2 jugadores): ");
        int modo = Integer.parseInt(sc.nextLine().trim());
        String otro = "";
        if (modo == 1) {
            System.out.print("Usuario 2: ");
            otro = sc.nextLine().trim();
            System.out.print("Password 2: ");
            String p2 = sc.nextLine().trim();
            try {
                ctrl.iniciarSesion(otro, p2);
            } catch (Exception e) {
                ctrl.registrarJugador(otro, p2);
            }
        }
        long seed = new Random().nextLong();
        ctrl.iniciarPartida(pid, otro, did, seed, false);
        menuJuego();
    }

    private static void cargarUltima() {
        try {
            ctrl.cargarUltimaPartida();
            System.out.println("Última partida cargada.");
            menuJuego();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void cargarGuardada() {
        List<String> ps = ctrl.obtenerNombresPartidasGuardadas();

        if (ps.isEmpty()) {
            System.out.println("Ninguna guardada.");
            return;
        } else {
            System.out.println("Guardadas:");
            for (String id : ps)
                System.out.println(" - " + id);
        }

        System.out.print("Elige ID: ");
        String id = sc.nextLine().trim();
        try {
            ctrl.cargarPartida(id);
            System.out.println("Partida '" + id + "' cargada.");
            menuJuego();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void eliminarGuardada() {
        List<String> ps = ctrl.obtenerNombresPartidasGuardadas();
        if (ps.isEmpty()) {
            System.out.println("Ninguna guardada.");
            return;
        }
        System.out.print("ID a eliminar: ");
        String id = sc.nextLine().trim();
        try {
            ctrl.eliminarPartidaGuardada(id);
            System.out.println("Guardada '" + id + "' eliminada.");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void menuJuego() {
        while (true) {
            System.out.println("\n--- EN JUEGO ---");
            System.out.println("P1: " + ctrl.getPuntosJugador1() + "  P2: " + ctrl.getPuntosJugador2());
            mostrarTablero();
            System.out.println("Fichas: " + ctrl.obtenerFichas());
            System.out.println("1.Añadir 2.Quitar 3.Cambiar 4.Pasar 5.Finir 6.Guardar 7.Abandonar");
            System.out.print("Opción: ");
            String opt = sc.nextLine().trim();
            try {
                int fin;
                switch (opt) {
                    case "1":
                        acc(1);
                        break;
                    case "2":
                        acc(2);
                        break;
                    case "3":
                        acc(5);
                        break;
                    case "4":
                        fin = ctrl.jugarScrabble(3, "");
                        if (fin != 0)
                            return;
                        //ctrl.jugarScrabble(7, "");
                        break;
                    case "5":
                        fin = ctrl.jugarScrabble(4, "");
                        if (fin != 0)
                            return;
                      //  ctrl.jugarScrabble(7, "");
                        break;
                    case "6":
                        System.out.print("Guardar ID: ");
                        ctrl.salirPartida(sc.nextLine().trim());
                        break;
                    case "7":
                        ctrl.jugarScrabble(6, "");
                        return;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void acc(int modo) {
        System.out.print("Parámetro: ");
        try {
            ctrl.jugarScrabble(modo, sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void mostrarTablero() {
        int N = ctrl.getTableroDimension();
        System.out.print("    ");
        for (int j = 0; j < N; j++)
            System.out.printf("%4d", j);
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < N; j++) {
                String l = ctrl.getLetraCelda(i, j);
                String b = ctrl.getBonusCelda(i, j);
                System.out.printf("[%2s]", l != null ? l : b);
            }
            System.out.println();
        }
    }
}
