package Presentacion.Drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Dominio.CtrlDominio;
import Dominio.Modelos.*;
import Dominio.*;

public class DriverPartidaAlgoritmo {
    
    // Scanner para entrada por teclado
    private static Scanner in = new Scanner(System.in);
    // Controlador de Dominio, que manejará la lógica de la partida
    private static CtrlDominio cd = new CtrlDominio();

    /**
     * Limpia la pantalla usando secuencias ANSI.
     * Nota: Esta técnica funciona en terminales compatibles (Unix, Linux, macOS y algunas en Windows).
     */
    private static void clearScreen() {
        // Secuencia ANSI para limpiar pantalla y mover el cursor a la esquina superior izquierda
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    /**
     * Muestra las fichas actuales disponibles.
     */
    private static void mostrarFichas() {
        System.out.println("Fichas actuales: " + cd.obtenerFichas());
    }
    
    /**
     * Muestra el tablero completo en consola.
     * Se imprime primero la cabecera (coordenadas X) y luego cada fila con la coordenada Y al inicio.
     */
    private static void mostrarTablero() {
        // Se obtiene el tablero solo una vez para no llamar al controlador repetidamente
        Tablero tablero = cd.obtenerTablero();
        
        // Cabecera de columnas (coordenadas X)
        System.out.print("    ");
        for (int j = 0; j < 15; j++) {
            System.out.printf("%4d", j);
        }
        System.out.println();

        // Imprimir cada fila con la coordenada Y
        for (int i = 0; i < 15; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < 15; j++) {
                Celda celda = tablero.getCelda(i, j);
                String display = obtenerDisplayCelda(celda);
                System.out.printf("[%2s]", display);
            }
            System.out.println();
        }
    }
    
    /**
     * Determina qué mostrar en una celda dependiendo de su contenido.
     * @param celda La celda a procesar.
     * @return String con la letra de la ficha, la abreviatura de la bonificación o "usada".
     */
    private static String obtenerDisplayCelda(Celda celda) {
        if (celda.getFicha() != null) {
            return celda.getFicha().getLetra();
        } else {
            if (!celda.bonusDisponible() && celda.getBonificacion() != TipoBonificacion.NINGUNA) {
                return "usada";
            } else {
                switch (celda.getBonificacion()) {
                    case DOBLE_LETRA:
                        return "DL";
                    case TRIPLE_LETRA:
                        return "TL";
                    case DOBLE_PALABRA:
                        return "DP";
                    case TRIPLE_PALABRA:
                        return "TP";
                    default:
                        return "  ";
                }
            }
        }
    }
    
    /**
     * Lee un archivo de texto, filtrando las líneas vacías y comentarios (líneas que comienzan con "#").
     * @param rutaArchivo La ruta del archivo a leer.
     * @return Lista de líneas válidas.
     * @throws IOException Si ocurre algún error en la lectura.
     */
    public static List<String> leerArchivo(String rutaArchivo) throws IOException {
        List<String> lineasArchivo = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                // Se ignoran líneas vacías o comentarios.
                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    lineasArchivo.add(linea);
                }
            }
        }
        return lineasArchivo;
    }
    
    /**
     * Método principal del driver para probar una partida contra el algoritmo (CtrlDominio).
     * Se realiza el registro del jugador, la carga de recursos necesarios (diccionario y fichas)
     * y se inicia el bucle de juego que se ejecuta hasta escribir "salir".
     */
    public static void main(String[] args) {
        System.out.println("Driver de prueba de Partida contra el Algoritmo (CtrlDominio)");

        // Registro del jugador solicitando nombre y contraseña
        System.out.println("\n--- Registro del Jugador ---");
        System.out.print("Ingrese el nombre del jugador: ");
        String nombreJugador = in.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String contrasena = in.nextLine();
        cd.registrarJugador(nombreJugador, contrasena);
        Jugador jugador = cd.obtenerJugador(nombreJugador);
        System.out.println("Jugador registrado: " + jugador.getNombre());

        // Lectura de archivos necesarios para la partida: diccionario y bolsa de fichas
        List<String> lineasArchivoDiccionario;
        List<String> lineasArchivoBolsa;
        try {
            lineasArchivoBolsa = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/letrasCAST.txt");
            lineasArchivoDiccionario = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/castellano.txt");
            // Se inicia la partida. El tercer parámetro (oponente) se deja vacío para indicar partida contra algoritmo.
            cd.iniciarPartida(1, jugador.getNombre(), "", lineasArchivoDiccionario, lineasArchivoBolsa);
        } catch (IOException e) {
            System.err.println("Error al cargar archivos: " + e.getMessage());
            return;
        }

        // Bucle principal del juego: se procesa cada jugada hasta escribir "salir"
        String input;
        do {
            // Limpiar la pantalla para no superponer salidas anteriores
            clearScreen();
            
            System.out.println("\n--- Tablero Actual ---");
            mostrarTablero();
            System.out.println("\n--- Fichas Disponibles ---");
            mostrarFichas();
            
            System.out.println("\nIngrese una jugada (o escriba 'salir' para terminar):");
            input = in.nextLine();
            if (!input.equalsIgnoreCase("salir") && !input.trim().isEmpty()) {
                cd.jugarScrabble(input);
            }
            
        } while (!input.equalsIgnoreCase("salir"));
        
        System.out.println("Partida finalizada. ¡Gracias por jugar!");
        in.close();
    }
}
