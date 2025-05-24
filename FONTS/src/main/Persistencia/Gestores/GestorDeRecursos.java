package Persistencia.Gestores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Dominio.Excepciones.BolsaNoEncontradaException;
import Dominio.Excepciones.DiccionarioNoEncontradoException;
import Dominio.Excepciones.RecursoExistenteException;
import Dominio.Excepciones.RecursoNoExistenteException;

import Persistencia.Gestores.Utilidades.Utils;

/**
 * Clase GestorDeRecursos.
 * Esta clase se encarga de gestionar los recursos del sistema, como diccionarios y bolsas de letras.
 * Permite crear, modificar, eliminar y listar recursos, así como verificar su existencia.
 */
public class GestorDeRecursos {


    /**
     * Ruta donde se encuentran los recursos.
     * Se asume que los recursos están organizados en subdirectorios por idioma.
     */
    private static final String RECURSOS = "FONTS/src/main/Persistencia/Datos/Recursos/";
    
    
    /**
     * Instancia de la clase Utils para operaciones comunes.
     * Se utiliza para leer archivos de texto y otras utilidades.
     */
    private Utils utils = new Utils();


    /**
     * Constructor de la clase GestorDeRecursos.
     */
    public GestorDeRecursos() {
        // Constructor vacío
    }

    /**
     * Verifica si un recurso existe en el sistema.
     * 
     * @param id ID del recurso
     * @return true si el recurso existe, false en caso contrario
     */
    public boolean existeRecurso(String id) {
        // Verifica si el directorio del idioma existe
        File dir = new File(RECURSOS + id);
        return dir.exists() && dir.isDirectory();
    }

