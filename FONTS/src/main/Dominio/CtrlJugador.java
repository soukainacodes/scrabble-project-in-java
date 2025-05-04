package Dominio;

import Dominio.Excepciones.PasswordInvalidaException;
import Dominio.Excepciones.PuntuacionInvalidaException;
import Dominio.Modelos.Jugador;

/**
 * Controlador de jugador que gestiona la sesión y los datos del jugador activo,
 * sin implicar detalles de persistencia.
 * <p>
 * Permite iniciar y cerrar sesión, cambiar la contraseña,
 * eliminar al jugador activo y actualizar su puntuación máxima.
 */
public class CtrlJugador {
    private Jugador jugadorActual;
    private Jugador segundoJugador;
    /**
     * Crea un controlador de jugador sin sesión iniciada.
     */
    public CtrlJugador() {
        this.jugadorActual = null;
        this.segundoJugador = null;
    }

    public Jugador setNuevoJugador(String nombre, String password) {
        return new Jugador(nombre,password);
    }

    /**
     * Comprueba si hay un jugador con sesión iniciada.
     *
     * @return {@code true} si existe un jugador activo, {@code false} en caso contrario.
     */
    public boolean haySesion() {
        return jugadorActual != null;
    }

    /**
     * Establece el jugador actual en sesión.
     *
     * @param nombre el nombre del jugador.
     * @param password la contraseña del jugador.
     * @throws PasswordInvalidaException si la contraseña es inválida.
     */
    public void setJugador(String nombre, String password) throws PasswordInvalidaException {
        // Crear un nuevo objeto Jugador con el nombre y la contraseña proporcionados
        Jugador j = new Jugador(nombre, password);

        // Validar la contraseña
        if (j.getPassword() == null ? password != null : !j.getPassword().equals(password)) {
            throw new PasswordInvalidaException();
        }

        // Asignar el jugador actual o el segundo jugador
        if (this.jugadorActual == null) {
            this.jugadorActual = j;
        } else {
            this.segundoJugador = j;
        }
    }

    /**
     * Finaliza la sesión actual, dejando sin jugador activo.
     */
    public void clearSesion() {
        this.jugadorActual = null;
    }

    /**
     * Obtiene el jugador actualmente en sesión.
     *
     * @return el {@link Jugador} activo, o {@code null} si no hay sesión iniciada.
     */
    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public void cambiarPassword(String antigua, String nueva) throws PasswordInvalidaException {
        if(jugadorActual.getPassword() != antigua) {
            throw new PasswordInvalidaException();
        } 
        else jugadorActual.setPassword(nueva);
    }

   
    /**
     * Actualiza la puntuación máxima del jugador activo si la nueva puntuación es mayor.
     *
     * @param nuevosPuntos la puntuación obtenida a comprobar.
     * @throws PuntuacionInvalidaException si la puntuación proporcionada es negativa.
     */
    public void actualizarPuntuacion(int nuevosPuntos)
            throws PuntuacionInvalidaException {
        if (nuevosPuntos < 0) {
            throw new PuntuacionInvalidaException(nuevosPuntos);
        }
        if (jugadorActual != null && nuevosPuntos > jugadorActual.getPuntos()) {
            jugadorActual.setPuntos(nuevosPuntos);
        }
    }
}
