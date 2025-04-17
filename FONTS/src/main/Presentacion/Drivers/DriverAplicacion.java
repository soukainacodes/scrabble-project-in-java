package Presentacion.Drivers;

import java.util.*;
import Dominio.CtrlDominio;

public class DriverAplicacion {
    private static final CtrlDominio ctrl = new CtrlDominio();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            if (!ctrl.haySesion()) menuPrincipal();
            else                  menuUsuario();
        }
    }

    private static void menuPrincipal() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1": iniciarSesion();   break;
            case "2": registrarse(true); break;
            case "3": System.exit(0);
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void menuUsuario() {
        System.out.println("\n===== MENÚ USUARIO =====");
        System.out.println("1. Cuenta");
        System.out.println("2. Ver Ranking");
        System.out.println("3. Cerrar Sesión");
        System.out.println("4. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1": subMenuCuenta();    break;
            case "2": verRanking();       break;
            case "3":
                ctrl.cerrarSesion();
                System.out.println("Sesión cerrada.");
                break;
            case "4": System.exit(0);
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void iniciarSesion() {
        System.out.print("Usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();

        if (ctrl.iniciarSesion(u, p)) {
            int puntos = ctrl.getPuntosActual();
            int pos    = ctrl.getPosicionActual();

            System.out.println();
            System.out.println("========================================");
            System.out.printf("   ¡ Bienvenido, %s !%n", u);
            System.out.printf("   Puntos: %d    Posición: %d%n", puntos, (pos > 0 ? pos : 0));
            System.out.println("========================================\n");
        } else {
            System.out.println("Credenciales inválidas.");
        }
    }

    private static void registrarse(boolean inicio) {
        System.out.print("Nuevo usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Nueva password: ");
        String p = sc.nextLine().trim();

        if (ctrl.crearUsuario(u, p)) {
            System.out.printf("Registrado '%s'. Puntos: 0%n", u);
            if (inicio) iniciarSesion();
        } else {
            System.out.println("Ese usuario ya existe.");
        }
    }

    private static void verRanking() {
        System.out.println("\n--- RANKING GLOBAL ---");
        var lista = ctrl.obtenerRanking();
        for (int i = 0; i < lista.size(); i++) {
            var e = lista.get(i);
            System.out.printf("%2d. %-15s %4d%n", i + 1, e.getKey(), e.getValue());
        }
    }

    private static void subMenuCuenta() {
        while (true) {
            String u     = ctrl.getUsuarioActual();
            int    puntos = ctrl.getPuntosActual();
            int    pos    = ctrl.getPosicionActual();

            System.out.println("\n--- TU CUENTA ---");
            System.out.println("Usuario: " + u);
            System.out.println("Puntos: " + puntos);
            if (pos > 0) System.out.println("Posición: " + pos);
            System.out.println("1. Cambiar Contraseña");
            System.out.println("2. Eliminar Perfil");
            System.out.println("3. Volver");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1": cambiarPassword(); break;
                case "2": eliminarPerfil();  return;
                case "3": return;
                default:  System.out.println("Opción no válida.");
            }
        }
    }

    private static void cambiarPassword() {
        System.out.print("Contraseña actual: ");
        String ant = sc.nextLine().trim();
        System.out.print("Nueva contraseña: ");
        String n1  = sc.nextLine().trim();
        System.out.print("Repita la nueva contraseña: ");
        String n2  = sc.nextLine().trim();

        if (!n1.equals(n2)) {
            System.out.println("Error: las contraseñas no coinciden.");
            return;
        }
        if (ctrl.cambiarPassword(ant, n1)) {
            System.out.println("Contraseña cambiada correctamente.");
        } else {
            System.out.println("Contraseña actual incorrecta. No se cambió.");
        }
    }

    private static void eliminarPerfil() {
        System.out.print("¿Seguro que desea eliminar su cuenta? (s/n): ");
        String r = sc.nextLine().trim().toLowerCase();
        if (!r.equals("s") && !r.equals("si")) {
            System.out.println("Eliminación cancelada.");
            return;
        }
        System.out.print("Confirme su contraseña: ");
        String pass = sc.nextLine().trim();

        if (ctrl.eliminarUsuario(pass)) {
            System.out.println("Perfil eliminado. Adiós.");
        } else {
            System.out.println("Contraseña incorrecta. No se eliminó.");
        }
    }
}
