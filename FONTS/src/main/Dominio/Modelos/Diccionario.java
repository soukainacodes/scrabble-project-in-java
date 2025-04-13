package Dominio.Modelos;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Diccionario {
    private Set<String> palabras;
    private List<String> listaPalabras; // Para mantener el orden de inserción 

    public Diccionario(String archivoDiccionario) {
        this.palabras = new HashSet<>();
        this.listaPalabras = new ArrayList<>();
        cargarPalabrasDesdeArchivo(archivoDiccionario);
    }

    private void cargarPalabrasDesdeArchivo(String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String palabra = linea.trim();
                if (!palabra.isEmpty()) {
                    palabras.add(palabra);
                    listaPalabras.add(palabra); // Guardar en la lista para mantener el orden
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el diccionario: " + e.getMessage());
        }
    }

    public boolean contienePalabra(String palabra) {
        return palabras.contains(palabra);
    }

    public int totalPalabras() {
        return palabras.size();
    }

    public void mostrarNumPalabras() {
        System.out.println("Diccionario cargado con " + totalPalabras() + " palabras.");
    }

    public void mostrarPrimeras20Palabras() {
        System.out.println("\nLas primeras 20 palabras del diccionario:");
        for (int i = 0; i < 20; i++) {
            System.out.println(listaPalabras.get(i));
        }
    }

    
}

