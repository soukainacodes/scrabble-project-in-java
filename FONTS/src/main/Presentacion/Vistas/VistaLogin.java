package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; // Añadir esta línea
import javax.swing.*;
import javax.swing.border.AbstractBorder;


public class VistaLogin extends JFrame {

    private JButton botonIniciar;
    private JButton botonRegistrar;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonEntrar;
    private boolean iniciarSeleccionado = true;
    private JLabel error;



    public VistaLogin() {
        setResizable(false);
        setTitle("SCRABBLE");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Centrar todo

        Color fondoColor = new Color(238, 238, 238, 255); // color crema
        getContentPane().setBackground(fondoColor);
        error = new JLabel("");
        error.setForeground(Color.RED);
        error.setFont(new Font("Arial", Font.PLAIN, 12));
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(320, 500)); // Antes era 450
        tarjeta.setBackground(fondoColor);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(fondoColor);

        botonIniciar = crearBotonSelector("Iniciar Sesión", true);
        botonRegistrar = crearBotonSelector("Registrar", false);

        panelBotones.add(botonIniciar);
        panelBotones.add(botonRegistrar);

        // --- Formulario ---
        JLabel labelUsuario = new JLabel("Nombre de Usuario");
        labelUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        labelUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoUsuario = new JTextField();
        campoUsuario.setMaximumSize(new Dimension(250, 60));
        campoUsuario.setBorder(new RoundedBorder(20, new Color(220, 190, 170)));
        campoUsuario.setBackground(fondoColor);
        campoUsuario.setHorizontalAlignment(JTextField.CENTER);
        campoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        // campoUsuario.setFont(new Font("Arial", Font.PLAIN, 24)); // tamaño de letra
        // grande
        JLabel labelContrasena = new JLabel("Contraseña");
        labelContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        labelContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoContrasena = new JPasswordField();
        campoContrasena.setMaximumSize(new Dimension(250, 60));
        campoContrasena.setBorder(new RoundedBorder(20, new Color(220, 190, 170)));
        campoContrasena.setBackground(fondoColor);
        campoContrasena.setHorizontalAlignment(JTextField.CENTER);
        campoContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoContrasena.addActionListener(e -> botonEntrar.doClick());

        // --- Botón Entrar ---
        botonEntrar = new JButton("Entrar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(86, 99, 243), getWidth(), 0,
                        new Color(86, 232, 243));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        botonEntrar.setForeground(Color.WHITE);
        botonEntrar.setFont(new Font("Arial", Font.BOLD, 16));
        botonEntrar.setFocusPainted(false);
        botonEntrar.setContentAreaFilled(false);
        botonEntrar.setBorderPainted(false);
        botonEntrar.setOpaque(false);
        botonEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonEntrar.setPreferredSize(new Dimension(200, 45));
        botonEntrar.setMaximumSize(new Dimension(200, 45));

        // Añadir título SCRABBLE
        // --- Agregando componentes ---

        // --- Título divertido y dinámico de SCRABBLE ---
        JPanel panelTitulo = new JPanel(); BoxLayout boxLayout = new BoxLayout(panelTitulo, BoxLayout.X_AXIS); panelTitulo.setLayout(boxLayout);
        panelTitulo.setBackground(fondoColor);
        panelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        String[] letras = { "S", "C", "R", "A", "B", "B", "L", "E" };
        Color[] colores = {
                new Color(220, 130, 95), // Naranja rojizo
                new Color(95, 170, 220), // Azul claro
                new Color(220, 180, 95), // Amarillo
                new Color(150, 220, 95), // Verde
                new Color(180, 95, 220), // Morado
                new Color(220, 95, 160), // Rosa
                new Color(95, 220, 190), // Turquesa
                new Color(235, 140, 80) // Naranja
        };

        // Espacio al inicio para centrar
        panelTitulo.add(Box.createHorizontalGlue());

                // Crear un arreglo para almacenar las referencias a las letras
        JLabel[] letrasLabels = new JLabel[letras.length];

