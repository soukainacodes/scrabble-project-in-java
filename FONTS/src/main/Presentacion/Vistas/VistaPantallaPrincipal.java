package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VistaPantallaPrincipal extends JPanel {

    // Colores consistentes con otras vistas
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);
    private static final Color LILA_CLARO = new Color(180, 95, 220);
    private static final Color LILA_OSCURO = new Color(52, 28, 87);
    private static final Color AZUL_CLARO = new Color(95, 170, 220);
    private static final Color AZUL_OSCURO = new Color(20, 40, 80);
    
    private JButton botonCrearPartida;
    private JButton botonUltimaPartida;
    private JButton botonCargarPartida;
    private String nombre = "";
    private JLabel mensajeBienvenida;
    
    public VistaPantallaPrincipal() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        // Crear y añadir componentes
        JPanel titlePanel = createTitlePanel();
        JPanel mainPanel = createMainPanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(700, 520)); // Consistente con VistaCuenta
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(APP_BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        // Cambiar título a "A JUGAR!"
        String[] letras = { "A", " ", "J", "U", "G", "A", "R", "!" };
        Color[] colores = {
            new Color(220, 130, 95),   // Naranja rojizo
            APP_BG_COLOR,              // Espacio (color de fondo)
            new Color(95, 170, 220),   // Azul claro
            new Color(220, 180, 95),   // Amarillo
            new Color(150, 220, 95),   // Verde
            new Color(220, 95, 160),   // Rosa
            new Color(180, 95, 220),   // Morado/Lila
            new Color(235, 140, 80)    // Naranja
        };
        
        JPanel fichasPanel = new JPanel();
        fichasPanel.setLayout(new BoxLayout(fichasPanel, BoxLayout.X_AXIS));
        fichasPanel.setBackground(APP_BG_COLOR);
        fichasPanel.add(Box.createHorizontalGlue());
        
        for (int i = 0; i < letras.length; i++) {
            final int idx = i;
            
            // Si es un espacio, agregar espacio en blanco
            if (letras[i].equals(" ")) {
                fichasPanel.add(Box.createHorizontalStrut(25));
                continue;
            }
            
            JLabel letra = new JLabel(letras[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                    );
                    // Dibuja ficha
                    g2.setColor(colores[idx]);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    // Sombra
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(3, 3, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            
            letra.setFont(new Font("Arial Black", Font.BOLD, 28));
            letra.setForeground(Color.WHITE);
            letra.setHorizontalAlignment(SwingConstants.CENTER);
            letra.setPreferredSize(new Dimension(40, 40));
            letra.setMaximumSize(new Dimension(40, 40));
            
            // Efecto hover al pasar el ratón
            letra.addMouseListener(new MouseAdapter() {
                @Override 
                public void mouseEntered(MouseEvent e) {
                    letra.setForeground(new Color(255, 255, 200));
                }
                
                @Override 
                public void mouseExited(MouseEvent e) {
                    letra.setForeground(Color.WHITE);
                }
            });
            
            fichasPanel.add(letra);
            
            // Añadir espacio entre letras
            if (i < letras.length - 1 && !letras[i+1].equals(" ")) 
                fichasPanel.add(Box.createHorizontalStrut(5));
        }
        
        fichasPanel.add(Box.createHorizontalGlue());
        panel.add(fichasPanel);
        
        return panel;
    }

    private JPanel createMainPanel() {
        // Panel con marco redondeado y sombra (consistente con VistaCuenta)
        JPanel marcoPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra externa mejorada
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 22, 22);
                
                // Borde más definido
                g2.setColor(new Color(220, 200, 150));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Gradiente interior mejorado
                GradientPaint gp = new GradientPaint(
                    0, 0, APP_BG_COLOR.brighter(), 
                    0, getHeight(), APP_BG_COLOR
                );
                g2.setPaint(gp);
                g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 18, 18);
                g2.dispose();
            }
        };
        marcoPanel.setOpaque(false);
        marcoPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Panel contenido principal con GridBagLayout
        JPanel contenidoPanel = new JPanel(new GridBagLayout());
        contenidoPanel.setOpaque(false);
        
        // Mensaje de bienvenida
        mensajeBienvenida = new JLabel("Bienvenido!");
        mensajeBienvenida.setFont(new Font("Arial", Font.BOLD, 26)); // Tamaño aumentado a 26 como en VistaCuenta
        mensajeBienvenida.setForeground(AZUL_OSCURO);
        mensajeBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Crear botones con el estilo mejorado
        botonCrearPartida = createStylishButton("Nueva Partida", LILA_CLARO);
        botonUltimaPartida = createStylishButton("Última Partida", AZUL_CLARO);
        botonCargarPartida = createStylishButton("Cargar Partida", LILA_CLARO);
        
        // Configuración para posicionamiento absoluto
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Añadir elementos al panel
        gbc.insets = new Insets(10, 0, 30, 0);  // Más espacio después del título
        contenidoPanel.add(mensajeBienvenida, gbc);
        
        gbc.insets = new Insets(10, 0, 10, 0);  // Espacio normal entre botones
        contenidoPanel.add(botonCrearPartida, gbc);
        contenidoPanel.add(botonUltimaPartida, gbc);
        contenidoPanel.add(botonCargarPartida, gbc);
        
        // Añadir el panel de contenido al marco
        marcoPanel.add(contenidoPanel, BorderLayout.CENTER);
        
        return marcoPanel;
    }
    
    private JButton createStylishButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Radio de las esquinas redondeadas
                int radius = 15;
                
                // Determinar estados del botón
                boolean isHovered = getModel().isRollover();
                boolean isPressed = getModel().isPressed();
                
                // Color base según estado
                Color bg = isPressed ? baseColor.darker().darker() : 
                          (isHovered ? baseColor.darker() : baseColor);
                
                // Efecto de sombra si está en hover
                if (isHovered && !isPressed) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, radius, radius);
                }
                
                // Dibujar el fondo del botón con gradiente
                g2.setPaint(new GradientPaint(0, 0,
                    new Color(Math.min(bg.getRed() + 25, 255), 
                              Math.min(bg.getGreen() + 25, 255), 
                              Math.min(bg.getBlue() + 25, 255)),
                    0, getHeight(), bg));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                // Efecto de brillo en la parte superior
                if (!isPressed) {
                    g2.setColor(new Color(255, 255, 255, 70));
                    g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() / 2 - 2, radius, radius);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Consistente con botones de VistaCuenta
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(300, 45)); // Tamaño consistente
        
        // Efecto al pasar el ratón
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return button;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        mensajeBienvenida.setText("Bienvenido, " + nombre + "!");
    }

    public void addVistaCrearPartida(ActionListener l) {
        botonCrearPartida.addActionListener(l);
    }

    public void addVistaCargarPartida(ActionListener l) {
        botonCargarPartida.addActionListener(l);
    }
    
    public void addVistaUltimaPartida(ActionListener l) {
        botonUltimaPartida.addActionListener(l);
    }
}