package Presentacion.Vistas;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class VistaScrabble extends JPanel {

    private static final int TILE_SIZE = 48;

    public VistaScrabble() {
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(186,187,200,255));

        // Tablero en el centro
        add(crearTablero(), BorderLayout.CENTER);

        // Rack + controles al sur
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(getBackground());

        southPanel.add(crearRack());
        southPanel.add(Box.createVerticalStrut(10)); // espacio
        southPanel.add(crearPanelControles());

        add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel crearTablero() {
        // Panel que contiene el grid, centrado y con tamaño fijo
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(getBackground());

        JPanel grid = new JPanel(new GridLayout(15, 15, 2, 2)) {
            @Override
            public Dimension getPreferredSize() {
                int total = TILE_SIZE * 15 + 2 * 14;
                return new Dimension(total, total);
            }

            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        grid.setBackground(new Color(200, 200, 200));

        for (int i = 0; i < 15 * 15; i++) {
            grid.add(new CellPanel());
        }
        wrapper.add(grid);
        return wrapper;
    }

    private class CellPanel extends JPanel {

        CellPanel() {
            super(new BorderLayout());
            setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
            setBorder(new LineBorder(Color.GRAY));
            setBackground(new Color(255, 248, 230));

            // Drop: acepta StringFlavor y pone un TileLabel draggable
            setTransferHandler(new TransferHandler() {
                @Override
                public boolean canImport(TransferSupport support) {
                    return support.isDataFlavorSupported(DataFlavor.stringFlavor)
                            && getComponentCount() == 0;
                }

                @Override
                public boolean importData(TransferSupport support) {
                    if (!canImport(support)) {
                        return false;
                    }
                    try {
                        String data = (String) support.getTransferable()
                                .getTransferData(DataFlavor.stringFlavor);
                        char letter = data.charAt(0);
                        int score = Integer.parseInt(data.substring(1));
                        TileLabel tile = new TileLabel(letter, score);
                        instalarDrag(tile);
                        removeAll();
                        add(tile, BorderLayout.CENTER);
                        revalidate();
                        repaint();
                        return true;
                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                }

                @Override
                public int getSourceActions(JComponent c) {
                    return NONE;
                }
            });
        }
    }

    private JPanel crearPanelControles() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        p.setBackground(getBackground());

        p.add(crearBotonControl("Abandonar"));
        p.add(crearBotonControl("Omitir"));
        p.add(crearBotonControl("Cambiar"));
        p.add(crearBotonPrimario("Colocar"));

        return p;
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

    private JButton crearBotonPrimario(String texto) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 180, 180),
                        getWidth(), 0, new Color(0, 210, 210)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(100, 40));
        return b;
    }

    private JPanel crearRack() {
        JPanel rack = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rack.setBackground(getBackground());

        List<String> fichas = Arrays.asList("A1", "I1", "O1", "S1", "R1", "M3", "Q10");
        for (String s : fichas) {
            char letter = s.charAt(0);
            int score = Integer.parseInt(s.substring(1));
            TileLabel tile = new TileLabel(letter, score);
            instalarDrag(tile);
            rack.add(tile);
        }

        // rack acepta devolver fichas
        rack.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport supp) {
                return supp.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            @Override
            public boolean importData(TransferSupport supp) {
                if (!canImport(supp)) {
                    return false;
                }
                try {
                    String data = (String) supp.getTransferable()
                            .getTransferData(DataFlavor.stringFlavor);
                    char letter = data.charAt(0);
                    int score = Integer.parseInt(data.substring(1));
                    TileLabel tile = new TileLabel(letter, score);
                    instalarDrag(tile);
                    rack.add(tile);
                    rack.revalidate();
                    rack.repaint();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });

        return rack;
    }

    /**
     * Hace draggable un TileLabel, eliminándolo en MOVE desde su contenedor
     * original.
     */
    private void instalarDrag(TileLabel tile) {
        tile.setTransferHandler(new TransferHandler() {
            @Override
            protected Transferable createTransferable(JComponent c) {
                return new StringSelection(tile.letter + String.valueOf(tile.score));
            }

            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            @Override
            protected void exportDone(JComponent src, Transferable data, int action) {
                if (action == MOVE) {
                    Container p = src.getParent();
                    p.remove(src);
                    p.revalidate();
                    p.repaint();
                }
            }
        });
        tile.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JComponent c = (JComponent) e.getSource();
                TransferHandler h = c.getTransferHandler();
                h.exportAsDrag(c, e, TransferHandler.MOVE);
            }
        });
    }

    /**
     * Componente que pinta la ficha estilo Scrabble
     */
    private static class TileLabel extends JComponent {

        private final char letter;
        private final int score;
        private final Dimension size = new Dimension(TILE_SIZE, TILE_SIZE);

        TileLabel(char letter, int score) {
            this.letter = letter;
            this.score = score;
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // fondo redondeado
            RoundRectangle2D bg = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 12, 12
            );
            g2.setColor(new Color(255, 223, 169));
            g2.fill(bg);
            g2.setColor(new Color(220, 180, 140));
            g2.setStroke(new BasicStroke(2));
            g2.draw(bg);

            // letra grande
            Font f1 = getFont().deriveFont(Font.BOLD, getHeight() * 0.5f);
            g2.setFont(f1);
            FontMetrics fm = g2.getFontMetrics();
            String s = String.valueOf(letter);
            int x = (getWidth() - fm.stringWidth(s)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(new Color(40, 40, 40));
            g2.drawString(s, x, y);

            // puntuación pequeña
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

    // Para prueba
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Scrabble");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.getContentPane().add(new VistaScrabble());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
