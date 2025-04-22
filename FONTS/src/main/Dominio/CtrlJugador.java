// Dominio/CtrlJugador.java
package Dominio;

import Dominio.Modelos.Jugador;
import Dominio.Excepciones.PasswordInvalidaException;
import Dominio.Excepciones.PuntuacionInvalidaException;
import Dominio.Excepciones.UsuarioNoEncontradoException;

/**
 * Gestiona solo el jugador activo, sin saber nada de persistencia.
 */
public class CtrlJugador {
    private Jugador jugadorActual;

    public CtrlJugador() {
        this.jugadorActual = null;
    }

    public boolean haySesion() {
        return jugadorActual != null;
    }

    public void setJugadorActual(Jugador j) {
        this.jugadorActual = j;
    }

    public void clearSesion() {
        this.jugadorActual = null;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public void cambiarPassword(String antigua, String nueva)
            throws PasswordInvalidaException {
        if (jugadorActual == null) return;
        if (!jugadorActual.validarPassword(antigua))
            throw new PasswordInvalidaException();
        jugadorActual.setPassword(nueva);
    }

    public void eliminarJugador(String password)
            throws PasswordInvalidaException {
        if (jugadorActual == null) return;
        if (!jugadorActual.validarPassword(password))
            throw new PasswordInvalidaException();
        clearSesion();
    }

    public void actualizarPuntuacion(int nuevosPuntos)
            throws  PuntuacionInvalidaException {


        if (nuevosPuntos < 0) {
            throw new PuntuacionInvalidaException(nuevosPuntos);
        }

        if (nuevosPuntos > jugadorActual.getPuntos()) {
            jugadorActual.setPuntos(nuevosPuntos);
        }
    }

}