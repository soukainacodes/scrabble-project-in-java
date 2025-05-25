package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class VistaFinPartida extends JFrame {
    
    private JLabel mensajeLabel;
    private JLabel titleLabel;
    private Timer timer;
    private final int DURACION_SEGUNDOS = 15;
    
    public VistaFinPartida(String mensaje) {
        setTitle("¡Fin de Partida!");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true); 
        setLayout(new BorderLayout());
        
        // Panel principal con fondo redondeado
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente rosa
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(220, 70, 170),     // Rosa más oscuro
                    0, getHeight(), new Color(250, 130, 190)  // Rosa más claro
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                // Añadir un efecto de brillo en la parte superior
                GradientPaint highlight = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 100),
                    0, getHeight()/2, new Color(255, 255, 255, 0)
                );
                g2d.setPaint(highlight);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 30, 30);
                
                g2d.dispose();
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Título decorativo
        titleLabel = new JLabel("¡FIN DE PARTIDA!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Añadir un separador decorativo
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(250, 2));
        separator.setForeground(new Color(255, 255, 255, 150));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Mejorar el mensaje
        mensajeLabel = new JLabel(mensaje);
        mensajeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mensajeLabel.setForeground(Color.WHITE);
        mensajeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Crear un icono decorativo (trofeo o similar)
        JLabel iconLabel = new JLabel(new ImageIcon(createTrophyIcon()));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Añadir componentes
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(separator);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(iconLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(mensajeLabel);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(Box.createVerticalGlue());
        
        add(mainPanel);
        
        // Crear timer para cerrar automáticamente después de exactamente 15 segundos
        timer = new Timer(DURACION_SEGUNDOS * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.setRepeats(false);
        
        // Configurar ventana
        setSize(600, 350);
        setLocationRelativeTo(null);
        
        // Añadir sombra al borde (efecto visual mejorado)
        getRootPane().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 80), 2),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
    }
    
    // Método para crear un icono de trofeo simple con Java2D
    private Image createTrophyIcon() {
        int size = 64;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar un trofeo simple
        g2d.setColor(new Color(255, 215, 0)); // Gold color
        
        // Copa del trofeo
        g2d.fillOval(size/4, size/12, size/2, size/4);
        g2d.fillRect(size/4, size/8, size/2, size/3);
        
        // Base
        g2d.fillRect(size/3, size/2, size/3, size/12);
        g2d.fillRect(size/4, size/2 + size/12, size/2, size/8);
        
        // Brillo
        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.fillOval(size/3, size/10, size/6, size/6);
        
        g2d.dispose();
        return image;
    }
    
    public void mostrar() {
        setVisible(true);
        timer.restart();
    }
    
    public void setMensaje(String mensaje) {
        mensajeLabel.setText(mensaje);
    }
    
    public void setDuracion(int segundos) {
        timer.setInitialDelay(segundos * 1000);
        timer.restart();
    }
}