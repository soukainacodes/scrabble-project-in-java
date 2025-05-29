package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;


/**
 * Vista de inicio de sesión y registro para la aplicación Scrabble.
 * Permite al usuario iniciar sesión o registrarse con un nombre de usuario y contraseña.
 * Incluye animaciones y un diseño atractivo con botones personalizados.
 */
public class VistaInicio extends JFrame {


    /**
     * Botón para iniciar sesión.
     */
    private ScrabbleTileButton botonIniciar;

    /**
     * Botón para registrar un nuevo usuario.
     */
    private ScrabbleTileButton botonRegistrar;

    /**
     * Campo de texto para ingresar el nombre de usuario.
     */
    private JTextField campoUsuario;

    /**
     * Campo de texto para ingresar la contraseña.
     * Utiliza JPasswordField para ocultar la entrada.
     */
    private JPasswordField campoContrasena;

    /**
     * Botón para entrar o registrarse, cambia su texto según la selección.
     * Utiliza un botón personalizado con estilo de ficha de Scrabble.
     */
    private ScrabbleTileButton botonEntrar;

    /**
     * Indica si el botón "Iniciar Sesión" está seleccionado.
     * Si es false, se asume que el botón "Registrar" está seleccionado.
     */
    private boolean iniciarSeleccionado = true;

    /**
     * Etiqueta para mostrar mensajes de error.
     * Se utiliza para informar al usuario sobre problemas de inicio de sesión o registro.
     */
    private JLabel error;

    /**
     * Colores personalizados para el diseño de la interfaz.
     * Utilizados para los botones y el fondo de la ventana.
     */
    private static final Color LILA_CLARO = new Color(180, 95, 220);

    /**
     * Color más oscuro para el botón de "Entrar" o "Registrarse".
     * Proporciona un contraste visual con el botón de selección.
     */
    private static final Color LILA_OSCURO = new Color(52, 28, 87);


    /**
     * Constructor de la vista de inicio.
     * Configura la ventana, los botones, campos de texto y etiquetas.
     * Añade animaciones y gestiona la interacción del usuario.
     */
    public VistaInicio() {
        setResizable(false);
        setTitle("SCRABBLE");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        Color fondoColor = new Color(242, 226, 177);
        getContentPane().setBackground(fondoColor);

        error = new JLabel("");
        error.setForeground(Color.RED);
        error.setFont(new Font("Arial", Font.PLAIN, 12));
        error.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(320, 500));
        tarjeta.setBackground(fondoColor);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0)); // Cambiar a GridLayout para garantizar espacio igual
        panelBotones.setBackground(fondoColor);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Añadir margen
        
        botonIniciar = new ScrabbleTileButton("Iniciar Sesión", LILA_CLARO);
        botonRegistrar = new ScrabbleTileButton("Registrar", LILA_CLARO);
        
        Dimension tamañoBotones = new Dimension(130, 45);
        botonIniciar.setPreferredSize(tamañoBotones);
        botonIniciar.setMaximumSize(tamañoBotones);
        botonRegistrar.setPreferredSize(tamañoBotones);
        botonRegistrar.setMaximumSize(tamañoBotones);

        botonIniciar.setSelected(true);
        botonRegistrar.setSelected(false);

        botonIniciar.addActionListener(e -> {
            iniciarSeleccionado = true;
            botonIniciar.setSelected(true);
            botonRegistrar.setSelected(false);
            botonEntrar.setText("Entrar");
        });

        botonRegistrar.addActionListener(e -> {
            iniciarSeleccionado = false;
            botonIniciar.setSelected(false);
            botonRegistrar.setSelected(true);
            botonEntrar.setText("Registrarse");
        });

        panelBotones.add(botonIniciar);
        panelBotones.add(botonRegistrar);

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
        campoContrasena.addActionListener(e -> {
            botonEntrar.doClick();
        });

        botonEntrar = new ScrabbleTileButton("Entrar", LILA_OSCURO);
        botonEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonEntrar.setPreferredSize(new Dimension(200, 45));
        botonEntrar.setMaximumSize(new Dimension(200, 45));

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

    /**
     * Método para obtener el botón de entrar o registrarse.
     * @return El botón configurado para iniciar sesión o registrarse.
     */
    public boolean getSeleccionado() {
        return iniciarSeleccionado;
    }

    /**
     * Añade un ActionListener al botón de entrar.
     * Este listener se activa cuando el usuario hace clic en el botón.
     * @param l El ActionListener a añadir.
     */
    public void entrar(ActionListener l)     { botonEntrar.addActionListener(l); }

    /**
     * Añade un ActionListener al campo contrasena.
     * Este listener se activa cuando el usuario presiona Enter en el campo de contraseña.
     * Se utiliza para permitir el inicio de sesión o registro al presionar Enter.
     * Esto es útil para mejorar la usabilidad, permitiendo que el usuario no tenga que hacer clic en el botón.
     * @param l El ActionListener a añadir.
     */
    public void pulsarEnter(ActionListener l){ campoContrasena.addActionListener(l); }

    /**
     * Añade un texto en la etiqueta de error.
     * Este texto se muestra cuando hay un problema con el inicio de sesión o registro.
     * @param txt El texto de error a mostrar.
     */
    public void setError(String txt)         { error.setText(txt); }

    /**
     * Obtiene el nombre de usuario ingresado por el usuario.
     * Estos valores se utilizan para iniciar sesión o registrarse.
     * @return El nombre de usuario ingresado.
     */
    public String getNombre()                { return campoUsuario.getText(); }

    /**
     * Obtiene la contraseña ingresada por el usuario.
     * Utiliza un JPasswordField para ocultar la entrada.
     * @return La contraseña ingresada como un array de caracteres.
     */
    public char[] getPassword()              { return campoContrasena.getPassword(); }

    /**
     * Limpia los campos de texto de nombre de usuario y contraseña.
     * Se utiliza para reiniciar el formulario después de un intento fallido.
     */
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

    /**
     * Clase interna para crear bordes redondeados personalizados.
     * Utilizada para los campos de texto y botones.
     */
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

    /**
     * Clase interna para crear botones personalizados con estilo de ficha de Scrabble.
     * Incluye animaciones de hover y selección, con colores personalizados.
     */
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
