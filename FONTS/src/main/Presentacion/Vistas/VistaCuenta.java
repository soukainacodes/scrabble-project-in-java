package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VistaCuenta extends JPanel {

    private JPanel positionPanel;

    public static final int CONTENT_WIDTH = 500;
    private static final Color BG = new Color(242, 226, 177); // Color crema
    private static final Color FG = new Color(20, 40, 80); 
    private static final Color LILA_OSCURO = new Color(52, 28, 87);
    private static final Color LILA_CLARO = new Color(180, 95, 220);

    private JButton btnCambiarNombre;
    private JButton btnCambiarPassword;
    private JButton btnEliminarCuenta;
    private JLabel valorNombre;
    private JLabel valorPuntos;
    private JLabel valorPosicion;
    private CircularProfileImage profileImage;
    private ActionListener profileChangeListener;

    public VistaCuenta() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add title panel first
        JPanel titlePanel = createTitlePanel();
        
        // Create main panel
        JPanel mainPanel = createMainPanel();
        
        // Add panels to layout
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(700, 450));
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        
        // Crear título estilo Scrabble usando fichas
        String[] letras = { "T", "U", " ", "C", "U", "E", "N", "T", "A" };
        Color[] colores = {
            new Color(220, 130, 95),  // Naranja rojizo
            new Color(95, 170, 220),  // Azul claro
            BG,                       // Fondo - espacio
            new Color(220, 180, 95),  // Amarillo
            new Color(150, 220, 95),  // Verde
            new Color(180, 95, 220),  // Morado/Lila
            new Color(220, 95, 160),  // Rosa
            new Color(95, 220, 190),  // Turquesa
            new Color(235, 140, 80)   // Naranja
        };
        
        JPanel fichasPanel = new JPanel();
        fichasPanel.setLayout(new BoxLayout(fichasPanel, BoxLayout.X_AXIS));
        fichasPanel.setBackground(BG);
        fichasPanel.add(Box.createHorizontalGlue());
        
        for (int i = 0; i < letras.length; i++) {
            final int idx = i;
            
            // Si es un espacio, agregar espacio en blanco
            if (letras[i].equals(" ")) {
                fichasPanel.add(Box.createHorizontalStrut(10));
                continue;
            }
            
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
            if (i < letras.length - 1 && !letras[i+1].equals(" ")) 
                fichasPanel.add(Box.createHorizontalStrut(5));
        }
        
        fichasPanel.add(Box.createHorizontalGlue());
        panel.add(fichasPanel);
        
        return panel;
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 10));
        panel.setBackground(BG);

        // Añadir un espacio superior adicional
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Profile section (image and user info)
        JPanel profileSection = createProfileSection();

        // Buttons section
        JPanel buttonsSection = createButtonsSection();

        panel.add(profileSection, BorderLayout.NORTH);
        panel.add(buttonsSection, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfileSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 5, 25));
        
        // Profile image (left side)
        profileImage = new CircularProfileImage(150); 
        profileImage.setToolTipText("Haz clic para cambiar tu foto de perfil");
        profileImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openImageChooser();
            }
    
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    
        JPanel imageContainer = new JPanel();
        imageContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 0));
        imageContainer.setBackground(BG);
        imageContainer.add(profileImage);
    
        // User info (right side)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(BG);
        infoPanel.setBorder(new EmptyBorder(0, 100, 0, 0));
    
        // Info sections
        JPanel welcomePanel = createInfoField("Bienvenido, ", "valorNombre");
        JPanel pointsPanel = createInfoField("Tu puntuación actual es: ", "valorPuntos");
        positionPanel = createInfoField("Tu posición es: ", "valorPosicion"); // Guardar referencia
    
        infoPanel.add(welcomePanel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(pointsPanel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(positionPanel);
    
        panel.add(imageContainer);
        panel.add(infoPanel);
    
        return panel;
    }
    
    private JPanel createInfoField(String labelText, String valueType) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BG);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", valueType.equals("valorNombre") ? 
                Font.BOLD : Font.PLAIN, valueType.equals("valorNombre") ? 24 : 18));
        label.setForeground(FG);
        
        JLabel value;
        
        switch(valueType) {
            case "valorNombre":
                valorNombre = new JLabel();
                valorNombre.setFont(new Font("Arial", Font.BOLD, 24));
                valorNombre.setForeground(LILA_OSCURO);
                value = valorNombre;
                break;
            case "valorPuntos":
                valorPuntos = new JLabel();
                valorPuntos.setFont(new Font("Arial", Font.BOLD, 18));
                valorPuntos.setForeground(LILA_OSCURO);
                value = valorPuntos;
                break;
            case "valorPosicion":
                valorPosicion = new JLabel();
                valorPosicion.setFont(new Font("Arial", Font.BOLD, 18));
                valorPosicion.setForeground(LILA_OSCURO);
                value = valorPosicion;
                break;
            default:
                value = new JLabel();
        }
        
        panel.add(label);
        panel.add(value);
        
        return panel;
    }
    
    
