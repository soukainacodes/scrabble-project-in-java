package Persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import Dominio.Excepciones.BolsaNoEncontradaException;
import Dominio.Excepciones.BolsaYaExistenteException;
import Dominio.Excepciones.DiccionarioNoEncontradoException;
import Dominio.Excepciones.DiccionarioYaExistenteException;
import Dominio.Excepciones.NoHayPartidaGuardadaException;
import Dominio.Excepciones.PartidaNoEncontradaException;
import Dominio.Excepciones.PartidaYaExistenteException;
import Dominio.Excepciones.PuntuacionInvalidaException;
import Dominio.Excepciones.RankingVacioException;
import Dominio.Excepciones.UsuarioNoEncontradoException;
import Dominio.Excepciones.UsuarioYaRegistradoException;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;

/**
 * Controlador de persistencia de la aplicación. Gestiona:
 * <ul>
 * <li>Usuarios y su archivo de datos.</li>
 * <li>Ranking de jugadores.</li>
 * <li>Partidas guardadas en memoria.</li>
 * <li>Diccionarios y bolsas de fichas desde recursos.</li>
 * </ul>
 */
public class CtrlPersistencia {

    /**
     * Ruta al archivo de datos de usuarios.
     */
    private static final String FILE_USUARIOS = "FONTS/src/main/Persistencia/Datos/usuarios.txt";
    /**
     * Ruta base de recursos de diccionarios y bolsas.
     */
    private static final String BASE_RECURSOS = "FONTS/src/main/Recursos/Idiomas";
    private static final String PARTIDAS = "FONTS/src/main/Persistencia/Datos/Partidas/";

    /**
     * Mapa nombre->[password, puntos] cargado desde disco.
     */
    private final Map<String, String[]> usuariosMap;

    /**
     * Conjunto ordenado de jugadores para el ranking (puntos desc, nombre asc).
     */
    private final NavigableSet<Map.Entry<String, Integer>> rankingSet;

    /**
     * Identificador de la última partida guardada.
     */
    private String ultimaPartida;

    /**
     * Map de diccionario de palabras por ID de idioma.
     */
    private final Map<String, List<String>> diccionarios;
    /**
     * Map de líneas de bolsa por ID de idioma.
     */
    private final Map<String, List<String>> bolsas;

