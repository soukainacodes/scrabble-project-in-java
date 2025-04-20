package Presentacion.Drivers;

import java.io.*;
import java.util.*;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;

public class DriverGestionPartidas {
    private static final Scanner sc = new Scanner(System.in);
    private static final CtrlDominio cd = new CtrlDominio();
    private static String modo = "", idioma = "", dificultad = "";

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
        var modos       = List.of("1 Jugador","2 Jugadores");
        var idiomas     = List.of("Catalán","Castellano","Inglés");
        var dificultades= List.of("Fácil","Intermedio","Difícil");

        for (String m : modos) for (String idio : idiomas) for (String dif : dificultades) {
            try {
                System.out.printf("--- Prueba: modo=%s, idioma=%s, dificultad=%s ---%n", m, idio, dif);
                modo = m; idioma = idio; dificultad = dif;

                // 1) Iniciar partida
                cd.iniciarPartida(
                  m.equals("1 Jugador") ? 0 : 1,
                  "PruebaUser","PruebaUser",
                  leerArchivo(obtenerPathDiccionario(idio)),
                  leerArchivo(obtenerPathBolsa(idio)),
                  0L,
                  switch(dif) {
                    case "Fácil"      -> 1;
                    case "Intermedio" -> 2;
                    default           -> 3;
                  }
                );

                // 2) Guardar
                String saveName = "test_"+m.replace(" ","")+"_"+idio+"_"+dif;
                cd.guardarPartida(saveName);
                System.out.println("- Guardada como: " + saveName);

                // 3) Listar
                var partidas = new ArrayList<>(cd.obtenerNombresPartidasGuardadas());
                System.out.println("Partidas disponibles: " + partidas);

                // 4) Cargar y 5) recargar última
                cd.cargarPartida(saveName);
                System.out.println("- Cargada: " + saveName);
                cd.cargarUltimaPartida();
                System.out.println("- Última partida recargada.");

                // 6) Eliminar
                cd.eliminarPartidaGuardada(saveName);
                System.out.println("- Partida eliminada de la persistencia.");

                System.out.println("Prueba completada con éxito.\n");
            } catch (Exception e) {
                errores = true;
                System.err.printf("Error en prueba [%s,%s,%s]: %s%n%n",
                                  modo, idioma, dificultad, e.getMessage());
            }
        }

        System.out.println("---------------------------------------------");
        System.out.println(!errores
            ? "Todas las pruebas se ejecutaron correctamente."
            : "Algunas pruebas fallaron. Revisar errores.");
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
            case "1": subMenuCrearPartida();           break;
            case "2": cd.cargarUltimaPartida(); subMenuPartida(); break;
            case "3": subMenuCargarPartida();          break;
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void subMenuCargarPartida() {
        clearScreen();
        System.out.println("\n===== CARGAR PARTIDA =====");
        var nombres = new ArrayList<>(cd.obtenerNombresPartidasGuardadas());
        if (nombres.isEmpty()) {
            System.out.println("No hay partidas guardadas."); return;
        }
        System.out.println("Partidas disponibles:");
        for (int i=0; i<nombres.size(); i++)
            System.out.printf(" %d. %s%n", i+1, nombres.get(i));

        System.out.print("Elige número o nombre ('0' para volver): ");
        String in = sc.nextLine().trim();
        if (in.equals("0")) return;
        String elegido = in.matches("\\d+")
                       ? nombres.get(Integer.parseInt(in)-1)
                       : in;
        cd.cargarPartida(elegido);
        subMenuPartida();
    }

    private static void subMenuCrearPartida() throws Exception {
        clearScreen();
        System.out.println("\n===== CREAR PARTIDA =====");
        System.out.println("1. Modo:       " + modo);
        System.out.println("2. Idioma:     " + idioma);
        System.out.println("3. Dificultad: " + dificultad);
        System.out.println("4. Finalizar");
        switch (sc.nextLine().trim()) {
            case "1": elegirModo();       break;
            case "2": elegirIdioma();     break;
            case "3": elegirDificultad(); break;
            case "4":
                if (modo.isEmpty()||idioma.isEmpty()||dificultad.isEmpty()) {
                    System.out.println("Completa todos los parámetros.");
                } else {
                    cd.iniciarPartida(
                      modo.equals("1 Jugador") ? 0 : 1,
                      "Alex","",
                      leerArchivo(obtenerPathDiccionario(idioma)),
                      leerArchivo(obtenerPathBolsa(idioma)),
                      0L,
                      switch(dificultad) {
                        case "Fácil"      -> 1;
                        case "Intermedio" -> 2;
                        default           -> 3;
                      }
                    );
                    subMenuPartida();
                }
                break;
            default: System.out.println("Opción no válida.");
        }
    }

