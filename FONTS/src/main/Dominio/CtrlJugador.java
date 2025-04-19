// Dominio/CtrlJugador.java
package Dominio;

import java.util.*;
import Persistencia.CtrlPersistencia;
import Dominio.Modelos.Jugador;

public class CtrlJugador {
    private Map<String, Jugador> jugadores;
    private CtrlPersistencia persistencia;

    public CtrlJugador() {
        persistencia = new CtrlPersistencia();
        jugadores   = persistencia.cargarUsuarios();
    }

    /** Crea y persiste un nuevo jugador; false si ya existía */
    public boolean crearJugador(String nombre, String password) {
        if (jugadores.containsKey(nombre)) return false;
        Jugador j = new Jugador(nombre, password);
        jugadores.put(nombre, j);
        persistencia.guardarUsuarios(jugadores);
        return true;
    }

    /** Devuelve el Jugador si existe y la password coincide, o null */
    public Jugador iniciarSesion(String nombre, String password) {
        Jugador j = jugadores.get(nombre);
        if (j != null && j.validarPassword(password)) return j;
        return null;
    }

    /** Actualiza la puntuación de un jugador (sólo si es mayor que la anterior) */
    public void actualizarPuntuacion(String nombre, int nuevosPuntos) {
        Jugador j = jugadores.get(nombre);
        if (j != null && nuevosPuntos > j.getPuntos()) {
            j.setPuntos(nuevosPuntos);
            persistencia.guardarUsuarios(jugadores);
        }
    }


    public boolean cambiarPassword(String nombre, String antigua, String nueva) {
        Jugador j = jugadores.get(nombre);
        if (j != null && j.validarPassword(antigua)) {
            j.setPassword(nueva);
            persistencia.guardarUsuarios(jugadores);
            return true;
        }
        return false;
    }

    /** Elimina un usuario, si la contraseña coincide devuelve true */
    public boolean eliminarJugador(String nombre, String password) {
        Jugador j = jugadores.get(nombre);
        if (j != null && j.validarPassword(password)) {
            jugadores.remove(nombre);
            persistencia.guardarUsuarios(jugadores);
            return true;
        }
        return false;
    }

        /**
     * Devuelve el objeto Jugador a
     * sociado a este nombre,
     * o null si no existe.
     */
    public Jugador getJugador(String nombre) {
        return jugadores.get(nombre);
    }


    public Map<String, Jugador> getJugadores() {
        return jugadores;
    }
}
