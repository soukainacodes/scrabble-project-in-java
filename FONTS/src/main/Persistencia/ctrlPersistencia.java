// Persistencia/CtrlPersistencia.java
package Persistencia;

import java.io.*;
import java.security.cert.PKIXCertPathValidatorResult;
import java.util.*;

import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;

public class CtrlPersistencia {
    private static final String FILE_USUARIOS =
        "FONTS/src/main/Persistencia/Datos/usuarios.txt";

    private Map<String,Partida> listaPartidas;
    public CtrlPersistencia(){
        listaPartidas = new HashMap<>();
    } 
    /** Lee todos los jugadores de usuarios.txt */
    public Map<String, Jugador> cargarUsuarios() {
        Map<String, Jugador> mapa = new HashMap<>();
        File f = new File(FILE_USUARIOS);
        if (!f.exists()) return mapa;

        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String línea;
            while ((línea = r.readLine()) != null) {
                String[] t = línea.trim().split(" ");
                if (t.length < 3) continue;
                String nombre = t[0], pass = t[1];
                int puntos = Integer.parseInt(t[2]);
                Jugador j = new Jugador(nombre, pass);
                j.setPuntos(puntos);
                mapa.put(nombre, j);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("[Persistencia] Error al cargar usuarios: " + e.getMessage());
        }
        return mapa;
    }

    /** Guarda todos los jugadores en usuarios.txt */
    public void guardarUsuarios(Map<String, Jugador> mapa) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE_USUARIOS))) {
            for (Jugador j : mapa.values()) {
                w.write(j.getNombre() + " " + j.getPassword() + " " + j.getPuntos());
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[Persistencia] Error al guardar usuarios: " + e.getMessage());
        }
    }

    public void cargarPartida() {
         
    }

    public void guardarPartida(String id, Partida partida) {
        listaPartidas.put(id, partida);
    }
}
