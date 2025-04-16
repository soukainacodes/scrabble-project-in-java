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
  


    // Constructor
    public CtrlPartida() {
        
        
        this.validador = new Validador();
        
        //Dar fichas para jugador 1
        //Dar fichas para jugador 2
       
 
    }

    public void crearPartida(int modo, List<String> players, List<String> lineasArchivo,  List<String> lineasArchivoBolsa){
        this.dawg = new Dawg();
        dawg.cargarFichasValidas(lineasArchivoBolsa);
        dawg.construirDesdeArchivo(lineasArchivo);
    
       this.partidaActual = new Partida(players, lineasArchivoBolsa);
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
       
        if(parts[0].contains("set") ){
            
           String ficha = parts[1];
           int x = Integer.parseInt(parts[2]);
           int y = Integer.parseInt(parts[3]);
           partidaActual.añadirFicha(ficha, x, y);
           
     }
     else if(parts[0].contains("get")){
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
           quitarFicha(x,y);
     }
     else if(parts[0].contains("reset")){

     }
     else if(parts[0].contains("pasar")){
        //for( Pair p : coordenadasPalabra){
           // quitarFicha(p.getFirst(),p.getSecond());
      //  }
        //finTurno(); 
     }
     else if(parts[0].contains("fin")){
        
           finTurno(); 
     }
     
    }
 
    //Funcion para añadir una ficha
    public void añadirFicha(String ficha, int x, int y){
        
    }
    
    //Funcion para quitar una ficha
    public boolean quitarFicha(int x, int y){
       return partidaActual.quitarFichaTablero(x,y);
    }

 

   
    public void finTurno(){
       
       System.out.println("aa");
         
        int puntos = validador.validarPalabra(partidaActual.getCoordenadasPalabras(), dawg, partidaActual.getTablero(), contadorTurno);
        if(puntos != 0){
            partidaActual.addPuntos(puntos);
            
            System.out.println("Palabra correcta");
           
            partidaActual.cambiarTurnoJugador();
            contadorTurno++;
            if(isAlgoritmo) {
                partidaActual.addPuntos(jugarAlgoritmo());
                partidaActual.cambiarTurnoJugador();
            }

            partidaActual.recuperarFichas();
            
           
           partidaActual.coordenadasClear();
            
        }
        else{
            System.out.println("Palabra incorrecta");
        }
        System.out.println("Jugador 1:" + partidaActual.getPuntosJugador1());
        System.out.println("Jugador 2:" + partidaActual.getPuntosJugador2());
    }

    private int jugarAlgoritmo(){
              
            // ALEX partidaActual.mostrarFichas();
            Pair<List<Pair<Ficha,Pair<Integer, Integer>>>, Integer> ss = algoritmo.find_all_words(partidaActual.getFichasJugador2() , dawg, partidaActual.getTablero());
            List<Pair<Ficha,Pair<Integer, Integer>>> s = ss.getFirst();
            // ALEX System.out.println(partidaActual.mostrarFichas());
            for (Pair<Ficha,Pair<Integer, Integer>> aa : s){
                System.out.println(aa.getFirst().getLetra());

                partidaActual.añadirFicha(aa.getFirst().getLetra(),aa.getSecond().getFirst(), aa.getSecond().getSecond());
                
            }
            
    return ss.getSecond();
    }
   
    public Tablero obtenerTablero()
    {
        return partidaActual.getTablero();
    }

  

}
