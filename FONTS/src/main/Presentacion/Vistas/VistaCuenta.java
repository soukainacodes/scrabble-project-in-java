package Presentacion.Vistas;

import javax.swing.*;
import java.awt.*;

public class VistaCuenta extends JPanel {

    public static final int CONTENT_WIDTH = 360;
    private static final Color BG = new Color(255, 248, 230);
    private static final Color FG = new Color(20, 40, 80);
    private static final Color BORDER = new Color(220, 220, 220);

    public VistaCuenta() {
        setLayout(new BorderLayout());
        setBackground(BG);

        // Wrapper para centrar y fijar ancho
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);

        // Panel de contenido con BoxLayout vertical
        JPanel content = new JPanel();
        content.setBackground(BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setMaximumSize(new Dimension(CONTENT_WIDTH, Integer.MAX_VALUE));

        // Añadimos hueco, título y botones
       // content.add(Box.createVerticalStrut(30));

        JLabel titulo = new JLabel("Cuenta");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(FG);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titulo);

        content.add(Box.createVerticalStrut(30));

        JButton btnCambiar = crearBotonBlanco("Cambiar Contraseña");
        JButton btnEliminar = crearBotonBlanco("Eliminar Usuario");

        content.add(btnCambiar);
        content.add(Box.createVerticalStrut(15));
        content.add(btnEliminar);

        // Pegamos el content en el wrapper centrado
        wrapper.add(content);
       add(wrapper);
    }

    private JButton crearBotonBlanco(String texto) {
        JButton boton = new JButton(texto) {

              @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0,new Color(86, 99, 243), getWidth(), 0, new Color(86, 232, 243));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };

        

        boton.setFont(new Font("Arial", Font.BOLD, 20));
       boton.setForeground(FG);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
       // boton.setBackground(Color.WHITE);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        //boton.setBorder(BorderFactory.createLineBorder(BORDER, 2));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(CONTENT_WIDTH, 50));
        return boton;
    }
}
