package Presentacion.Vistas;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Vista gráfica para mostrar y gestionar la cuenta del usuario.
 *  - Fotografía de perfil (clic para cambiarla)
 *  - Nombre, puntuación y posición en la clasificación
 *  - Botones para modificar datos o eliminar la cuenta
 * 
 * Todos los estilos (colores, fuentes…) se definen en constantes
 * para facilitar futuros cambios.
 */
public class VistaCuenta extends JPanel {

    /* === Estilos de la vista ===================================================== */
    private static final int CONTENT_WIDTH = 450;
    private static final Color BG            = new Color(242, 226, 177);                // Crema
    private static final Color FG            = new Color(20,   40,  80);                // Texto oscuro
    private static final Color LILA_OSCURO   = new Color(52,   28,  87);
    private static final Color LILA_CLARO    = new Color(180,  95, 220);

    /* === Componentes ============================================================= */
    private final JLabel valorNombre   = new JLabel();
    private final JLabel valorPuntos   = new JLabel();
    private final JLabel valorPosicion = new JLabel();

    private final JButton btnCambiarNombre;
    private final JButton btnCambiarPassword;
    private final JButton btnEliminarCuenta;

    private CircularProfileImage profileImage;
    private ActionListener profileChangeListener;

    /* === Constructor ============================================================= */
    public VistaCuenta() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(new EmptyBorder(5, 20, 5, 20));
        setPreferredSize(new Dimension(700, 520));

        // Paneles principales
        add(crearPanelTitulo(),  BorderLayout.NORTH);
        add(crearPanelContenido(), BorderLayout.CENTER);

        /* Valores iniciales de ejemplo */
        setNombre("Jugador");
        setPuntos("0");
        setPosicion(0);

        // Botones
        btnCambiarNombre   = crearBoton("Cambiar Nombre",   LILA_CLARO);
        btnCambiarPassword = crearBoton("Cambiar Contraseña", LILA_CLARO);
        btnEliminarCuenta  = crearBoton("Eliminar Cuenta",   LILA_OSCURO);

        btnCambiarNombre.setPreferredSize(new Dimension(210, 40));   // Aumentado de 180x35 a 210x40
        btnCambiarPassword.setPreferredSize(new Dimension(210, 40)); // Aumentado de 180x35 a 210x40



        // Insertamos los botones en su contenedor
        JPanel botones = crearPanelBotones();
        
        // Primera fila: botones normales
        JPanel filaSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        filaSuperior.setOpaque(false);
        filaSuperior.add(btnCambiarNombre);
        filaSuperior.add(btnCambiarPassword);
        
        // Segunda fila: botón de eliminar cuenta
        JPanel filaInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filaInferior.setOpaque(false);
        filaInferior.add(btnEliminarCuenta);
        
        // Añadir al panel principal de botones
        botones.add(filaSuperior);
        botones.add(Box.createVerticalStrut(15)); // Más espacio entre filas
        botones.add(filaInferior);

