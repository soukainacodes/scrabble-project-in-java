package Presentacion.Drivers;

import java.util.*;
import java.io.*;

import Dominio.CtrlDominio;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Tablero;
import Dominio.Modelos.Celda;
import Dominio.Modelos.TipoBonificacion;
import Dominio.Excepciones.UsuarioYaRegistradoException;
import Dominio.Excepciones.UsuarioNoEncontradoException;

public class DriverPartidaCon2Jugadores {

    private static Scanner in = null;
    private static CtrlDominio cd;

    private static void mostrarFichas() {
        System.out.println(cd.obtenerFichas());
    }

    private static void mostrarTablero() {
        // Mostrar cabecera de columnas (coordenadas X)
        System.out.print("    ");
        for (int j = 0; j < 15; j++) {
            System.out.printf("%4d", j);
        }
        System.out.println();

        // Mostrar cada fila
        Tablero tablero = cd.obtenerTablero();
        for (int i = 0; i < 15; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < 15; j++) {
                Celda celda = tablero.getCelda(i, j);
                String display;
                if (celda.getFicha() != null) {
                    display = celda.getFicha().getLetra();
                } else if (!celda.bonusDisponible() && celda.getBonificacion() != TipoBonificacion.NINGUNA) {
                    display = "usada";
                } else {
                    switch (celda.getBonificacion()) {
                        case DOBLE_LETRA:   display = "DL"; break;
                        case TRIPLE_LETRA:  display = "TL"; break;
                        case DOBLE_PALABRA: display = "DP"; break;
                        case TRIPLE_PALABRA:display = "TP"; break;
                        default:            display = "  "; break;
                    }
                }
                System.out.printf("[%2s]", display);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        in = new Scanner(System.in);
        cd = new CtrlDominio();
        System.out.println("Driver de prueba de Partida con 2 Jugadores");

        // Crear 2 jugadores
        try {
            cd.registrarJugador("j1", "1234");
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }
        try {
            cd.registrarJugador("j2", "1234");
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        Jugador j1, j2;
        try {
            j1 = cd.obtenerJugador("j1");
            j2 = cd.obtenerJugador("j2");
        } catch (UsuarioNoEncontradoException e) {
            System.err.println("Error al recuperar jugador: " + e.getMessage());
            return;
        }

        // Crear partida
        List<String> lineasArchivo;
        List<String> lineasArchivoBolsa;
        try {
            lineasArchivoBolsa = leerArchivo("./resources/letrasCAST.txt");
            lineasArchivo     = leerArchivo("./resources/castellano.txt");
            cd.iniciarPartida(1, j1.getNombre(), j2.getNombre(), lineasArchivo, lineasArchivoBolsa);
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
            return;
        }

        // Jugar
        mostrarTablero();
        mostrarFichas();

        String input = in.nextLine();
        while (!"salir".equalsIgnoreCase(input)) {
            cd.jugarScrabble(input);
            mostrarTablero();
            mostrarFichas();
            input = in.nextLine();
        }
    }

    public static List<String> leerArchivo(String rutaArchivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
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
}
