package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import Dominio.CtrlDominio;
import Dominio.Excepciones.BolsaNoEncontradaException;
import Dominio.Excepciones.BolsaYaExistenteException;
import Dominio.Excepciones.DiccionarioNoEncontradoException;
import Dominio.Excepciones.DiccionarioYaExistenteException;
import Dominio.Excepciones.NoHayPartidaGuardadaException;
import Dominio.Excepciones.PartidaNoEncontradaException;
import Dominio.Excepciones.PasswordInvalidaException;
import Dominio.Excepciones.RankingVacioException;
import Dominio.Excepciones.UsuarioNoEncontradoException;
import Dominio.Excepciones.UsuarioYaRegistradoException;

/**
 * Driver principal para gestionar y probar todas las funcionalidades del juego de Scrabble.
 * Permite gestionar usuarios, diccionarios, bolsas y partidas, así como interactuar con el tablero.
 */
public class DriverScrabble {
    private static final Scanner sc = new Scanner(System.in);
    private static final CtrlDominio ctrl = new CtrlDominio();

    /**
     * Constructor por defecto para la clase DriverScrabble.
     * Inicializa los valores necesarios para gestionar el juego de Scrabble.
     */
    public DriverScrabble() {
        // Constructor vacío
    }

    /**
     * Punto de entrada principal del driver.
     * Muestra el menú principal o de usuario según el estado de la sesión.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) throws UsuarioYaRegistradoException, UsuarioNoEncontradoException,PasswordInvalidaException {
        while (true) {
            try {
                if (!ctrl.haySesion()) menuPrincipal();
                else menuUsuario();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Muestra el menú principal para usuarios no autenticados.
     * Permite iniciar sesión, registrarse o salir.
     * @throws IOException 
     */
    private static void menuPrincipal() throws UsuarioNoEncontradoException,PasswordInvalidaException, IOException {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
        String opt = sc.nextLine().trim();
        switch (opt) {
            case "1": iniciarSesion();     break;
            case "2": registrarse();       break;
            case "3": System.exit(0);      break;
            default:  System.out.println("Opción no válida.");
        }
    }

    /**
     * Muestra el menú principal para usuarios autenticados.
     * Permite gestionar cuenta, diccionarios, partidas o cerrar sesión.
     *
     * @throws UsuarioNoEncontradoException si no se encuentra el usuario actual.
     * @throws IOException 
     */
    private static void menuUsuario() throws UsuarioYaRegistradoException, UsuarioNoEncontradoException,PasswordInvalidaException, IOException {
        System.out.println("\n===== MENÚ USUARIO =====");
        System.out.println("1. Gestión de Cuenta");
        System.out.println("2. Gestión de Diccionarios y Bolsas");
        System.out.println("3. Gestión de Partidas");
        System.out.println("4. Cerrar Sesión");
        System.out.println("5. Salir");
        System.out.print("Opción: ");
        String opt = sc.nextLine().trim();
        switch (opt) {
            case "1": subMenuCuenta(); break;
            case "2": subMenuDiccionarios(); break;
            case "3": subMenuPartidas(); break;
            case "4": ctrl.cerrarSesion(); System.out.println("Sesión cerrada."); break;
            case "5": System.exit(0); break;
            default: System.out.println("Opción no válida.");
        }
    }

