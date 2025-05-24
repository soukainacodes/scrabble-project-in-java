package Persistencia.Gestores;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.json.JSONObject;
import org.json.JSONArray;

import Dominio.Excepciones.*;

/**
 * Gestor que se encarga de las operaciones de persistencia relacionadas con los usuarios.
 * <p>
 * Esta clase proporciona métodos para la gestión de usuarios en el sistema de almacenamiento,
 * incluyendo operaciones de creación, lectura, actualización y eliminación de datos de usuario.
 * </p>
 */
public class GestorDeUsuarios {

    
    private static final String JUGADORES = "FONTS/src/main/Persistencia/Datos/Jugadores/";
    private static final String PARTIDAS = "FONTS/src/main/Persistencia/Datos/Partidas/";


    /**
     * Constructor de la clase GestorDeUsuarios.
     *  
    */
    public GestorDeUsuarios() {
        // Constructor vacío
    }


    /**
     * Verifica si un jugador existe en el sistema.
     * 
     * @param username Nombre de usuario del jugador
     * @return true si el jugador existe, false en caso contrario
     */
    public boolean existeJugador(String username) {
        // Define la ruta del directorio del jugador
        File userDir = new File(JUGADORES + username);
        // Comprueba si la carpeta del jugador existe
        return userDir.exists() && userDir.isDirectory();
    }


    /**
     * Añade un nuevo jugador al sistema.
     * 
     * @param username Nombre de usuario del jugador
     * @param password Contraseña del jugador
     * @throws IOException                  Si ocurre un error al acceder a los archivos
     * @throws UsuarioYaRegistradoException Si el jugador ya existe
     */
    public void anadirJugador(String username, String password) throws IOException, UsuarioYaRegistradoException {
        // Verificar si el jugador ya existe
        if (existeJugador(username)) {
            throw new UsuarioYaRegistradoException(username);
        }

        // Crear el directorio del jugador
        Path userDir = Paths.get(JUGADORES + username);
        Files.createDirectories(userDir);

        // Crear el archivo JSON del jugador
        Path userFile = userDir.resolve(username + ".json");

        // Preparar los datos del jugador en formato JSON
        JSONObject jugadorJson = new JSONObject();
        jugadorJson.put("nombre", username); // Usado en la interfaz
        jugadorJson.put("password", password); // Para autenticación
        jugadorJson.put("maxpuntos", 0); // Puntuación inicial
        jugadorJson.put("ultimaPartidaGuardada", JSONObject.NULL); // Sin partida guardada

        // Escribir el archivo JSON
        try (BufferedWriter writer = Files.newBufferedWriter(userFile, StandardCharsets.UTF_8)) {
            writer.write(jugadorJson.toString(4)); // 4 espacios para formato legible
        }
    }

