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

    /**
     * Crea un controlador de jugador sin sesión iniciada.
     */
    public CtrlJugador() {
        this.jugadorActual = null;
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
     * @param j el objeto {@link Jugador} que se convertirá en el jugador activo.
     */
    public void setJugadorActual(Jugador j) {
        this.jugadorActual = j;
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
