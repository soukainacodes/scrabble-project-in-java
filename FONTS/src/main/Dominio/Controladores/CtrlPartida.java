import java.util.ArrayList;
import java.util.List;


import main.Dominio.Controladores.Modelos.Partida.Dawg.*;

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
       
       if(players.size() == 1){
        this.isAlgoritmo = true;
      //  this.algoritmo = new CtrlAlgoritmo(tablero, partidaActual.getFichasJugador2() , dawg);
       }
       else this.isAlgoritmo = false;
      
   }

    public void mostrarTablero(){
       
        partidaActual.mostrarTablero();
    }

    public void mostrarFichas(){

           System.out.println( partidaActual.mostrarFichas());
        
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
     else if(parts[0].contains("fin")){
        
           finTurno(); 
     }
     
    }
 
    //Funcion para añadir una ficha
    public void añadirFicha(int ficha, int x, int y){
        System.out.println(ficha);
        System.out.println(x);
        System.out.println(y);
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
        int puntos = validador.validarPalabra(coordenadasPalabra, dawg, tablero);
        if(puntos >= 0){
            coordenadasPalabra.clear();
            System.out.println("Palabra correcta");
            System.out.println("Puntos: " + puntos );
            partidaActual.cambiarTurnoJugador();
            contadorTurno++;
            if(isAlgoritmo) {
               // List<Pair<String,List<Pair<Integer,Integer>>>> lista = algoritmo.BuscarJugada();
               
        }
        else{
            System.out.println("Palabra incorrecta");
        }
    }

   

  
}
}
