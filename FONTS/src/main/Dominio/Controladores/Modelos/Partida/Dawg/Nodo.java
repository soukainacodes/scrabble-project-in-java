package main.Dominio.Controladores.Modelos.Partida.Dawg;

import java.util.HashMap;
import java.util.Map;

/**
 * La clase Nodo representa un nodo de un DAWG (Directed Acyclic Word Graph).
 * Cada nodo contiene:
 * - Una letra (o ficha) que lo identifica.
 * - Un mapa de hijos que relaciona fichas con nodos hijos.
 * - Un indicador de si el nodo marca el final de una palabra válida.
 */
public class Nodo {
    // La letra o ficha que representa este nodo (en caso se ser la raíz, puede ser null)
    private String letra; 

    // Mapa de hijos: cada entrada asocia una ficha (String) a un nodo destino (Nodo)
    Map<String, Nodo> hijos = new HashMap<>();

    // Indica si este nodo marca el final de una palabra válida
    boolean palabraValidaHastaAqui = false;
    
    /**
     * Constructor que inicializa el nodo asignándole la letra o ficha.
     * @param letra La letra que identifica este nodo.
     */
    public Nodo(String letra) {
        this.letra = letra;
    }
    
    /**
     * Constructor para la raíz del DAWG, donde la letra es null.
     */
    public Nodo() {
        this(null);
    }
    
    /**
     * Verifica si este nodo representa el final de una palabra válida.
     * @return true si el nodo marca el fin de una palabra; false en otro caso.
     */
    public boolean esValida() {
        return palabraValidaHastaAqui;
    }
    
    /**
     * Obtiene la letra (o String de la ficha) que representa este nodo.
     * @return La letra del nodo.
     */
    public String getLetra() {
        return letra;
    }
    
    /**
     * Devuelve el mapa de hijos que contiene las conexiones hacia otros nodos.
     * @return El mapa de hijos (clave: ficha de tipo Strong; valor: nodo hijo).
     */
    public Map<String, Nodo> getHijos() {
        return hijos;
    }

    /**
     * Compara este nodo con otro objeto para determinar si son iguales.
     * Dos nodos se consideran iguales si:
     * - Tienen la misma letra (o ambas son null).
     * - Tienen el mismo valor en 'palabraValidaHastaAqui'.
     * - Tienen la misma cantidad de hijos y cada hijo se compara por clave y por referencia.
     *
     * @param obj El objeto a comparar.
     * @return true si los nodos son iguales; false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        // Si ambos objetos son la misma instancia, son iguales.
        if (this == obj)
            return true;
        // Si el objeto es null o no es de la misma clase, no son iguales.
        if (obj == null || getClass() != obj.getClass())
            return false;
        // Convertimos el objeto a Nodo para comparar sus atributos.
        Nodo otro = (Nodo) obj;
        
        // Comparamos la letra considerando que puede ser null.
        if (letra == null) {
            if (otro.letra != null)
                return false;
        } else if (!letra.equals(otro.letra)) {
            return false;
        }
        
        // Comparamos el indicador de fin de palabra.
        if (this.palabraValidaHastaAqui != otro.palabraValidaHastaAqui)
            return false;
        
        // Verificamos que ambos nodos tengan el mismo número de hijos.
        if (this.hijos.size() != otro.hijos.size())
            return false;
        
        // Recorremos el mapa de hijos y comprobamos que cada entrada sea idéntica.
        for (Map.Entry<String, Nodo> entrada : this.hijos.entrySet()) {
            Nodo hijoOtro = otro.hijos.get(entrada.getKey());
            // Se compara por referencia: deben ser el mismo objeto.
            if (hijoOtro == null || entrada.getValue() != hijoOtro)
                return false;
        }
        return true;
    }
    
    /**
     * Se sobreescribe el método hashCode para proporcionar un código hash único para este nodo.
     * Calcula el código hash del nodo basándose en sus atributos:
     * - La bandera 'palabraValidaHastaAqui'
     * - La letra del nodo
     * - Las entradas del mapa de hijos (clave y la referencia del hijo)
     *
     * Se usa el número 31 como multiplicador (por ser un número primo y por eficiencia)
     * para combinar de forma única los atributos y obtener una buena distribución.
     *
     * @return El valor hash del nodo.
     */
    @Override
    public int hashCode() {
        // Inicializa 'resultado' en función de la bandera: 1 si es fin de palabra, 0 si no.
        int resultado = (palabraValidaHastaAqui ? 1 : 0);
        // Incorpora la letra. Si es null, suma 0; sino, suma el hash de la letra.
        resultado = 31 * resultado + (letra == null ? 0 : letra.hashCode());
        // Para cada hijo, se incorpora el hash de la clave y el hash de la referencia del hijo
        for (Map.Entry<String, Nodo> entrada : hijos.entrySet()) {
            // La clave (ficha) influye en el hash.
            resultado = 31 * resultado + entrada.getKey().hashCode();
            // Se utiliza System.identityHashCode() para obtener el hash basado en la referencia
            resultado = 31 * resultado + System.identityHashCode(entrada.getValue());
        }
        return resultado;
    }
    
    /**
     * Retorna una representación en cadena del nodo que incluye:
     * - La letra del nodo.
     * - El indicador 'palabraValidaHastaAqui'.
     * - Las claves (fichas) del mapa de hijos.
     *
     * @return Una cadena descriptiva del nodo.
     */
    @Override
    public String toString() {
        return "Nodo {letra=" + letra 
                + ", palabraValidaHastaAqui=" + palabraValidaHastaAqui 
                + ", hijos=" + hijos.keySet() + "}";
    }
}
