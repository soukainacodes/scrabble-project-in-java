package Presentacion.Vistas;

import java.awt.*;
import javax.swing.*;
public class VistaMenuPrincipal extends JPanel {

    public static final String BIENVENIDA = "BIENVENIDA";
    public static final String OTRA = "OTRA";
    public static final String RECURSOS = "RECURSOS";
    private final VistaMenuLateral menuLateral;
   
    private final JPanel panelMenuIzquierdo;
    private final JButton botonToggleMenu;
    private final JPanel cards;
    private boolean menuVisible = true;

    public VistaMenuPrincipal() {
        setBackground(new Color(255, 248, 230));
        setLayout(new BorderLayout());

        // --- BOTÓN TOGGLE siempre visible en la cabecera ---
        botonToggleMenu = new JButton("✖");
        botonToggleMenu.setFont(new Font("Arial", Font.BOLD, 24));
        botonToggleMenu.setFocusPainted(false);
        botonToggleMenu.setBorderPainted(false);
        botonToggleMenu.setContentAreaFilled(false);
        botonToggleMenu.setBackground(getBackground());
        botonToggleMenu.addActionListener(e -> toggleMenu());

        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCabecera.setBackground(getBackground());
        panelCabecera.add(botonToggleMenu);
        add(panelCabecera, BorderLayout.NORTH);

        menuLateral = new VistaMenuLateral();

        // --- Panel lateral completo (WEST) ---
        panelMenuIzquierdo = new JPanel(new BorderLayout());
        panelMenuIzquierdo.setBackground(getBackground());
        panelMenuIzquierdo.setPreferredSize(new Dimension(200, 0));
        panelMenuIzquierdo.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(220, 220, 220)));
        panelMenuIzquierdo.add(menuLateral, BorderLayout.CENTER);
        add(panelMenuIzquierdo, BorderLayout.WEST);
      
        // --- Contenedor de pantallas (CardLayout) ---
        cards = new JPanel(new CardLayout());
        cards.add(new VistaPantallaPrincipal(), BIENVENIDA);
       cards.add(new VistaCuenta(), OTRA);
       cards.add( new VistaRecursos(), RECURSOS);
        // --- Wrapper para centrar y fijar ancho ---
        JPanel centroWrapper = new JPanel(new GridBagLayout());
        centroWrapper.setBackground(getBackground());
        cards.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        centroWrapper.add(cards, gbc);
        add(centroWrapper, BorderLayout.CENTER);

        menuLateral.addVerCuentaListener(e -> muestraCard(OTRA));
        menuLateral.addJugarListener(e -> muestraCard(BIENVENIDA));
        menuLateral.addVistaRecursos (e -> muestraCard(RECURSOS));
        
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

    public void muestraCard(String clave) {
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, clave);
    }
}


