/**
 * Controlador de persistencia de la aplicación.
 * Gestiona usuarios, ranking, partidas, diccionarios y bolsas de fichas.
 */
package Persistencia;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import Dominio.Excepciones.*;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;

public class CtrlPersistencia {
    private static final String FILE_USUARIOS = "FONTS/src/main/Persistencia/Datos/usuarios.txt";
    private static final String BASE_RECURSOS = "FONTS/src/main/Recursos/Idiomas";

    private final Map<String, Jugador> usuariosMap;
    private final NavigableSet<Jugador> rankingSet;
    private final Map<String, Partida> listaPartidas;
    private String ultimaPartida;

    private final Map<String, List<String>> diccionarios;
    private final Map<String, List<String>> bolsas;

    /**
     * Construye el controlador, cargando usuarios y recursos iniciales.
     */
    public CtrlPersistencia() {
        this.usuariosMap = cargarUsuariosDesdeDisco();
        this.rankingSet = new TreeSet<>(
                Comparator.comparingInt(Jugador::getPuntos).reversed()
                        .thenComparing(Jugador::getNombre));
        this.rankingSet.addAll(usuariosMap.values());
        this.listaPartidas = new HashMap<>();
        this.diccionarios = new HashMap<>();
        this.bolsas = new HashMap<>();
        this.ultimaPartida = null;
        cargarRecursosDesdeDisco();
    }

    // ─── Usuarios ─────────────────────────────────────────────────────────────

    /**
     * Obtiene un jugador por nombre.
     *
     * @param nombre nombre de usuario
     * @return el Jugador correspondiente
     * @throws UsuarioNoEncontradoException si no existe el usuario
     */
    public Jugador getJugador(String nombre) throws UsuarioNoEncontradoException {
        Jugador j = usuariosMap.get(nombre);
        if (j == null) {
            throw new UsuarioNoEncontradoException(nombre);
        }
        return j;
    }

    /**
     * Añade un nuevo jugador y actualiza el archivo de persistencia.
     * 
     * @param j jugador a añadir
     * @throws UsuarioYaRegistradoException si ya existe un usuario con ese nombre
     */
    public void addJugador(Jugador j) throws UsuarioYaRegistradoException {
        if (usuariosMap.containsKey(j.getNombre()))
            throw new UsuarioYaRegistradoException(j.getNombre());
        usuariosMap.put(j.getNombre(), j);
        rankingSet.add(j);
        guardarUsuariosEnDisco();
    }

    public boolean existeJugador(String nombre){
        Jugador j = usuariosMap.get(nombre);
         if (j == null) return false;
        return true;
    }

    /**
     * Actualiza los datos de un jugador existente.
     *
     * @param j jugador actualizado
     * @throws UsuarioNoEncontradoException si el jugador no está registrado
     */
    public void updateJugador(Jugador j) throws UsuarioNoEncontradoException {
        if (!usuariosMap.containsKey(j.getNombre())) {
            throw new UsuarioNoEncontradoException(j.getNombre());
        }

        rankingSet.remove(j);
        usuariosMap.put(j.getNombre(), j);
        rankingSet.add(j);
        guardarUsuariosEnDisco();
    }

    /**
     * Elimina un jugador y actualiza la persistencia.
     *
     * @param nombre nombre del jugador a eliminar
     * @throws UsuarioNoEncontradoException si no existe un jugador con ese nombre
     */
    public void removeJugador(String nombre) throws UsuarioNoEncontradoException {
        Jugador j = usuariosMap.remove(nombre);
        if (j == null) {
            throw new UsuarioNoEncontradoException(nombre);
        }

        rankingSet.remove(j);
        guardarUsuariosEnDisco();
    }

    /**
     * Reporta una nueva puntuación; si es mayor que la actual, la actualiza.
     *
     * @param nombre       nombre del jugador
     * @param nuevosPuntos nueva puntuación
     * @throws UsuarioNoEncontradoException si el jugador no está registrado
     * @throws PuntuacionInvalidaException  si la puntuación es negativa
     */
    public void reportarPuntuacion(String nombre, int nuevosPuntos)
            throws  PuntuacionInvalidaException {
        if (nuevosPuntos < 0) {
            throw new PuntuacionInvalidaException(nuevosPuntos);
        }

        Jugador j = usuariosMap.get(nombre);
        

        if (nuevosPuntos > j.getPuntos()) {
            rankingSet.remove(j);
            j.setPuntos(nuevosPuntos);
            rankingSet.add(j);
            guardarUsuariosEnDisco();
        }
    }

    // ─── Ranking ─────────────────────────────────────────────────────────────

