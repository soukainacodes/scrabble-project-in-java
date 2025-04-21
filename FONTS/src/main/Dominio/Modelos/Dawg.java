package Dominio.Modelos;

import java.util.*;

import Dominio.Excepciones.*;

/**
 * Representa un DAWG (Directed Acyclic Word Graph) para validación y búsqueda eficiente de palabras.
 * <p>
 * Permite cargar fichas válidas y construir la estructura DAWG a partir de un listado de palabras ordenadas.
 * Ofrece operaciones de tokenización, búsqueda de palabra, búsqueda de prefijo y obtención de nodos.
 * </p>
 */
public class Dawg {

    /** Nodo raíz del DAWG. */
    private Nodo raiz;

    /** Conjunto de fichas válidas (letras o grupos de letras permitidos en tokenización). */
    private Set<String> fichasValidas;

    /** Longitud máxima de fichas válidas, para optimizar la tokenización. */
    private int longitudMaxFicha;

    /** Registro auxiliar para minimización de nodos equivalentes. */
    private Map<Nodo, Nodo> registro;

    /**
     * Construye un DAWG inicializando con las fichas válidas y las palabras en el diccionario.
     *
     * @param lineasArchivoBolsa  Líneas con fichas válidas (formato: "ficha ...").
     * @param lineasArchivo       Líneas con palabras ordenadas para construir el DAWG.
     */
    public Dawg(List<String> lineasArchivoBolsa, List<String> lineasArchivo) {
        this.raiz = new Nodo();
        this.fichasValidas = new HashSet<>();
        this.longitudMaxFicha = 1;
        this.registro = new HashMap<>();
        cargarFichasValidas(lineasArchivoBolsa);
        construirDesdeArchivo(lineasArchivo);
    }

    /**
     * Obtiene el nodo raíz del DAWG.
     *
     * @return Nodo raíz.
     */
    public Nodo getRaiz() {
        return raiz;
    }

    /**
     * Carga las fichas válidas desde las líneas proporcionadas.
     * Establece también la longitud máxima de ficha.
     *
     * @param lineasArchivo Lista de cadenas con al menos una ficha por línea.
     */
    private void cargarFichasValidas(List<String> lineasArchivo) {
        fichasValidas.clear();
        longitudMaxFicha = 1;
        for (String linea : lineasArchivo) {
            String ficha = linea.trim().split("\\s+")[0];
            fichasValidas.add(ficha);
            longitudMaxFicha = Math.max(longitudMaxFicha, ficha.length());
        }
    }

    /**
     * Tokeniza una palabra en fichas válidas (greedy por longitud máxima).
     *
     * @param palabra Palabra a tokenizar.
     * @return Lista de tokens reconocidos, o lista vacía si falla.
     */
    public List<String> tokenizarPalabra(String palabra) {
        List<String> tokens = new ArrayList<>();
        String mayus = palabra.toUpperCase();
        int n = mayus.length(), i = 0;
        while (i < n) {
            boolean enc = false;
            int maxLen = Math.min(longitudMaxFicha, n - i);
            for (int len = maxLen; len > 0; len--) {
                String sub = mayus.substring(i, i + len);
                if (fichasValidas.contains(sub)) {
                    tokens.add(sub);
                    i += len;
                    enc = true;
                    break;
                }
            }
            if (!enc) break;
        }
        return tokens;
    }

