
package Presentacion;

import Dominio.*;
import Presentacion.Vistas.*;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.SwingUtilities; // si usas invokeLater
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.Box;

public class CtrlPresentacion {

    private CtrlDominio ctrl = new CtrlDominio();
    
    public CtrlPresentacion(){
        vistaPrincipal();
    }
    public static void vistaPrincipal() {
           UIManager.put("Label.font",  new Font("Droid Sans Fallback", Font.BOLD, 23));
    UIManager.put("Button.font", new Font("Tahoma", Font.PLAIN, 16));
        VistaMenuPrincipal vMP = new VistaMenuPrincipal();

    }   

}