package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class VistaMenuLateral extends JPanel {

    private JButton botonVerCuenta;
    private JButton botonJugar;
    private JButton botonRecursos;
    private  JButton botonRanking;
    private JButton botonCerrarSesion;
    public VistaMenuLateral() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(238,238,238,255)); // color crema 

        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(220, 220, 220))); // línea separadora

        add(Box.createVerticalStrut(10));

        add(Box.createVerticalStrut(30));

        // --- Botones de menú ---
       
        botonVerCuenta = crearBotonMenu("Ver Cuenta");
        add(botonVerCuenta);
        add(Box.createVerticalStrut(20));

        botonJugar = crearBotonMenu("Jugar");
        add(botonJugar);
        add(Box.createVerticalStrut(20));

        botonRanking = crearBotonMenu("Ranking");
        add(botonRanking);
        add(Box.createVerticalStrut(20));

        botonRecursos = crearBotonMenu("Recursos");
        add(botonRecursos);
        add(Box.createVerticalStrut(20));


        botonCerrarSesion = crearBotonMenu("Cerrar Sesión");
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

    public void addVistaRecursos(ActionListener l) {
        botonRecursos.addActionListener(l);
    }

    public void addVistaRanking(ActionListener l) {
        botonRanking.addActionListener(l);
    }

    public void cerrarSesion(ActionListener l) {
        botonCerrarSesion.addActionListener(l);
    }
}
