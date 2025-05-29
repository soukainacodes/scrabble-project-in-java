package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;


/**
 * Vista que muestra el ranking de jugadores con sus puntuaciones.
 * Utiliza un diseño atractivo y consistente con otras vistas del sistema.
 */
public class VistaRanking extends JPanel {

    /**
     * Color del fondo de la vista.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Crema
    
    /**
     * Color para el borde contenedor.
     */   
    private static final Color BORDE_COLOR = new Color(220, 200, 150);
    
    /**
     * Colores para la medalla de Oro.
     */
    private static final Color GOLD_COLOR = new Color(255, 215, 0);

    /**
     * Color para la medalla de Plata.
     */
    private static final Color SILVER_COLOR = new Color(189, 195, 199);

    /**
     * Color para la medalla de Bronce.
     */
    private static final Color BRONZE_COLOR = new Color(230, 126, 34);

    /**
     * Colores personalizados para el scrollbar.
     */
    private static final Color SCROLLBAR_THUMB = new Color(180, 180, 180);

    /**
     * Color del track del scrollbar.
     */
    private static final Color SCROLLBAR_TRACK = new Color(240, 240, 240);
    
    /**
     * Ancho de la tabla para que ocupe todo el espacio disponible.
     */
    private static final int TABLE_WIDTH = 640;  // Aumentado para llenar todo el ancho
    
