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

/**
 * Vista del juego que muestra el tablero, los racks de fichas y los controles.
 * Permite a los jugadores interactuar con el juego, colocar fichas y gestionar sus racks.
 */
public class VistaJuego extends JPanel {

    /**
     * Tamaño de cada celda del tablero.
     * Se utiliza para definir el tamaño de las celdas en el grid del tablero.
     */
    private static final int TILE_SIZE = 32;

    /**
     * Panel que contiene el grid del tablero.
     * Se utiliza para organizar las celdas del tablero en una cuadrícula.
     */
    private JPanel grid;

    /**
     * Matriz de celdas que representan el tablero.
     * Cada celda es un CellPanel que puede contener fichas.
     */
    private CellPanel[][] cells = new CellPanel[15][15];
    
    /**
     * Color del fondo de la interfaz.
     */
    private static final Color BG = new Color(242, 226, 177); 


    /**
     * Colores personalizados para el juego.
     * Se utilizan para los botones y otros elementos de la interfaz.
     */
    private JComponent tabla;

    /**
     * Colores personalizados para botón.
     */
    private static final Color LILA_OSCURO = new Color(52, 28, 87);

    /**
     * Color lila claro para botones.
     */
    private static final Color LILA_CLARO = new Color(180, 95, 220);

    /**
     * Color de primer plano para el texto.
     * Se utiliza para definir el color del texto en la interfaz.
     */
    private static final Color FG = new Color(20, 40, 80);  // Texto oscuro
    
    /**
     * Etiqueta para mostrar mensajes de error.
     * Se utiliza para informar al usuario sobre errores o acciones inválidas.
     */
    private JLabel errorLabel;


    /**
     * Atril de fichas del jugador.
     */
    private JPanel rack;


    /**
     * Botón de Fin de Turno.
     * Se utiliza para finalizar el turno del jugador actual y pasar al siguiente.
     */
    private JButton finTurno;

    /**
     * Botón de Reset.
     * Se utiliza para reiniciar el juego o el tablero a su estado inicial.
     */
    private JButton reset;

    /**
     * Botón para pasar el turno sin realizar ninguna acción.
     * Permite al jugador pasar su turno sin colocar fichas.
     */
    private JButton pasar;

    /**
     * Botón para salir del juego.
     * Permite al jugador abandonar el juego y volver al menú principal o cerrar la aplicación.
     */
    private JButton salir;

    /**
     * Botón de Ayuda.
     * Proporciona una palabra según las fichas del atril y las coloca en el tablero y pasa turno automáticamente.
     */
    private JButton ayuda;


    /**
    * Listener para manejar acciones de fichas en el tablero.
    */
    private TileActionListener tileActionListener;


    /**
     * Imagen circular para el avatar del jugador 1.
     * Se utiliza para mostrar la imagen del perfil del primer jugador.
     */
    private CircularProfileImage player1Image;

    /**
     * Imagen circular para el avatar del jugador 2.
     * Se utiliza para mostrar la imagen del perfil del segundo jugador.
     */
    private CircularProfileImage player2Image;

    /**
     * Etiquetas para mostrar el nombre y la puntuación de los jugadores.
     * Se utilizan para mostrar la información del jugador 1 y del jugador 2.
     */
    private JLabel player1Name;

    /**
     * Etiqueta para mostrar la puntuación del jugador 1.
     */
    private JLabel player1Score;

    /**
     * Etiqueta para mostrar el nombre del jugador 2.
     */
    private JLabel player2Name;

    /**
     * Etiqueta para mostrar la puntuación del jugador 2.
     */
    private JLabel player2Score;

    /**
     * Puntuación del primer jugador.
     * Se utiliza para llevar el conteo del primer jugador.
     */
    private int score1 = 0;

    /**
     * Puntuación del segundo jugador.
     * Se utiliza para llevar el conteo de puntos del segundo jugador.
     */
    private int score2 = 0;


    /**
     * Constructor de la vista del juego.
     * Configura el layout, el tamaño y los componentes principales de la vista.
     *
     * @param nombre1 Nombre del primer jugador.
     * @param nombre2 Nombre del segundo jugador.
     */
    public VistaJuego(String nombre1, String nombre2) {
        setLayout(new BorderLayout(5, 5));
        setSize(1920, 1080);
        setBackground(BG); 
        add(crearTabla(nombre1,nombre2), BorderLayout.WEST);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(getBackground());

        southPanel.add(crearRack());
        southPanel.add(Box.createVerticalStrut(10)); 
        
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setOpaque(false);
        
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(200, 0, 0));  
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setVisible(false);
        
