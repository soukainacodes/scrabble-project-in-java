package Persistencia.Gestores.Utilidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class Utils {
        /**
     * Convierte una cadena de fichas a un JSONArray.
     * 
     * @param fichas Cadena de fichas
     * @return JSONArray de fichas
     */
public List<String> leerArchivoTexto(String ruta) throws IOException {
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
     * Convierte una lista de datos de partida a un JSON.
     * 
     * @param partidaData Lista de datos de la partida
     * @return JSON de la partida
     */
    public String partidaListToJson(List<String> partidaData) {
        // Usamos un JSONObject directamente ya que el orden no importa
        JSONObject partidaJson = new JSONObject();

        // 0. Partida acabada
        partidaJson.put("partida_acabada", partidaData.get(0));

        // 1. nombre_partida
        partidaJson.put("nombre_partida", partidaData.get(1));

        // 2. numero_turnos
        partidaJson.put("numero_turnos", Integer.parseInt(partidaData.get(2)));

        // 3. turno_jugador
        partidaJson.put("turno_jugador", Integer.parseInt(partidaData.get(3)));

        // 4. jugador_1
        partidaJson.put("jugador_1", partidaData.get(4));

        // 5. jugador_2
        partidaJson.put("jugador_2", partidaData.get(5));

        // 6. puntos_jugador_1
        partidaJson.put("puntos_jugador_1", Integer.parseInt(partidaData.get(6)));

        // 7. puntos_jugador_2
        partidaJson.put("puntos_jugador_2", Integer.parseInt(partidaData.get(7)));

        // 8. fichas_jugador_1
        JSONArray fichasJugador1 = parseFichas(partidaData.get(8));
        partidaJson.put("fichas_jugador_1", fichasJugador1);

        // 9. fichas_jugador_2
        JSONArray fichasJugador2 = parseFichas(partidaData.get(9));
        partidaJson.put("fichas_jugador_2", fichasJugador2);

        // 10. fichas_restantes
        int numFichasRestantes = Integer.parseInt(partidaData.get(10));
        JSONArray fichasRestantes = new JSONArray();
        for (int i = 11; i < 11 + numFichasRestantes; i++) {
            fichasRestantes.put(partidaData.get(i)); // Añadimos la ficha como una cadena
        }
        partidaJson.put("bolsa", fichasRestantes);

        // 11. posiciones_tablero
        int numFichasUsadas = Integer.parseInt(partidaData.get(11 + numFichasRestantes));
        JSONArray posicionesTablero = new JSONArray();
        // Stop 1 element before the end to avoid including the resource
        for (int i = 11 + numFichasRestantes + 1; i < partidaData.size() - 1; i++) {
            posicionesTablero.put(partidaData.get(i)); // Añadimos la posición como una cadena
        }
        partidaJson.put("posiciones_tablero", posicionesTablero);

        // 12. recurso - always the last element
        partidaJson.put("recurso", partidaData.get(partidaData.size() - 1));
        // Retornamos el JSON como una cadena con formato bonito
        return partidaJson.toString(2); // Formateo bonito del JSON
    }

    /**
     * Convierte un JSON de partida a una lista de datos.
     * 
     * @param partidaJson JSON de la partida
     * @return Lista de datos de la partida
     */
    public List<String> jsonToPartidaList(String partidaJson) {
        // Inicializamos la lista que contendrá los datos de la partida
        List<String> partidaData = new ArrayList<>();

        // Convertimos el JSON a un JSONObject
        JSONObject partida = new JSONObject(partidaJson);

        // Extraemos los valores en el orden exacto que hemos establecido
        partidaData.add(String.valueOf(partida.getInt("partida_acabada"))); // 0. partida_acabada
        partidaData.add(partida.getString("nombre_partida")); // 1. nombre_partida
        partidaData.add(String.valueOf(partida.getInt("numero_turnos"))); // 2. numero_turnos
        partidaData.add(String.valueOf(partida.getInt("turno_jugador"))); // 3. turno_jugador
        partidaData.add(partida.getString("jugador_1")); // 4. jugador_1
        partidaData.add(partida.getString("jugador_2")); // 5. jugador_2
        partidaData.add(String.valueOf(partida.getInt("puntos_jugador_1"))); // 6. puntos_jugador_1
        partidaData.add(String.valueOf(partida.getInt("puntos_jugador_2"))); // 7. puntos_jugador_2

        // 8. fichas_jugador_1
        JSONArray fichasJugador1 = partida.getJSONArray("fichas_jugador_1");
        partidaData.add(parseFichasToString(fichasJugador1));

        // 9. fichas_jugador_2
        JSONArray fichasJugador2 = partida.getJSONArray("fichas_jugador_2");
        partidaData.add(parseFichasToString(fichasJugador2));

        // 10. fichas_restantes
        JSONArray fichasRestantes = partida.getJSONArray("bolsa");
        partidaData.add(String.valueOf(fichasRestantes.length())); // Añadimos el número de fichas restantes
        for (int i = 0; i < fichasRestantes.length(); i++) {
            partidaData.add(fichasRestantes.getString(i)); // Añadimos cada ficha restante
        }

        // 11. posiciones_tablero
        JSONArray posicionesTablero = partida.getJSONArray("posiciones_tablero");
        partidaData.add(String.valueOf(posicionesTablero.length())); // Añadimos el número de fichas restantes
        for (int i = 0; i < posicionesTablero.length(); i++) {
            if (posicionesTablero.isNull(i)) {
                partidaData.add("null"); // Add a string representation of null
            } else {
                partidaData.add(posicionesTablero.getString(i)); // Añadimos cada posición del tablero
            }
        }

        // 12. recurso
        partidaData.add(partida.getString("recurso")); // Último elemento es el recurso
        return partidaData;
    }

    /**
     * Convierte una cadena de fichas a un JSONArray.
     * 
     * @param fichaData Cadena de fichas
     * @return JSONArray de fichas
     */
    private JSONArray parseFichas(String fichaData) {
        JSONArray fichas = new JSONArray();
        String[] fichaArray = fichaData.split(" ");
        for (int i = 0; i < fichaArray.length; i += 2) {
            fichas.put(fichaArray[i] + " " + fichaArray[i + 1]); // Guardamos la ficha como una cadena
        }
        return fichas;
    }

    /**
     * Convierte un JSONArray de fichas a una cadena.
     * 
     * @param fichas JSONArray de fichas
     * @return Cadena representando las fichas
     */
    private String parseFichasToString(JSONArray fichas) {
        StringBuilder fichasStr = new StringBuilder();
        for (int i = 0; i < fichas.length(); i++) {
            fichasStr.append(fichas.getString(i)).append(" ");
        }
        return fichasStr.toString().trim();
    }

}
