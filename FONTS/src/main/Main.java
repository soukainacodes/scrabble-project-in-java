package main;

import Presentacion.*;
import java.io.IOException;


/* Clase Main:
 * Punto de entrada de la aplicacion.
 * Crea una instancia de CtrlPresentacion que se encargara de gestionar la presentación de la aplicacion.
 */
public class Main {

    // Atributo que almacena la instancia de CtrlPresentacion
    private static CtrlPresentacion ctrlPresentacion;

    



    // Metodo main: Punto de entrada de la aplicacion
    public static void main(String args[]) throws IOException {

            // Inicializa la instancia de CtrlPresentacion
            ctrlPresentacion = new CtrlPresentacion();

    }
}