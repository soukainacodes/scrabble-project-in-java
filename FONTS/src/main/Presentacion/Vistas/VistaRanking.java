package Presentacion.Vistas;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaRanking extends JPanel {

    private static final int CONTENT_WIDTH = 360;
    private static final Color BG = new Color(238, 238, 238, 255);
    private DefaultTableModel model;

    private JTable table;

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
        String[] columnNames = {"Nombre", "Puntuación máxima"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Crear JTable
        JTable table = new JTable(model);
        
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Scroll pane
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(CONTENT_WIDTH, 200));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(238, 238, 238, 255), 1));

        // Añadir al content y wrapper
        content.add(scroll, BorderLayout.CENTER);
        wrapper.add(content);
        add(wrapper, BorderLayout.CENTER);

    }

    public void setLista(List<Map.Entry<String, Integer>> list) {
        model.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            var e = list.get(i);
            model.addRow(new Object[]{e.getKey(), e.getValue()});
        }
    }

}