    /**
     * Devuelve la lista ordenada de usuarios con puntos > 0.
     *
     * @return lista de pares (nombre, puntos)
     * @throws RankingVacioException si no hay ningún jugador con puntuación
     *                               positiva
     */
    public List<Map.Entry<String, Integer>> obtenerRanking()
            throws RankingVacioException {
        List<Map.Entry<String, Integer>> ranking = rankingSet.stream()
                .filter(j -> j.getPuntos() > 0)
                .map(j -> Map.entry(j.getNombre(), j.getPuntos()))
                .collect(Collectors.toList());

        if (ranking.isEmpty()) {
            throw new RankingVacioException();
        }

        return ranking;
    }

    /**
     * Obtiene la posición en el ranking de un usuario.
     * 
     * @param nombre nombre del usuario
     * @return posición (1-based)
     * @throws NoSuchElementException si no está en el ranking
     */
    public int getPosition(String nombre) {
        int pos = 1;
        for (Jugador j : rankingSet) {
            if (j.getNombre().equals(nombre))
                return pos;
            if (j.getPuntos() > 0)
                pos++;
        }
        throw new NoSuchElementException("Usuario no encontrado: " + nombre);
    }

    // ─── Partidas ────────────────────────────────────────────────────────────

    /**
     * Guarda una partida en memoria.
     *
     * @param id      identificador de la partida
     * @param partida objeto Partida a guardar
     * @throws PartidaYaExistenteException si ya existe una partida con el mismo ID
     */
    public void guardarPartida(String id, Partida partida)
            throws PartidaYaExistenteException {
        if (listaPartidas.containsKey(id)) {
            throw new PartidaYaExistenteException(id);
        }
        listaPartidas.put(id, partida);
        ultimaPartida = id;
    }

    /**
     * Carga una partida previamente guardada.
     *
     * @param id identificador de la partida
     * @return la Partida cargada
     * @throws PartidaNoEncontradaException si no existe ninguna partida con ese ID
     */
    public Partida cargarPartida(String id) throws PartidaNoEncontradaException {
        Partida p = listaPartidas.get(id);
        if (p == null) {
            throw new PartidaNoEncontradaException(id);
        }
        return p;
    }

    /**
     * Carga la última partida guardada.
     *
     * @return la última Partida
     * @throws NoHayPartidaGuardadaException si no hay ninguna partida guardada
     */
    public Partida cargarUltimaPartida() throws NoHayPartidaGuardadaException {
        if (ultimaPartida == null || !listaPartidas.containsKey(ultimaPartida)) {
            throw new NoHayPartidaGuardadaException();
        }
        return listaPartidas.get(ultimaPartida);
    }

    /**
     * Obtiene un mapa (no modificable) de todas las partidas guardadas.
     * 
     * @return mapa de id -> Partida
     */
    public Map<String, Partida> getListaPartidas() {
        return Collections.unmodifiableMap(listaPartidas);
    }

    /**
     * Elimina una partida guardada.
     *
     * @param id identificador de la partida
     * @throws PartidaNoEncontradaException si no existe la partida con ese ID
     */
    public void removePartida(String id) throws PartidaNoEncontradaException {
        if (!listaPartidas.containsKey(id)) {
            throw new PartidaNoEncontradaException(id);
        }

        listaPartidas.remove(id);
        if (id.equals(ultimaPartida)) {
            ultimaPartida = null;
        }
    }

    // ─── Diccionarios y Bolsas ───────────────────────────────────────────────

    /**
     * Carga desde disco los diccionarios y bolsas de la carpeta de recursos.
     */
    private void cargarRecursosDesdeDisco() {
        File base = new File(BASE_RECURSOS);
        if (!base.isDirectory())
            return;
        for (File dir : Objects.requireNonNull(base.listFiles(File::isDirectory))) {
            String id = dir.getName();
            try {
                File fd = new File(dir, id + "_diccionario.txt");
                if (fd.exists()) {
                    diccionarios.put(id, leerArchivoTexto(fd.getPath()));
                }
                File fb = new File(dir, id + "_bolsa.txt");
                if (fb.exists()) {
                    bolsas.put(id, leerArchivoTexto(fb.getPath()));
                }
            } catch (IOException ignored) {
                // ignorar recursos corruptos
            }
        }
    }

    /**
     * Devuelve los IDs de diccionarios disponibles.
     * 
     * @return conjunto de IDs
     */
    public Set<String> getDiccionarioIDs() {
        return diccionarios.keySet();
    }

    /**
     * Devuelve los IDs de bolsas disponibles.
     * 
     * @return conjunto de IDs
     */
    public Set<String> getBolsaIDs() {
        return bolsas.keySet();
    }

    /**
     * Obtiene un diccionario por ID.
     * 
     * @param id identificador de diccionario
     * @return lista de palabras
     * @throws DiccionarioNoEncontradoException si no existe el ID
     */
    public List<String> getDiccionario(String id)
            throws DiccionarioNoEncontradoException {
        List<String> datos = diccionarios.get(id);
        if (datos == null) {
            throw new DiccionarioNoEncontradoException(
                    String.format("Diccionario '%s' no encontrado.", id));
        }
        return Collections.unmodifiableList(datos);
    }

