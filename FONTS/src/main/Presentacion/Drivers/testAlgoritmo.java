import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.Dominio.Controladores.Modelos.Partida.Dawg.*;


public class testAlgoritmo {
     public static void main(String[] args) {
 
        Tablero t = new Tablero();
        
    
        List<Ficha> fichas =  new ArrayList<>();
        fichas.add(new Ficha(101, "L", 4));
        fichas.add(new Ficha(101, "O", 4));
        fichas.add(new Ficha(101, "O", 4));
        fichas.add(new Ficha(101, "A", 4));
        Dawg dawg = new Dawg();
        List<String> lineasArchivo; //Información para el diccionario (DAWG)
        List<String> lineasArchivoBolsa; //Información para la bolsa
        try{System.out.println(System.getProperty("user.dir"));
            lineasArchivoBolsa = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/letrasCAST.txt"); 
            lineasArchivo = leerArchivo("./FONTS/src/main/Recursos/Idiomas/Castellano/castellano.txt");
            dawg.cargarFichasValidas(lineasArchivoBolsa);
             dawg.construirDesdeArchivo(lineasArchivo);
             
           Algoritmo a = new Algoritmo(fichas, dawg,t);
           //Tablero t2 = a.copy(t);
             boolean colocadaF = t.ponerFicha(new Ficha(101, "F", 4), 4, 4);
            
             //boolean colocadas = t2.ponerFicha(new Ficha(101, "F", 4), 3, 2);
             a.find_all_words();

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
      
    }
    public static List<String> leerArchivo(String rutaArchivo) throws IOException {
        List<String> lineasArchivo = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                // Saltamos líneas vacías o comentarios
                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    lineasArchivo.add(linea);
                }
            }
        }
        
        return lineasArchivo;
    }
}
