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
        configuracion();
        crearVistaLogin();
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
            vMenuPrincipal = new VistaMenuPrincipal();
            crearVistaMenuLateral();
            crearVistaPantallaPrincipal();
            vLogin.revalidate();
            vLogin.repaint();
            vLogin.setContentPane(vMenuPrincipal);
            vLogin.setLocationRelativeTo(null);
            vLogin.pack();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    //VISTAS
    private void crearVistaMenuLateral() {
        vMenuLateral = new VistaMenuLateral();
        vMenuPrincipal.addMenuLateral(vMenuLateral);

        vPantallaPrincipal = new VistaPantallaPrincipal();

        vCuenta = new VistaCuenta();
        vRanking = new VistaRanking();
        vRecursos = new VistaRecursos();

        //Esto no se si deberia ir aqui
        vCargarPartida = new VistaCargarPartida();
        vCrearPartida = new VistaCrearPartida();

        vMenuPrincipal.addCard("PRINCIPAL", vPantallaPrincipal);
        vMenuPrincipal.addCard("CUENTA", vCuenta);
        vMenuPrincipal.addCard("RANKING", vRanking);
        vMenuPrincipal.addCard("CARGARPARTIDA", vCargarPartida);

        vMenuPrincipal.addCard("CREARPARTIDA", vCrearPartida);

        vMenuLateral.addVerCuentaListener(e -> vMenuPrincipal.muestraCard("CUENTA"));
        vMenuLateral.addJugarListener(e -> vMenuPrincipal.muestraCard("PRINCIPAL"));
        vMenuLateral.addVistaRecursos(e -> vMenuPrincipal.muestraCard("RECURSOS"));
        vMenuLateral.addVistaRanking(e -> vMenuPrincipal.muestraCard("RANKING"));
        vMenuLateral.cerrarSesion(e -> cerrarSesion());

        vPantallaPrincipal.addVistaCrearPartida(e -> vMenuPrincipal.muestraCard("CREARPARTIDA"));
        vPantallaPrincipal.addVistaCargarPartida(e -> vMenuPrincipal.muestraCard("CARGARPARTIDA"));
       // vPantallaPrincipal.jugarScrabble(e -> jugarPartida());
        vCrearPartida.jugarPartida(e -> jugarPartida());
        vCargarPartida.jugarPartida(e -> jugarPartida());
        //    crearPartida.jugarPartida(e -> jugarPartida());
        //  cargarPartida.jugarPartida(e -> jugarPartida());
        //  vMenuLateral.addJugarListener(e -> muestraCard(BIENVENIDA));
        //  vMenuLateral.addVistaRecursos(e -> muestraCard(RECURSOS));
        //  vMenuLateral.addVistaRanking(e -> muestraCard("RANKING"));
        //  vMenuLateral.cerrarSesion(e -> cerrarSesion());
    }

    private void cerrarSesion() {

        ctrlDominio.cerrarSesion();
        vLogin.dispose();
        crearVistaLogin();

    }

    private void jugarPartida() {

        vScrabble = new VistaScrabble();
        vMenuPrincipal.jugarPartida(vScrabble);
    }

    private void crearVistaPantallaPrincipal() {

        //   vMenuPrincipal.addCard("BIENVENIDA", vPantallaPrincipal);
        //   vMenuPrincipal.addCard("BIENVENIDA", vPantallaPrincipal);
        //   vMenuPrincipal.addCard("BIENVENIDA", vPantallaPrincipal);
    }
    //Vistas del menu principal

    private void crearVistaCrearPartida() {

    }

    private void crearVistaCargarPartida() {

    }

    //Vistas del menu lateral
    private void crearVistaCuenta() {

    }

    private void crearVistaRanking() {

    }

    private void crearVistaRecursos() {

    }

}
