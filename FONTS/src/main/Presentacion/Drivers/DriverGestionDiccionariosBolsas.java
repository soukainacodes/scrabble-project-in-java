package Presentacion.Drivers;

import java.io.*;
import java.util.*;

import Dominio.CtrlDominio;
import Dominio.Excepciones.BolsaNoEncontradaException;
import Dominio.Excepciones.BolsaYaExistenteException;
import Dominio.Excepciones.DiccionarioNoEncontradoException;
import Dominio.Excepciones.DiccionarioYaExistenteException;

/**
 * Driver para gestionar y probar la funcionalidad de diccionarios y bolsas.
 * Permite realizar operaciones como:
 * <ul>
 *   <li>Consultar diccionarios y bolsas disponibles.</li>
 *   <li>Ejecutar pruebas automáticas de creación y eliminación de recursos.</li>
 *   <li>Gestionar recursos manualmente (añadir/eliminar).</li>
 * </ul>
 */

 
public class DriverGestionDiccionariosBolsas {
    private static final Scanner sc = new Scanner(System.in);
    private static final CtrlDominio ctrl = new CtrlDominio();

    /**
     * Punto de entrada principal del driver.
     * Muestra un menú principal para interactuar con las funcionalidades.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     * @throws DiccionarioNoEncontradoException si no se encuentra un diccionario
     * @throws BolsaNoEncontradaException si no se encuentra una bolsa
     * @throws DiccionarioYaExistenteException si ya existe un diccionario con el mismo ID
     * @throws BolsaYaExistenteException si ya existe una bolsa con el mismo ID
     */
    public static void main(String[] args) throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, DiccionarioYaExistenteException, BolsaYaExistenteException {
        while (true) {
            menuPrincipal();
        }
    }

    /**
     * Muestra el menú principal y gestiona las opciones seleccionadas por el usuario.
     *
     * @throws DiccionarioNoEncontradoException si no se encuentra un diccionario
     * @throws BolsaNoEncontradaException si no se encuentra una bolsa
     * @throws DiccionarioYaExistenteException si ya existe un diccionario con el mismo ID
     * @throws BolsaYaExistenteException si ya existe una bolsa con el mismo ID
     */
    private static void menuPrincipal() throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, DiccionarioYaExistenteException, BolsaYaExistenteException {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Consultar diccionarios y bolsas");
        System.out.println("2. Juego de pruebas");
        System.out.println("3. Gestión de diccionarios y bolsas");
        System.out.println("4. Salir");
        System.out.print("Opción: ");
        switch (sc.nextLine().trim()) {
            case "1": consultarRecursos();   break;
            case "2": juegoPruebas();        break;
            case "3": gestionRecursos();     break;
            case "4": System.exit(0);        break;
            default:  System.out.println("Opción no válida.");
        }
    }

    /**
     * Consulta y muestra los diccionarios y bolsas disponibles.
     */
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

    /**
     * Ejecuta un conjunto de pruebas automáticas para verificar la funcionalidad
     * de creación, consulta y eliminación de diccionarios y bolsas.
     */
    private static void juegoPruebas() {
        System.out.print("\033[H\033[2J");
        System.out.println("=== Ejecutando juego de pruebas de Diccionarios/Bolsas ===\n");
        boolean errores = false;
        List<String> testIDs = List.of(
            "Pingu", "HelloKitty", "DragonBall",
            "Mickey", "SpongeBob", "Pokemon"
        );

        for (String id : testIDs) {
            System.out.println("────────────────────────────────────────");
            System.out.println("PRUEBA RECURSO ID = " + id);
            System.out.println("────────────────────────────────────────");
            try {
                List<String> palabras = List.of("A", "B", "C");
                ctrl.crearDiccionario(id, palabras);
                System.out.println("-> Diccionario '" + id + "' creado.");

                Map<String,int[]> bolsa = new LinkedHashMap<>();
                bolsa.put("A", new int[]{1,1});
                bolsa.put("B", new int[]{2,2});
                bolsa.put("C", new int[]{3,3});
                ctrl.crearBolsa(id, bolsa);
                System.out.println("-> Bolsa '" + id + "' creada.");

                System.out.println("\nListado tras creación:");
                consultarRecursos();

                try {
                    ctrl.eliminarIdiomaCompleto(id);
                    System.out.println("-> Recurso '" + id + "' eliminado.\n");
                } catch (IOException ex) {
                    throw new RuntimeException("No se pudo eliminar: " + ex.getMessage(), ex);
                }

            } catch (Exception e) {
                errores = true;
                System.err.println("Error en prueba ID " + id + ": " + e.getMessage() + "\n");
            }
        }

        System.out.println("────────────────────────────────────────");
        if (!errores) {
            System.out.println("Todas las pruebas se ejecutaron correctamente.");
        } else {
            System.out.println("Algunas pruebas fallaron. Revisar errores.");
        }
        System.out.println("────────────────────────────────────────\n");
    }

