package Presentacion.Vistas;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class VistaScrabble extends JPanel {

    private static final int TILE_SIZE = 32;
    private JPanel grid;

    public VistaScrabble() {
        setLayout(new BorderLayout(5, 5));
        setSize(1920, 1080);
        setBackground(new Color(238, 238, 238, 255));
        add(crearTabla(), BorderLayout.WEST);
        // Tablero en el centro

        // Rack + controles al sur
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(getBackground());

        southPanel.add(crearRack());
        southPanel.add(Box.createVerticalStrut(10)); // espacio
        southPanel.add(crearPanelControles());

        add(southPanel, BorderLayout.SOUTH);
        grid = new JPanel(new GridLayout(15, 15, 2, 2)) {
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

    }

    public void crearTablero() {
        // Panel que contiene el grid, centrado y con tamaño fijo
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(getBackground());

        wrapper.add(grid);

        add(wrapper, BorderLayout.CENTER);
        wrapper.revalidate();
        wrapper.repaint();
        revalidate();
        repaint();

    }

    public void configurarTablero(String b, int i, int j) {
        CellPanel cell;
        if (b.equals("DL")) {
            cell = new CellPanel(189, 236, 248, "DL", i, j);

        } else if (b.equals("TL")) {
            cell = new CellPanel(69, 192, 236, "TL", i, j);

        } else if (b.equals("DP")) {
            cell = new CellPanel(244, 180, 188, "DP", i, j);

        } else if (b.equals("TP")) {
            cell = new CellPanel(247, 77, 90, "TP", i, j);

        } else {
            cell = new CellPanel(255, 248, 230, "", i, j);

        }

        cell.addPropertyChangeListener("tile", evt -> {
            TileLabel tile = (TileLabel) evt.getNewValue();
            // ¡aquí recibes la ficha sin haber definido otra clase!
            System.out.println("Ha caído la ficha " + tile.getLetter() + " en " + tile.getRow() + " " + tile.getCol());
            // p.ej. modelo.placeTile(pos, tile);
        });
        cell.addPropertyChangeListener("tileRemoved", evt -> {
            TileLabel removed = (TileLabel) evt.getOldValue();
            System.out.println("Ha salido la ficha " + removed.getLetter() + " en " + removed.getRow() + " " + removed.getCol());
            // Actualiza tu modelo, UI, etc.
        });
        grid.add(cell);
        System.out.println("se añaden cosas");
    }

    private class CellPanel extends JPanel {

        //  private final Label placeholder;
        CellPanel(int R, int G, int B, String bonus, int row, int col) {
            super(new BorderLayout());
            setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
            setBorder(new LineBorder(Color.GRAY));
            setBackground(new Color(R, G, B));

            //   placeholder = new Label(bonus, Label.CENTER);
            // placeholder.setForeground(Color.BLACK);
            // add(placeholder, BorderLayout.CENTER);
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
                        System.out.println("Importando " + data);
                        String[] parts = data.trim().split(" ");
                        String letter = parts[0];
                        int score = Integer.parseInt(parts[1]);
                        TileLabel tile = new TileLabel(letter, score, row, col);
                        instalarDrag(tile);

                        // notifica inserción
                        firePropertyChange("tile", null, tile);

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

        @Override
        public void remove(Component comp) {
            // captura la ficha que se va a eliminar
            if (comp instanceof TileLabel) {
                firePropertyChange("tileRemoved", comp, null);
            }
            super.remove(comp);
        }

        @Override
        public void removeAll() {
            // si hay una única ficha, la notificamos antes de quitar todo
            if (getComponentCount() == 1) {
                Component old = getComponent(0);
                if (old instanceof TileLabel) {
                    firePropertyChange("tileRemoved", old, null);
                }
            }
            super.removeAll();
        }
    }


    private JPanel crearPanelControles() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        p.setBackground(getBackground());

        p.add(crearBotonControl("Reset"));
        p.add(crearBotonControl("Pasar"));
        p.add(crearBotonControl("Fin Turno"));
        p.add(crearBotonPrimario("Salir"));

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

    public void modificarRack(String ficha) {

        String[] parts = ficha.trim().split(" ");

        int score = Integer.parseInt(parts[1]);
        TileLabel tile = new TileLabel(parts[0], score, -1, -1);
        instalarDrag(tile);
        rack.add(tile);
    }

    private JPanel rack;

    private JPanel crearRack() {
        rack = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rack.setBackground(getBackground());

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
                    String[] parts = data.trim().split(" ");
                    String letter = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    TileLabel tile = new TileLabel(letter, score, -1, -1);
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
                return new StringSelection(tile.letter + " " +  String.valueOf(tile.score));
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

        private final String letter;
        private final int score;
        private final int row;
        private final int col;
        private final Dimension size = new Dimension(TILE_SIZE, TILE_SIZE);

        public String getLetter() {
            return this.letter;
        }

        public int getRow() {
            return this.row;
        }

        public int getCol() {
            return this.col;
        }

        TileLabel(String letter, int score, int row, int col) {
            this.letter = letter;
            this.score = score;
            this.row = row;
            this.col = col;
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

    private JComponent crearTabla() {
        // Creamos un componente que pinta todo el scoreboard
        JComponent tabla = new JComponent() {
            private int score1 = 1500;
            private int score2 = 1200;
            private final int WIDTH = 200;
            private final int HEIGHT = 100;
            private final int ARC = 20;
            private final Color BG = Color.WHITE;
            private final Color HEADER1_COLOR = new Color(100, 150, 255);
            private final Color HEADER2_COLOR = new Color(255, 100, 100);
            private final Color DIVIDER_COLOR = new Color(220, 220, 220);
            private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 14);
            private final Font SCORE_FONT = new Font("Arial", Font.BOLD, 28);

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(WIDTH, HEIGHT);
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }

            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // 1) Fondo redondeado
                g2.setColor(BG);
                g2.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC, ARC);

                // 2) Cabecera Jugador 1
                int headerH = 24;
                g2.setColor(HEADER1_COLOR);
                g2.fillRoundRect(0, 0, WIDTH / 2, headerH, ARC, ARC);
                g2.fillRect(0, headerH / 2, WIDTH / 2, headerH / 2);

                // 3) Cabecera Jugador 2
                g2.setColor(HEADER2_COLOR);
                g2.fillRoundRect(WIDTH / 2, 0, WIDTH / 2, headerH, ARC, ARC);
                g2.fillRect(WIDTH / 2, headerH / 2, WIDTH / 2, headerH / 2);

                // 4) Texto de headers
                g2.setFont(HEADER_FONT);
                g2.setColor(Color.WHITE);
                FontMetrics fmH = g2.getFontMetrics();
                String t1 = "JUGADOR 1", t2 = "JUGADOR 2";
                int x1 = (WIDTH / 2 - fmH.stringWidth(t1)) / 2;
                int x2 = WIDTH / 2 + (WIDTH / 2 - fmH.stringWidth(t2)) / 2;
                int yH = (headerH + fmH.getAscent() - fmH.getDescent()) / 2;
                g2.drawString(t1, x1, yH);
                g2.drawString(t2, x2, yH);

                // 5) Línea divisoria vertical
                g2.setColor(DIVIDER_COLOR);
                int y0 = headerH + 8, y1 = HEIGHT - 16, xm = WIDTH / 2;
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(xm, y0, xm, y1);

                // 6) Puntuaciones
                g2.setFont(SCORE_FONT);
                FontMetrics fmS = g2.getFontMetrics();
                // Jugador1
                String s1 = String.valueOf(score1);
                int sx1 = (WIDTH / 2 - fmS.stringWidth(s1)) / 2;
                int sy = HEIGHT - (HEIGHT - headerH) / 2 + fmS.getAscent() / 2 - 4;
                g2.setColor(HEADER1_COLOR);
                g2.drawString(s1, sx1, sy);
                // Jugador2
                String s2 = String.valueOf(score2);
                int sx2 = WIDTH / 2 + (WIDTH / 2 - fmS.stringWidth(s2)) / 2;
                g2.setColor(HEADER2_COLOR);
                g2.drawString(s2, sx2, sy);

                g2.dispose();
            }
        };

        // Añadimos un pequeño margen
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(tabla, BorderLayout.NORTH);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return wrapper;
    }
}
