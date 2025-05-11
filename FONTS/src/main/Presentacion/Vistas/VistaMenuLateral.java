package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class VistaMenuLateral extends JPanel {

    private JButton botonVerCuenta;
    private JButton botonJugar;

    public VistaMenuLateral() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(255, 248, 230)); // color crema

        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(220, 220, 220))); // línea separadora

        add(Box.createVerticalStrut(10));

        add(Box.createVerticalStrut(30));

        // --- Botones de menú ---
        String[] opciones = {"Ver Cuenta", "Ranking", "Diccionarios", "Añadir Jugador", "Cerrar Sesión"};

        botonVerCuenta = crearBotonMenu("Ver Cuenta");
        add(botonVerCuenta);
        add(Box.createVerticalStrut(20));

        botonJugar = crearBotonMenu("Jugar");
        add(botonJugar);
        add(Box.createVerticalStrut(20));

        JButton botonRanking = crearBotonMenu("Ranking");
        add(botonRanking);
        add(Box.createVerticalStrut(20));

        JButton botonDiccionarios = crearBotonMenu("Diccionarios");
        add(botonDiccionarios);
        add(Box.createVerticalStrut(20));

        JButton botonAddJugador = crearBotonMenu("Añadir Jugador");
        add(botonDiccionarios);
        add(Box.createVerticalStrut(20));

        JButton botonCerrarSesion = crearBotonMenu("Cerrar Sesión");
        add(botonCerrarSesion);
        add(Box.createVerticalStrut(20));


    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(new Color(20, 40, 80));
        boton.setBackground(new Color(255, 248, 230));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 40));

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setForeground(new Color(86, 99, 243)); // Azul al pasar
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setForeground(new Color(20, 40, 80)); // Vuelve a normal
            }
        });

        return boton;
    }


    public void addVerCuentaListener(ActionListener l) {
        botonVerCuenta.addActionListener(l);
    }

    public void addJugarListener(ActionListener l) {
        botonJugar.addActionListener(l);
    }
}
