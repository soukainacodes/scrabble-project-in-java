package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class VistaInicio extends JFrame {

    private ScrabbleTileButton botonIniciar;
    private ScrabbleTileButton botonRegistrar;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private ScrabbleTileButton botonEntrar;
    private boolean iniciarSeleccionado = true;
    private JLabel error;

    private static final Color LILA_CLARO = new Color(180, 95, 220);
    private static final Color LILA_OSCURO = new Color(52, 28, 87);

    public VistaInicio() {
        // Configuración inicial de la ventana
        setResizable(false);
        setTitle("SCRABBLE");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        // Color de fondo crema
        Color fondoColor = new Color(242, 226, 177);
        getContentPane().setBackground(fondoColor);

        // Label de error
        error = new JLabel("");
        error.setForeground(Color.RED);
        error.setFont(new Font("Arial", Font.PLAIN, 12));
        error.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel principal (tarjeta)
        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(320, 500));
        tarjeta.setBackground(fondoColor);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Panel de selección Iniciar vs Registrar ---
                // --- Panel de selección Iniciar vs Registrar ---
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0)); // Cambiar a GridLayout para garantizar espacio igual
        panelBotones.setBackground(fondoColor);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Añadir margen
        
        // Creamos ambos botones con estilo de ficha
        botonIniciar = new ScrabbleTileButton("Iniciar Sesión", LILA_CLARO);
        botonRegistrar = new ScrabbleTileButton("Registrar", LILA_CLARO);
        
        // Tamaño más pequeño para los botones si es necesario
        Dimension tamañoBotones = new Dimension(130, 45);
        botonIniciar.setPreferredSize(tamañoBotones);
        botonIniciar.setMaximumSize(tamañoBotones);
        botonRegistrar.setPreferredSize(tamañoBotones);
        botonRegistrar.setMaximumSize(tamañoBotones);

        // Estado inicial
        botonIniciar.setSelected(true);
        botonRegistrar.setSelected(false);

        // Acción al pulsar "Iniciar Sesión"
        botonIniciar.addActionListener(e -> {
            iniciarSeleccionado = true;
            botonIniciar.setSelected(true);
            botonRegistrar.setSelected(false);
            botonEntrar.setText("Entrar");
        });

        // Acción al pulsar "Registrar"
        botonRegistrar.addActionListener(e -> {
            iniciarSeleccionado = false;
            botonIniciar.setSelected(false);
            botonRegistrar.setSelected(true);
            botonEntrar.setText("Registrarse");
        });

        panelBotones.add(botonIniciar);
        panelBotones.add(botonRegistrar);

        // --- Campos Usuario y Contraseña ---
        JLabel labelUsuario = new JLabel("Nombre de Usuario");
        labelUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        labelUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoUsuario = new JTextField();
        campoUsuario.setMaximumSize(new Dimension(250, 65));
        campoUsuario.setBorder(new RoundedBorder(20, new Color(220, 190, 170)));
        campoUsuario.setBackground(fondoColor);
        campoUsuario.setHorizontalAlignment(JTextField.CENTER);
        campoUsuario.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel labelContrasena = new JLabel("Contraseña");
        labelContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        labelContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoContrasena = new JPasswordField();
        campoContrasena.setMaximumSize(new Dimension(250, 65));
        campoContrasena.setBorder(new RoundedBorder(20, new Color(220, 190, 170)));
        campoContrasena.setBackground(fondoColor);
        campoContrasena.setHorizontalAlignment(JTextField.CENTER);
        campoContrasena.setFont(new Font("Arial", Font.BOLD, 16));
        // Añadir el listener para cuando se pulse Enter en el campo de contraseña
        campoContrasena.addActionListener(e -> {
            // Simular clic en el botón Entrar
            botonEntrar.doClick();
        });

        // --- Botón Entrar/Registrarse ---
        botonEntrar = new ScrabbleTileButton("Entrar", LILA_OSCURO);
        botonEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonEntrar.setPreferredSize(new Dimension(200, 45));
        botonEntrar.setMaximumSize(new Dimension(200, 45));

        // --- Título SCRABBLE animado ---
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.X_AXIS));
        panelTitulo.setBackground(fondoColor);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        panelTitulo.add(Box.createHorizontalGlue());

        String[] letras = { "S","C","R","A","B","B","L","E" };
        Color[] colores = {
            new Color(220,130,95),
            new Color(95,170,220),
            new Color(220,180,95),
            new Color(150,220,95),
            new Color(180,95,220),
            new Color(220,95,160),
            new Color(95,220,190),
            new Color(235,140,80)
        };
        JLabel[] letrasLabels = new JLabel[letras.length];

        for (int i = 0; i < letras.length; i++) {
            final int idx = i;
            JLabel letra = new JLabel(letras[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                    // Ficha con fondo y sombra
                    g2.setColor(colores[idx]);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
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
            letra.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    letra.setForeground(new Color(255,255,200));
                }
                @Override public void mouseExited(MouseEvent e) {
                    letra.setForeground(Color.WHITE);
                }
            });
            panelTitulo.add(letra);
            letrasLabels[i] = letra;
            if (i < letras.length - 1) panelTitulo.add(Box.createHorizontalStrut(5));
        }

        new Timer(3000, ae -> {
            for (int i = 0; i < letrasLabels.length; i++) {
                JLabel lbl = letrasLabels[i];
                Timer t = new Timer(100 * i, ev -> animarLetra(lbl));
                t.setRepeats(false);
                t.start();
            }
        }).start();

        panelTitulo.add(Box.createHorizontalGlue());

        // --- Montaje final ---
        tarjeta.add(panelTitulo);
        tarjeta.add(panelBotones);
        tarjeta.add(labelUsuario);
        tarjeta.add(Box.createVerticalStrut(5));
        tarjeta.add(campoUsuario);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(labelContrasena);
        tarjeta.add(Box.createVerticalStrut(5));
        tarjeta.add(campoContrasena);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(error);
        tarjeta.add(botonEntrar);
        tarjeta.add(Box.createVerticalGlue());

        add(tarjeta);
        setVisible(true);
    }

    /** Devuelve true si “Iniciar Sesión” está activo, false si “Registrar” */
    public boolean getSeleccionado() {
        return iniciarSeleccionado;
    }

    public void entrar(ActionListener l)     { botonEntrar.addActionListener(l); }
    public void pulsarEnter(ActionListener l){ campoContrasena.addActionListener(l); }
    public void setError(String txt)         { error.setText(txt); }
    public String getNombre()                { return campoUsuario.getText(); }
    public char[] getPassword()              { return campoContrasena.getPassword(); }

    /** Rebote de letra */
    private void animarLetra(JLabel letra) {
        Point pos = letra.getLocation();
        Timer anim = new Timer(20, null);
        final int[] step = {0}, max = {15};
        anim.addActionListener(e -> {
            if (step[0] < max[0]) {
                int dy = (int)(10 * Math.sin(step[0] * Math.PI / max[0]));
                letra.setLocation(pos.x, pos.y - dy);
                step[0]++;
            } else {
                letra.setLocation(pos);
                anim.stop();
            }
        });
        anim.start();
    }

    /** Borde redondeado para JTextField/JPasswordField */
    static class RoundedBorder extends AbstractBorder {
        private int radius; private Color color;
        public RoundedBorder(int r, Color c) { radius = r; color = c; }
        @Override public void paintBorder(Component c, Graphics g,
                                          int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(new BasicStroke(3));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
    }

    /** JButton estilo Scrabble con “rise” y estado seleccionado */
    private class ScrabbleTileButton extends JButton {
        private final Color base, highlight, shadow;
        private final int radius = 15;
        private boolean selected = false;

        public ScrabbleTileButton(String text, Color baseColor) {
            super(text);
            this.base      = baseColor;
            this.highlight = baseColor.brighter();
            this.shadow    = baseColor.darker();
            setForeground(Color.WHITE);
            setFont(getFont().deriveFont(Font.BOLD, 14f));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            // Al pulsar, cambiar inmediatamente el estado y repintar
            addActionListener(e -> {
                // No hacemos nada extra aquí; VistaLogin gestiona selected
            });
        }

        public void setSelected(boolean sel) {
            this.selected = sel;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            boolean hover = getModel().isRollover();
            Color face = selected ? LILA_OSCURO : base;

            // Drop-shadow on hover
            if (hover) {
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(3, 3, w - 3, h - 3, radius, radius);
            }

            // Main face
            g2.setColor(face);
            g2.fillRoundRect(
                0, 0,
                w - (hover ? 3 : 0),
                h - (hover ? 3 : 0),
                radius, radius
            );

            // Bevel: top/left highlight
            g2.setColor(highlight);
            g2.drawLine(radius, 0, w - radius - (hover ? 3 : 0), 0);
            g2.drawLine(0, radius, 0, h - radius - (hover ? 3 : 0));

            // Bevel: bottom/right shadow
            g2.setColor(shadow);
            g2.drawLine(
                radius,
                h - 1 - (hover ? 3 : 0),
                w - radius - (hover ? 3 : 0),
                h - 1 - (hover ? 3 : 0)
            );
            g2.drawLine(
                w - 1 - (hover ? 3 : 0),
                radius,
                w - 1 - (hover ? 3 : 0),
                h - radius - (hover ? 3 : 0)
            );

            // Outline when selected
            if (selected) {
                g2.setColor(highlight);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(
                    1, 1,
                    w - 2 - (hover ? 3 : 0),
                    h - 2 - (hover ? 3 : 0),
                    radius, radius
                );
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
