package Presentacion;

import Dominio.CtrlDominio;
import Dominio.Excepciones.*;
import Presentacion.Vistas.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CtrlPresentacion {

    private CtrlDominio ctrlDominio = new CtrlDominio();
    // Añadir después de las declaraciones de vistas
    private Image aplicarIconoVentana;

    // Vistas
    private VistaLogin vLogin;
    private VistaMenuPrincipal vMenuPrincipal;
    private VistaMenuLateral vMenuLateral;
    private VistaPantallaPrincipal vPantallaPrincipal;
    private VistaRanking vRanking;
    private VistaCuenta vCuenta;
    private VistaRecursos vRecursos;

    private VistaCargarPartida vCargarPartida;
    private VistaCrearPartida vCrearPartida;
    private VistaScrabble vScrabble;

    private VistaPassword vPassword;
    private VistaCambiar vCambiar;
    private VistaExplorador vAddRecurso;
    private VistaLogin vSegundoJugador;

    private String nombreSegundoJugador = "";
    public CtrlPresentacion() throws IOException {
        configuracion();
        crearVistaLogin();
        aplicarIconos();
    }

    private void crearVistaLogin() {
        vLogin = new VistaLogin();
        // Aplicar icono inmediatamente después de crear la ventana de login
    if (aplicarIconoVentana != null) {
        List<Image> iconos = new ArrayList<>();
        int[] tamaños = {16, 24, 32, 48, 64, 128};
        
        for (int tamaño : tamaños) {
            Image imagenEscalada = aplicarIconoVentana.getScaledInstance(
                tamaño, tamaño, Image.SCALE_SMOOTH);
            iconos.add(imagenEscalada);
        }
        
        vLogin.setIconImages(iconos);
    }
        if (!ctrlDominio.haySesion()) {
            vLogin.entrar(e -> crearVistaMenuPrincipal());
        }
    }

    private void crearVistaMenuPrincipal() {

        String usuario = vLogin.getNombre();
        String password = new String(vLogin.getPassword());

        try {
            if (vLogin.getSeleccionado()) {
                ctrlDominio.iniciarSesion(usuario, password);
            } else {
                ctrlDominio.registrarJugador(usuario, password);
                ctrlDominio.iniciarSesion(usuario, password);
            }

            vMenuPrincipal = new VistaMenuPrincipal();
            crearVistaMenuLateral();
            crearVistaPantallaPrincipal();
            vLogin.revalidate();
            vLogin.repaint();
            vLogin.setContentPane(vMenuPrincipal);
            vLogin.setLocationRelativeTo(null);
            vLogin.pack();

        } catch (Exception e) {
            // System.err.println("Error: " + e.getMessage());
            vLogin.setError(e.getMessage());
        }

    }

    // VISTAS
    private void crearVistaMenuLateral() {
        vMenuLateral = new VistaMenuLateral();
        vMenuPrincipal.addMenuLateral(vMenuLateral);

        vPantallaPrincipal = new VistaPantallaPrincipal();
        vPantallaPrincipal.setNombre(ctrlDominio.getUsuarioActual());
        try {
            vCuenta = new VistaCuenta();

        } catch (Exception e) {

        }

        vRanking = new VistaRanking();
        vRecursos = new VistaRecursos();

        // Esto no se si deberia ir aqui
        vCargarPartida = new VistaCargarPartida();

        vCrearPartida = new VistaCrearPartida();

        vMenuPrincipal.addCard("PRINCIPAL", vPantallaPrincipal);
        vMenuPrincipal.addCard("CUENTA", vCuenta);
        vMenuPrincipal.addCard("RANKING", vRanking);
        vMenuPrincipal.addCard("RECURSOS", vRecursos);
        vMenuPrincipal.addCard("CARGARPARTIDA", vCargarPartida);

        vMenuPrincipal.addCard("CREARPARTIDA", vCrearPartida);

        vMenuLateral.addVerCuentaListener(e -> crearVistaCuenta());
        vMenuLateral.addJugarListener(e -> crearVistaPantallaPrincipal());

        vMenuLateral.addVistaRanking(e -> {
            try {
                crearVistaRanking();
            } catch (IOException ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        });

        vMenuLateral.addVistaRecursos(e -> {
            crearVistaRecursos();
        });

        vMenuLateral.addVistaRecursos(e -> vMenuPrincipal.muestraCard("RECURSOS"));
        vMenuLateral.cerrarSesion(e -> cerrarSesion());
        vPantallaPrincipal.addVistaCrearPartida(e -> crearVistaCrearPartida());
        vPantallaPrincipal.addVistaCargarPartida(e -> crearVistaCargarPartida());        // vPantallaPrincipal.jugarScrabble(e -> jugarPartida());

        vCargarPartida.jugarPartida(e -> jugarPartida());

    }

    private void cerrarSesion() {

        ctrlDominio.cerrarSesion();
        vLogin.dispose();
        crearVistaLogin();

    }

    private void crearPartida() {
        String id = vCrearPartida.getID();
        int modo = vCrearPartida.getModo();
        String idioma = vCrearPartida.getIdioma();
        String nombre = "";
        
       
        try {
            if(id != null && id != "" ){
                 long seed = new Random().nextLong();
                ctrlDominio.iniciarPartida(id, idioma, seed ,true);
            }
             
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
      
    }

    private void jugarPartida() {
        vScrabble = new VistaScrabble();
        vMenuPrincipal.jugarPartida(vScrabble);
    }

    private void crearVistaPantallaPrincipal() {
        vPantallaPrincipal.setNombre(ctrlDominio.getUsuarioActual());
        vMenuPrincipal.muestraCard("PRINCIPAL");
    }
    // Vistas del menu principal

    private void crearVistaCrearPartida() {

        vCrearPartida.jugarPartida(e -> crearPartida());
        vCrearPartida.addSegundoJugador(e -> crearVistaSegundoJugador());
        List<String> ids = ctrlDominio.obtenerRecursos();
        String[] idiomas = ids.toArray(new String[0]);
        vCrearPartida.setIdiomas(idiomas);
        vMenuPrincipal.muestraCard("CREARPARTIDA");

    }

    private void crearVistaCargarPartida() {
        // Obtener la lista de partidas guardadas del usuario actual
        List<String> partidasGuardadas = ctrlDominio.obtenerNombresPartidasGuardadas();
        
        // Cargar la lista en la vista
        vCargarPartida.setPartidas(partidasGuardadas);
        
        // Configurar los listeners de botones
        vCargarPartida.jugarPartida(e -> cargarPartidaSeleccionada());
        vCargarPartida.eliminarPartida(e -> eliminarPartidaSeleccionada());
        
        // Mostrar la vista
        vMenuPrincipal.muestraCard("CARGARPARTIDA");
    }
    
    private void cargarPartidaSeleccionada() {
        String partidaSeleccionada = vCargarPartida.getSeleccionada();
        if (partidaSeleccionada != null) {
            try {
                ctrlDominio.cargarPartida(partidaSeleccionada);
                jugarPartida(); // Inicia la partida cargada
            } catch (Exception e) {
                System.err.println("Error al cargar la partida: " + e.getMessage());
                // Podrías mostrar un diálogo de error si lo deseas
            }
        }
    }
    
    private void eliminarPartidaSeleccionada() {
        String partidaSeleccionada = vCargarPartida.getSeleccionada();
        if (partidaSeleccionada != null) {
            try {
                ctrlDominio.eliminarPartidaGuardada(partidaSeleccionada);
                // Actualizar la lista después de eliminar
                vCargarPartida.removePartida(partidaSeleccionada);
            } catch (Exception e) {
                System.err.println("Error al eliminar la partida: " + e.getMessage());
                // Podrías mostrar un diálogo de error si lo deseas
            }
        }
    }

    private void crearVistaSegundoJugador() {
        vSegundoJugador = new VistaLogin();
        vSegundoJugador.entrar(e -> addSegundoJugador());
    }

    private void addSegundoJugador() {
        nombreSegundoJugador = vSegundoJugador.getNombre();
        String password = new String(vSegundoJugador.getPassword());
        try {
            if (vSegundoJugador.getSeleccionado()) {
                ctrlDominio.iniciarSesionSegundoJugador(nombreSegundoJugador, password);
            } else {
                ctrlDominio.registrarJugador(nombreSegundoJugador, password);
                ctrlDominio.iniciarSesionSegundoJugador(nombreSegundoJugador, password);
            }
            vSegundoJugador.dispose();
        } catch (Exception e) {
            // System.err.println("Error: " + e.getMessage());
            vSegundoJugador.setError(e.getMessage());
        }

    }

        private void crearVistaCuenta() {
            try {
                String username = ctrlDominio.getUsuarioActual();
                vCuenta.setNombre(username);
                
                try {
                    vCuenta.setPuntos(Integer.toString(ctrlDominio.getPuntosActual()));
                    System.out.println("Puntos cargados: " + ctrlDominio.getPuntosActual()); 
                } catch (UsuarioNoEncontradoException e) {
                    // Si hay error al obtener los puntos, mostrar 0
                    vCuenta.setPuntos("0");
                    System.out.println("Error al cargar puntos: " + e.getMessage());
                }
                
                try {
                    int posicion = ctrlDominio.obtenerPosicion(username);
                    // Asegurarse de que la posición siempre sea un valor positivo
                    if (posicion > 0) {
                        vCuenta.setPosicion(posicion);
                        System.out.println("Posición cargada: " + posicion);
                    } else {
                        System.out.println("Posición recibida inválida: " + posicion);
                        // Si el usuario aparece en ranking pero la posición es 0, mostrar 1
                        vCuenta.setPosicion(1); // Asignar la primera posición si hay confusión
                    }
                } catch (UsuarioNoEncontradoException | IOException e) {
                    // Si el usuario no está en el ranking, mostrar "Sin clasificar"
                    vCuenta.setPosicion(0);
                    System.out.println("Usuario no clasificado en ranking: " + e.getMessage());
                }
            
            // Cargar imagen de perfil si está disponible
            try {
                BufferedImage profileImage = ctrlDominio.getProfileImage(username);
                if (profileImage != null) {
                    vCuenta.setProfileImage(profileImage);
                }
            } catch (Exception ex) {
                // Ignorar silenciosamente si no hay imagen de perfil
            }
            
            // Agregar listener para cambios de imagen de perfil
            vCuenta.setProfileChangeListener(e -> {
                File selectedFile = (File) e.getSource();
                try {
                    BufferedImage newImage = ImageIO.read(selectedFile);
                    if (newImage != null) {
                        ctrlDominio.saveProfileImage(newImage);
                    }
                } catch (IOException ex) {
                    System.err.println("Error al guardar la imagen de perfil: " + ex.getMessage());
                }
            });
            
        } catch (Exception e) {
            // Capturar cualquier otro error general pero no mostrar mensajes repetidos
            System.err.println("Error general al cargar datos del perfil: " + e.getMessage());
        }
    
        // Añadir listeners para botones
        vCuenta.cambiarNombre(e -> crearVistaCambiarNombre());
        vCuenta.cambiarPassword(e -> crearVistaCambiarPassword());
        vCuenta.eliminarJugador(e -> crearVistaEliminarJugador());
    
        // Mostrar la vista de cuenta
        vMenuPrincipal.muestraCard("CUENTA");
    }

    private void crearVistaCambiarPassword() {
        vCambiar = new VistaCambiar("password");
        vCambiar.setVisible(true);

        vCambiar.cambiar(e -> cambiarPassword());
    }

    private void crearVistaEliminarJugador() {

        vPassword = new VistaPassword(ctrlDominio.getUsuarioActual());
        vPassword.setVisible(true);
        vPassword.verificar(e -> eliminarJugador());

    }

    private void crearVistaCambiarNombre() {
        vCambiar = new VistaCambiar("nombre");
        vCambiar.setVisible(true);
        vCambiar.setResizable(false);
        vCambiar.cambiar(e -> cambiarNombre());

    }

    private void cambiarPassword() {
        String passwordActual = new String(vCambiar.getPasswordActual());
        String passwordNueva = new String(vCambiar.getPassword());
        try {
            ctrlDominio.cambiarPassword(passwordActual, passwordNueva);
            vCambiar.dispose();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    private void cambiarNombre() {
        String nombre = new String(vCambiar.getNombre());
        String password = new String(vCambiar.getPassword());
        try {

            ctrlDominio.cambiarNombre(nombre, password);

            vCuenta.setNombre(ctrlDominio.getUsuarioActual());
            vCambiar.dispose();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    private void eliminarJugador() {
        String password = new String(vPassword.getPassword());
        try {
            ctrlDominio.eliminarUsuario(password);
            vPassword.dispose();
            cerrarSesion();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void crearVistaRanking() throws IOException {
        try {
            var list = ctrlDominio.obtenerRanking();
            for (int i = 0; i < list.size(); i++) {
                var e = list.get(i);

            }
            vRanking.setLista(list);

            vMenuPrincipal.muestraCard("RANKING");
        } catch (RankingVacioException e) {
            System.out.println("Ranking vacío.");
        }

    }

    private void crearVistaRecursos() {
        List<String> ids = ctrlDominio.obtenerRecursos();
        vRecursos.setLista(ids);
        vRecursos.eliminarRecurso(e -> eliminarRecurso());
        vRecursos.addRecurso(e -> crearVistaAddRecurso());
        vMenuPrincipal.muestraCard("RECURSOS");

    }

    private void crearVistaAddRecurso() {
        vAddRecurso = new VistaExplorador();
        vAddRecurso.addAñadirListener(e -> {
            String ruta = vAddRecurso.elegirArchivo();
            if (ruta != null) {
                // System.out.println("Archivo elegido: " + ruta);
                try {
                    if (vAddRecurso.textAreaisEmpty(1)) {
                        vAddRecurso.listToTextArea(leeTexto(ruta), 1);
                    } else {
                        vAddRecurso.listToTextArea(leeTexto(ruta), 2);
                    }

                } catch (IOException xe) {
                }

            }
            // System.out.println(rutas[0] + " " + rutas[1]);
        });

        vAddRecurso.aceptar(e -> addRecurso());
        vAddRecurso.setVisible(true);

    }

    private void addRecurso() {
        String id = vAddRecurso.getID();

        List<String> diccionario = vAddRecurso.textAreaToList(1);
        var bolsa = vAddRecurso.textAreaToList(2);
        try {
            ctrlDominio.crearRecurso(id, diccionario, bolsa);
        } catch (Exception e) {
        }

    }

    // Botones
    private void eliminarRecurso() {

        try {
            ctrlDominio.eliminarRecurso(vRecursos.getSeleccionado());
            vRecursos.removeLista(vRecursos.getSeleccionado());

        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

    }

    private static List<String> leeTexto(String ruta) throws IOException {
        var res = new ArrayList<String>();
        try (var br = new BufferedReader(new FileReader(ruta))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (!l.isBlank()) {
                    res.add(l.trim());
                }
            }
        }
        return res;
    }

    private void configuracion() {
        Font fuenteGlobal = new Font("Arial", Font.PLAIN, 24);
        UIManager.put("Button.font", fuenteGlobal);
        UIManager.put("Label.font", fuenteGlobal);
        UIManager.put("TextField.font", fuenteGlobal);
        UIManager.put("PasswordField.font", fuenteGlobal);
        UIManager.put("ComboBox.font", fuenteGlobal);
        UIManager.put("CheckBox.font", fuenteGlobal);
        UIManager.put("RadioButton.font", fuenteGlobal);
        UIManager.put("Menu.font", fuenteGlobal);
        UIManager.put("MenuItem.font", fuenteGlobal);
        // Se eliminaron las líneas duplicadas de configuración de fuentes

        // Establecer icono global de la aplicación
        try {
            // Cargar el icono de la aplicación
            ImageIcon icon = new ImageIcon("FONTS/src/main/Recursos/Imagenes/scrabble_logo.png");

            // Alternativa usando classpath si la anterior falla
            if (icon.getIconWidth() <= 0) {
                java.net.URL iconURL = getClass().getResource("/Recursos/Imagenes/scrabble_logo.png");
                if (iconURL != null) {
                    icon = new ImageIcon(iconURL);
                }
            }

            // Crear versiones del icono en múltiples tamaños
            if (icon.getIconWidth() > 0) {
                // Crear una lista de iconos en diferentes tamaños
                List<Image> iconos = new ArrayList<>();
                int[] tamaños = { 16, 24, 32, 48, 64, 128 };

                for (int tamaño : tamaños) {
                    Image imagenEscalada = icon.getImage().getScaledInstance(
                            tamaño, tamaño, Image.SCALE_SMOOTH);
                    iconos.add(imagenEscalada);
                }

                // Guardar la versión más grande para uso general
                aplicarIconoVentana = iconos.get(iconos.size() - 1);

            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono global: " + e.getMessage());
        }
    }

    // Añade este nuevo método
    private void aplicarIconos() {
        if (aplicarIconoVentana != null && vLogin != null) {
            try {
                // Crear una lista de iconos en diferentes tamaños para mejor visualización
                ImageIcon icon = new ImageIcon(aplicarIconoVentana);
                List<Image> iconos = new ArrayList<>();
                int[] tamaños = { 16, 24, 32, 48, 64, 128 };

                for (int tamaño : tamaños) {
                    Image imagenEscalada = icon.getImage().getScaledInstance(
                            tamaño, tamaño, Image.SCALE_SMOOTH);
                    iconos.add(imagenEscalada);
                }

                vLogin.setIconImages(iconos);
            } catch (Exception e) {
                System.err.println("Error al aplicar iconos: " + e.getMessage());
            }
        }
    }

     

}