        ((JPanel) getComponent(1)).add(botones, BorderLayout.SOUTH); // al content panel
    }

    /* === Panel título ============================================================ */
    private JPanel crearPanelTitulo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(15, 0, 5, 0));

        String[] letras = { "T", "U", " ", "C", "U", "E", "N", "T", "A" };
        Color[] colores = {
                new Color(220, 130,  95), new Color( 95, 170, 220), BG,
                new Color(220, 180,  95), new Color(150, 220,  95), new Color(180,  95, 220),
                new Color(220,  95, 160), new Color( 95, 220, 190), new Color(235, 140,  80)
        };

        JPanel fichas = new JPanel();
        fichas.setBackground(BG);
        fichas.setLayout(new BoxLayout(fichas, BoxLayout.X_AXIS));
        fichas.add(Box.createHorizontalGlue());

        for (int i = 0; i < letras.length; i++) {
            if (" ".equals(letras[i])) {
                fichas.add(Box.createHorizontalStrut(10));
                continue;
            }
            JLabel l = crearFichaTitulo(letras[i], colores[i]);
            fichas.add(l);
            if (i < letras.length - 1 && !" ".equals(letras[i + 1]))
                fichas.add(Box.createHorizontalStrut(5));
        }
        fichas.add(Box.createHorizontalGlue());
        p.add(fichas);
        return p;
    }

    private JLabel crearFichaTitulo(String texto, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 3, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial Black", Font.BOLD, 28));  // TAMAÑO AUMENTADO
        l.setPreferredSize(new Dimension(40, 40));          // TAMAÑO AUMENTADO
        l.addMouseListener(new HoverEfectoTexto(l));
        return l;
    }

    /* === Panel contenido ========================================================= */
    private JPanel crearPanelContenido() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);

        /* ==== Panel perfil (foto + datos) ==== */
        JPanel perfil = new JPanel(new BorderLayout(20, 0));
        perfil.setOpaque(false);

        // Foto circular - TAMAÑO AUMENTADO
        profileImage = new CircularProfileImage(140);  // Aumentado de 120 a 140
        profileImage.setToolTipText("Haz clic para cambiar tu foto de perfil");
        profileImage.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { elegirImagen(); }
            @Override public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
            @Override public void mouseExited (MouseEvent e) { setCursor(Cursor.getDefaultCursor()); }
        });
        JPanel fotoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fotoWrapper.setOpaque(false);
        fotoWrapper.add(profileImage);
        perfil.add(fotoWrapper, BorderLayout.WEST);

        // Datos de usuario
        JPanel datos = new JPanel();
        datos.setOpaque(false);
        datos.setLayout(new BoxLayout(datos, BoxLayout.Y_AXIS));

        // TAMAÑOS DE LETRA AUMENTADOS
        datos.add(crearFilaInfo("Bienvenido, ", valorNombre, 26));     // 22 → 26
        datos.add(Box.createVerticalStrut(10));                        // 4 → 10 (más espacio)
        datos.add(crearFilaInfo("Puntuación máx.: ", valorPuntos, 20)); // 18 → 20
        datos.add(Box.createVerticalStrut(10));                        // 4 → 10 (más espacio)
        datos.add(crearFilaInfo("Posición: ", valorPosicion, 20));     // 18 → 20

        perfil.add(datos, BorderLayout.CENTER);

        /* ==== Panel redondeado con sombra mejorado ==== */
        JPanel marco = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra externa mejorada
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 22, 22);
                
                // Borde más definido
                g2.setColor(new Color(220, 200, 150));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Gradiente interior mejorado
                GradientPaint gp = new GradientPaint(
                    0, 0, BG.brighter(), 
                    0, getHeight(), BG
                );
                g2.setPaint(gp);
                g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 18, 18);
                g2.dispose();
            }
        };
        marco.setOpaque(false);
        marco.setBorder(new EmptyBorder(30, 30, 30, 30)); // ESPACIO AUMENTADO 20 → 30
        marco.add(perfil);

        content.add(marco, BorderLayout.CENTER);
        return content;
    }

    private JPanel crearFilaInfo(String etiqueta, JLabel valor, int fontSize) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 8)); // 2 → 8 (más espacio vertical)
        fila.setOpaque(false);
        JLabel l = new JLabel(etiqueta);
        l.setForeground(FG);
        l.setFont(new Font("Arial", Font.PLAIN, fontSize));
        valor.setForeground(LILA_OSCURO);
        valor.setFont(new Font("Arial", Font.BOLD, fontSize));
        fila.add(l);
        fila.add(valor);
        return fila;
    }

    /* === Panel botones =========================================================== */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Añadir margen vertical
        return panel;
    }

    private JButton crearBoton(String texto, Color base) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int radius = 15; // 14 → 15 (bordes más redondeados)
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
        b.setPreferredSize(new Dimension(180, 35)); // 34 → 35 (botones ligeramente más altos)
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

    /* === Getters / setters públicos ============================================= */
    public void setProfileChangeListener(ActionListener l) { this.profileChangeListener = l; }
    public void cambiarNombre    (ActionListener l) { btnCambiarNombre.addActionListener(l); }
    public void cambiarPassword  (ActionListener l) { btnCambiarPassword.addActionListener(l); }
    public void eliminarJugador  (ActionListener l) { btnEliminarCuenta.addActionListener(l); }

    public void setNombre(String nombre)   { 
        valorNombre.setText(nombre + "!"); 
        System.out.println("Nombre establecido: " + nombre);
    }
    
    public void setPuntos(String puntos)   { 
        valorPuntos.setText(puntos + " pts"); 
        System.out.println("Puntos establecidos: " + puntos);
        valorPuntos.setVisible(true);
        revalidate();
        repaint();
    }
    
    public void setPosicion(int pos) { 
        valorPosicion.setText(pos <= 0 ? "No clasificado" : String.valueOf(pos)); 
        System.out.println("Posición establecida: " + pos);
        valorPosicion.setVisible(true);
        revalidate();
        repaint();
    }
    
    public void setProfileImage(BufferedImage img) { profileImage.setImage(img); }

    /* === Selección de imagen de perfil ========================================== */
    private void elegirImagen() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Selecciona una imagen de perfil");
        fc.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage img = ImageIO.read(fc.getSelectedFile());
                if (img != null) {
                    setProfileImage(img);
                    if (profileChangeListener != null) {
                        profileChangeListener.actionPerformed(new ActionEvent(fc.getSelectedFile(), 0, "profileImageChanged"));
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /* === Sub‑clases internas ===================================================== */
    // Las clases internas se mantienen sin cambios
    /* === Sub‑clases internas ===================================================== */
    private static class HoverEfectoTexto extends MouseAdapter {
        private final JLabel label;
        HoverEfectoTexto(JLabel l) { this.label = l; }
        @Override public void mouseEntered(MouseEvent e) { label.setForeground(new Color(255, 255, 200)); }
        @Override public void mouseExited (MouseEvent e) { label.setForeground(Color.WHITE); }
    }

    /**
     * Imagen circular con sombra y borde.
     */
    private static class CircularProfileImage extends JPanel {
        private final int size;
        private BufferedImage image;
        private BufferedImage defaultImage;

        CircularProfileImage(int size) {
            this.size = size;
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
            g2.fillOval(3, 3, size - 2, size - 2); // sombra
            Ellipse2D.Double clip = new Ellipse2D.Double(0, 0, size, size);
            g2.setClip(clip);
            g2.drawImage(image, 0, 0, size, size, null);
            g2.setClip(null);
            g2.setColor(new Color(180, 180, 180));
            g2.setStroke(new BasicStroke(2));
            g2.draw(clip);
            g2.dispose();
        }
    }
}
