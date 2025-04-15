package Dominio;
import java.util.ArrayList;
import java.util.List;


import Dominio.Modelos.*;

public class CtrlPartida {
    
    private Partida partidaActual;
    private Dawg dawg;
    private Validador validador;
    private boolean finTurno;
    private int contadorTurno;
    private Tablero tablero;
    private Bolsa bolsa;
    private boolean isAlgoritmo;
    private Algoritmo algoritmo;
    private List<Pair<Integer,Integer>> coordenadasPalabra;


    // Constructor
    public CtrlPartida() {
        this.coordenadasPalabra = new ArrayList<>();
        
        this.validador = new Validador();
        
        //Dar fichas para jugador 1
        //Dar fichas para jugador 2
       
 
    }

    public void crearPartida(int modo, List<String> players, List<String> lineasArchivo,  List<String> lineasArchivoBolsa){
        this.dawg = new Dawg();
        dawg.cargarFichasValidas(lineasArchivoBolsa);
        dawg.construirDesdeArchivo(lineasArchivo);
      
       this.tablero = new Tablero();
       this.bolsa = new Bolsa(lineasArchivoBolsa);
       this.partidaActual = new Partida(players, tablero, bolsa);
       this.finTurno = false;
       this.contadorTurno = 0;
       
       if(modo == 1){
        this.isAlgoritmo = true;
        this.algoritmo = new Algoritmo();
       }
       else this.isAlgoritmo = false;
      
   }


    
    public List<String> obtenerFichas(){
        return partidaActual.obtenerFichas();
    }

    
    

    public void cargarPartida(){

    }

    public void guardarPartida(){

    }
    
    public void jugarScrabble(String input){
        String[] parts = input.split(" ");
        System.out.println(parts[0]);
        if(parts[0].contains("set") ){
            
           int ficha = Integer.parseInt(parts[1]);
           int x = Integer.parseInt(parts[2]);
           int y = Integer.parseInt(parts[3]);
           añadirFicha(ficha,x,y);
           
     }
     else if(parts[0].contains("get")){
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
           quitarFicha(x,y);
     }
     else if(parts[0].contains("reset")){

     }
     else if(parts[0].contains("pasar")){
        for( Pair p : coordenadasPalabra){
           // quitarFicha(p.getFirst(),p.getSecond());
        }
        finTurno(); 
     }
     else if(parts[0].contains("fin")){
        
           finTurno(); 
     }
     
    }
 
    //Funcion para añadir una ficha
    public void añadirFicha(int ficha, int x, int y){
     
        if(!tablero.getCelda(x, y).estaOcupada()){
            coordenadasPalabra.add(Pair.createPair(x,y));
            tablero.ponerFicha(partidaActual.getFicha(ficha), x, y);
        }
        
    }
    
    //Funcion para quitar una ficha
    public boolean quitarFicha(int x, int y){
        Ficha f = tablero.quitarFicha(x,y);
        if(f != null){
            coordenadasPalabra.remove(Pair.createPair(x,y));
            partidaActual.setFicha(f);
            return true;
        }
        return false;
          
    }

 

   
    public void finTurno(){
       
         
        int puntos = validador.validarPalabra(coordenadasPalabra, dawg, tablero, contadorTurno);
        if(puntos != 0){
            
            
            System.out.println("Palabra correcta");
            for(Pair<Integer,Integer> k : coordenadasPalabra ){
                 System.out.println(k.getFirst() + k.getSecond());
            }
            partidaActual.cambiarTurnoJugador();
            contadorTurno++;
            if(isAlgoritmo) {
               
            // ALEX partidaActual.mostrarFichas();
            List<Pair<Ficha,Pair<Integer, Integer>>> s = algoritmo.find_all_words(partidaActual.getFichasJugador2() , dawg, tablero);
           
            // ALEX System.out.println(partidaActual.mostrarFichas());
            for (Pair<Ficha,Pair<Integer, Integer>> aa : s){
                int i = -1;
                for(Ficha f : partidaActual.getFichasJugador2()){
                    i++;
                    if(f.equals(aa.getFirst())) break;
                }
                añadirFicha(i,aa.getSecond().getFirst(), aa.getSecond().getSecond());
                
            }
            partidaActual.cambiarTurnoJugador();
            }

            partidaActual.recuperarFichas();
            
           coordenadasPalabra.clear();
            System.out.println(coordenadasPalabra.size());
        }
        else{
            System.out.println("Palabra incorrecta");
        }
    }

   
    public Tablero obtenerTablero()
    {
        return tablero;
    }

  

}
