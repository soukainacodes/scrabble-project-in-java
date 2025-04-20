package Presentacion.Drivers;

import java.io.*;
import java.util.*;

import Dominio.CtrlDominio;

public class DriverGestionDiccionariosBolsas {
    private static final Scanner sc = new Scanner(System.in);
    private static final CtrlDominio ctrl = new CtrlDominio();

    public static void main(String[] args) {
        while (true) {
            menuPrincipal();
        }
    }

    private static void menuPrincipal() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Consultar diccionarios y bolsas");
        System.out.println("2. Gestión de diccionarios y bolsas");
        System.out.println("3. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1": consultarRecursos();       break;
            case "2": gestionRecursos();         break;
            case "3": System.exit(0);            break;
            default:  System.out.println("Opción no válida.");
        }
    }

    private static void consultarRecursos() {
        Set<String> dicIDs = ctrl.obtenerIDsDiccionarios();
        Set<String> bolIDs = ctrl.obtenerIDsBolsas();
        Set<String> all    = new TreeSet<>();
        all.addAll(dicIDs);
        all.addAll(bolIDs);

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

    private static void gestionRecursos() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE RECURSOS ===");
            System.out.println("1. Eliminar diccionario+bolsa");
            System.out.println("2. Añadir nuevo diccionario+bolsa");
            System.out.println("3. Volver");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1": eliminarRecurso();    break;
                case "2": anadirRecurso();      break;
                case "3": return;
                default:  System.out.println("Opción no válida.");
            }
        }
    }

    private static void eliminarRecurso() {
        System.out.print("ID a eliminar (0 para volver): ");
        String id = sc.nextLine().trim();
        if ("0".equals(id)) return;

        try {
            ctrl.eliminarIdiomaCompleto(id);
            System.out.println("Recurso '" + id + "' eliminado.");
        } catch (Exception e) {
            System.err.println("Error al eliminar recurso: " + e.getMessage());
        }
    }

    private static void anadirRecurso() {
        while (true) {
            System.out.println("\n--- AÑADIR RECURSO ---");
            System.out.println("1. Desde directorio existente");
            System.out.println("2. Desde teclado");
            System.out.println("3. Volver");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1": anadirDesdeDirectorio(); break;
                case "2": anadirDesdeTeclado();     break;
                case "3": return;
                default:  System.out.println("Opción no válida.");
            }
        }
    }

    private static void anadirDesdeDirectorio() {
        System.out.print("Ruta al directorio (0 para volver): ");
        String dirPath = sc.nextLine().trim();
        if ("0".equals(dirPath)) return;

        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            System.err.println("No es un directorio válido.");
            return;
        }
        String id = dir.getName();
        File fDicc = new File(dir, id + "_diccionario.txt");
        File fBol  = new File(dir, id + "_bolsa.txt");
        if (!fDicc.exists() || !fBol.exists()) {
            System.err.println("Faltan '" + fDicc.getName() + "' o '" + fBol.getName() + "'.");
            return;
        }

        try {
            List<String> palabras = leerArchivoTexto(fDicc.getAbsolutePath());
            ctrl.crearDiccionario(id, palabras);
            Map<String,int[]> bolsa = leerArchivoBolsa(fBol.getAbsolutePath());
            ctrl.crearBolsa(id, bolsa);
            System.out.println("Recurso '" + id + "' añadido desde directorio.");
        } catch (Exception e) {
            System.err.println("Error al añadir recurso: " + e.getMessage());
        }
    }

    private static void anadirDesdeTeclado() {
        System.out.print("Nuevo ID (0 para volver): ");
        String id = sc.nextLine().trim();
        if ("0".equals(id)) return;

        // Diccionario
        System.out.println("Introduce palabras (MAYÚSCULAS), línea vacía para terminar:");
        List<String> palabras = new ArrayList<>();
        while (true) {
            String w = sc.nextLine().trim();
            if (w.isEmpty()) break;
            palabras.add(w);
        }

        // Bolsa
        System.out.println("Introduce líneas de bolsa: \"Ficha repeticiones puntos\" (vacío para terminar):");
        Map<String,int[]> bolsa = new LinkedHashMap<>();
        while (true) {
            String l = sc.nextLine().trim();
            if (l.isEmpty()) break;
            String[] t = l.split("\\s+");
            if (t.length != 3) {
                System.err.println("Formato inválido. Debe ser: Ficha número puntos");
                continue;
            }
            try {
                int rep = Integer.parseInt(t[1]);
                int pts = Integer.parseInt(t[2]);
                bolsa.put(t[0], new int[]{rep, pts});
            } catch (NumberFormatException e) {
                System.err.println("Valores numéricos incorrectos.");
            }
        }

        try {
            ctrl.crearDiccionario(id, palabras);
            ctrl.crearBolsa(id, bolsa);
            System.out.println("Recurso '" + id + "' creado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear recurso: " + e.getMessage());
        }
    }

    // ─── Helpers de lectura ──────────────────────────────────

    private static List<String> leerArchivoTexto(String ruta) throws IOException {
        List<String> lista = new ArrayList<>();
        try (var br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) lista.add(linea);
            }
        }
        return lista;
    }

    private static Map<String,int[]> leerArchivoBolsa(String ruta) throws IOException {
        Map<String,int[]> mapa = new LinkedHashMap<>();
        try (var br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                String[] t = linea.split("\\s+");
                mapa.put(t[0], new int[]{
                    Integer.parseInt(t[1]),
                    Integer.parseInt(t[2])
                });
            }
        }
        return mapa;
    }
}
