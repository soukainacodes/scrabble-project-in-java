package Presentacion.Vistas;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Un JFrame que contiene un único botón para abrir el explorador de archivos.
 */
public class VistaExplorador extends JFrame {

    public VistaExplorador() {
        setTitle("Explorador de Archivos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 248, 230));
        setLayout(new GridBagLayout());

        JButton btnAbrirExplorador = new JButton("Abrir Archivo…");
        btnAbrirExplorador.setFont(new Font("Arial", Font.PLAIN, 16));
        btnAbrirExplorador.setPreferredSize(new Dimension(160, 40));
        btnAbrirExplorador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Selecciona un archivo");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt")
                );

                int resultado = chooser.showOpenDialog(VistaExplorador.this);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    File archivo = chooser.getSelectedFile();
                    System.out.println("Archivo elegido: " + archivo.getAbsolutePath());
                }
            }
        });

        add(btnAbrirExplorador);
    }

    public void mostrar() {
        setVisible(true);
    }

    // Para prueba rápida
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaExplorador ve = new VistaExplorador();
            ve.mostrar();
        });
    }
}
