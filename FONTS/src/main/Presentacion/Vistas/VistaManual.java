package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class VistaManual extends JPanel {

    // Colores consistentes con VistaRecursos
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Crema
    private static final Color FG = new Color(20, 40, 80);               // Texto oscuro
    private static final Color LILA_OSCURO = new Color(52, 28, 87);      // Lila oscuro
    private static final Color LILA_CLARO = new Color(180, 95, 220);     // Lila claro
    private static final Color BORDE_COLOR = new Color(220, 200, 150);   // Borde de paneles
    
    private JEditorPane contenidoHTML;

    public VistaManual() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(new EmptyBorder(5, 20, 5, 20)); // Consistente con VistaRecursos
        setPreferredSize(new Dimension(700, 520)); // Consistente con VistaRecursos

        // Panel superior con título usando fichas de letras en dos líneas
        JPanel titlePanel = createTitlePanel();
        
        // Panel de contenido con bordes redondeados
        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout());
        
        // Creación del editor HTML para el contenido del manual
        contenidoHTML = new JEditorPane();
        contenidoHTML.setEditable(false);
        contenidoHTML.setContentType("text/html");
        
        // Configurar estilos personalizados para HTML
        HTMLEditorKit kit = new HTMLEditorKit();
        contenidoHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {color: #283050; font-family: Arial, sans-serif; margin: 20px; background-color: white;}");
        styleSheet.addRule("h1, h2, h3 {color: #34195F; font-family: 'Arial Black', Arial, sans-serif;}");
        styleSheet.addRule("h1 {font-size: 28px; margin-top: 25px; border-bottom: 3px solid #B45FDC; padding-bottom: 8px;}");
        styleSheet.addRule("h2 {font-size: 22px; margin-top: 20px; color: #34195F;}");
        styleSheet.addRule("h3 {font-size: 18px; margin-top: 15px; color: #34195F;}");
        styleSheet.addRule("p {font-size: 16px; line-height: 1.7; margin: 12px 0;}");
        styleSheet.addRule("ul, ol {margin-left: 25px; padding-left: 25px;}");
        styleSheet.addRule("li {margin: 8px 0; line-height: 1.5; font-size: 16px;}");
        styleSheet.addRule("code {background-color: #F5F5F5; padding: 3px 6px; border-radius: 4px; font-family: Consolas, monospace;}");
        styleSheet.addRule(".importante {background-color: #FFF3CD; border-left: 5px solid #FFD700; padding: 15px; margin: 15px 0; border-radius: 5px;}");
        styleSheet.addRule(".tip {background-color: #D1ECF1; border-left: 5px solid #17A2B8; padding: 15px; margin: 15px 0; border-radius: 5px;}");
        styleSheet.addRule(".ejemplo {background-color: #F8F9FA; border: 1px solid #DDD; padding: 18px; margin: 15px 0; border-radius: 5px;}");
        
        // Contenido HTML de ejemplo
        contenidoHTML.setText(getContenidoHTML());
        
        // Scroll con UI consistente con VistaRecursos
        JScrollPane scrollPane = new JScrollPane(contenidoHTML);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); // Scroll más rápido
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Añadir los paneles directamente a este JPanel
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createTitlePanel() {
        // Panel principal para las dos líneas
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setBackground(APP_BG_COLOR);
        titlePanel.setBorder(new EmptyBorder(15, 0, 15, 0)); // Consistente con VistaRecursos
        
        // Primera línea: "MANUAL"
        JPanel linea1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linea1Panel.setBackground(APP_BG_COLOR);
        
        // Segunda línea: "DEL JUEGO"
        JPanel linea2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linea2Panel.setBackground(APP_BG_COLOR);
        
        // Crear letras para "MANUAL"
        String[] letras1 = {"M", "A", "N", "U", "A", "L"};
        Color[] colores1 = {
            new Color(220, 130, 95), // Naranja rojizo
            new Color(95, 170, 220), // Azul claro
            new Color(220, 180, 95), // Amarillo
            new Color(150, 220, 95), // Verde
            new Color(180, 95, 220), // Morado/Lila
            new Color(220, 95, 160)  // Rosa
        };
        
        // Crear letras para "DEL JUEGO"
        String[] letras2 = {"D", "E", "L", " ", "J", "U", "E", "G", "O"};
        Color[] colores2 = {
            new Color(95, 220, 190), // Turquesa
            new Color(235, 140, 80), // Naranja
            new Color(220, 130, 95), // Naranja rojizo
            APP_BG_COLOR,            // Fondo (espacio)
            new Color(95, 170, 220), // Azul claro
            new Color(220, 180, 95), // Amarillo
            new Color(150, 220, 95), // Verde
            new Color(180, 95, 220), // Morado/Lila
            new Color(220, 95, 160)  // Rosa
        };
        
        // Añadir letras de "MANUAL"
        JPanel fichas1 = new JPanel();
        fichas1.setLayout(new BoxLayout(fichas1, BoxLayout.X_AXIS));
        fichas1.setBackground(APP_BG_COLOR);
        fichas1.add(Box.createHorizontalGlue());
        
        for (int i = 0; i < letras1.length; i++) {
            JLabel l = crearFichaTitulo(letras1[i], colores1[i]);
            fichas1.add(l);
            if (i < letras1.length - 1) {
                fichas1.add(Box.createHorizontalStrut(5)); // Espacio entre letras
            }
        }
        
        fichas1.add(Box.createHorizontalGlue());
        linea1Panel.add(fichas1);
        
        // Añadir letras de "DEL JUEGO"
        JPanel fichas2 = new JPanel();
        fichas2.setLayout(new BoxLayout(fichas2, BoxLayout.X_AXIS));
        fichas2.setBackground(APP_BG_COLOR);
        fichas2.add(Box.createHorizontalGlue());
        
        for (int i = 0; i < letras2.length; i++) {
            if (" ".equals(letras2[i])) {
                fichas2.add(Box.createHorizontalStrut(15)); // Espacio entre palabras
            } else {
                JLabel l = crearFichaTitulo(letras2[i], colores2[i]);
                fichas2.add(l);
                if (i < letras2.length - 1 && !" ".equals(letras2[i + 1])) {
                    fichas2.add(Box.createHorizontalStrut(5)); // Espacio entre letras
                }
            }
        }
        
        fichas2.add(Box.createHorizontalGlue());
        linea2Panel.add(fichas2);
        
        // Añadir las dos líneas al panel principal
        titlePanel.add(linea1Panel);
        titlePanel.add(linea2Panel);
        
        return titlePanel;
    }
    
    private JLabel crearFichaTitulo(String texto, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Igual que VistaRecursos
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 3, getWidth(), getHeight(), 10, 10); // Igual que VistaRecursos
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial Black", Font.BOLD, 28)); // Igual que VistaRecursos
        l.setPreferredSize(new Dimension(40, 40));         // Igual que VistaRecursos
        l.addMouseListener(new HoverEfectoTexto(l));
        return l;
    }
    
    // Panel con esquinas redondeadas y sombra - Igual que en VistaRecursos
    private class RoundedPanel extends JPanel {
        
        private static final int CORNER_RADIUS = 20; // Igual que VistaRecursos
        
        public RoundedPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Pequeño margen para la sombra
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibujar sombra - Igual que VistaRecursos
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, CORNER_RADIUS, CORNER_RADIUS);
            
            // Dibujar borde - Igual que VistaRecursos
            g2.setColor(BORDE_COLOR);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
            
            // Dibujar fondo con gradiente - Igual que VistaRecursos
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
    
    // Efecto de hover para las fichas del título - Igual que VistaRecursos
    private static class HoverEfectoTexto extends MouseAdapter {
        private final JLabel label;
        HoverEfectoTexto(JLabel l) { this.label = l; }
        @Override public void mouseEntered(MouseEvent e) { 
            label.setForeground(new Color(255, 255, 200));
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override public void mouseExited (MouseEvent e) { 
            label.setForeground(Color.WHITE);
            label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
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