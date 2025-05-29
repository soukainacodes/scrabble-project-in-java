package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;


/**
 * VistaCargarPartida es una clase que representa la interfaz gráfica para cargar partidas guardadas.
 * Permite al usuario seleccionar una partida de una lista, cargarla o eliminarla.
 * La interfaz incluye un título estilizado, una lista de partidas y botones para interactuar con ellas.
 */
public class VistaCargarPartida extends JPanel {

    /**
     * Color de fondo de la aplicación.
     * Este color se utiliza como fondo principal de la interfaz.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);
    
    
    /**
     * Color utilizado para el thumb del scrollbar.
     * Este color se utiliza para la parte deslizante del scrollbar.
     */
    private static final Color SCROLLBAR_THUMB = new Color(180, 180, 180);
    
    /**
     * Color utilizado para el track del scrollbar.
     * Este color se utiliza para el fondo del scrollbar.
     */
    private static final Color SCROLLBAR_TRACK = new Color(240, 240, 240);
    
    
    /**
     * Color lila claro utilizado para el fondo de los botones.
     * Este color se utiliza para el fondo de los botones de la interfaz.
     */
    private static final Color LILA_CLARO = new Color(180, 95, 220);
    
    
    /**
     * Color lila oscuro utilizado para el fondo de los botones.
     * Este color se utiliza para el fondo de los botones de la interfaz.
     */
    private static final Color LILA_OSCURO = new Color(52, 28, 87);
    
    
    /**
     * Color del borde del panel de lista.
     * Este color se utiliza para el borde del panel que contiene la lista de partidas guardadas.
     */
    private static final Color BORDE_COLOR = new Color(220, 200, 150);
    
    
    /**
     * Botón para cargar una partida guardada.
     * Este botón permite al usuario cargar la partida seleccionada de la lista.
     */
    private JButton botonCargar;
    
    /**
     * Botón para eliminar una partida guardada.
     * Este botón permite al usuario eliminar la partida seleccionada de la lista.
     */
    private JButton botonEliminar;
    
    
    /**
     * Modelo de lista que contiene los nombres de las partidas guardadas.
     * Utilizado por la JList para mostrar las partidas.
     */
    private DefaultListModel<String> model;
    
    /**
     * Lista que muestra las partidas guardadas.
     * Utiliza un modelo de lista para gestionar los elementos.
     */
    private JList<String> lista;
    
    /**
     * Panel redondeado que contiene la lista de partidas.
     * Este panel tiene un fondo con gradiente y bordes redondeados.
     */
    private RoundedPanel listaPanel;
    
    
    /**
     * Label para mostrar mensajes de error.
     */
    private JLabel errorLabel;

    /**
     * Constructor de la vista Cargar Partida.
     * Configura el layout, colores y componentes de la interfaz.
     */
    public VistaCargarPartida() {
        // Configuración del panel principal
        setLayout(new BorderLayout());
        // Establecer colores y bordes consistentes con otras vistas
        setBackground(APP_BG_COLOR);
        // Borde para el panel principal
        setBorder(new EmptyBorder(5, 20, 5, 20)); 
        // Tamaño preferido del panel, consistente con otras vistas
        setPreferredSize(new Dimension(700, 520)); 
        // Crear paneles para el título y el contenido principal
        JPanel titlePanel = createTitlePanel();
        // Panel principal que contendrá la lista y los botones
        JPanel mainPanel = createMainPanel();


        // Añadir los paneles al panel principal
        add(titlePanel, BorderLayout.NORTH);
        // Añadir el panel principal al centro del layout
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Crea el panel del título con un diseño estilizado.
     * Incluye un título con fichas al estilo Scrabble.
     * @return JPanel con el título estilizado.
     */
    private JPanel createTitlePanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(APP_BG_COLOR);
        p.setBorder(new EmptyBorder(15, 0, 5, 0)); 
        String[] letras = {"P", "A", "R", "T", "I", "D", "A", "S", " ", "G", "U", "A", "R", "D", "A", "D", "A", "S"};
        Color[] colores = {
            new Color(220, 130, 95), // Naranja rojizo
            new Color(95, 170, 220), // Azul claro
            new Color(220, 180, 95), // Amarillo
            new Color(150, 220, 95), // Verde
            new Color(180, 95, 220), // Morado/Lila
            new Color(220, 95, 160), // Rosa
            new Color(95, 220, 190), // Turquesa
            new Color(235, 140, 80), // Naranja
            APP_BG_COLOR, // Fondo (espacio)
            new Color(220, 130, 95), // Naranja rojizo
            new Color(95, 170, 220), // Azul claro
            new Color(220, 180, 95), // Amarillo
            new Color(150, 220, 95), // Verde
            new Color(180, 95, 220), // Morado/Lila
            new Color(220, 95, 160), // Rosa
            new Color(95, 220, 190), // Turquesa
            new Color(235, 140, 80), // Naranja
            new Color(150, 220, 95) // Verde
        };

        JPanel fichas = new JPanel();
        fichas.setLayout(new BoxLayout(fichas, BoxLayout.X_AXIS));
        fichas.setBackground(APP_BG_COLOR);
        fichas.add(Box.createHorizontalGlue());

        for (int i = 0; i < letras.length; i++) {
            if (" ".equals(letras[i])) {
                fichas.add(Box.createHorizontalStrut(10));
                continue;
            }
            JLabel l = crearFichaTitulo(letras[i], colores[i]);
            fichas.add(l);
            if (i < letras.length - 1 && !" ".equals(letras[i + 1])) {
                fichas.add(Box.createHorizontalStrut(5));
            }
        }

        fichas.add(Box.createHorizontalGlue());
        p.add(fichas);
        return p;
    }

