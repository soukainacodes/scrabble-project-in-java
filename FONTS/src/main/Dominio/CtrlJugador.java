// Dominio/CtrlJugador.java
package Dominio;

import java.util.*;
import Persistencia.CtrlPersistencia;
import Dominio.Modelos.Jugador;

public class CtrlJugador {
    private Map<String, Jugador> jugadores;
    private CtrlPersistencia persistencia;

    /** Constructor principal: recibe la persistencia desde fuera */
    public CtrlJugador(CtrlPersistencia persistencia) {
        this.persistencia = persistencia;
        this.jugadores    = persistencia.cargarUsuarios();
    }

    /** Constructor por compatibilidad: crea su propia persistencia */
    public CtrlJugador() {
        this(new CtrlPersistencia());
    }

    // ————— resto sin cambios —————

    public boolean crearJugador(String nombre, String password) {
        if (jugadores.containsKey(nombre)) return false;
        Jugador j = new Jugador(nombre, password);
        jugadores.put(nombre, j);
        persistencia.guardarUsuarios(jugadores);
        return true;
    }

    public Jugador iniciarSesion(String nombre, String password) {
        Jugador j = jugadores.get(nombre);
        if (j != null && j.validarPassword(password)) return j;
        return null;
    }

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

    public boolean eliminarJugador(String nombre, String password) {
        Jugador j = jugadores.get(nombre);
        if (j != null && j.validarPassword(password)) {
            jugadores.remove(nombre);
            persistencia.guardarUsuarios(jugadores);
            return true;
        }
        return false;
    }

    public Jugador getJugador(String nombre) {
        return jugadores.get(nombre);
    }

    public Map<String, Jugador> getJugadores() {
        return jugadores;
    }
}
