// Persistencia/CtrlPersistencia.java
package Persistencia;

import java.io.*;
import java.util.*;

import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
import Dominio.Excepciones.UsuarioYaRegistradoException;

public class CtrlPersistencia {
    private static final String FILE_USUARIOS =
        "FONTS/src/main/Persistencia/Datos/usuarios.txt";

    private final Map<String, Jugador> usuariosMap;
    private final Map<String, Partida> listaPartidas;

    public CtrlPersistencia() {
        this.usuariosMap   = cargarUsuariosDesdeDisco();
        this.listaPartidas = new HashMap<>();
    }

    private Map<String, Jugador> cargarUsuariosDesdeDisco() {
        Map<String, Jugador> mapa = new HashMap<>();
        File f = new File(FILE_USUARIOS);
        if (!f.exists()) return mapa;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String línea;
            while ((línea = r.readLine()) != null) {
                String[] t = línea.trim().split(" ");
                if (t.length < 3) continue;
                String nombre = t[0], pass = t[1];
                int puntos = Integer.parseInt(t[2]);
                Jugador j = new Jugador(nombre, pass);
                j.setPuntos(puntos);
                mapa.put(nombre, j);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("[Persistencia] Error cargando usuarios: " + e.getMessage());
        }
        return mapa;
    }

    private void guardarUsuariosEnDisco() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE_USUARIOS))) {
            for (Jugador j : usuariosMap.values()) {
                w.write(j.getNombre() + " " + j.getPassword() + " " + j.getPuntos());
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[Persistencia] Error guardando usuarios: " + e.getMessage());
        }
    }

    // Usuarios
    public Map<String, Jugador> getAllUsuarios() {
        return Collections.unmodifiableMap(usuariosMap);
    }

    public Jugador getJugador(String nombre) {
        return usuariosMap.get(nombre);
    }

    public void addJugador(Jugador j) throws UsuarioYaRegistradoException {
        if (usuariosMap.containsKey(j.getNombre()))
            throw new UsuarioYaRegistradoException(j.getNombre());
        usuariosMap.put(j.getNombre(), j);
        guardarUsuariosEnDisco();
    }

    public void updateJugador(Jugador j) {
        usuariosMap.put(j.getNombre(), j);
        guardarUsuariosEnDisco();
    }

    public void removeJugador(String nombre) {
        usuariosMap.remove(nombre);
        guardarUsuariosEnDisco();
    }

    // Partidas
    public void guardarPartida(String id, Partida partida) {
        listaPartidas.put(id, partida);
        System.out.println("[Persistencia] Partida '" + id + "' guardada.");
    }

    public Partida cargarPartida(String id) {
        return listaPartidas.get(id);
    }
}
