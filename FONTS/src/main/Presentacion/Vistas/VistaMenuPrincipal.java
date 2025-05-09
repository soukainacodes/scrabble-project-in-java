
package Presentacion.Vistas;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.*;
public class VistaMenuPrincipal extends JFrame {

    private final JLabel titolVista = new JLabel(" こんにちは世");
    private final JButton iniciarSesionButton = new JButton("Iniciar Sesión");
    private final JButton registrarseButton = new JButton("Registrarse");
    private final JButton salirButton = new JButton("Salir");
      
    public VistaMenuPrincipal() {
   //   UIManager.put("Label.font",  new Font("Arial", Font.BOLD, 23));
      
    setTitle("안녕하세요");     
    setSize(500, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Fijar tamaño preferido a los botones
    Dimension tamBoton = new Dimension(150, 40);
    iniciarSesionButton.setPreferredSize(tamBoton);
    registrarseButton.setPreferredSize(tamBoton);
    salirButton.setPreferredSize(tamBoton);

    // Panel con GridBagLayout
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(Color.PINK);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 40, 10, 40); // Margen: top, left, bottom, right
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTH;    // Alinea arriba

    // Título: permite expansión horizontal
    gbc.gridy   = 0;
    gbc.fill    = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.weighty = 0;
    panel.add(titolVista, gbc);

    // Botones: SIN expansión, tamaño fijo
    gbc.fill    = GridBagConstraints.NONE;
    gbc.weightx = 0;

    gbc.gridy = 1;
    panel.add(iniciarSesionButton, gbc);

    gbc.gridy = 2;
    panel.add(registrarseButton, gbc);

    gbc.gridy = 3;
    panel.add(salirButton, gbc);

    // Relleno para empujar los botones hacia arriba
    gbc.gridy   = 4;
    gbc.weighty = 1;
    panel.add(Box.createVerticalGlue(), gbc);

    setContentPane(panel);
    setVisible(true);
}


}
