package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Vista para eliminar un jugador, solicitando confirmación mediante la contraseña actual.
 * Permite al usuario eliminar su cuenta de forma segura.
 */
public class VistaEliminarJugador extends JFrame {

    /**
     * Colores personalizados para la interfaz.
     * Utilizados para el fondo, texto, bordes y mensajes de error.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Crema

    /**
     * Color para el texto oscuro.
     */
    private static final Color FG = new Color(20, 40, 80);               // Texto oscuro

    /**
     * Color para los bordes de los paneles.
     * Utilizado para dar un aspecto elegante a los campos de entrada y botones.
     */
    private static final Color BORDE_COLOR = new Color(220, 200, 150);   // Borde de paneles

    /**
     * Color para mensajes de error.
     * Utilizado para resaltar mensajes de error en la interfaz.
     */
    private static final Color ROJO_ERROR = new Color(200, 0, 0);        // Color para mensajes de error
    

    /**
     * Botón para verificar la contraseña y proceder con la eliminación de la cuenta.
     * Se activa al hacer clic y valida la contraseña ingresada.
     */
    private JButton botonVerificar;

    /**
     * Campo de entrada para la contraseña actual del usuario.
     * Se utiliza para verificar la identidad antes de eliminar la cuenta.
     */
    private JPasswordField campoPassword;

    /**
     * Etiqueta para mostrar mensajes de error.
     * Se utiliza para informar al usuario sobre problemas con la contraseña ingresada.
     */
    private JLabel errorLabel;


    /**
     * Constructor de la vista de eliminación de jugador.
     * Configura la ventana, los paneles y los componentes necesarios para solicitar la contraseña.
     *
     * @param username Nombre de usuario del jugador que se va a eliminar.
     */
    public VistaEliminarJugador(String username) {
        super("Confirmar contraseña");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(APP_BG_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel titlePanel = crearPanelTitulo("ELIMINAR CUENTA");
        
        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel(username);
        userLabel.setFont(new Font("Arial Black", Font.BOLD, 18));
        userLabel.setForeground(FG);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Mensaje de advertencia
        JLabel warningLabel = new JLabel("<html><div style='text-align:center; padding-left:15px;'>" +
                "<span style='color:rgb(200,0,0); font-weight:bold; display:block; margin:5px;'>¡Atención! Estás a punto de eliminar tu cuenta.<br>" +
                "Esta acción no se puede deshacer.</span><br><br>" +
                "Por razones de seguridad, confirma tu contraseña:</div></html>");
        warningLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        warningLabel.setForeground(FG);
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(userLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(warningLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        formPanel.setOpaque(false);
        formPanel.add(crearPanelCampo("Contraseña actual:", campoPassword = crearCampoPassword()));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(infoPanel);
        centerPanel.add(formPanel);
        
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(ROJO_ERROR);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setVisible(false);
        
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setOpaque(false);
        errorPanel.add(errorLabel);
        buttonPanel.add(errorPanel, BorderLayout.NORTH);
        
        JPanel botonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonContainer.setOpaque(false);
        botonVerificar = createStylishButton("Eliminar Cuenta");
        botonVerificar.setBackground(new Color(220, 60, 60));
        botonContainer.add(botonVerificar);
        buttonPanel.add(botonContainer, BorderLayout.CENTER);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panelPrincipal.add(titlePanel, BorderLayout.NORTH);
        panelPrincipal.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(panelPrincipal);
        pack();
        
        Dimension size = getSize();
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        
        setLocationRelativeTo(null);
    }
    

    /**
     * Crea un panel con el título de la ventana, mostrando cada letra en fichas de colores.
     * @param titulo El título a mostrar en el panel.
     * @return Un JPanel con el título estilizado.
     */
    private JPanel crearPanelTitulo(String titulo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(APP_BG_COLOR);
        panel.setBorder(new EmptyBorder(5, 0, 15, 0));
        
        String[] letras = titulo.split("");
        Color[] colores = new Color[letras.length];
        
        // Asignar colores alternados
        for (int i = 0; i < letras.length; i++) {
            if (" ".equals(letras[i])) {
                colores[i] = APP_BG_COLOR;
            } else {
                // Colores alternados para las fichas
                switch (i % 6) {
                    case 0: colores[i] = new Color(220, 130, 95); break;  // Naranja rojizo
                    case 1: colores[i] = new Color(95, 170, 220); break;   // Azul claro
                    case 2: colores[i] = new Color(220, 180, 95); break;   // Amarillo
                    case 3: colores[i] = new Color(150, 220, 95); break;   // Verde
                    case 4: colores[i] = new Color(180, 95, 220); break;   // Morado/Lila
                    case 5: colores[i] = new Color(220, 95, 160); break;   // Rosa
                }
            }
        }
        
        JPanel fichas = new JPanel();
        fichas.setLayout(new BoxLayout(fichas, BoxLayout.X_AXIS));
        fichas.setBackground(APP_BG_COLOR);
        fichas.add(Box.createHorizontalGlue());
        
        for (int i = 0; i < letras.length; i++) {
            if (" ".equals(letras[i])) {
                fichas.add(Box.createHorizontalStrut(10));
                continue;
            }
            JLabel l = crearFichaTitulo(letras[i], colores[i]);
            fichas.add(l);
            if (i < letras.length - 1 && !" ".equals(letras[i + 1]))
                fichas.add(Box.createHorizontalStrut(5));
        }
        
        fichas.add(Box.createHorizontalGlue());
        panel.add(fichas);
        
        return panel;
    }
    

    /**
     * Crea una etiqueta estilizada para las fichas del título.
     * Cada ficha tiene un fondo redondeado y un efecto de hover.
     * @param texto El texto a mostrar en la ficha.
     * @param color El color de fondo de la ficha.
     * @return Una JLabel con el texto estilizado.
     */
    private JLabel crearFichaTitulo(String texto, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 3, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial Black", Font.BOLD, 20));
        l.setPreferredSize(new Dimension(28, 28));
        l.addMouseListener(new HoverEfectoTexto(l));
        return l;
    }
    

    /**
     * Crea un panel con un campo de entrada y su etiqueta asociada.
     * Utiliza un diseño de BorderLayout para organizar el campo y la etiqueta.
     * @param labelText El texto de la etiqueta del campo.
     * @param field El componente de entrada (JTextField, JPasswordField, etc.).
     * @return Un JPanel con el campo y su etiqueta.
     */
    private JPanel crearPanelCampo(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(FG);
        
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setOpaque(false);
        labelPanel.add(label);
        
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    

    /**
     * Crea un campo de contraseña estilizado con esquinas redondeadas y fondo personalizado.
     * Utiliza un JPasswordField con un renderizado personalizado para el fondo.
     * @return Un JPasswordField estilizado.
     */
    private JPasswordField crearCampoPassword() {
        JPasswordField field = new JPasswordField(20) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_COLOR, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        field.setOpaque(false);
        
        return field;
    }
    

    /**
     * Crea un botón estilizado con esquinas redondeadas y efectos de hover.
     * Utiliza un renderizado personalizado para el fondo y los efectos visuales.
     * @param text El texto del botón.
     * @return Un JButton estilizado.
     */
    private JButton createStylishButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int radius = 15;
                
                boolean isHovered = getModel().isRollover();
                boolean isPressed = getModel().isPressed();
                
                Color bg = isPressed ? new Color(180, 40, 40) : (isHovered ? new Color(200, 50, 50) : new Color(220, 60, 60));
                
                if (isHovered && !isPressed) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, radius, radius);
                }
                
                g2.setPaint(new GradientPaint(0, 0,
                    new Color(Math.min(bg.getRed() + 25, 255), 
                              Math.min(bg.getGreen() + 25, 255), 
                              Math.min(bg.getBlue() + 25, 255)),
                    0, getHeight(), bg));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                if (!isPressed) {
                    g2.setColor(new Color(255, 255, 255, 70));
                    g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() / 2 - 2, radius, radius);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(180, 40));
        
        // Efecto al pasar el ratón
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return button;
    }
    

