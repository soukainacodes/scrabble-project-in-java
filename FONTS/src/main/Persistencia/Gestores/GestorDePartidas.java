package Persistencia.Gestores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import Dominio.Excepciones.PartidaNoEncontradaException;
import Dominio.Excepciones.UsuarioNoEncontradoException;
import Persistencia.Gestores.Utilidades.Utils;


public class GestorDePartidas {

    private static final String PARTIDAS = "FONTS/src/main/Persistencia/Datos/Partidas/";
    

    // Añade estas variables miembro a tu clase GestorDePartidas
    private final GestorDeUsuarios gestorUsuarios;
    private final GestorDeRecursos gestorRecursos;
    private final Utils utils = new Utils();

    /**
     * Constructor de la clase GestorDePartidas.
     * Crea el directorio de partidas si no existe.
     */
    public GestorDePartidas() {
        File partidasDir = new File(PARTIDAS);
        if (!partidasDir.exists()) {
            partidasDir.mkdirs();
        }
        
        // Inicializa los gestores
        gestorUsuarios = new GestorDeUsuarios();
        gestorRecursos = new GestorDeRecursos();
    }


    /**
     * Comprueba si una partida existe en el sistema.
     * @param id ID de la partida
     * @return  true si la partida existe, false en caso contrario
    */
    public boolean existePartida(String id) {
        // Verifica si el archivo de la partida existe
        File partidaFile = new File(PARTIDAS + "partida_" + id + ".json");
        return partidaFile.exists();
    }


    /**
     * Verifica si una partida está acabada.
     * 
     * @param id ID de la partida
     * @return true si la partida está acabada, false en caso contrario
     */
    public boolean esPartidaAcabada(String id) {
        try {
            // Verifica si la partida existe
            if (!existePartida(id)) {
                return false;
            }

            // Leemos el archivo y parseamos directamente el JSON
            String content = Files.readString(Paths.get(PARTIDAS + "partida_" + id + ".json"),
                    StandardCharsets.UTF_8);
            JSONObject partidaJson = new JSONObject(content);

            // El campo se guarda como un entero (0 o 1)
            int acabadaValue = partidaJson.getInt("partida_acabada");
            return acabadaValue == 1;

        } catch (IOException e) {
            System.err.println("[Persistencia] Error al leer la partida: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el recurso de una partida.
     * 
     * @param id ID de la partida
     * @return Recurso de la partida
     */
    public String obtenerRecursoPartida(String id) {
        // Verifica si la partida existe
        if (!existePartida(id)) {
            return null;
        }

        // Carga el archivo de la partida
        String rutaArchivo = PARTIDAS + "partida_" + id + ".json";
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
            JSONObject partidaJson = new JSONObject(contenido);

            System.out.println("El recurso de la partida es: " + partidaJson.getString("recurso"));
            return partidaJson.getString("recurso");
        } catch (IOException e) {
            System.err.println("[Persistencia] Error al cargar el recurso de la partida: " + e.getMessage());
            return null;
        }
    }

    /**
     * Guarda una partida en el sistema.
     * 
     * @param username        Nombre de usuario del jugador
     * @param segundoJugador  Nombre de usuario del segundo jugador
     * @param id              ID de la partida
     * @param partida         Lista de datos de la partida
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public void guardarPartida(String username, String segundoJugador, String id, List<String> partida)
            throws UsuarioNoEncontradoException {
        String nombreArchivo = "partida_" + id + ".json";
        String rutaArchivo = PARTIDAS + nombreArchivo;


        // Convertir la lista de datos a JSON
        String jsonPartida = utils.partidaListToJson(partida);

        // Escribir el JSON en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(jsonPartida);
            System.out.println("Partida guardada exitosamente en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("[Persistencia] Error al guardar la partida: " + e.getMessage());
        }

    }

    /**
     * Carga una partida del sistema.
     * 
     * @param id ID de la partida a cargar
     * @return Lista de datos de la partida
     * @throws PartidaNoEncontradaException Si la partida no existe
     */
    public List<String> cargarPartida(String id) throws PartidaNoEncontradaException {
        // Verifica si la partida existe
        if (!existePartida(id)) {
            throw new PartidaNoEncontradaException(id);
        }

        // Carga el archivo de la partida
        String rutaArchivo = PARTIDAS + "partida_" + id + ".json";
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
            return utils.jsonToPartidaList(contenido);
        } catch (IOException e) {
            throw new PartidaNoEncontradaException(id);
        }
    }

    /**
     * Elimina una partida del sistema.
     * 
     * @param id ID de la partida a eliminar
     * @throws PartidaNoEncontradaException Si la partida no existe
     */
    public void eliminarPartida(String id) throws PartidaNoEncontradaException {
        // Verifica si la partida existe
        if (!existePartida(id)) {
            throw new PartidaNoEncontradaException(id);
        }

        // Elimina el archivo de la partida
        File partidaFile = new File(PARTIDAS + "partida_" + id + ".json");
        if (partidaFile.delete()) {
            System.out.println("Partida eliminada: " + partidaFile.getPath());
        } else {
            System.out.println("No se pudo eliminar la partida.");
        }
    }

    /**
     * Lista todas las partidas no acabadas del jugador actual.
     * 
     * @param jugadorActual Nombre de usuario del jugador actual
     * @return Lista de IDs de partidas no acabadas
     */
    public List<String> listarPartidasNoAcabadas(String jugadorActual) {
        List<String> partidasNoAcabadas = new ArrayList<>();
        File dir = new File(PARTIDAS);

        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.getName().endsWith(".json")) {
                    String id = file.getName().replace("partida_", "").replace(".json", "");

                    try {
                        // Verificamos si la partida no está acabada
                        if (!esPartidaAcabada(id)) {
                            // Leer el archivo JSON para verificar si el jugador actual participa
                            String content = Files.readString(Paths.get(PARTIDAS + file.getName()),
                                    StandardCharsets.UTF_8);
                            JSONObject partidaJson = new JSONObject(content);

                            // Extraer el recurso y los jugadores de la partida
                            String recurso = partidaJson.getString("recurso");
                            String jugador1 = partidaJson.getString("jugador_1");
                            String jugador2 = partidaJson.getString("jugador_2");

                            // Verificar si:
                            // 1. El jugador actual participa (es jugador_1 o jugador_2)
                            // 2. Ambos jugadores existen en el sistema
                            // 3. El recurso existe
                            if ((jugador1.equals(jugadorActual) || jugador2.equals(jugadorActual)) &&
                                    gestorUsuarios.existeJugador(jugador1) &&
                                    gestorUsuarios.existeJugador(jugador2) &&
                                    gestorRecursos.existeRecurso(recurso)) {
                                partidasNoAcabadas.add(id);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("[Persistencia] Error al procesar partida " + id + ": " + e.getMessage());
                    }
                }
            }
        }
        return partidasNoAcabadas; // Retorna la lista de partidas no acabadas
    }

