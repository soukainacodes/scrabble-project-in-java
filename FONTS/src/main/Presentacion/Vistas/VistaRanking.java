package Presentacion.Vistas;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaRanking extends JPanel {
        private static final int CONTENT_WIDTH = 360;
    private static final Color BG = new Color(238,238,238,255);

    public VistaRanking() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Wrapper para centrar y fijar ancho máximo
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);

        // Panel de contenido con BorderLayout
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);
        content.setMaximumSize(new Dimension(CONTENT_WIDTH, Integer.MAX_VALUE));

        // Modelo de tabla con dos columnas
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {
                // filas de ejemplo, puedes dejar vacío
                {"Alice",   120},
                {"Bob",     95 },
                {"Charlie", 110},
            },
            new String[] {"Nombre", "Puntuación máxima"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // celdas no editables
            }
        };

        // Crear JTable
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Scroll pane
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(CONTENT_WIDTH, 200));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));

        // Añadir al content y wrapper
        content.add(scroll, BorderLayout.CENTER);
        wrapper.add(content);
        add(wrapper, BorderLayout.CENTER);

        
    }

}