    /**
     * Rutas de las imágenes de medallas de oro.
     */
    private static final String[] GOLD_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_oro.png"
    };

    /**
     * Rutas de las imágenes de medallas de plata.
     */
    private static final String[] SILVER_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_plata.png"
    };

    /**
     * Rutas de las imágenes de medallas de bronce.
     */
    private static final String[] BRONZE_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_bronce.png"
    };
    
    /**
     * Icono de medalla de oro.
     * Si no se puede cargar la imagen, se creará un icono de respaldo.
     */
    private ImageIcon goldMedalIcon;

    /**
     * Icono de medalla de plata.
     * Si no se puede cargar la imagen, se creará un icono de respaldo.
     */
    private ImageIcon silverMedalIcon;

    /**
     * Icono de medalla de bronce.
     * Si no se puede cargar la imagen, se creará un icono de respaldo.
     */
    private ImageIcon bronzeMedalIcon;
    
    /**
     * Modelo de tabla para mostrar el ranking.
     * Contiene las columnas: Posición, Nombre y Puntuación.
     */
    private DefaultTableModel model;

    /**
     * Tabla que muestra el ranking de jugadores.
     * Utiliza un renderizador personalizado para mostrar medallas.
     */
    private JTable table;

    /**
     * Panel de contenido que contiene la tabla y otros componentes.
     * Se utiliza para organizar el layout de la vista.
     */
    private JPanel contentPanel;

    /**
     * Panel con esquinas redondeadas que contiene la tabla.
     * Se utiliza para dar un aspecto más atractivo y consistente.
     */
    private RoundedPanel tablaPanel;


    /**
     * Constructor de la vista de ranking.
     * Configura el layout, carga las imágenes de medallas y crea los componentes necesarios.
     */
    public VistaRanking() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(new EmptyBorder(5, 20, 5, 20)); // Consistente con otras vistas
        setPreferredSize(new Dimension(700, 520)); // Tamaño consistente con VistaCuenta y VistaPantallaPrincipal
    
        // Cargar imágenes de medallas
        loadMedalImages();
        
        // Crear y añadir componentes
        JPanel titlePanel = crearPanelTitulo();
        contentPanel = crearPanelContenido();
        
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    

    /**
     * Crea el panel de título con el texto "TOP JUGADORES" estilizado.
     * Utiliza fichas de Scrabble con colores personalizados.
     * 
     * @return JPanel con el título estilizado.
     */
    private JPanel crearPanelTitulo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(APP_BG_COLOR);
        p.setBorder(new EmptyBorder(15, 0, 5, 0)); // Exactamente igual que en VistaCuenta
        
        // Crear título estilo Scrabble con fichas
        String[] letras = { "T", "O", "P", " ", "J", "U", "G", "A", "D", "O", "R", "E", "S" };
        Color[] colores = {
            new Color(220, 130, 95),   // Naranja rojizo
            new Color(95, 170, 220),   // Azul claro
            new Color(220, 180, 95),   // Amarillo
            APP_BG_COLOR,              // Fondo (espacio)
            new Color(150, 220, 95),   // Verde
            new Color(180, 95, 220),   // Morado/Lila
            new Color(220, 95, 160),   // Rosa
            new Color(95, 220, 190),   // Turquesa
            new Color(235, 140, 80),   // Naranja
            new Color(220, 130, 95),   // Naranja rojizo
            new Color(95, 170, 220),   // Azul claro
            new Color(220, 180, 95),   // Amarillo
            new Color(150, 220, 95)    // Verde
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
            if (i < letras.length - 1 && !" ".equals(letras[i + 1]))
                fichas.add(Box.createHorizontalStrut(5));
        }
        fichas.add(Box.createHorizontalGlue());
        p.add(fichas);
        return p;
    }


    /**
     * Crea una ficha de título estilizada con esquinas redondeadas y efecto hover.
     * Utiliza un JLabel personalizado para mostrar el texto con un fondo de color.
     * 
     * @param texto El texto a mostrar en la ficha.
     * @param color El color de fondo de la ficha.
     * @return JLabel configurado como ficha de título.
     */
    private JLabel crearFichaTitulo(String texto, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
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
        l.setFont(new Font("Arial Black", Font.BOLD, 28));  // Igual que VistaCuenta
        l.setPreferredSize(new Dimension(40, 40));          // Igual que VistaCuenta
        l.addMouseListener(new HoverEfectoTexto(l));
        return l;
    }


    /**
     * Crea el panel de contenido que contiene la tabla de ranking.
     * Configura el modelo de tabla, renderizador y scroll pane.
     * 
     * @return JPanel con la tabla de ranking.
     */
    private JPanel crearPanelContenido() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(APP_BG_COLOR);
        
        // Panel de tabla con esquinas redondeadas (como en VistaRecursos)
        tablaPanel = new RoundedPanel();
        tablaPanel.setLayout(new BorderLayout());
        tablaPanel.setBackground(APP_BG_COLOR);
        
        // Crear modelo de tabla
        String[] columns = {"Posición", "Nombre", "Puntuación"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Crear y configurar tabla
        table = new JTable(model);
        table.setTableHeader(null);
        table.setBackground(APP_BG_COLOR);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);
        
        // Configurar anchos de columnas (proporción personalizada)
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(TABLE_WIDTH / 6);    // Posición (más estrecha)
        columnModel.getColumn(0).setMaxWidth(TABLE_WIDTH / 6);
        columnModel.getColumn(1).setPreferredWidth(TABLE_WIDTH / 2);    // Nombre (más ancha)
        columnModel.getColumn(2).setPreferredWidth(TABLE_WIDTH / 3);    // Puntuación
        
        // Establecer renderizador personalizado
        table.setDefaultRenderer(Object.class, new RankingRenderer());
        
        // IMPORTANTE: Crear un panel personalizado que abarque completamente
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(APP_BG_COLOR);
        tableContainer.add(table, BorderLayout.CENTER);
        
        // Scroll con UI consistente y transparente
        JScrollPane scrollPane = new JScrollPane(tableContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(APP_BG_COLOR);
        scrollPane.setBackground(APP_BG_COLOR);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        
        // UI personalizada para scrollbar
        scrollPane.getVerticalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getHorizontalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        
        // Añadir un pequeño margen interno (como en VistaRecursos)
        tablaPanel.add(scrollPane, BorderLayout.CENTER);
        content.add(tablaPanel, BorderLayout.CENTER);
        
        return content;
    }
    
    /**
     * Actualiza la lista de jugadores en el ranking.
     * Limpia el modelo de tabla y añade las filas correspondientes.
     * Asegura que haya al menos 15 filas visibles, rellenando con filas vacías si es necesario.
     * 
     * @param list Lista de jugadores con sus puntuaciones.
     */
    public void setLista(List<Map.Entry<String, Integer>> list) {
        model.setRowCount(0);
        
        // Añadir filas a la tabla
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, Integer> entry = list.get(i);
            model.addRow(new Object[]{i + 1, entry.getKey(), entry.getValue()});
        }
        
        // Asegurar que haya suficientes filas para llenar el espacio
        int minRows = 15; // Incrementado para garantizar que llene toda la altura
        if (list.size() < minRows) {
            for (int i = list.size(); i < minRows; i++) {
                model.addRow(new Object[]{"", "", ""});
            }
        }
        
        // Forzar a la tabla a recalcular su tamaño
        table.revalidate();
        contentPanel.revalidate();
    }
    
    /**
     * Clase personalizada para el panel con esquinas redondeadas y sombra.
     * Utiliza un Graphics2D para dibujar un borde redondeado y un fondo con gradiente.
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
     * Carga las imágenes de las medallas desde los archivos especificados.
     * Si no se pueden cargar, crea iconos de respaldo con colores personalizados.
     */
    private void loadMedalImages() {
        boolean goldLoaded = false;
        boolean silverLoaded = false;
        boolean bronzeLoaded = false;
        
        for (String path : GOLD_MEDAL_PATHS) {
            try {
                ImageIcon icon = loadImageFromFile(path);
                if (icon != null) {
                    goldMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    goldLoaded = true;
                    break;
                }
            } catch (Exception e) {
            }
        }
        
        for (String path : SILVER_MEDAL_PATHS) {
            try {
                ImageIcon icon = loadImageFromFile(path);
                if (icon != null) {
                    silverMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    silverLoaded = true;
                    break;
                }
            } catch (Exception e) {
            }
        }
        
        for (String path : BRONZE_MEDAL_PATHS) {
            try {
                ImageIcon icon = loadImageFromFile(path);
                if (icon != null) {
                    bronzeMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    bronzeLoaded = true;
                    break;
                }
            } catch (Exception e) {
            }
        }
        
        // Si no se pudieron cargar, crear iconos de respaldo
        if (!goldLoaded) {
            goldMedalIcon = createFallbackMedalIcon(GOLD_COLOR, "1");
        }
        
        if (!silverLoaded) {
            silverMedalIcon = createFallbackMedalIcon(SILVER_COLOR, "2");
        }
        
        if (!bronzeLoaded) {
            bronzeMedalIcon = createFallbackMedalIcon(BRONZE_COLOR, "3");
        }
    }
    
    /**
     * Carga una imagen desde un archivo y devuelve un ImageIcon.
     * Si el archivo no existe o hay un error, devuelve null.
     * 
     * @param path Ruta del archivo de imagen.
     * @return ImageIcon cargado o null si no se pudo cargar.
     */
    private ImageIcon loadImageFromFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return new ImageIcon(file.getAbsolutePath());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    

    /**
     * Crea un icono de medalla de respaldo con un círculo simple y texto.
     * Utiliza un BufferedImage para dibujar el círculo y el texto.
     * 
     * @param color Color del círculo de la medalla.
     * @param text Texto a mostrar en la medalla.
     * @return ImageIcon con el diseño de respaldo.
     */
    private ImageIcon createFallbackMedalIcon(Color color, String text) {
        // Crear un icono circular simple como respaldo
        int size = 30;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillOval(0, 0, size - 1, size - 1);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (size - fm.stringWidth(text)) / 2, (size + fm.getAscent()) / 2);
        g.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Clase personalizada para el scrollbar con un diseño minimalista.
     * Utiliza colores personalizados y elimina los botones de incremento/decremento.
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
     * Clase para manejar el efecto hover en los textos de las fichas del título.
     * Cambia el color del texto al pasar el mouse por encima.
    */
    private static class HoverEfectoTexto extends MouseAdapter {
        private final JLabel label;
        HoverEfectoTexto(JLabel l) { this.label = l; }
        @Override public void mouseEntered(MouseEvent e) { label.setForeground(new Color(255, 255, 200)); }
        @Override public void mouseExited (MouseEvent e) { label.setForeground(Color.WHITE); }
    }
    
    /**
     * Clase personalizada para renderizar las celdas de la tabla del ranking.
     * Muestra medallas para las posiciones 1, 2 y 3, y aplica estilos personalizados
     */
    private class RankingRenderer extends DefaultTableCellRenderer {
        private final Font boldFont = new Font("Arial", Font.BOLD, 15);
        private final Font regularFont = new Font("Arial", Font.BOLD, 14);
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                
            // Para filas vacías (cuando no hay suficientes jugadores)
            if (value == null || value.toString().isEmpty()) {
                JLabel emptyLabel = new JLabel("");
                emptyLabel.setOpaque(true);
                emptyLabel.setBackground(APP_BG_COLOR);
                // Eliminar cualquier margen en las celdas vacías
                emptyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                return emptyLabel;
            }
                
            // Para columna de posición con medallas
            if (column == 0 && row < 3) {
                return createMedalComponent(row);
            }
            
            // Para otras celdas, usar renderizador estándar
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, false, row, column);
            
            // Ajustar el borde para que sea más pequeño pero mantenga legibilidad
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            
            Color rowBackground;
            
            // Formatear columna de posición con insignias coloridas para posiciones después del top 3
            if (column == 0) {
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setText(String.valueOf(value));
                rowBackground = row % 2 == 0 ? APP_BG_COLOR : new Color(235, 220, 170); // Alternar fila más sutil
                label.setFont(regularFont);
                label.setForeground(Color.DARK_GRAY);
            } 
            // Formatear columna de nombre
            else if (column == 1) {
                label.setHorizontalAlignment(JLabel.LEFT);
                if (row < 3) {
                    rowBackground = (row == 0) ? new Color(255, 215, 0, 90) : 
                                  (row == 1) ? new Color(189, 195, 199, 90) : 
                                               new Color(230, 126, 34, 90);
                    label.setFont(boldFont);
                } else {
                    rowBackground = row % 2 == 0 ? APP_BG_COLOR : new Color(235, 220, 170);
                    label.setFont(regularFont);
                }
                label.setForeground(Color.DARK_GRAY);
            } 
            // Formatear columna de puntuación
            else {
                label.setHorizontalAlignment(JLabel.RIGHT);
                if (row < 3) {
                    rowBackground = (row == 0) ? new Color(255, 215, 0, 90) : 
                                  (row == 1) ? new Color(189, 195, 199, 90) : 
                                               new Color(230, 126, 34, 90);
                    label.setFont(boldFont);
                } else {
                    rowBackground = row % 2 == 0 ? APP_BG_COLOR : new Color(235, 220, 170);
                    label.setFont(regularFont);
                }
                label.setForeground(Color.DARK_GRAY);
            }
            
            // Aplicar el fondo calculado
            label.setBackground(rowBackground);
            
            return label;
        }
        
        // Crear un componente de medalla con iconos de imágenes
        private Component createMedalComponent(int position) {
            JLabel medalLabel = new JLabel();
            medalLabel.setHorizontalAlignment(JLabel.CENTER);
            
            // Establecer el icono de medalla apropiado según la posición
            if (position == 0) {
                medalLabel.setIcon(goldMedalIcon);
                medalLabel.setBackground(new Color(255, 215, 0, 90));
            } else if (position == 1) {
                medalLabel.setIcon(silverMedalIcon);
                medalLabel.setBackground(new Color(189, 195, 199, 90));
            } else {
                medalLabel.setIcon(bronzeMedalIcon);
                medalLabel.setBackground(new Color(230, 126, 34, 90));
            }
            
            medalLabel.setOpaque(true);
            // Eliminar márgenes para que la medalla toque el borde
            medalLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            return medalLabel;
        }
    }
}