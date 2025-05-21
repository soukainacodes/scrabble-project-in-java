package Presentacion.Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaFinal extends JFrame {
    
    private JLabel mensajeLabel;
    private Timer timer;
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
                
                // Fondo con gradiente
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(100, 180, 255),
                    0, getHeight(), new Color(150, 220, 255)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
       
        // Mensaje label
        mensajeLabel = new JLabel(mensaje);
        mensajeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mensajeLabel.setForeground(Color.WHITE);
        mensajeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Añadir componentes
        mainPanel.add(Box.createVerticalGlue());
        
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(mensajeLabel);
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
        
        // Configurar ventana
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Añadir sombra al borde (efecto visual)
        getRootPane().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
    }
    
    public void mostrar() {
        setVisible(true);
        timer.start();
    }
    
    // Métodos para personalizar el mensaje si es necesario
    public void setMensaje(String mensaje) {
        mensajeLabel.setText(mensaje);
    }
    
    public void setDuracion(int segundos) {
        timer.setInitialDelay(segundos * 1000);
        timer.restart();
    }
    
  
    
   
}