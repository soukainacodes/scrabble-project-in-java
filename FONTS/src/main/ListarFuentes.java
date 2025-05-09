package main;

import java.awt.GraphicsEnvironment;

public class ListarFuentes {
    public static void main(String[] args) {
        String[] fuentes = 
            GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        for (String f : fuentes) {
            System.out.println(f);
        }
    }
}
