// FONTS/src/main/Persistencia/CtrlPersistencia.java
package Persistencia;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
import Dominio.Excepciones.UsuarioYaRegistradoException;

/**
 * Controla persistencia de usuarios, ranking, partidas,
 * y además diccionarios y bolsas de fichas.
 */
public class CtrlPersistencia {
    private static final String FILE_USUARIOS =
        "FONTS/src/main/Persistencia/Datos/usuarios.txt";
    private static final String BASE_RECURSOS =
        "FONTS/src/main/Recursos/Idiomas";

    private final Map<String, Jugador> usuariosMap;
    private final NavigableSet<Jugador> rankingSet;
    private final Map<String, Partida> listaPartidas;
    private String ultimaPartida;

    // Nuevos: diccionarios y bolsas
    private final Map<String, List<String>> diccionarios;
    private final Map<String, Map<String,int[]>> bolsas;

    public CtrlPersistencia() {
        this.ultimaPartida = null;
        this.usuariosMap   = cargarUsuariosDesdeDisco();
        this.rankingSet    = new TreeSet<>(
            Comparator.comparingInt(Jugador::getPuntos).reversed()
                      .thenComparing(Jugador::getNombre)
        );
        this.rankingSet.addAll(usuariosMap.values());
        this.listaPartidas = new HashMap<>();
        this.diccionarios  = new HashMap<>();
        this.bolsas        = new HashMap<>();
        cargarRecursosDesdeDisco();
    }

    // ─── Persistencia de usuarios ─────────────────────────────────

    private Map<String, Jugador> cargarUsuariosDesdeDisco() {
        Map<String, Jugador> mapa = new HashMap<>();
        File f = new File(FILE_USUARIOS);
        if (!f.exists()) return mapa;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            for (String línea; (línea = r.readLine()) != null; ) {
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

    public List<Map.Entry<String,Integer>> obtenerRanking() {
        return rankingSet.stream()
                         .filter(j -> j.getPuntos() > 0)
                         .map(j -> Map.entry(j.getNombre(), j.getPuntos()))
                         .collect(Collectors.toList());
    }

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
    }

    public Partida cargarPartida(String id) {
        return listaPartidas.get(id);
    }

    public Partida cargarUltimaPartida() {
        return listaPartidas.get(ultimaPartida);
    }

    public Map<String,Partida> getListaPartidas() {
        return Collections.unmodifiableMap(listaPartidas);
    }

    public void removePartida(String id) {
        listaPartidas.remove(id);
        if (id.equals(ultimaPartida)) ultimaPartida = null;
    }

    // ─── Diccionarios y Bolsas ───────────────────────────────────

    private void cargarRecursosDesdeDisco() {
        File base = new File(BASE_RECURSOS);
        if (!base.isDirectory()) return;
        for (File dir : base.listFiles(File::isDirectory)) {
            String id = dir.getName();
            try {
                File fd = new File(dir, id + "_diccionario.txt");
                if (fd.exists()) {
                    diccionarios.put(id, leerArchivoTexto(fd.getPath()));
                }
                File fb = new File(dir, id + "_bolsa.txt");
                if (fb.exists()) {
                    bolsas.put(id, leerArchivoBolsa(fb.getPath()));
                }
            } catch (IOException ignored) {}
        }
    }

    /** IDs de diccionarios disponibles */
    public Set<String> getDiccionarioIDs() {
        return Collections.unmodifiableSet(diccionarios.keySet());
    }

    /** IDs de bolsas disponibles */
    public Set<String> getBolsaIDs() {
        return Collections.unmodifiableSet(bolsas.keySet());
    }

    /** Añade un nuevo diccionario y lo graba en disco */
    public void addDiccionario(String id, List<String> palabras) throws IOException {
        if (diccionarios.containsKey(id))
            throw new IOException("Diccionario ya existente: " + id);
        File dir = new File(BASE_RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException("No se pudo crear carpeta: " + dir);
        File f = new File(dir, id + "_diccionario.txt");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (String p : palabras) w.write(p + "\n");
        }
        diccionarios.put(id, new ArrayList<>(palabras));
    }

    /** Añade una nueva bolsa y la graba en disco */
    public void addBolsa(String id, Map<String,int[]> bolsaData) throws IOException {
        if (bolsas.containsKey(id))
            throw new IOException("Bolsa ya existente: " + id);
        File dir = new File(BASE_RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException("No se pudo crear carpeta: " + dir);
        File f = new File(dir, id + "_bolsa.txt");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (var e : bolsaData.entrySet()) {
                w.write(e.getKey() + " " + e.getValue()[0] + " " + e.getValue()[1] + "\n");
            }
        }
        bolsas.put(id, new LinkedHashMap<>(bolsaData));
    }

    /** Elimina por completo diccionario y bolsa de un mismo ID */
    public void removeIdiomaCompleto(String id) throws IOException {
        diccionarios.remove(id);
        bolsas.remove(id);
        File dir = new File(BASE_RECURSOS, id);
        if (dir.exists()) {
            for (File f : dir.listFiles()) f.delete();
            if (!dir.delete()) throw new IOException("No se pudo borrar carpeta: " + dir);
        }
    }

    // ─── Helpers lectura ────────────────────────────────────────

    private List<String> leerArchivoTexto(String ruta) throws IOException {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            for (String l; (l = br.readLine()) != null; ) {
                l = l.trim();
                if (!l.isEmpty()) out.add(l);
            }
        }
        return out;
    }

    private Map<String,int[]> leerArchivoBolsa(String ruta) throws IOException {
        Map<String,int[]> out = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            for (String l; (l = br.readLine()) != null; ) {
                l = l.trim();
                if (l.isEmpty() || l.startsWith("#")) continue;
                String[] t = l.split("\\s+");
                out.put(t[0], new int[]{ Integer.parseInt(t[1]), Integer.parseInt(t[2]) });
            }
        }
        return out;
    }
}
