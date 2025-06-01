package Dominio.Modelos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Representa una bolsa de fichas para el juego de Scrabble.
 * <p>
 * Permite inicializarse a partir de líneas de configuración (por ejemplo,
 * leídas de un fichero), barajar las fichas aleatoriamente y extraer fichas de
 * forma controlada.
 * </p>
 */
public class Bolsa {

    /**
     * Conjunto de fichas disponibles en la bolsa.
     */
    private List<Ficha> conjuntoDeFichas;

    /**
     * Construye una bolsa de fichas a partir de líneas de configuración. Cada
     * línea debe tener el formato: "Letra Cantidad Puntuacion".
     *
     * @param lineasArchivo Lista de cadenas con la configuración de fichas.
     */
    public Bolsa(List<String> lineasArchivo) {
        this.conjuntoDeFichas = new ArrayList<>();
        for (String linea : lineasArchivo) {
            String[] partes = linea.trim().split("\\s+");
            if (partes.length == 3) {
                String letra = partes[0];
                int cantidad = Integer.parseInt(partes[1]);
                int puntuacion = Integer.parseInt(partes[2]);
                for (int i = 0; i < cantidad; i++) {
                    this.conjuntoDeFichas.add(new Ficha(letra, puntuacion));
                }
            }
        }
        
    }

    /**
     * Construye una bolsa de fichas usando una semilla para el barajeo. Útil
     * para reproducibilidad en tests.
     *
     * @param lineasArchivo Lista de cadenas con la configuración de fichas.
     * @param seed Semilla para la aleatoriedad.
     */
    public Bolsa(List<String> lineasArchivo, long seed) {
        this.conjuntoDeFichas = new ArrayList<>();
        for (String linea : lineasArchivo) {
            String[] partes = linea.trim().split("\\s+");
            if (partes.length == 3) {
                String letra = partes[0];
                int cantidad = Integer.parseInt(partes[1]);
                int puntuacion = Integer.parseInt(partes[2]);
                for (int i = 0; i < cantidad; i++) {
                    this.conjuntoDeFichas.add(new Ficha(letra, puntuacion));
                }
            }
        }
        Random random = new Random(seed);
        Collections.shuffle(this.conjuntoDeFichas, random);
    }

    /**
     * Extrae y elimina la primera ficha de la bolsa.
     *
     * @return Ficha extraída, o {@code null} si la bolsa está vacía.
     */
    public Ficha sacarFicha() {
        if (this.conjuntoDeFichas.isEmpty()) {
            return null;
        }
        return this.conjuntoDeFichas.remove(0);
    }

    /**
     * Indica si la bolsa está vacía (no quedan fichas).
     *
     * @return {@code true} si no hay fichas en la bolsa, {@code false} en caso
     * contrario.
     */
    public boolean isEmpty() {
        return this.conjuntoDeFichas.isEmpty();
    }

    /**
     * Añade una ficha a la bolsa.
     * @param ficha Ficha a añadir.
     */
    public void addFichaBolsa(Ficha ficha) {
        this.conjuntoDeFichas.add(ficha);
    }

    /**
     * Mezcla las fichas de la bolsa aleatoriamente.
     * <p>
     * Utiliza el método {@link Collections#shuffle(List)} para barajar las
     * fichas.
     * </p>
     */
    public void mezclarBolsa() {
        Collections.shuffle(this.conjuntoDeFichas);
    }

    /**
     * Devuelve el número de fichas restantes en la bolsa.
     *
     * @return Número de fichas restantes.
     */
    public List<String> toListString() {
        List<String> bolsaList = new ArrayList<>();
        Map<String, Integer> conteoLetras = new HashMap<>(); // Para contar las letras
        Map<String, Integer> puntuacionLetras = new HashMap<>(); // Para almacenar la puntuacion de la letra

        for (Ficha ficha : conjuntoDeFichas) {
            String letra = ficha.getLetra();
            int puntuacion = ficha.getPuntuacion();

            // Contar la frecuencia de cada letra
            if (conteoLetras.containsKey(letra)) {
                conteoLetras.put(letra, conteoLetras.get(letra) + 1);
            } else {
                conteoLetras.put(letra, 1);
                puntuacionLetras.put(letra, puntuacion); // Guarda la puntuacion de la letra
            }
        }

        // Crear la lista de strings con el formato "Letra Cantidad Puntuacion"
        for (Map.Entry<String, Integer> entry : conteoLetras.entrySet()) {
            String letra = entry.getKey();
            int cantidad = entry.getValue();
            int puntuacion = puntuacionLetras.get(letra); // Obtiene la puntuacion de la letra.
            String fichaString = String.format("%s %d %d", letra, cantidad, puntuacion);
            bolsaList.add(fichaString);
        }
        return bolsaList;
    }

}
