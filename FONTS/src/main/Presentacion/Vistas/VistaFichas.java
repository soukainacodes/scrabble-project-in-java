package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class VistaFichas extends JFrame {

    private static final int TILE_SIZE = 32;
    private JPanel rack;
    private JButton botonAceptar;
    private List<SelectableTileLabel> selectedTiles = new ArrayList<>();
    private ActionListener acceptListener;

    public VistaFichas() {
        setTitle("Seleccionar Fichas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 10));
        setBackground(new Color(242, 226, 177));
       
        // Crear el panel de fichas (rack)
        rack = crearRack();
        add(rack, BorderLayout.CENTER);
        
        // Crear el botón de aceptar
        botonAceptar = crearBotonControl("Aceptar");
        botonAceptar.addActionListener(e -> {
            if (acceptListener != null) {
                acceptListener.actionPerformed(e);
            }
        });
        
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(getBackground());
        panelBoton.add(botonAceptar);
        add(panelBoton, BorderLayout.SOUTH);
        
        // Configurar tamaño y posición
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    
    private JPanel crearRack() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(242, 226, 177));
        return panel;
    }
    
    public void addTile(String letter, int score) {
        SelectableTileLabel tile = new SelectableTileLabel(letter, score);
        rack.add(tile);
        rack.revalidate();
        rack.repaint();
    }
    
    public void clearRack() {
        rack.removeAll();
        selectedTiles.clear();
        rack.revalidate();
        rack.repaint();
    }
    
    public void setAcceptListener(ActionListener listener) {
        this.acceptListener = listener;
    }
    
    public String getSelectedTiles() {
        StringBuilder result = new StringBuilder();
        for (SelectableTileLabel tile : selectedTiles) {
            result.append(tile.letter);
            result.append(" ");
        }
        return result.toString();
    }
    
    private JButton crearBotonControl(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setForeground(new Color(60, 60, 80));
        b.setBackground(new Color(255, 255, 255));
        b.setOpaque(true);
        b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        b.setPreferredSize(new Dimension(100, 40));
        return b;
    }
    
    // Componente para una ficha seleccionable
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
    
    // Método para cargar fichas desde un formato similar al de VistaScrabble
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