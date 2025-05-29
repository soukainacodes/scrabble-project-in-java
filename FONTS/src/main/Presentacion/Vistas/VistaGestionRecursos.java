package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Vista para la gestión de recursos del juego.
 * Permite al usuario añadir un Diccionario y una Bolsa, ya sea manualmente o a partir de archivos.
 */
public class VistaGestionRecursos extends JFrame {

    /**
     * Color para el fondo de la aplica.
     */
    private static final Color APP_BG_COLOR = new Color(242, 226, 177); // Crema

    /**
     * Color texto oscuro.
     */
    private static final Color FG = new Color(20, 40, 80); // Texto oscuro

    /**
     * Color texto secundario.
     */
    private static final Color FG_SUB = new Color(80, 90, 100); // Texto secundario

    /**
     * Colores para los botones.
     */
    private static final Color LILA_CLARO = new Color(180, 95, 220); // Lila claro

    /**
     * Color para el borde del panel.
     */
    private static final Color BORDE_COLOR = new Color(220, 200, 150); // Borde de paneles

    /**
     * Color para el botón de añadir diccionario.
     */
    private static final Color AZUL_CLARO = new Color(95, 170, 220); // Azul claro para botones

    /**
     * Campo de texto para el ID.
     */
    private JTextField txtID;

    /**
     * Etiqueta para mostrar el ID si se proporciona.
     */
    private JLabel labelID;

    /**
     * Áreas de texto para el Diccionario.
     * Se utilizan JTextArea para permitir múltiples líneas de texto.
     */
    private JTextArea textArea1;

    /**
     * Área de texto para la Bolsa.
     * Se utiliza JTextArea para permitir múltiples líneas de texto.
     */
    private JTextArea textArea2;

    /**
     * Botones para añadir el Diccionario de un archivo.
     */
    private JButton btnArchivo;
    /**
     * Botón para añadir la Bolsa a partir de un archivo.
     */
    private JButton btnArchivoBolsa;

    /**
     * Botón para aceptar los cambios.
     * Se utiliza para confirmar la entrada del usuario.
     */
    private JButton btnAceptar;

    /**
     * Etiqueta para mostrar mensajes de error.
     * Se utiliza para informar al usuario sobre problemas con la entrada.
     */
    private JLabel errorLabel;