    /**
     * Elimina un jugador del sistema.
     * 
     * @param username Nombre de usuario del jugador
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws IOException                  Si ocurre un error al acceder a los archivos
     */
    public void eliminarJugador(String username) throws UsuarioNoEncontradoException, IOException {
        // Usa el método existente para verificar si el jugador existe
        if (!existeJugador(username)) {
            throw new UsuarioNoEncontradoException(username);
        }

        // Primero eliminar todas las partidas relacionadas con este jugador
        File partidasDir = new File(PARTIDAS);
        if (partidasDir.exists() && partidasDir.isDirectory()) {
            File[] archivosPartidas = partidasDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (archivosPartidas != null) {
                for (File archivoPartida : archivosPartidas) {
                    try {
                        // Leer el archivo de la partida
                        String contenidoPartida = Files.readString(archivoPartida.toPath(), StandardCharsets.UTF_8);
                        JSONObject partidaJson = new JSONObject(contenidoPartida);

                        // Verificar si el jugador es jugador_1 o jugador_2
                        if ((partidaJson.has("jugador_1") && partidaJson.getString("jugador_1").equals(username)) ||
                                (partidaJson.has("jugador_2") && partidaJson.getString("jugador_2").equals(username))) {

                            // Eliminar la partida
                            if (!archivoPartida.delete()) {
                                System.err.println("No se pudo eliminar la partida: " + archivoPartida.getName());
                            } else {
                                System.out.println("Partida eliminada: " + archivoPartida.getName());
                            }
                        }
                    } catch (IOException e) {
                        System.err.println(
                                "Error al procesar partida " + archivoPartida.getName() + ": " + e.getMessage());
                    }
                }
            }
        }

        // Define la ruta de la carpeta del jugador
        File userDir = new File(JUGADORES + username);

        // Elimina todos los archivos dentro de la carpeta
        File[] archivos = userDir.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (!archivo.delete()) {
                    throw new IOException("No se pudo eliminar el archivo: " + archivo.getName());
                }
            }
        }

        // Elimina la carpeta del jugador
        if (!userDir.delete()) {
            throw new IOException("No se pudo eliminar la carpeta del jugador: " + username);
        }

        System.out.println("Jugador " + username + " eliminado completamente con todas sus partidas asociadas");
    }

    /**
     * Verifica la contraseña de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @param password Contraseña a verificar
     * @return true si la contraseña es correcta, false en caso contrario
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public boolean verificarContrasena(String username, String password) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");

        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Verificar la contraseña
            return jugadorJson.getString("password").equals(password);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    /**
     * Actualiza el nombre de un jugador.
     * 
     * @param username    Nombre de usuario del jugador
     * @param newUsername Nuevo nombre de usuario
     * @throws UsuarioNoEncontradoException         Si el jugador no existe
     * @throws UsuarioYaRegistradoException        Si el nuevo nombre ya está en uso
     */
    public void actualizarNombre(String username, String newUsername)
            throws UsuarioNoEncontradoException, UsuarioYaRegistradoException {
        // Verifica si el jugador existe
        if (!existeJugador(username)) {
            throw new UsuarioNoEncontradoException(username);
        }

        // Verifica si el nuevo nombre ya está en uso
        if (existeJugador(newUsername)) {
            throw new UsuarioYaRegistradoException(newUsername);
        }

        // Define las rutas relevantes
        Path oldUserDir = Paths.get(JUGADORES + username);
        Path oldUserFile = oldUserDir.resolve(username + ".json");
        Path newUserDir = Paths.get(JUGADORES + newUsername);
        Path newUserFile = newUserDir.resolve(newUsername + ".json");

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(oldUserFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Actualizar el nombre de usuario en el JSON
            jugadorJson.put("nombre", newUsername);

            // Crear el nuevo directorio si no existe
            Files.createDirectories(newUserDir);

            // Escribir el archivo JSON con el nuevo nombre
            Files.writeString(newUserFile, jugadorJson.toString(4), StandardCharsets.UTF_8);

            // Comprobar si existe la imagen de perfil y transferirla
            File oldProfileImage = new File(oldUserDir.toFile(), username + ".png");
            if (oldProfileImage.exists()) {
                BufferedImage image = ImageIO.read(oldProfileImage);
                if (image != null) {
                    File newProfileImage = new File(newUserDir.toFile(), newUsername + ".png");
                    ImageIO.write(image, "png", newProfileImage);
                    oldProfileImage.delete(); // Eliminar la imagen antigua
                }
            }

            // Transferir cualquier otro archivo que pueda existir en el directorio
            File[] oldFiles = oldUserDir.toFile().listFiles();
            if (oldFiles != null) {
                for (File oldFile : oldFiles) {
                    if (!oldFile.getName().equals(username + ".json") &&
                            !oldFile.getName().equals(username + ".png")) {

                        // Determinar el nuevo nombre para el archivo (si comienza con el username
                        // antiguo)
                        String newFileName = oldFile.getName();
                        if (newFileName.startsWith(username)) {
                            newFileName = newFileName.replaceFirst(username, newUsername);
                        }

                        // Copiar el archivo al nuevo directorio
                        Files.copy(oldFile.toPath(),
                                newUserDir.resolve(newFileName),
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        oldFile.delete(); // Eliminar el archivo antiguo
                    }
                }
            }

            // Eliminar el archivo antiguo
            Files.delete(oldUserFile);

            // Eliminar el directorio antiguo (debe estar vacío)
            Files.delete(oldUserDir);

        } catch (IOException e) {
            throw new UncheckedIOException("Error al actualizar el nombre del jugador: " + username, e);
        }
        // Actualizar el nombre en todas las partidas guardadas
        File partidasDir = new File(PARTIDAS);
        if (partidasDir.exists() && partidasDir.isDirectory()) {
            File[] archivos = partidasDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (archivos != null) {
                for (File archivoPartida : archivos) {
                    try {
                        // Leer el archivo de la partida
                        String contenidoPartida = Files.readString(archivoPartida.toPath(), StandardCharsets.UTF_8);
                        JSONObject partidaJson = new JSONObject(contenidoPartida);

                        boolean modificado = false;

                        // Actualizar el jugador_1 si es necesario
                        if (partidaJson.has("jugador_1") &&
                                partidaJson.getString("jugador_1").equals(username)) {
                            partidaJson.put("jugador_1", newUsername);
                            modificado = true;
                        }

                        // Actualizar el jugador_2 si es necesario
                        if (partidaJson.has("jugador_2") &&
                                partidaJson.getString("jugador_2").equals(username)) {
                            partidaJson.put("jugador_2", newUsername);
                            modificado = true;
                        }

                        // Guardar los cambios si se modificó la partida
                        if (modificado) {
                            Files.writeString(archivoPartida.toPath(),
                                    partidaJson.toString(4),
                                    StandardCharsets.UTF_8);
                        }
                    } catch (IOException e) {
                        System.err.println("Error al actualizar partida: " + archivoPartida.getName());
                    }
                }
            }
        }
    }

    /**
     * Actualiza la contraseña de un jugador.
     * 
     * @param username   Nombre de usuario del jugador
     * @param currentPass Contraseña actual
     * @param newPass    Nueva contraseña
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public void actualizarContrasena(String username, String currentPass, String newPass)
            throws UsuarioNoEncontradoException {
        // Verificar la contraseña actual
        if (!verificarContrasena(username, currentPass)) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Actualizar la contraseña
            jugadorJson.put("password", newPass);

            // Escribir el archivo JSON actualizado
            Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    /**
     * Actualiza la puntuación máxima de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @param pts      Nueva puntuación máxima
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     * @throws PuntuacionInvalidaException  Si la puntuación es negativa
     */
    public void actualizarPuntuacion(String username, int pts)
            throws UsuarioNoEncontradoException, PuntuacionInvalidaException {
        // Verificar que la puntuación no sea negativa
        if (pts < 0) {
            throw new PuntuacionInvalidaException(pts);
        }

        // Obtener la puntuación actual
        int maxpuntosActual = obtenerPuntuacion(username);

        // Actualizar la puntuación solo si el nuevo valor es mayor
        if (pts > maxpuntosActual) {
            // Define la ruta del archivo JSON del jugador
            Path userFile = Paths.get(JUGADORES + username, username + ".json");

            try {
                // Leer el contenido del archivo JSON
                String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
                JSONObject jugadorJson = new JSONObject(contenido);

                // Actualizar la puntuación
                jugadorJson.put("maxpuntos", pts);

                // Escribir el archivo JSON actualizado
                Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
            }
        }
    }

    /**
     * Actualiza la última partida guardada de un jugador.
     * 
     * @param username  Nombre de usuario del jugador
     * @param idPartida ID de la partida a guardar
     * @throws UsuarioNoEncontradoException         Si el jugador no existe
     * @throws UltimaPartidaNoExistenteException    Si no hay partida guardada
     */
    public void actualizarUltimaPartida(String username, String idPartida)
            throws UsuarioNoEncontradoException, UltimaPartidaNoExistenteException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");

        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Actualizar la última partida guardada
            jugadorJson.put("ultimaPartidaGuardada", idPartida);

            // Escribir el archivo JSON actualizado
            Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    /**
     * Obtiene la puntuación máxima de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @return Puntuación máxima del jugador
     * @throws UsuarioNoEncontradoException Si el jugador no existe
     */
    public int obtenerPuntuacion(String username) throws UsuarioNoEncontradoException {
        // Define la ruta del archivo JSON del jugador

        // Verifica si el jugador existe
        if (!existeJugador(username)) {
            throw new UsuarioNoEncontradoException(username);
        }

        Path userFile = Paths.get(JUGADORES + username, username + ".json");

        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Obtener la puntuación
            return jugadorJson.getInt("maxpuntos");
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    /**
     * Obtiene la última partida guardada de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @return ID de la última partida guardada
     * @throws UsuarioNoEncontradoException         Si el jugador no existe
     * @throws UltimaPartidaNoExistenteException    Si no hay partida guardada
     */
    public String obtenerUltimaPartida(String username)
            throws UsuarioNoEncontradoException, UltimaPartidaNoExistenteException {
        // Define la ruta del archivo JSON del jugador
        Path userFile = Paths.get(JUGADORES + username, username + ".json");

        // Verifica si el archivo existe
        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            // Leer el contenido del archivo JSON
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);

            // Verificar si existe una última partida guardada y no es nula
            if (!jugadorJson.has("ultimaPartidaGuardada") || jugadorJson.isNull("ultimaPartidaGuardada")) {
                throw new UltimaPartidaNoExistenteException();
            }

            // Obtener la última partida guardada
            return jugadorJson.getString("ultimaPartidaGuardada");
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    /**
     * Guarda la imagen de perfil de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     * @param image    Imagen a guardar
     */
    public void guardarImagenPerfil(String username, BufferedImage image) {
        // Verificar si el usuario existe
        if (!existeJugador(username)) {
            return;
        }

        // Crear el directorio del jugador si no existe
        File dir = new File(JUGADORES + username);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("No se pudo crear el directorio para la imagen de perfil de " + username);
                return;
            }
        }

        // Definir la ruta de la imagen de perfil
        File imagenFile = new File(dir, username + ".png");

        try {
            // Guardar la imagen en el archivo
            javax.imageio.ImageIO.write(image, "png", imagenFile);
            System.out.println("Imagen de perfil guardada para " + username);
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen de perfil de " + username + ": " + e.getMessage());
        }
    }

    /**
     * Verifica si un jugador existe en el sistema.
     * 
     * @param username Nombre de usuario del jugador
     * @return true si el jugador existe, false en caso contrario
     */
    public BufferedImage obtenerImagenPerfil(String username) {
        // Verificar si el usuario existe
        if (!existeJugador(username)) {
            return null;
        }

        // Definir la ruta de la imagen de perfil
        File imagenFile = new File(JUGADORES + username + "/" + username + ".png");

        // Verificar si la imagen existe
        if (!imagenFile.exists()) {
            return null;
        }

        try {
            // Leer y retornar la imagen
            return javax.imageio.ImageIO.read(imagenFile);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de perfil de " + username + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina la imagen de perfil de un jugador.
     * 
     * @param username Nombre de usuario del jugador
     */
    public void eliminarImagenPerfil(String username) {
        // Verificar si el usuario existe
        if (!existeJugador(username)) {
            return;
        }

        // Definir la ruta de la imagen de perfil
        File imagenFile = new File(JUGADORES + username + "/" + username + ".png");

        // Verificar si la imagen existe
        if (imagenFile.exists()) {
            // Eliminar la imagen
            if (imagenFile.delete()) {
                System.out.println("Imagen de perfil eliminada para " + username);
            } else {
                System.err.println("No se pudo eliminar la imagen de perfil de " + username);
            }
        }
    }

}