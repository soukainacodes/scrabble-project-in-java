package Dominio.Modelos;

import java.util.ArrayList;
import java.util.List;


/**
 * Representa una partida de Scrabble con dos jugadores.
 * Gestiona turnos, fichas de cada jugador, puntuaciones, bolsa y tablero.
 */
public class Partida {

    private String idPartida;

    private String recursoPartida;

    private int partidaAcabada = 0;
    /** Fichas del jugador 1 en mano. */
    private List<Ficha> fichasJugador1;
    /** Fichas del jugador 2 en mano. */
    private List<Ficha> fichasJugador2;
    /** Puntos acumulados del jugador 1. */
    private int puntosJugador1;
    /** Puntos acumulados del jugador 2. */
    private int puntosJugador2;
    /** Turno actual: {@code true} para jugador 1, {@code false} para jugador 2. */
    private boolean turnoJugador;
    /** Bolsa común de fichas para la partida. */
    private Bolsa bolsa;
    /** Tablero de juego. */
    private Tablero tablero;
    /** Contador de turnos jugados. */
    private int contadorTurno;
    /** Coordenadas de la palabra en curso en el tablero. */
    private List<Pair<Integer,Integer>> coordenadasPalabra;

    /**
     * Constructor de la partida.
     * Inicializa los jugadores, la bolsa y el tablero.
     * 
     * @param id ID de la partida.
     * @param lineasArchivoBolsa Lista de líneas del archivo de la bolsa.
     * @param seed Semilla para la aleatoriedad de la bolsa.
     */
    public Partida(String id, List<String> lineasArchivoBolsa, long seed) {
        this.idPartida = id;
        this.partidaAcabada = 0;
        this.fichasJugador1 = new ArrayList<>();
        this.fichasJugador2 = new ArrayList<>();
        this.puntosJugador1 = 0;
        this.puntosJugador2 = 0;
        // Inicializar bolsa
        if (seed != 0) this.bolsa = new Bolsa(lineasArchivoBolsa, seed);
        else this.bolsa = new Bolsa(lineasArchivoBolsa);
        // Inicializar tablero y estado
        this.tablero = new Tablero();
        this.contadorTurno = 0;
        this.turnoJugador = true; //Empieza jugador 1
        this.coordenadasPalabra = new ArrayList<>();
        // Repartir 7 fichas alternando turnos
        for (int i = 0; i < 14; i++) {
            setFicha(bolsa.sacarFicha());
            turnoJugador = !turnoJugador;
        }
    }

    /**
     * Constructor de la partida.
     * Inicializa los jugadores, la bolsa y el tablero a partir de cadenas de texto.
     *
     * @param id ID de la partida.
     * @param bolsaTexto Texto con las fichas de la bolsa.
     * @param tableroTexto Texto con las fichas del tablero.
     */
    public Partida(String id, List<String> bolsaTexto, List<String> tableroTexto){
        this.fichasJugador1 = new ArrayList<>();
        this.fichasJugador2 = new ArrayList<>();
        this.puntosJugador1 = 0;
        this.puntosJugador2 = 0;
        this.idPartida = id;
        this.bolsa = new Bolsa(bolsaTexto);
        
        this.tablero = new Tablero();
        for (String posicion : tableroTexto) {
           
            String[] parts = posicion.split(" ");        
            String letra = parts[0];
            int puntuacion = Integer.parseInt(parts[1]);
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[3]);
            tablero.ponerFicha(new Ficha(letra, puntuacion), x, y); 
        }
        
