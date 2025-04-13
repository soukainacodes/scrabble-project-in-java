package Dominio.Modelos;
import java.util.*;

public class Bolsa {
    // Atributos
    private int id;
    private List<Ficha> conjuntoDeFichas;
    private String idioma;

    // Constructor
    public Bolsa(List<String> lineasArchivo) {
        
        this.conjuntoDeFichas = new ArrayList<>();
       
        inicializarBolsa(lineasArchivo);
    }

    // Método para inicializar la bolsa cargando desde un archivo
    private void inicializarBolsa(List<String> lineasArchivo) {
        for (String linea : lineasArchivo) {
            String[] partes = linea.split(" ");
            if (partes.length == 3) {
                String letra = partes[0];
                int cantidad = Integer.parseInt(partes[1]);
                int puntuacion = Integer.parseInt(partes[2]);
                
                for (int i = 0; i < cantidad; i++) {
                    conjuntoDeFichas.add(new Ficha(conjuntoDeFichas.size() + 1, letra, puntuacion));
                }
            }
        }
        
        // Mezclar las fichas
        Collections.shuffle(conjuntoDeFichas);
    }

    // Método para mostrar todas las fichas con su puntuación
    public void mostrarFichas() {
        for (Ficha ficha : conjuntoDeFichas) {
            System.out.println("Ficha: " + ficha.getLetra() + ", Puntuación: " + ficha.getPuntuacion());
        }
    }

    // Método para mostrar solo las primeras 20 fichas
    public void mostrarPrimeras20Fichas() {
        for (int i = 0; i < Math.min(20, conjuntoDeFichas.size()); i++) {
            Ficha ficha = conjuntoDeFichas.get(i);
            System.out.println("Ficha: " + ficha.getLetra() + ", Puntuación: " + ficha.getPuntuacion());
        }
    }

    // Métodos auxiliares
    public int getId() {
        return id;
    }

    public List<Ficha> getConjuntoDeFichas() {
        return conjuntoDeFichas;
    }

    public String getIdioma() {
        return idioma;
    }

    public void añadirFicha(Ficha ficha) {
        conjuntoDeFichas.add(ficha);
    }

    public Ficha sacarFichaAleatoria() {
        if (conjuntoDeFichas.isEmpty()) {
            System.out.println("La bolsa está vacía.");
            return null;
        }
        return conjuntoDeFichas.remove((int) (Math.random() * conjuntoDeFichas.size()));
    }

    @Override
    public String toString() {
        return "Bolsa{" +
                "id=" + id +
                ", totalFichas=" + conjuntoDeFichas.size() +
                ", idioma='" + idioma + '\'' +
                '}';
    }
}