    /**
     * Constructor de la vista de gestión de recursos.
     * Configura la ventana, los paneles y los componentes necesarios.
     *
     * @param label Texto para el ID si se proporciona, de lo contrario se crea un
     *              campo de texto.
     * @param title Título de la ventana.
     */
    public VistaGestionRecursos(String label, String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 15));
        panelPrincipal.setBackground(APP_BG_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = crearPanelTitulo("GESTIÓN DE RECURSOS");

        RoundedPanel idPanel = new RoundedPanel();
        idPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JLabel lblID = new JLabel("ID:");
        lblID.setFont(new Font("Arial", Font.BOLD, 16));
        lblID.setForeground(FG);

        if (label.isEmpty()) {
            txtID = crearTextField(15);
            idPanel.add(lblID);
            idPanel.add(txtID);
        } else {
            labelID = new JLabel(label);
            labelID.setFont(new Font("Arial", Font.PLAIN, 16));
            labelID.setForeground(FG);
            idPanel.add(lblID);
            idPanel.add(labelID);
        }

        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout(0, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel columnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        columnPanel.setOpaque(false);

        JPanel col1 = new JPanel();
        col1.setLayout(new BorderLayout(0, 10));
        col1.setOpaque(false);

        JLabel lblDic = new JLabel("Diccionario", SwingConstants.CENTER);
        lblDic.setFont(new Font("Arial", Font.BOLD, 18));
        lblDic.setForeground(FG);
        lblDic.setBorder(new EmptyBorder(0, 0, 5, 0));

        textArea1 = crearTextArea();
        JScrollPane scroll1 = new JScrollPane(textArea1);
        scroll1.setBorder(BorderFactory.createLineBorder(BORDE_COLOR, 2, true));

        col1.add(lblDic, BorderLayout.NORTH);
        col1.add(scroll1, BorderLayout.CENTER);

        JPanel col2 = new JPanel();
        col2.setLayout(new BorderLayout(0, 10));
        col2.setOpaque(false);

        JLabel lblBolsa = new JLabel("Bolsa", SwingConstants.CENTER);
        lblBolsa.setFont(new Font("Arial", Font.BOLD, 18));
        lblBolsa.setForeground(FG);
        lblBolsa.setBorder(new EmptyBorder(0, 0, 5, 0));

        textArea2 = crearTextArea();
        JScrollPane scroll2 = new JScrollPane(textArea2);
        scroll2.setBorder(BorderFactory.createLineBorder(BORDE_COLOR, 2, true));

        col2.add(lblBolsa, BorderLayout.NORTH);
        col2.add(scroll2, BorderLayout.CENTER);

        columnPanel.add(col1);
        columnPanel.add(col2);

        JLabel instruccion = new JLabel(
                "<html><div style='text-align:center;'>" +
                        "<i>* Inserta el diccionario en orden alfabético y la bolsa en el formato correcto</i>" +
                        "</div></html>");
        instruccion.setFont(new Font("Arial", Font.ITALIC, 14));
        instruccion.setForeground(FG_SUB);
        instruccion.setHorizontalAlignment(SwingConstants.CENTER);
        instruccion.setBorder(new EmptyBorder(10, 0, 0, 0));

        contentPanel.add(columnPanel, BorderLayout.CENTER);
        contentPanel.add(instruccion, BorderLayout.SOUTH);

        RoundedPanel bottomPanel = new RoundedPanel();
        bottomPanel.setLayout(new BorderLayout(15, 0));
        bottomPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(200, 0, 0)); // Rojo para errores
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setVisible(false);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setOpaque(false);
        errorPanel.add(errorLabel);
        bottomPanel.add(errorPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel lblArchivos = new JLabel(
                "<html><div style='text-align:left;'>" +
                        "También puedes añadirlo a través de archivos.<br>" +
                        "Primero añade el Diccionario y después la Bolsa." +
                        "</div></html>");
        lblArchivos.setFont(new Font("Arial", Font.PLAIN, 14));
        lblArchivos.setForeground(FG);
        lblArchivos.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblArchivos.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnArchivo = createStylishButton("Añadir Diccionario a partir de archivo", AZUL_CLARO);
        btnArchivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnArchivo.setMaximumSize(new Dimension(350, 40));
        btnArchivo.setPreferredSize(new Dimension(350, 40));

        btnArchivoBolsa = createStylishButton("Añadir Bolsa a partir de archivo", AZUL_CLARO);
        btnArchivoBolsa.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnArchivoBolsa.setMaximumSize(new Dimension(350, 40));
        btnArchivoBolsa.setPreferredSize(new Dimension(350, 40));

        leftPanel.add(lblArchivos);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(btnArchivo);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(btnArchivoBolsa);

        // Botón aceptar a la derecha
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        btnAceptar = createStylishButton("Aceptar", LILA_CLARO);
        btnAceptar.setPreferredSize(new Dimension(160, 40));
        rightPanel.add(btnAceptar);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        JPanel centerWrapper = new JPanel(new BorderLayout(0, 15));
        centerWrapper.setOpaque(false);
        centerWrapper.add(idPanel, BorderLayout.NORTH);
        centerWrapper.add(contentPanel, BorderLayout.CENTER);
        centerWrapper.add(bottomPanel, BorderLayout.SOUTH);

        panelPrincipal.add(titlePanel, BorderLayout.NORTH);
        panelPrincipal.add(centerWrapper, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Crea un panel con el título estilizado.
     * Cada letra del título se muestra como una ficha de color alternado.
     *
     * @param titulo El título a mostrar en el panel.
     * @return JPanel con el título estilizado.
     */
    private JPanel crearPanelTitulo(String titulo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(APP_BG_COLOR);
        panel.setBorder(new EmptyBorder(5, 0, 15, 0));

        String[] letras = titulo.split("");
        Color[] colores = new Color[letras.length];

        for (int i = 0; i < letras.length; i++) {
            if (" ".equals(letras[i])) {
                colores[i] = APP_BG_COLOR;
            } else {
                switch (i % 6) {
                    case 0:
                        colores[i] = new Color(220, 130, 95);
                        break; // Naranja rojizo
                    case 1:
                        colores[i] = new Color(95, 170, 220);
                        break; // Azul claro
                    case 2:
                        colores[i] = new Color(220, 180, 95);
                        break; // Amarillo
                    case 3:
                        colores[i] = new Color(150, 220, 95);
                        break; // Verde
                    case 4:
                        colores[i] = new Color(180, 95, 220);
                        break; // Morado/Lila
                    case 5:
                        colores[i] = new Color(220, 95, 160);
                        break; // Rosa
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
     * Crea una etiqueta estilizada que representa una ficha de título.
     * La ficha tiene un fondo redondeado y un efecto de sombra.
     *
     * @param texto El texto a mostrar en la ficha.
     * @param color El color de fondo de la ficha.
     * @return JLabel con el texto estilizado.
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
        l.setFont(new Font("Arial Black", Font.BOLD, 20));
        l.setPreferredSize(new Dimension(28, 28));
        l.addMouseListener(new HoverEfectoTexto(l));
        return l;
    }

    /**
     * Crea un JTextField estilizado con bordes redondeados y fondo transparente.
     * Utiliza un renderizado suave para mejorar la apariencia.
     *
     * @param columns Número de columnas del campo de texto.
     * @return JTextField estilizado.
     */
    private JTextField crearTextField(int columns) {
        JTextField field = new JTextField(columns) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_COLOR, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        field.setOpaque(false);

        return field;
    }

    /**
     * Crea un JTextArea estilizado con bordes redondeados y fondo transparente.
     * Utiliza un renderizado suave para mejorar la apariencia.
     *
     * @return JTextArea estilizado.
     */
    private JTextArea crearTextArea() {
        JTextArea ta = new JTextArea(10, 20);
        ta.setFont(new Font("Consolas", Font.PLAIN, 14));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

        // Configuraciones para mejorar rendimiento con textos grandes
        Document doc = ta.getDocument();
        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).setDocumentFilter(new DocumentFilter() {
                // Implementación vacía que se puede extender si es necesario para optimizar la inserción de grandes cantidades de texto, para vamos a suponer que los diccionarios en manual serán cortos.
            });
        }

        ta.getDocument().putProperty("filterNewlines", Boolean.FALSE);
        ta.setDoubleBuffered(true);

        System.setProperty("awt.text.textComponentPool", "20");

        ta.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);
        ta.putClientProperty("i18n", Boolean.FALSE);

        ((AbstractDocument) ta.getDocument()).setAsynchronousLoadPriority(-1);

        // Configurar aspecto visual
        ta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        return ta;
    }

    /**
     * Crea un botón estilizado con un fondo redondeado y un efecto de hover.
     * Utiliza un renderizado suave para mejorar la apariencia.
     *
     * @param text      El texto del botón.
     * @param baseColor El color base del botón.
     * @return JButton estilizado.
     */
    private JButton createStylishButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Radio de las esquinas redondeadas
                int radius = 15;

                boolean isHovered = getModel().isRollover();
                boolean isPressed = getModel().isPressed();

                Color bg = isPressed ? baseColor.darker() : (isHovered ? baseColor.darker() : baseColor);

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
     * Clase interna para crear un panel con bordes redondeados y sombra.
     * Utiliza Graphics2D para dibujar el fondo, borde y sombra.
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
     * Clase interna para manejar el efecto de hover en las etiquetas del título.
     * Cambia el color del texto al pasar el ratón por encima.
     */
    private static class HoverEfectoTexto extends MouseAdapter {
        private final JLabel label;

        HoverEfectoTexto(JLabel l) {
            this.label = l;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            label.setForeground(new Color(255, 255, 200));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            label.setForeground(Color.WHITE);
        }
    }

    /**
     * Muestra un diálogo para seleccionar un archivo de texto.
     * Utiliza JFileChooser para permitir al usuario elegir un archivo con extensión .txt.
     *
     * @return Ruta absoluta del archivo seleccionado, o null si se cancela la selección.
     */
    public String elegirArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos TXT", "txt"));
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    /**
     * Permite añadir un ActionListener al botón "Añadir Diccionario".
     * Este botón se utiliza para cargar un Diccionario desde un archivo.
     *
     * @param l ActionListener que se ejecutará al hacer clic en el botón.
     */
    public void addAñadirListener(ActionListener l) {
        btnArchivo.addActionListener(l);
    }

    /**
     * Permite añadir un ActionListener al botón "Añadir Bolsa".
     * Este botón se utiliza para cargar una Bolsa desde un archivo.
     *
     * @param l ActionListener que se ejecutará al hacer clic en el botón.
     */
    public void addAñadirBolsaListener(ActionListener l) {
        btnArchivoBolsa.addActionListener(l);
    }

    /**
     * Permite añadir un ActionListener al botón "Aceptar".
     * Este botón se utiliza para confirmar la entrada del usuario.
     *
     * @param l ActionListener que se ejecutará al hacer clic en el botón.
     */
    public void aceptar(ActionListener l) {
        btnAceptar.addActionListener(l);
    }

    /**
     * Convierte el texto del JTextArea especificado en una lista de Strings.
     * Cada línea del JTextArea se convierte en un elemento de la lista.
     *
     * @param textArea 1 para Diccionario, 2 para Bolsa.
     * @return Lista de Strings con las líneas del JTextArea.
     */
    public List<String> textAreaToList(int textArea) {
        String text = textArea == 1 ? textArea1.getText() : textArea2.getText();

        if (text == null || text.isEmpty()) {
            return List.of(); // lista vacía si no hay nada
        }
        return Arrays.stream(text.split("\\R"))
                .collect(Collectors.toList());
    }

    /**
     * Verifica si el JTextArea especificado está vacío.
     *
     * @param textArea 1 para Diccionario, 2 para Bolsa.
     * @return true si el JTextArea está vacío o contiene solo espacios en blanco,
     *         false en caso contrario.
     */
    public boolean textAreaisEmpty(int textArea) {
        String text = (textArea == 1)
                ? textArea1.getText()
                : textArea2.getText();
        return text == null || text.trim().isEmpty();
    }

    /**
     * Llena un JTextArea con una lista de Strings, separando cada elemento por un
     * salto de línea.
     *
     * @param lines     Lista de Strings a mostrar en el JTextArea.
     * @param textArea  1 para Diccionario, 2 para Bolsa.
     */
    public void listToTextArea(List<String> lines, int textArea) {
        if (lines == null || lines.isEmpty()) {
            if (textArea == 1) {
                textArea1.setText("");
            } else {
                textArea2.setText("");
            }
            return;
        }
        JTextArea targetArea = (textArea == 1) ? textArea1 : textArea2;
        targetArea.setEditable(false); 

        Document doc = targetArea.getDocument();
        ((AbstractDocument) doc).setAsynchronousLoadPriority(-1);

        try {
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line).append('\n');
            }
            targetArea.setText(sb.toString());
            targetArea.setCaretPosition(0);
        } finally {
            targetArea.setEditable(true);
        }
    }

    /**
     * Obtiene el texto ingresado en el campo ID
     *
     * @return String con el ID
     */
    public String getID() {
        return txtID != null ? txtID.getText().trim() : "";
    }

    /**
     * Muestra un mensaje de error en la etiqueta de error.
     * El mensaje se oculta automáticamente después de 10 segundos.
     *
     * @param mensaje El mensaje de error a mostrar.
     */
    public void setError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);

        new Timer(10000, (e) -> {
            errorLabel.setVisible(false);
        }).start();

        revalidate();
        pack();
    }
}