package Presentacion.Vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VistaExplorador extends JFrame {

    private static final Color BG = new Color(242, 226, 177);
    private static final Color FG_TITLE = new Color(40, 50, 60);
    private static final Color FG_SUB = new Color(80, 90, 100);
    private static final Color BORDER = new Color(220, 220, 220);

    private JTextField txtID;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton btnArchivo;
    private JButton btnAceptar;

    public VistaExplorador() {
        setTitle("Mi Ventana de Recursos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        setSize(800, 550);
        setLocationRelativeTo(null);

        // ===== Norte: etiqueta ID y campo de texto =====
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTop.setBackground(BG);
        JLabel lblID = new JLabel("ID:");
        lblID.setFont(new Font("Arial", Font.BOLD, 14));
        lblID.setForeground(FG_TITLE);
        txtID = new JTextField(10);
        txtID.setFont(new Font("Arial", Font.PLAIN, 14));
        txtID.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        panelTop.add(lblID);
        panelTop.add(txtID);
        add(panelTop, BorderLayout.NORTH);

        // ===== Centro: dos columnas con label + textArea cada una =====
        JPanel panelColumns = new JPanel(new GridLayout(1, 2, 10, 0));
        panelColumns.setBackground(BG);

        // Primera columna: "Diccionario"
        JPanel col1 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
        col1.setBackground(BG);
        JLabel lblDic = new JLabel("Diccionario");
        lblDic.setFont(new Font("Arial", Font.BOLD, 16));
        lblDic.setForeground(FG_TITLE);
        lblDic.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea1 = crearTextArea();
        col1.add(lblDic);
        col1.add(Box.createVerticalStrut(4));
        col1.add(new JScrollPane(textArea1));

        // Segunda columna: "Bolsa"
        JPanel col2 = new JPanel();
        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));
        col2.setBackground(BG);
        JLabel lblBolsa = new JLabel("Bolsa");
        lblBolsa.setFont(new Font("Arial", Font.BOLD, 16));
        lblBolsa.setForeground(FG_TITLE);
        lblBolsa.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea2 = crearTextArea();
        col2.add(lblBolsa);
        col2.add(Box.createVerticalStrut(4));
        col2.add(new JScrollPane(textArea2));

        panelColumns.add(col1);
        panelColumns.add(col2);

        // Wrapper central con instrucción
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(BG);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        centerWrapper.add(panelColumns);
        centerWrapper.add(Box.createVerticalStrut(10));

        JLabel instruccion = new JLabel("* Inserta el diccionario en orden alfabetico y la bolsa en el formato correcto");
        instruccion.setFont(new Font("Arial", Font.ITALIC, 12));
        instruccion.setForeground(FG_SUB);
        instruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(instruccion);

        add(centerWrapper, BorderLayout.CENTER);

        // ===== Sur: etiqueta + botón Añadir a la izquierda, Aceptar a la derecha =====
        btnArchivo = crearBotonBlanco("Añadir Archivo");
        btnAceptar = crearBotonBlanco("Aceptar");

        // Panel izquierdo con label encima del botón Añadir
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBackground(BG);
        JLabel lblAñadirInfo = new JLabel(
                "<html><center>También puedes añadirlo a través de archivos.<br>"
                + "Primero añade el Diccionario y después la Bolsa.</center></html>"
        );
        lblAñadirInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAñadirInfo.setForeground(FG_SUB);
        lblAñadirInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(lblAñadirInfo);
        westPanel.add(Box.createVerticalStrut(6));
        westPanel.add(btnArchivo);

        // Panel sur principal
        JPanel panelSouth = new JPanel(new BorderLayout());
        panelSouth.setBackground(BG);
        panelSouth.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelSouth.add(westPanel, BorderLayout.WEST);
        panelSouth.add(btnAceptar, BorderLayout.EAST);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private JTextArea crearTextArea() {
        JTextArea ta = new JTextArea();
        ta.setFont(new Font("Arial", Font.PLAIN, 14));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return ta;
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

    private JButton crearBotonBlanco(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(FG_TITLE);
        boton.setBackground(Color.WHITE);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createLineBorder(BORDER, 2));
        boton.setPreferredSize(new Dimension(160, 40));
        return boton;
    }

    /**
     * Permite añadir un ActionListener al botón “Añadir Archivo”
     */
    public void addAñadirListener(ActionListener l) {
        btnArchivo.addActionListener(l);
    }

    /**
     * Permite añadir un ActionListener al botón “Aceptar”
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

        // Unimos las líneas usando el separador de línea del sistema
        String text = String.join(System.lineSeparator(), lines);

        // Asignamos el texto al área correspondiente
        if (textArea == 1) {
            textArea1.setText(text);
        } else {
            textArea2.setText(text);
        }
    }

    /**
     * Obtiene el texto ingresado en el campo ID
     *
     * @return String con el ID
     */
    public String getID() {
        return txtID.getText().trim();
    }
}
