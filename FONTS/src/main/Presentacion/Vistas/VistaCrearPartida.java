package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

public class VistaCrearPartida extends JPanel {

    private static final int CONTENT_WIDTH = 360;
    private static final Color BG = new Color(238, 238, 238);
    private static final Color FG = new Color(20, 40, 80);
    private static final Color BORDER = new Color(220, 220, 220);
    private JButton botonCrearPartida;
    private JButton botonIniciarSesion;
    private String[] idiomas = new String[0];
    private JComboBox<String> comboJugadores;
    private JComboBox<String> comboIdiomas;

    private JTextField id;

    public VistaCrearPartida() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Wrapper para centrar y fijar ancho
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);

        // Panel de contenido en columna
        JPanel content = new JPanel();
        content.setBackground(BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setMaximumSize(new Dimension(CONTENT_WIDTH, Integer.MAX_VALUE));
        JLabel lblID = new JLabel("ID");
        lblID.setFont(new Font("Arial", Font.BOLD, 16));
        lblID.setForeground(FG);
        lblID.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblID);

        content.add(Box.createVerticalStrut(8));

        id = new JTextField();
        id.setFont(new Font("Arial", Font.PLAIN, 14));
        id.setMaximumSize(new Dimension(CONTENT_WIDTH, thirtySix()));
        id.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(id);


        content.add(Box.createVerticalStrut(35));


        // Desplegable de número de jugadores
        JLabel lblJugadores = new JLabel("Número de jugadores:");
        lblJugadores.setFont(new Font("Arial", Font.BOLD, 16));
        lblJugadores.setForeground(FG);
        lblJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblJugadores);

        content.add(Box.createVerticalStrut(8));

        String[] opcionesJug = {"1 Jugador", "2 Jugadores"};
        comboJugadores = new JComboBox<>(opcionesJug);
        comboJugadores.setFont(new Font("Arial", Font.PLAIN, 14));
        comboJugadores.setMaximumSize(new Dimension(CONTENT_WIDTH, thirtySix()));
        comboJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(comboJugadores);

        content.add(Box.createVerticalStrut(15));

        // Botón Iniciar Sesión (oculto inicialmente)
        botonIniciarSesion = new JButton("Añadir Segundo Jugador");
        styleButton(botonIniciarSesion);
        botonIniciarSesion.setVisible(false);
        content.add(botonIniciarSesion);

        content.add(Box.createVerticalStrut(20));

        // Desplegable de idioma
        JLabel lblIdioma = new JLabel("Idioma:");
        lblIdioma.setFont(new Font("Arial", Font.BOLD, 16));
        lblIdioma.setForeground(FG);
        lblIdioma.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblIdioma);

        content.add(Box.createVerticalStrut(8));

        comboIdiomas = new JComboBox<>();
        comboIdiomas.setFont(new Font("Arial", Font.PLAIN, 14));
        comboIdiomas.setMaximumSize(new Dimension(CONTENT_WIDTH, thirtySix()));
        comboIdiomas.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(comboIdiomas);

        content.add(Box.createVerticalStrut(30));

        // Botón Crear
        botonCrearPartida = new JButton("Crear");
        stylePrimaryButton(botonCrearPartida);
        content.add(botonCrearPartida);

        // Listener para mostrar/ocultar el botón de iniciar sesión
        comboJugadores.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String sel = (String) e.getItem();
                    botonIniciarSesion.setVisible("2 Jugadores".equals(sel));
                    revalidate();
                }
            }
        });

        wrapper.add(content);
        add(wrapper, BorderLayout.CENTER);

    }



    public void setIdiomas(String[] idiomas) {
        this.idiomas = idiomas;
        
        comboIdiomas.setModel(new DefaultComboBoxModel<>(idiomas));
        comboIdiomas.revalidate();
        comboIdiomas.repaint();
    }

    public int getModo() {
        return comboJugadores.getSelectedIndex();
    }

    public String getIdioma() {
        return (String) comboIdiomas.getSelectedItem();
    }

    public String getID(){
        return id.getText();
    }

    /**
     * Da estilo uniforme a botones secundarios
     */
    private void styleButton(JButton b) {
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setForeground(FG);
        b.setBackground(Color.WHITE);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createLineBorder(BORDER, 2));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(160, 36));
    }

    /**
     * Da estilo uniforme al botón principal
     */
    private void stylePrimaryButton(JButton b) {
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(160, 36));
        b.setPreferredSize(new Dimension(160, 36));
        b.setBackground(new Color(86, 200, 200));
        b.setOpaque(true);
    }

    /**
     * Helper para altura consistente
     */
    private int thirtySix() {
        return 36;
    }

    public void jugarPartida(ActionListener l) {
        botonCrearPartida.addActionListener(l);
    }

    public void addSegundoJugador(ActionListener l) {
        botonIniciarSesion.addActionListener(l);
    }
}
