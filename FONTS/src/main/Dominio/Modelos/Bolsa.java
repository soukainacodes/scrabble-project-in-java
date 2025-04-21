package Dominio.Modelos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Representa una bolsa de fichas para el juego de Scrabble.
 * <p>
 * Permite inicializarse a partir de líneas de configuración (por ejemplo, leídas de un fichero),
 * barajar las fichas aleatoriamente y extraer fichas de forma controlada.
 * </p>
 */
public class Bolsa {

    /**
     * Conjunto de fichas disponibles en la bolsa.
     */
    private List<Ficha> conjuntoDeFichas;

    /**
     * Idioma asociado a esta bolsa (por ejemplo, "ES" para español).
     */
    private String idioma;

    /**
     * Construye una bolsa de fichas a partir de líneas de configuración.
     * Cada línea debe tener el formato: "Letra Cantidad Puntuacion".
     *
     * @param lineasArchivo Lista de cadenas con la configuración de fichas.
     */
    public Bolsa(List<String> lineasArchivo) {
        this.conjuntoDeFichas = new ArrayList<>();
        inicializarBolsa(lineasArchivo);
        Collections.shuffle(this.conjuntoDeFichas);
    }

    /**
     * Construye una bolsa de fichas usando una semilla para el barajeo.
     * Útil para reproducibilidad en tests.
     *
     * @param lineasArchivo Lista de cadenas con la configuración de fichas.
     * @param seed          Semilla para la aleatoriedad.
     */
    public Bolsa(List<String> lineasArchivo, long seed) {
        this.conjuntoDeFichas = new ArrayList<>();
        inicializarBolsa(lineasArchivo);
        Random random = new Random(seed);
        Collections.shuffle(this.conjuntoDeFichas, random);
    }

    /**
     * Inicializa la bolsa cargando fichas según las líneas de configuración.
     *
     * @param lineasArchivo Líneas con formato "Letra Cantidad Puntuacion".
     */
    private void inicializarBolsa(List<String> lineasArchivo) {
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
     * Devuelve el idioma asociado a esta bolsa.
     *
     * @return Código de idioma (p.ej., "ES").
     */
    public String getIdioma() {
        return this.idioma;
    }

    /**
     * Extrae y elimina una ficha aleatoria de la bolsa.
     *
     * @return Ficha extraída, o {@code null} si la bolsa está vacía.
     */
    public Ficha sacarFichaAleatoria() {
        if (this.conjuntoDeFichas.isEmpty()) {
            return null;
        }
        int index = (int) (Math.random() * this.conjuntoDeFichas.size());
        return this.conjuntoDeFichas.remove(index);
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
     * @return {@code true} si no hay fichas en la bolsa, {@code false} en caso contrario.
     */
    public boolean isEmpty() {
        return this.conjuntoDeFichas.isEmpty();
    }

    /**
     * Devuelve el número de fichas restantes en la bolsa.
     *
     * @return Cantidad de fichas disponibles.
     */
    public int size() {
        return this.conjuntoDeFichas.size();
    }
}
