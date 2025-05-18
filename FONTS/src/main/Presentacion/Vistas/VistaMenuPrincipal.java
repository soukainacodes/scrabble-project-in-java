package Presentacion.Vistas;

import java.awt.*;
import javax.swing.*;

public class VistaMenuPrincipal extends JPanel {

    public static final String OTRA = "OTRA";
    public static final String RECURSOS = "RECURSOS";

    private final JPanel panelMenuIzquierdo;
    private final JPanel menuContainer; // Container for the left menu area
    private final JButton botonToggleMenu;
    private final JPanel cards;
    private final JPanel centroWrapper;
    private boolean menuVisible = true;

    public VistaMenuPrincipal() {
        setBackground(new Color(242, 226, 177));
        setLayout(new BorderLayout());
        
        // --- Container for the menu area (always present with same width) ---
        menuContainer = new JPanel(new BorderLayout());
        menuContainer.setPreferredSize(new Dimension(200, 0));
        menuContainer.setBackground(getBackground());
        
        // --- Panel lateral completo (WEST) ---
        panelMenuIzquierdo = new JPanel(new BorderLayout());
        panelMenuIzquierdo.setBackground(new Color(230, 220, 245)); // Color lila para el panel completo
        panelMenuIzquierdo.setPreferredSize(new Dimension(200, 0));
        
        // --- BOTÓN TOGGLE integrado en el panel izquierdo ---
        botonToggleMenu = new JButton("✖");
        botonToggleMenu.setFont(new Font("Arial", Font.BOLD, 24));
        botonToggleMenu.setFocusPainted(false);
        botonToggleMenu.setBorderPainted(false);
        botonToggleMenu.setContentAreaFilled(false);
        botonToggleMenu.setBackground(panelMenuIzquierdo.getBackground());
        botonToggleMenu.addActionListener(e -> toggleMenu());
        
        // Agregar el botón al NORTE del panel izquierdo, no al panel principal
        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCabecera.setBackground(panelMenuIzquierdo.getBackground());
        panelCabecera.add(botonToggleMenu);
        panelMenuIzquierdo.add(panelCabecera, BorderLayout.NORTH);
        
        // Add the menu panel to the container
        menuContainer.add(panelMenuIzquierdo, BorderLayout.CENTER);
        
        // Añadir el contenedor del menú al panel principal
        add(menuContainer, BorderLayout.WEST);

        // --- Contenedor de pantallas (CardLayout) ---
        cards = new JPanel(new CardLayout());
        
        // --- Wrapper para centrar y fijar ancho ---
        centroWrapper = new JPanel(new GridBagLayout());
        centroWrapper.setBackground(getBackground());
        cards.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        centroWrapper.add(cards, gbc);
        add(centroWrapper, BorderLayout.CENTER);
    }

    public void addMenuLateral(VistaMenuLateral menuLateral){
        // Añadimos un panel que contendrá el menú lateral
        JPanel menuContainerInternal = new JPanel(new BorderLayout());
        menuContainerInternal.setBackground(panelMenuIzquierdo.getBackground());
        menuContainerInternal.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Sin bordes para el contenedor
        
        // Añade el menú lateral al contenedor
        menuContainerInternal.add(menuLateral, BorderLayout.CENTER);
        
        // Añade el contenedor del menú al panel izquierdo
        panelMenuIzquierdo.add(menuContainerInternal, BorderLayout.CENTER);
    }
    
    public void addCard(String nombre, Component vista) {
        cards.add(vista, nombre);
    }

    public void jugarPartida(VistaScrabble vs) {
        // Limpia todo el contenido de este panel
        removeAll();

        // Cambiamos el layout para que VistaScrabble ocupe todo el espacio
        setLayout(new BorderLayout());

        // Instanciamos y añadimos la vista del tablero
        vs = new VistaScrabble();
        add(vs, BorderLayout.CENTER);

        // Refrescamos la UI
        revalidate();
        repaint();
    }

    private void cerrarSesion() {
        // Abrir la ventana de login
        VistaLogin login = new VistaLogin();
        login.setVisible(true);

        // Cerrar la ventana actual que contiene este panel
        Window ventana = SwingUtilities.getWindowAncestor(this);
        if (ventana != null) {
            ventana.dispose();
        }
    }

    private void toggleMenu() {
        if (menuVisible) {
            // Hide menu but keep a small container for the button
            // Create a panel for just the button
            JPanel soloBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
            soloBoton.setBackground(getBackground());
            soloBoton.add(botonToggleMenu);
            
            // Change button to show "menu" icon
            botonToggleMenu.setText("☰");
            
            // Reset the preferred size to minimum to allow content expansion
            menuContainer.setPreferredSize(new Dimension(50, 0));
            menuContainer.removeAll();
            menuContainer.add(soloBoton, BorderLayout.NORTH);
        } else {
            // Restore the menu with full size
            menuContainer.setPreferredSize(new Dimension(200, 0));
            menuContainer.removeAll();
            menuContainer.add(panelMenuIzquierdo, BorderLayout.CENTER);
            botonToggleMenu.setText("✖");
            
            // Make sure button is in the header panel
            JPanel panelCabecera = (JPanel)panelMenuIzquierdo.getComponent(0);
            if (!panelCabecera.isAncestorOf(botonToggleMenu)) {
                panelCabecera.add(botonToggleMenu);
            }
        }
        menuVisible = !menuVisible;
        
        // Revalidate and repaint the entire panel to ensure proper layout update
        revalidate();
        repaint();
    }

    public void muestraCard(String clave) {
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, clave);
    }
}