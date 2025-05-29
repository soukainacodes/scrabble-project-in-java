package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.basic.BasicComboBoxRenderer;


/**
 * Vista para crear una nueva partida en el juego.
 * Permite seleccionar el número de jugadores, idioma y un ID de partida.
 * Incluye un botón para iniciar sesión como segundo jugador si se selecciona esa opción.
 */
public class VistaCrearPartida extends JPanel {

    /**
     * Color de fondo de la aplicación.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177); // Color crema

    /**
     * Color de primer plano para el texto y elementos destacados.
     */
    private static final Color FG = new Color(20, 40, 80);

    /**
     * Color lila claro para botones.
     */
    private static final Color LILA_CLARO = new Color(180, 95, 220);


    /**
     * Color lila oscuro para botones.
     */
    private static final Color LILA_OSCURO = new Color(52, 28, 87);


    /**
     * Ancho del panel de contenido principal.
     * Se utiliza para establecer el tamaño máximo de los componentes dentro del panel.
     */
    private static final int CONTENT_WIDTH = 450;
    
    /**
     * Botón para crear la partida.
     * Este botón se utiliza para iniciar el proceso de creación de una nueva partida.
     */
    private JButton botonCrearPartida;


    /**
     * Botón para iniciar sesión como segundo jugador.
     * Este botón solo es visible si se selecciona la opción de 2 jugadores.
     */
    private JButton botonIniciarSesion;


    /**
     * ComboBox para seleccionar el número de jugadores.
     * Permite elegir entre 1 o 2 jugadores para la partida.
     */
    private JComboBox<String> comboJugadores;

    /**
     * ComboBox para seleccionar el recurso de la partida.
     * Muestra una lista de recursos disponibles para la partida.
     */
    private JComboBox<String> comboIdiomas;

    /**
     * Campo de texto para ingresar el ID de la partida.
     * Este campo es obligatorio para identificar la partida creada.
     */
    private JTextField id;


    /**
     * Etiqueta para mostrar mensajes de error.
     * Se utiliza para informar al usuario sobre problemas al crear la partida.
     */
    private JLabel errorLabel;

    /**
     * Array de idiomas disponibles para la partida.
     * Este array se utiliza para poblar el JComboBox de idiomas.
     */
    private String[] idiomas;