    /**
     * Muestra el submenú de gestión de la cuenta del usuario actual.
     * Permite cambiar contraseña, ver ranking, eliminar perfil o volver.
     *
     * @throws UsuarioNoEncontradoException si no se encuentra el usuario actual.
     * @throws IOException 
     */
    private static void subMenuCuenta() throws UsuarioNoEncontradoException, IOException {
        while (true) {
            String user = ctrl.getUsuarioActual();
            int puntos = ctrl.getPuntosActual();
            int pos = ctrl.getPosicionActual();

            System.out.println("\n--- TU CUENTA ---");
            System.out.println("Usuario:  " + user);
            System.out.println("Puntos:   " + puntos);
            System.out.println(pos > 0 ? "Posición: " + pos : "Posición: Sin clasificar");
            System.out.println("1. Cambiar Contraseña");
            System.out.println("2. Ver Ranking");
            System.out.println("3. Eliminar Perfil");
            System.out.println("4. Volver");
            System.out.print("Opción: ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1": cambiarPassword(); break;
                case "2":
                    try {
                        verRanking();
                    } catch (RankingVacioException e) {
                        System.out.println("No hay puntuaciones registradas.");
                    }
                    break;
                case "3": eliminarPerfil(); return;
                case "4": return;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Cambia la contraseña del usuario actual.
     *
     * @throws UsuarioNoEncontradoException si no se encuentra el usuario actual.
     */
    private static void cambiarPassword() throws UsuarioNoEncontradoException {
        System.out.print("Contraseña actual: ");
        String ant = sc.nextLine().trim();
        System.out.print("Nueva contraseña: ");
        String n1 = sc.nextLine().trim();
        System.out.print("Repita la nueva contraseña: ");
        String n2 = sc.nextLine().trim();
        if (!n1.equals(n2)) {
            System.out.println("Error: las contraseñas no coinciden.");
            return;
        }
        try {
            ctrl.cambiarPassword(ant, n1);
            System.out.println("Contraseña cambiada correctamente.");
        } catch (PasswordInvalidaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Muestra el ranking global de jugadores.
     *
     * @throws RankingVacioException si no hay jugadores en el ranking.
     * @throws IOException 
     */
    private static void verRanking() throws RankingVacioException, IOException {
        System.out.println("\n--- RANKING GLOBAL ---");
        var lista = ctrl.obtenerRanking();
        for (int i = 0; i < lista.size(); i++) {
            var e = lista.get(i);
            System.out.printf("%2d. %-15s %4d%n", i + 1, e.getKey(), e.getValue());
        }
    }

    /**
     * Elimina el perfil del usuario actual tras confirmación.
     * @throws IOException 
     */
    private static void eliminarPerfil() throws IOException {
        System.out.print("¿Seguro que desea eliminar su cuenta? (s/n): ");
        String r = sc.nextLine().trim().toLowerCase();
        if (!r.equals("s") && !r.equals("si")) {
            System.out.println("Eliminación cancelada.");
            return;
        }
        System.out.print("Confirme su contraseña: ");
        String pass = sc.nextLine().trim();
        try {
            ctrl.eliminarUsuario(pass);
            System.out.println("Perfil eliminado. Adiós.");
        } catch (PasswordInvalidaException | UsuarioNoEncontradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Inicia sesión con un usuario existente.
     * @throws IOException 
     */
    private static void iniciarSesion() throws IOException {
        System.out.print("Usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        try {
            ctrl.iniciarSesion(u, p);
            int puntos = ctrl.getPuntosActual();
            int pos = ctrl.getPosicionActual();
            System.out.println("\n========================================");
            System.out.printf("   ¡Bienvenido, %s!%n", u);
            System.out.printf("   Puntos: %d    ", puntos);
            System.out.println(pos > 0 ? "Posición: " + pos : "Posición: Sin Clasificar");
            System.out.println("========================================\n");
        } catch (UsuarioNoEncontradoException | PasswordInvalidaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    private static void registrarse() throws UsuarioNoEncontradoException,PasswordInvalidaException {
        System.out.print("Nuevo usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Nueva contraseña: ");
        String p = sc.nextLine().trim();
        try {
            ctrl.registrarJugador(u, p);
            System.out.println("Registrado '" + u + "'.");
            //ctrl.iniciarSesion(u, p);
            System.out.println("Sesión iniciada como '" + u + "'.");
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (Exception ignored) {
        }
    }

    /**
     * Muestra el submenú de gestión de diccionarios y bolsas.
     * Permite consultar, añadir o eliminar recursos.
     */
    private static void subMenuDiccionarios() {
        while (true) {
            System.out.println("\n=== GESTIÓN DICCIONARIOS Y BOLSAS ===");
            System.out.println("1. Consultar diccionarios y bolsas");
            System.out.println("2. Añadir diccionario+bolsa");
            System.out.println("3. Eliminar diccionario+bolsa");
            System.out.println("4. Volver");
            System.out.print("Opción: ");
            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1": consultarRecursos(); break;
                    case "2": addResourceMenu(); break;
                    case "3": deleteResource(); break;
                    case "4": return;
                    default: System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    /**
     * Consulta y muestra los diccionarios y bolsas disponibles.
     */
    private static void consultarRecursos() {
        Set<String> dicIDs = ctrl.obtenerIDsDiccionarios();
        Set<String> bolIDs = ctrl.obtenerIDsBolsas();
        Set<String> all    = new TreeSet<>();
        all.addAll(dicIDs); all.addAll(bolIDs);
        if (all.isEmpty()) {
            System.out.println("No hay entradas registradas.");
            return;
        }
        System.out.println("\nID     | Diccionario                | Bolsa");
        System.out.println("-------+----------------------------+----------------");
        for (String id : all) {
            String d = dicIDs.contains(id) ? id + "_diccionario.txt" : "(n/a)";
            String b = bolIDs.contains(id) ? id + "_bolsa.txt"       : "(n/a)";
            System.out.printf("%-6s | %-26s | %s%n", id, d, b);
        }
    }

    /**
     * Muestra el submenú para añadir recursos (diccionario+bolsa).
     */
    private static void addResourceMenu() {
        while (true) {
            System.out.println("\n--- AÑADIR RECURSO ---");
            System.out.println("1. Desde directorio existente");
            System.out.println("2. Desde teclado");
            System.out.println("3. Volver");
            System.out.print("Opción: ");
            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1": addFromDirectory();  break;
                    case "2": addFromKeyboard();   break;
                    case "3": return;
                    default:   System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    /**
     * Añade un recurso desde un directorio existente.
     */
    private static void addFromDirectory() {
        System.out.print("Ruta al directorio (0 para volver): ");
        String dirPath = sc.nextLine().trim();
        if (dirPath.equals("0")) return;
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            System.out.println("No es un directorio válido.");
            return;
        }
        String id = dir.getName();
        File fDic = new File(dir, id + "_diccionario.txt");
        File fBol = new File(dir, id + "_bolsa.txt");
        if (!fDic.exists() || !fBol.exists()) {
            System.out.println("Faltan archivos en el directorio.");
            return;
        }
        try {
            List<String> palabras = leerArchivoTexto(fDic.getAbsolutePath());
            ctrl.crearDiccionario(id, palabras);
            Map<String,int[]> bolsa = leerArchivoBolsa(fBol.getAbsolutePath());
            ctrl.crearBolsa(id, bolsa);
            System.out.println("Recurso '" + id + "' añadido desde directorio.");
        } catch (IOException e) {
            System.out.println("ERROR I/O: " + e.getMessage());
        } catch (DiccionarioYaExistenteException | BolsaYaExistenteException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Añade un recurso desde el teclado.
     */
    private static void addFromKeyboard() {
        System.out.print("Nuevo ID (0 para volver): ");
        String id = sc.nextLine().trim();
        if (id.equals("0")) return;
        System.out.println("Introduce palabras (una por línea, MAYÚSCULAS), línea vacía para terminar:");
        List<String> palabras = new ArrayList<>();
        while (true) {
            String w = sc.nextLine().trim();
            if (w.isEmpty()) break;
            palabras.add(w);
        }
        System.out.println("Introduce líneas de bolsa: \"Letra repeticiones puntos\" (vacío para terminar):");
        Map<String,int[]> bolsa = new LinkedHashMap<>();
        while (true) {
            String l = sc.nextLine().trim();
            if (l.isEmpty()) break;
            String[] t = l.split("\\s+");
            int rep = Integer.parseInt(t[1]);
            int pts = Integer.parseInt(t[2]);
            bolsa.put(t[0], new int[]{rep, pts});
        }
        try {
            ctrl.crearDiccionario(id, palabras);
            ctrl.crearBolsa(id, bolsa);
            System.out.println("Recurso '" + id + "' creado correctamente.");
        } catch (DiccionarioYaExistenteException | BolsaYaExistenteException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR I/O: " + e.getMessage());
        }
    }

    /**
     * Elimina un recurso (diccionario+bolsa) seleccionado por el usuario.
     */
    private static void deleteResource() {
        System.out.print("ID a eliminar (0 para volver): ");
        String id = sc.nextLine().trim();
        if (id.equals("0")) return;
        try {
            ctrl.eliminarIdiomaCompleto(id);
            System.out.println("Recurso '" + id + "' eliminado.");
        } catch (DiccionarioNoEncontradoException | BolsaNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR I/O: " + e.getMessage());
        }
    }

    /**
     * Lee un archivo de texto y devuelve una lista de líneas.
     *
     * @param ruta la ruta del archivo de texto.
     * @return una lista de líneas del archivo.
     * @throws IOException si ocurre un error de E/S.
     */
    private static List<String> leerArchivoTexto(String ruta) throws IOException {
        List<String> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) lista.add(linea);
            }
        }
        return lista;
    }

    /**
     * Lee un archivo de bolsa y devuelve un mapa de letras y sus propiedades.
     *
     * @param ruta la ruta del archivo de bolsa.
     * @return un mapa de letras y sus propiedades.
     * @throws IOException si ocurre un error de E/S.
     */
    private static Map<String,int[]> leerArchivoBolsa(String ruta) throws IOException {
        Map<String,int[]> mapa = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] t = linea.split("\\s+");
                mapa.put(t[0], new int[]{Integer.parseInt(t[1]), Integer.parseInt(t[2])});
            }
        }
        return mapa;
    }

    /**
     * Muestra el submenú de gestión de partidas.
     * Permite crear, cargar o ver ranking de partidas.
     * @throws IOException 
     */
    private static void subMenuPartidas() throws UsuarioYaRegistradoException, UsuarioNoEncontradoException,PasswordInvalidaException, IOException{
        while (true) {
            System.out.println("\n=== GESTIÓN DE PARTIDAS ===");
            System.out.println("1. Crear nueva partida");
            System.out.println("2. Cargar última partida");
            System.out.println("3. Cargar partida guardada");
            System.out.println("4. Ver ranking");
            System.out.println("5. Volver");
            System.out.print("Opción: ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1": crearPartida(); break;
                case "2": cargarUltimaPartida(); break;
                case "3": cargarPartidaGuardada(); break;
                case "4":
                    try {
                        verRanking();
                    } catch (RankingVacioException e) {
                        System.out.println("No hay puntuaciones registradas.");
                    }
                    break;
                case "5": return;
                default:   System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Crea una nueva partida solicitando los datos necesarios al usuario.
     * @throws IOException 
     */
    private static void crearPartida() throws UsuarioYaRegistradoException, UsuarioNoEncontradoException,PasswordInvalidaException, IOException {
        try {
            System.out.println("\n--- Crear nueva partida ---");
            System.out.println("Selecciona modo:");
            System.out.println("1) 1 Jugador");
            System.out.println("2) 2 Jugadores");
            System.out.print("Opción: ");
            String mopt = sc.nextLine().trim();
            int modo;
            if      (mopt.equals("1")) modo = 0;
            else if (mopt.equals("2")) modo = 1;
            else { System.out.println("Opción no válida."); return; }

            List<String> ids = new ArrayList<>(ctrl.obtenerIDsDiccionarios());
            if (ids.isEmpty()) { System.out.println("No hay diccionarios disponibles."); return; }
            System.out.println("Selecciona diccionario:");
            for (int i = 0; i < ids.size(); i++) {
                System.out.printf("%2d) %s%n", i+1, ids.get(i));
            }
            System.out.print("Opción: ");
            String dopt = sc.nextLine().trim();
            String idDic;
            if (dopt.matches("\\d+")) {
                int idx = Integer.parseInt(dopt) - 1;
                if (idx >= 0 && idx < ids.size()) idDic = ids.get(idx);
                else { System.out.println("Selección no válida."); return; }
            } else if (ids.contains(dopt)) idDic = dopt;
            else { System.out.println("Selección no válida."); return; }

            String jugador1 = ctrl.getUsuarioActual();
            String jugador2 = "";
            if (modo == 1) {
                System.out.print("1. Iniciar sesion: \n");
                System.out.print("2. Registrar jugador: \n");

                String opcion = sc.nextLine().trim();
                System.out.print("Usuario: \n");
                jugador2= sc.nextLine().trim();
                if (jugador2.isEmpty()) { System.out.println("Nombre no válido."); return; }
                if (jugador2.equals(jugador1)) { System.out.println("No te puedes añadir a ti mismo."); return; }
                System.out.print("Contraseña: \n");
                String password2 = sc.nextLine().trim();
               if("1".equals(opcion)){
                    ctrl.iniciarSesion(jugador2, password2);
              } else if("2".equals(opcion)) {
                 
                    ctrl.registrarJugador(jugador2, password2);
                    System.out.println("Jugador '" + jugador2 + "' registrado.");
               
               }
               else { System.out.println("Opción no válida."); return; }
               
            }

            long seed = new Random().nextLong();
            ctrl.iniciarPartida(modo, jugador2, idDic, seed, false);
            menuJuego();
        } catch (DiccionarioNoEncontradoException | BolsaNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Carga la última partida guardada.
     */
    private static void cargarUltimaPartida() {
        try {
            ctrl.cargarUltimaPartida();
            System.out.println("Partida cargada.");
            menuJuego();
        } catch (NoHayPartidaGuardadaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Carga una partida guardada seleccionada por el usuario.
     */
    private static void cargarPartidaGuardada() {
        List<String> partidas = new ArrayList<>(ctrl.obtenerNombresPartidasGuardadas());
        if (partidas.isEmpty()) { System.out.println("No hay partidas guardadas."); return; }
        System.out.println("Partidas guardadas:");
        for (int i = 0; i < partidas.size(); i++) {
            System.out.printf("%2d) %s%n", i+1, partidas.get(i));
        }
        System.out.print("Elige número o ID (0 para volver): ");
        String inopt = sc.nextLine().trim();
        if (inopt.equals("0")) return;
        String id;
        if (inopt.matches("\\d+")) {
            int idx = Integer.parseInt(inopt) - 1;
            if (idx >= 0 && idx < partidas.size()) id = partidas.get(idx);
            else { System.out.println("Selección no válida."); return; }
        } else if (partidas.contains(inopt)) id = inopt;
        else { System.out.println("Selección no válida."); return; }

        try {
            ctrl.cargarPartida(id);
            System.out.println("Partida '" + id + "' cargada.");
            menuJuego();
        } catch (PartidaNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Muestra el tablero actual de la partida.
     */
    private static void mostrarTablero() {
        int N = ctrl.getTableroDimension();
        System.out.print("    ");
        for (int j = 0; j < N; j++) System.out.printf("%4d", j);
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < N; j++) {
                String letra = ctrl.getLetraCelda(i, j);
                String bono  = ctrl.getBonusCelda(i, j);
                String disp  = letra != null ? letra : bono;
                System.out.printf("[%2s]", disp);
            }
            System.out.println();
        }
    }

    /**
     * Muestra el menú de juego y permite realizar acciones en la partida.
     */
    private static void menuJuego() {
        while (true) {
            System.out.println("\n--- ESTADO DE LA PARTIDA ---");
            System.out.println(" Puntos Jugador 1: " + ctrl.getPuntosJugador1());
            System.out.println(" Puntos Jugador 2: " + ctrl.getPuntosJugador2());
            mostrarTablero();
            System.out.println("Fichas: " + ctrl.obtenerFichas());
            System.out.println("\n1. Añadir ficha");
            System.out.println("2. Quitar ficha");
            System.out.println("3. Cambiar fichas");
            System.out.println("4. Pasar turno");
            System.out.println("5. Finalizar turno");
            System.out.println("6. Guardar partida");
            System.out.println("7. Abandonar partida");
            System.out.print("Opción: ");
            String opt = sc.nextLine().trim();
            try {
                int fin;
                switch (opt) {
                    case "1": subMenuAnadir();   break;
                    case "2": subMenuQuitar();   break;
                    case "3": subMenuCambiar();  break;
                    case "4":
                        fin = ctrl.jugarScrabble(3, "");
                        if (fin != 0) { System.out.println("Partida finalizada."); return; }
                        System.out.println("Turno de IA...");
                        fin = ctrl.jugarScrabble(7, "");
                        if (fin != 0) { System.out.println("Partida finalizada."); return; }
                        break;
                    case "5":
                        fin = ctrl.jugarScrabble(4, "");
                        if (fin != 0) { System.out.println("Partida finalizada."); return; }
                        System.out.println("Turno de IA...");
                        fin = ctrl.jugarScrabble(7, "");
                        if (fin != 0) { System.out.println("Partida finalizada."); return; }
                        break;
                    case "6":
                        System.out.print("Nombre para guardar: ");
                        String save = sc.nextLine().trim();
                        if (!save.isEmpty()) {
                            ctrl.guardarPartida(save);
                            System.out.println("Partida guardada como '" + save + "'.");
                        } else {
                            System.out.println("Nombre inválido.");
                        }
                        break;
                    case "7":
                        System.out.print("¿Seguro que deseas abandonar la partida? (s/n): ");
                        String r = sc.nextLine().trim().toLowerCase();
                        if (r.equals("s") || r.equals("si")) {
                            ctrl.jugarScrabble(6, "");
                            System.out.println("Has abandonado la partida.");
                            return;
                        }
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    /**
     * Muestra el submenú para añadir fichas al tablero.
     */
    private static void subMenuAnadir() {
        System.out.print("Inserta ficha y posición (formato: LETRA fila columna), o '0' para volver: ");
        String input = sc.nextLine().trim();
        if (!input.equals("0")) {
            try { ctrl.jugarScrabble(1, input); }
            catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        }
    }

    /**
     * Muestra el submenú para quitar fichas del tablero.
     */
    private static void subMenuQuitar() {
        System.out.print("Inserta posición a quitar (formato: fila columna), o '0' para volver: ");
        String input = sc.nextLine().trim();
        if (!input.equals("0")) {
            try { ctrl.jugarScrabble(2, input); }
            catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        }
    }

    /**
     * Muestra el submenú para cambiar fichas.
     */
    private static void subMenuCambiar() {
        System.out.print("Inserta fichas a cambiar (letras separadas por espacio), o '0' para volver: ");
        String input = sc.nextLine().trim();
        if (!input.equals("0")) {
            try { ctrl.jugarScrabble(5, input); }
            catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        }
    }
}
