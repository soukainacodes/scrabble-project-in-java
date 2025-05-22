package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class VistaManual extends JFrame {

    // Colores consistentes con las otras vistas
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Crema
    private static final Color FG = new Color(20, 40, 80);               // Texto oscuro
    private static final Color LILA_OSCURO = new Color(52, 28, 87);      // Lila oscuro
    private static final Color LILA_CLARO = new Color(180, 95, 220);     // Lila claro
    private static final Color BORDE_COLOR = new Color(220, 200, 150);   // Borde de paneles
    
    private JButton btnCerrar;
    private JEditorPane contenidoHTML;

    public VistaManual() {
        super("Manual del Juego");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Panel principal con fondo nuevo
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 15));
        panelPrincipal.setBackground(APP_BG_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel superior con título usando fichas de letras
        JPanel titlePanel = crearPanelTitulo("MANUAL DEL JUEGO");
        
        // Panel de contenido con bordes redondeados
        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Creación del editor HTML para el contenido del manual
        contenidoHTML = new JEditorPane();
        contenidoHTML.setEditable(false);
        contenidoHTML.setContentType("text/html");
        
        // Configurar estilos personalizados para HTML
        HTMLEditorKit kit = new HTMLEditorKit();
        contenidoHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {color: #283050; font-family: Arial, sans-serif; margin: 10px; background-color: white;}");
        styleSheet.addRule("h1, h2, h3 {color: #34195F; font-family: 'Arial Black', Arial, sans-serif;}");
        styleSheet.addRule("h1 {font-size: 24px; margin-top: 20px; border-bottom: 2px solid #B45FDC;}");
        styleSheet.addRule("h2 {font-size: 20px; margin-top: 16px; color: #34195F;}");
        styleSheet.addRule("h3 {font-size: 16px; margin-top: 12px; color: #34195F;}");
        styleSheet.addRule("p {font-size: 14px; line-height: 1.6; margin: 10px 0;}");
        styleSheet.addRule("ul, ol {margin-left: 20px; padding-left: 20px;}");
        styleSheet.addRule("li {margin: 5px 0; line-height: 1.4;}");
        styleSheet.addRule("code {background-color: #F5F5F5; padding: 2px 4px; border-radius: 3px; font-family: Consolas, monospace;}");
        styleSheet.addRule(".importante {background-color: #FFF3CD; border-left: 4px solid #FFD700; padding: 12px; margin: 10px 0; border-radius: 4px;}");
        styleSheet.addRule(".tip {background-color: #D1ECF1; border-left: 4px solid #17A2B8; padding: 12px; margin: 10px 0; border-radius: 4px;}");
        styleSheet.addRule(".ejemplo {background-color: #F8F9FA; border: 1px solid #DDD; padding: 15px; margin: 10px 0; border-radius: 4px;}");
        
        // Contenido HTML de ejemplo (reemplazar con tu contenido real)
        contenidoHTML.setText(getContenidoHTML());
        
        // Scroll para el contenido
        JScrollPane scrollPane = new JScrollPane(contenidoHTML);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDE_COLOR, 2, true));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll más rápido
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botón con estilo mejorado
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        btnCerrar = createStylishButton("Cerrar");
        buttonPanel.add(btnCerrar);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Añadir los paneles al panel principal
        panelPrincipal.add(titlePanel, BorderLayout.NORTH);
        panelPrincipal.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(panelPrincipal);
        
        // Establecer tamaño preferido y hacer que sea mínimamente grande
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
    }
    
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
                Color bg = isPressed ? LILA_OSCURO.darker() : (isHovered ? LILA_OSCURO : LILA_CLARO);
                
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
        button.setPreferredSize(new Dimension(120, 40));
        
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
    
    // Método para añadir acción al botón cerrar
    public void addCerrarListener(ActionListener l) {
        btnCerrar.addActionListener(l);
    }
    
    // Método para establecer el contenido HTML
    public void setContenidoHTML(String html) {
        contenidoHTML.setText(html);
        contenidoHTML.setCaretPosition(0); // Desplazar al inicio
    }
    
    // Método para obtener contenido HTML de ejemplo
    private String getContenidoHTML() {
        return "<html><body>" +
               "<h1>Manual del Juego de Scrabble</h1>" +
               "<div class='importante'><p><strong>Nota:</strong> Este manual explica todas las reglas y funcionamiento del juego de Scrabble implementado.</p></div>" +
               
               "<h2>Objetivos del juego</h2>" +
               "<p>El objetivo del Scrabble es conseguir la mayor puntuación posible formando palabras con las fichas disponibles sobre el tablero de juego. Las palabras pueden leerse de izquierda a derecha o de arriba a abajo.</p>" +
               
               "<h2>Componentes del juego</h2>" +
               "<ul>" +
               "    <li><strong>Tablero:</strong> Cuadrícula de 15×15 casillas con casillas especiales que multiplican el valor.</li>" +
               "    <li><strong>Fichas:</strong> 100 fichas con letras y sus valores correspondientes.</li>" +
               "    <li><strong>Atril:</strong> Cada jugador tiene un atril con 7 fichas.</li>" +
               "</ul>" +
               
               "<h2>Casillas especiales</h2>" +
               "<ul>" +
               "    <li><strong>Doble letra (DL):</strong> Duplica el valor de la letra colocada en esa casilla.</li>" +
               "    <li><strong>Triple letra (TL):</strong> Triplica el valor de la letra colocada en esa casilla.</li>" +
               "    <li><strong>Doble palabra (DP):</strong> Duplica el valor de toda la palabra.</li>" +
               "    <li><strong>Triple palabra (TP):</strong> Triplica el valor de toda la palabra.</li>" +
               "</ul>" +
               
               "<h2>Inicio del juego</h2>" +
               "<p>Al inicio, cada jugador recibe 7 fichas aleatoriamente de la bolsa. Se determina quién comienza, y el primer jugador debe formar una palabra que pase por la casilla central.</p>" +
               
               "<div class='ejemplo'>" +
               "    <h3>Ejemplo de turno</h3>" +
               "    <p>Un jugador con las fichas C, A, S, A, R, E, P puede formar la palabra 'CASA' horizontalmente. Si una 'A' cae en DL, esa letra valdrá el doble. Si además, la palabra pasa por DP, toda la palabra duplicará su valor.</p>" +
               "</div>" +
               
               "<h2>Reglas básicas</h2>" +
               "<ol>" +
               "    <li>Cada jugador, en su turno, debe intentar formar una palabra usando sus fichas y las ya existentes en el tablero.</li>" +
               "    <li>Las nuevas palabras deben conectarse con las existentes, compartiendo al menos una letra.</li>" +
               "    <li>Todas las palabras formadas deben existir en el diccionario oficial.</li>" +
               "    <li>Después de colocar fichas, el jugador repone hasta tener 7 fichas nuevamente.</li>" +
               "    <li>Un jugador puede pasar su turno o cambiar fichas.</li>" +
               "</ol>" +
               
               "<div class='tip'>" +
               "    <p><strong>Consejo:</strong> Busca siempre las casillas multiplicadoras para maximizar tu puntuación. Las casillas TP pueden triplicar el valor de toda tu palabra.</p>" +
               "</div>" +
               
               "<h2>Fin del juego</h2>" +
               "<p>El juego termina cuando:</p>" +
               "<ul>" +
               "    <li>Un jugador coloca todas sus fichas y no quedan más en la bolsa.</li>" +
               "    <li>Todos los jugadores pasan dos veces consecutivas.</li>" +
               "    <li>No es posible realizar más jugadas válidas.</li>" +
               "</ul>" +
               
               "<h2>Puntuación final</h2>" +
               "<p>Al finalizar, se resta de la puntuación de cada jugador el valor de las fichas que le quedaron. El jugador que colocó todas sus fichas suma el valor de las fichas que quedaron a los demás jugadores.</p>" +
               
               "</body></html>";
    }
}