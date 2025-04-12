import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import main.Dominio.Controladores.Modelos.Partida.Dawg.*;
class Position {
    public int row;
    public int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
   

}
public class Algoritmo {
    int puntos;
  
    int puntosRecorrerDireccion;
    List<String> fichass;
    List<Ficha> f;
    Dawg diccionario;
    Tablero tablero;
    
    
    public Algoritmo(List<Ficha> fichas,  Dawg diccionario, Tablero tablero ){
       this.puntos = 0; 
        //diccionari
       this.tablero = tablero;
       
       this.fichass = new ArrayList<>();
       this.f = fichas;
       for(Ficha f: fichas){
        fichass.add(f.getLetra());
       }
       
       this.diccionario = diccionario;
        //fichas del jugador
       
    }

    private boolean isEmpty(Position pos){
        int x = pos.row;
        int y = pos.col;
        return (tablero.getCelda(x, y) != null && !tablero.getCelda(x, y).estaOcupada());
    }

    private boolean isFilled(Position pos){
        int x = pos.row;
        int y = pos.col;
        return (tablero.getCelda(x, y) != null && tablero.getCelda(x, y).estaOcupada());
    }

    private boolean isDentroTablero(Position pos){
        int x = pos.row;
        int y = pos.col;
        return (tablero.getCelda(x, y) != null);
    }



  

    private ArrayList<Position> find_anchors(){
    ArrayList<Position> anchors = new ArrayList<>();
        for(int i=0;i<15;++i){
            for(int j=0;j<15;j++){
                Position pos = new Position(i,j);
                boolean empty = isEmpty(pos);
                boolean vecinoOcupado = (isFilled(left(pos)) || isFilled(right(pos)) || isFilled(up(pos)) || isFilled(down(pos)));     
                
                if(empty && vecinoOcupado){
                    anchors.add(pos);
                }
            }
        }
        return anchors;
    }

    private Tablero copy(Tablero tablero){
        
        Tablero t =  new Tablero();
        for(int i=0;i<15;++i){
            for(int j=0;j<15;++j){
                if(tablero.getCelda(i, j) != null && tablero.getFicha(i, j) != null){
                t.ponerFicha(tablero.getFicha(i, j),i,j);
                }
            }
        }
        return t;
    }

    private Position left(Position pos){
        int row = pos.row;
        int col = pos.col;

        return new Position(row, col - 1);
    }

    private Position right(Position pos){
        int row = pos.row;
        int col = pos.col;

        return new Position(row, col + 1);
    }

    private Position up(Position pos){
        int row = pos.row;
        int col = pos.col;

        return new Position(row -1, col);
    }
    private Position down(Position pos){
        int row = pos.row;
        int col = pos.col;

        return new Position(row + 1, col);
    }

    private void ponerFicha(String s, Position pos, Tablero t){
        int row = pos.row;
        int col = pos.col;
        for( Ficha ficha : f ){
            if(ficha.getLetra().contains(s)){
                t.ponerFicha(ficha, row, col);
                break;
            }
        }    
    }

    private void palabra_parcial(String palabra, Position last_pos){
        System.out.println(palabra);
        Tablero t = copy(tablero);
        Position play_pos = last_pos;
        int lenght = palabra.length()-1;
        
        while(lenght >= 0){
            ponerFicha(String.valueOf(palabra.charAt(lenght)), play_pos, t);
            lenght--;
            play_pos = left(play_pos);
            
        }  
          System.out.println(puntos);
        t.mostrarTablero();     
        System.out.println("");
    }
   private boolean recorrerDireccion(Position pos, boolean direccion, String s) {
    StringBuilder palabra = new StringBuilder();
    StringBuilder palabra2 = new StringBuilder();
    int xx = pos.row;
    int yy = pos.col;
    int x = xx, y = yy;
    int puntosFicha = 0;
    int dx, dy;
    puntosRecorrerDireccion = 0;
   
    // Determine direction of traversal
    if (direccion) {
        dx = 1; // horizontal
        dy = 0;
    } else {
        dx = 0; // vertical
        dy = 1;
    }

    // Recorre en ambas direcciones
    for (int dir = -1; dir <= 1; dir += 2) {
        x = xx;
        y = yy;
        StringBuilder currentPalabra = (dir == -1) ? palabra2 : palabra;

        while (true) {
            // Adjust coordinates before checking
            x += dir * dx;
            y += dir * dy;

            // Check if cell is out of bounds or empty
            if (x < 0 || y < 0 || 
                tablero.getCelda(x, y) == null || 
                tablero.getFicha(x, y) == null) {
                break;
            }

            String letra = tablero.getFicha(x, y).getLetra();
            Position p = new Position(x, y);

           puntosRecorrerDireccion += getFichaPuntuacion(p, false);
           
            // Append letter to appropriate StringBuilder
            if (dir == 1) {
                currentPalabra.append(letra);
            } else {
                currentPalabra.insert(0, letra);
            }

        
        }
    }

    // Combine words and check dictionary
   
    Position p = new Position(xx, yy);
   // puntosRecorrerDireccion += getFichaPuntuacion(p, true);
    
   // if(tablero.getCelda(xx,yy).isDobleTriplePalabra()) {
     //   puntosRecorrerDireccion *= tablero.getCelda(xx,yy).getBonificacion().getMultiplicador();
   // }
    String palabraCompleta = palabra2.toString() + s + palabra.toString();
    System.out.println(palabraCompleta);
    if(diccionario.buscarPalabra(palabraCompleta)){
        
        return true;
    }else{
        return false;
    }

}

private int getFichaPuntuacion(Position pos, boolean vacia){
        int x = pos.row;
        int y = pos.col;
        if(tablero.getFicha(x,y) != null){
        
        int p =  tablero.getFicha(x,y).getPuntuacion();
        if(vacia && tablero.getCelda(x, y).isDobleTripleLetra()){
            p *= tablero.getCelda(x,y).getBonificacion().getMultiplicador();
        }
        return p;
        }
        return 0;
    }

