package main.Dominio.Controladores.Modelos.Dawg;

import java.io.*;
import java.util.*;

import main.Dominio.Controladores.Excepciones.TokenizacionException;
import main.Dominio.Controladores.Excepciones.PalabraInvalidaException;
import main.Dominio.Controladores.Excepciones.PrefijoNoEncontradoException;
import main.Dominio.Controladores.Excepciones.NodoNoEncontradoException;

/**
 * La clase Dawg implementa un DAWG (Directed Acyclic Word Graph), una estructura
 * eficiente para el almacenamiento y la búsqueda de palabras mediante tokenización.
 * <p>
 * Esta clase permite:
 * <ul>
 *   <li>Cargar un conjunto de fichas válidas desde un archivo.</li>
 *   <li>Tokenizar palabras en base a las fichas válidas, probando la ficha más larga primero.</li>
 *   <li>Construir la estructura DAWG leyendo un archivo de palabras ordenado lexicográficamente.</li>
 *   <li>Realizar búsquedas de palabras completas y de prefijos en el DAWG.</li>
 *   <li>Obtener el último nodo tras tokenizar una palabra.</li>
 * </ul>
 * <p>
 * Se asume que el fichero de fichas siempre es correcto, de modo que no se realiza ninguna 
 * validación adicional al cargar las fichas.
 * 
 * @author 
 */
public class Dawg {
    /** Nodo raíz del DAWG. */
    private Nodo raiz;
    
    /** Conjunto de fichas válidas (las letras o grupos de letras permitidos). */
    private Set<String> fichasValidas;
    
    /** Longitud máxima de una ficha (utilizada para la tokenización de palabras). */
    private int longitudMaxFicha;
    
    /**
     * Registro para la minimización: se mapean nodos equivalentes ya existentes para
     * evitar duplicados en la estructura.
     */
    private Map<Nodo, Nodo> registro;
    
    /**
     * Constructor que inicializa la estructura del DAWG.
     */
    public Dawg() {
        this.raiz = new Nodo();
        this.fichasValidas = new HashSet<>();
        this.longitudMaxFicha = 1;
        this.registro = new HashMap<>();
    }
    
    /**
     * Devuelve el nodo raíz del DAWG.
     *
     * @return el nodo raíz.
     */
    public Nodo getRaiz() {
        return raiz;
    }
    
    /**
     * Carga desde un archivo la lista de fichas válidas.
     * <p>
     * Se utiliza únicamente la primera columna del archivo, y se actualiza la longitud máxima
     * de una ficha. Se asume que el fichero siempre es correcto, por lo que no se realiza validación extra.
     *
     * @param rutaArchivo la ruta del archivo que contiene las fichas válidas.
     * @throws IOException si ocurre algún error al leer el archivo.
     */
    public void cargarFichasValidas(String rutaArchivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            fichasValidas.clear();
            longitudMaxFicha = 1;
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                // Ignorar líneas vacías o líneas que comienzan con "#"
                if (linea.isEmpty() || linea.startsWith("#"))
                    continue;
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
    }
    
    /**
     * Tokeniza una palabra dividiéndola en fichas válidas.
     * <p>
     * Se intenta primero con la ficha de mayor longitud, y si en algún punto no se encuentra
     * ninguna ficha válida, se lanza una excepción {@code TokenizacionException}.
     *
     * @param palabra la palabra que se desea tokenizar.
     * @return una lista de tokens (fichas) que componen la palabra.
     * @throws TokenizacionException si alguna parte de la palabra no puede ser tokenizada.
     */
    public List<String> tokenizarPalabra(String palabra) throws TokenizacionException {
        List<String> tokens = new ArrayList<>();
        int n = palabra.length();
        int i = 0;
        
        while (i < n) {
            boolean encontrado = false;
            int maxLen = Math.min(longitudMaxFicha, n - i);
            
            // Probar con la ficha más larga hacia la más corta.
            for (int len = maxLen; len >= 1; len--) {
                String subcadena = palabra.substring(i, i + len);
                if (fichasValidas.contains(subcadena)) {
                    tokens.add(subcadena);
                    i += len;
                    encontrado = true;
                    break;
                }
            }
            
            // Si no se encuentra ninguna ficha válida, se lanza una excepción.
            if (!encontrado) {
                throw new TokenizacionException(palabra.substring(i));
            }
        }
        
        return tokens;
    }
    
