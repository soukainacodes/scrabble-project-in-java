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

public class VistaExplorador extends JFrame {

    // Colores consistentes con las otras vistas
    private static final Color APP_BG_COLOR = new Color(242, 226, 177);  // Crema
    private static final Color FG = new Color(20, 40, 80);               // Texto oscuro
    private static final Color FG_SUB = new Color(80, 90, 100);          // Texto secundario
    private static final Color LILA_OSCURO = new Color(52, 28, 87);      // Lila oscuro
    private static final Color LILA_CLARO = new Color(180, 95, 220);     // Lila claro
    private static final Color BORDE_COLOR = new Color(220, 200, 150);   // Borde de paneles
    private static final Color AZUL_CLARO = new Color(95, 170, 220);     // Azul claro para botones
    
    private JTextField txtID;
    private JLabel labelID;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton btnArchivo;
    private JButton btnArchivoBolsa;
    private JButton btnAceptar;
    private JLabel errorLabel;
        

    public VistaExplorador(String label) {
        setTitle("Explorador de Recursos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);


        
        // Panel principal con fondo nuevo
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 15));
        panelPrincipal.setBackground(APP_BG_COLOR);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel superior con título usando fichas de letras
        JPanel titlePanel = crearPanelTitulo("EXPLORADOR");
        
        // ===== Panel de ID con estilo mejorado =====
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
        
