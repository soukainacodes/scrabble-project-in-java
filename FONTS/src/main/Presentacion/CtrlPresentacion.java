package Presentacion;

import Dominio.CtrlDominio;
import Presentacion.Vistas.*;
import java.awt.Font;
import javax.swing.UIManager;

public class CtrlPresentacion {

    private CtrlDominio ctrlDominio = new CtrlDominio();

    //Vistas
    private VistaLogin vLogin;
    private VistaMenuPrincipal vMenuPrincipal;
    private VistaMenuLateral vMenuLateral;
    private VistaPantallaPrincipal vPantallaPrincipal;
    private VistaRanking vRanking;
    private VistaCuenta vCuenta;
    private VistaRecursos vRecursos;

    private VistaCargarPartida vCargarPartida;
    private VistaCrearPartida vCrearPartida;
    private VistaScrabble vScrabble;

    private VistaExplorador vAddRecurso;

    private boolean sesionIniciada = false;

    public CtrlPresentacion() {
        vistaPrincipal();
    }

    private void configuracion() {
        Font fuenteGlobal = new Font("Arial", Font.PLAIN, 24);
        UIManager.put("Button.font", fuenteGlobal);
        UIManager.put("Label.font", fuenteGlobal);
        UIManager.put("TextField.font", fuenteGlobal);
        UIManager.put("PasswordField.font", fuenteGlobal);
        UIManager.put("ComboBox.font", fuenteGlobal);
        UIManager.put("CheckBox.font", fuenteGlobal);
        UIManager.put("RadioButton.font", fuenteGlobal);
        UIManager.put("Menu.font", fuenteGlobal);
        UIManager.put("MenuItem.font", fuenteGlobal);
    }

    private void vistaPrincipal() {

        configuracion();

    }

    private void crearVistaLogin() {
        vLogin = new VistaLogin();
        if (!sesionIniciada) {
            vLogin.addVistaPrincipal(e -> crearVistaMenuPrincipal());
        } else {
           // vLogin.addVistaPrincipal(e -> crearVistaMenuPrincipal());
        }

    }

    private void crearVistaMenuPrincipal() {

        String usuario = vLogin.getNombre();
        String password = new String(vLogin.getPassword());
        System.out.println(password);
        try {
            ctrlDominio.iniciarSesion(usuario, password);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    //VISTAS
    private void crearVistaMenuLateral() {

    }

    private void crearVistaPantallaPrincipal() {

    }
    //Vistas del menu principal

    private void crearVistaCrearPartida() {

    }

    private void crearVistaCargarPartida() {

    }

    //Vistas del menu principal
    private void crearVistaCuenta() {

    }

    private void crearVistaRanking() {

    }

    private void crearVistaRecursos() {

    }

}
