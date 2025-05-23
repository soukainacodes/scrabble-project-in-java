package Presentacion.Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class VistaFinal extends JFrame {
    
    private JLabel mensajeLabel;
    private JLabel titleLabel;
    private JProgressBar progressBar;
    private Timer timer;
    private Timer progressTimer;
    private final int DURACION_SEGUNDOS = 3;
    
    public VistaFinal(String mensaje) {
        setTitle("¡Fin de Partida!");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true); // Remove window decorations for a cleaner look
        setLayout(new BorderLayout());
        
        // Panel principal con fondo redondeado
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente mejorado
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(70, 130, 220),
                    0, getHeight(), new Color(130, 190, 250)
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
        mensajeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mensajeLabel.setForeground(Color.WHITE);
        mensajeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Crear un icono decorativo (trofeo o similar)
        JLabel iconLabel = new JLabel(new ImageIcon(createTrophyIcon()));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Barra de progreso para mostrar el tiempo restante
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(new Color(255, 255, 255, 180));
        progressBar.setBackground(new Color(70, 130, 180, 80));
        progressBar.setBorderPainted(false);
        progressBar.setMaximumSize(new Dimension(200, 4));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
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
        mainPanel.add(progressBar);
        mainPanel.add(Box.createVerticalGlue());
        
        add(mainPanel);
        
        // Crear timer para cerrar automáticamente
        timer = new Timer(DURACION_SEGUNDOS * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.setRepeats(false);
        
        // Timer para actualizar la barra de progreso
        progressTimer = new Timer(30, new ActionListener() {
            private long startTime;
            private long duration = DURACION_SEGUNDOS * 1000;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                int value = 100 - (int)(elapsed * 100 / duration);
                if (value <= 0) {
                    progressBar.setValue(0);
                    progressTimer.stop();
                } else {
                    progressBar.setValue(value);
                }
            }
            
            public void reset() {
                startTime = System.currentTimeMillis();
            }
        });
        
        // Configurar ventana
        setSize(450, 350);
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
        // Iniciar ambos timers
        timer.start();
        ((ActionListener)progressTimer.getActionListeners()[0]).actionPerformed(null); // reset
        progressTimer.start();
    }
    
    public void setMensaje(String mensaje) {
        mensajeLabel.setText(mensaje);
    }
    
    public void setDuracion(int segundos) {
        timer.setInitialDelay(segundos * 1000);
        timer.restart();
    }
}