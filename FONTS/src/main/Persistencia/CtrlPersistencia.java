package Persistencia;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
import Dominio.Excepciones.UsuarioYaRegistradoException;

/**
 * Controla persistencia de usuarios, ranking y partidas.
 * Mantiene un Map para acceso rápido y un NavigableSet para el ranking ordenado.
 */
public class CtrlPersistencia {
    private static final String FILE_USUARIOS =
        "FONTS/src/main/Persistencia/Datos/usuarios.txt";

    private final Map<String, Jugador> usuariosMap;
    private final NavigableSet<Jugador> rankingSet;
    private final Map<String, Partida> listaPartidas;
    private String ultimaPartida;
    public CtrlPersistencia() {
        this.ultimaPartida = null;
        this.usuariosMap   = cargarUsuariosDesdeDisco();
        // Comparator: puntos desc, nombre asc
        this.rankingSet    = new TreeSet<>(
            Comparator.comparingInt(Jugador::getPuntos).reversed()
                      .thenComparing(Jugador::getNombre)
        );
        this.rankingSet.addAll(usuariosMap.values());
        this.listaPartidas = new HashMap<>();
    }

    // ─── Persistencia de usuarios ─────────────────────────────────

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

    public Jugador getJugador(String nombre) {
        return usuariosMap.get(nombre);
    }

    public void addJugador(Jugador j) throws UsuarioYaRegistradoException {
        if (usuariosMap.containsKey(j.getNombre()))
            throw new UsuarioYaRegistradoException(j.getNombre());
        usuariosMap.put(j.getNombre(), j);
        rankingSet.add(j);
        guardarUsuariosEnDisco();
    }

    public void updateJugador(Jugador j) {
        // Si cambian puntos, actualizar rankingSet
        rankingSet.remove(j);
        usuariosMap.put(j.getNombre(), j);
        rankingSet.add(j);
        guardarUsuariosEnDisco();
    }

    public void removeJugador(String nombre) {
        Jugador j = usuariosMap.remove(nombre);
        if (j != null) {
            rankingSet.remove(j);
            guardarUsuariosEnDisco();
        }
    }

    /**
     * Actualiza la puntuación si es mayor, ajustando el rankingSet.
     */
    public void reportarPuntuacion(String nombre, int nuevosPuntos) {
        Jugador j = usuariosMap.get(nombre);
        if (j != null && nuevosPuntos > j.getPuntos()) {
            rankingSet.remove(j);
            j.setPuntos(nuevosPuntos);
            rankingSet.add(j);
            guardarUsuariosEnDisco();
        }
    }

    // ─── Ranking ─────────────────────────────────────────────────

    /**
     * Devuelve el ranking ordenado (nombre, puntos), solo puntuaciones > 0.
     */
    public List<Map.Entry<String,Integer>> obtenerRanking() {
        return rankingSet.stream()
                         .filter(j -> j.getPuntos() > 0)
                         .map(j -> Map.entry(j.getNombre(), j.getPuntos()))
                         .collect(Collectors.toList());
    }

    /**
     * Posición 1‑based en el ranking, lanza NoSuchElementException si no existe.
     */
    public int getPosition(String nombre) {
        int pos = 1;
        for (Jugador j : rankingSet) {
            if (j.getNombre().equals(nombre)) return pos;
            if (j.getPuntos() > 0) pos++;
        }
        throw new NoSuchElementException("Usuario no encontrado: " + nombre);
    }

    // ─── Partidas ────────────────────────────────────────────────

    public void guardarPartida(String id, Partida partida) {
        listaPartidas.put(id, partida);
        ultimaPartida = id;
       // System.out.println("[Persistencia] Partida '" + id + "' guardada.");
    }

    public Partida cargarPartida(String id) {
        return listaPartidas.get(id);
    }

    public Partida cargarUltimaPartida(){
        return listaPartidas.get(ultimaPartida);
    }
}
