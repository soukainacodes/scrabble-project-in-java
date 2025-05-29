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
 * Vista que muestra los recursos disponibles en el juego.
 * Permite añadir, eliminar y modificar recursos.
 */
public class VistaRecursos extends JPanel {

    /**
     * Colores personalizados para la interfaz.
     * Estos colores son consistentes con el estilo del juego.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);

    /**
     * Colores para el scrollbar.
     * Estos colores son utilizados para personalizar la apariencia del scrollbar.
     */
    private static final Color SCROLLBAR_THUMB = new Color(180, 180, 180);

    /**
     * Color del track del scrollbar.
     * Este color se utiliza para el fondo del scrollbar.
     */
    private static final Color SCROLLBAR_TRACK = new Color(240, 240, 240);

    /**
     * Color para los botones.
     * Estos colores son utilizados para los botones de la interfaz.
     */
    private static final Color LILA_CLARO = new Color(180, 95, 220);

    /**
     * Color oscuro para los botones.
     * Este color se utiliza para el estado presionado de los botones.
     */
    private static final Color LILA_OSCURO = new Color(52, 28, 87);

    /**
     * Color del borde contenedor.
     * Este color se utiliza para el borde contendor.
     */
    private static final Color BORDE_COLOR = new Color(220, 200, 150);

    /**
     * Dimensiones del contenido de la vista.
     */
    private static final int CONTENT_WIDTH = 600;

    /**
     * Altura del contenido de la vista.
     */
    private static final int CONTENT_HEIGHT = 330;

    /**
     * Botón para añadir un nuevo recurso.
     */
    private JButton botonAdd;

    /**
     * Modelo de lista para gestionar los recursos.
     */
    private DefaultListModel<String> model;

    /**
     * Lista que muestra los recursos disponibles.
     * Permite seleccionar un recurso para eliminar o modificar.
     */
    private JList<String> lista;

    /**
     * Botones para eliminar un recursos.
     */
    private JButton botonEliminar;

    /**
     * Botón para modificar un recurso seleccionado.
     */
    private JButton botonModificar;

    /**
     * Panel que contiene la lista de recursos con esquinas redondeadas.
     * Este panel mejora la estética de la vista.
     */
    private RoundedPanel listaPanel;


    /**
     * Constructor de la vista de recursos.
     * Configura el layout, colores y componentes de la vista.
     */
    public VistaRecursos() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(new EmptyBorder(5, 20, 5, 20)); // Consistente con otras vistas
        setPreferredSize(new Dimension(700, 520)); // Consistente con otras vistas

        // Crear y añadir componentes
        JPanel titlePanel = createTitlePanel();
        JPanel mainPanel = createMainPanel();

        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Crea el panel de título con el logo Scrabble.
     * Este panel contiene un título estilizado con fichas de Scrabble.
     *
     * @return JPanel con el título estilizado
     */
    private JPanel createTitlePanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(APP_BG_COLOR);
        p.setBorder(new EmptyBorder(15, 0, 5, 0)); // Exactamente igual que en VistaPantallaPrincipal

        // Crear título estilo Scrabble con fichas
        String[] letras = {"R", "E", "C", "U", "R", "S", "O", "S"};
        Color[] colores = {
            new Color(220, 130, 95), // Naranja rojizo
            new Color(95, 170, 220), // Azul claro
            new Color(220, 180, 95), // Amarillo
            new Color(150, 220, 95), // Verde
            new Color(220, 95, 160), // Rosa
            new Color(180, 95, 220), // Morado/Lila
            new Color(95, 220, 190), // Turquesa
            new Color(235, 140, 80) // Naranja
        };

        JPanel fichas = new JPanel();
        fichas.setLayout(new BoxLayout(fichas, BoxLayout.X_AXIS));
        fichas.setBackground(APP_BG_COLOR);
        fichas.add(Box.createHorizontalGlue());

