package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class VistaRecursos extends JPanel {

    // Colores (consistentes con VistaRanking)
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);
    private static final Color SCROLLBAR_THUMB = new Color(180, 180, 180);
    private static final Color SCROLLBAR_TRACK = new Color(240, 240, 240);
    private static final Color LILA_CLARO = new Color(180, 95, 220);
    private static final Color LILA_OSCURO = new Color(52, 28, 87);
    
    // Dimensiones
    private static final int CONTENT_WIDTH = 600;
    private static final int CONTENT_HEIGHT = 330;
    
    private JButton botonAdd;
    private DefaultListModel<String> model;
    private JList<String> lista;
    private JButton botonEliminar;

    public VistaRecursos() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Crear y añadir componentes
        JPanel titlePanel = createTitlePanel();
        JPanel mainPanel = createMainPanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(700, 450)); // Consistente con VistaRanking
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(APP_BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        // Crear título estilo Scrabble con fichas (igual que en VistaRanking)
        String[] letras = { "R", "E", "C", "U", "R", "S", "O", "S" };
        Color[] colores = {
            new Color(220, 130, 95),   // Naranja rojizo
            new Color(95, 170, 220),   // Azul claro
            new Color(220, 180, 95),   // Amarillo
            new Color(150, 220, 95),   // Verde
            new Color(220, 95, 160),   // Rosa
            new Color(180, 95, 220),   // Morado/Lila
            new Color(95, 220, 190),   // Turquesa
            new Color(235, 140, 80)    // Naranja
        };
        
        JPanel fichasPanel = new JPanel();
        fichasPanel.setLayout(new BoxLayout(fichasPanel, BoxLayout.X_AXIS));
        fichasPanel.setBackground(APP_BG_COLOR);
        fichasPanel.add(Box.createHorizontalGlue());
        
        for (int i = 0; i < letras.length; i++) {
            final int idx = i;
            
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
            if (i < letras.length - 1) 
                fichasPanel.add(Box.createHorizontalStrut(5));
        }
        
        fichasPanel.add(Box.createHorizontalGlue());
        panel.add(fichasPanel);
        
        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(APP_BG_COLOR);
        
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
                
                if (isSelected) {
                    label.setBackground(LILA_CLARO);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(index % 2 == 0 ? APP_BG_COLOR : new Color(225, 225, 225));
                    label.setForeground(Color.DARK_GRAY);
                }
                
                return label;
            }
        });
        
        // Scroll con UI consistente con VistaRanking
        JScrollPane scrollPane = new JScrollPane(lista);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getViewport().setBackground(APP_BG_COLOR);
        scrollPane.setBackground(APP_BG_COLOR);
        
        // UI personalizada para scrollbar (igual que en VistaRanking)
        scrollPane.getVerticalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getHorizontalScrollBar().setUI(new MinimalistScrollBarUI());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        
        scrollPane.setPreferredSize(new Dimension(CONTENT_WIDTH, CONTENT_HEIGHT));
        
        // Panel con bordes redondeados para contener la lista
        JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Crear un borde redondeado con un color ligeramente más oscuro
                g2.setColor(new Color(220, 200, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Crear fondo interior con el color de fondo de la app
                g2.setColor(APP_BG_COLOR);
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 18, 18);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setOpaque(false);
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        roundedPanel.add(scrollPane);
        
        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botonesPanel.setBackground(APP_BG_COLOR);
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        botonAdd = createStylishButton("Añadir Recurso");
        botonEliminar = createStylishButton("Eliminar Recurso");
        
        botonesPanel.add(botonAdd);
        botonesPanel.add(botonEliminar);
        
        panel.add(roundedPanel, BorderLayout.CENTER);
        panel.add(botonesPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
private JButton createStylishButton(String text) {
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
        
        // Para asegurar que el tamaño sea correcto para componentes no-opacos
        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.width = Math.max(size.width, 150);
            size.height = Math.max(size.height, 40);
            return size;
        }
    };
    
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setForeground(Color.WHITE);
    button.setBackground(LILA_CLARO);
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setOpaque(false);
    
    // Efecto al pasar el ratón (cambio de color y efecto "pop-out")
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(LILA_OSCURO);
            button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(LILA_CLARO);
            button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            // Efecto de presionado
            button.setBorder(BorderFactory.createEmptyBorder(3, 3, 1, 1));
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            // Restaurar efecto hover
            button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
    });
    
    return button;
}
    // Clase UI de ScrollBar minimalista (copiada de VistaRanking para consistencia)
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
    
    // Métodos públicos para la funcionalidad
    public void addRecurso(ActionListener l) {
        botonAdd.addActionListener(l);
    }
    
    public void eliminarRecurso(ActionListener l) {
        botonEliminar.addActionListener(l);
    }

    public void removeLista(String s) {
        model.removeElement(s);
    }

    public void setLista(List<String> lista) {
        model.removeAllElements();
        for (String recurso : lista) {
            model.addElement(recurso);
        }
    }

    public String getSeleccionado() {
        return lista.getSelectedValue();
    }
}