    /**
     * Panel personalizado con esquinas redondeadas y sombra.
     * Utiliza un renderizado personalizado para el fondo y los bordes.
     */
    private class RoundedPanel extends JPanel {
        
        private static final int CORNER_RADIUS = 20;
        
        public RoundedPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); 
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            

            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, CORNER_RADIUS, CORNER_RADIUS);

            g2.setColor(BORDE_COLOR);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
            
            GradientPaint gp = new GradientPaint(
                0, 0, APP_BG_COLOR.brighter(), 
                0, getHeight(), APP_BG_COLOR
            );
            g2.setPaint(gp);
            g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 5, CORNER_RADIUS - 2, CORNER_RADIUS - 2);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    /**
     * Clase para manejar el efecto de hover en las etiquetas del título.
     * Cambia el color del texto al pasar el ratón por encima.
     */
    private static class HoverEfectoTexto extends MouseAdapter {
        private final JLabel label;
        HoverEfectoTexto(JLabel l) { this.label = l; }
        @Override public void mouseEntered(MouseEvent e) { label.setForeground(new Color(255, 255, 200)); }
        @Override public void mouseExited (MouseEvent e) { label.setForeground(Color.WHITE); }
    }
    
    /**
     * Método para registrar un ActionListener en el botón de verificación.
     * Permite que la vista notifique al controlador cuando se hace clic en el botón.
     * @param l El ActionListener a registrar.
     */
    public void verificar(ActionListener l) {
        botonVerificar.addActionListener(l);
    }
    

    /**
     * Método para obtener la contraseña ingresada en el campo de contraseña.
     * Se utiliza para verificar la identidad del usuario antes de eliminar la cuenta.
     * @return La contraseña ingresada como un array de caracteres.
     */
    public char[] getPassword() {
        return campoPassword.getPassword();
    }
    
    /**
     * Método para establecer un mensaje de error en la interfaz.
     * Muestra un mensaje de error en la etiqueta correspondiente y lo oculta después de 5 segundos.
     * @param mensaje El mensaje de error a mostrar.
     */
    public void setError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
        
        new Timer(5000, (e) -> {
            errorLabel.setVisible(false);
        }).start();
        
        revalidate();
        pack();
    }
}