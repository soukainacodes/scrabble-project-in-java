package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VistaCambiar extends JFrame {

    // Carga estática de la fuente Montserrat desde recursos
    private static Font montserratRegular;
    private static Font montserratBold;
    private JButton botonCambiar;
    private JTextField campoNombre;
    private JPasswordField campoPassword;
    private JPasswordField campoPasswordActual;

    public VistaCambiar(String asunto) {
        super(
                "nombre".equals(asunto) ? "Cambiar nombre"
                : "password".equals(asunto) ? "Cambiar contraseña"
                : "Aplicación"
        );

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // PANEL PRINCIPAL con BorderLayout
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // PANEL DE FORMULARIO con GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Etiqueta y campo de Nombre
        JLabel nameLabel = new JLabel("nombre".equals(asunto) ? "Nombre:" : "password".equals(asunto) ? "Contraseña actual:" : "");
        nameLabel.setFont(montserratRegular != null
                ? montserratRegular.deriveFont(14f)
                : UIManager.getFont("Label.font").deriveFont(14f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        if (asunto.equals("nombre")) {
            campoNombre = new JTextField(20);
            campoNombre.setFont(montserratRegular != null
                    ? montserratRegular.deriveFont(14f)
                    : UIManager.getFont("TextField.font").deriveFont(14f));
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            formPanel.add(campoNombre, gbc);
        } else {
            campoPasswordActual = new JPasswordField(20);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            formPanel.add(campoPasswordActual, gbc);
        }

        // Reiniciar fill y weight para siguiente fila
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        // Etiqueta y campo de Contraseña
        JLabel passLabel = new JLabel("nombre".equals(asunto) ? "Contraseña:" : "password".equals(asunto) ? "Contraseña Nueva:"  : "");
        passLabel.setFont(montserratRegular != null
                ? montserratRegular.deriveFont(14f)
                : UIManager.getFont("Label.font").deriveFont(14f));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passLabel, gbc);

        campoPassword = new JPasswordField(20);
        campoPassword.setFont(montserratRegular != null
                ? montserratRegular.deriveFont(14f)
                : UIManager.getFont("TextField.font").deriveFont(14f));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(campoPassword, gbc);

        // Agregar el formPanel al centro
        content.add(formPanel, BorderLayout.CENTER);

        // PANEL DE BOTÓN en el SUR, centrado
        botonCambiar = new JButton("Cambiar");
        botonCambiar.setFont(montserratBold != null
                ? montserratBold.deriveFont(14f)
                : UIManager.getFont("Button.font").deriveFont(Font.BOLD, 14f));
        botonCambiar.setBackground(new Color(66, 133, 244));
        botonCambiar.setForeground(Color.WHITE);
        botonCambiar.setOpaque(true);
        botonCambiar.setBorderPainted(false);
        botonCambiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonCambiar.setMargin(new Insets(10, 20, 10, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(botonCambiar);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(content);
        pack();
    }

    public void cambiar(ActionListener l) {
        botonCambiar.addActionListener(l);
    }   

    public String getNombre() {
        return campoNombre.getText();
    }

    public char[] getPassword() {
        return campoPassword.getPassword();
    }

     public char[] getPasswordActual() {
        return campoPasswordActual.getPassword();
    }
}
