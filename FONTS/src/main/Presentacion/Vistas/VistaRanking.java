package Presentacion.Vistas;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

public class VistaRanking extends JPanel {

    // Match login background color
    private static final Color APP_BG_COLOR = new Color(238, 238, 238);
    
    // Bold, vibrant medal colors
    private static final Color GOLD_COLOR = new Color(255, 215, 0);
    private static final Color SILVER_COLOR = new Color(189, 195, 199);
    private static final Color BRONZE_COLOR = new Color(230, 126, 34);
    
    // Text colors
    private static final Color HEADER_TEXT = new Color(44, 62, 80);
    private static final Color MEDAL_TEXT = new Color(50, 50, 50);
    
    // Scrollbar colors
    private static final Color SCROLLBAR_THUMB = new Color(180, 180, 180);
    private static final Color SCROLLBAR_TRACK = new Color(240, 240, 240);
    
    // Fixed dimensions
    private static final int TABLE_WIDTH = 450;
    private static final int TABLE_HEIGHT = 370; // Made a little taller to compensate for no headers
    
    // Medal image paths - try multiple paths to find the correct one
    private static final String[] GOLD_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_oro.png"
    };
    
    private static final String[] SILVER_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_plata.png"
    };
    
    private static final String[] BRONZE_MEDAL_PATHS = {
        "FONTS/src/main/Recursos/Imagenes/medalla_bronce.png"
    };
    
    // Medal image cache
    private ImageIcon goldMedalIcon;
    private ImageIcon silverMedalIcon;
    private ImageIcon bronzeMedalIcon;
    
    private DefaultTableModel model;
    private JTable table;
    private JPanel contentPanel;



    public VistaRanking() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Reduced top padding

        // Load medal images
        loadMedalImages();
        
        // Create and add components
        JPanel titlePanel = createTitlePanel();
        contentPanel = createTablePanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Set fixed preferred size for the entire panel
        setPreferredSize(new Dimension(500, 450));
    }
    