    private static void elegirModo() throws Exception {
        clearScreen();
        System.out.println("\n1. 1 Jugador\n2. 2 Jugadores");
        String o = sc.nextLine().trim();
        if (o.equals("1")) modo="1 Jugador";
        else if (o.equals("2")) modo="2 Jugadores";
        else System.out.println("Opción no válida.");
        subMenuCrearPartida();
    }

    private static void elegirIdioma() throws Exception {
        clearScreen();
        System.out.println("\n1. Catalán\n2. Castellano\n3. Inglés");
        String o = sc.nextLine().trim();
        if (o.equals("1")) idioma="Catalán";
        else if (o.equals("2")) idioma="Castellano";
        else if (o.equals("3")) idioma="Inglés";
        else System.out.println("Opción no válida.");
        subMenuCrearPartida();
    }

    private static void elegirDificultad() throws Exception {
        clearScreen();
        System.out.println("\n1. Fácil\n2. Intermedio\n3. Difícil");
        String o = sc.nextLine().trim();
        if (o.equals("1")) dificultad="Fácil";
        else if (o.equals("2")) dificultad="Intermedio";
        else if (o.equals("3")) dificultad="Difícil";
        else System.out.println("Opción no válida.");
        subMenuCrearPartida();
    }

    private static void subMenuPartida() {
        try {
            clearScreen();
            System.out.println("\n--- TABLERO ACTUAL ---");
            imprimirTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            System.out.println(cd.obtenerFichas());
            System.out.println("\n1. Abandonar partida");
            System.out.println("2. Guardar Partida");
            System.out.print("Opción: ");
            String o = sc.nextLine().trim();
            if (o.equals("1")) cd.cargarUltimaPartida();
            else if (o.equals("2")) subMenuGuardar();
            else System.out.println("Opción no válida.");
        } catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
        }
    }

    private static void subMenuGuardar() {
        System.out.print("Nombre para guardar: ");
        String n = sc.nextLine().trim();
        if (!n.isEmpty()) {
            cd.guardarPartida(n);
            System.out.println("Guardada como: "+n);
        } else {
            System.out.println("Nombre inválido.");
        }
    }

    private static void imprimirTablero() {
        int N = cd.getTableroDimension();
        System.out.print("    ");
        for (int j=0; j<N; j++) System.out.printf("%4d", j);
        System.out.println();
        for (int i=0; i<N; i++) {
            System.out.printf("%2d: ", i);
            for (int j=0; j<N; j++) {
                String letra = cd.getLetraCelda(i,j);
                String bono  = cd.getBonusCelda(i,j);
                String disp  = letra != null ? letra : bono;
                System.out.printf("[%2s]", disp);
            }
            System.out.println();
        }
    }

    // Rutas de recursos
    private static String obtenerPathDiccionario(String idio) {
        return "./FONTS/src/main/Recursos/Idiomas/"
             + switch(idio) {
                 case "Catalán"    -> "Catalan";
                 case "Castellano" -> "Castellano";
                 case "Inglés"     -> "Ingles";
                 default            -> "";
               }
             + "/"
             + switch(idio) {
                 case "Catalán"    -> "catalan.txt";
                 case "Castellano" -> "castellano.txt";
                 case "Inglés"     -> "english.txt";
                 default           -> "";
               };
    }
    private static String obtenerPathBolsa(String idio) {
        return "./FONTS/src/main/Recursos/Idiomas/"
             + switch(idio) {
                 case "Catalán"    -> "Catalan";
                 case "Castellano" -> "Castellano";
                 case "Inglés"     -> "Ingles";
                 default            -> "";
               }
             + "/"
             + switch(idio) {
                 case "Catalán"    -> "letrasCAT.txt";
                 case "Castellano" -> "letrasCAST.txt";
                 case "Inglés"     -> "letrasENG.txt";
                 default           -> "";
               };
    }

    private static List<String> leerArchivo(String ruta) throws IOException {
        var lineas = new ArrayList<String>();
        try (var br = new BufferedReader(new FileReader(ruta))) {
            String l;
            while ((l = br.readLine()) != null) {
                l = l.trim();
                if (!l.isEmpty() && !l.startsWith("#")) lineas.add(l);
            }
        }
        return lineas;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J"); System.out.flush();
    }
}