        this.coordenadasPalabra = new ArrayList<>();
    }
    
    /**
     * Establece el ID de la partida.
     *
     * @param id ID de la partida.
     */
    public void setIdPartida(String id){
        this.idPartida = id;
    }


    /**
     * Establece las fichas del jugador 1 a partir de una cadena de texto.
     * La cadena debe contener pares de letra y puntuación separados por espacios.
     *
     * @param fichas Cadena con las fichas del jugador 1.
     */
    public void setFichasJugador1(String fichas){
        String[] fichasString = fichas.trim().split(" ");
        
        for(int letra = 0;letra < fichasString.length ; letra+=2){
            Ficha ficha = new Ficha(fichasString[letra],Integer.parseInt(fichasString[letra+1]));
            fichasJugador1.add(ficha);    
        }
    }

    
    /**
     * Obtiene el tablero de la partida.
     *
     * @return Tablero actual.
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Obtiene la bolsa de la partida.
     *
     * @return Bolsa actual.
     */
    public Bolsa getBolsa(){
        return bolsa;
    }


    /**
     * Recupera el recurso de la partida.
     * @return Recurso de la partida.
     */
    public String getRecursoPartida(){
        return recursoPartida;
    }

    /**
     * Establece el recurso de la partida.
     *
     * @param recurso Recurso de la partida.
     */
    public void setRecursoPartida(String recurso){
        recursoPartida = recurso;
    }

    /**
     * Obtiene el contador de turnos jugados.
     *
     * @return Número de turnos.
     */
    public int getContadorTurno() {
        return contadorTurno;
    }

    /**
     * Aumenta el contador de turnos jugados.
     */
    public void aumentarContador() {
        contadorTurno++;
    }

    /**
     * Establece el contador de turnos jugados.
     *
     * @param contador Número de turnos.
     */
    public void setContadorTurno(int contador){
        contadorTurno = contador;
    }

    /**
     * Obtiene las coordenadas de la palabra en curso.
     *
     * @return Lista de pares de coordenadas (x, y).
     */
    public List<Pair<Integer,Integer>> getCoordenadasPalabras() {
        return coordenadasPalabra;
    }

    /**
     * Limpia las coordenadas de la palabra en curso.
     */
    public void coordenadasClear() {
        coordenadasPalabra.clear();
    }

    /**
     * Añade una ficha al tablero en la posición dada y la registra para el turno.
     * Si se trata de un comodín ('#'), se usa para colocar la letra indicada.
     *
     * @param letra Letra o comodín a colocar.
     * @param x     Fila en el tablero.
     * @param y     Columna en el tablero.
     */
    public void añadirFicha(String letra, int x, int y) {
        if (!tablero.getCelda(x, y).estaOcupada()) {
            
            String[] parts = letra.trim().split(" ");
            Ficha ficha = getFichaString(parts[0]);

            if(parts.length > 1) {
                tablero.ponerComodin(parts[0], x, y);
                quitarFicha("#");
                coordenadasPalabra.add(Pair.createPair(x, y));
            }
            else if(ficha != null) {
                tablero.ponerFicha(ficha, x, y);
                quitarFicha(letra);
                coordenadasPalabra.add(Pair.createPair(x, y));
            }
                
            
        }
    }

    /**
     * Suma puntos al jugador cuyo turno es actual.
     *
     * @param pts Puntos a añadir.
     */
    public void addPuntos(int pts) {
        if (turnoJugador) puntosJugador1 += pts;
        else puntosJugador2 += pts;
    }

    /**
     * Quita la ficha del tablero en (x,y) y la devuelve a la mano del jugador.
     *
     * @param x Fila.
     * @param y Columna.
     * @return {@code true} si la ficha se retiró, {@code false} si estaba vacía.
     */
    public boolean quitarFichaTablero(int x, int y) {
        Ficha f = tablero.quitarFicha(x, y);
        if (f != null) {
            coordenadasPalabra.remove(Pair.createPair(x, y));
            if(f.getPuntuacion() == 0) {
                f = new Ficha("#", 0);
            }
            setFicha(f);
            return true;
        }
        return false;
    }

    /**
     * Añade una ficha a la mano del jugador actual.
     * @param ficha Ficha a añadir.
     */
    public void setFicha(Ficha ficha) {
        if (turnoJugador) fichasJugador1.add(ficha);
        else fichasJugador2.add(ficha);
    }

    /**
     * Obtiene el número de fichas en mano del jugador actual.
     * @return Tamaño de la lista de fichas.
     */
    public int getListSize() {
        return turnoJugador ? fichasJugador1.size() : fichasJugador2.size();
    }

    /**
     * Rellena las manos de ambos jugadores hasta 7 fichas desde la bolsa.
     * @return {@code true} si se pudieron reponer todas las fichas, {@code false} si la bolsa se vació.
     */
    public boolean recuperarFichas() {
        boolean ok = true;
        while (fichasJugador1.size() < 7 && !bolsa.isEmpty()) fichasJugador1.add(bolsa.sacarFicha());
        while (fichasJugador2.size() < 7 && !bolsa.isEmpty()) fichasJugador2.add(bolsa.sacarFicha());
        if (fichasJugador1.size() < 7 || fichasJugador2.size() < 7) ok = false;
        return ok;
    }

    /**
     * Cambia el turno del jugador actual.
     */
    public void cambiarTurnoJugador() {
        turnoJugador = !turnoJugador;
    }

    /**
     * Establece el turno del jugador actual.
     *
     * @param turno {@code true} para jugador 1, {@code false} para jugador 2.
     */
    public void setTurnoJugador(boolean turno) {
        turnoJugador = turno;
    }

    /**
     * Obtiene la ficha en índice {@code n} de la mano del jugador actual.
     *
     * @param n Índice de la ficha.
     * @return Objeto {@link Ficha} en la mano.
     */
    public Ficha getFicha(int n) {
        return turnoJugador ? fichasJugador1.get(n) : fichasJugador2.get(n);
    }

    /**
     * Obtiene la ficha con letra {@code s} de la mano del jugador actual.
     *
     * @param s Letra de la ficha a buscar.
     * @return Objeto {@link Ficha} correspondiente a la letra, o {@code null} si no existe.
     */
    public Ficha getFichaString(String s) {
        List<Ficha> mano = turnoJugador ? fichasJugador1 : fichasJugador2;
        for (Ficha f : mano) if (f.getLetra().equals(s)) return f;
        return null;
    }

    /**
     * Elimina la primera ocurrencia de la ficha con letra {@code s} de la mano actual.
     *
     * @param s Letra de la ficha a quitar.
     */
    public void quitarFicha(String s) {
        List<Ficha> mano = turnoJugador ? fichasJugador1 : fichasJugador2;
        for (int i = 0; i < mano.size(); i++) {
            if (mano.get(i).getLetra().equals(s)) {
                mano.remove(i);
                return;
            }
        }
    }

    /**
     * Verifica si la bolsa está vacía.
     *
     * @return {@code true} si no quedan fichas.
     */
    public boolean isBolsaEmpty() {
        return bolsa.isEmpty();
    }

    /**
     * Establece las fichas del jugador actual.
     *
     * @param fichas Lista de fichas a establecer.
     */
    public void setFichas(List<Ficha> fichas){
        if(turnoJugador) {
            fichasJugador1 = fichas;
        }
        else {
            fichasJugador2 = fichas;
        }
    }

    /**
     * Obtiene las letras de las fichas en mano del jugador actual.
     *
     * @return Lista de cadenas con las letras.
     */
    public List<String> obtenerFichas() {
        List<String> letras = new ArrayList<>();
        List<Ficha> mano = turnoJugador ? fichasJugador1 : fichasJugador2;
        for (Ficha f : mano) letras.add(f.getLetra() + " " + Integer.toString(f.getPuntuacion()));
        return letras;
    }

    /**
     * Obtiene el turno actual.
     *
     * @return {@code true} si es turno de jugador1, {@code false} de jugador2.
     */
    public boolean getTurnoJugador() {
        return turnoJugador;
    }

    /**
     * Bloquea en el tablero las celdas donde se colocaron fichas este turno.
     */
    public void bloquearCeldas() {
        for (Pair<Integer,Integer> p : coordenadasPalabra) 
            tablero.bloquearCelda(p.getFirst(), p.getSecond());
    }

    /**
     * Establece los puntos del jugador actual.
     *
     * @param puntos Nueva puntuación a asignar.
     */
    public void setPuntos(int puntos) {
        if (turnoJugador) puntosJugador1 = puntos;
        else puntosJugador2 = puntos;
    }

    /**
     * Establece los puntos de cada jugador.
     *
     * @param puntos Puntos del jugador 1.
     */
    public void setPuntosJugador1(int puntos) {
        this.puntosJugador1 = puntos;
    }

    /**
     * Establece los puntos de cada jugador.
     *
     * @param puntos Puntos del jugador 2.
     */
    public void setPuntosJugador2(int puntos) {
        this.puntosJugador2 = puntos;
    }

    /**
     * Obtiene la puntuación de jugador1.
     *
     * @return Puntos acumulados jugador1.
     */
    public int getPuntosJugador1() {
        return puntosJugador1;
    }

    /**
     * Obtiene la puntuación de jugador2.
     *
     * @return Puntos acumulados jugador2.
     */
    public int getPuntosJugador2() {
        return puntosJugador2;
    }

    /**
     * Obtiene la lista de fichas (objetos) del jugador actual.
     *
     * @return Lista de {@link Ficha}.
     */
    public List<Ficha> getFichasJugador() {
        return turnoJugador ? fichasJugador1 : fichasJugador2;
    }



    /**
     * Establece el estado de la partida como acabada.
     */
    public void setPartidaAcabada(){
            this.partidaAcabada = 1;
    }

    /**
     * Obtiene el estado de la partida.
     * @return {@code 0} si en curso, {@code 1} si acabada.
     */
    public int getPartidaAcabada(){
        return partidaAcabada;
    }


    /**
     * Obtiene el ID de la partida.
     *
     * @return ID de la partida.
     */ 
    public String getIdPartida() {
        return idPartida;
    }
}