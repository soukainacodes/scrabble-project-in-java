package Dominio.Excepciones;

/**
 * Excepción lanzada cuando se intenta acceder o eliminar una partida que no existe.
 * 
 * Esta excepción se utiliza principalmente cuando:
 * <ul>
 *   <li>Se intenta cargar una partida con un identificador que no existe en el sistema</li>
 *   <li>Se intenta eliminar o modificar una partida que ya ha sido borrada</li>
 *   <li>Se busca información sobre una partida que no está en la base de datos</li>
 *   <li>El archivo de la partida ha sido corrompido o no puede ser leído correctamente</li>
 * </ul>
 * 
 */
public class PartidaNoEncontradaException extends Exception {

    /**
     * Construye una nueva PartidaNoEncontradaException con un mensaje predeterminado.
     * El mensaje predeterminado indica que la partida no existe.
     */
    public PartidaNoEncontradaException() {
        super("La partida no existe.");
    }

    /**
     * Construye una nueva PartidaNoEncontradaException con un mensaje personalizado.
     * El mensaje personalizado incluye el ID de la partida que no se encontró.
     *
     * @param id el ID de la partida que causó la excepción.
     */
    public PartidaNoEncontradaException(String id) {
        super(String.format("No existe ninguna partida con ID '%s'.", id));
    }
}