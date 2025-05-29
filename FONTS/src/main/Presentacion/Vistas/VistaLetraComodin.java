package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Vista que permite al usuario seleccionar una letra para representar un comodín en el juego.
 * Incluye un campo de texto para ingresar la letra y un botón para aceptar la selección.
 */
public class VistaLetraComodin extends JFrame {

    /**
     * Color de fondo de la aplicación.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Crema

    /**
     * Color para el texto oscuro de la interfaz.
     */
    private static final Color FG = new Color(20, 40, 80);               // Texto oscuro

    /**
     * Color para botón.
     */
    private static final Color LILA_OSCURO = new Color(52, 28, 87);      // Lila oscuro

    /**
     * Color para botón hover.
     */
    private static final Color LILA_CLARO = new Color(180, 95, 220);     // Lila claro

    /**
     * Color para el borde de los paneles.
     */
    private static final Color BORDE_COLOR = new Color(220, 200, 150);   // Borde de paneles
    

    /**
     * Campo de texto para ingresar la letra del comodín.
     */
    private JTextField campoTexto;

    /**
     * Botón para aceptar la selección de la letra.
     */
    private JButton botonAceptar;

    /**
     * Etiqueta para mostrar mensajes de error.
     */
    private JLabel errorLabel;
    
    /**
     * Constructor de la vista.
     * Configura la ventana, los paneles y los componentes con un diseño atractivo y funcional.
     */
    public VistaLetraComodin() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(APP_BG_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        formPanel.setOpaque(false);
        
        campoTexto = crearCampoTexto();
        formPanel.add(crearPanelCampo("Escoge que letra representa el comodín:", campoTexto));
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(200, 0, 0));
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
        botonAceptar = createStylishButton("Aceptar");
        botonContainer.add(botonAceptar);
        buttonPanel.add(botonContainer, BorderLayout.CENTER);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
                
        panelPrincipal.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(panelPrincipal);
        pack();
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    
    /**
     * Crea un panel con un campo de texto y una etiqueta.
     * Este panel se utiliza para organizar los campos de entrada en la vista.
     * @param labelText Texto de la etiqueta que describe el campo.
     * @param field Componente de entrada (JTextField, JComboBox, etc.) que se añadirá al panel.
     * @return Un JPanel que contiene la etiqueta y el campo de entrada.
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
     * Crea un campo de texto estilizado con bordes redondeados y fondo transparente.
     * Este campo se utiliza para ingresar la letra del comodín.
     * @return Un JTextField configurado con estilo personalizado.
     */
    private JTextField crearCampoTexto() {
        JTextField field = new JTextField(15) {
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
     * Este botón se utiliza para aceptar la selección de la letra del comodín.
     * @param text Texto que se mostrará en el botón.
     * @return Un JButton configurado con estilo personalizado.
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
                
                Color bg = isPressed ? LILA_OSCURO : (isHovered ? LILA_OSCURO : LILA_CLARO);
                
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
     * Panel personalizado con esquinas redondeadas y fondo con gradiente.
     * Este panel se utiliza para contener los componentes de la vista.
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
     * Añade un ActionListener al botón de aceptar.
     * Este método permite que la vista notifique a los controladores cuando el usuario hace clic en el botón.
     * @param l El ActionListener que se añadirá al botón.
     */
    public void aceptar(ActionListener l) {
        botonAceptar.addActionListener(l);
    }   

    /**
     * Obtiene el texto ingresado en el campo de texto.
     * Este método se utiliza para recuperar la letra del comodín seleccionada por el usuario.
     * @return La letra ingresada en el campo de texto.
     */
    public String getTexto() {
        return campoTexto.getText();
    }
    
    /**
     * Establece el texto del campo de texto.
     * Este método se utiliza para prellenar el campo de texto con una letra específica.
     * @param texto La letra que se establecerá en el campo de texto.
     */
    public void setTexto(String texto) {
        campoTexto.setText(texto);
    }

    /**
     * Establece un mensaje de error que se mostrará en la vista.
     * Este método se utiliza para informar al usuario sobre errores o problemas con la entrada.
     * El mensaje desaparece automáticamente después de 5 segundos.
     * @param mensaje El mensaje de error a mostrar.
     */
    public void setError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
        
        new Timer(5000, (e) -> {
            errorLabel.setVisible(false);
        }).start();
        
        revalidate();
        repaint();
    }
    
    /**
     * Limpia el campo de texto.
     * Este método se utiliza para borrar cualquier letra ingresada por el usuario.
     */
    public void focusEnCampo() {
        campoTexto.requestFocusInWindow();
    }
}