    /**
     * Obtiene el segundo jugador de una partida.
     * 
     * @param id ID de la partida
     * @return Nombre del segundo jugador
     * @throws PartidaNoEncontradaException Si la partida no existe
     * @throws UsuarioNoEncontradoException Si el segundo jugador no existe
     */
    public String obtenerJugadorActual(String id) throws PartidaNoEncontradaException, UsuarioNoEncontradoException {
        // Verifica si la partida existe
        if (!existePartida(id)) {
            throw new PartidaNoEncontradaException(id);
        }

        // Carga el archivo de la partida
        String rutaArchivo = PARTIDAS + "partida_" + id + ".json";
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)), StandardCharsets.UTF_8);
            JSONObject partidaJson = new JSONObject(contenido);

            return partidaJson.getString("jugador_1");
        } catch (IOException e) {
            throw new UsuarioNoEncontradoException();
        }
    }


    /**
     * Obtiene el segundo jugador de una partida.
     * 
     * @param id ID de la partida
     * @return Nombre del segundo jugador
     * @throws PartidaNoEncontradaException Si la partida no existe
     * @throws UsuarioNoEncontradoException  Si el usuario no existe
     */
    public String obtenerSegundoJugador(String id) throws PartidaNoEncontradaException, UsuarioNoEncontradoException {
        // Verifica si la partida existe
        if (!existePartida(id)) {
            throw new PartidaNoEncontradaException(id);
        }
        // Carga el archivo de la partida
        String rutaArchivo = PARTIDAS + "partida_" + id + ".json";
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)), StandardCharsets.UTF_8);
            JSONObject partidaJson = new JSONObject(contenido);

            return partidaJson.getString("jugador_2");
        } catch (IOException e) {
            throw new UsuarioNoEncontradoException();
        }
    }

    
}
