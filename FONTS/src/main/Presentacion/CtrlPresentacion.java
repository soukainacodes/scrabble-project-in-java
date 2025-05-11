
package Presentacion;

import Dominio.*;
import Presentacion.Vistas.*;
import java.awt.Font;
import javax.swing.UIManager;

public class CtrlPresentacion {

    private CtrlDominio ctrl = new CtrlDominio();
    
    public CtrlPresentacion(){
        vistaPrincipal();
    }
    public static void vistaPrincipal() {
Font fuenteGlobal = new Font("Arial", Font.PLAIN, 24);

// Aplicar la fuente global
UIManager.put("Button.font", fuenteGlobal);
UIManager.put("Label.font", fuenteGlobal);
UIManager.put("TextField.font", fuenteGlobal);
UIManager.put("PasswordField.font", fuenteGlobal);
UIManager.put("ComboBox.font", fuenteGlobal);
UIManager.put("CheckBox.font", fuenteGlobal);
UIManager.put("RadioButton.font", fuenteGlobal);
UIManager.put("Menu.font", fuenteGlobal);
UIManager.put("MenuItem.font", fuenteGlobal);
        VistaLogin vMP = new VistaLogin();

    }   

}