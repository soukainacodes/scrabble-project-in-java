package Presentacion.Vistas;

import java.awt.*;
import javax.swing.*;

public class VistaMenuPrincipal extends JPanel {

   
    public static final String OTRA = "OTRA";
    public static final String RECURSOS = "RECURSOS";

  
//    private final VistaPantallaPrincipal pantallaPrincipal;
 //   private final VistaCrearPartida crearPartida;
 //   private final VistaCargarPartida cargarPartida;
    private final JPanel panelMenuIzquierdo;
    private final JButton botonToggleMenu;
    private final JPanel cards;
    private boolean menuVisible = true;

    public VistaMenuPrincipal() {
        setBackground(new Color(238,238,238,255));
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

        //menuLateral = new VistaMenuLateral();

        // --- Panel lateral completo (WEST) ---
        panelMenuIzquierdo = new JPanel(new BorderLayout());
        panelMenuIzquierdo.setBackground(getBackground());
        panelMenuIzquierdo.setPreferredSize(new Dimension(200, 0));
        panelMenuIzquierdo.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(220, 220, 220)));
        
        

        // --- Contenedor de pantallas (CardLayout) ---
       // pantallaPrincipal = new VistaPantallaPrincipal();
      //  crearPartida = new VistaCrearPartida();
      //  cargarPartida = new VistaCargarPartida();
        
        cards = new JPanel(new CardLayout());
        
      
        // --- Wrapper para centrar y fijar ancho ---
        JPanel centroWrapper = new JPanel(new GridBagLayout());
        centroWrapper.setBackground(getBackground());
        cards.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        centroWrapper.add(cards, gbc);
        add(centroWrapper, BorderLayout.CENTER);

       // menuLateral.addVerCuentaListener(e -> muestraCard(OTRA));
       // menuLateral.addJugarListener(e -> muestraCard(BIENVENIDA));
       // menuLateral.addVistaRecursos(e -> muestraCard(RECURSOS));
       // menuLateral.addVistaRanking(e -> muestraCard("RANKING"));
       // menuLateral.cerrarSesion(e -> cerrarSesion());
       // pantallaPrincipal.addVistaCrearPartida(e -> muestraCard("CREARPARTIDA"));
     //   pantallaPrincipal.addVistaCargarPartida(e -> muestraCard("CARGARPARTIDA"));

    //    crearPartida.jugarPartida(e -> jugarPartida());
      //  cargarPartida.jugarPartida(e -> jugarPartida());

    }

   public void addMenuLateral(VistaMenuLateral menuLateral){
    panelMenuIzquierdo.add(menuLateral, BorderLayout.CENTER);
    add(panelMenuIzquierdo, BorderLayout.WEST);
    
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