    /**
     * Construye el DAWG minimizado a partir de un listado ordenado de palabras.
     *
     * @param lineasArchivo Lista de palabras ya ordenadas.
     */
    private void construirDesdeArchivo(List<String> lineasArchivo) {
        this.raiz = new Nodo();
        registro.clear();
        List<String> ultimaTokens = null;
        List<Nodo> ultimoCamino = new ArrayList<>();
        ultimoCamino.add(raiz);

        for (String linea : lineasArchivo) {
            List<String> tokens = tokenizarPalabra(linea);
            if (ultimaTokens == null) {
                // Primer palabra: insert completo
                Nodo actual = raiz;
                ultimoCamino = new ArrayList<>();
                ultimoCamino.add(raiz);
                for (int j = 0; j < tokens.size(); j++) {
                    String token = tokens.get(j);
                    Nodo nodo = new Nodo(token);
                    nodo.palabraValidaHastaAqui = (j == tokens.size() - 1);
                    actual.hijos.put(token, nodo);
                    actual = nodo;
                    ultimoCamino.add(nodo);
                }
                ultimaTokens = tokens;
            } else {
                // Minimizar el camino anterior
                int maxPref = Math.min(ultimaTokens.size(), tokens.size());
                int pref = 0;
                while (pref < maxPref && ultimaTokens.get(pref).equals(tokens.get(pref))) {
                    pref++;
                }
                // Minimizar nodos tras la divergencia
                for (int k = ultimaTokens.size(); k > pref; k--) {
                    Nodo nodoMin = ultimoCamino.get(k);
                    Nodo padre = ultimoCamino.get(k - 1);
                    String tok = ultimaTokens.get(k - 1);
                    Nodo ext = registro.get(nodoMin);
                    if (ext != null) {
                        padre.hijos.put(tok, ext);
                    } else {
                        registro.put(nodoMin, nodoMin);
                    }
                }
                // Reconstruir a partir de prefijo común
                Nodo prefNodo = (pref == 0 ? raiz : ultimoCamino.get(pref));
                ultimoCamino = new ArrayList<>(ultimoCamino.subList(0, pref + 1));
                for (int j = pref; j < tokens.size(); j++) {
                    String token = tokens.get(j);
                    Nodo nodo = new Nodo(token);
                    nodo.palabraValidaHastaAqui = (j == tokens.size() - 1);
                    prefNodo.hijos.put(token, nodo);
                    prefNodo = nodo;
                    ultimoCamino.add(nodo);
                }
                ultimaTokens = tokens;
            }
        }
        // Minimizar resto del último camino
        if (ultimaTokens != null) {
            for (int k = ultimaTokens.size(); k > 0; k--) {
                Nodo nodoMin = ultimoCamino.get(k);
                Nodo padre = ultimoCamino.get(k - 1);
                String tok = ultimaTokens.get(k - 1);
                Nodo ext = registro.get(nodoMin);
                if (ext != null) {
                    padre.hijos.put(tok, ext);
                } else {
                    registro.put(nodoMin, nodoMin);
                }
            }
        }
        registro.clear();
    }

    /**
     * Comprueba si una palabra existe en el DAWG.
     *
     * @param palabra Palabra a buscar.
     * @return {@code true} si existe exactamente la palabra, {@code false} otherwise.
     */
    public boolean buscarPalabra(String palabra) {
        Nodo actual = raiz;
        for (String token : tokenizarPalabra(palabra)) {
            actual = actual.hijos.get(token);
            if (actual == null) return false;
        }
        return actual.palabraValidaHastaAqui;
    }

    /**
     * Comprueba si existe algún sufijo en el DAWG que comience con el prefijo dado.
     *
     * @param prefijo Prefijo a buscar.
     * @return {@code true} si el prefijo es válido, {@code false} en caso contrario.
     */
    public boolean buscarPrefijo(String prefijo) {
        Nodo actual = raiz;
        for (String token : tokenizarPalabra(prefijo)) {
            actual = actual.hijos.get(token);
            if (actual == null) return false;
        }
        return true;
    }

    /**
     * Obtiene el último nodo correspondiente a la palabra dada, sin validar existencia.
     *
     * @param palabra Palabra/tokenización a navegar.
     * @return Último nodo alcanzado, o {@code null} si no existe.
     */
    public Nodo buscarUltimoNodo(String palabra) {
        Nodo actual = raiz;
        for (String token : tokenizarPalabra(palabra)) {
            actual = actual.hijos.get(token);
            if (actual == null) return null;
        }
        return actual;
    }
}