    /**
     * Muestra un menú para gestionar recursos (añadir/eliminar diccionarios y bolsas).
     *
     * @throws DiccionarioNoEncontradoException si no se encuentra un diccionario
     * @throws BolsaNoEncontradaException si no se encuentra una bolsa
     * @throws DiccionarioYaExistenteException si ya existe un diccionario con el mismo ID
     * @throws BolsaYaExistenteException si ya existe una bolsa con el mismo ID
     */
    private static void gestionRecursos() throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, DiccionarioYaExistenteException, BolsaYaExistenteException {
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

    /**
     * Elimina un recurso (diccionario y bolsa) por su ID.
     *
     * @throws DiccionarioNoEncontradoException si no se encuentra el diccionario
     * @throws BolsaNoEncontradaException si no se encuentra la bolsa
     */
    private static void eliminarRecurso() throws DiccionarioNoEncontradoException, BolsaNoEncontradaException {
        System.out.print("ID a eliminar (0 para volver): ");
        String id = sc.nextLine().trim();
        if ("0".equals(id)) return;

        try {
            ctrl.eliminarIdiomaCompleto(id);
            System.out.println("Recurso '" + id + "' eliminado.");
        } catch (IOException e) {
            System.err.println("Error al eliminar recurso: " + e.getMessage());
        }
    }

    /**
     * Añade un nuevo recurso (diccionario y bolsa) desde un directorio o desde el teclado.
     *
     * @throws DiccionarioYaExistenteException si ya existe un diccionario con el mismo ID
     * @throws BolsaYaExistenteException si ya existe una bolsa con el mismo ID
     */
    private static void anadirRecurso() throws DiccionarioYaExistenteException, BolsaYaExistenteException {
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

    /**
     * Añade un recurso desde un directorio existente.
     *
     * @throws DiccionarioYaExistenteException si ya existe un diccionario con el mismo ID
     * @throws BolsaYaExistenteException si ya existe una bolsa con el mismo ID
     */
    private static void anadirDesdeDirectorio() throws DiccionarioYaExistenteException, BolsaYaExistenteException {
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
            System.err.println("Faltan archivos en el directorio.");
            return;
        }

        try {
            List<String> palabras = leerArchivoTexto(fDicc.getAbsolutePath());
            ctrl.crearDiccionario(id, palabras);

            Map<String,int[]> bolsa = leerArchivoBolsa(fBol.getAbsolutePath());
            ctrl.crearBolsa(id, bolsa);

            System.out.println("Recurso '" + id + "' añadido desde directorio.");
        } catch (IOException e) {
            System.err.println("Error I/O: " + e.getMessage());
        }
    }

    /**
     * Añade un recurso desde el teclado.
     *
     * @throws DiccionarioYaExistenteException si ya existe un diccionario con el mismo ID
     * @throws BolsaYaExistenteException si ya existe una bolsa con el mismo ID
     */
    private static void anadirDesdeTeclado() throws DiccionarioYaExistenteException, BolsaYaExistenteException {
        System.out.print("Nuevo ID (0 para volver): ");
        String id = sc.nextLine().trim();
        if ("0".equals(id)) return;

        System.out.println("Introduce palabras (MAYÚSCULAS), línea vacía para terminar:");
        List<String> palabras = new ArrayList<>();
        while (true) {
            String w = sc.nextLine().trim();
            if (w.isEmpty()) break;
            palabras.add(w);
        }

        System.out.println("Introduce líneas de bolsa: \"Ficha repeticiones puntos\" (vacío para terminar):");
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
        } catch (IOException e) {
            System.err.println("Error al crear recurso: " + e.getMessage());
        }
    }

    /**
     * Lee un archivo de texto y devuelve una lista de líneas no vacías.
     *
     * @param ruta ruta del archivo
     * @return lista de líneas leídas
     * @throws IOException si ocurre un error de lectura
     */
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

    /**
     * Lee un archivo de bolsa y devuelve un mapa con los datos de las fichas.
     *
     * @param ruta ruta del archivo
     * @return mapa con las fichas y sus datos (repeticiones y puntos)
     * @throws IOException si ocurre un error de lectura
     */
    private static Map<String,int[]> leerArchivoBolsa(String ruta) throws IOException {
        Map<String,int[]> mapa = new LinkedHashMap<>();
        try (var br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
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
