package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 * Vista para cambiar fichas en un juego de Scrabble.
 * Permite al usuario seleccionar fichas y aceptarlas para su cambio.
 */
public class VistaResetFichas extends JFrame {


    /**
     * Tamaño de las fichas en el rack.
     */
    private static final int TILE_SIZE = 32;

    /**
     * Colores personalizados para la interfaz.
     * Estos colores se utilizan para el fondo, bordes y texto de la ventana.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Color crema de fondo

    /**
     * Color para paneles internos y bordes.
     * Este color se utiliza para los paneles que contienen las fichas y otros elementos.
     */
    private static final Color BORDER_COLOR = new Color(200, 180, 120);  // Color de bordes

    /**
     * Color del texto.
     * Este color se utiliza para el texto instructivo y otros elementos de texto en la ventana.
     */
    private static final Color TEXT_COLOR = new Color(60, 60, 80);       // Color del texto



    /**
     * Color del botón de aceptar.
     * Este color se utiliza para el botón que permite al usuario aceptar las fichas seleccionadas.
     */
    private static final Color BUTTON_COLOR = new Color(128, 64, 200);  // Morado para el botón
    

    /**
     * Panel que contiene las fichas seleccionables.
     * Este panel se utiliza para mostrar las fichas que el usuario puede seleccionar para cambiar.
     */
    private JPanel rack;

    /**
     * Botón para aceptar las fichas seleccionadas.
     * Este botón se utiliza para confirmar la selección de fichas y proceder con el cambio.
     */
    private JButton botonAceptar;

    /**
     * Lista de fichas seleccionadas por el usuario.
     * Esta lista almacena las fichas que el usuario ha seleccionado para cambiar.
     */
    private List<SelectableTileLabel> selectedTiles = new ArrayList<>();

    /**
     * Listener para el botón de aceptar.
     * Este listener se ejecuta cuando el usuario hace clic en el botón de aceptar.
     */
    private ActionListener acceptListener;


    /**
     * Constructor de la vista.
     * Configura la ventana, los paneles y los botones con un diseño atractivo y funcional.
     */
    public VistaResetFichas() {
        setTitle("Cambiar Fichas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 15));
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
        panelPrincipal.setBackground(APP_BG_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(panelPrincipal);
        
        // Texto instructivo
        JLabel lblInstruccion = new JLabel("Selecciona las fichas que deseas cambiar y dale a Aceptar");
        lblInstruccion.setFont(new Font("Arial", Font.BOLD, 14));
        lblInstruccion.setForeground(TEXT_COLOR);
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel superior con texto instructivo
        JPanel panelInstruccion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInstruccion.setOpaque(false);
        panelInstruccion.add(lblInstruccion);
        panelPrincipal.add(panelInstruccion, BorderLayout.NORTH);
       
        // Crear el panel de fichas (rack)
        rack = crearRack();
        
        // Envolver el rack en un panel con borde y título
        JPanel panelRack = new JPanel(new BorderLayout());
        panelRack.setOpaque(false);
        panelRack.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelRack.add(rack, BorderLayout.CENTER);
        
        panelPrincipal.add(panelRack, BorderLayout.CENTER);
        
        // Crear el botón de aceptar
        botonAceptar = crearBotonControl("Aceptar");
        botonAceptar.addActionListener(e -> {
            if (acceptListener != null) {
                acceptListener.actionPerformed(e);
            }
        });
        
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setOpaque(false);
        panelBoton.add(botonAceptar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);
        
        // Configurar tamaño y posición
        setSize(600, 350);
        setLocationRelativeTo(null);
    }
    

    /**
     * Crea un panel para el rack de fichas.
     * Este panel utiliza un diseño de flujo centrado y tiene un fondo personalizado.
     * 
     * @return JPanel configurado para el rack de fichas.
     */
    private JPanel crearRack() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(APP_BG_COLOR);
        return panel;
    }
    

    /**
     * Añade una ficha al rack.
     * Esta ficha es representada por un objeto SelectableTileLabel que permite la selección.
     * 
     * @param letter Letra de la ficha.
     * @param score Puntuación de la ficha.
     */
    public void addTile(String letter, int score) {
        SelectableTileLabel tile = new SelectableTileLabel(letter, score);
        rack.add(tile);
        rack.revalidate();
        rack.repaint();
    }
    

    /**
     * Elimina todas las fichas del rack.
     * Este método limpia el rack y resetea la lista de fichas seleccionadas.
     */
    public void clearRack() {
        rack.removeAll();
        selectedTiles.clear();
        rack.revalidate();
        rack.repaint();
    }
    

    /**
     * Establece el listener para el botón de aceptar.
     * Este listener se ejecuta cuando el usuario hace clic en el botón de aceptar.
     * 
     * @param listener ActionListener que maneja el evento de aceptación.
     */
    public void setAcceptListener(ActionListener listener) {
        this.acceptListener = listener;
    }
    

    /**
     * Obtiene las letras de las fichas seleccionadas.
     * Este método devuelve una cadena con las letras de las fichas que el usuario ha seleccionado.
     * 
     * @return String con las letras de las fichas seleccionadas.
     */
    public String getSelectedTiles() {
        StringBuilder result = new StringBuilder();
        for (SelectableTileLabel tile : selectedTiles) {
            result.append(tile.letter);
            result.append(" ");
        }
        return result.toString();
    }
    

    /**
     * Crea un botón con estilo personalizado.
     * Este botón tiene esquinas redondeadas, un fondo con gradiente y un efecto hover.
     * 
     * @param texto Texto que se mostrará en el botón.
     * @return JButton configurado con estilo personalizado.
     */
    private JButton crearBotonControl(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                // Dibujar botón con esquinas redondeadas
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Determinar color según estado
                Color baseColor = getModel().isPressed() ? BUTTON_COLOR.darker() : 
                            (getModel().isRollover() ? BUTTON_COLOR.brighter() : BUTTON_COLOR);
                
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
     * Clase interna que representa una ficha seleccionable.
     * Esta clase extiende JComponent y permite la selección de fichas con un diseño personalizado.
     */
    private class SelectableTileLabel extends JComponent {
        private final String letter;
        private final int score;
        private boolean selected = false;
        private final Dimension size = new Dimension(TILE_SIZE, TILE_SIZE);
        
        public SelectableTileLabel(String letter, int score) {
            this.letter = letter;
            this.score = score;
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            
            // Manejar eventos de ratón para selección
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    toggleSelection();
                }
            });
        }
        
