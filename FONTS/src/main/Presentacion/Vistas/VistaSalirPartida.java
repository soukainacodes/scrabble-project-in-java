package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Vista que se muestra cuando el usuario quiere salir de una partida en curso.
 * Ofrece opciones para abandonar la partida (perdiéndola) o simplemente salir (guardándola).
 */
public class VistaSalirPartida extends JFrame {
    
    // Colores
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Color crema de fondo 
    private static final Color PANEL_COLOR = new Color(244, 236, 217);   // Color para paneles internos
    private static final Color BORDER_COLOR = new Color(200, 180, 120);  // Color de bordes
    
    private JButton botonAbandonar;
    private JButton botonSalir;
    private ActionListener abandonarListener;
    private ActionListener salirListener;
    
    public VistaSalirPartida() {
        // Configuración básica de la ventana
        setTitle("¿Desea salir?");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 20));
        
        // Panel principal con fondo estilo Scrabble
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
        panelPrincipal.setBackground(APP_BG_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(panelPrincipal);
        
        // Panel para el mensaje principal
        JPanel panelMensaje = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelMensaje.setOpaque(false);
        JLabel mensaje = new JLabel("¿Qué desea hacer?");
        mensaje.setFont(new Font("Arial", Font.BOLD, 20));
        mensaje.setForeground(new Color(60, 60, 80));
        panelMensaje.add(mensaje);
        panelPrincipal.add(panelMensaje, BorderLayout.NORTH);

        // Panel para el texto de advertencia con estilo mejorado
        JPanel panelAdvertencia = new JPanel();
        panelAdvertencia.setOpaque(false);
        panelAdvertencia.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel advertencia = new JLabel("<html><center>Recuerda que, si eliges <b>«Abandonar»</b>, la partida se dará<br>por finalizada y no podrás retomarla</center></html>");
        advertencia.setFont(new Font("Arial", Font.ITALIC, 14));
        advertencia.setForeground(new Color(180, 0, 0));
        panelAdvertencia.add(advertencia);
        
        // Envolver el panel de advertencia en otro panel para añadir padding
        JPanel panelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentral.setOpaque(false);
        panelCentral.add(panelAdvertencia);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panelBotones.setOpaque(false);
        
        // Botón Abandonar (rojo)
        botonAbandonar = crearBoton("Abandonar", new Color(220, 60, 60));
        botonAbandonar.addActionListener(e -> {
            if (abandonarListener != null) {
                abandonarListener.actionPerformed(e);
            }
            dispose();
        });
        panelBotones.add(botonAbandonar);
        
        // Botón Salir (azul)
        botonSalir = crearBoton("Salir", new Color(100, 150, 255));
        botonSalir.addActionListener(e -> {
            if (salirListener != null) {
                salirListener.actionPerformed(e);
            }
            dispose();
        });
        panelBotones.add(botonSalir);
        
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        // Configurar tamaño y posición
        setSize(510, 350);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Crea un botón con estilo personalizado.
     * @param texto Texto del botón
     * @param color Color base del botón
     * @return Botón estilizado
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                // Dibujar botón con esquinas redondeadas
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Determinar color según estado
                Color baseColor = getModel().isPressed() ? color.darker() : 
                            (getModel().isRollover() ? color.brighter() : color);
                
                // Dibujar fondo con gradiente
                g2.setPaint(new GradientPaint(
                    0, 0, baseColor.brighter(),
                    0, getHeight(), baseColor
                ));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                // Dibujar borde
                g2.setColor(baseColor.darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2.dispose();
                
                // Asegurar que el texto se dibuje encima
                super.paintComponent(g);
            }
        };
        
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setPreferredSize(new Dimension(150, 45));
        
        // Efecto hover con cursor de mano
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }
    
    /**
     * Establece el listener para el botón Abandonar.
     * @param listener ActionListener a ejecutar cuando se pulse el botón
     */
    public void setAbandonarListener(ActionListener listener) {
        this.abandonarListener = listener;
    }
    
    /**
     * Establece el listener para el botón Salir.
     * @param listener ActionListener a ejecutar cuando se pulse el botón
     */
    public void setSalirListener(ActionListener listener) {
        this.salirListener = listener;
    }
    
    /**
     * Método auxiliar para crear y mostrar el diálogo.
     * @return Instancia de VistaSalir visible
     */
    public static VistaSalirPartida mostrar() {
        VistaSalirPartida dialog = new VistaSalirPartida();
        dialog.setVisible(true);
        return dialog;
    }
}