package Dominio.Excepciones;

/**
 * Excepción lanzada cuando no se encuentra un diccionario solicitado.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta acceder a un diccionario que no existe en el sistema</li>
 *   <li>El archivo de definición del diccionario no puede ser encontrado</li>
 *   <li>Se intenta cargar un diccionario cuyo identificador no está registrado</li>
 * </ul>
 * 
 */
public class DiccionarioNoEncontradoException extends Exception {
    private static final String DEFAULT_MSG = "Diccionario no encontrado.";

    /**
     * Construye una nueva DiccionarioNoEncontradoException con un mensaje predeterminado.
     * El mensaje predeterminado incluye una sugerencia para verificar el nombre del diccionario.
     */
    public DiccionarioNoEncontradoException() {
        super(DEFAULT_MSG + " Por favor, verifique el nombre del diccionario.");
    }

    /**
     * Construye una nueva DiccionarioNoEncontradoException con un mensaje personalizado.
     *
     * @param message el mensaje detallado que describe el error.
     */
    public DiccionarioNoEncontradoException(String message) {
        super(message);
    }
}