private JPanel createTitlePanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    panel.setBackground(APP_BG_COLOR);
    panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    
    // Try to load the title image
    ImageIcon titleIcon = loadImageFromClasspath("FONTS/src/main/Recursos/Imagenes/top_jugadores.png");
    if (titleIcon == null) {
        titleIcon = loadImageFromFile("FONTS/src/main/Recursos/Imagenes/top_jugadores.png");
    }
    
    if (titleIcon != null) {
        // Resize image if needed - adjust width as necessary while maintaining aspect ratio
        int maxWidth = 300; // Maximum width for the title image
        if (titleIcon.getIconWidth() > maxWidth) {
            float ratio = (float)maxWidth / titleIcon.getIconWidth();
            int newHeight = Math.round(titleIcon.getIconHeight() * ratio);
            titleIcon = new ImageIcon(titleIcon.getImage().getScaledInstance(
                maxWidth, newHeight, Image.SCALE_SMOOTH));
        }
        
        JLabel titleLabel = new JLabel(titleIcon);
        panel.add(titleLabel);
    } else {
        // Fallback to text if image can't be loaded
        JLabel titleLabel = new JLabel("TOP JUGADORES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(HEADER_TEXT);
        panel.add(titleLabel);
    }
    
    return panel;
}


    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(APP_BG_COLOR);
        
        // Create table model - still needs column names internally even if not displayed
        String[] columns = {"Posición", "Nombre", "Puntuación"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create and configure table
        table = new JTable(model);
        
        // Hide the table header completely
        table.setTableHeader(null);
        
        // Set background color for the table itself
        table.setBackground(APP_BG_COLOR);
        
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);
        
        // Set column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(0).setMaxWidth(100);
        columnModel.getColumn(1).setPreferredWidth(250); // Wider name column
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(2).setMaxWidth(150);
        
        // Set custom renderer
        table.setDefaultRenderer(Object.class, new RankingRenderer());
        
        // Create scroll pane with minimalist style
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove border completely
        scrollPane.getViewport().setBackground(APP_BG_COLOR);
        scrollPane.setBackground(APP_BG_COLOR);
        
        // Custom scrollbar UI for minimalist look
        scrollPane.getVerticalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Thinner scrollbar
        scrollPane.getHorizontalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8)); // Thinner scrollbar
        
        // Set fixed size for scroll pane to ensure proper scrolling with many entries
        scrollPane.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Sets the ranking list data to be displayed in the table.
     * 
     * @param list List of entries containing player names and scores
     */
    public void setLista(List<Map.Entry<String, Integer>> list) {
        model.setRowCount(0);
        
        // Add rows to the table
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, Integer> entry = list.get(i);
            model.addRow(new Object[]{i + 1, entry.getKey(), entry.getValue()});
        }
        
        // Force table to recalculate its size based on content
        table.revalidate();
        contentPanel.revalidate();
    }
    
    private void loadMedalImages() {
        boolean goldLoaded = false;
        boolean silverLoaded = false;
        boolean bronzeLoaded = false;
        
        // Try to load gold medal
        for (String path : GOLD_MEDAL_PATHS) {
            try {
                // Try using class loader
                ImageIcon icon = loadImageFromClasspath(path);
                if (icon != null) {
                    goldMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    goldLoaded = true;
                    break;
                }
                
                // Try as direct file
                icon = loadImageFromFile(path);
                if (icon != null) {
                    goldMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    goldLoaded = true;
                    break;
                }
            } catch (Exception e) {
                // Continue to next path
            }
        }
        
        // Try to load silver medal
        for (String path : SILVER_MEDAL_PATHS) {
            try {
                // Try using class loader
                ImageIcon icon = loadImageFromClasspath(path);
                if (icon != null) {
                    silverMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    silverLoaded = true;
                    break;
                }
                
                // Try as direct file
                icon = loadImageFromFile(path);
                if (icon != null) {
                    silverMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    silverLoaded = true;
                    break;
                }
            } catch (Exception e) {
                // Continue to next path
            }
        }
        
        // Try to load bronze medal
        for (String path : BRONZE_MEDAL_PATHS) {
            try {
                // Try using class loader
                ImageIcon icon = loadImageFromClasspath(path);
                if (icon != null) {
                    bronzeMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    bronzeLoaded = true;
                    break;
                }
                
                // Try as direct file
                icon = loadImageFromFile(path);
                if (icon != null) {
                    bronzeMedalIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    bronzeLoaded = true;
                    break;
                }
            } catch (Exception e) {
                // Continue to next path
            }
        }
        
        // If any image couldn't be loaded, create fallback icons
        if (!goldLoaded) {
            System.err.println("Could not load gold medal image, using fallback");
            goldMedalIcon = createFallbackMedalIcon(GOLD_COLOR, "1");
        }
        
        if (!silverLoaded) {
            System.err.println("Could not load silver medal image, using fallback");
            silverMedalIcon = createFallbackMedalIcon(SILVER_COLOR, "2");
        }
        
        if (!bronzeLoaded) {
            System.err.println("Could not load bronze medal image, using fallback");
            bronzeMedalIcon = createFallbackMedalIcon(BRONZE_COLOR, "3");
        }
    }
    
    // Helper method to load image from classpath resources
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
    
    // Helper method to load image from file system
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
    
    private ImageIcon createFallbackMedalIcon(Color color, String text) {
        // Create a simple colored circle icon as fallback
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
    
    // Minimalist ScrollBar UI class
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
                            8, 8); // Rounded corners
            
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
    
    // Custom renderer with medal icons
    private class RankingRenderer extends DefaultTableCellRenderer {
        // Bold font for winners
        private final Font boldFont = new Font("Arial", Font.BOLD, 15);
        // Regular font
        private final Font regularFont = new Font("Arial", Font.PLAIN, 14);
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                
            // For position column with medals
            if (column == 0 && row < 3) {
                return createMedalComponent(row);
            }
            
            // For other cells, use standard renderer
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, false, row, column);
            
            // Reset border
            label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            
            Color rowBackground;
            
            // Format position column with colorful badges for positions after top 3
            if (column == 0) {
                label.setHorizontalAlignment(JLabel.CENTER);
                // Regular rows
                label.setText(String.valueOf(value));
                rowBackground = row % 2 == 0 ? APP_BG_COLOR : new Color(225, 225, 225);
                label.setFont(regularFont);
                label.setForeground(Color.DARK_GRAY);
            } 
            // Format name column
            else if (column == 1) {
                label.setHorizontalAlignment(JLabel.LEFT);
                if (row < 3) {
                    rowBackground = (row == 0) ? GOLD_COLOR : 
                                   (row == 1) ? SILVER_COLOR : BRONZE_COLOR;
                    label.setFont(boldFont);
                } else {
                    rowBackground = row % 2 == 0 ? APP_BG_COLOR : new Color(225, 225, 225);
                    label.setFont(regularFont);
                }
                label.setForeground(Color.DARK_GRAY);
            } 
            // Format score column
            else {
                label.setHorizontalAlignment(JLabel.RIGHT);
                if (row < 3) {
                    rowBackground = (row == 0) ? GOLD_COLOR : 
                                   (row == 1) ? SILVER_COLOR : BRONZE_COLOR;
                    label.setFont(boldFont);
                } else {
                    rowBackground = row % 2 == 0 ? APP_BG_COLOR : new Color(225, 225, 225);
                    label.setFont(regularFont);
                }
                label.setForeground(Color.DARK_GRAY);
            }
            
            // Apply the calculated background
            label.setBackground(rowBackground);
            
            return label;
        }
        
        // Create a medal component with image icons
        private Component createMedalComponent(int position) {
            JLabel medalLabel = new JLabel();
            medalLabel.setHorizontalAlignment(JLabel.CENTER);
            
            // Set appropriate medal icon based on position
            if (position == 0) {
                medalLabel.setIcon(goldMedalIcon);
                medalLabel.setBackground(GOLD_COLOR);
            } else if (position == 1) {
                medalLabel.setIcon(silverMedalIcon);
                medalLabel.setBackground(SILVER_COLOR);
            } else {
                medalLabel.setIcon(bronzeMedalIcon);
                medalLabel.setBackground(BRONZE_COLOR);
            }
            
            medalLabel.setOpaque(true);
            return medalLabel;
        }
    }
}