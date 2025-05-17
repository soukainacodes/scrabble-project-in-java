package Presentacion.Vistas;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

public class VistaRanking extends JPanel {

    // Match login background color
    private static final Color APP_BG_COLOR = new Color(238, 238, 238);
    
    // Gradient colors for header - matching login button gradient
    private static final Color HEADER_GRADIENT_START = new Color(86, 165, 243);
    private static final Color HEADER_GRADIENT_END = new Color(120, 232, 243);
    private static final Color BORDER_COLOR = new Color(220, 190, 170); // Match login input border
    
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
    private static final int TABLE_HEIGHT = 350; 
    
    private DefaultTableModel model;
    private JTable table;
    private JPanel contentPanel;

    public VistaRanking() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("TABLA DE CLASIFICACIÓN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(HEADER_TEXT);
        
        panel.add(titleLabel);
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(APP_BG_COLOR);
        
        // Create table model
        String[] columns = {"Posición", "Nombre", "Puntuación"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create and configure table
        table = new JTable(model);
        
        // Set background color for the table itself
        table.setBackground(APP_BG_COLOR);
        
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);
        
        // Set custom renderer for header - gradient style matching login buttons
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(null);
        header.setReorderingAllowed(false);
        
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
    
    // Custom header renderer with gradient matching login buttons
    private class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel headerLabel = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            
            // Custom panel with gradient painting
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Create gradient from left to right
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(250, 250, 250), 
                        getWidth(), 0, new Color(240, 240, 240));
                    
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Add subtle bottom border
                    g2d.setColor(BORDER_COLOR);
                    g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                }
            };
            
            panel.setLayout(new BorderLayout());
            
            // Configure header text
            headerLabel.setHorizontalAlignment(JLabel.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
            headerLabel.setForeground(HEADER_TEXT);
            headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            
            // Add label to panel
            panel.add(headerLabel, BorderLayout.CENTER);
            return panel;
        }
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
    
    private class RankingRenderer extends DefaultTableCellRenderer {
        // Bold font for winners
        private final Font boldFont = new Font("Arial", Font.BOLD, 15);
        // Regular font
        private final Font regularFont = new Font("Arial", Font.PLAIN, 14);
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, false, row, column);
            
            // Reset border
            label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            
            Color rowBackground;
            
            // Format position column with colorful badges for top 3
            if (column == 0) {
                label.setHorizontalAlignment(JLabel.CENTER);
                
                // Apply medal styling for top 3 positions
                if (row == 0) {
                    label.setText("1º");
                    rowBackground = GOLD_COLOR;
                    label.setFont(boldFont);
                    label.setForeground(MEDAL_TEXT);
                } else if (row == 1) {
                    label.setText("2º");
                    rowBackground = SILVER_COLOR;
                    label.setFont(boldFont);
                    label.setForeground(MEDAL_TEXT);
                } else if (row == 2) {
                    label.setText("3º");
                    rowBackground = BRONZE_COLOR;
                    label.setFont(boldFont);
                    label.setForeground(MEDAL_TEXT);
                } else {
                    // Regular rows
                    label.setText(String.valueOf(value));
                    rowBackground = row % 2 == 0 ? APP_BG_COLOR : new Color(225, 225, 225);
                    label.setFont(regularFont);
                    label.setForeground(Color.DARK_GRAY);
                }
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
    }
}