package Presentacion.Vistas;

import java.awt.*;
import javax.swing.*;

public class VistaCuenta extends JPanel {

    public VistaCuenta() {
        setBackground(new Color(255, 248, 230));


        JLabel titulo = new JLabel("Cuenta");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(20, 40, 80));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton botonCambiarPassword = crearBotonBlanco("Cambiar Contraseña");
        JButton botonEliminarUsuario = crearBotonBlanco("Eliminar Usuario");

        add(Box.createVerticalStrut(30));
        add(titulo);
        add(Box.createVerticalStrut(30));
      
        add(Box.createVerticalStrut(15));
        add(botonCambiarPassword);
        add(Box.createVerticalStrut(15));
        add(botonEliminarUsuario);
    }

    private JButton crearBotonBlanco(String texto) {
        JButton boton = new JButton(texto);
        boton.setForeground(new Color(20, 40, 80));
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(255, 248, 230));
        boton.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        boton.setPreferredSize(new Dimension(250, 50));
        boton.setMaximumSize(new Dimension(250, 50));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        return boton;
    }
}
