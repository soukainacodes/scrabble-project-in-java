import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class Persistencia {

    public boolean existeJugador(String username) {
        File userDir = new File("FONTS/src/main/Persistencia/Datos/Jugadores/" + username);
        return userDir.exists() && userDir.isDirectory();
    }

    public void anadirJugador(String username, String password) throws IOException, UsuarioYaRegistradoException {
        if (existeJugador(username)) {
            throw new UsuarioYaRegistradoException(username);
        }

        // Crear el directorio del jugador y sus padres si no existen
        Path userDir = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username);
        Files.createDirectories(userDir);

        // Crear el archivo JSON del jugador
        Path userFile = userDir.resolve(username + ".json");
        JSONObject jugadorJson = new JSONObject();
        jugadorJson.put("username", username);
        jugadorJson.put("password", password);
        jugadorJson.put("maxpoints", 0);

        // Escribir el JSON en el archivo
        try (BufferedWriter writer = Files.newBufferedWriter(userFile, StandardCharsets.UTF_8)) {
            writer.write(jugadorJson.toString(4));
        }
    }

    public void eliminarJugador(String username) throws UsuarioNoEncontradoException, IOException {
        File userDir = new File("FONTS/src/main/Persistencia/Datos/Jugadores/" + username);

        if (!userDir.exists() || !userDir.isDirectory()) {
            throw new UsuarioNoEncontradoException(username);
        }

        File[] archivos = userDir.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (!archivo.delete()) {
                    throw new IOException("No se pudo eliminar el archivo: " + archivo.getName());
                }
            }
        }

        if (!userDir.delete()) {
            throw new IOException("No se pudo eliminar la carpeta del jugador: " + username);
        }
    }

    public void actualizarContrasena(String username, String newPass) throws UsuarioNoEncontradoException {
        Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");

        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);
            jugadorJson.put("password", newPass);

            Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    public void actualizarPuntuacion(String username, int pts)
            throws UsuarioNoEncontradoException, PuntuacionInvalidaException {
        if (pts < 0) {
            throw new PuntuacionInvalidaException(pts);
        }

        int maxpointsActual = getPuntuacion(username);

        if (pts > maxpointsActual) {
            Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");

            try {
                String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
                JSONObject jugadorJson = new JSONObject(contenido);
                jugadorJson.put("maxpoints", pts);

                Files.writeString(userFile, jugadorJson.toString(4), StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
            }
        }
    }

    public int getPuntuacion(String username) throws UsuarioNoEncontradoException {
        Path userFile = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores", username, username + ".json");

        if (!Files.exists(userFile)) {
            throw new UsuarioNoEncontradoException(username);
        }

        try {
            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
            JSONObject jugadorJson = new JSONObject(contenido);
            return jugadorJson.getInt("maxpoints");
        } catch (IOException e) {
            throw new UncheckedIOException("Error al acceder al archivo del jugador: " + username, e);
        }
    }

    public List<Map.Entry<String, Integer>> generarRanking() throws IOException {
        Path jugadoresDir = Paths.get("FONTS/src/main/Persistencia/Datos/Jugadores");
        List<Map.Entry<String, Integer>> ranking = new ArrayList<>();

        if (Files.exists(jugadoresDir) && Files.isDirectory(jugadoresDir)) {
            try (Stream<Path> paths = Files.list(jugadoresDir)) {
                paths.filter(Files::isDirectory).forEach(dir -> {
                    Path userFile = dir.resolve(dir.getFileName() + ".json");
                    if (Files.exists(userFile)) {
                        try {
                            String contenido = Files.readString(userFile, StandardCharsets.UTF_8);
                            JSONObject jugadorJson = new JSONObject(contenido);
                            String username = jugadorJson.getString("username");
                            int maxpoints = jugadorJson.getInt("maxpoints");
                            ranking.add(Map.entry(username, maxpoints));
                        } catch (IOException e) {
                            System.err.println("Error al procesar el archivo de jugador: " + userFile);
                        }
                    }
                });
            }
        }

        ranking.sort((e1, e2) -> {
            int cmp = Integer.compare(e2.getValue(), e1.getValue());
            if (cmp == 0) {
                return e1.getKey().compareTo(e2.getKey());
            }
            return cmp;
        });

        return ranking;
    }

    public int obtenerPosicion(String name) throws UsuarioNoEncontradoException, IOException {
        List<Map.Entry<String, Integer>> ranking = generarRanking();

        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getKey().equals(name)) {
                return i + 1;
            }
        }

        throw new UsuarioNoEncontradoException("El jugador no fue encontrado en el ranking: " + name);
    }

    public static void main(String[] args) {
        Persistencia persistencia = new Persistencia();

        try {
            System.out.println("=== Test de Gestión de Jugadores ===\n");

            // Test 1: Añadir jugadores
            System.out.println("1. Añadiendo jugadores...");
            persistencia.anadirJugador("player1", "password123");
            System.out.println("Jugador player1 añadido.");
            persistencia.anadirJugador("player2", "password456");
            System.out.println("Jugador player2 añadido.");
            persistencia.anadirJugador("player3", "password789");
            System.out.println("Jugador player3 añadido.\n");

            // Test 2: Verificar existencia
            System.out.println("2. Verificando existencia de jugadores...");
            System.out.println("¿Existe player1? " + persistencia.existeJugador("player1"));
            System.out.println("¿Existe player4? " + persistencia.existeJugador("player4") + "\n");

            // Test 3: Actualizar puntuaciones
            System.out.println("3. Actualizando puntuaciones...");
            persistencia.actualizarPuntuacion("player1", 100);
            System.out.println("Puntuación de player1 actualizada a 100");
            persistencia.actualizarPuntuacion("player2", 150);
            System.out.println("Puntuación de player2 actualizada a 150");
            persistencia.actualizarPuntuacion("player3", 75);
            System.out.println("Puntuación de player3 actualizada a 75\n");

            // Test 4: Obtener puntuaciones
            System.out.println("4. Obteniendo puntuaciones...");
            System.out.println("Puntuación de player1: " + persistencia.getPuntuacion("player1"));
            System.out.println("Puntuación de player2: " + persistencia.getPuntuacion("player2"));
            System.out.println("Puntuación de player3: " + persistencia.getPuntuacion("player3") + "\n");

            // Test 5: Actualizar contraseña
            System.out.println("5. Actualizando contraseña...");
            persistencia.actualizarContrasena("player1", "newpassword123");
            System.out.println("Contraseña de player1 actualizada.\n");

            // Test 6: Generar ranking
            System.out.println("6. Generando ranking...");
            List<Map.Entry<String, Integer>> ranking = persistencia.generarRanking();
            System.out.println("Ranking actual:");
            for (int i = 0; i < ranking.size(); i++) {
                System.out.printf("%d. %s: %d puntos%n",
                        i + 1,
                        ranking.get(i).getKey(),
                        ranking.get(i).getValue());
            }
            System.out.println();

            // Test 7: Obtener posición en el ranking
            System.out.println("7. Obteniendo posiciones en el ranking...");
            System.out.println("Posición de player1: " + persistencia.obtenerPosicion("player1"));
            System.out.println("Posición de player2: " + persistencia.obtenerPosicion("player2"));
            System.out.println("Posición de player3: " + persistencia.obtenerPosicion("player3") + "\n");

            // Test 8: Eliminar jugador
            System.out.println("8. Eliminando jugador...");
            persistencia.eliminarJugador("player2");
            System.out.println("Jugador player2 eliminado.");
            System.out.println("¿Existe player2? " + persistencia.existeJugador("player2") + "\n");

            // Test 9: Generar ranking final
            System.out.println("9. Ranking final después de eliminar player2:");
            ranking = persistencia.generarRanking();
            for (int i = 0; i < ranking.size(); i++) {
                System.out.printf("%d. %s: %d puntos%n",
                        i + 1,
                        ranking.get(i).getKey(),
                        ranking.get(i).getValue());
            }

        } catch (UsuarioYaRegistradoException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (UsuarioNoEncontradoException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (PuntuacionInvalidaException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// Clases de excepciones
class UsuarioYaRegistradoException extends Exception {
    public UsuarioYaRegistradoException(String username) {
        super("El usuario ya está registrado: " + username);
    }
}

class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException(String username) {
        super("El usuario no fue encontrado: " + username);
    }
}

class PuntuacionInvalidaException extends Exception {
    public PuntuacionInvalidaException(int puntuacion) {
        super("Puntuación inválida: " + puntuacion);
    }
}
