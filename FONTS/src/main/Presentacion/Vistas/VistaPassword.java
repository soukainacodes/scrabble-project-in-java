package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
public class VistaPassword extends JFrame {

    // Carga estática de la fuente Montserrat desde recursos
    private static Font montserratRegular;
    private static Font montserratBold;
    private JButton botonVerificar;
    private JPasswordField campoPassword;


    public VistaPassword(String username) {
        super("Confirmar contraseña");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        // PANEL PRINCIPAL
        JPanel content = new JPanel();
        content.setBackground(Color.white);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // USUARIO
        JLabel userLabel = new JLabel(username);
        userLabel.setFont(montserratBold != null 
            ? montserratBold.deriveFont(18f) 
            : UIManager.getFont("Label.font").deriveFont(Font.BOLD, 18f));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(userLabel);
        content.add(Box.createVerticalStrut(12));

        // EXPLICACIÓN
        JLabel explanation = new JLabel(
            "<html><div style='text-align:center;'>Por razones de seguridad, "
          + "confirma tu contraseña antes de continuar.</div></html>"
        );
        explanation.setFont(montserratRegular != null 
            ? montserratRegular.deriveFont(12f) 
            : UIManager.getFont("Label.font").deriveFont(12f));
        explanation.setForeground(new Color(80,80,80));
        explanation.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(explanation);
        content.add(Box.createVerticalStrut(20));

        // PANEL DE CONTRASEÑA con GridBag para mejor alineación
        JPanel passPanel = new JPanel(new GridBagLayout());
        passPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(montserratRegular != null 
            ? montserratRegular.deriveFont(14f) 
            : UIManager.getFont("Label.font").deriveFont(14f));
        gbc.gridx = 0;
        passPanel.add(passLabel, gbc);

        campoPassword = new JPasswordField(15);
        campoPassword.setFont(montserratRegular != null 
            ? montserratRegular.deriveFont(14f) 
            : UIManager.getFont("TextField.font").deriveFont(14f));
        gbc.gridx = 1;
        passPanel.add(campoPassword, gbc);

        content.add(passPanel);
        content.add(Box.createVerticalStrut(25));

        // BOTÓN VERIFICAR
        botonVerificar = new JButton("Verificar");
        botonVerificar.setFont(montserratBold != null 
            ? montserratBold.deriveFont(14f) 
            : UIManager.getFont("Button.font").deriveFont(Font.BOLD, 14f));
        botonVerificar.setBackground(new Color(66, 133, 244));
        botonVerificar.setForeground(Color.white);
        botonVerificar.setOpaque(true);
        botonVerificar.setBorderPainted(false);
        botonVerificar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonVerificar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       
        content.add(botonVerificar);

        // SETUP FINAL
        setContentPane(content);
       pack();
      
    }
     public void verificar(ActionListener l) {
        botonVerificar.addActionListener(l);
    }
     public char[] getPassword() {
        return campoPassword.getPassword();
    }
   
}