    /**
     * Constructor de la vista de creación de partida.
     * Configura el diseño, los componentes y su estilo.
     */
    public VistaCrearPartida() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20)); 

        JPanel titlePanel = createTitlePanel();
        JPanel contentPanel = createContentPanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(700, 520));
    }
    
    /**
     * Crea el panel del título con un diseño estilizado que simula fichas de Scrabble.
     * Cada letra del título se muestra como una ficha con un color diferente.
     * 
     * @return JPanel con el título estilizado
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(APP_BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0)); 
        
        String[] letras = { "N", "U", "E", "V", "A", " ", "P", "A", "R", "T", "I", "D", "A" };
        Color[] colores = {
            new Color(220, 130, 95),   // Naranja rojizo
            new Color(95, 170, 220),   // Azul claro
            new Color(220, 180, 95),   // Amarillo
            new Color(150, 220, 95),   // Verde
            new Color(180, 95, 220),   // Morado/Lila
            APP_BG_COLOR,              // Fondo (espacio)
            new Color(220, 95, 160),   // Rosa
            new Color(95, 220, 190),   // Turquesa
            new Color(235, 140, 80),   // Naranja
            new Color(220, 130, 95),   // Naranja rojizo
            new Color(95, 170, 220),   // Azul claro
            new Color(220, 180, 95),   // Amarillo
            new Color(150, 220, 95)    // Verde
        };
        
        JPanel fichasPanel = new JPanel();
        fichasPanel.setLayout(new BoxLayout(fichasPanel, BoxLayout.X_AXIS));
        fichasPanel.setBackground(APP_BG_COLOR);
        fichasPanel.add(Box.createHorizontalGlue());
        
        for (int i = 0; i < letras.length; i++) {
            final int idx = i;
            
            if (letras[i].equals(" ")) {
                fichasPanel.add(Box.createHorizontalStrut(10));
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
     * Crea el panel de contenido principal con campos para ID de partida, número de jugadores,
     * recurso y botones para crear la partida o añadir un segundo jugador.
     * 
     * @return JPanel con el contenido principal
     */
    private JPanel createContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(APP_BG_COLOR);
        
        JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(220, 200, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.setColor(APP_BG_COLOR);
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 18, 18);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        roundedPanel.setLayout(new GridBagLayout());
        roundedPanel.setOpaque(false);
        
        JPanel content = new JPanel();
        content.setBackground(APP_BG_COLOR);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 30, 10, 30)); 
        
        JLabel lblID = createStylishLabel("ID de Partida");
        lblID.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblID);
        content.add(Box.createVerticalStrut(5)); 
        
        id = createStylishTextField();
        id.setMaximumSize(new Dimension(CONTENT_WIDTH, 40));
        id.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(id);
        content.add(Box.createVerticalStrut(15)); 
        
        JLabel lblJugadores = createStylishLabel("Número de jugadores");
        lblJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblJugadores);
        content.add(Box.createVerticalStrut(5)); 
        
        String[] opcionesJug = {"1 Jugador", "2 Jugadores"};
        comboJugadores = createStylishComboBox(opcionesJug);
        comboJugadores.setMaximumSize(new Dimension(CONTENT_WIDTH, 40));
        comboJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(comboJugadores);
        content.add(Box.createVerticalStrut(10));
        
        botonIniciarSesion = createStylishButton("Añadir Segundo Jugador", LILA_CLARO);
        botonIniciarSesion.setVisible(false);
        botonIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        botonIniciarSesion.setMaximumSize(new Dimension(CONTENT_WIDTH, 35));
        botonIniciarSesion.setPreferredSize(new Dimension(CONTENT_WIDTH, 35));
        botonIniciarSesion.setMinimumSize(new Dimension(CONTENT_WIDTH, 35));
        
        content.add(botonIniciarSesion);
        content.add(Box.createVerticalStrut(15));
        
        JLabel lblIdioma = createStylishLabel("Idioma");
        lblIdioma.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblIdioma);
        content.add(Box.createVerticalStrut(5)); 
        
        comboIdiomas = createStylishComboBox(new String[0]);
        comboIdiomas.setMaximumSize(new Dimension(CONTENT_WIDTH, 40));
        comboIdiomas.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(comboIdiomas);
        content.add(Box.createVerticalStrut(20)); 
        
        botonCrearPartida = createStylishButton("Crear Partida", LILA_OSCURO);
        botonCrearPartida.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCrearPartida.setMaximumSize(new Dimension(CONTENT_WIDTH, 35));
        botonCrearPartida.setPreferredSize(new Dimension(CONTENT_WIDTH, 35)); 
        botonCrearPartida.setMinimumSize(new Dimension(CONTENT_WIDTH, 35));
        content.add(botonCrearPartida);
        
        content.add(Box.createVerticalStrut(15));
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(200, 0, 0));
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        errorLabel.setVisible(false);
        content.add(errorLabel);
        
        comboJugadores.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String sel = (String) e.getItem();
                    boolean mostrarBoton = "2 Jugadores".equals(sel);
                    botonIniciarSesion.setVisible(mostrarBoton);
                    
                    content.validate();
                    
                    roundedPanel.validate();
                    
                    botonIniciarSesion.setMaximumSize(new Dimension(CONTENT_WIDTH, 35));
                    botonIniciarSesion.setPreferredSize(new Dimension(CONTENT_WIDTH, 35));
                    botonIniciarSesion.setMinimumSize(new Dimension(CONTENT_WIDTH, 35));
                }
            }
        });
        
        roundedPanel.add(content);
        mainPanel.add(roundedPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Crea una etiqueta estilizada con un diseño atractivo.
     * 
     * @param text El texto de la etiqueta
     * @return JLabel estilizado
     */
    private JLabel createStylishLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(FG);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    
    /**
     * Crea un campo de texto estilizado con un fondo sutil y bordes redondeados.
     * 
     * @return JTextField estilizado
     */
    private JTextField createStylishTextField() {
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 240),
                        0, getHeight(), new Color(255, 255, 255, 200)
                    );
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                    
                    g2.setColor(hasFocus() ? LILA_CLARO : new Color(200, 200, 200));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                    
                    if (hasFocus()) {
                        g2.setColor(new Color(LILA_CLARO.getRed(), LILA_CLARO.getGreen(), LILA_CLARO.getBlue(), 60));
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 14, 14);
                    }
                    
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        
        textField.setOpaque(false);
        textField.setBorder(new EmptyBorder(5, 15, 5, 15));
        textField.setBackground(new Color(0, 0, 0, 0));
        textField.setFont(new Font("Arial", Font.PLAIN, 15));
        textField.setHorizontalAlignment(JTextField.CENTER);
        
        return textField;
    }
    
    /**
     * Crea un JComboBox estilizado con un renderizador personalizado para las celdas.
     * Incluye un efecto hover y un botón de flecha elegante.
     * 
     * @param options Opciones del JComboBox
     * @return JComboBox estilizado
     */
    private JComboBox<String> createStylishComboBox(String[] options) {
        BasicComboBoxRenderer renderer = new BasicComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, 
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                if (isSelected) {
                    label.setBackground(LILA_CLARO);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(FG);
                }
                
                return label;
            }
        };
        
        JComboBox<String> comboBox = new JComboBox<>(options) {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 240),
                    0, getHeight(), new Color(255, 255, 255, 200)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        comboBox.setFont(new Font("Arial", Font.PLAIN, 15));
        comboBox.setBackground(new Color(0, 0, 0, 0));
        comboBox.setForeground(FG);
        comboBox.setOpaque(false);
        comboBox.setRenderer(renderer);
        
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    public int getWidth() {
                        return 20;
                    }
                    
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        boolean isHovered = getModel().isRollover();
                        g2.setColor(isHovered ? LILA_OSCURO : LILA_CLARO);
                        
                        int[] xPoints = {getWidth()/2-5, getWidth()/2+5, getWidth()/2};
                        int[] yPoints = {getHeight()/2-2, getHeight()/2-2, getHeight()/2+4};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        
                        if (isHovered) {
                            g2.setColor(new Color(255, 255, 255, 60));
                            g2.drawLine(xPoints[0], yPoints[0]+1, xPoints[1], yPoints[1]+1);
                        }
                        
                        g2.dispose();
                    }
                    
                    {
                        addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                setCursor(new Cursor(Cursor.HAND_CURSOR));
                                repaint();
                            }
                            
                            @Override
                            public void mouseExited(MouseEvent e) {
                                repaint();
                            }
                        });
                    }
                };
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                return button;
            }
            
            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            }
            
            @Override
            protected ComboPopup createPopup() {
                return new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = super.createScroller();
                        scroller.getViewport().setOpaque(true);
                        scroller.getViewport().setBackground(Color.WHITE);
                        scroller.setOpaque(true);
                        scroller.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                        
                        return scroller;
                    }
                    
                    @Override
                    protected void configureList() {
                        super.configureList();
                        list.setOpaque(true);
                        list.setBackground(Color.WHITE);
                        
                        list.addMouseMotionListener(new MouseAdapter() {
                            @Override
                            public void mouseMoved(MouseEvent e) {
                                Point location = e.getPoint();
                                int index = list.locationToIndex(location);
                                if (index >= 0) {
                                    list.setSelectedIndex(index);
                                }
                            }
                        });
                    }
                    
                    @Override
                    public void show() {
                        super.show();
                        list.setFixedCellHeight(30); 
                    }
                };
            }
        });
        
        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
                comboBox.setBorder(BorderFactory.createLineBorder(LILA_CLARO, 1));
                comboBox.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                comboBox.setBorder(null);
                comboBox.repaint();
            }
        });
        
        return comboBox;  
    }
    
    /**
     * Crea un botón estilizado con esquinas redondeadas y un efecto hover.
     * 
     * @param text El texto del botón
     * @param baseColor El color base del botón
     * @return JButton estilizado
     */
    private JButton createStylishButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int radius = 15;
                boolean isHovered = getModel().isRollover();
                Color bgColor = isHovered ? baseColor.darker() : baseColor;
                
                if (isHovered) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(5, 5, getWidth() - 8, getHeight() - 8, radius, radius);
                }
                
                g2.setColor(bgColor);
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
                
                g2.setColor(getForeground());
                g2.setFont(getFont());
                
                FontMetrics fm = g2.getFontMetrics();
                String buttonText = getText();
                int x = (getWidth() - fm.stringWidth(buttonText)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                g2.drawString(buttonText, x, y);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        button.setPreferredSize(new Dimension(180, 35)); // Altura reducida
        button.setMinimumSize(new Dimension(180, 35));
        button.setMaximumSize(new Dimension(180, 35));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
        
        return button;
    }
    
    /**
     * Establece los idiomas disponibles para el JComboBox de idiomas.
     * Actualiza el modelo del JComboBox y lo repinta para reflejar los cambios.    
     * @param idiomas Array de idiomas a establecer
     * 
     */
    public void setIdiomas(String[] idiomas) {
        this.idiomas = idiomas;
        comboIdiomas.setModel(new DefaultComboBoxModel<>(idiomas));
        comboIdiomas.revalidate();
        comboIdiomas.repaint();
    }

    /**
     * Obtiene el número de jugadores seleccionado en el JComboBox.
     * 
     * @return El índice del número de jugadores seleccionado (0 para 1 jugador, 1 para 2 jugadores)
     */
    public int getModo() {
        return comboJugadores.getSelectedIndex();
    }

    /**
     * Obtiene el idioma seleccionado en el JComboBox.
     * 
     * @return El idioma seleccionado como String
     */
    public String getIdioma() {
        return (String) comboIdiomas.getSelectedItem();
    }

    /**
     * Obtiene el ID de la partida ingresado en el campo de texto.
     * 
     * @return El ID de la partida como String
     */
    public String getID() {
        return id.getText();
    }

    /**
     * Añade un ActionListener al botón de crear partida.
     * 
     * @param l ActionListener a añadir al botón
     */
    public void jugarPartida(ActionListener l) {
        botonCrearPartida.addActionListener(l);
    }

    /**
     * Añade un ActionListener al botón de iniciar sesión como segundo jugador.
     * Este botón solo es visible si se selecciona la opción de 2 jugadores.
     * 
     * @param l ActionListener a añadir al botón
     */
    public void addSegundoJugador(ActionListener l) {
        botonIniciarSesion.addActionListener(l);
    }
    
    /**
     * Muestra un mensaje de error en la interfaz
     * @param mensaje El mensaje de error a mostrar
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