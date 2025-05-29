package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Vista que muestra el manual del juego en formato HTML.
 * Permite cargar y visualizar manuales de usuario con enlaces externos.
 */
public class VistaManual extends JPanel {

    /**
     * Ruta donde se encuentran los manuales HTML.
     */
    private static final String RUTA_MANUALES = "FONTS/src/main/Recursos/Manuales/";

    /**
     * Color para el fondo de la aplicación.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177); // Crema

    /**
     * Color para los bordes.
     */
    private static final Color BORDE_COLOR = new Color(220, 200, 150);

    /**
     * Panel que contiene el contenido del manual en HTML.
     * Utiliza JEditorPane para mostrar el contenido con estilos personalizados.
     */
    private JEditorPane contenidoHTML;

    /**
     * Constructor de la vista del manual.
     * Configura el layout, los paneles y el contenido HTML.
     */
    public VistaManual() {
        setLayout(new BorderLayout());
        setBackground(APP_BG_COLOR);
        setBorder(new EmptyBorder(5, 20, 5, 20));
        setPreferredSize(new Dimension(700, 520));

        JPanel titlePanel = createTitlePanel();

        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout());

        contenidoHTML = new JEditorPane();
        contenidoHTML.setEditable(false);
        contenidoHTML.setContentType("text/html");

