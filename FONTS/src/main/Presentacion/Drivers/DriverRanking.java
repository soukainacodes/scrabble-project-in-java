package Presentacion.Drivers;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Dominio.CtrlRanking;

/**
 * Clase Driver para testear la funcionalidad del Ranking.
 * <p>
 * Esta clase contiene el método main y se encarga de la interacción por consola,
 * mostrando el ranking y permitiendo probar las operaciones del controlador.
 * </p>
 */
public class DriverRanking {

    public static void main(String[] args) {
        CtrlRanking ctrlRanking = new CtrlRanking();
        Scanner scanner = new Scanner(System.in);
        int opcion;
        
        do {
            System.out.println("\n--- Menú del Ranking ---");
            System.out.println("1. Agregar jugador");
            System.out.println("2. Modificar puntuación de jugador");
            System.out.println("3. Mostrar ranking");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre del jugador: ");
                    String nombreNuevo = scanner.nextLine();
                    System.out.print("Ingrese la puntuación: ");
                    int puntuacionNueva = Integer.parseInt(scanner.nextLine());
                    ctrlRanking.crearJugador(nombreNuevo, puntuacionNueva);
                    System.out.println("Jugador agregado.");
                    break;
                    
                case 2:
                    System.out.print("Ingrese el nombre del jugador a modificar: ");
                    String nombreModificar = scanner.nextLine();
                    System.out.print("Ingrese la nueva puntuación: ");
                    int nuevaPuntuacion = Integer.parseInt(scanner.nextLine());
                    ctrlRanking.modificarRanking(nombreModificar, nuevaPuntuacion);
                    break;
                    
                case 3:
                    System.out.println("\n--- Ranking ---");
                    // Se obtiene el ranking y se muestra cada entrada en el mismo formato que se guarda.
                    List<Map.Entry<String, Integer>> listaRanking = ctrlRanking.getRankingOrdenado();
                    for (Map.Entry<String, Integer> entry : listaRanking) {
                        System.out.println(entry.getKey() + " " + entry.getValue());
                    }
                    break;
                    
                case 4:
                    System.out.println("Saliendo...");
                    break;
                    
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
            
        } while (opcion != 4);
        
        scanner.close();
    }
}