    /**
     * Construye el controlador, cargando usuarios desde disco, inicializando
     * ranking y recargando recursos de diccionarios y bolsas.
     */
    public CtrlPersistencia() {
        this.usuariosMap = cargarUsuariosDesdeDisco();
        this.rankingSet = new TreeSet<>(
                Comparator.comparingInt((Map.Entry<String, Integer> e) -> e.getValue()).reversed()
                        .thenComparing(Map.Entry::getKey));
        this.rankingSet.addAll(usuariosMap.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), Integer.parseInt(e.getValue()[1])))
                .collect(Collectors.toList()));
        this.diccionarios = new HashMap<>();
        this.bolsas = new HashMap<>();
        this.ultimaPartida = null;
        cargarRecursosDesdeDisco();
    }

    // ─── Usuarios ─────────────────────────────────────────────────────────────
    /**
     * Obtiene un jugador registrado por su nombre.
     *
     * @param nombre nombre de usuario buscado
     * @return el objeto Jugador correspondiente
     * @throws UsuarioNoEncontradoException si no existe un usuario con dicho
     *                                      nombre
     */
    public String getJugador(String nombre) throws UsuarioNoEncontradoException {
        if (!usuariosMap.containsKey(nombre)) {
            throw new UsuarioNoEncontradoException(nombre);
        }
        return nombre;
    }

    /**
     * Añade un nuevo jugador y persiste el cambio en disco.
     *
     * @param nombre   nombre del jugador
     * @param password contraseña del jugador
     * @throws UsuarioYaRegistradoException si el nombre ya está en uso
     */
    public void addJugador(String nombre, String password) throws UsuarioYaRegistradoException {
        if (usuariosMap.containsKey(nombre)) {
            throw new UsuarioYaRegistradoException(nombre);
        }
        usuariosMap.put(nombre, new String[] { password, "0" });
        rankingSet.add(Map.entry(nombre, 0));
        guardarUsuariosEnDisco();
    }

    /**
     * Verifica la existencia de un usuario.
     *
     * @param nombre nombre de usuario
     * @return true si existe, false en caso contrario
     */
    public boolean existeJugador(String nombre) {
        return usuariosMap.containsKey(nombre);
    }

    public static void escribirListaEnNuevoArchivo(List<String> datos) {

        String nombreArchivo = "partida_" + datos.get(0) + ".txt";
        String directorioDestino = PARTIDAS;

        if (!directorioDestino.endsWith(System.getProperty("file.separator"))) {
            directorioDestino += System.getProperty("file.separator");
        }

        String rutaCompleta = directorioDestino + nombreArchivo;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta))) {
            for (String linea : datos) {
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("Archivo creado exitosamente en: " + rutaCompleta);
        } catch (IOException e) {
            System.err.println("[EscrituraArchivo] Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * Actualiza los puntos de un jugador y persiste el cambio en disco.
     *
     * @param nombre       nombre del jugador
     * @param nuevosPuntos nuevos puntos del jugador
     * @throws UsuarioNoEncontradoException si no existe un usuario con dicho
     *                                      nombre
     */
    public void updateJugador(String nombre, int nuevosPuntos) throws UsuarioNoEncontradoException {
        if (!usuariosMap.containsKey(nombre)) {
            throw new UsuarioNoEncontradoException(nombre);
        }

        // Actualizar los puntos en el mapa
        String[] datosUsuario = usuariosMap.get(nombre);
        datosUsuario[1] = String.valueOf(nuevosPuntos);
        usuariosMap.put(nombre, datosUsuario);

        // Actualizar el ranking
        rankingSet.removeIf(entry -> entry.getKey().equals(nombre));
        rankingSet.add(Map.entry(nombre, nuevosPuntos));

        // Guardar los cambios en disco
        guardarUsuariosEnDisco();
    }

    /**
     * Elimina un jugador y actualiza la persistencia.
     *
     * @param nombre nombre del jugador a eliminar
     * @throws UsuarioNoEncontradoException si no existe un usuario con ese nombre
     */
    public void removeJugador(String nombre) throws UsuarioNoEncontradoException {
        // Eliminar al jugador del mapa de usuarios
        String[] datosUsuario = usuariosMap.remove(nombre);
        if (datosUsuario == null) {
            throw new UsuarioNoEncontradoException(nombre);
        }

        // Eliminar al jugador del ranking
        rankingSet.removeIf(entry -> entry.getKey().equals(nombre));

        // Guardar los cambios en disco
        guardarUsuariosEnDisco();
    }

    /**
     * Reporta una nueva puntuación para un jugador, y actualiza el ranking si
     * supera su récord.
     *
     * @param nombre       nombre del jugador
     * @param nuevosPuntos nueva puntuación
     * @throws PuntuacionInvalidaException si la puntuación es negativa
     */
    public void reportarPuntuacion(String nombre, int nuevosPuntos)
            throws PuntuacionInvalidaException {
        if (nuevosPuntos < 0) {
            throw new PuntuacionInvalidaException(nuevosPuntos);
        }

        // Verificar si el jugador existe en el mapa
        String[] datosUsuario = usuariosMap.get(nombre);
        if (datosUsuario != null) {
            int puntosActuales = Integer.parseInt(datosUsuario[1]);
            if (nuevosPuntos > puntosActuales) {
                // Actualizar los puntos en el mapa
                datosUsuario[1] = String.valueOf(nuevosPuntos);
                usuariosMap.put(nombre, datosUsuario);

                // Actualizar el ranking
                rankingSet.removeIf(entry -> entry.getKey().equals(nombre));
                rankingSet.add(Map.entry(nombre, nuevosPuntos));

                // Guardar los cambios en disco
                guardarUsuariosEnDisco();
            }
        }
    }

    // ─── Ranking ─────────────────────────────────────────────────────────────
    /**
     * Devuelve el ranking de jugadores con puntos positivos.
     *
     * @return lista de pares (nombre, puntos) ordenada
     * @throws RankingVacioException si no hay jugadores con >0 puntos
     */
    public List<Map.Entry<String, Integer>> obtenerRanking()
            throws RankingVacioException {
        List<Map.Entry<String, Integer>> ranking = rankingSet.stream()
                .filter(entry -> entry.getValue() > 0) // Filtrar jugadores con puntos > 0
                .collect(Collectors.toList());
        if (ranking.isEmpty()) {
            throw new RankingVacioException();
        }
        return ranking;
    }

    /**
     * Obtiene la posición 1-based de un jugador en el ranking.
     *
     * @param nombre nombre del jugador
     * @return posición en el ranking
     * @throws NoSuchElementException si el jugador no está en el ranking
     */
    public int getPosition(String nombre) {
        int pos = 1;
        for (Map.Entry<String, Integer> entry : rankingSet) {
            if (entry.getKey().equals(nombre)) {
                return pos;
            }
            if (entry.getValue() > 0) {
                pos++;
            }
        }
        throw new NoSuchElementException("Usuario no encontrado: " + nombre);
    }

    // ─── Partidas ────────────────────────────────────────────────────────────

    /**
     * Guarda una partida en memoria y marca como última.
     *
     * @param id      identificador único de la partida
     * @param partida lista de datos de la partida
     * @throws PartidaYaExistenteException si ya existe una partida con ese ID
     */
    public void guardarPartida(String id, List<String> partida)
            throws PartidaYaExistenteException {
        File archivoPartida = new File(PARTIDAS + "partida_" + id + ".txt");

        // Verificar si el archivo ya existe
        if (archivoPartida.exists()) {
            throw new PartidaYaExistenteException(id);
        }

        // Actualizar la última partida
        ultimaPartida = id;

        // Escribir la partida en un nuevo archivo
        escribirListaEnNuevoArchivo(partida);
    }

    /**
     * Carga una partida previamente guardada.
     *
     * @param id identificador de la partida
     * @return la instancia de Partida
     * @throws PartidaNoEncontradaException si no existe dicho ID
     */
    public List<String> cargarPartida(String id) throws PartidaNoEncontradaException {
        String rutaArchivo = PARTIDAS + "partida_" + id + ".txt";
        try {
            return leerArchivoTexto(rutaArchivo);
        } catch (Exception e) {
            throw new PartidaNoEncontradaException(id);
        }
    }

    /**
     * Carga la última partida guardada en memoria.
     *
     * @return lista de líneas de la última partida almacenada
     * @throws NoHayPartidaGuardadaException si no hay ninguna partida reciente
     */
    public List<String> cargarUltimaPartida() throws NoHayPartidaGuardadaException {
        if (ultimaPartida == null) {
            throw new NoHayPartidaGuardadaException();
        }

        String rutaArchivo = PARTIDAS + "partida_" + ultimaPartida + ".txt";
        File archivoPartida = new File(rutaArchivo);

        // Verificar si el archivo de la última partida existe
        if (!archivoPartida.exists()) {
            throw new NoHayPartidaGuardadaException();
        }

        try {
            // Leer y devolver las líneas del archivo
            return leerArchivoTexto(rutaArchivo);
        } catch (IOException e) {
            throw new NoHayPartidaGuardadaException();
        }
    }

    /**
     * Devuelve un mapa no modificable de todas las partidas guardadas.
     *
     * @return mapa id->contenido de la partida como lista de líneas
     */
    public Map<String, List<String>> getListaPartidas() {
        Map<String, List<String>> partidas = new HashMap<>();
        File directorioPartidas = new File(PARTIDAS);

        // Verificar si el directorio de partidas existe
        if (directorioPartidas.exists() && directorioPartidas.isDirectory()) {
            for (File archivo : Objects.requireNonNull(directorioPartidas.listFiles())) {
                if (archivo.isFile() && archivo.getName().startsWith("partida_")) {
                    String id = archivo.getName().replace("partida_", "").replace(".txt", "");
                    try {
                        partidas.put(id, leerArchivoTexto(archivo.getPath()));
                    } catch (IOException e) {
                        System.err.println("[Persistencia] Error al leer la partida: " + archivo.getName());
                    }
                }
            }
        }

        return Collections.unmodifiableMap(partidas);
    }

    /**
     * Elimina la partida con el ID dado.
     *
     * @param id identificador de la partida
     * @throws PartidaNoEncontradaException si no existe el ID
     */
    public void removePartida(String id) throws PartidaNoEncontradaException {
        String rutaArchivo = PARTIDAS + "partida_" + id + ".txt";
        File archivoPartida = new File(rutaArchivo);

        // Verificar si el archivo existe
        if (!archivoPartida.exists()) {
            throw new PartidaNoEncontradaException(id);
        }

        // Eliminar el archivo
        if (!archivoPartida.delete()) {
            throw new PartidaNoEncontradaException("No se pudo eliminar la partida con ID: " + id);
        }

        // Actualizar la última partida si corresponde
        if (id.equals(ultimaPartida)) {
            ultimaPartida = null;
        }
    }

    // ─── Diccionarios y Bolsas ───────────────────────────────────────────────
    /**
     * Carga desde disco todos los recursos de idiomas disponibles.
     */
    private void cargarRecursosDesdeDisco() {
        File base = new File(BASE_RECURSOS);
        if (!base.isDirectory()) {
            return;
        }
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
                // Ignorar recursos corruptos
            }
        }
    }

    /**
     * Obtiene los IDs de todos los diccionarios cargados.
     *
     * @return conjunto de IDs de diccionarios
     */
    public Set<String> getDiccionarioIDs() {
        return diccionarios.keySet();
    }

    /**
     * Obtiene los IDs de todas las bolsas cargadas.
     *
     * @return conjunto de IDs de bolsas
     */
    public Set<String> getBolsaIDs() {
        return bolsas.keySet();
    }

    /**
     * Recupera un diccionario en memoria por su ID.
     *
     * @param id identificador del diccionario
     * @return lista de palabras (inmutable)
     * @throws DiccionarioNoEncontradoException si no existe ese ID
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
     * Recupera una bolsa en memoria por su ID.
     *
     * @param id identificador de la bolsa
     * @return lista de líneas de definición de bolsa (inmutable)
     * @throws BolsaNoEncontradaException si no existe ese ID
     */
    public List<String> getBolsa(String id) throws BolsaNoEncontradaException {
        List<String> datos = bolsas.get(id);
        if (datos == null) {
            throw new BolsaNoEncontradaException(
                    String.format("Bolsa '%s' no encontrada.", id));
        }
        return Collections.unmodifiableList(datos);
    }

    /**
     * Añade un nuevo diccionario y lo persiste en disco.
     *
     * @param id       identificador del idioma
     * @param palabras lista de palabras a guardar
     * @throws DiccionarioYaExistenteException si el ID ya existe
     * @throws IOException                     si hay error de E/S
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
     * Añade una nueva bolsa de fichas y la persiste en disco.
     *
     * @param id        identificador del idioma
     * @param bolsaData mapa letra -> [cantidad, valor]
     * @throws BolsaYaExistenteException si el ID ya existe
     * @throws IOException               si hay error de E/S
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
        bolsas.put(id, new ArrayList<>(leerArchivoTexto(f.getPath())));
    }

    /**
     * Elimina completamente un idioma (diccionario y bolsa) de memoria y disco.
     *
     * @param id identificador del idioma
     * @throws DiccionarioNoEncontradoException si el diccionario no existe
     * @throws BolsaNoEncontradaException       si la bolsa no existe
     * @throws IOException                      si ocurre error de E/S al borrar
     */
    public void removeIdiomaCompleto(String id)
            throws DiccionarioNoEncontradoException,
            BolsaNoEncontradaException,
            IOException {
        if (!diccionarios.containsKey(id)) {
            throw new DiccionarioNoEncontradoException(
                    String.format("No existe ningún diccionario con ID '%s'.", id));
        }
        if (!bolsas.containsKey(id)) {
            throw new BolsaNoEncontradaException(
                    String.format("No existe ninguna bolsa con ID '%s'.", id));
        }
        diccionarios.remove(id);
        bolsas.remove(id);
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
     * @return lista de líneas leídas
     * @throws IOException si hay error de lectura
     */
    private List<String> leerArchivoTexto(String ruta) throws IOException {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            for (String l; (l = br.readLine()) != null;) {
                l = l.trim();
                if (!l.isEmpty()) {
                    out.add(l);
                }
            }
        }
        return out;
    }

    /**
     * Persiste todos los usuarios actuales en el archivo de disco.
     */
    private void guardarUsuariosEnDisco() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE_USUARIOS))) {
            for (Map.Entry<String, String[]> entry : usuariosMap.entrySet()) {
                String nombre = entry.getKey();
                String[] datos = entry.getValue();
                String password = datos[0];
                String puntos = datos[1];
                w.write(nombre + " " + password + " " + puntos);
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[Persistencia] Error guardando usuarios: " + e.getMessage());
        }
    }
    /**
     * Carga usuarios desde el archivo de disco.
     *
     * @return mapa nombre->Jugador con datos cargados o vacío si no existe
     *         archivo
     */
    /**
     * Carga usuarios desde el archivo de disco.
     *
     * @return mapa nombre->[password, puntos] con datos cargados o vacío si no
     *         existe archivo
     */
    private Map<String, String[]> cargarUsuariosDesdeDisco() {
        Map<String, String[]> mapa = new HashMap<>();
        File f = new File(FILE_USUARIOS);
        if (!f.exists()) {
            return mapa;
        }
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            for (String línea; (línea = r.readLine()) != null;) {
                String[] t = línea.trim().split(" ");
                if (t.length < 3) {
                    continue;
                }
                String nombre = t[0], password = t[1];
                String puntos = t[2];
                mapa.put(nombre, new String[] { password, puntos });
            }
        } catch (IOException e) {
            System.err.println("[Persistencia] Error cargando usuarios: " + e.getMessage());
        }
        return mapa;
    }
}