    /**
     * Construye el DAWG leyendo un archivo de palabras línea a línea.
     * <p>
     * Se asume que el archivo está ordenado lexicográficamente. Durante la lectura, se realiza
     * la minimización de nodos para evitar duplicados, insertando únicamente las partes no comunes
     * entre palabras consecutivas.
     *
     * @param rutaArchivo la ruta del archivo que contiene las palabras.
     * @throws IOException si ocurre algún error al leer el archivo.
     * @throws TokenizacionException si alguna palabra no puede ser tokenizada correctamente.
     */
    public void construirDesdeArchivo(String rutaArchivo) throws IOException, TokenizacionException {
        this.raiz = new Nodo();
        this.registro.clear();
        BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        List<String> ultimaPalabraTokens = null;
        List<Nodo> ultimoCaminoNodos = new ArrayList<>();
        ultimoCaminoNodos.add(raiz);
        
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;
            List<String> tokens = tokenizarPalabra(linea);
            
            if (ultimaPalabraTokens == null) {
                // Primera palabra: inserción completa desde la raíz.
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
                // Calcular el número de fichas que tienen prefijo común
                while (prefijoComun < maxPrefijo && ultimaPalabraTokens.get(prefijoComun).equals(tokens.get(prefijoComun))) {
                    prefijoComun++;
                }
                // Minimización: actualizar los nodos que ya no se comparten con el prefijo común.
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
                
                // Inserción del resto de la palabra actual a partir del prefijo común.
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
        br.close();
        // Minimización del último camino creado para asegurar la integridad del DAWG.
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
    
    /**
     * Busca una palabra completa en el DAWG.
     * <p>
     * La palabra se tokeniza y se recorre la estructura desde la raíz, lanzando una excepción
     * {@code PalabraInvalidaException} si en algún momento no se encuentra la ficha o la
     * palabra no es válida en ese nodo.
     *
     * @param palabra la palabra a buscar.
     * @return {@code true} si la palabra se encuentra en el DAWG.
     * @throws TokenizacionException si la palabra no puede ser tokenizada.
     * @throws PalabraInvalidaException si la palabra no se encuentra en el DAWG.
     */
    public boolean buscarPalabra(String palabra) throws TokenizacionException, PalabraInvalidaException {
        List<String> tokens = tokenizarPalabra(palabra);
        Nodo nodoActual = raiz;
        for (String ficha : tokens) {
            Nodo siguiente = nodoActual.hijos.get(ficha);
            if (siguiente == null) {
                throw new PalabraInvalidaException(palabra);
            }
            nodoActual = siguiente;
        }
        if (!nodoActual.palabraValidaHastaAqui) {
            throw new PalabraInvalidaException(palabra);
        }
        return true;
    }
    
    /**
     * Verifica si existe algún camino en el DAWG que coincida con el prefijo de entrada.
     * <p>
     * Se tokeniza el prefijo y se recorre la estructura; si en algún punto no se encuentra la
     * ficha correspondiente, se lanza una excepción {@code PrefijoNoEncontradoException}.
     *
     * @param prefijo el prefijo a buscar.
     * @return {@code true} si el prefijo existe en el DAWG.
     * @throws TokenizacionException si el prefijo no puede ser tokenizado.
     * @throws PrefijoNoEncontradoException si el prefijo no se encuentra en el DAWG.
     */
    public boolean buscarPrefijo(String prefijo) throws TokenizacionException, PrefijoNoEncontradoException {
        List<String> tokens = tokenizarPalabra(prefijo);
        Nodo nodoActual = raiz;
        for (String ficha : tokens) {
            Nodo siguiente = nodoActual.hijos.get(ficha);
            if (siguiente == null) {
                throw new PrefijoNoEncontradoException(prefijo);
            }
            nodoActual = siguiente;
        }
        return true;
    }
    
    /**
     * Retorna el último nodo en el DAWG siguiendo el camino tokenizado a partir de la palabra dada.
     * <p>
     * Si en algún punto de la tokenización no se encuentra la ficha correspondiente en el grafo,
     * se lanza una excepción {@code NodoNoEncontradoException}.
     *
     * @param palabra la palabra cuya ruta en el DAWG se desea seguir.
     * @return el último nodo alcanzado tras tokenizar la palabra.
     * @throws TokenizacionException si la palabra no puede ser tokenizada.
     * @throws NodoNoEncontradoException si no se encuentra el nodo correspondiente en la estructura.
     */
    public Nodo buscarUltimoNodo(String palabra) throws TokenizacionException, NodoNoEncontradoException {
        List<String> tokens = tokenizarPalabra(palabra);
        Nodo nodoActual = raiz;
        for (String ficha : tokens) {
            Nodo siguiente = nodoActual.hijos.get(ficha);
            if (siguiente == null) {
                throw new NodoNoEncontradoException(palabra);
            }
            nodoActual = siguiente;
        }
        return nodoActual;
    }
}
