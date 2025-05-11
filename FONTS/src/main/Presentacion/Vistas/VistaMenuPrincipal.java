package Presentacion.Vistas;

import java.awt.*;
import javax.swing.*;

public class VistaMenuPrincipal extends JPanel {

    public static final String BIENVENIDA = "BIENVENIDA";
    // Puedes añadir más keys aquí para otras vistas:
    public static final String OTRA = "OTRA";

    private final JPanel panelMenuIzquierdo;
    private final JButton botonToggleMenu;
    private final JPanel cards;
    private boolean menuVisible = true;

    public VistaMenuPrincipal() {
        setBackground(new Color(255, 248, 230));
        setLayout(new BorderLayout());

        // --- Panel lateral completo (WEST) ---
        panelMenuIzquierdo = new JPanel(new BorderLayout());
        panelMenuIzquierdo.setBackground(getBackground());
        panelMenuIzquierdo.setPreferredSize(new Dimension(200, 0));
        panelMenuIzquierdo.setBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(220, 220, 220))
        );

        // 1) Botón de toggle en la cabecera del lateral
        botonToggleMenu = new JButton("✖");
        botonToggleMenu.setFont(new Font("Arial", Font.BOLD, 24));
        botonToggleMenu.setFocusPainted(false);
        botonToggleMenu.setBorderPainted(false);
        botonToggleMenu.setContentAreaFilled(false);
        botonToggleMenu.setBackground(getBackground());
        botonToggleMenu.addActionListener(e -> toggleMenu());

        JPanel cabeceraMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cabeceraMenu.setBackground(getBackground());
        cabeceraMenu.add(botonToggleMenu);

        panelMenuIzquierdo.add(cabeceraMenu, BorderLayout.NORTH);
        panelMenuIzquierdo.add(new VistaMenuLateral(), BorderLayout.CENTER);
        
        add(panelMenuIzquierdo, BorderLayout.WEST);

        // --- Contenedor de pantallas (CardLayout) ---
        cards = new JPanel(new CardLayout());
        // Añadimos la pantalla de bienvenida
        cards.add(new VistaPantallaPrincipal(), BIENVENIDA);
        // Ejemplo de otra pantalla
        cards.add(new JPanel(){{
            setBackground(getBackground());
            add(new JLabel("Pantalla Secundaria"));
        }}, OTRA);

        // --- Wrapper para centrar y fijar ancho ---
        JPanel centroWrapper = new JPanel(new GridBagLayout());
        centroWrapper.setBackground(getBackground());
        // Limitamos el ancho máximo de las tarjetas
        cards.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        centroWrapper.add(cards, gbc);

        add(centroWrapper, BorderLayout.CENTER);
    }

    private void toggleMenu() {
        if (menuVisible) {
            remove(panelMenuIzquierdo);
            botonToggleMenu.setText("☰");
        } else {
            add(panelMenuIzquierdo, BorderLayout.WEST);
            botonToggleMenu.setText("✖");
        }
        menuVisible = !menuVisible;
        revalidate();
        repaint();
    }

    /** Muestra la tarjeta cuyo identificador sea `clave`. */
    public void muestraCard(String clave) {
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, clave);
    }
}
