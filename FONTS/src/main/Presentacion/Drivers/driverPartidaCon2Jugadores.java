package Presentacion.Drivers;

import java.util.*;
import java.io.*;

import Dominio.Modelos.*;
import Dominio.*;

public class DriverPartidaCon2Jugadores {

    private static Scanner in = null;
    private static CtrlDominio cd;

    private static void mostrarFichas()
    {
        System.out.println(cd.obtenerFichas());
    }

    private static void mostrarTablero() {
        // Mostrar cabecera de columnas (coordenadas X)
        System.out.print("    "); // Espacio inicial para la columna de las coordenadas Y
        for (int j = 0; j < 15; j++) {
            System.out.printf("%4d", j);
        }
        System.out.println(); // Salto de línea después de la cabecera

        // Mostrar cada fila con la coordenada Y al inicio
        for (int i = 0; i < 15; i++) {
            System.out.printf("%2d: ", i); // Imprime la coordenada Y seguida de dos puntos y un espacio
            for (int j = 0; j < 15; j++) {
                Tablero tablero = cd.obtenerTablero();
                Celda celda = tablero.getCelda(i, j);
                String display;
                // Si hay ficha, muestra la letra
                if (celda.getFicha() != null) {
                    display = celda.getFicha().getLetra();
                } else {
                    // Si no hay ficha, revisa si la bonificación ya fue usada
                    if (!celda.bonusDisponible() && celda.getBonificacion() != TipoBonificacion.NINGUNA) {
                        display = "usada";
                    } else {
                        // Según la bonificación, asigna la abreviatura correspondiente
                        switch (celda.getBonificacion()) {
                            case DOBLE_LETRA:
                                display = "DL";
                                break;
                            case TRIPLE_LETRA:
                                display = "TL";
                                break;
                            case DOBLE_PALABRA:
                                display = "DP";
                                break;
                            case TRIPLE_PALABRA:
                                display = "TP";
                                break;
                            default:
                                display = "  ";
                                break;
                        }
                    }
                }
                System.out.printf("[%2s]", display);
            }
            System.out.println(); // Salto de línea al finalizar la fila
        }
    }

    public static void main(String[] args) {
        in = new Scanner(System.in);
        cd = new CtrlDominio();
        System.out.println("Driver de prueba de Partida con 2 Jugadores");

        // Crear 2 jugadores

        cd.registrarJugador("j1", "1234");
        cd.registrarJugador("j2", "1234");
        Jugador j1 = cd.obtenerJugador("j1");
        Jugador j2 = cd.obtenerJugador("j2");
        // Crear partida
        List<String> lineasArchivo; // Información para el diccionario (DAWG)
        List<String> lineasArchivoBolsa; // Información para la bolsa
        // Leer las fichas y el diccionario.
        try {
            lineasArchivoBolsa = leerArchivo("./resources/letrasCAST.txt");
            lineasArchivo = leerArchivo("./resources/castellano.txt");
            cd.iniciarPartida(1, j1.getNombre(), j2.getNombre(), lineasArchivo, lineasArchivoBolsa);
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }

        // Jugar

        // Mostrar tablero por terminal, visualmente.
        mostrarTablero();

        mostrarFichas();
        String input = in.nextLine();
        while (input != "salir") {

            cd.jugarScrabble(input);

            mostrarTablero();
            mostrarFichas();

            input = in.nextLine();
        }
    }

    public static List<String> leerArchivo(String rutaArchivo) throws IOException {
        List<String> lineasArchivo = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                // Saltamos líneas vacías o comentarios
                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    lineasArchivo.add(linea);
                }
            }
        }

        return lineasArchivo;
    }
}