    /**
     * Crea el panel principal que contiene la lista de partidas y los botones.
     * @return JPanel con la lista de partidas y botones.
     */
    private JLabel crearFichaTitulo(String texto, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 3, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial Black", Font.BOLD, 22));  // Reducido de 24 a 22
        l.setPreferredSize(new Dimension(32, 32));          // Reducido de 35x35 a 32x32
        l.addMouseListener(new HoverEfectoTexto(l));
        return l;
    }

    /**
     * Crea el panel principal que contiene la lista de partidas y los botones.
     * Este panel incluye una lista estilizada con scroll, botones para cargar y eliminar partidas,
     * y un label para mostrar mensajes de error.
     * @return JPanel con la lista de partidas y botones.
     */
    private JPanel createMainPanel() {
        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(APP_BG_COLOR);

        // Panel de lista con esquinas redondeadas
        listaPanel = new RoundedPanel();
        listaPanel.setLayout(new BorderLayout());
        listaPanel.setBackground(APP_BG_COLOR);

        // Lista con estilos mejorados
        model = new DefaultListModel<>();
        lista = new JList<>(model);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Arial", Font.PLAIN, 14));
        lista.setFixedCellHeight(40);
        lista.setBackground(APP_BG_COLOR);

        // Personalizar apariencia de celdas
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                label.setFont(new Font("Arial", Font.BOLD, 14));

                if (isSelected) {
                    label.setBackground(LILA_CLARO);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(index % 2 == 0 ? APP_BG_COLOR : new Color(235, 220, 170)); // Color alternado consistente
                    label.setForeground(Color.DARK_GRAY);
                }

                return label;
            }
        });

        // Scroll con UI consistente y transparente
        JScrollPane scrollPane = new JScrollPane(lista);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(APP_BG_COLOR);
        scrollPane.setBackground(APP_BG_COLOR);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());

        // UI personalizada para scrollbar
        scrollPane.getVerticalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getHorizontalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));

        // Añadir un pequeño margen interno
        listaPanel.add(scrollPane, BorderLayout.CENTER);

        // Crear y configurar el label de error
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(200, 0, 0));  // Rojo para errores
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setVisible(false);
        
        // Panel para el mensaje de error
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setOpaque(false);
        errorPanel.add(errorLabel);
        
        // Panel de botones (fuera del panel redondeado)
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botonesPanel.setOpaque(false);

        botonCargar = createStylishButton("Cargar Partida");
        botonEliminar = createStylishButton("Eliminar Partida");

        botonesPanel.add(botonCargar);
        botonesPanel.add(botonEliminar);
        
        // Panel para contener el error y los botones
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(errorPanel, BorderLayout.NORTH);
        bottomPanel.add(botonesPanel, BorderLayout.SOUTH);

        // Añadir componentes al panel principal
        content.add(listaPanel, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        return content;
    }

    /**
     * Clase interna que define un panel con esquinas redondeadas y sombra.
     * Utiliza un gradiente para el fondo y un borde estilizado.
     */
    private class RoundedPanel extends JPanel {

        private static final int CORNER_RADIUS = 20;

        public RoundedPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Pequeño margen para la sombra
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibujar sombra
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, CORNER_RADIUS, CORNER_RADIUS);

            // Dibujar borde
            g2.setColor(BORDE_COLOR);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);

            // Dibujar fondo con gradiente
            GradientPaint gp = new GradientPaint(
                    0, 0, APP_BG_COLOR.brighter(),
                    0, getHeight(), APP_BG_COLOR
            );
            g2.setPaint(gp);
            g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 5, CORNER_RADIUS - 2, CORNER_RADIUS - 2);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Crea un botón estilizado con esquinas redondeadas y efectos de hover.
     * Utiliza un gradiente de color y un efecto de sombra al pasar el ratón.
     * @param text El texto del botón.
     * @return JButton estilizado.
     */
    private JButton createStylishButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Radio de las esquinas redondeadas
                int radius = 15;

                // Determinar si el botón está en estado hover o presionado
                boolean isHovered = getModel().isRollover();
                boolean isPressed = getModel().isPressed();

                // Color base según estado
                Color bg = isPressed ? LILA_OSCURO : (isHovered ? LILA_OSCURO : LILA_CLARO);

                // Efecto de sombra si está en hover
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

        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(180, 40));

        // Efecto al pasar el ratón
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }



    /**
     * Clase interna que define un ScrollBar UI minimalista y estilizado.
     * Utiliza colores personalizados para el thumb y el track, y elimina los botones de incremento/decremento.
     */
    private class MinimalistScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = SCROLLBAR_THUMB;
            this.trackColor = SCROLLBAR_TRACK;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

      

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y,
                    thumbBounds.width, thumbBounds.height,
                    8, 8); // Esquinas redondeadas

            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }


    /**
     * Clase interna que define un efecto de hover para el texto de las fichas del título.
     * Cambia el color del texto al pasar el ratón por encima.
     */
    private static class HoverEfectoTexto extends MouseAdapter {

        private final JLabel label;

        HoverEfectoTexto(JLabel l) {
            this.label = l;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            label.setForeground(new Color(255, 255, 200));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            label.setForeground(Color.WHITE);
        }
    }


    /**
     * Método para añadir un ActionListener al botón de cargar partida.
     * @param l ActionListener que se ejecutará al hacer clic en el botón.
     */
    public void jugarPartida(ActionListener l) {
        botonCargar.addActionListener(l);
    }

    /**
     * Método para añadir un ActionListener al botón de eliminar partida.
     * @param l ActionListener que se ejecutará al hacer clic en el botón.
     */
    public void eliminarPartida(ActionListener l) {
        botonEliminar.addActionListener(l);
    }

    /**
     * Añade una partida a la lista de partidas guardadas.
     * @param nombre El nombre de la partida a añadir.
     */
    public void removePartida(String nombre) {
        model.removeElement(nombre);
    }

    /**
     * Establece la lista de partidas guardadas en la vista.
     * Limpia la lista actual y añade las partidas proporcionadas.
     * @param partidas Lista de nombres de partidas a mostrar.
     */
    public void setPartidas(List<String> partidas) {
        model.removeAllElements();
        for (String partida : partidas) {
            model.addElement(partida);
        }
    }


    /**
     * Obtiene el nombre de la partida seleccionada en la lista.
     * @return El nombre de la partida seleccionada, o null si no hay ninguna seleccionada.
     */
    public String getSeleccionada() {
        return lista.getSelectedValue();
    }

    /**
     * Muestra un mensaje de error en la interfaz
     * @param mensaje El mensaje de error a mostrar
     */
    public void setError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
        
        // Hacer que el mensaje desaparezca después de 10 segundos
        new Timer(10000, (e) -> {
            errorLabel.setVisible(false);
        }).start();
        
        // Ajustar el tamaño de la ventana si es necesario
        revalidate();
        repaint();
    }
}
