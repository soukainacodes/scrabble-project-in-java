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
        setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Crear y añadir componentes
        JPanel titlePanel = createTitlePanel();
        JPanel mainPanel = createMainPanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(700, 450)); // Consistente con otras vistas
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(APP_BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 0, 20, 0));
        
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
        // Usar un panel con GridBagLayout para mejor control de posiciones
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(APP_BG_COLOR);
        
        // Mensaje de bienvenida
        mensajeBienvenida = new JLabel("Bienvenido!");
        mensajeBienvenida.setFont(new Font("Arial", Font.BOLD, 22));
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
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Añadir elementos al panel
        gbc.insets = new Insets(10, 0, 30, 0);  // Más espacio después del título
        panel.add(mensajeBienvenida, gbc);
        
        gbc.insets = new Insets(5, 0, 5, 0);  // Espacio normal entre botones
        panel.add(botonCrearPartida, gbc);
        panel.add(botonUltimaPartida, gbc);
        panel.add(botonCargarPartida, gbc);
        
        return panel;
    }
    
    private JButton createStylishButton(String text, Color baseColor) {
        // Panel contenedor para asegurar tamaño fijo sin importar el estado del botón
        JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setBackground(APP_BG_COLOR);
        buttonContainer.setPreferredSize(new Dimension(300, 60));
        
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Radio de las esquinas redondeadas
                int radius = 15;
                
                // Determinar si el botón está en estado hover
                boolean isHovered = getModel().isRollover();
                
                // Efecto de sombra si está en hover
                if (isHovered) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(5, 5, getWidth() - 8, getHeight() - 8, radius, radius);
                }
                
                // Dibujar el fondo del botón
                g2.setColor(getBackground());
                g2.fillRoundRect(isHovered ? 0 : 2, isHovered ? 0 : 2, 
                                getWidth() - (isHovered ? 2 : 4), 
                                getHeight() - (isHovered ? 2 : 4), 
                                radius, radius);
                
                // Dibujar el texto
                FontMetrics fm = g2.getFontMetrics();
                Rectangle textRect = new Rectangle(0, 0, getWidth(), getHeight());
                String buttonText = getText();
                g2.setColor(getForeground());
                g2.setFont(getFont());
                int x = (textRect.width - fm.stringWidth(buttonText)) / 2;
                int y = (textRect.height - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(buttonText, x, y);
                
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Letra más grande
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        // Usar un margen fijo para evitar cambios de tamaño
        button.setMargin(new Insets(10, 10, 10, 10));
        
        // Efecto al pasar el ratón (sin cambiar tamaños para evitar movimientos)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
        
        buttonContainer.add(button, BorderLayout.CENTER);
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