package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VistaSalir extends JFrame {
    
    private JButton botonAbandonar;
    private JButton botonSalir;
    private ActionListener abandonarListener;
    private ActionListener salirListener;
    
    public VistaSalir() {
        setTitle("¿Desea salir?");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(238, 238, 238, 255));
        
        // Panel para el mensaje
        JPanel panelMensaje = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelMensaje.setBackground(getBackground());
        JLabel mensaje = new JLabel("¿Qué desea hacer?");
        mensaje.setFont(new Font("Arial", Font.BOLD, 16));
        mensaje.setForeground(new Color(60, 60, 80));
        panelMensaje.add(mensaje);
        add(panelMensaje, BorderLayout.NORTH);
        
        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(getBackground());
        
        // Botón Abandonar
        botonAbandonar = crearBoton("Abandonar", new Color(255, 100, 100));
        botonAbandonar.addActionListener(e -> {
            if (abandonarListener != null) {
                abandonarListener.actionPerformed(e);
            }
            dispose();
        });
        panelBotones.add(botonAbandonar);
        
        // Botón Salir
        botonSalir = crearBoton("Salir", new Color(100, 150, 255));
        botonSalir.addActionListener(e -> {
            if (salirListener != null) {
                salirListener.actionPerformed(e);
            }
            dispose();
        });
        panelBotones.add(botonSalir);
        
        add(panelBotones, BorderLayout.CENTER);
        
        // Configurar tamaño y posición
        setSize(300, 150);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        boton.setPreferredSize(new Dimension(120, 40));
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    public void setAbandonarListener(ActionListener listener) {
        this.abandonarListener = listener;
    }
    
    public void setSalirListener(ActionListener listener) {
        this.salirListener = listener;
    }
    
    // Helper method for showing the dialog
    public static VistaSalir mostrar() {
        VistaSalir dialog = new VistaSalir();
        dialog.setVisible(true);
        return dialog;
    }
}