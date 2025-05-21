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

public class VistaRanking extends JPanel {

    // Colores consistentes con VistaCuenta y VistaPantallaPrincipal
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Crema
    private static final Color FG = new Color(20, 40, 80);               // Texto oscuro
    private static final Color LILA_OSCURO = new Color(52, 28, 87);      // Exactamente igual que VistaCuenta
    private static final Color LILA_CLARO = new Color(180, 95, 220);     // Exactamente igual que VistaCuenta
    
    // Colores específicos de ranking
    private static final Color GOLD_COLOR = new Color(255, 215, 0);
    private static final Color SILVER_COLOR = new Color(189, 195, 199);
    private static final Color BRONZE_COLOR = new Color(230, 126, 34);
    private static final Color HEADER_TEXT = new Color(44, 62, 80);
    private static final Color SCROLLBAR_THUMB = new Color(180, 180, 180);
    private static final Color SCROLLBAR_TRACK = new Color(240, 240, 240);
    
    // Dimensiones
    private static final int TABLE_WIDTH = 500;  // Aumentado para aprovechar más espacio
    private static final int TABLE_HEIGHT = 330;
    
    // Rutas de imágenes
    private static final String[] GOLD_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_oro.png"
    };
    private static final String[] SILVER_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_plata.png"
    };
    private static final String[] BRONZE_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_bronce.png"
    };
    
    // Iconos de medallas
    private ImageIcon goldMedalIcon;
    private ImageIcon silverMedalIcon;
    private ImageIcon bronzeMedalIcon;
    
    // Componentes de la tabla
    private DefaultTableModel model;
    private JTable table;
    private JPanel contentPanel;

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

    private JPanel crearPanelContenido() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(APP_BG_COLOR);
        
        // Panel con marco redondeado y sombra (consistente con VistaCuenta)
        JPanel marco = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra externa mejorada - exactamente igual que VistaCuenta
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 22, 22);
                
                // Borde más definido - exactamente igual que VistaCuenta
                g2.setColor(new Color(220, 200, 150));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Gradiente interior mejorado - exactamente igual que VistaCuenta
                GradientPaint gp = new GradientPaint(
                    0, 0, APP_BG_COLOR.brighter(), 
                    0, getHeight(), APP_BG_COLOR
                );
                g2.setPaint(gp);
                g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 18, 18);
                g2.dispose();
            }
        };
        marco.setOpaque(false);
        marco.setBorder(new EmptyBorder(30, 30, 30, 30)); // Igual que VistaCuenta
        
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
        
        // Crear scrollpane con estilo minimalista
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getViewport().setBackground(APP_BG_COLOR);
        scrollPane.setBackground(APP_BG_COLOR);
        
        // UI personalizada para scrollbar
        scrollPane.getVerticalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getHorizontalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        
        // Añadir la tabla al marco
        marco.add(scrollPane, BorderLayout.CENTER);
        
        content.add(marco, BorderLayout.CENTER);
        return content;
    }
    
    /**
     * Establece los datos de la lista de ranking para mostrar en la tabla.
     */
    public void setLista(List<Map.Entry<String, Integer>> list) {
        model.setRowCount(0);
        
        // Añadir filas a la tabla
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, Integer> entry = list.get(i);
            model.addRow(new Object[]{i + 1, entry.getKey(), entry.getValue()});
        }
        
        // Forzar a la tabla a recalcular su tamaño
        table.revalidate();
        contentPanel.revalidate();
    }
    
    private void loadMedalImages() {
        boolean goldLoaded = false;
        boolean silverLoaded = false;
        boolean bronzeLoaded = false;
        
        // Intentar cargar medalla de oro
        for (String path : GOLD_MEDAL_PATHS) {
            try {
                ImageIcon icon = loadImageFromFile(path);
                if (icon != null) {
                    goldMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    goldLoaded = true;
                    break;
                }
            } catch (Exception e) {
                // Continuar con la siguiente ruta
            }
        }
        
        // Intentar cargar medalla de plata
        for (String path : SILVER_MEDAL_PATHS) {
            try {
                ImageIcon icon = loadImageFromFile(path);
                if (icon != null) {
                    silverMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    silverLoaded = true;
                    break;
                }
            } catch (Exception e) {
                // Continuar con la siguiente ruta
            }
        }
        
        // Intentar cargar medalla de bronce
        for (String path : BRONZE_MEDAL_PATHS) {
            try {
                ImageIcon icon = loadImageFromFile(path);
                if (icon != null) {
                    bronzeMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    bronzeLoaded = true;
                    break;
                }
            } catch (Exception e) {
                // Continuar con la siguiente ruta
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
    
    // Método para cargar imagen desde archivos
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
    
    // Método para cargar imagen desde classpath
    private ImageIcon loadImageFromClasspath(String path) {
        try {
            java.net.URL url = getClass().getClassLoader().getResource(path);
            if (url != null) {
                return new ImageIcon(url);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
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
    
    /* === Sub‑clases internas ===================================================== */
    // Clase UI de ScrollBar minimalista
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
    
    // Efecto de hover para las fichas del título
    private static class HoverEfectoTexto extends MouseAdapter {
        private final JLabel label;
        HoverEfectoTexto(JLabel l) { this.label = l; }
        @Override public void mouseEntered(MouseEvent e) { label.setForeground(new Color(255, 255, 200)); }
        @Override public void mouseExited (MouseEvent e) { label.setForeground(Color.WHITE); }
    }
    
    // Renderizador personalizado con iconos de medallas
    private class RankingRenderer extends DefaultTableCellRenderer {
        private final Font boldFont = new Font("Arial", Font.BOLD, 15);
        private final Font regularFont = new Font("Arial", Font.PLAIN, 14);
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                
            // Para columna de posición con medallas
            if (column == 0 && row < 3) {
                return createMedalComponent(row);
            }
            
            // Para otras celdas, usar renderizador estándar
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, false, row, column);
            
            // Restablecer borde
            label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            
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
            return medalLabel;
        }
    }
}