    private void extend_right(String palabraParcial,Nodo nodo_actual, Position next_pos, boolean anchor_filled){
        if(!isFilled(next_pos) && nodo_actual.esValida() && anchor_filled){
            palabra_parcial(palabraParcial, left(next_pos));
        }
            if(isDentroTablero(next_pos)){
                if(isEmpty(next_pos)){
                    for( String s : nodo_actual.getHijos().keySet()){
                        System.out.println(puntos);
                        if(fichass.contains(s)  && recorrerDireccion(next_pos, true,s)){
                            fichass.remove(s);
                            puntos += puntosRecorrerDireccion;
                           
                            //System.out.println(puntosRecorrerDireccion);
                            extend_right(palabraParcial + s, nodo_actual.getHijos().get(s), right(next_pos), true);
                            puntos -= puntosRecorrerDireccion;
                            fichass.add(s);

                        }
                    
                    }
                }
                else{
                    String existing_letter = getFicha(next_pos);
                   
                    if(nodo_actual.getHijos().containsKey(existing_letter)){
                        int p = getFichaPuntuacion(next_pos,true);
                       // puntos += p;
                        extend_right(palabraParcial + existing_letter, nodo_actual.getHijos().get(existing_letter), right(next_pos),true);
                        puntos -= p;
                    }
                }
          }   
    }

    private String getFicha(Position pos){
        int row = pos.row;
        int col = pos.col;

        return tablero.getFicha(row,col).getLetra();
        
    }

    private void left_part(String partial_word, Nodo nodo_actual, Position pos, int limit ){

        extend_right(partial_word, nodo_actual, pos, false);
        if( limit > 0){
            for( String s : nodo_actual.getHijos().keySet()){
                if(fichass.contains(s)){
                    fichass.remove(s);
                    
                    int p = getFichaPuntuacion(pos,true);
                   // puntos += p;
                    left_part(partial_word + s, nodo_actual.getHijos().get(s), pos, limit - 1 );
                    puntos -=p;
                    fichass.add(s);
                }
            }
        }

    }
    public void find_all_words(){
        ArrayList<Position> anchors = new ArrayList<>();
        anchors = find_anchors();
        
        puntos = 0;
      
        for(Position pos : anchors){
            if(isFilled(left(pos))){

                Position scan_pos = left(pos);
                puntos = getFichaPuntuacion(scan_pos,false);
            
                String partial_word = getFicha(scan_pos);
                
                while(isFilled(left(scan_pos))){
                    puntos += getFichaPuntuacion(scan_pos,false);
                    scan_pos = left(scan_pos);
                    
                    partial_word = getFicha(scan_pos) + partial_word;
                }
                
                Nodo pw_node = diccionario.buscarUltimoNodo(partial_word);
               // System.out.println(partial_word);
                if(pw_node != null){
                    
                   extend_right(partial_word, pw_node, pos, false);    
                 
                    
                }
            }
            else{
                int limit = 0;
                Position scan_pos = pos;
                puntos = getFichaPuntuacion(scan_pos,false);
                while(isEmpty(left(scan_pos)) && !anchors.contains(left(scan_pos))){
                    limit++;
                    scan_pos = left(scan_pos);
                }
                left_part("",diccionario.getRaiz(), pos, limit);
            }
        }
    }
        

}
