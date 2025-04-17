// Presentacion/Drivers/DriverAplicacion.java
package Presentacion.Drivers;

import java.util.*;
import Dominio.CtrlJugador;
import Dominio.CtrlRanking;
import Dominio.Modelos.Jugador;

public class DriverAplicacion {
    private static CtrlJugador ctrlJ   = new CtrlJugador();
    private static CtrlRanking ctrlR   = new CtrlRanking(ctrlJ);
    private static Jugador   jugadorActu;
    private static Scanner   sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            if (jugadorActu == null) menuPrincipal();
            else                     menuUsuario();
        }
    }

    private static void menuPrincipal() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1": iniciarSesion();  break;
            case "2": registrarse(true);    break;
            case "3": System.exit(0);
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void menuUsuario() {
        System.out.println("\n===== MENÚ USUARIO =====");
        System.out.println("1. Cuenta");
        System.out.println("2. Ver Ranking");
        System.out.println("3. Añadir Usuario");
        System.out.println("3. Cerrar Sesión");
        System.out.println("4. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1": subMenuCuenta();  break;
            case "2": verRanking();      break;
            case "3": registrarse(false);    break;
            case "4": jugadorActu = null; System.out.println("Sesión cerrada."); break;
            case "5": System.exit(0);
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void subMenuCuenta() {
        while (true) {
            System.out.println("\n--- TU CUENTA ---");
            System.out.println("Usuario: " + jugadorActu.getNombre());
            System.out.println("Puntos: " + jugadorActu.getPuntos());
            int pos = ctrlR.getPosition(jugadorActu.getNombre());
            if (pos > 0) {
                System.out.println("Posición: " + pos);
            }
            System.out.println("1. Cambiar Contraseña");
            System.out.println("2. Eliminar Perfil");
            System.out.println("3. Volver");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1": cambiarPassword();   break;
                case "2": eliminarPerfil();    return;  // vuelve al menú principal sin sesión
                case "3": return;
                default:  System.out.println("Opción no válida.");
            }
        }
    }

    private static void cambiarPassword() {
        System.out.print("Contraseña actual: ");
        String ant = sc.nextLine().trim();
        System.out.print("Nueva contraseña: ");
        String nue = sc.nextLine().trim();
        if (ctrlJ.cambiarPassword(jugadorActu.getNombre(), ant, nue)) {
            System.out.println("Contraseña cambiada correctamente.");
            // refrescar objeto
            jugadorActu = ctrlJ.iniciarSesion(jugadorActu.getNombre(), nue);
        } else {
            System.out.println("Contraseña incorrecta. No se cambió.");
        }
    }

    private static void eliminarPerfil() {
        System.out.print("Confirme su contraseña para eliminar su cuenta: ");
        String pass = sc.nextLine().trim();
        if (ctrlJ.eliminarJugador(jugadorActu.getNombre(), pass)) {
            System.out.println("Perfil eliminado. Adiós.");
            jugadorActu = null;
        } else {
            System.out.println("Contraseña incorrecta. No se eliminó.");
        }
    }

    private static void iniciarSesion() {
        System.out.print("Usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();
        Jugador j = ctrlJ.iniciarSesion(u, p);
        if (j != null) {
            jugadorActu = j;
            System.out.printf("¡Bienvenido %s! Puntos: %d", u, j.getPuntos());
            int pos = ctrlR.getPosition(u);
            if (pos > 0) System.out.printf("   Posición: %d%n", pos);
            else         System.out.println();
        } else {
            System.out.println("Credenciales inválidas.");
        }
    }

    private static void registrarse(boolean inicio) {
        System.out.print("Nuevo usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Nueva password: ");
        String p = sc.nextLine().trim();
        if (ctrlJ.crearJugador(u, p)) {
            if(inicio) jugadorActu = ctrlJ.iniciarSesion(u, p);
            System.out.printf("Registrado '%s'. Puntos: 0%n", u);
            // pos = 1 si es el único, pero lo omitimos si -1
        } else {
            System.out.println("Ese usuario ya existe.");
        }
    }

    private static void verRanking() {
        System.out.println("\n--- RANKING GLOBAL ---");
        var lista = ctrlR.getRankingOrdenado();
        for (int i = 0; i < lista.size(); i++) {
            var e = lista.get(i);
            System.out.printf("%2d. %-15s %4d%n", i+1, e.getKey(), e.getValue());
        }
    }
}
