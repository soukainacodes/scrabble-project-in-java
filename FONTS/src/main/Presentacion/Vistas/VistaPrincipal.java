package Presentacion.Vistas;

import java.awt.*;
import javax.swing.*;

/**
 * Vista principal de la aplicación que contiene el menú lateral y el área de contenido.
 * Permite alternar la visibilidad del menú y gestionar las diferentes vistas de la aplicación.
 */
public class VistaPrincipal extends JPanel {

    /**
     * Colores personalizados para la interfaz.
     */
    private final JPanel panelMenuIzquierdo;

    /**
     * Panel que contiene el menú lateral y el botón para alternar su visibilidad.
     */
    private final JPanel menuContainer; 

    /**
     * Botón para alternar la visibilidad del menú lateral.
     * Cambia entre mostrar el menú completo y un botón para abrirlo.
     */
    private final JButton botonToggleMenu;

    /**
     * Panel que contiene las tarjetas de contenido.
     * Utiliza un CardLayout para gestionar múltiples vistas.
     */
    private final JPanel cards;

    /**
     * Panel que envuelve el área central de contenido.
     * Permite un mejor control del layout y la disposición de las tarjetas.
     */
    private final JPanel centroWrapper;

    /**
     * Indica si el menú lateral está visible o no.
     * Se utiliza para alternar entre mostrar y ocultar el menú.
     */
    private boolean menuVisible = true;


    /**
     * Constructor de la vista principal.
     * Configura el panel principal, el menú lateral y el contenedor de tarjetas.
     */
    public VistaPrincipal() {
        setBackground(new Color(242, 226, 177));
        setLayout(new BorderLayout());
        menuContainer = new JPanel(new BorderLayout());
        menuContainer.setPreferredSize(new Dimension(200, 0));
        menuContainer.setBackground(getBackground());
        
        panelMenuIzquierdo = new JPanel(new BorderLayout());
        panelMenuIzquierdo.setBackground(new Color(230, 220, 245)); 
        panelMenuIzquierdo.setPreferredSize(new Dimension(200, 0));
        
        botonToggleMenu = new JButton("✖");
        botonToggleMenu.setFont(new Font("Arial", Font.BOLD, 24));
        botonToggleMenu.setFocusPainted(false);
        botonToggleMenu.setBorderPainted(false);
        botonToggleMenu.setContentAreaFilled(false);
        botonToggleMenu.setBackground(panelMenuIzquierdo.getBackground());
        botonToggleMenu.addActionListener(e -> toggleMenu());
        
        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCabecera.setBackground(panelMenuIzquierdo.getBackground());
        panelCabecera.add(botonToggleMenu);
        panelMenuIzquierdo.add(panelCabecera, BorderLayout.NORTH);
        
        menuContainer.add(panelMenuIzquierdo, BorderLayout.CENTER);
        
        add(menuContainer, BorderLayout.WEST);

        cards = new JPanel(new CardLayout());
        
        centroWrapper = new JPanel(new GridBagLayout());
        centroWrapper.setBackground(getBackground());
        cards.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        centroWrapper.add(cards, gbc);
        add(centroWrapper, BorderLayout.CENTER);
    }


    /**
     * Añade un menú lateral al panel izquierdo.
     * @param menuLateral El menú lateral a añadir.
     */
    public void addMenuLateral(VistaMenuLateral menuLateral){
        JPanel menuContainerInternal = new JPanel(new BorderLayout());
        menuContainerInternal.setBackground(panelMenuIzquierdo.getBackground());
        menuContainerInternal.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Sin bordes para el contenedor
        
        menuContainerInternal.add(menuLateral, BorderLayout.CENTER);
        
        panelMenuIzquierdo.add(menuContainerInternal, BorderLayout.CENTER);
    }
  
    
    /**
     * Añade una tarjeta al panel de tarjetas.
     * @param nombre El nombre de la tarjeta.
     * @param vista La vista que representa la tarjeta.
     */
    public void addCard(String nombre, Component vista) {
        cards.add(vista, nombre);
    }


    /**
     * Inicia una partida de Scrabble mostrando la vista del juego.
     * @param vs La vista del juego que se mostrará.
     */
    public void jugarPartida(VistaJuego vs) {
        // Limpia todo el contenido de este panel
        removeAll();

        // Cambiamos el layout para que VistaScrabble ocupe todo el espacio
        setLayout(new BorderLayout());

        // Instanciamos y añadimos la vista del tablero
       
        add(vs, BorderLayout.CENTER);

        // Refrescamos la UI
        revalidate();
        repaint();
    }


    /**
     * Muestra una tarjeta específica en el panel de tarjetas.
     * @param clave La clave de la tarjeta a mostrar.
     */
    private void toggleMenu() {
        if (menuVisible) {
            
            JPanel soloBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
            soloBoton.setBackground(getBackground());
            soloBoton.add(botonToggleMenu);
            botonToggleMenu.setText("☰");
            
            menuContainer.setPreferredSize(new Dimension(50, 0));
            menuContainer.removeAll();
            menuContainer.add(soloBoton, BorderLayout.NORTH);
        } else {
            menuContainer.setPreferredSize(new Dimension(200, 0));
            menuContainer.removeAll();
            menuContainer.add(panelMenuIzquierdo, BorderLayout.CENTER);
            botonToggleMenu.setText("✖");
            
            JPanel panelCabecera = (JPanel)panelMenuIzquierdo.getComponent(0);
            if (!panelCabecera.isAncestorOf(botonToggleMenu)) {
                panelCabecera.add(botonToggleMenu);
            }
        }
        menuVisible = !menuVisible;
        
        revalidate();
        repaint();
    }

    /**
     * Muestra una tarjeta específica en el panel de tarjetas.
     * @param clave La clave de la tarjeta a mostrar.
     */
    public void muestraCard(String clave) {
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, clave);
    }
}