        errorPanel.add(errorLabel);
        southPanel.add(errorPanel);
        southPanel.add(Box.createVerticalStrut(5)); 
        
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


    /**
     * Crea el tablero del juego.
     * Configura el panel que contiene el grid y añade las celdas al tablero.
     */
    public void crearTablero() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(getBackground());

        wrapper.add(grid);

        add(wrapper, BorderLayout.CENTER);
        wrapper.revalidate();
        wrapper.repaint();
        revalidate();
        repaint();

    }

    /**
     * Configura una celda del tablero con un tipo de bonificación.
     * Crea un CellPanel con el color y tipo de bonificación especificados.
     *
     * @param b Tipo de bonificación ("DL", "TL", "DP", "TP" o vacío).
     * @param i Fila de la celda.
     * @param j Columna de la celda.
     */
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

   

    /**
     * Establece el listener para manejar acciones de fichas en el tablero.
     *
     * @param listener El listener que manejará las acciones de fichas.
     */
    public void setTileActionListener(TileActionListener listener) {
        this.tileActionListener = listener;
    }


    /**
     * Interfaz para escuchar eventos de colocación y eliminación de fichas en el tablero.
     * Permite a los componentes interesados recibir notificaciones cuando se coloca o elimina una ficha.
     */
    public interface TileActionListener {

        /**
         * Método llamado cuando se coloca una ficha en el tablero.
         * Proporciona la letra de la ficha, su puntuación y su posición en el tablero.
         *
         * @param letter La letra de la ficha colocada.
         * @param score La puntuación de la ficha colocada.
         * @param row Fila donde se coloca la ficha.
         * @param col Columna donde se coloca la ficha.
         */
        void onTilePlaced(String letter, int score, int row, int col);

        /**
         * Método llamado cuando se elimina una ficha del tablero.
         * Proporciona la letra de la ficha, su puntuación y su posición en el tablero.
         *
         * @param letter La letra de la ficha eliminada.
         * @param score La puntuación de la ficha eliminada.
         * @param row Fila donde estaba la ficha.
         * @param col Columna donde estaba la ficha.
         */
        void onTileRemoved(String letter, int score, int row, int col);
    }

    /**
     * Clase interna que representa un panel de celda del tablero.
     * Cada celda puede contener una ficha y tiene un color de fondo específico según el tipo de bonificación.
     */
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


    /**
     * Crea el panel de controles del juego.
     * Contiene botones para acciones como reset, pasar turno, fin de turno, ayuda y salir.
     *
     * @return El panel de controles configurado.
     */
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

    /**
     * Función para añadir un listener al botón de fin de turno.
     * @param l Listener que se ejecutará cuando se pulse el botón de fin de turno.
     */
    public void finTurno(ActionListener l) {
        finTurno.addActionListener(l);
    }

    /**
     * Función para añadir un listener al botón de reset.
     * @param l Listener que se ejecutará cuando se pulse el botón de reset.
     */
    public void reset(ActionListener l) {
        reset.addActionListener(l);
    }

    /**
     * Función para añadir un listener al botón de pasar turno.
     * @param l Listener que se ejecutará cuando se pulse el botón de pasar turno.
     */
    public void pasar(ActionListener l) {
        pasar.addActionListener(l);
    }

    /**
     * Función para añadir un listener al botón de salir.
     * @param l Listener que se ejecutará cuando se pulse el botón de salir.
     */
    public void salir(ActionListener l) {
        salir.addActionListener(l);
    }

    /**
     * Función para añadir un listener al botón de ayuda.
     * Este botón proporciona una palabra según las fichas del atril y las coloca en el tablero.
     * @param l Listener que se ejecutará cuando se pulse el botón de ayuda.
     */
    public void ayuda(ActionListener l) {
        ayuda.addActionListener(l);
    }

    /**
     * Crea un botón de control con un texto específico y un color base.
     * Este botón tiene un diseño personalizado con efectos visuales al pasar el ratón.
     *
     * @param texto El texto que se mostrará en el botón.
     * @return El botón creado con el texto y color especificados.
     */
    private JButton crearBotonControl(String texto) {
        return crearBoton(texto, LILA_CLARO);
    }
    
    /**
     * Crea un botón con un texto y un color base.
     * Este botón tiene un diseño personalizado con efectos visuales al pasar el ratón.
     *
     * @param texto El texto que se mostrará en el botón.
     * @param base El color base del botón.
     * @return El botón creado con el texto y color especificados.
     */
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

    /**
     * Limpia el rack de fichas, el atril de fichas del jugador.
     */
    public void clearRack() {
        rack.removeAll();
        rack.revalidate();
        rack.repaint();
    }

    /**
     * Modifica el rack añadiendo una ficha.
     * La ficha se crea a partir de una cadena que contiene la letra y la puntuación.
     *
     * @param ficha Cadena con la letra y la puntuación de la ficha, separadas por un espacio.
     */
    public void modificarRack(String ficha) {

        String[] parts = ficha.trim().split(" ");

        int score = Integer.parseInt(parts[1]);
        TileLabel tile = new TileLabel(parts[0], score, -1, -1);
        instalarDrag(tile);
        rack.add(tile);
    }


    

    /**
     * Crea el panel que contiene el rack de fichas del jugador.
     * Este panel tiene un diseño personalizado con un fondo redondeado y un título.
     *
     * @return El panel del rack configurado.
     */
    private JPanel crearRack() {
        JPanel exterior = new JPanel(new GridBagLayout());
        exterior.setOpaque(false);
        
        JPanel contenedor = new JPanel(new BorderLayout(0, 0));
        contenedor.setOpaque(false);
        
        int anchoMaximo = 650; 
        contenedor.setPreferredSize(new Dimension(anchoMaximo, 80));
        contenedor.setMaximumSize(new Dimension(anchoMaximo, 80));
        
        rack = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                
                Color bgRack = new Color(250, 240, 210); 
                
                int radius = 18;
                g2.setColor(bgRack);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                
                g2.setColor(new Color(220, 190, 150));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, radius, radius);
                
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
     * Instala el soporte de arrastre en una ficha del juego.
     * Permite que la ficha sea arrastrada y soltada en el tablero.
     *
     * @param tile La ficha a la que se le instalará el soporte de arrastre.
     */
    private void instalarDrag(TileLabel tile) {
        tile.setTransferHandler(new TransferHandler() {
            @Override
            protected Transferable createTransferable(JComponent c) {
                if (tile.estaBloqueada()) {
                    return null;
                }
                return new StringSelection(tile.letter + " " + String.valueOf(tile.score));
            }

            @Override
            public int getSourceActions(JComponent c) {
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
                if (!tile.estaBloqueada()) {
                    JComponent c = (JComponent) e.getSource();
                    TransferHandler h = c.getTransferHandler();
                    h.exportAsDrag(c, e, TransferHandler.MOVE);
                }
            }
        });
    }

    /**
     * Clase interna que representa una etiqueta de ficha del juego.
     * Cada ficha tiene una letra, puntuación, fila y columna asociadas.
     * Permite arrastrar y soltar fichas en el tablero.
     */
    private static class TileLabel extends JComponent {

        private final String letter;
        private final int score;
        private final int row;
        private final int col;
        private final Dimension size = new Dimension(TILE_SIZE, TILE_SIZE);
        private boolean bloqueada = false;  

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
            repaint(); 
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

            RoundRectangle2D bg = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 12, 12
            );

            if (bloqueada) {
                g2.setColor(new Color(230, 198, 144));
            } else {
                g2.setColor(new Color(255, 223, 169));
            }

            g2.fill(bg);
            g2.setColor(new Color(220, 180, 140));
            g2.setStroke(new BasicStroke(2));
            g2.draw(bg);

            Font f1 = getFont().deriveFont(Font.BOLD, getHeight() * 0.5f);
            g2.setFont(f1);
            FontMetrics fm = g2.getFontMetrics();
            String s = String.valueOf(letter);
            int x = (getWidth() - fm.stringWidth(s)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(new Color(40, 40, 40));
            g2.drawString(s, x, y);

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


    /**
     * Coloca una ficha en el tablero en la posición especificada.
     * Si la letra es nula o vacía, se elimina cualquier ficha existente en esa celda.
     *
     * @param letra La letra de la ficha a colocar.
     * @param puntos Los puntos asociados a la ficha.
     * @param i Fila donde se colocará la ficha.
     * @param j Columna donde se colocará la ficha.
     */
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
     * Bloquea todas las fichas del tablero.
     * Recorre todas las celdas y bloquea cada ficha que encuentre.
     */
    public void bloquearTodasLasFichas() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                bloquearFicha(i, j, true);
            }
        }
    }


    /**
     * Establece los puntos de los jugadores y actualiza las etiquetas correspondientes.
     * También repinta la tabla si es necesario.
     *
     * @param score1 Puntuación del jugador 1.
     * @param score2 Puntuación del jugador 2.
     */
    public void setPuntos(int score1, int score2) {
        this.score1 = score1;
        this.score2 = score2;
        
        // Update the score labels
        if (player1Score != null) {
            player1Score.setText(score1 + " pts");
        }
        
        if (player2Score != null) {
            player2Score.setText(score2 + " pts");
        }

        if (tabla != null) {
            tabla.repaint();
        }
    }
    
    
    
    /**
     * Crea la tabla de jugadores con sus nombres y puntuaciones.
     * Configura un panel que muestra los avatares, nombres y puntuaciones de los jugadores.
     *
     * @param nombre1 Nombre del jugador 1.
     * @param nombre2 Nombre del jugador 2.
     * @return El componente JComponent que contiene la tabla de jugadores.
     */
    private JComponent crearTabla(String nombre1, String nombre2) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelJugadores = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 20, 20);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                g2.dispose();
            }
        };
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setOpaque(false);
        
        panelJugadores.setPreferredSize(new Dimension(200, 180));
        
        panelJugadores.add(crearPanelJugador(nombre1, score1, new Color(100, 150, 255), 1));
        System.out.println("Jugador 1: " + nombre1 + " con score: " + score1);
        panelJugadores.add(Box.createVerticalStrut(10));
        
        panelJugadores.add(crearPanelJugador(nombre2, score2, new Color(255, 100, 100), 2));
        
        wrapper.add(panelJugadores, BorderLayout.NORTH);
        tabla = wrapper;
        return wrapper;
    }

    /**
     * Crea un panel para mostrar la información de un jugador.
     * Incluye un avatar circular, nombre y puntuación del jugador.
     *
     * @param nombre Nombre del jugador.
     * @param puntos Puntuación del jugador.
     * @param color Color del panel del jugador.
     * @param jugadorId Identificador del jugador (1 o 2).
     * @return El panel configurado con la información del jugador.
     */
    private JPanel crearPanelJugador(String nombre, int puntos, Color color, int jugadorId) {
        JPanel panel = new JPanel(new BorderLayout(20, 0)); 
        panel.setOpaque(false);
        
        CircularProfileImage avatarPanel = new CircularProfileImage(50);
        
        if (jugadorId == 1) {
            player1Image = avatarPanel;
            player1Name = new JLabel(nombre);
            player1Score = new JLabel(puntos + " pts");
        } else {
            player2Image = avatarPanel;
            player2Name = new JLabel(nombre);
            player2Score = new JLabel(puntos + " pts");
        }
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nombreLabel = (jugadorId == 1) ? player1Name : player2Name;
        nombreLabel.setForeground(color.darker());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 15));
        nombreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel puntosLabel = (jugadorId == 1) ? player1Score : player2Score;
        puntosLabel.setForeground(FG);
        puntosLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        puntosLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(nombreLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(puntosLabel);
        
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        wrapperPanel.add(avatarPanel, BorderLayout.WEST);
        wrapperPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER); // Espacio adicional
        wrapperPanel.add(infoPanel, BorderLayout.EAST);
        
        JPanel decoratedPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(color);
                g2.fillRect(0, 0, 5, getHeight());
                
                Color bgColor = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    30 
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
        
        new Timer(10000, (e) -> {
            errorLabel.setVisible(false);
        }).start();
        
        revalidate();
        repaint();
    }



    /**
     * Establece la imagen del jugador 1 en el perfil circular.
     * Si la imagen es nula, se usa una imagen por defecto.
     *
     * @param img La imagen del jugador 1.
     */
    public void setPlayer1Image(BufferedImage img) {
        if (player1Image != null) {
            player1Image.setImage(img);
        }
    }

    /**
     * Establece la imagen del jugador 2 en el perfil circular.
     * Si la imagen es nula, se usa una imagen por defecto.
     *
     * @param img La imagen del jugador 2.
     */
    public void setPlayer2Image(BufferedImage img) {
        if (player2Image != null) {
            player2Image.setImage(img);
        }
    }

    /**
     * Clase interna que representa una imagen de perfil circular.
     * Esta clase se encarga de mostrar una imagen circular con un borde y un número opcional.
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
            
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(3, 3, size - 2, size - 2);
            
            Ellipse2D.Double clip = new Ellipse2D.Double(0, 0, size, size);
            g2.setClip(clip);
            g2.drawImage(image, 0, 0, size, size, null);
            g2.setClip(null);
            
            g2.setColor(new Color(180, 180, 180));
            g2.setStroke(new BasicStroke(2));
            g2.draw(clip);
            
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
