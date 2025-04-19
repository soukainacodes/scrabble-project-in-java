package Dominio.Modelos;

import java.io.*;
import java.util.*;

import Dominio.Excepciones.*;

public class Dawg {
    // Nodo raíz del DAWG
    private Nodo raiz;
    // Conjunto de fichas válidas (las letras o grupos de letras permitidos)
    private Set<String> fichasValidas;
    // Longitud máxima de una ficha (para tokenizar)
    private int longitudMaxFicha;
    // Registro para la minimización: se mapean nodos equivalentes ya existentes
    private Map<Nodo, Nodo> registro;
    
    // Constructor por defecto
    
    
    /**
     * Constructor que inicializa y carga directamente desde los archivos
     * @param lineasArchivoBolsa líneas del archivo de fichas válidas
     * @param lineasArchivo líneas del archivo de palabras ordenadas
     */
    public Dawg(List<String> lineasArchivoBolsa, List<String> lineasArchivo) {
        this.raiz = new Nodo();
        this.fichasValidas = new HashSet<>();
        this.longitudMaxFicha = 1;
        this.registro = new HashMap<>();
        cargarFichasValidas(lineasArchivoBolsa);
        construirDesdeArchivo(lineasArchivo);
    }
    
    // Devuelve el nodo raíz para empezar a iterar en el DAWG
    public Nodo getRaiz() {
        return raiz;
    }
    
    // ---------------------------------------------------
    // Métodos existentes (sin cambios)
    // ---------------------------------------------------
    
    // Carga desde un archivo la lista de fichas válidas (solo se usa la primera columna del archivo)
    private void cargarFichasValidas(List<String> lineasArchivo) {
        fichasValidas.clear();
        longitudMaxFicha = 1;
        for (String linea : lineasArchivo) {
            String[] partes = linea.split("\\s+");
            if (partes.length > 0) {
                String ficha = partes[0];
                fichasValidas.add(ficha);
                if (ficha.length() > longitudMaxFicha) {
                    longitudMaxFicha = ficha.length();
                }
            }
        }
    }
    
    // Tokeniza una palabra en fichas válidas
    public List<String> tokenizarPalabra(String palabra) {
        List<String> tokens = new ArrayList<>();
        String mayuscula = palabra.toUpperCase();
        int n = mayuscula.length();
        int i = 0;
        while (i < n) {
            boolean encontrado = false;
            int maxLen = Math.min(longitudMaxFicha, n - i);
            for (int len = maxLen; len >= 1; len--) {
                String subcadena = mayuscula.substring(i, i + len);
                if (fichasValidas.contains(subcadena)) {
                    tokens.add(subcadena);
                    i += len;
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("No se puede tokenizar la secuencia: " + mayuscula.substring(i));
                break;
            }
        }
        return tokens;
    }
    
    // Construye el DAWG leyendo las palabras ordenadas
    private void construirDesdeArchivo(List<String> lineasArchivo) {
        this.raiz = new Nodo();
        this.registro.clear();
        List<String> ultimaPalabraTokens = null;
        List<Nodo> ultimoCaminoNodos = new ArrayList<>();
        ultimoCaminoNodos.add(raiz);
        for (String linea : lineasArchivo) {
            List<String> tokens = tokenizarPalabra(linea);
            if (ultimaPalabraTokens == null) {
                Nodo nodoActual = raiz;
                ultimoCaminoNodos = new ArrayList<>();
                ultimoCaminoNodos.add(raiz);
                for (int j = 0; j < tokens.size(); j++) {
                    String ficha = tokens.get(j);
                    Nodo nuevoNodo = new Nodo(ficha);
                    if (j == tokens.size() - 1) {
                        nuevoNodo.palabraValidaHastaAqui = true;
                    }
                    nodoActual.hijos.put(ficha, nuevoNodo);
                    nodoActual = nuevoNodo;
                    ultimoCaminoNodos.add(nuevoNodo);
                }
                ultimaPalabraTokens = tokens;
            } else {
                int prefijoComun = 0;
                int maxPrefijo = Math.min(ultimaPalabraTokens.size(), tokens.size());
                while (prefijoComun < maxPrefijo && ultimaPalabraTokens.get(prefijoComun).equals(tokens.get(prefijoComun))) {
                    prefijoComun++;
                }
                for (int i = ultimaPalabraTokens.size(); i > prefijoComun; i--) {
                    Nodo nodoAminimizar = ultimoCaminoNodos.get(i);
                    Nodo nodoPadre = ultimoCaminoNodos.get(i - 1);
                    String ficha = ultimaPalabraTokens.get(i - 1);
                    Nodo nodoExistente = registro.get(nodoAminimizar);
                    if (nodoExistente != null) {
                        nodoPadre.hijos.put(ficha, nodoExistente);
                    } else {
                        registro.put(nodoAminimizar, nodoAminimizar);
                    }
                }
                Nodo nodoPrefijo = (prefijoComun == 0) ? raiz : ultimoCaminoNodos.get(prefijoComun);
                if (prefijoComun == 0) {
                    ultimoCaminoNodos = new ArrayList<>();
                    ultimoCaminoNodos.add(raiz);
                } else {
                    ultimoCaminoNodos = new ArrayList<>(ultimoCaminoNodos.subList(0, prefijoComun + 1));
                }
                for (int j = prefijoComun; j < tokens.size(); j++) {
                    String ficha = tokens.get(j);
                    Nodo nuevoNodo = new Nodo(ficha);
                    if (j == tokens.size() - 1) {
                        nuevoNodo.palabraValidaHastaAqui = true;
                    }
                    nodoPrefijo.hijos.put(ficha, nuevoNodo);
                    nodoPrefijo = nuevoNodo;
                    ultimoCaminoNodos.add(nuevoNodo);
                }
                ultimaPalabraTokens = tokens;
            }
        }
        if (ultimaPalabraTokens != null) {
            for (int i = ultimaPalabraTokens.size(); i > 0; i--) {
                Nodo nodoAminimizar = ultimoCaminoNodos.get(i);
                Nodo nodoPadre = ultimoCaminoNodos.get(i - 1);
                String ficha = ultimaPalabraTokens.get(i - 1);
                Nodo nodoExistente = registro.get(nodoAminimizar);
                if (nodoExistente != null) {
                    nodoPadre.hijos.put(ficha, nodoExistente);
                } else {
                    registro.put(nodoAminimizar, nodoAminimizar);
                }
            }
        }
        registro.clear();
    }
    
    // Métodos de búsqueda (sin cambios)
    public boolean buscarPalabra(String palabra) {
        List<String> tokens = tokenizarPalabra(palabra);
        Nodo nodoActual = raiz;
        for (String ficha : tokens) {
            Nodo siguiente = nodoActual.hijos.get(ficha);
            if (siguiente == null) return false;
            nodoActual = siguiente;
        }
        return nodoActual.palabraValidaHastaAqui;
    }
    
    public boolean buscarPrefijo(String prefijo) {
        List<String> tokens = tokenizarPalabra(prefijo);
        Nodo nodoActual = raiz;
        for (String ficha : tokens) {
            Nodo siguiente = nodoActual.hijos.get(ficha);
            if (siguiente == null) return false;
            nodoActual = siguiente;
        }
        return true;
    }
    
    public Nodo buscarUltimoNodo(String palabra) {
        List<String> tokens = tokenizarPalabra(palabra);
        Nodo nodoActual = raiz;
        for (String ficha : tokens) {
            Nodo siguiente = nodoActual.hijos.get(ficha);
            if (siguiente == null) {
                return null;
            }
            nodoActual = siguiente;
        }
        return nodoActual;
    }
}
