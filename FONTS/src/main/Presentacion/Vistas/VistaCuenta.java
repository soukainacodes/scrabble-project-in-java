package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VistaCuenta extends JPanel {

    public static final int CONTENT_WIDTH = 360;
    private static final Color BG = new Color(238, 238, 238, 255);
    private static final Color FG = new Color(20, 40, 80);
    private JButton btnCambiar;
    private JButton btnEliminar;
    private JButton btnEditarNombre;
    private JLabel valorNombre;
    private JLabel valorPuntos;
    public VistaCuenta() {
        setLayout(new BorderLayout());
        setBackground(BG);
        // Margen general alrededor
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Wrapper con FlowLayout para alinear a la izquierda
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setBackground(BG);

        // Panel vertical donde apilamos las filas
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG);
        // Limitar el ancho, permitir altura variable
        content.setMaximumSize(new Dimension(CONTENT_WIDTH, Integer.MAX_VALUE));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ——— Fila de Nombre ———
        JPanel rowNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rowNombre.setBackground(BG);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
        lblNombre.setForeground(FG);

        valorNombre = new JLabel();
        valorNombre.setFont(new Font("Arial", Font.PLAIN, 20));
        valorNombre.setForeground(FG);

        // ——— Botón de editar nombre ———
        // Carga tu icono de lápiz (pon el PNG/ICO en recursos y ajusta la ruta)
        btnEditarNombre = crearBotonBlanco("Editar");

        // Montamos la fila
        rowNombre.add(lblNombre);
        rowNombre.add(valorNombre);
        rowNombre.add(btnEditarNombre);          // <-- añadimos el botón aquí
        content.add(rowNombre);

        content.add(Box.createVerticalStrut(8));

        // ——— Fila de Puntos ———
        JPanel rowPuntos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rowPuntos.setBackground(BG);
        JLabel lblPuntos = new JLabel("Puntos:");
        lblPuntos.setFont(new Font("Arial", Font.BOLD, 20));
        valorPuntos = new JLabel();
        valorPuntos.setFont(new Font("Arial", Font.PLAIN, 20));
        valorPuntos.setForeground(FG);
        rowPuntos.add(lblPuntos);
        rowPuntos.add(valorPuntos);
        content.add(rowPuntos);

        content.add(Box.createVerticalStrut(20));

        // ——— Botones ———
        btnCambiar = crearBotonBlanco("Cambiar Contraseña");
        btnEliminar = crearBotonBlanco("Eliminar Jugador");

        content.add(btnCambiar);
        content.add(Box.createVerticalStrut(10));
        content.add(btnEliminar);

        // Montamos todo
        wrapper.add(content);
        // Lo ponemos en NORTH para que quede arriba
        add(wrapper, BorderLayout.NORTH);
    }

    public void cambiarPassword(ActionListener l) {
        btnCambiar.addActionListener(l);
    }

    public void eliminarJugador(ActionListener l) {
        btnEliminar.addActionListener(l);
    }

    public void cambiarNombre(ActionListener l) {
        btnEditarNombre.addActionListener(l);
    }

    public void setNombre(String nombre) {
        valorNombre.setText(nombre);
    }

    public void setPuntos(String puntos){
        valorPuntos.setText(puntos);
    }

    private JButton crearBotonBlanco(String texto) {
        JButton boton = new JButton(texto);

        boton.setForeground(new Color(20, 40, 80));
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(255, 248, 230));
        boton.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        boton.setMaximumSize(new Dimension(250, 50));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        return boton;
    }
}
