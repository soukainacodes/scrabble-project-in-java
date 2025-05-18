package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;

public class VistaMenuLateral extends JPanel {

    // Colores principales
    private static final Color COLOR_FONDO = new Color(230, 220, 245); // Lila flojo/suave
    private static final Color COLOR_TEXTO = new Color(60, 20, 90); // Púrpura oscuro
    private static final Color COLOR_HOVER = new Color(180, 95, 220); // Lila más intenso
    private static final Color COLOR_SEPARADOR = new Color(200, 180, 230); // Lila medio para separadores
    private static final Color COLOR_BOTON_NORMAL = new Color(240, 235, 250); // Lila muy suave para botones

    private JButton botonVerCuenta;
    private JButton botonJugar;
    private JButton botonRecursos;
    private JButton botonRanking;
    private JButton botonCerrarSesion;

    public VistaMenuLateral() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COLOR_FONDO);
        
        // Borde con un degradado sutil en el lado derecho
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_SEPARADOR),
                BorderFactory.createEmptyBorder(25, 15, 15, 15)
        ));

        // Título del menú
        JLabel tituloMenu = new JLabel("MENÚ");
        tituloMenu.setFont(new Font("Arial Black", Font.BOLD, 18));
        tituloMenu.setForeground(COLOR_TEXTO);
        tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(tituloMenu);
        
        add(Box.createVerticalStrut(30));
        add(crearSeparadorDecorado());

        // --- Botones de menú ---
        botonVerCuenta = crearBotonMenu("Ver Cuenta");
        add(botonVerCuenta);
        add(crearSeparadorDecorado());

        botonJugar = crearBotonMenu("Jugar");
        add(botonJugar);
        add(crearSeparadorDecorado());

        botonRanking = crearBotonMenu("Ranking");
        add(botonRanking);
        add(crearSeparadorDecorado());

        botonRecursos = crearBotonMenu("Recursos");
        add(botonRecursos);
        add(crearSeparadorDecorado());

        // Espacio flexible que empuja el botón de cerrar sesión hacia abajo
        add(Box.createVerticalGlue());
        
        // Aplicar un estilo diferente al botón de cerrar sesión
        botonCerrarSesion = crearBotonMenu("Cerrar Sesión");
        botonCerrarSesion.setForeground(new Color(170, 50, 50));
        add(botonCerrarSesion);
        add(Box.createVerticalStrut(20));
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Determinar si el botón está en hover
                boolean isHovered = getModel().isRollover();
                
                // Color de fondo basado en estado
                g2.setColor(isHovered ? COLOR_HOVER : COLOR_BOTON_NORMAL);
                
                // Dibujar fondo con bordes redondeados
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                    0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.fill(roundedRect);
                
                // Dibujar efecto de brillo en la parte superior si está en hover
                if (isHovered) {
                    g2.setColor(new Color(255, 255, 255, 80));
                    g2.fillRect(0, 0, getWidth(), getHeight()/2);
                    
                    // Dibujar texto en blanco si está en hover
                    g2.setColor(Color.WHITE);
                } else {
                    // Dibujar texto en color normal
                    g2.setColor(COLOR_TEXTO);
                }
                
                // Dibujar texto centrado
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                g2.drawString(getText(), 
                        (getWidth() - textWidth) / 2,
                        (getHeight() - textHeight) / 2 + fm.getAscent());
                
                g2.dispose();
            }
            
            // Asegurar que el tamaño preferido incluya espacio para los bordes redondeados
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                return new Dimension(size.width + 20, 40);
            }
        };
        
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 40));
        boton.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        // Cursor de mano al pasar por encima
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        
        return boton;
    }
    
    private JComponent crearSeparadorDecorado() {
        JPanel separador = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar línea con degradado
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(COLOR_SEPARADOR.getRed(), COLOR_SEPARADOR.getGreen(), COLOR_SEPARADOR.getBlue(), 0),
                    getWidth()/2, 0, COLOR_SEPARADOR,
                    true);
                g2.setPaint(gradient);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(10, getHeight()/2, getWidth()-10, getHeight()/2);
                g2.dispose();
            }
        };
        separador.setOpaque(false);
        separador.setPreferredSize(new Dimension(1, 20));
        separador.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
        return separador;
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