    /**
     * Obtiene una bolsa por ID.
     * 
     * @param id identificador de bolsa
     * @return lista de líneas de bolsa
     * @throws BolsaNoEncontradaException si no existe el ID
     */
    public List<String> getBolsa(String id)
            throws BolsaNoEncontradaException {
        List<String> datos = bolsas.get(id);
        if (datos == null) {
            throw new BolsaNoEncontradaException(
                    String.format("Bolsa '%s' no encontrada.", id));
        }
        return Collections.unmodifiableList(datos);
    }

    /**
     * Añade un nuevo diccionario y lo graba en disco.
     *
     * @param id       ID de recurso
     * @param palabras lista de palabras
     * @throws DiccionarioYaExistenteException si ya existe un diccionario con ese
     *                                         ID
     * @throws IOException                     si ocurre error de E/S al escribir
     */
    public void addDiccionario(String id, List<String> palabras)
            throws DiccionarioYaExistenteException, IOException {
        if (diccionarios.containsKey(id)) {
            throw new DiccionarioYaExistenteException(id);
        }

        File dir = new File(BASE_RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + dir.getPath());
        }

        File f = new File(dir, id + "_diccionario.txt");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (String p : palabras) {
                w.write(p + "\n");
            }
        }

        diccionarios.put(id, new ArrayList<>(palabras));
    }

    /**
     * Añade una nueva bolsa y la graba en disco.
     *
     * @param id        ID de recurso
     * @param bolsaData mapa letra->[cantidad, valor]
     * @throws BolsaYaExistenteException si ya existe una bolsa con ese ID
     * @throws IOException               si ocurre error de E/S al crear archivos
     */
    public void addBolsa(String id, Map<String, int[]> bolsaData)
            throws BolsaYaExistenteException, IOException {
        if (bolsas.containsKey(id)) {
            throw new BolsaYaExistenteException(id);
        }

        File dir = new File(BASE_RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear carpeta: " + dir.getPath());
        }

        File f = new File(dir, id + "_bolsa.txt");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (var e : bolsaData.entrySet()) {
                w.write(e.getKey() + " " + e.getValue()[0] + " " + e.getValue()[1] + "\n");
            }
        }

        // Cargamos la bolsa desde disco (como texto) para simular cómo se usaría luego
        bolsas.put(id, new ArrayList<>(leerArchivoTexto(f.getPath())));
    }

    /**
     * Elimina completamente el diccionario y la bolsa de un mismo ID.
     *
     * @param id ID del recurso (idioma)
     * @throws DiccionarioNoEncontradoException si no existe el diccionario con ese
     *                                          ID
     * @throws BolsaNoEncontradaException       si no existe la bolsa con ese ID
     * @throws IOException                      si ocurre cualquier error de E/S al
     *                                          borrar archivos
     */
    public void removeIdiomaCompleto(String id)
            throws DiccionarioNoEncontradoException,
            BolsaNoEncontradaException,
            IOException {
        // Comprobamos primero memoria
        if (!diccionarios.containsKey(id)) {
            throw new DiccionarioNoEncontradoException(
                    String.format("No existe ningún diccionario con ID '%s'.", id));
        }
        if (!bolsas.containsKey(id)) {
            throw new BolsaNoEncontradaException(
                    String.format("No existe ninguna bolsa con ID '%s'.", id));
        }

        // Eliminamos de las estructuras en memoria
        diccionarios.remove(id);
        bolsas.remove(id);

        // Ahora eliminamos de disco
        File dir = new File(BASE_RECURSOS, id);
        if (dir.exists()) {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                if (!f.delete()) {
                    throw new IOException("No se pudo borrar el archivo: " + f.getPath());
                }
            }
            if (!dir.delete()) {
                throw new IOException("No se pudo borrar la carpeta: " + dir.getPath());
            }
        }
    }

    // ─── Helpers de E/S ─────────────────────────────────────────────────────

    /**
     * Lee todas las líneas no vacías de un archivo de texto.
     * 
     * @param ruta ruta del archivo
     * @return lista de líneas
     * @throws IOException si hay error de lectura
     */
    private List<String> leerArchivoTexto(String ruta) throws IOException {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            for (String l; (l = br.readLine()) != null;) {
                l = l.trim();
                if (!l.isEmpty())
                    out.add(l);
            }
        }
        return out;
    }

    /**
     * Guarda todos los usuarios en disco.
     */
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

    /**
     * Carga usuarios desde el archivo de disco.
     * 
     * @return mapa nombre->Jugador
     */
    private Map<String, Jugador> cargarUsuariosDesdeDisco() {
        Map<String, Jugador> mapa = new HashMap<>();
        File f = new File(FILE_USUARIOS);
        if (!f.exists())
            return mapa;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            for (String línea; (línea = r.readLine()) != null;) {
                String[] t = línea.trim().split(" ");
                if (t.length < 3)
                    continue;
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
}