        for (int i = 0; i < letras.length; i++) {
            JLabel l = crearFichaTitulo(letras[i], colores[i]);
            fichas.add(l);
            if (i < letras.length - 1) {
                fichas.add(Box.createHorizontalStrut(5));
            }
        }

        fichas.add(Box.createHorizontalGlue());
        p.add(fichas);
        return p;
    }


    /**
     * Crea una etiqueta estilizada que representa una ficha de Scrabble.
     * Esta etiqueta tiene un fondo redondeado y un efecto de hover.
     *
     * @param texto El texto de la ficha
     * @param color El color de fondo de la ficha
     * @return JLabel con el estilo de ficha
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
        l.setFont(new Font("Arial Black", Font.BOLD, 28));  // Igual que otras vistas
        l.setPreferredSize(new Dimension(40, 40));          // Igual que otras vistas
        l.addMouseListener(new HoverEfectoTexto(l));
        return l;
    }


    /**
     * Crea el panel principal que contiene la lista de recursos y los botones.
     * Este panel tiene un diseño mejorado con esquinas redondeadas y scroll personalizado.
     *
     * @return JPanel con la lista de recursos y botones
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

        // Panel de botones (fuera del panel redondeado)
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botonesPanel.setOpaque(false);

        botonAdd = createStylishButton("Añadir Recurso");
        botonEliminar = createStylishButton("Eliminar Recurso");
        botonModificar = createStylishButton("Editar Recurso");

        botonesPanel.add(botonAdd);
        botonesPanel.add(botonModificar);
        botonesPanel.add(botonEliminar);

        // Añadir componentes al panel principal
        content.add(listaPanel, BorderLayout.CENTER);
        content.add(botonesPanel, BorderLayout.SOUTH);

        return content;
    }

    /**
     * Clase interna que representa un panel con esquinas redondeadas.
     * Este panel se utiliza para mejorar la estética de la lista de recursos.
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
     * Este botón se utiliza para añadir, eliminar o modificar recursos.
     *
     * @param text El texto del botón
     * @return JButton estilizado
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
     * Clase interna que personaliza el comportamiento del scrollbar.
     * Utiliza un diseño minimalista y esquinas redondeadas.
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
     * Clase interna que maneja el efecto de hover en las etiquetas de texto.
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
     * Añade un ActionListener al botón de añadir recurso.
     * Este listener se activa cuando el usuario hace clic en el botón.
     *
     * @param l ActionListener a añadir
     */
    public void addRecurso(ActionListener l) {
        botonAdd.addActionListener(l);
    }

    /**
     * Añade un ActionListener al botón de eliminar recurso.
     * Este listener se activa cuando el usuario hace clic en el botón.
     *
     * @param l ActionListener a añadir
     */
    public void eliminarRecurso(ActionListener l) {
        botonEliminar.addActionListener(l);
    }


    /**
     * Añade un ActionListener al botón de modificar recurso.
     * Este listener se activa cuando el usuario hace clic en el botón.
     *
     * @param l ActionListener a añadir
     */
    public void modificarRecurso(ActionListener l) {
        botonModificar.addActionListener(l);
    }


    /**
     * Añade un recurso a la lista.
     * Este método se utiliza para actualizar la lista de recursos disponibles.
     *
     * @param s El recurso a añadir
     */
    public void removeLista(String s) {
        model.removeElement(s);
    }


    /**
     * Establece la lista de recursos disponibles.
     * Este método actualiza la lista con los recursos proporcionados.
     *
     * @param lista Lista de recursos a establecer
     */
    public void setLista(List<String> lista) {
        model.removeAllElements();
        for (String recurso : lista) {
            model.addElement(recurso);
        }
    }


    /**
     * Obtiene el modelo de la lista de recursos.
     * Este modelo se utiliza para gestionar los recursos en la lista.
     *
     * @return DefaultListModel con los recursos
     */
    public String getSeleccionado() {
        return lista.getSelectedValue();
    }
}