        private void toggleSelection() {
            selected = !selected;
            if (selected) {
                selectedTiles.add(this);
            } else {
                selectedTiles.remove(this);
            }
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo redondeado (color diferente cuando está seleccionado)
            RoundRectangle2D bg = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 12, 12
            );
            
            if (selected) {
                // Color destacado para selección
                g2.setColor(new Color(255, 200, 100));
            } else {
                // Color normal
                g2.setColor(new Color(255, 223, 169));
            }
            
            g2.fill(bg);
            
            // Borde
            if (selected) {
                g2.setColor(new Color(255, 140, 0)); // Borde más oscuro para selección
                g2.setStroke(new BasicStroke(3));
            } else {
                g2.setColor(new Color(220, 180, 140));
                g2.setStroke(new BasicStroke(2));
            }
            g2.draw(bg);

            // Letra grande
            Font f1 = getFont().deriveFont(Font.BOLD, getHeight() * 0.5f);
            g2.setFont(f1);
            FontMetrics fm = g2.getFontMetrics();
            String s = String.valueOf(letter);
            int x = (getWidth() - fm.stringWidth(s)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(new Color(40, 40, 40));
            g2.drawString(s, x, y);

            // Puntuación pequeña
            Font f2 = getFont().deriveFont(Font.PLAIN, getHeight() * 0.2f);
            g2.setFont(f2);
            fm = g2.getFontMetrics();
            String sc = String.valueOf(score);
            int sx = getWidth() - fm.stringWidth(sc) - 4;
            int sy = getHeight() - fm.getDescent() - 2;
            g2.drawString(sc, sx, sy);

            g2.dispose();
        }
    }
    
    /**
     * Carga fichas en el rack desde una lista de cadenas.
     * Cada cadena debe contener la letra y la puntuación separadas por un espacio.
     * 
     * @param fichas Lista de cadenas con las fichas a cargar.
     */
    public void cargarFichas(List<String> fichas) {
        clearRack();
        for (String ficha : fichas) {
            String[] parts = ficha.trim().split(" ");
            if (parts.length >= 2) {
                String letter = parts[0];
                int score = Integer.parseInt(parts[1]);
                addTile(letter, score);
            }
        }
    }
}