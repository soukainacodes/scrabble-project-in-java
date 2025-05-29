package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 * Vista del menú de partidas, donde el usuario puede crear, cargar o acceder a la última partida.
 * Incluye un diseño atractivo y botones estilizados.
 */
public class VistaMenuPartidas extends JPanel {

    /**
     * Colores personalizados para la interfaz.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);

    /**
     * Color para el botón de crear partida o cargar partida de las que están guardadas.
     */
    private static final Color LILA_CLARO = new Color(180, 95, 220);

    /**
     * Color para el botón de cargar última partida.
     */
    private static final Color AZUL_CLARO = new Color(95, 170, 220);

    /**
     * Color para el botón destacado.
     */
    private static final Color AZUL_OSCURO = new Color(20, 40, 80);
    
    /**
     * Botón para crear una nueva partida.
     */
    private JButton botonCrearPartida;

    /**
     * Botón para acceder a la última partida guardada.
     */
    private JButton botonUltimaPartida;

    /**
     * Botón para cargar una partida guardada.
     */
    private JButton botonCargarPartida;

    /**
     * Nombre del usuario, utilizado para personalizar el mensaje de bienvenida.
     */
    private String nombre = "";

    /**
     * Etiqueta que muestra un mensaje de bienvenida al usuario.
     * Se actualiza con el nombre del usuario cuando se establece.
     */
    private JLabel mensajeBienvenida;
    
    /**
     * Etiqueta que muestra mensajes de error en la interfaz.
     * Se utiliza para informar al usuario sobre problemas al cargar partidas.
     */
    private JLabel errorLabel;
    
    /**
     * Constructor de la vista del menú de partidas.
     * Configura el panel principal, los botones y el diseño de la interfaz.
     */
    public VistaMenuPartidas() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JPanel titlePanel = createTitlePanel();
        JPanel mainPanel = createMainPanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(700, 520)); 
    }


    /**
     * Crea el panel de título con las letras del juego Scrabble.
     * Cada letra tiene un color específico y un efecto al pasar el ratón.
     * 
     * @return JPanel con el título estilizado.
     *
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(APP_BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
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
                    g2.setColor(colores[idx]);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
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
            
            if (i < letras.length - 1 && !letras[i+1].equals(" ")) 
                fichasPanel.add(Box.createHorizontalStrut(5));
        }
        
        fichasPanel.add(Box.createHorizontalGlue());
        panel.add(fichasPanel);
        
        return panel;
    }


    /**
     * Crea el panel principal que contiene los botones y el mensaje de bienvenida.
     * Utiliza un diseño con bordes redondeados y sombra para mejorar la estética.
     * 
     * @return JPanel con el contenido principal del menú de partidas.
     */
    private JPanel createMainPanel() {
        JPanel marcoPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 22, 22);
                
                g2.setColor(new Color(220, 200, 150));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
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
        
        JPanel contenidoPanel = new JPanel(new GridBagLayout());
        contenidoPanel.setOpaque(false);
        
        mensajeBienvenida = new JLabel("Bienvenido!");
        mensajeBienvenida.setFont(new Font("Arial", Font.BOLD, 26)); 
        mensajeBienvenida.setForeground(AZUL_OSCURO);
        mensajeBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        
        botonCrearPartida = createStylishButton("Nueva Partida", LILA_CLARO);
        botonUltimaPartida = createStylishButton("Última Partida", AZUL_CLARO);
        botonCargarPartida = createStylishButton("Cargar Partida", LILA_CLARO);
        
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(200, 0, 0));  
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setVisible(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.insets = new Insets(10, 0, 30, 0);  
        contenidoPanel.add(mensajeBienvenida, gbc);
        
        gbc.insets = new Insets(10, 0, 10, 0);  
        contenidoPanel.add(botonCrearPartida, gbc);
        contenidoPanel.add(botonUltimaPartida, gbc);
        contenidoPanel.add(botonCargarPartida, gbc);
        
        gbc.insets = new Insets(20, 0, 5, 0);  
        contenidoPanel.add(errorLabel, gbc);
        
        marcoPanel.add(contenidoPanel, BorderLayout.CENTER);
        
        return marcoPanel;
    }
    

    /**
     * Crea un botón estilizado con esquinas redondeadas, gradiente y efectos de hover.
     * 
     * @param text El texto del botón.
     * @param baseColor El color base del botón.
     * @return JButton estilizado.
     */
    private JButton createStylishButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int radius = 15;
                
                boolean isHovered = getModel().isRollover();
                boolean isPressed = getModel().isPressed();
                
                Color bg = isPressed ? baseColor.darker().darker() : 
                          (isHovered ? baseColor.darker() : baseColor);
                
                if (isHovered && !isPressed) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, radius, radius);
                }
                
                g2.setPaint(new GradientPaint(0, 0,
                    new Color(Math.min(bg.getRed() + 25, 255), 
                              Math.min(bg.getGreen() + 25, 255), 
                              Math.min(bg.getBlue() + 25, 255)),
                    0, getHeight(), bg));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
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

    /**
     * Establece el nombre del usuario y actualiza el mensaje de bienvenida.
     * 
     * @param nombre El nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
        mensajeBienvenida.setText("Bienvenido, " + nombre + "!");
    }

    /**
     * Añade un ActionListener al botón de crear partida.
     * 
     * @param l El ActionListener a añadir.
     */
    public void addVistaCrearPartida(ActionListener l) {
        botonCrearPartida.addActionListener(l);
    }


    /**
     * Añade un ActionListener al botón de cargar partida.
     * 
     * @param l El ActionListener a añadir.
     */
    public void addVistaCargarPartida(ActionListener l) {
        botonCargarPartida.addActionListener(l);
    }
    

    /**
     * Añade un ActionListener al botón de cargar la última partida.
     * 
     * @param l El ActionListener a añadir.
     */
    public void cargarUltimaPartida(ActionListener l) {
        botonUltimaPartida.addActionListener(l);
    }
    
    /**
     * Muestra un mensaje de error en la interfaz.
     * El mensaje desaparece automáticamente después de 10 segundos.
     * 
     * @param mensaje El mensaje de error a mostrar.
     */
    public void setError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
        
        new Timer(10000, (e) -> {
            errorLabel.setVisible(false);
        }).start();
        
        revalidate();
        repaint();
    }

}