        // Crear ficha para cada letra
        for (int i = 0; i < letras.length; i++) {
            final int index = i;
            JLabel letra = new JLabel(letras[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Dibujar ficha cuadrada con esquinas redondeadas
                    g2.setColor(colores[index]);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                    // Añadir sombra
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(3, 3, getWidth(), getHeight(), 10, 10);

                    super.paintComponent(g);
                }
            };

            // Estilo de la letra
            letra.setFont(new Font("Arial Black", Font.BOLD, 28));
            letra.setForeground(Color.WHITE);
            letra.setHorizontalAlignment(SwingConstants.CENTER);
            letra.setPreferredSize(new Dimension(40, 40));
            letra.setMaximumSize(new Dimension(40, 40));
            letra.setMinimumSize(new Dimension(40, 40));

            // Efecto hover - cambia de color en vez de mover
            letra.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    letra.setForeground(new Color(255, 255, 200));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    letra.setForeground(Color.WHITE);
                }
            });

            panelTitulo.add(letra);
            // Guardar referencia a la letra
            letrasLabels[i] = letra;
            
            // Pequeño espacio entre letras
            if (i < letras.length - 1) {
                panelTitulo.add(Box.createHorizontalStrut(5));
            }
        }

        // Añadir animación al título - Este es el código nuevo
        Timer timerAnimacion = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Animar cada letra con un pequeño retraso entre ellas
                for (int i = 0; i < letrasLabels.length; i++) {
                    final int index = i;
                    // Retrasar cada letra un poco
                    Timer letraTimer = new Timer(100 * i, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Crear animación de rebote
                            animarLetra(letrasLabels[index]);
                        }
                    });
                    letraTimer.setRepeats(false);
                    letraTimer.start();
                }
            }
        });
        timerAnimacion.start();


        // Espacio al final para centrar
        panelTitulo.add(Box.createHorizontalGlue());
        // Añadir panel de título en lugar del JLabel
        tarjeta.add(panelTitulo);

        tarjeta.add(Box.createVerticalStrut(10));

        tarjeta.add(panelBotones);
        tarjeta.add(Box.createVerticalStrut(20)); // Espacio más pequeño aquí

        // --- Agregando componentes ---
        // botonEntrar.addActionListener(vistaMenuPrincipal);
        // --- Agregando componentes ---
        // tarjeta.add(panelBotones);
        tarjeta.add(Box.createVerticalStrut(20)); // Espacio más pequeño aquí
        tarjeta.add(labelUsuario);
        tarjeta.add(Box.createVerticalStrut(5));
        tarjeta.add(campoUsuario);
        tarjeta.add(Box.createVerticalStrut(15)); // Menos espacio aquí también
        tarjeta.add(labelContrasena);
        tarjeta.add(Box.createVerticalStrut(5));
        tarjeta.add(campoContrasena);
        tarjeta.add(Box.createVerticalStrut(20)); // Un poco menos aquí
        tarjeta.add(error);
        tarjeta.add(botonEntrar);
        tarjeta.add(Box.createVerticalGlue()); // 👈 Esto empuja todo hacia arriba

        add(tarjeta); // Añadir tarjeta centrada

        setVisible(true);

    }

    public void entrar(ActionListener l) {
        botonEntrar.addActionListener(l);
    }

    public void pulsarEnter(ActionListener l) {
        campoContrasena.addActionListener(l);
    }

    public void setError(String texto) {
        error.setText(texto);
    }

    private JButton crearBotonSelector(String texto, boolean seleccionado) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                if ((texto.equals("Iniciar Sesión") && iniciarSeleccionado)
                        || (texto.equals("Registrar") && !iniciarSeleccionado)) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp = new GradientPaint(0, 0, new Color(86, 165, 243), getWidth(), 0,
                            new Color(120, 232, 243));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                }
                super.paintComponent(g);
            }
        };

        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setOpaque(false);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(140, 40));

        boton.addActionListener(e -> {
            iniciarSeleccionado = texto.equals("Iniciar Sesión");
            botonIniciar.repaint();
            botonRegistrar.repaint();
        });

        return boton;
    }

    public boolean getSeleccionado() {
        return iniciarSeleccionado;
    }

    // Clase para bordes redondeados en los JTextField
    static class RoundedBorder extends AbstractBorder {

        private int radius;
        private Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
    }

    public String getNombre() {
        return campoUsuario.getText();
    }

    public char[] getPassword() {
        return campoContrasena.getPassword();
    }


        private void animarLetra(JLabel letra) {
        // Posición original
        Point posOriginal = letra.getLocation();
        
        // Temporizador para subir y bajar
        Timer animacionTimer = new Timer(20, null);
        final int[] step = {0};
        final int totalSteps = 15;
        
        animacionTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step[0] < totalSteps) {
                    // Subir - definir una curva sinusoidal para un movimiento suave
                    int desplazamiento = (int)(10 * Math.sin(step[0] * Math.PI / totalSteps));
                    letra.setLocation(posOriginal.x, posOriginal.y - desplazamiento);
                    step[0]++;
                } else {
                    // Volver a posición original
                    letra.setLocation(posOriginal);
                    animacionTimer.stop();
                }
            }
        });
        
        animacionTimer.start();
    }

}
