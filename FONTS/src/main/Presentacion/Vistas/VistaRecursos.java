package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

public class VistaRecursos extends JPanel {

    private static final int CONTENT_WIDTH = 360;
    private static final Color BG = new Color(238, 238, 238, 255);
    private static final Color FG_TITLE = new Color(40, 50, 60);
    private static final Color FG_SUB = new Color(80, 90, 100);
    private static final Color BORDER = new Color(220, 220, 220);
    private JButton botonAdd;
    private DefaultListModel<String> model;
    private JList<String> lista;

    private JButton botonEliminar;

    public VistaRecursos() {
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

        // Subtítulo
        JLabel subtitulo = new JLabel("Lista de Recursos");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(FG_SUB);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(subtitulo);

        content.add(Box.createVerticalStrut(20));

        // Lista dentro de un JScrollPane
        model = new DefaultListModel<>();

        // ... añade más recursos si quieres
        lista = new JList<>(model);
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

        // Panel de botones al final
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botones.setBackground(BG);
        botonAdd = crearBotonBlanco("Añadir Recurso");
        botones.add(botonAdd);
        botonEliminar = crearBotonBlanco("Eliminar Recurso");
        botones.add(botonEliminar);
      
       
        content.add(botones);

        // Pegamos content en el wrapper
        wrapper.add(content);
        add(wrapper, BorderLayout.CENTER);
    }

    public void addRecurso(ActionListener l) {
        botonAdd.addActionListener(l);
    }
    public void eliminarRecurso(ActionListener l) {
        botonEliminar.addActionListener(l);
    }

    public void removeLista(String s) {
        model.removeElement(s);
    }

    public void setLista(List<String> lista) {
         model.removeAllElements();
        for (String recurso : lista) {
            model.addElement(recurso);
        }
    }

    public String getSeleccionado() {
        return lista.getSelectedValue();
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

    public void addAddRecursos(ActionListener l) {
        botonAdd.addActionListener(l);
    }
}
