package Dominio.Modelos;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa un nodo en un DAWG (Directed Acyclic Word Graph).
 * <p>
 * Cada nodo almacena:
 * <ul>
 *   <li>Una letra o ficha identificadora (null para la raíz).</li>
 *   <li>Un mapa de hijos que asocia fichas a nodos descendientes.</li>
 *   <li>Un indicador de si marca el final de una palabra válida.</li>
 * </ul>
 */
public class Nodo {

    /** Letra o ficha que identifica el nodo; null en el nodo raíz. */
    private String letra;

    /** Hijos de este nodo: clave = ficha, valor = nodo descendiente. */
    Map<String, Nodo> hijos = new HashMap<>();  // paquete-access para DAWG

    /** Indica si este nodo corresponde al fin de una palabra válida. */
    boolean palabraValidaHastaAqui = false;     // paquete-access para DAWG

    /**
     * Construye un nodo que representa la ficha dada.
     *
     * @param letra Cadena con la ficha (puede ser null para la raíz).
     */
    public Nodo(String letra) {
        this.letra = letra;
    }

    /**
     * Construye el nodo raíz del DAWG (sin letra asociada).
     */
    public Nodo() {
        this(null);
    }

    /**
     * Indica si el nodo marca el final de una palabra válida.
     *
     * @return {@code true} si es un fin de palabra, {@code false} en otro caso.
     */
    public boolean esValida() {
        return palabraValidaHastaAqui;
    }

    /**
     * Obtiene la letra o ficha de este nodo.
     *
     * @return Cadena con la letra, o {@code null} si es la raíz.
     */
    public String getLetra() {
        return letra;
    }

    /**
     * Devuelve el mapa de nodos hijos.
     *
     * @return Mapa (String → Nodo) de hijos.
     */
    public Map<String, Nodo> getHijos() {
        return hijos;
    }

    /**
     * Compara este nodo con otro para igualdad semántica.
     * 
     * Dos nodos son iguales si:
     * <ul>
     *   <li>Tienen misma letra (o ambas null).</li>
     *   <li>Comparten el estado de "fin de palabra".</li>
     *   <li>Tienen idénticos hijos (mismo conjunto de claves y mismas referencias de nodos).</li>
     * </ul>
     *
     * @param obj Objeto a comparar.
     * @return {@code true} si son iguales, {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Nodo otro = (Nodo) obj;
        if (palabraValidaHastaAqui != otro.palabraValidaHastaAqui) return false;
        if (letra == null ? otro.letra != null : !letra.equals(otro.letra)) return false;
        if (hijos.size() != otro.hijos.size()) return false;
        for (Map.Entry<String, Nodo> entrada : hijos.entrySet()) {
            Nodo hijoOtro = otro.hijos.get(entrada.getKey());
            if (hijoOtro == null || entrada.getValue() != hijoOtro) return false;
        }
        return true;
    }

    /**
     * Genera un código hash consistente con {@link #equals(Object)}.
     * 
     * Combina:
     * <ul>
     *   <li>Estado de fin de palabra.</li>
     *   <li>Hash de la letra.</li>
     *   <li>Hash de las entradas en el mapa de hijos (clave y referencia).</li>
     * </ul>
     *
     * @return Código hash del nodo.
     */
    @Override
    public int hashCode() {
        int result = (palabraValidaHastaAqui ? 1 : 0);
        result = 31 * result + (letra != null ? letra.hashCode() : 0);
        for (Map.Entry<String, Nodo> entrada : hijos.entrySet()) {
            result = 31 * result + entrada.getKey().hashCode();
            result = 31 * result + System.identityHashCode(entrada.getValue());
        }
        return result;
    }
}
