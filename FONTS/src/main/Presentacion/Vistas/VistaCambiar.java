package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VistaCambiar extends JFrame {

    // Colores consistentes con las otras vistas
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Crema
    private static final Color FG = new Color(20, 40, 80);               // Texto oscuro
    private static final Color LILA_OSCURO = new Color(52, 28, 87);      // Lila oscuro
    private static final Color LILA_CLARO = new Color(180, 95, 220);     // Lila claro
    private static final Color BORDE_COLOR = new Color(220, 200, 150);   // Borde de paneles
    
    private JButton botonCambiar;
    private JTextField campoNombre;
    private JPasswordField campoPassword;
    private JPasswordField campoPasswordActual;
    private JPasswordField campoConfirmPassword; // Campo de confirmación de contraseña

    public VistaCambiar(String asunto) {
        super(
            "nombre".equals(asunto) ? "Cambiar Nombre"
            : "password".equals(asunto) ? "Cambiar Contraseña"
            : "Aplicación"
        );

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con fondo nuevo
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(APP_BG_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel del título con estilo similar a VistaCuenta
        JPanel titlePanel = crearPanelTitulo(asunto);
        
        // Panel de contenido con bordes redondeados
        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        formPanel.setOpaque(false);
        
        // Etiquetas y campos, ahora con estilo consistente
        if ("nombre".equals(asunto)) {
            formPanel.add(crearPanelCampo("Nombre nuevo:", campoNombre = crearCampoTexto()));
            formPanel.add(crearPanelCampo("Contraseña actual:", campoPasswordActual = crearCampoPassword()));
        } else {
            formPanel.add(crearPanelCampo("Contraseña actual:", campoPasswordActual = crearCampoPassword()));
            formPanel.add(crearPanelCampo("Contraseña nueva:", campoPassword = crearCampoPassword()));
            formPanel.add(crearPanelCampo("Confirmar contraseña:", campoConfirmPassword = crearCampoPassword())); // Añadido campo de confirmación
        }
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        // Panel de botón con estilo mejorado
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        
        botonCambiar = createStylishButton("Guardar Cambios");
        buttonPanel.add(botonCambiar);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Agregar los componentes al panel principal
        panelPrincipal.add(titlePanel, BorderLayout.NORTH);
        panelPrincipal.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(panelPrincipal);
        pack();
        setLocationRelativeTo(null);
    }
    
    private JPanel crearPanelTitulo(String asunto) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(APP_BG_COLOR);
        panel.setBorder(new EmptyBorder(5, 0, 15, 0));
        
        String titulo = "nombre".equals(asunto) ? "CAMBIAR NOMBRE" : "CAMBIAR PASSWORD";
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
    
    private JPasswordField crearCampoPassword() {
        JPasswordField field = new JPasswordField(15) {
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
    
    private JButton createStylishButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Radio de las esquinas redondeadas
                int radius = 15;
                
                // Determinar si el botón está en estado hover o presionado
                boolean isHovered = getModel().isRollover();
                boolean isPressed = getModel().isPressed();
                
                // Color base según estado
                Color bg = isPressed ? LILA_OSCURO : (isHovered ? LILA_OSCURO : LILA_CLARO);
                
                // Efecto de sombra si está en hover
                if (isHovered && !isPressed) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, radius, radius);
                }
                
                // Dibujar el fondo del botón con gradiente
                g2.setPaint(new GradientPaint(0, 0,
                    new Color(Math.min(bg.getRed() + 25, 255), 
                              Math.min(bg.getGreen() + 25, 255), 
                              Math.min(bg.getBlue() + 25, 255)),
                    0, getHeight(), bg));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                // Efecto de brillo en la parte superior
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
    
    // Panel con esquinas redondeadas y sombra
    private class RoundedPanel extends JPanel {
        
        private static final int CORNER_RADIUS = 20;
        
        public RoundedPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Pequeño margen para la sombra
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibujar sombra
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, CORNER_RADIUS, CORNER_RADIUS);
            
            // Dibujar borde
            g2.setColor(BORDE_COLOR);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
            
            // Dibujar fondo con gradiente
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
    
    // Efecto de hover para las fichas del título
    private static class HoverEfectoTexto extends MouseAdapter {
        private final JLabel label;
        HoverEfectoTexto(JLabel l) { this.label = l; }
        @Override public void mouseEntered(MouseEvent e) { label.setForeground(new Color(255, 255, 200)); }
        @Override public void mouseExited (MouseEvent e) { label.setForeground(Color.WHITE); }
    }
    
    // Métodos públicos
    public void cambiar(ActionListener l) {
        botonCambiar.addActionListener(l);
    }   

    public String getNombre() {
        return campoNombre != null ? campoNombre.getText() : "";
    }

    public char[] getPassword() {
        return campoPassword != null ? campoPassword.getPassword() : new char[0];
    }

    public char[] getPasswordActual() {
        return campoPasswordActual != null ? campoPasswordActual.getPassword() : new char[0];
    }
    
    // Método para obtener la contraseña de confirmación
    public char[] getConfirmPassword() {
        return campoConfirmPassword != null ? campoConfirmPassword.getPassword() : new char[0];
    }
}