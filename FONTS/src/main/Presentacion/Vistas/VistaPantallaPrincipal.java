package Presentacion.Vistas;
import java.awt.*;
import javax.swing.*;

public class VistaPantallaPrincipal extends JPanel {

    public VistaPantallaPrincipal() {
        setBackground(new Color(255, 248, 230));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalStrut(30));

        JLabel titulo = new JLabel("Bienvenido [Usuario]");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(20, 40, 80));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);

        add(Box.createVerticalStrut(10));

        JLabel puntuacion = new JLabel("Tu puntuación máxima es X");
        puntuacion.setFont(new Font("Arial", Font.PLAIN, 18));
        puntuacion.setForeground(new Color(60, 80, 100));
        puntuacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(puntuacion);

        add(Box.createVerticalStrut(5));

        JLabel ranking = new JLabel("Estás en la posición X en el Ranking");
       ranking.setFont(new Font("Arial", Font.PLAIN, 18));
        ranking.setForeground(new Color(60, 80, 100));
        ranking.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(ranking);

        add(Box.createVerticalStrut(30));

        add(crearBotonDegradado("Nueva Partida"));
        add(Box.createVerticalStrut(15));
        add(crearBotonBlanco("Última Partida"));
        add(Box.createVerticalStrut(15));
        add(crearBotonBlanco("Cargar Partida"));
    }

    private JButton crearBotonDegradado(String texto) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(86, 99, 243), getWidth(), 0, new Color(86, 232, 243));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);
      //  b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setMaximumSize(new Dimension(250, 50));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    private JButton crearBotonBlanco(String texto) {
        JButton b = new JButton(texto);
        b.setForeground(new Color(20, 40, 80));
       // b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setBackground(new Color(255, 248, 230));
        b.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        b.setMaximumSize(new Dimension(250, 50));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }
}