        HTMLEditorKit kit = new HTMLEditorKit();
        contenidoHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                "body {color: #283050; font-family: Arial, sans-serif; margin: 20px; background-color: white;}");
        styleSheet.addRule("h1, h2, h3 {color: #34195F; font-family: 'Arial Black', Arial, sans-serif;}");
        styleSheet.addRule(
                "h1 {font-size: 28px; margin-top: 25px; border-bottom: 3px solid #B45FDC; padding-bottom: 8px;}");
        styleSheet.addRule("h2 {font-size: 22px; margin-top: 20px; color: #34195F;}");
        styleSheet.addRule("h3 {font-size: 18px; margin-top: 15px; color: #34195F;}");
        styleSheet.addRule("p {font-size: 16px; line-height: 1.7; margin: 12px 0;}");
        styleSheet.addRule("ul, ol {margin-left: 25px; padding-left: 25px;}");
        styleSheet.addRule("li {margin: 8px 0; line-height: 1.5; font-size: 16px;}");
        styleSheet.addRule(
                "code {background-color: #F5F5F5; padding: 3px 6px; border-radius: 4px; font-family: Consolas, monospace;}");
        styleSheet.addRule(
                ".importante {background-color: #FFF3CD; border-left: 5px solid #FFD700; padding: 15px; margin: 15px 0; border-radius: 5px;}");
        styleSheet.addRule(
                ".tip {background-color: #D1ECF1; border-left: 5px solid #17A2B8; padding: 15px; margin: 15px 0; border-radius: 5px;}");
        styleSheet.addRule(
                ".ejemplo {background-color: #F8F9FA; border: 1px solid #DDD; padding: 18px; margin: 15px 0; border-radius: 5px;}");

        contenidoHTML.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String href = e.getDescription();

                    if (href != null && (href.startsWith("http:") || href.startsWith("https:"))) {
                        try {
                            Desktop.getDesktop().browse(new URI(href));
                        } catch (Exception ex) {
                            System.err.println("Error al abrir enlace externo: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        // Intentar cargar el manual por defecto
        try {
            cargarManual("manual.html");
        } catch (IOException e) {
            // Si no se puede cargar, mostrar un mensaje de error
            contenidoHTML.setText("<html><body><h1>Error al cargar el manual</h1>" +
                    "<p>No se pudo cargar el manual. Por favor, inténtelo de nuevo.</p>" +
                    "<p>Error: " + e.getMessage() + "</p></body></html>");
        }

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

    /**
     * Carga un archivo de manual HTML desde la carpeta de manuales
     * 
     * @param nombreArchivo Nombre del archivo HTML (ej: "manual.html")
     * @throws IOException Si hay problemas al leer el archivo
     */
    public void cargarManual(String nombreArchivo) throws IOException {
        File archivo = new File(RUTA_MANUALES + nombreArchivo);
        StringBuilder contenido = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
                contenido.append("\n");
            }
        }

        setContenidoHTML(contenido.toString());
    }

    /**
     * Crea el panel de título con el texto "MANUAL DEL JUEGO".
     * Utiliza un diseño de dos líneas con colores personalizados para cada letra.
     * 
     * @return El panel de título configurado.
     */
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setBackground(APP_BG_COLOR);
        titlePanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        JPanel linea1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linea1Panel.setBackground(APP_BG_COLOR);

        JPanel linea2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linea2Panel.setBackground(APP_BG_COLOR);

        String[] letras1 = { "M", "A", "N", "U", "A", "L" };
        Color[] colores1 = {
                new Color(220, 130, 95), // Naranja rojizo
                new Color(95, 170, 220), // Azul claro
                new Color(220, 180, 95), // Amarillo
                new Color(150, 220, 95), // Verde
                new Color(180, 95, 220), // Morado/Lila
                new Color(220, 95, 160) // Rosa
        };

        // Crear letras para "DEL JUEGO"
        String[] letras2 = { "D", "E", "L", " ", "J", "U", "E", "G", "O" };
        Color[] colores2 = {
                new Color(95, 220, 190), // Turquesa
                new Color(235, 140, 80), // Naranja
                new Color(220, 130, 95), // Naranja rojizo
                APP_BG_COLOR, // Fondo (espacio)
                new Color(95, 170, 220), // Azul claro
                new Color(220, 180, 95), // Amarillo
                new Color(150, 220, 95), // Verde
                new Color(180, 95, 220), // Morado/Lila
                new Color(220, 95, 160) // Rosa
        };

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

        JPanel fichas2 = new JPanel();
        fichas2.setLayout(new BoxLayout(fichas2, BoxLayout.X_AXIS));
        fichas2.setBackground(APP_BG_COLOR);
        fichas2.add(Box.createHorizontalGlue());

        for (int i = 0; i < letras2.length; i++) {
            if (" ".equals(letras2[i])) {
                fichas2.add(Box.createHorizontalStrut(15));
            } else {
                JLabel l = crearFichaTitulo(letras2[i], colores2[i]);
                fichas2.add(l);
                if (i < letras2.length - 1 && !" ".equals(letras2[i + 1])) {
                    fichas2.add(Box.createHorizontalStrut(5));
                }
            }
        }

        fichas2.add(Box.createHorizontalGlue());
        linea2Panel.add(fichas2);

        titlePanel.add(linea1Panel);
        titlePanel.add(linea2Panel);

        return titlePanel;
    }

    /**
     * Crea una etiqueta personalizada para las fichas del título.
     * Utiliza un fondo redondeado y un efecto de hover.
     * 
     * @param texto El texto de la ficha.
     * @param color El color de fondo de la ficha.
     * @return La etiqueta configurada.
     */
    private JLabel crearFichaTitulo(String texto, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
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
        l.setFont(new Font("Arial Black", Font.BOLD, 28));
        l.setPreferredSize(new Dimension(40, 40));
        l.addMouseListener(new HoverEfectoTexto(l));
        return l;
    }

    /**
     * Panel redondeado que contiene el contenido del manual.
     * Tiene un borde y un fondo con gradiente.
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
                    0, getHeight(), APP_BG_COLOR);
            g2.setPaint(gp);
            g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 5, CORNER_RADIUS - 2, CORNER_RADIUS - 2);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Clase para manejar el efecto hover en las etiquetas del título.
     * Cambia el color del texto y el cursor al pasar el mouse.
     */
    private static class HoverEfectoTexto extends MouseAdapter {
        private final JLabel label;

        HoverEfectoTexto(JLabel l) {
            this.label = l;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            label.setForeground(new Color(255, 255, 200));
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            label.setForeground(Color.WHITE);
            label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Establece el contenido HTML del manual.
     * Desplaza el cursor al inicio del contenido.
     * 
     * @param html El contenido HTML a mostrar.
     */
    public void setContenidoHTML(String html) {
        contenidoHTML.setText(html);
        contenidoHTML.setCaretPosition(0); // Desplazar al inicio
    }
}