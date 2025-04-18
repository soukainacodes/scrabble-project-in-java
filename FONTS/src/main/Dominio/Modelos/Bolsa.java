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

    public boolean isEmpty(){
        return conjuntoDeFichas.isEmpty();
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
