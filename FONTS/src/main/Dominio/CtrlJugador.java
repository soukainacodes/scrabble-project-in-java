package Dominio;


import Dominio.Modelos.Jugador;

/**
 * Controlador de Jugador.
 * Este controlador gestiona la información del jugador actual y el segundo jugador en el juego.
 * 
 */
public class CtrlJugador {

    /**
     * Jugador actual.
     * Este jugador representa la sesión iniciada en el juego.
     */
    private Jugador jugadorActual;


    /**
     * Segundo jugador.
     * Este jugador representa al segundo jugador en el juego.
     */ 
    private Jugador segundoJugador;



    /**
     * Constructor de CtrlJugador.
     * Inicializa el jugador actual y el segundo jugador a null.
     */
    public CtrlJugador() {
        this.jugadorActual = null;
        this.segundoJugador = null;
    }


    /**
     * Establece el jugador actual.
     * 
     * @param nombre Nombre del jugador.
     */
    public void setJugadorActual(String nombre) {
        this.jugadorActual = new Jugador(nombre);
    }


    /**
     * Establece el segundo jugador.
     * 
     * @param nombre Nombre del segundo jugador.
     */
    public void setSegundoJugador(String nombre) {
        this.segundoJugador = new Jugador(nombre);
    }


    /**
     * Resetea el jugador actual.
     * Esto establece el jugador actual a null.
     */
    public void resetSegundoJugador(){
        this.segundoJugador = null;
    }

    
    /**
     * Obtiene el nombre del jugador actual.
     * 
     * @return Jugador actual.
     */
    public String getJugadorActual() {
        return jugadorActual.getNombre();
    }


    /**
     * Obtiene el nombre del segundo jugador.
     * 
     * @return Segundo jugador.
     */
    public String getSegundoJugador()
    {
        return segundoJugador.getNombre();
    }
    
    /**
     * Verifica si hay una sesión activa.
     * 
     * @return true si hay una sesión activa, false en caso contrario.
     */
    public boolean haySesion() {
        return jugadorActual != null;
    }

    /**
     * Limpia la sesión actual.
     * Esto establece el jugador actual y el segundo jugador a null.
     */
    public void clearSesion() {
        this.jugadorActual = null;
        this.segundoJugador = null;
    }

}