    /**
     * Lista todos los recursos disponibles en el sistema.
     * 
     * @return Lista de nombres de recursos
     */
    public List<String> listarRecursos() {
        List<String> idiomas = new ArrayList<>();
        File dir = new File(RECURSOS);
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles(File::isDirectory))) {
                idiomas.add(file.getName());
            }
        }
        return idiomas;
    }

    
    /**
     * Obtiene el diccionario de un recurso.
     * 
     * @param id ID del recurso
     * @return Lista de palabras del diccionario
     * @throws IOException Si ocurre un error de E/S
     */
    public List<String> obtenerDiccionario(String id) throws IOException {
        List<String> palabras = new ArrayList<>();
        File diccionarioFile = new File(RECURSOS + id, id + "_diccionario.txt");
        if (diccionarioFile.exists() && diccionarioFile.isFile()) {
            palabras = utils.leerArchivoTexto(diccionarioFile.getPath());
        } else {
            throw new IOException("No se encontró el diccionario para el idioma: " + id);
        }
        return palabras;
    }

  
   
    /**
     * Obtiene la bolsa de un recurso.
     * 
     * @param id ID del recurso
     * @return Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException Si ocurre un error de E/S
     */
    public List<String> obtenerBolsa(String id) throws IOException {
        List<String> bolsa = new ArrayList<>();
        File bolsaFile = new File(RECURSOS + id, id + "_bolsa.txt");
        if (bolsaFile.exists() && bolsaFile.isFile()) {
            bolsa = utils.leerArchivoTexto(bolsaFile.getPath());
        } else {
            throw new IOException("No se encontró la bolsa para el idioma: " + id);
        }
        return bolsa;
    }

    /**
     * Crea un nuevo recurso en el sistema.
     * 
     * @param id       Identificador único del recurso
     * @param palabras Lista de palabras para el diccionario
     * @param bolsa    Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException               Si ocurre un error de E/S
     * @throws RecursoExistenteException Si el recurso ya existe
     */
    public void crearRecurso(String id, List<String> palabras, List<String> bolsa)
            throws IOException, RecursoExistenteException {
        // Verifica si el recurso ya existe
        if (existeRecurso(id)) {
            throw new RecursoExistenteException(id);
        }

        // Crea el directorio del recurso si no existe
        File dir = new File(RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear el directorio: " + dir.getPath());
        }

        // Crea el archivo del diccionario
        crearDiccionario(id, palabras);

        // Crea el archivo de la bolsa directamente con la lista de strings
        crearBolsa(id, bolsa);
    }

    /**
     * Crea un nuevo archivo de diccionario para un recurso.
     * 
     * @param id       Identificador del recurso
     * @param palabras Lista de palabras para el diccionario
     * @throws IOException               Si ocurre un error al escribir el archivo
     * @throws RecursoExistenteException Si el recurso ya existe
     */
    public void crearDiccionario(String id, List<String> palabras) throws IOException, RecursoExistenteException {

        // Crea el directorio del recurso si no existe
        File dir = new File(RECURSOS, id);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear el directorio: " + dir.getPath());
        }

        // Crea el archivo del diccionario
        File diccionarioFile = new File(dir, id + "_diccionario.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(diccionarioFile))) {
            for (String palabra : palabras) {
                writer.write(palabra);
                writer.newLine();
            }
        }
    }

    /**
     * Crea un nuevo archivo de bolsa para un recurso.
     * 
     * @param id    Identificador del recurso
     * @param bolsa Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void crearBolsa(String id, List<String> bolsa) throws IOException {
        // Crea el archivo de la bolsa
        File bolsaFile = new File(RECURSOS + id, id + "_bolsa.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bolsaFile))) {
            for (String linea : bolsa) {
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    /**
     * Modifica un recurso existente en el sistema.
     * 
     * @param id       Identificador único del recurso
     * @param palabras Lista de palabras para el diccionario
     * @param bolsaData    Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException               Si ocurre un error de E/S
     * @throws RecursoNoExistenteException Si el recurso no existe
     */
    public void modificarRecurso(String id, List<String> palabras, List<String> bolsaData)
            throws IOException, RecursoNoExistenteException {
        // Verifica si el recurso existe
        if (!existeRecurso(id)) {
            throw new IOException("El recurso no existe: " + id);
        }

        // Modifica el diccionario
        modificarDiccionario(id, palabras);

        // Modifica la bolsa
        modificarBolsa(id, bolsaData);
    }

    /**
     * Modifica el archivo de bolsa para un recurso.
     * 
     * @param id    Identificador del recurso
     * @param bolsa Lista de strings con formato "LETRA FRECUENCIA PUNTOS"
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void modificarBolsa(String id, List<String> bolsa) throws IOException {
        // Crea el archivo de la bolsa
        File bolsaFile = new File(RECURSOS + id, id + "_bolsa.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bolsaFile))) {
            for (String linea : bolsa) {
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    /**
     * Modifica el archivo de diccionario para un recurso.
     * 
     * @param id       Identificador del recurso
     * @param palabras Lista de palabras para el diccionario
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void modificarDiccionario(String id, List<String> palabras)
            throws IOException {
        File diccionarioFile = new File(RECURSOS + id, id + "_diccionario.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(diccionarioFile))) {
            for (String palabra : palabras) {
                writer.write(palabra);
                writer.newLine();
            }
        }
    }

    /**
     * Elimina un recurso del sistema.
     * 
     * @param id Identificador del recurso
     * @throws DiccionarioNoEncontradoException Si el diccionario no se encuentra
     * @throws BolsaNoEncontradaException       Si la bolsa no se encuentra
     * @throws IOException                     Si ocurre un error de E/S
     */
    public void eliminarRecurso(String id)
            throws DiccionarioNoEncontradoException, BolsaNoEncontradaException, IOException {
        // Define la ruta de la carpeta del recurso
        File recursoDir = new File(RECURSOS + id);

        // Verifica si la carpeta existe
        if (!recursoDir.exists() || !recursoDir.isDirectory()) {
            throw new IOException("El recurso no existe: " + id);
        }

        // Verifica si el diccionario existe
        File diccionarioFile = new File(recursoDir, id + "_diccionario.txt");
        if (!diccionarioFile.exists()) {
            throw new DiccionarioNoEncontradoException("No se encontró el diccionario para el idioma: " + id);
        }

        // Verifica si la bolsa existe
        File bolsaFile = new File(recursoDir, id + "_bolsa.txt");
        if (!bolsaFile.exists()) {
            throw new BolsaNoEncontradaException("No se encontró la bolsa para el idioma: " + id);
        }

        // Elimina los archivos del recurso
        if (!diccionarioFile.delete()) {
            throw new IOException("No se pudo eliminar el diccionario: " + diccionarioFile.getName());
        }
        if (!bolsaFile.delete()) {
            throw new IOException("No se pudo eliminar la bolsa: " + bolsaFile.getName());
        }

        // Elimina la carpeta del recurso
        if (!recursoDir.delete()) {
            throw new IOException("No se pudo eliminar la carpeta del recurso: " + id);
        }
    }
    
}
