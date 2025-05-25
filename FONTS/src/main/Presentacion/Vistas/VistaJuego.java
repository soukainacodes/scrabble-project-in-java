package Presentacion.Vistas;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class VistaJuego extends JPanel {

    private static final int TILE_SIZE = 32;
    private JPanel grid;
    private CellPanel[][] cells = new CellPanel[15][15];
    
    // Colores temáticos (agregados desde VistaCuenta)
    private static final Color BG = new Color(242, 226, 177);  // Crema
    private static final Color LILA_OSCURO = new Color(52, 28, 87);
    private static final Color LILA_CLARO = new Color(180, 95, 220);
    private static final Color FG = new Color(20, 40, 80);  // Texto oscuro
    
    // Añadir el error label como variable de clase
    private JLabel errorLabel;

    // Añadir estas variables de clase
    private CircularProfileImage player1Image;
    private CircularProfileImage player2Image;
    private JLabel player1Name;
    private JLabel player1Score;
    private JLabel player2Name;
    private JLabel player2Score;

    private int score1 = 0;
    private int score2 = 0;

    public VistaJuego(String nombre1, String nombre2) {
        setLayout(new BorderLayout(5, 5));
        setSize(1920, 1080);
        setBackground(BG);  // Cambiado al nuevo color crema
        add(crearTabla(nombre1,nombre2), BorderLayout.WEST);
        // Tablero en el centro

        // Rack + controles al sur
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(getBackground());

        southPanel.add(crearRack());
        southPanel.add(Box.createVerticalStrut(10)); // espacio
        
        // Crear y añadir el panel de error
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setOpaque(false);
        
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(200, 0, 0));  // Rojo para errores
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setVisible(false);
        
        errorPanel.add(errorLabel);
        southPanel.add(errorPanel);
        southPanel.add(Box.createVerticalStrut(5)); // espacio después del error
        
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
            if (tileActionListener != null) {
                tileActionListener.onTilePlaced(
                        tile.getLetter(),
                        tile.getScore(),
                        tile.getRow(),
                        tile.getCol()
                );
            }
        });
        cell.addPropertyChangeListener("tileRemoved", evt -> {
            TileLabel removed = (TileLabel) evt.getOldValue();
            System.out.println("Ha salido la ficha " + removed.getLetter() + " en " + removed.getRow() + " " + removed.getCol());
            if (tileActionListener != null) {
                tileActionListener.onTileRemoved(
                        removed.getLetter(),
                        removed.score,
                        removed.getRow(),
                        removed.getCol()
                );
            }
        });
        cells[i][j] = cell;
        grid.add(cell);

    }

    private TileActionListener tileActionListener;

    public void setTileActionListener(TileActionListener listener) {
        this.tileActionListener = listener;
    }

    public interface TileActionListener {

        void onTilePlaced(String letter, int score, int row, int col);

        void onTileRemoved(String letter, int score, int row, int col);
    }

    private class CellPanel extends JPanel {

        private final Label placeholder;

        CellPanel(int R, int G, int B, String bonus, int row, int col) {
            super(new BorderLayout());
            setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
            setBorder(new LineBorder(Color.GRAY));
            setBackground(new Color(R, G, B));

            placeholder = new Label(bonus, Label.CENTER);
            placeholder.setForeground(Color.BLACK);
            add(placeholder, BorderLayout.CENTER);
            // Drop: acepta StringFlavor y pone un TileLabel draggable
            setTransferHandler(new TransferHandler() {
                @Override
                public boolean canImport(TransferSupport support) {
                    // Check if there's already a tile (not just any component)
                    for (Component comp : getComponents()) {
                        if (comp instanceof TileLabel) {
                            return false;
                        }
                    }
                    return support.isDataFlavorSupported(DataFlavor.stringFlavor);
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

                        // Hide the placeholder but don't remove it
                        placeholder.setVisible(false);

                        // notifica inserción
                        firePropertyChange("tile", null, tile);

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
                // Make the placeholder visible again
                placeholder.setVisible(true);
            }
            super.remove(comp);
        }

        @Override
        public void removeAll() {
            // si hay una única ficha, la notificamos antes de quitar todo
            for (Component comp : getComponents()) {
                if (comp instanceof TileLabel) {
                    firePropertyChange("tileRemoved", comp, null);
                    break;
                }
            }

            // Only remove TileLabels, keep the placeholder
            Component[] components = getComponents();
            for (Component comp : components) {
                if (comp instanceof TileLabel) {
                    super.remove(comp);
                }
            }

            // Make sure placeholder is visible
            placeholder.setVisible(true);
        }
    }

    private JButton finTurno;
    private JButton reset;
    private JButton pasar;
    private JButton salir;
    private JButton ayuda;

    private JPanel crearPanelControles() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        p.setBackground(getBackground());
        
        reset = crearBotonControl("Reset");
        p.add(reset);
        
        pasar = crearBotonControl("Pasar");
        p.add(pasar);
        
        finTurno = crearBoton("Fin de turno", LILA_OSCURO); 
        finTurno.setPreferredSize(new Dimension(160, 40)); // Hacemos este botón aún más ancho
        p.add(finTurno);
        
        ayuda = crearBotonControl("Ayuda");
        p.add(ayuda);
        
        salir = crearBotonControl("Salir");
        p.add(salir);

        return p;
    }

    public void finTurno(ActionListener l) {
        finTurno.addActionListener(l);
    }

    public void reset(ActionListener l) {
        reset.addActionListener(l);
    }

    public void pasar(ActionListener l) {
        pasar.addActionListener(l);
    }

    public void salir(ActionListener l) {
        salir.addActionListener(l);
    }

    public void ayuda(ActionListener l) {
        ayuda.addActionListener(l);
    }
    private JButton crearBotonControl(String texto) {
        return crearBoton(texto, LILA_CLARO);
    }
    
    private JButton crearBotonPrimario(String texto) {
        return crearBoton(texto, LILA_OSCURO);
    }
    
    private JButton crearBoton(String texto, Color base) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int radius = 15;
                boolean over = getModel().isRollover();
                boolean press = getModel().isPressed();
                Color bg = press ? base.darker().darker() : (over ? base.darker() : base);
                
                if (over && !press) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, radius, radius);
                }
                
                g2.setPaint(new GradientPaint(0, 0,
                        new Color(Math.min(bg.getRed() + 25, 255), Math.min(bg.getGreen() + 25, 255), Math.min(bg.getBlue() + 25, 255)),
                        0, getHeight(), bg));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                if (!press) {
                    g2.setColor(new Color(255, 255, 255, 70));
                    g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() / 2 - 2, radius, radius);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setPreferredSize(new Dimension(100, 40));
        
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return b;
    }

    public void clearRack() {
        rack.removeAll();
        rack.revalidate();
        rack.repaint();
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
        // Panel exterior con márgenes para centrar el rack
        JPanel exterior = new JPanel(new GridBagLayout());
        exterior.setOpaque(false);
        
        // Creamos un panel contenedor con borde y efecto visual
        JPanel contenedor = new JPanel(new BorderLayout(0, 0));
        contenedor.setOpaque(false);
        
        // Limitar el ancho máximo del contenedor
        int anchoMaximo = 650; // Puedes ajustar este valor según necesites
        contenedor.setPreferredSize(new Dimension(anchoMaximo, 80));
        contenedor.setMaximumSize(new Dimension(anchoMaximo, 80));
        
        // El rack en sí mismo
        rack = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo del rack ligeramente más claro que el fondo general
                Color bgRack = new Color(250, 240, 210); // Tono crema más claro
                
                // Dibujar fondo redondeado
                int radius = 18;
                g2.setColor(bgRack);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                
                // Borde fino
                g2.setColor(new Color(220, 190, 150));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, radius, radius);
                
                // Efecto de iluminación superior
                g2.setPaint(new GradientPaint(0, 0, 
                        new Color(255, 255, 255, 100), 
                        0, getHeight()/2, new Color(255, 255, 255, 0)));
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()/2, radius, radius);
                
                g2.dispose();
            }
        };
        
        // Altura mínima para el rack
        rack.setPreferredSize(new Dimension(100, 70));
        rack.setMinimumSize(new Dimension(100, 70));
        
        // Hacemos que el panel sea transparente para que se vea nuestro fondo personalizado
        rack.setOpaque(false);
        
        // Agregamos un borde invisible para padding
        rack.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Añadir etiqueta de "TUS FICHAS"
        JLabel titulo = new JLabel("TUS FICHAS");
        titulo.setFont(new Font("Arial", Font.BOLD, 12));
        titulo.setForeground(LILA_OSCURO);
        titulo.setHorizontalAlignment(JLabel.CENTER);
        
        // Panel para el título con fondo redondeado
        JPanel tituloPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tituloPanel.setOpaque(false);
        tituloPanel.add(titulo);
        
        // Agregar al contenedor
        contenedor.add(tituloPanel, BorderLayout.NORTH);
        contenedor.add(rack, BorderLayout.CENTER);
        
        // Configuramos el TransferHandler para el rack
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

        // Añadir el contenedor centrado al panel exterior
        exterior.add(contenedor);
        
        return exterior;
    }

    /**
     * Hace draggable un TileLabel, eliminándolo en MOVE desde su contenedor
     * original.
     */
    private void instalarDrag(TileLabel tile) {
        tile.setTransferHandler(new TransferHandler() {
            @Override
            protected Transferable createTransferable(JComponent c) {
                // No permitir arrastre si está bloqueada
                if (tile.estaBloqueada()) {
                    return null;
                }
                return new StringSelection(tile.letter + " " + String.valueOf(tile.score));
            }

            @Override
            public int getSourceActions(JComponent c) {
                // No permitir arrastre si está bloqueada
                if (tile.estaBloqueada()) {
                    return NONE;
                }
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
                // Solo iniciar arrastre si la ficha no está bloqueada
                if (!tile.estaBloqueada()) {
                    JComponent c = (JComponent) e.getSource();
                    TransferHandler h = c.getTransferHandler();
                    h.exportAsDrag(c, e, TransferHandler.MOVE);
                }
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
        private boolean bloqueada = false;  // Nueva propiedad para bloqueo

        public String getLetter() {
            return this.letter;
        }

        public int getRow() {
            return this.row;
        }

        public int getCol() {
            return this.col;
        }

        public int getScore() {
            return this.score;
        }

        public boolean estaBloqueada() {
            return this.bloqueada;
        }

        public void setBloqueada(boolean bloqueada) {
            this.bloqueada = bloqueada;
            repaint(); // Para actualizar la visualización
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

            // Color según bloqueo (opcional)
            if (bloqueada) {
                // Color más oscuro para fichas bloqueadas
                g2.setColor(new Color(230, 198, 144));
            } else {
                g2.setColor(new Color(255, 223, 169));
            }

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

    public void ponerFichaTablero(String letra, int puntos, int i, int j) {
        if (letra != null && !letra.isEmpty()) {

            for (Component comp : cells[i][j].getComponents()) {
                if (comp instanceof TileLabel) {
                    cells[i][j].remove(comp);
                    break;
                }
            }

            for (Component comp : cells[i][j].getComponents()) {
                if (comp instanceof Label) {
                    comp.setVisible(false);
                    break;
                }
            }

            TileLabel tile = new TileLabel(letra, puntos, i, j);
            instalarDrag(tile);
            cells[i][j].add(tile, BorderLayout.CENTER);

            // Asegurarse que todas las actualizaciones visuales se realizan
            cells[i][j].revalidate();
            cells[i][j].repaint();
            grid.revalidate();
            grid.repaint();
        } else {;
            if (cells[i][j] != null) {
                cells[i][j].removeAll();
                cells[i][j].revalidate();
                cells[i][j].repaint();
            }
            grid.revalidate();
            grid.repaint();
        }
    }

    /**
     * Bloquea o desbloquea una ficha en la posición especificada.
     *
     * @param i Fila de la ficha
     * @param j Columna de la ficha
     * @param bloquear true para bloquear, false para desbloquear
     * @return true si la ficha fue encontrada y su estado cambió, false en caso
     * contrario
     */
    public boolean bloquearFicha(int i, int j, boolean bloquear) {
        if (cells[i][j] == null) {
            return false;
        }

        for (Component comp : cells[i][j].getComponents()) {
            if (comp instanceof TileLabel) {
                TileLabel tile = (TileLabel) comp;
                tile.setBloqueada(bloquear);
                return true;
            }
        }

        return false;
    }

    /**
     * Bloquea todas las fichas en el tablero
     */
    public void bloquearTodasLasFichas() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                bloquearFicha(i, j, true);
            }
        }
    }

    public void setPuntos(int score1, int score2) {
        this.score1 = score1;
        this.score2 = score2;
        if (tabla != null) {
            tabla.repaint();
        }
    }
    private JComponent tabla;

    private JComponent crearTabla(String nombre1, String nombre2) {
        // Crear panel principal con padding
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Crear panel para contener los jugadores
        JPanel panelJugadores = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Pintar fondo redondeado con borde suave
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra externa
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 20, 20);
                
                // Fondo principal
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                g2.dispose();
            }
        };
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setOpaque(false);
        
        // Tamaño fijo para el panel
        panelJugadores.setPreferredSize(new Dimension(200, 180));
        
        // Crear y añadir cada jugador
        panelJugadores.add(crearPanelJugador(nombre1, score1, new Color(100, 150, 255), 1));
        
        // Separador
        panelJugadores.add(Box.createVerticalStrut(10));
        
        // Segundo jugador
        panelJugadores.add(crearPanelJugador(nombre2, score2, new Color(255, 100, 100), 2));
        
        wrapper.add(panelJugadores, BorderLayout.NORTH);
        tabla = wrapper;
        return wrapper;
    }

    // Crear un panel para cada jugador
    private JPanel crearPanelJugador(String nombre, int puntos, Color color, int jugadorId) {
        JPanel panel = new JPanel(new BorderLayout(20, 0)); // Aumentado de 10 a 20 para más espacio entre imagen y texto
        panel.setOpaque(false);
        
        // Avatar circular (versión reducida)
        CircularProfileImage avatarPanel = new CircularProfileImage(50);
        
        // Guardar referencias para poder actualizar después
        if (jugadorId == 1) {
            player1Image = avatarPanel;
            player1Name = new JLabel(nombre);
            player1Score = new JLabel(puntos + " pts");
        } else {
            player2Image = avatarPanel;
            player2Name = new JLabel(nombre);
            player2Score = new JLabel(puntos + " pts");
        }
        
        // Panel para nombre y puntuación
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        // Nombre con color destacado
        JLabel nombreLabel = (jugadorId == 1) ? player1Name : player2Name;
        nombreLabel.setForeground(color.darker());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 15));
        nombreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Puntuación
        JLabel puntosLabel = (jugadorId == 1) ? player1Score : player2Score;
        puntosLabel.setForeground(FG);
        puntosLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        puntosLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Añadir al panel de información
        infoPanel.add(nombreLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(puntosLabel);
        
        // Alinear todo con padding
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Añadir componentes con más espacio entre ellos
        wrapperPanel.add(avatarPanel, BorderLayout.WEST);
        wrapperPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER); // Espacio adicional
        wrapperPanel.add(infoPanel, BorderLayout.EAST);
        
        // Decoración de fondo con color del jugador
        JPanel decoratedPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Franja lateral con el color del jugador
                g2.setColor(color);
                g2.fillRect(0, 0, 5, getHeight());
                
                // Fondo con el color del jugador pero MUY transparente (alpha = 30)
                Color bgColor = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    30 // Mucho más transparente (antes era 50)
                );
                g2.setColor(bgColor);
                g2.fillRect(5, 0, getWidth() - 5, getHeight());
                
                g2.dispose();
            }
        };
        
        decoratedPanel.setOpaque(false);
        decoratedPanel.add(wrapperPanel);
        
        return decoratedPanel;
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



    // Métodos para actualizar las imágenes de perfil
    public void setPlayer1Image(BufferedImage img) {
        if (player1Image != null) {
            player1Image.setImage(img);
        }
    }

    public void setPlayer2Image(BufferedImage img) {
        if (player2Image != null) {
            player2Image.setImage(img);
        }
    }

    /**
     * Imagen circular con sombra y borde.
     */
    private static class CircularProfileImage extends JPanel {
        private final int size;
        private BufferedImage image;
        private BufferedImage defaultImage;
        private final int playerId;

        CircularProfileImage(int size) {
            this.size = size;
            this.playerId = 0;
            setPreferredSize(new Dimension(size, size));
            setOpaque(false);
            cargarImagenPorDefecto();
        }
        
        CircularProfileImage(int size, int playerId) {
            this.size = size;
            this.playerId = playerId;
            setPreferredSize(new Dimension(size, size));
            setOpaque(false);
            cargarImagenPorDefecto();
        }

        private void cargarImagenPorDefecto() {
            try {
                File f = new File("FONTS/src/main/Recursos/Imagenes/default_profile.png");
                defaultImage = f.exists() ? ImageIO.read(f) : crearImagenSilhouette();
            } catch (IOException e) {
                defaultImage = crearImagenSilhouette();
            }
            image = defaultImage;
        }

        private BufferedImage crearImagenSilhouette() {
            BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(new GradientPaint(0, 0, new Color(190, 190, 190), size, size, new Color(230, 230, 230)));
            g.fillOval(0, 0, size, size);
            g.setColor(new Color(240, 240, 240));
            g.fillOval(size / 4, size / 6, size / 2, size / 2);              // cabeza
            g.fillOval(size / 4 - size / 8, size / 2, size / 2 + size / 4, size / 2); // torso
            g.setColor(new Color(0, 0, 0, 30));
            g.setStroke(new BasicStroke(2));
            g.drawOval(2, 2, size - 4, size - 4);
            g.dispose();
            return img;
        }

        public void setImage(BufferedImage img) {
            if (img == null) return;
            double scale = (double) size / Math.max(img.getWidth(), img.getHeight());
            int w = (int) (img.getWidth() * scale);
            int h = (int) (img.getHeight() * scale);
            BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = scaled.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(img, (size - w) / 2, (size - h) / 2, w, h, null);
            g.dispose();
            this.image = scaled;
            repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Sombra
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(3, 3, size - 2, size - 2);
            
            // Avatar
            Ellipse2D.Double clip = new Ellipse2D.Double(0, 0, size, size);
            g2.setClip(clip);
            g2.drawImage(image, 0, 0, size, size, null);
            g2.setClip(null);
            
            // Borde
            g2.setColor(new Color(180, 180, 180));
            g2.setStroke(new BasicStroke(2));
            g2.draw(clip);
            
            // Número de jugador
            if (playerId > 0) {
                g2.setFont(new Font("Arial", Font.BOLD, 22));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                String text = String.valueOf(playerId);
                int textX = (size - fm.stringWidth(text)) / 2;
                int textY = (size + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, textX, textY);
            }
            
            g2.dispose();
        }
    }
}
