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

    public static final int CONTENT_WIDTH = 500;
    private static final Color BG = new Color(238, 238, 238, 255);
    private static final Color FG = new Color(20, 40, 80);
    private static final Color ACCENT = new Color(34, 83, 120);
    private static final Color CARD_BG = new Color(250, 250, 250);
    private static final Color HOVER_COLOR = new Color(240, 240, 240);
    
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
        
        // Create main panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 30));
        panel.setBackground(BG);
        
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
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Profile image (left side)
        profileImage = new CircularProfileImage(150); // Larger size
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
        
        JPanel imageContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imageContainer.setBackground(CARD_BG);
        imageContainer.add(profileImage);
        
        // User info (right side)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        // Welcome text with name
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setBackground(CARD_BG);
        
        JLabel welcomeLabel = new JLabel("Bienvenido, ");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(FG);
        
        valorNombre = new JLabel();
        valorNombre.setFont(new Font("Arial", Font.BOLD, 24));
        valorNombre.setForeground(ACCENT);
        
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(valorNombre);
        
        // Points info
        JPanel pointsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pointsPanel.setBackground(CARD_BG);
        
        JLabel pointsLabel = new JLabel("Tu puntuación actual es: ");
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        pointsLabel.setForeground(FG);
        
        valorPuntos = new JLabel();
        valorPuntos.setFont(new Font("Arial", Font.BOLD, 18));
        valorPuntos.setForeground(ACCENT);
        
        pointsPanel.add(pointsLabel);
        pointsPanel.add(valorPuntos);
        
        // Position info
        JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        positionPanel.setBackground(CARD_BG);
        
        JLabel positionLabel = new JLabel("Tu posición es: ");
        positionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        positionLabel.setForeground(FG);
        
        valorPosicion = new JLabel();
        valorPosicion.setFont(new Font("Arial", Font.BOLD, 18));
        valorPosicion.setForeground(ACCENT);
        
        positionPanel.add(positionLabel);
        positionPanel.add(valorPosicion);
        
        // Add components to info panel
        infoPanel.add(welcomePanel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(pointsPanel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(positionPanel);
        
        // Add both sections to main panel
        panel.add(imageContainer);
        panel.add(infoPanel);
        
        return panel;
    }
    
    private JPanel createButtonsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 15, 0)); // 1 row, 3 columns with gap
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Create stylish buttons with icons and effects
        btnCambiarNombre = createStylishButton("Cambiar Nombre", 
                "FONTS/src/main/Recursos/Imagenes/edit_icon.png", 
                new Color(52, 152, 219)); // Blue
        
        btnCambiarPassword = createStylishButton("Cambiar Contraseña", 
                "FONTS/src/main/Recursos/Imagenes/key_icon.png", 
                new Color(46, 204, 113)); // Green
        
        btnEliminarCuenta = createStylishButton("Eliminar Cuenta", 
                "FONTS/src/main/Recursos/Imagenes/delete_icon.png", 
                new Color(231, 76, 60)); // Red
        
        panel.add(btnCambiarNombre);
        panel.add(btnCambiarPassword);
        panel.add(btnEliminarCuenta);
        
        return panel;
    }
    
    private JButton createStylishButton(String text, String iconPath, Color accentColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(accentColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
        // Try to load icon
        try {
            ImageIcon originalIcon = new ImageIcon(iconPath);
            Image img = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(img);
            button.setIcon(icon);
            button.setIconTextGap(10);
            button.setHorizontalTextPosition(JButton.RIGHT);
            button.setVerticalTextPosition(JButton.CENTER);
        } catch (Exception e) {
            // Continue without icon
        }
        
        // Create shadow border
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 40), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        // Add 3D effect and hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(lighten(accentColor, 0.1f));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 60), 1),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(accentColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 40), 1),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(darken(accentColor, 0.1f));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(lighten(accentColor, 0.1f));
            }
        });
        
        return button;
    }
    
    // Helper methods for color manipulation
    private Color lighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() * (1 + factor)));
        int g = Math.min(255, (int)(color.getGreen() * (1 + factor)));
        int b = Math.min(255, (int)(color.getBlue() * (1 + factor)));
        return new Color(r, g, b);
    }
    
    private Color darken(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
    
    private void openImageChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona una imagen de perfil");
        
        // Apply filter to only show image files
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
                    
                    // Notify listener about the image change
                    if (profileChangeListener != null) {
                        profileChangeListener.actionPerformed(
                            new java.awt.event.ActionEvent(selectedFile, 0, "profileImageChanged")
                        );
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar la imagen: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    public void setProfileChangeListener(ActionListener listener) {
        this.profileChangeListener = listener;
    }
    
    public void setProfileImage(BufferedImage image) {
        profileImage.setImage(image);
    }
    
    public void setPosicion(int posicion) {
        valorPosicion.setText(String.valueOf(posicion));
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
        private int size;
        private BufferedImage defaultImage;
        
        public CircularProfileImage(int size) {
            this.size = size;
            setPreferredSize(new Dimension(size, size));
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
            g2d.fillOval(size/4, size/6, size/2, size/2);
            // Body
            g2d.fillOval(size/4 - size/8, size/2, size/2 + size/4, size/2);
            
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