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
    private VistaInicio vLogin;
    private VistaPrincipal vMenuPrincipal;
    private VistaMenuLateral vMenuLateral;
    private VistaMenuPartidas vPantallaPrincipal;
    private VistaRanking vRanking;
    private VistaCuenta vCuenta;
    private VistaRecursos vRecursos;
    private VistaSalirPartida vSalir;
    private VistaManual vManual;

    private VistaCargarPartida vCargarPartida;
    private VistaCrearPartida vCrearPartida;
    private VistaJuego vScrabble;

    private VistaEliminarJugador vPassword;
    private VistaCambiarDatos vCambiar;
    private VistaGestionRecursos vAddRecurso;
    private VistaInicio vSegundoJugador;
    private VistaResetFichas vFichas;
    private VistaLetraComodin vLetra;
    private String nombreSegundoJugador = "";

    public CtrlPresentacion() throws IOException {
        configuracion();
        crearVistaLogin();
        aplicarIconos();
    }

    private void crearVistaLogin() {
        if (vLogin == null || !vLogin.isDisplayable()) {
            vLogin = new VistaInicio();
        }
        
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
            vLogin.entrar(e -> iniciarSesion());
        }
    }

    private void iniciarSesion() {
        String usuario = vLogin.getNombre();
        String password = new String(vLogin.getPassword());

        try {
            if (vLogin.getSeleccionado()) {
                ctrlDominio.iniciarSesion(usuario, password);
            } else {
                ctrlDominio.registrarJugador(usuario, password);
                ctrlDominio.iniciarSesion(usuario, password);
            }
            crearVistaMenuPrincipal();
        } catch (Exception e) {
            vLogin.setError(e.getMessage());
        }
    }

    private void crearVistaMenuPrincipal() {

        vMenuPrincipal = new VistaPrincipal();
        crearVistaMenuLateral();
        crearVistaPantallaPrincipal();
        vLogin.revalidate();
        vLogin.repaint();
        vLogin.setContentPane(vMenuPrincipal);
        vLogin.setLocationRelativeTo(null);

        //vLogin.setSize(1280, 720);
        vLogin.pack();

    }

    // VISTAS
    private void crearVistaMenuLateral() {
        vMenuLateral = new VistaMenuLateral();
        vMenuPrincipal.addMenuLateral(vMenuLateral);

        vPantallaPrincipal = new VistaMenuPartidas();
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
        // Dentro del método crearVistaMenuLateral(), añade esta línea junto con los otros listeners
        vMenuLateral.addVistaManual(e -> crearVistaManual());

        vMenuLateral.addVistaRecursos(e -> vMenuPrincipal.muestraCard("RECURSOS"));
        vMenuLateral.cerrarSesion(e -> cerrarSesion());

        vCargarPartida.jugarPartida(e -> cargarPartida());
        vPantallaPrincipal.addVistaCrearPartida(e -> crearVistaCrearPartida());
        vPantallaPrincipal.addVistaCargarPartida(e -> crearVistaCargarPartida());
    }

    private void cargarPartida() {
        String partida = vCargarPartida.getSeleccionada();
        try {
            ctrlDominio.cargarPartida(partida);
            jugarPartida();
        } catch (Exception e) {
            vCargarPartida.setError(e.getMessage());
        }

    }

    private void cargarUltimaPartida() {
        try {
            ctrlDominio.cargarUltimaPartida();
            jugarPartida();
        } catch (Exception e) {

            vPantallaPrincipal.setError(e.getMessage());
        }

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

        try {
            if (id != null && id != "") {
                if (modo == 1 && nombreSegundoJugador.equals("")) {
                    System.out.println("ERROR: ");
                } else {
                    long seed = new Random().nextLong();

                    ctrlDominio.iniciarPartida(id, nombreSegundoJugador, idioma, seed, true);

                    jugarPartida();
                }

            }

        } catch (Exception e) {
            System.out.println("ERRORRRR: " + e.getMessage());
        }

    }

    private void jugarPartida() {
        try {
            vScrabble = new VistaJuego(ctrlDominio.getUsuarioActual(), ctrlDominio.getSegundoJugador());
        } catch (Exception e) {
            System.err.println("Error al crear la vista de Scrabble: " + e.getMessage());
        }

        vLogin.setSize(1280, 800);
        vScrabble.setPuntos(ctrlDominio.getPuntosJugador1(), ctrlDominio.getPuntosJugador2());

        int N = ctrlDominio.getTableroDimension();
        for (int fila = 0; fila < N; ++fila) {
            for (int col = 0; col < N; col++) {

                String b = ctrlDominio.getBonusCelda(fila, col);
                vScrabble.configurarTablero(b, fila, col);
            }
        }

        vScrabble.setTileActionListener(new VistaJuego.TileActionListener() {
            boolean cola = false;
            int row_cola = -1;
            int col_cola = -1;
            String letra_cola = "";

            @Override
            public void onTilePlaced(String letter, int score, int row, int col) {
                // Handle tile placement in the controller
                if (letter.equals("#")) {
                    if (vLetra == null || !vLetra.isDisplayable()) {
                        vLetra = new VistaLetraComodin();
                    }

                    vLetra.setVisible(true);
                    vLetra.aceptar(e -> ponerComodin(row, col));

                } else {
                    System.out.println("Aqui no se deberia ejecutar 1");
                    ponerFicha(letter, score, row, col);
                }

                if (ctrlDominio.getLetraCelda(row, col) == null) {
                    cola = true;
                    row_cola = row;
                    col_cola = col;
                    letra_cola = letter;

                }

            }

            @Override
            public void onTileRemoved(String letter, int score, int row, int col) {
                // Handle tile removal in the controller

                quitarFicha(letter, score, row, col);
                if (cola && letra_cola.equals(letter)) {

                    if (letra_cola.equals("#")) {
                        ponerFicha(vLetra.getTexto(), score, row_cola, col_cola);
                    } else {
                        System.out.println("Aqui no se deberia ejecutar 2");
                        ponerFicha(letter, score, row_cola, col_cola);
                    }
                    cola = false;
                }
            }
        });

        vScrabble.crearTablero();
        // vScrabble.finTurno(e -> f
        vScrabble.pasar(e -> pasarTurno());
        vScrabble.finTurno(e -> finTurno());
        vScrabble.reset(e -> crearVistaFichas());
        vScrabble.salir(e -> crearVistaSalir());
        vScrabble.ayuda(e -> mostrarTablero());
        List<String> fichas = ctrlDominio.obtenerFichas();
        for (String ficha : fichas) {
            vScrabble.modificarRack(ficha);
        }
        actualizarTablero();
        // vMenuPrincipal.muestraCard("SCRABBLE");
        vMenuPrincipal.jugarPartida(vScrabble);
    }

    private void ponerComodin(int row, int col) {
        ponerFicha(vLetra.getTexto(), 0, row, col);
        vLetra.dispose();
    }

    private void mostrarTablero() {
        int N = ctrlDominio.getTableroDimension();
        System.out.print("    ");
        for (int j = 0; j < N; j++) {
            System.out.printf("%4d", j);
        }
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < N; j++) {
                String l = ctrlDominio.getLetraCelda(i, j);
                String b = ctrlDominio.getBonusCelda(i, j);
                System.out.printf("[%2s]", l != null ? l : b);
            }
            System.out.println();
        }
    System.out.println("Fichas: " + ctrlDominio.obtenerFichas());
    }

    private void ayuda() {
        try {
            ctrlDominio.jugarScrabble(7, "");
            actualizarTablero();
        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }

    }

    private void crearVistaFinal(boolean abandonada, int resultado) {
        String mensaje;

        String perdedor = resultado == 1 ? ctrlDominio.getUsuarioActual() : nombreSegundoJugador;
        String ganador = resultado == 1 ? nombreSegundoJugador : ctrlDominio.getUsuarioActual();
        nombreSegundoJugador = "";
        if (abandonada) {
            mensaje = "Jugador " + perdedor + " ha abandonado la partida. Jugador " + ganador + " gana.";
        } else {
            mensaje = "Fin de la partida. Jugador " + ganador + " ha ganado con " + resultado + " puntos.";
        }
        VistaFinPartida vFinal = new VistaFinPartida(mensaje);
        vFinal.setDuracion(3);
        vFinal.setLocationRelativeTo(null);
        vFinal.setResizable(false);
        
        vFinal.mostrar();
        crearVistaMenuPrincipal();

    }

    private void crearVistaSalir() {
        VistaSalirPartida vSalir = new VistaSalirPartida();
        vSalir.setVisible(true);
        vSalir.setLocationRelativeTo(null);
        vSalir.setResizable(false);

        vSalir.setAbandonarListener(e -> {
            try {
                nombreSegundoJugador = ctrlDominio.getSegundoJugador();
            
                crearVistaFinal(true, ctrlDominio.jugarScrabble(6, ""));
                
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
            vSalir.dispose();
        });

        vSalir.setSalirListener(e -> {
            try {
                ctrlDominio.salirPartida();
            } catch (Exception exe) {
            }
            vSalir.dispose();
            crearVistaMenuPrincipal();
            nombreSegundoJugador = "";
        });
    }

    private void crearVistaFichas() {
        try {
            vFichas = new VistaResetFichas();

            List<String> fichas = ctrlDominio.obtenerFichas();
            vFichas.cargarFichas(fichas);
            vFichas.setAcceptListener(e -> {
                String seleccionadas = vFichas.getSelectedTiles();
                // Process selected tiles
                try {
                    int resultado = ctrlDominio.jugarScrabble(5, seleccionadas);
                    if (resultado != 0) {
                        crearVistaFinal(false, resultado);
                    }
                    vFichas.dispose();
                    vScrabble.clearRack();
                    List<String> fichas2 = ctrlDominio.obtenerFichas();
                    for (String ficha : fichas2) {
                        vScrabble.modificarRack(ficha);
                    }
                    actualizarTablero();
                } catch (Exception ex) {
                    System.out.println("ERROR: " + ex.getMessage());
                    return;
                }

            });
            vFichas.setVisible(true);
        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }
    }

    private void ponerFicha(String letra, int puntuacion, int fila, int col) {
        System.out.println(letra);
        String parametros = letra + " " + Integer.toString(puntuacion) + " " + Integer.toString(fila) + " " + Integer.toString(col);

        try {
            //  String parametros = letra + " " + Integer.toString(fila) + " " + Integer.toString(col);
            if (letra != null && !letra.isEmpty() && !letra.equals("")) {
                ctrlDominio.jugarScrabble(1, parametros);
            }

            //actualizarTablero();
        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }
    }

    private void quitarFicha(String letra, int puntuacion, int fila, int col) {
        try {
           // System.out.println("Quitar ficha: ");
            String parametros = Integer.toString(fila) + " " + Integer.toString(col);
            ctrlDominio.jugarScrabble(2, parametros);
        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }
    }

    private void actualizarTablero() {
        int N = ctrlDominio.getTableroDimension();
        for (int fila = 0; fila < N; ++fila) {
            for (int col = 0; col < N; col++) {
                try {
                    String letra = ctrlDominio.getLetraCelda(fila, col);
                    int puntos = ctrlDominio.getPuntuacionCelda(fila, col);
                    vScrabble.ponerFichaTablero(letra, puntos, fila, col);
                } catch (Exception e) {
                }

            }

        }
        vScrabble.bloquearTodasLasFichas();
        vScrabble.clearRack();
        List<String> fichas = ctrlDominio.obtenerFichas();
        for (String ficha : fichas) {
            vScrabble.modificarRack(ficha);
        }
        //      System.out.println("Puntos: " + ctrlDominio.getPuntosJugador1() + " " + ctrlDominio.getPuntosJugador2());
        vScrabble.setPuntos(ctrlDominio.getPuntosJugador1(), ctrlDominio.getPuntosJugador2());
    }

    private void finTurno() {
        try {
            int resultado = ctrlDominio.jugarScrabble(4, "");
            if (resultado != 0) {
                crearVistaFinal(false, resultado);
            }
            actualizarTablero();
        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }
    }

    private void pasarTurno() {
        try {
            int resultado = ctrlDominio.jugarScrabble(3, "");
            if (resultado != 0) {
                crearVistaFinal(false, resultado);
            }
            actualizarTablero();

        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }

    }

    private void crearVistaPantallaPrincipal() {
        // vPantallaPrincipal.jugarScrabble(e -> jugarPartida());
        vPantallaPrincipal.cargarUltimaPartida(e -> cargarUltimaPartida());
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
        vSegundoJugador = new VistaInicio();
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
            nombreSegundoJugador = "";
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
                    // Si el usuario aparece en el ranking pero la posición es 0, mostrar 1
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
        vLogin.pack();
    }

    private void crearVistaCambiarPassword() {
        if (vCambiar == null || !vCambiar.isDisplayable()) {
            vCambiar = new VistaCambiarDatos("password");
            vCambiar.setLocationRelativeTo(null);
        }

        vCambiar.setVisible(true);
        vCambiar.setLocationRelativeTo(null);
        vCambiar.cambiar(e -> cambiarPassword());
    }

    private void crearVistaEliminarJugador() {
        if (vPassword == null || !vPassword.isDisplayable()) {
            vPassword = new VistaEliminarJugador(ctrlDominio.getUsuarioActual());
            vPassword.setLocationRelativeTo(null);
        }
        vPassword.setVisible(true);
        vPassword.setLocationRelativeTo(null);
        vPassword.setResizable(false);
        vPassword.verificar(e -> eliminarJugador());

    }

    private void crearVistaCambiarNombre() {
        if (vCambiar == null || !vCambiar.isDisplayable()) {
            vCambiar = new VistaCambiarDatos("nombre");
            vCambiar.setLocationRelativeTo(null);
        }

        vCambiar.setVisible(true);
        vCambiar.setResizable(false);
        vCambiar.cambiar(e -> cambiarNombre());

    }

    private void cambiarPassword() {
        String passwordActual = new String(vCambiar.getPasswordActual());
        String passwordNueva = new String(vCambiar.getPassword());
        String confirmPassword = new String(vCambiar.getConfirmPassword());

        // Verificar que las nuevas contraseñas coincidan
        if (!passwordNueva.equals(confirmPassword)) {
            vCambiar.setError("Las contraseñas nuevas no coinciden");
            return;
        }

        try {
            ctrlDominio.cambiarPassword(passwordActual, passwordNueva);
            vCambiar.dispose();
        } catch (Exception e) {
            vCambiar.setError(e.getMessage());
            vCambiar.setError(e.getMessage());
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void cambiarNombre() {
        String nombre = new String(vCambiar.getNombre());
        String password = new String(vCambiar.getPasswordActual());
        try {
            ctrlDominio.cambiarNombre(nombre, password);
            vCuenta.setNombre(ctrlDominio.getUsuarioActual());
            vCambiar.dispose();
        } catch (Exception e) {
            // Mostrar el error en la UI en lugar de en la consola
            vCambiar.setError(e.getMessage());
            System.err.println("Error: " + e.getMessage()); // Opcional: mantener también el log en consola
        }
    }

    private void eliminarJugador() {
        String password = new String(vPassword.getPassword());
        try {
            ctrlDominio.eliminarUsuario(password);
            vPassword.dispose();
            cerrarSesion();
        } catch (Exception e) {
            vPassword.setError("Error: " + e.getMessage());
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
            vLogin.pack();
        } catch (RankingVacioException e) {
            System.out.println("Ranking vacío.");
        }

    }

    private void crearVistaRecursos() {

        List<String> ids = ctrlDominio.obtenerRecursos();
        vRecursos.setLista(ids);
        vRecursos.eliminarRecurso(e -> eliminarRecurso());
        vRecursos.modificarRecurso(e -> crearVistaModificarRecurso());
        vRecursos.addRecurso(e -> crearVistaAddRecurso());
        vMenuPrincipal.muestraCard("RECURSOS");

    }

    private void crearVistaAddRecurso() {
        if (vAddRecurso == null || !vAddRecurso.isDisplayable()) {
            vAddRecurso = new VistaGestionRecursos("", "Añadir Recurso");

            // Listener para el botón de añadir diccionario
            vAddRecurso.addAñadirListener(e -> {
                String ruta = vAddRecurso.elegirArchivo();
                if (ruta != null) {
                    try {
                        vAddRecurso.listToTextArea(leeTexto(ruta), 1);
                    } catch (IOException xe) {
                        vAddRecurso.setError("Error al leer el archivo: " + xe.getMessage());
                    }
                }
            });

            // Añade este código para el botón de añadir bolsa
            vAddRecurso.addAñadirBolsaListener(e -> {
                String ruta = vAddRecurso.elegirArchivo();
                if (ruta != null) {
                    try {
                        vAddRecurso.listToTextArea(leeTexto(ruta), 2);
                    } catch (IOException xe) {
                        vAddRecurso.setError("Error al leer el archivo: " + xe.getMessage());
                    }
                }
            });

            vAddRecurso.aceptar(e -> addRecurso());
            vAddRecurso.setVisible(true);
        }
    }

    private void crearVistaModificarRecurso() {
        String idRecurso = vRecursos.getSeleccionado();
        if (vAddRecurso == null || !vAddRecurso.isDisplayable()) {
            vAddRecurso = new VistaGestionRecursos(idRecurso, "Modificar Recurso");

            try {
                // Cargar datos existentes...
                List<String> diccionario = ctrlDominio.obtenerDiccionario(idRecurso);
                vAddRecurso.listToTextArea(diccionario, 1);
                List<String> bolsa = ctrlDominio.obtenerBolsa(idRecurso);
                vAddRecurso.listToTextArea(bolsa, 2);
            } catch (Exception e) {
                System.err.println("Error al cargar el recurso: " + e.getMessage());
                e.printStackTrace();
            }

            // Listener para el botón de añadir diccionario
            vAddRecurso.addAñadirListener(e -> {
                String ruta = vAddRecurso.elegirArchivo();
                if (ruta != null) {
                    try {
                        vAddRecurso.listToTextArea(leeTexto(ruta), 1);
                    } catch (IOException xe) {
                        vAddRecurso.setError("Error al leer el archivo: " + xe.getMessage());
                    }
                }
            });

            // Añade este código para el botón de añadir bolsa
            vAddRecurso.addAñadirBolsaListener(e -> {
                String ruta = vAddRecurso.elegirArchivo();
                if (ruta != null) {
                    try {
                        vAddRecurso.listToTextArea(leeTexto(ruta), 2);
                    } catch (IOException xe) {
                        vAddRecurso.setError("Error al leer el archivo: " + xe.getMessage());
                    }
                }
            });

            vAddRecurso.aceptar(e -> modificarRecurso());
            vAddRecurso.setVisible(true);
        }
    }

    private void addRecurso() {
        String id = vAddRecurso.getID();

        // Verificar que el ID no esté vacío
        if (id == null || id.trim().isEmpty()) {
            vAddRecurso.setError("El ID no puede estar vacío");
            return;
        }

        List<String> diccionario = vAddRecurso.textAreaToList(1);
        List<String> bolsa = vAddRecurso.textAreaToList(2);

        // Verificar que no estén vacíos
        if (diccionario.isEmpty()) {
            vAddRecurso.setError("El diccionario no puede estar vacío");
            return;
        }

        if (bolsa.isEmpty()) {
            vAddRecurso.setError("La bolsa no puede estar vacía");
            return;
        }

        try {
            ctrlDominio.crearRecurso(id, diccionario, bolsa);
            vAddRecurso.dispose();  // Cerrar la ventana solo si todo fue exitoso
            crearVistaRecursos();   // Actualizar la lista de recursos
        } catch (RecursoExistenteException e) {
            vAddRecurso.setError("Ya existe un recurso con ID '" + id + "'");
        } catch (FormatoDiccionarioInvalidoException e) {
            vAddRecurso.setError("Formato de diccionario inválido. Verifica que las palabras estén en mayúsculas y ordenadas");
        } catch (FormatoBolsaInvalidoException e) {
            vAddRecurso.setError("Formato de bolsa inválido. Formato correcto: 'LETRA FRECUENCIA PUNTOS'");
        } catch (Exception e) {
            vAddRecurso.setError("Error: " + e.getMessage());
        }
    }

    private void modificarRecurso() {
        String id = vRecursos.getSeleccionado();
        List<String> diccionario = vAddRecurso.textAreaToList(1);
        List<String> bolsa = vAddRecurso.textAreaToList(2);

        // Verificar que no estén vacíos
        if (diccionario.isEmpty()) {
            vAddRecurso.setError("El diccionario no puede estar vacío");
            return;
        }

        if (bolsa.isEmpty()) {
            vAddRecurso.setError("La bolsa no puede estar vacía");
            return;
        }

        try {
            ctrlDominio.modificarRecurso(id, diccionario, bolsa);
            vAddRecurso.dispose();
            crearVistaRecursos(); // Recargar la vista de recursos
        } catch (FormatoDiccionarioInvalidoException e) {
            vAddRecurso.setError("Formato de diccionario inválido. Verifica que las palabras estén en mayúsculas y ordenadas");
        } catch (FormatoBolsaInvalidoException e) {
            vAddRecurso.setError("Formato de bolsa inválido. Formato correcto: 'LETRA FRECUENCIA PUNTOS'");
        } catch (Exception e) {
            vAddRecurso.setError("Error: " + e.getMessage());
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
                int[] tamaños = {16, 24, 32, 48, 64, 128};

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
                int[] tamaños = {16, 24, 32, 48, 64, 128};

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

    private void crearVistaManual() {
        if (vManual == null) {
            vManual = new VistaManual();
            vMenuPrincipal.addCard("MANUAL", vManual);
        }

        vMenuPrincipal.muestraCard("MANUAL");
    }

}