private JPanel createButtonsSection() {
    // Cambiar de GridLayout a FlowLayout para respetar los tamaños preferidos
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    panel.setBackground(BG);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

    // Crear botones con estilo consistente
    btnCambiarNombre = createStylishButton("Cambiar Nombre", LILA_CLARO);
    btnCambiarPassword = createStylishButton("Cambiar Contraseña", LILA_CLARO);
    btnEliminarCuenta = createStylishButton("Eliminar Cuenta", LILA_OSCURO);
    
    panel.add(btnCambiarNombre);
    panel.add(btnCambiarPassword);
    panel.add(btnEliminarCuenta);

    return panel;
}
    private JButton createStylishButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            private final Color hoverColor = baseColor.darker();
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Radio de las esquinas redondeadas
                int radius = 15;
                boolean isHovered = getModel().isRollover();
                Color bgColor = isHovered ? hoverColor : baseColor;
                
                // Dibujar sombra si está en hover
                if (isHovered) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(5, 5, getWidth() - 8, getHeight() - 8, radius, radius);
                }
                
                // Dibujar fondo del botón
                g2.setColor(bgColor);
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
                
                // Dibujar texto
                g2.setColor(getForeground());
                g2.setFont(getFont());
                
                FontMetrics fm = g2.getFontMetrics();
                String buttonText = getText();
                int x = (getWidth() - fm.stringWidth(buttonText)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                // Ya no necesitamos ajustar la posición para los iconos
                
                g2.drawString(buttonText, x, y);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
         // Reducir el tamaño de los botones
        button.setPreferredSize(new Dimension(180, 35)); // Reducido de 40 a 35 en altura
        button.setMinimumSize(new Dimension(180, 35));
        button.setMaximumSize(new Dimension(180, 35)); // Añadir tamaño máximo
        
        // Efecto al pasar el ratón simplificado
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
        
        return button;
    }
    
   
    private void openImageChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona una imagen de perfil");
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Imágenes", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(selectedFile);
                if (img != null) {
                    profileImage.setImage(img);
                    
                    if (profileChangeListener != null) {
                        profileChangeListener.actionPerformed(
                                new java.awt.event.ActionEvent(selectedFile, 0, "profileImageChanged"));
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al cargar la imagen: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Métodos públicos
    public void setProfileChangeListener(ActionListener listener) {
        this.profileChangeListener = listener;
    }

    public void setProfileImage(BufferedImage image) {
        profileImage.setImage(image);
    }

    // Actualiza el método setPosicion para ocultar el panel cuando sea 0
    public void setPosicion(int posicion) {
        if (posicion <= 0) {
            // Ocultar el panel de posición si el jugador no está clasificado
            if (positionPanel != null) {
                positionPanel.setVisible(false);
            }
        } else {
            // Mostrar el panel de posición con el valor correspondiente
            valorPosicion.setText(String.valueOf(posicion));
            if (positionPanel != null) {
                positionPanel.setVisible(true);
            }
        }
    }

    public void cambiarNombre(ActionListener l) {
        btnCambiarNombre.addActionListener(l);
    }

    public void cambiarPassword(ActionListener l) {
        btnCambiarPassword.addActionListener(l);
    }

    public void eliminarJugador(ActionListener l) {
        btnEliminarCuenta.addActionListener(l);
    }

    public void setNombre(String nombre) {
        valorNombre.setText(nombre + "!");
    }

    public void setPuntos(String puntos) {
        valorPuntos.setText(puntos + " pts");
    }

    // Inner class for circular profile image
    private class CircularProfileImage extends JPanel {
        private BufferedImage image;
        private final int size;
        private BufferedImage defaultImage;

        public CircularProfileImage(int size) {
            this.size = size;
            setPreferredSize(new Dimension(size, size));
            setOpaque(false);
            loadDefaultImage();
        }

        private void loadDefaultImage() {
            try {
                // Try to load default profile image
                File defaultFile = new File("FONTS/src/main/Recursos/Imagenes/default_profile.png");
                if (defaultFile.exists()) {
                    defaultImage = ImageIO.read(defaultFile);
                } else {
                    // Create a default image with user silhouette if file doesn't exist
                    defaultImage = createDefaultUserImage();
                }
            } catch (IOException e) {
                defaultImage = createDefaultUserImage();
            }

            image = defaultImage;
        }

        private BufferedImage createDefaultUserImage() {
            BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();

            // Background circle with gradient
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(180, 180, 180),
                    size, size, new Color(220, 220, 220));
            g2d.setPaint(gradient);
            g2d.fillOval(0, 0, size - 1, size - 1);

            // User silhouette (improved)
            g2d.setColor(new Color(240, 240, 240));
            // Head
            g2d.fillOval(size / 4, size / 6, size / 2, size / 2);
            // Body
            g2d.fillOval(size / 4 - size / 8, size / 2, size / 2 + size / 4, size / 2);

            // Add subtle shadow
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(2, 2, size - 5, size - 5);

            g2d.dispose();
            return img;
        }

        public void setImage(BufferedImage newImage) {
            if (newImage != null) {
                // Resize image while maintaining aspect ratio
                double scale = (double) size / Math.max(newImage.getWidth(), newImage.getHeight());
                int width = (int) (newImage.getWidth() * scale);
                int height = (int) (newImage.getHeight() * scale);

                BufferedImage resized = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = resized.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                // Center the image
                int x = (size - width) / 2;
                int y = (size - height) / 2;

                g.drawImage(newImage, x, y, width, height, null);
                g.dispose();

                this.image = resized;
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (image != null) {
                Graphics2D g2 = (Graphics2D) g.create();

                // Enable antialiasing for smoother edges
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow under the image for 3D effect
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillOval(3, 3, size - 2, size - 2);

                // Create circular clipping region
                Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
                g2.setClip(circle);

                // Draw the image
                g2.drawImage(image, 0, 0, size, size, null);

                // Draw border with gradient for 3D effect
                g2.setClip(null);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(new Color(180, 180, 180));
                g2.draw(circle);

                g2.dispose();
            }
        }
    }
}