        // ===== Centro: panel principal con las columnas =====
        RoundedPanel contentPanel = new RoundedPanel();
        contentPanel.setLayout(new BorderLayout(0, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel de columnas
        JPanel columnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        columnPanel.setOpaque(false);
        
        // Primera columna: "Diccionario"
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
        
        // Segunda columna: "Bolsa"
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
        
        // Instrucciones
        JLabel instruccion = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<i>* Inserta el diccionario en orden alfabético y la bolsa en el formato correcto</i>" +
            "</div></html>"
        );
        instruccion.setFont(new Font("Arial", Font.ITALIC, 14));
        instruccion.setForeground(FG_SUB);
        instruccion.setHorizontalAlignment(SwingConstants.CENTER);
        instruccion.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        contentPanel.add(columnPanel, BorderLayout.CENTER);
        contentPanel.add(instruccion, BorderLayout.SOUTH);
        
        // ===== Panel inferior con botones =====
        RoundedPanel bottomPanel = new RoundedPanel();
        bottomPanel.setLayout(new BorderLayout(15, 0));
        bottomPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        
        // Dentro del constructor, justo después de crear el panel de botones pero antes de añadirlo:
        // Después de crear bottomPanel pero antes de añadirlo al centerWrapper
        
        // Crear y configurar el label de error
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        errorLabel.setForeground(new Color(200, 0, 0));  // Rojo para errores
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setVisible(false);
        
        // Añadir el errorLabel en la parte superior del panel inferior
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setOpaque(false);
        errorPanel.add(errorLabel);
        bottomPanel.add(errorPanel, BorderLayout.NORTH);


        // Panel izquierdo con información y botones de archivo
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel lblArchivos = new JLabel(
            "<html><div style='text-align:left;'>" +
            "También puedes añadirlo a través de archivos.<br>" +
            "Primero añade el Diccionario y después la Bolsa." +
            "</div></html>"
        );
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
        
        // Montar los paneles principales
        JPanel centerWrapper = new JPanel(new BorderLayout(0, 15));
        centerWrapper.setOpaque(false);
        centerWrapper.add(idPanel, BorderLayout.NORTH);
        centerWrapper.add(contentPanel, BorderLayout.CENTER);
        centerWrapper.add(bottomPanel, BorderLayout.SOUTH);
        
        // Añadir los paneles al panel principal
        panelPrincipal.add(titlePanel, BorderLayout.NORTH);
        panelPrincipal.add(centerWrapper, BorderLayout.CENTER);
        
        setContentPane(panelPrincipal);
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
    
    private JTextField crearTextField(int columns) {
        JTextField field = new JTextField(columns) {
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
    
private JTextArea crearTextArea() {
    JTextArea ta = new JTextArea(10, 20);
    ta.setFont(new Font("Consolas", Font.PLAIN, 14));
    ta.setLineWrap(true);
    ta.setWrapStyleWord(true);
    
    // Configuraciones para mejorar rendimiento con textos grandes
    Document doc = ta.getDocument();
    if (doc instanceof AbstractDocument) {
        ((AbstractDocument)doc).setDocumentFilter(new DocumentFilter() {
            // Implementación vacía que se puede extender si es necesario
            // para optimizar la inserción de grandes cantidades de texto
        });
    }
    
    // Optimizaciones de rendimiento para JTextArea
    ta.getDocument().putProperty("filterNewlines", Boolean.FALSE);
    ta.setDoubleBuffered(true);
    
    // Aumentar el tamaño del buffer interno para manejar textos grandes
    System.setProperty("awt.text.textComponentPool", "20");
    
    // Desactivar validación en tiempo real para mejorar rendimiento
    ta.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);
    ta.putClientProperty("i18n", Boolean.FALSE);
    
    // Reducir actualizaciones de UI durante cambios de texto
    ((AbstractDocument)ta.getDocument()).setAsynchronousLoadPriority(-1);
    
    // Configurar aspecto visual
    ta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    
    return ta;
}
    
    private JButton createStylishButton(String text, Color baseColor) {
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
                Color bg = isPressed ? baseColor.darker() : (isHovered ? baseColor.darker() : baseColor);
                
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
     * Permite añadir un ActionListener al botón "Añadir Diccionario"
     */
    public void addAñadirListener(ActionListener l) {
        btnArchivo.addActionListener(l);
    }

    /**
     * Permite añadir un ActionListener al botón "Añadir Bolsa"
     */
    public void addAñadirBolsaListener(ActionListener l) {
        btnArchivoBolsa.addActionListener(l);
    }

    /**
     * Permite añadir un ActionListener al botón "Aceptar"
     */
    public void aceptar(ActionListener l) {
        btnAceptar.addActionListener(l);
    }

    /**
     * Lee todo el texto de un JTextArea y lo convierte en una lista de Strings,
     * separando por cualquier tipo de salto de línea.
     *
     * @param textArea el JTextArea de donde leer (1 para Diccionario, 2 para
     * Bolsa)
     * @return List<String> con cada línea como un elemento de la lista
     */
    public List<String> textAreaToList(int textArea) {
        String text = textArea == 1 ? textArea1.getText() : textArea2.getText();

        if (text == null || text.isEmpty()) {
            return List.of();  // lista vacía si no hay nada
        }
        return Arrays.stream(text.split("\\R"))
                .collect(Collectors.toList());
    }

    public boolean textAreaisEmpty(int textArea) {
        String text = (textArea == 1)
                ? textArea1.getText()
                : textArea2.getText();
        return text == null || text.trim().isEmpty();
    }

public void listToTextArea(List<String> lines, int textArea) {
        // Si la lista es nula o está vacía, borramos el contenido
        if (lines == null || lines.isEmpty()) {
            if (textArea == 1) {
                textArea1.setText("");
            } else {
                textArea2.setText("");
            }
            return;
        }
    
        JTextArea targetArea = (textArea == 1) ? textArea1 : textArea2;
    
        // Optimizaciones para mejorar rendimiento con textos grandes
        targetArea.setEditable(false);  // Temporalmente desactivar edición
        
        // Desactivar actualizaciones de interfaz durante la carga
        Document doc = targetArea.getDocument();
        ((AbstractDocument)doc).setAsynchronousLoadPriority(-1);
        
        try {
            // Construir el texto de manera eficiente
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line).append('\n');
            }
            
            // Establecer todo el texto de una vez
            targetArea.setText(sb.toString());
            
            // Mover al inicio
            targetArea.setCaretPosition(0);
        } finally {
            // Restaurar edición
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
     * Muestra un mensaje de error en la interfaz
     * @param mensaje El mensaje de error a mostrar
     */
    public void setError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
        
        // Hacer que el mensaje desaparezca después de 5 segundos
        new Timer(5000, (e) -> {
            errorLabel.setVisible(false);
        }).start();
        
        // Ajustar el tamaño de la ventana si es necesario
        revalidate();
        pack();
    }
}