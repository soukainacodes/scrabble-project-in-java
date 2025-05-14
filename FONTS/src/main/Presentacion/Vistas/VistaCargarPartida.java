package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
public class VistaCargarPartida extends JPanel {

    private static final int CONTENT_WIDTH = 360;
    private static final Color BG = new Color(255, 248, 230);
    private static final Color FG_TITLE = new Color(40, 50, 60);
    private static final Color FG_SUB = new Color(80, 90, 100);
    private static final Color BORDER = new Color(220, 220, 220);
    private JButton botonCargar;

    public VistaCargarPartida() {

        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Wrapper para centrar y fijar ancho máximo
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);
        wrapper.setBackground(BG);

        // Panel de contenido vertical
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG);
        content.setMaximumSize(new Dimension(CONTENT_WIDTH, Integer.MAX_VALUE));

        // Título principal
        JLabel titulo = new JLabel("Recursos");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(FG_TITLE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titulo);
        content.add(Box.createVerticalStrut(8));

        // Lista dentro de un JScrollPane
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Castellano");
        model.addElement("Catalan");
        model.addElement("Inglés");
        model.addElement("Animación");
        // ... añade más recursos si quieres

        JList<String> lista = new JList<>(model);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Arial", Font.PLAIN, 16));
        lista.setFixedCellHeight(40);
        lista.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        scroll.setPreferredSize(new Dimension(CONTENT_WIDTH, 160));  // alto mínimo
        scroll.setMaximumSize(new Dimension(CONTENT_WIDTH, 300));    // crece hasta 300px

        content.add(scroll);

        content.add(Box.createVerticalStrut(20));

        botonCargar = crearBotonBlanco("Cargar Partida");

        content.add(botonCargar);

        wrapper.add(content);
        add(wrapper, BorderLayout.CENTER);
    }

    private JButton crearBotonBlanco(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(FG_TITLE);
        boton.setBackground(Color.WHITE);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createLineBorder(BORDER, 2));
        boton.setPreferredSize(new Dimension(160, 40));
        return boton;
    }

    public void jugarPartida(ActionListener l) {
        botonCargar.addActionListener(l);
    }

}
