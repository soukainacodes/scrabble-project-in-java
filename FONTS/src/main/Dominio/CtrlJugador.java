package Dominio;


import Dominio.Modelos.Jugador;


/**
 * Controlador de Jugador.
 * Este controlador gestiona la sesión del jugador actual y el segundo jugador.
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
     * @param password Contraseña del jugador.
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

    public void resetSegundoJugador(){
        this.segundoJugador = null;
    }

    
    /**
     * Obtiene el jugador actual.
     * 
     * @return Jugador actual.
     */
    public String getJugadorActual() {
        return jugadorActual.getNombre();
    }


    /**
     * Obtiene el segundo jugador.
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
    * Limpia la sesión del jugador actual y el segundo jugador.
    * Esto se utiliza para cerrar la sesión y reiniciar el estado del controlador.
    */
    public void clearSesion() {
        this.jugadorActual = null;
        this.segundoJugador = null;
    }



 
}
