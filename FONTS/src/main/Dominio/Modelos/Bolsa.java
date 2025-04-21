package Dominio.Modelos;
import java.util.*;

public class Bolsa {
    // Atributos
    private List<Ficha> conjuntoDeFichas;
    private String idioma;

    // Constructor
    public Bolsa(List<String> lineasArchivo) {
        
        this.conjuntoDeFichas = new ArrayList<>();
       
        inicializarBolsa(lineasArchivo);

       
        
        Collections.shuffle(conjuntoDeFichas);
    }

    public Bolsa(List<String> lineasArchivo, long seed) {
        
        this.conjuntoDeFichas = new ArrayList<>();
       
        inicializarBolsa(lineasArchivo);

        
        Random random = new Random(seed);
        Collections.shuffle(conjuntoDeFichas, random);
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
                    conjuntoDeFichas.add(new Ficha( letra, puntuacion));
                }
            }
        }
    }

    

    // Métodos auxiliares

   public String getIdioma() {
        return idioma;
    }

    public Ficha sacarFichaAleatoria() {
        if (conjuntoDeFichas.isEmpty()) {
            return null;
        }
        return conjuntoDeFichas.remove((int) (Math.random() * conjuntoDeFichas.size()));
    }

    public Ficha sacarFicha() {
        if (conjuntoDeFichas.isEmpty()) {
            return null;
        }
        return conjuntoDeFichas.remove((0));
    }

    public boolean isEmpty(){
        return conjuntoDeFichas.isEmpty();
    }
  
}
