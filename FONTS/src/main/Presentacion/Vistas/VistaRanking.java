package Presentacion.Vistas;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
public class VistaRanking extends JPanel {
        private static final int CONTENT_WIDTH = 360;
        private static final Color BG = new Color(238,238,238,255);
    private DefaultTableModel model;
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
        // Modelo de tabla con dos columnas
         model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
        // Si quieres que todas las celdas sean editables:
        return true;
        // O bien, por ejemplo, sólo la columna de puntuación:
        // return column == 1;
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

    public void setLista(List<Map.Entry<String, Integer>> list){

        for (int i = 0; i < list.size(); i++) {
            var e = list.get(i);
    model.addRow(new Object[]{ e.getKey(), e.getValue() });
}
    }

}