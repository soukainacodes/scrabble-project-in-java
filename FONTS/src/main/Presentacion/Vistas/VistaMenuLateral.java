package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;


/**
 * Vista del menú lateral de la aplicación.
 * Contiene botones para acceder a diferentes funcionalidades del juego.
 */
public class VistaMenuLateral extends JPanel {

    /**
     * Color fondo de la interfaz.
     */
    private static final Color COLOR_FONDO = new Color(230, 220, 245); 

    /**
     * Color para el texto del menú y botones.
     */
    private static final Color COLOR_TEXTO = new Color(60, 20, 90); // Púrpura oscuro

    /**
     * Color para el efecto hover de los botones.
     */
    private static final Color COLOR_HOVER = new Color(180, 95, 220); 
    
    /**
     * Color para los separadores del menú.
     */
    private static final Color COLOR_SEPARADOR = new Color(200, 180, 230); 
    
    /**
     * Color para los botones en estado normal.
     * Un lila muy suave para que no canse la vista.
     */
    private static final Color COLOR_BOTON_NORMAL = new Color(240, 235, 250); // Lila muy suave para botones


    /**
     * Botones del menú lateral.
     */
    private JButton botonVerCuenta;

    /**
     * Botón para mostrar el emnú de jugador.
     */
    private JButton botonJugar;

    /**
     * Botón para acceder a los recursos del juego.
     */
    private JButton botonRecursos;

    /**
     * Botón para acceder al ranking de jugadores.
     */
    private JButton botonRanking;

    /**
     * Botón para acceder al manual del juego.
     */
    private JButton botonManual;

    /**
     * Botón para cerrar sesión.
     * Este botón tiene un estilo diferente para destacar su función de cierre de sesión.
     */
    private JButton botonCerrarSesion;


    /**
     * Constructor de la vista del menú lateral.
     * Configura el diseño, los botones y sus estilos.
     */
    public VistaMenuLateral() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COLOR_FONDO);
        
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_SEPARADOR),
                BorderFactory.createEmptyBorder(25, 15, 15, 15)
        ));

        JLabel tituloMenu = new JLabel("MENÚ");
        tituloMenu.setFont(new Font("Arial Black", Font.BOLD, 18));
        tituloMenu.setForeground(COLOR_TEXTO);
        tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(tituloMenu);
        
        add(Box.createVerticalStrut(30));
        add(crearSeparadorDecorado());

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

        botonManual = crearBotonMenu("Manual");
        add(botonManual);
        add(crearSeparadorDecorado());

        add(Box.createVerticalGlue());
        
        botonCerrarSesion = crearBotonMenu("Cerrar Sesión");
        botonCerrarSesion.setForeground(new Color(170, 50, 50));
        add(botonCerrarSesion);
        add(Box.createVerticalStrut(20));
    }

    /**
     * Crea un botón con estilo personalizado para el menú lateral.
     * El botón tiene un fondo redondeado y cambia de color al pasar el mouse.
     *
     * @param texto El texto que se mostrará en el botón.
     * @return El botón creado con el estilo personalizado.
     */
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isHovered = getModel().isRollover();
                
                g2.setColor(isHovered ? COLOR_HOVER : COLOR_BOTON_NORMAL);
                
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                    0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.fill(roundedRect);
                
                if (isHovered) {
                    g2.setColor(new Color(255, 255, 255, 80));
                    g2.fillRect(0, 0, getWidth(), getHeight()/2);
                    
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(COLOR_TEXTO);
                }
                
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                g2.drawString(getText(), 
                        (getWidth() - textWidth) / 2,
                        (getHeight() - textHeight) / 2 + fm.getAscent());
                
                g2.dispose();
            }
            
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
    

    /**
     * Crea un separador decorado con un efecto de degradado.
     * Este separador se utiliza para dividir visualmente las secciones del menú.
     *
     * @return Un componente JPanel que actúa como separador decorado.
     */
    private JComponent crearSeparadorDecorado() {
        JPanel separador = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
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


    /**
     * Añade un ActionListener al botón de ver cuenta.
     * @param l El ActionListener a añadir.
     */
    public void addVerCuentaListener(ActionListener l) {
        botonVerCuenta.addActionListener(l);
    }

    /**
     * Añade un ActionListener al botón de jugar.
     * @param l El ActionListener a añadir.
     */
    public void addJugarListener(ActionListener l) {
        botonJugar.addActionListener(l);
    }

    /**
     * Añade un ActionListener al botón de recursos.
     * @param l El ActionListener a añadir.
     */
    public void addVistaRecursos(ActionListener l) {
        botonRecursos.addActionListener(l);
    }

    /**
     * Añade un ActionListener al botón de manual.
     * @param l El ActionListener a añadir.
     */
    public void addVistaManual(ActionListener l) {
        botonManual.addActionListener(l);
    }

    /**
     * Añade un ActionListener al botón de ranking.
     * @param l El ActionListener a añadir.
     */
    public void addVistaRanking(ActionListener l) {
        botonRanking.addActionListener(l);
    }

    /**
     * Añade un ActionListener al botón de cerrar sesión.
     * Este botón tiene un estilo diferente para destacar su función de cierre de sesión.
     * @param l El ActionListener a añadir.
     */
    public void cerrarSesion(ActionListener l) {
        botonCerrarSesion.addActionListener(l);
    }
}