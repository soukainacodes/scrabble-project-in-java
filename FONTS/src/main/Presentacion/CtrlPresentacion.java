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

/**
 * Controlador de presentación que gestiona la interacción entre la vista y el dominio.
 * Se encarga de crear y mostrar las diferentes vistas de la aplicación.
 */
public class CtrlPresentacion {

    /**
     * Controlador de dominio que gestiona la lógica del negocio.
     */
    private CtrlDominio ctrlDominio = new CtrlDominio();
    
    /**
     * Icono que se aplica a las ventanas de la aplicación.
     * Se carga desde un archivo de imagen.
     */
    private Image aplicarIconoVentana;

    /**
     * Vista de Login y Registro.
     */
    private VistaInicio vLogin;

    /**
     * Vista de configuración inicial de la aplicación.
     * Se utiliza para aplicar iconos y configuraciones de estilo.
     */
    private VistaPrincipal vMenuPrincipal;

    /**
     * Vista del menú lateral de la aplicación.
     */
    private VistaMenuLateral vMenuLateral;

    /**
     * Vista de las diferentes opciones del menú principal de Juego
     * Crear nueva partida, cargar partida.
     */
    private VistaMenuPartidas vPantallaPrincipal;
    
    /**
     * Vista del ranking de jugadores.
     */
    private VistaRanking vRanking;
    
    
    /**
     * Vista de la cuenta del jugador.
     * Permite ver los datos del jugador actual y opciones para modificarlos.
     */
    private VistaCuenta vCuenta;
    
    /**
     * Vista donde se listan todos los recursos y opciones para gestionar los recursos del juego.
     */
    private VistaRecursos vRecursos;
    
    /**
     * Vista para salir de una partida en curso.
     * Permite al usuario abandonar la partida o salir sin abandonarla.
     */
    private VistaSalirPartida vSalir;
    
    /**
     * Vista del manual del juego.
     * Se utiliza para mostrar las reglas y guías del juego Scrabble.
     */
    private VistaManual vManual;

    /**
     * Vista para cargar una partida guardada.
     * Se utiliza para seleccionar y cargar partidas previamente guardadas.
     */
    private VistaCargarPartida vCargarPartida;
    
    /**
     * Vista para crear una nueva partida.
     * Se utiliza para configurar los parámetros de la partida antes de iniciarla.
     */
    private VistaCrearPartida vCrearPartida;
    
    /**
     * Vista del juego Scrabble.
     * Se utiliza para gestionar la partida de Scrabble en curso.
     */
    private VistaJuego vScrabble;

    /**
     * Vista para eliminar un jugador.
     * Se utiliza para gestionar la eliminación de jugadores del sistema.
     */
    private VistaEliminarJugador vPassword;
    
    /**
     * Vista con para cambiar los datos del jugador.
     * Se utiliza para modificar el nombre, contraseña o imagen de perfil del jugador.
     */
    private VistaCambiarDatos vCambiar;
    
    /**
     * Vista con opciones para añadir un nuevo recurso, modificarlo o eliminarlo al juego.
     * Se utiliza para gestionar los recursos del juego.
     */
    private VistaGestionRecursos vAddRecurso;
    
    /**
     * Vista para el segundo jugador.
     * Se utiliza cuando se inicia una partida con dos jugadores.
     */
    private VistaInicio vSegundoJugador;
    
    /**
     * Vista para cambiar las fichas del jugador.
     * Se utiliza cuando el jugador quiere cambiar sus fichas actuales.
     */
    private VistaResetFichas vFichas;
    
    /**
     * Vista para seleccionar una letra comodín.
     * Se utiliza cuando el jugador coloca un comodín en el tablero.
     */
    private VistaLetraComodin vLetra;
    
    /**
     * Incializa el nombre del segundo jugador con un valor vacío.
     */
    private String nombreSegundoJugador = "";

    /**
     * Constructor de CtrlPresentacion.
     * Inicializa la configuración de la aplicación, crea la vista de Login y aplica los iconos.
     * @throws IOException Si ocurre un error al cargar los iconos o configuraciones.
     */
    public CtrlPresentacion() throws IOException {
        configuracion();
        crearVistaLogin();
        aplicarIconos();
    }


    /**
     * Crea la primera vista de la aplicación, que es la de Login.
     */
    private void crearVistaLogin() {
        if (vLogin == null || !vLogin.isDisplayable()) {
            vLogin = new VistaInicio();
        }

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

    /**
     * Función para iniciar sesión o registrar un nuevo jugador.
     */
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



    
    /**
     * Crear vista del menú principal de la aplicación.
     * Esta vista es la pantalla principal que se muestra al usuario después de iniciar sesión.
     * Se ven las opciones de Crear nueva partida, cargar útlima partida y cargar partida guardada.
     * 
     * Es la misma vista que se muestra al darle clic a "Jugar" en el menú lateral.
     */
    private void crearVistaMenuPrincipal() {

        vMenuPrincipal = new VistaPrincipal();
        crearVistaMenuLateral();
        crearVistaPantallaPrincipal();
        vLogin.revalidate();
        vLogin.repaint();
        vLogin.setContentPane(vMenuPrincipal);
        vLogin.setLocationRelativeTo(null);

        vLogin.pack();

    }

    /**
     * Crea la vista del menú lateral de la aplicación.
     * Esta vista contiene opciones para acceder a diferentes secciones de la aplicación,
     * como la cuenta del usuario, el ranking, los recursos,
     * Además, permite al usuario cerrar sesión y acceder a la pantalla principal del juego.
     */
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
        vMenuLateral.addVistaManual(e -> crearVistaManual());

        vMenuLateral.addVistaRecursos(e -> vMenuPrincipal.muestraCard("RECURSOS"));
        vMenuLateral.cerrarSesion(e -> cerrarSesion());

        vCargarPartida.jugarPartida(e -> cargarPartida());
        vPantallaPrincipal.addVistaCrearPartida(e -> crearVistaCrearPartida());
        vPantallaPrincipal.addVistaCargarPartida(e -> crearVistaCargarPartida());
    }

    /**
     * Cargar una partida guardada seleccionada por el usuario.
     */
    private void cargarPartida() {
        String partida = vCargarPartida.getSeleccionada();
        try {
            ctrlDominio.cargarPartida(partida);
            System.out.println(ctrlDominio.getSegundoJugador());
            System.out.println("nombre " + nombreSegundoJugador);
            if(ctrlDominio.getSegundoJugador() == "propAI"){
                 jugarPartida();
            }
            else {
                vSegundoJugador = new VistaInicio();
                vSegundoJugador.entrar(e -> addSegundoJugador(true));
            }
        } catch (Exception e) {
            vCargarPartida.setError(e.getMessage());
        }

    }

    /**
     * Método para cargar la última partida guardada del juagador actual.
     */
    private void cargarUltimaPartida() {
        
        try {
            ctrlDominio.cargarUltimaPartida();
            if (ctrlDominio.getSegundoJugador().equals("propAI")) {
               jugarPartida();
            } else {
                vSegundoJugador = new VistaInicio();
                vSegundoJugador.entrar(e -> addSegundoJugador(true));
            }       
        } catch (Exception e) {

            vPantallaPrincipal.setError(e.getMessage());
        }

    }

    /**
     * Método para cerrar sesión del usuario actual.
     */
    private void cerrarSesion() {

        ctrlDominio.cerrarSesion();
        vLogin.dispose();
        crearVistaLogin();

    }

    /**
     * Crear una nueva partida de Scrabble.
     * Este método se llama cuando el usuario ha configurado los parámetros de la partida
     */
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

    /**
     * Inicializa una partida de Scrabble.
     * Crea la vista del juego y configura el tablero y las fichas.
     * * Si el segundo jugador es una IA, se inicia la partida directamente.
     * * Si el segundo jugador es un jugador humano, se solicita su nombre a través de una vista de inicio.
     */
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

        
        try {
            String jugador1 = ctrlDominio.getUsuarioActual();
            BufferedImage profileImage1 = ctrlDominio.getProfileImage(jugador1);
            if (profileImage1 != null) {
                vScrabble.setPlayer1Image(profileImage1);
            }
        } catch (Exception ex) {
            // Ignorar silenciosamente si no hay imagen de perfil
        }

    
        try {
            String jugador2 = ctrlDominio.getSegundoJugador();
            BufferedImage profileImage2 = ctrlDominio.getProfileImage(jugador2);
            if (profileImage2 != null) {
                vScrabble.setPlayer2Image(profileImage2);
            }
        } catch (Exception ex) {
        }

       

        vScrabble.setTileActionListener(new VistaJuego.TileActionListener() {
            boolean cola = false;
            int row_cola = -1;
            int col_cola = -1;
            String letra_cola = "";

            @Override
            public void onTilePlaced(String letter, int score, int row, int col) {
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
        vScrabble.pasar(e -> pasarTurno());
        vScrabble.finTurno(e -> finTurno());
        vScrabble.reset(e -> crearVistaFichas());
        vScrabble.salir(e -> crearVistaSalir());
        vScrabble.ayuda(e -> ayuda());
        List<String> fichas = ctrlDominio.obtenerFichas();
        for (String ficha : fichas) {
            vScrabble.modificarRack(ficha);
        }
        actualizarTablero();
        
        vMenuPrincipal.jugarPartida(vScrabble);
    }


    /**
     * Metodo para colocar un comodín en el tablero.
     * @param row Fila donde se colocará el comodín.
     * @param col Columna donde se colocará el comodín.
     */
    private void ponerComodin(int row, int col) {
        ponerFicha(vLetra.getTexto(), 0, row, col);
        vLetra.dispose();
    }



    /**
     * Opción de ayuda en el juego.
     * Permite al jugador obtener ayuda durante la partida, colocando una palabra  en el tablero. Se pasa tuno automaticamente al siguiente jugador.
     */
    private void ayuda() {
        try {
            int resultado = ctrlDominio.jugarScrabble(7, "");
            if (resultado != 0) {
                        crearVistaFinal(false, resultado);
                    }
            actualizarTablero();
        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }

    }

    /**
     * Crea la vista de fin de partida.
     * Muestra un mensaje indicando el ganador y su puntuación.
     * @param abandonada Indica si la partida fue abandonada por un jugador.
     * @param resultado El resultado de la partida, donde 1 indica que el jugador actual ganó y 2 que el segundo jugador ganó.
     */
    private void crearVistaFinal(boolean abandonada, int resultado) {
        String mensaje;
        if(nombreSegundoJugador.equals("")) nombreSegundoJugador = "propAI";
        String ganador = resultado == 1 ? ctrlDominio.getUsuarioActual() : nombreSegundoJugador;
        String perdedor = resultado == 1 ? nombreSegundoJugador : ctrlDominio.getUsuarioActual();
        String puntosGanador = resultado == 1
            ? Integer.toString(ctrlDominio.getPuntosJugador1())
            : Integer.toString(ctrlDominio.getPuntosJugador2());
        System.out.println("Ganador: " + ganador);
        System.out.println("Perdedor: " + perdedor);
      
        if (abandonada) {
            mensaje = "Jugador " + perdedor + " ha abandonado la partida. Jugador " + ganador + " gana.";
        } else {
            mensaje = "Fin de la partida. Jugador " + ganador + " ha ganado con " + puntosGanador + " puntos.";
        }
        nombreSegundoJugador = "";
        VistaFinPartida vFinal = new VistaFinPartida(mensaje);
        vFinal.setDuracion(3);
        vFinal.setLocationRelativeTo(null);
        vFinal.setResizable(false);

        vFinal.mostrar();
        crearVistaMenuPrincipal();

    }


    /**
     * Crea la vista de salir de una partida en curso.
     * Permite al usuario abandonar la partida o salir sin abandonarla.
     */
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

    /**
     * Crea la vista de Fichas para cambiarlas.
     * Permite al usuario seleccionar fichas para cambiar por otras.
     * Esta vista se muestra cuando el usuario decide cambiar sus fichas actuales con el boton "Reset" en el juego.
     */
    private void crearVistaFichas() {
        try {
            vFichas = new VistaResetFichas();

            List<String> fichas = ctrlDominio.obtenerFichas();
            vFichas.cargarFichas(fichas);
            vFichas.setAcceptListener(e -> {
                String seleccionadas = vFichas.getSelectedTiles();
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

    /**
     * Pone una ficha en el tablero de Scrabble.
     * @param letra La letra de la ficha a poner.
     * @param puntuacion La puntuación asociada a la ficha.
     * @param fila Fila donde se colocará la ficha en el tablero.
     * @param col Columna donde se colocará la ficha en el tablero.
     */
    private void ponerFicha(String letra, int puntuacion, int fila, int col) {
        System.out.println(letra);
        String parametros = letra + " " + Integer.toString(puntuacion) + " " + Integer.toString(fila) + " " + Integer.toString(col);

        try {
            if (letra != null && !letra.isEmpty() && !letra.equals("")) {
                ctrlDominio.jugarScrabble(1, parametros);
            }

     
        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }
    }

    /**
     * Quita una ficha del tablero de Scrabble.
     * @param letra La letra de la ficha a quitar.
     * @param puntuacion La puntuación asociada a la ficha.
     * @param fila Fila donde se encuentra la ficha en el tablero.
     * @param col Columna donde se encuentra la ficha en el tablero.
     */
    private void quitarFicha(String letra, int puntuacion, int fila, int col) {
        try {
            // System.out.println("Quitar ficha: ");
            String parametros = Integer.toString(fila) + " " + Integer.toString(col);
            ctrlDominio.jugarScrabble(2, parametros);
        } catch (Exception e) {
            vScrabble.setError(e.getMessage());
        }
    }

    /**
     * Actualiza el tablero de Scrabble con las letras y puntuaciones actuales.
     */
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
        vScrabble.setPuntos(ctrlDominio.getPuntosJugador1(), ctrlDominio.getPuntosJugador2());
    }

    /**
     * Método que se llama al finalizar el turno del jugador, después de colocar una palabra.
     * Actualiza el tablero y puntuación.
     */
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

    /**
     * Método para pasar el turno al siguiente jugador.
     * Este método se llama cuando el jugador actual ha terminado su turno y quiere pasar al siguiente.
     */
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


    /**
     * Crea la vista de Pantalla Principal tras hacer login, misma vista al darle clic a "Jugar" en el menu lateral.
     */
    private void crearVistaPantallaPrincipal() {
        // vPantallaPrincipal.jugarScrabble(e -> jugarPartida());
        vPantallaPrincipal.cargarUltimaPartida(e -> cargarUltimaPartida());
        vPantallaPrincipal.setNombre(ctrlDominio.getUsuarioActual());
        vMenuPrincipal.muestraCard("PRINCIPAL");
    }
    
    
    /**
     * Crea la vista de crear partida.
     * Esta vista permite al usuario crear una nueva partida, seleccionar el identificador, recurso y el modo de juego.
     */
    private void crearVistaCrearPartida() {

        vCrearPartida.jugarPartida(e -> crearPartida());
        vCrearPartida.addSegundoJugador(e -> crearVistaSegundoJugador());
        List<String> ids = ctrlDominio.obtenerRecursos();
        String[] idiomas = ids.toArray(new String[0]);
        vCrearPartida.setIdiomas(idiomas);
        vMenuPrincipal.muestraCard("CREARPARTIDA");

    }

    /**
     * Crea la vista del ranking de jugadores.
     * Esta vista muestra la lista de jugadores ordenados por punutación máxima.
     */
    private void crearVistaCargarPartida() {
        List<String> partidasGuardadas = ctrlDominio.obtenerNombresPartidasGuardadas();

        vCargarPartida.setPartidas(partidasGuardadas);

        vCargarPartida.eliminarPartida(e -> eliminarPartidaSeleccionada());

        vMenuPrincipal.muestraCard("CARGARPARTIDA");
    }

    /**
     * Metodo para cargar la partida seleccionada.
     * Este método se llama cuando el usuario selecciona una partida guardada y quiere cargarla.
     * Carga la partida seleccionada y muestra la vista del juego.
     */
    private void cargarPartidaSeleccionada() {
        String partidaSeleccionada = vCargarPartida.getSeleccionada();
        if (partidaSeleccionada != null) {
            try {
                ctrlDominio.cargarPartida(partidaSeleccionada);
                jugarPartida(); 
            } catch (Exception e) {
                System.err.println("Error al cargar la partida: " + e.getMessage());
            }
        }
    }

    /**
     * Elimina la partida seleccionada de la lista de partidas guardadas.
     */
    private void eliminarPartidaSeleccionada() {
        String partidaSeleccionada = vCargarPartida.getSeleccionada();
        if (partidaSeleccionada != null) {
            try {
                ctrlDominio.eliminarPartidaGuardada(partidaSeleccionada);
                vCargarPartida.removePartida(partidaSeleccionada);
            } catch (Exception e) {
                System.err.println("Error al eliminar la partida: " + e.getMessage());
            }
        }
    }

    /**
     * Crea la vista de Login o Registro para el segundo jugador.
     */
    private void crearVistaSegundoJugador() {
        vSegundoJugador = new VistaInicio();
        vSegundoJugador.entrar(e -> addSegundoJugador(false));
    }


    /**
     * Metodo para añadir un segundo jugador a la partida.
     * Este método se llama cuando se inicia una partida con dos jugadores.
     * Si se está cargando una partida, verifica que el nombre del segundo jugador coincida con el guardado.
     * Si no se está cargando una partida, registra al segundo jugador o inicia sesión si ya existe.
     * @param cargar Indica si se está cargando una partida o no.
     */
    private void addSegundoJugador(boolean cargar) {
        nombreSegundoJugador = vSegundoJugador.getNombre();
        String password = new String(vSegundoJugador.getPassword());
        try {
            if (cargar) {
                if( ctrlDominio.getSegundoJugador().equals("propAI")) {
                    jugarPartida();
                } else {

                    if(nombreSegundoJugador.equals(ctrlDominio.getSegundoJugador())) {
                     jugarPartida();
                     vSegundoJugador.dispose();
                    }
                    else {
                        vSegundoJugador.setError("El nombre del segundo jugador no coincide.");
                    }
                }
                
            }
            else{
                if (vSegundoJugador.getSeleccionado()) {
                ctrlDominio.iniciarSesionSegundoJugador(nombreSegundoJugador, password);
            } else {
                ctrlDominio.registrarJugador(nombreSegundoJugador, password);
                ctrlDominio.iniciarSesionSegundoJugador(nombreSegundoJugador, password);
            }

            }
        } catch (Exception e) {
            
            vSegundoJugador.setError(e.getMessage());
        }

    }

    /**
     * Crea la vista de cuenta del jugador actual.
     * Esta vista muestra el nombre del jugador, sus puntos, su posición en el ranking y su imagen de perfil.
     * También mustra las opciones de cambiar el nombre, avatar, la contraseña y eliminar el jugador.
     */
    private void crearVistaCuenta() {

        try {
            String username = ctrlDominio.getUsuarioActual();
            vCuenta.setNombre(username);

            try {
                vCuenta.setPuntos(Integer.toString(ctrlDominio.getPuntosActual()));
                System.out.println("Puntos cargados: " + ctrlDominio.getPuntosActual());
            } catch (UsuarioNoEncontradoException e) {
                vCuenta.setPuntos("0");
                System.out.println("Error al cargar puntos: " + e.getMessage());
            }

            try {
                int posicion = ctrlDominio.obtenerPosicion(username);
                if (posicion > 0) {
                    vCuenta.setPosicion(posicion);
                    System.out.println("Posición cargada: " + posicion);
                } else {
                    System.out.println("Posición recibida inválida: " + posicion);
                    vCuenta.setPosicion(1); 
                }
            } catch (UsuarioNoEncontradoException | IOException e) {
                vCuenta.setPosicion(0);
                System.out.println("Usuario no clasificado en ranking: " + e.getMessage());
            }

            try {
                BufferedImage profileImage = ctrlDominio.getProfileImage(username);
                if (profileImage != null) {
                    vCuenta.setProfileImage(profileImage);
                }
            } catch (Exception ex) {
            }

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
            System.err.println("Error general al cargar datos del perfil: " + e.getMessage());
        }

        vCuenta.cambiarNombre(e -> crearVistaCambiarNombre());
        vCuenta.cambiarPassword(e -> crearVistaCambiarPassword());
        vCuenta.eliminarJugador(e -> crearVistaEliminarJugador());

        vMenuPrincipal.muestraCard("CUENTA");
        vLogin.pack();
    }

    /**
     * Crea una vista para cambiar la contraseña del jugador actual.
     */
    private void crearVistaCambiarPassword() {
        if (vCambiar == null || !vCambiar.isDisplayable()) {
            vCambiar = new VistaCambiarDatos("password");
            vCambiar.setLocationRelativeTo(null);
        }

        vCambiar.setVisible(true);
        vCambiar.setLocationRelativeTo(null);
        vCambiar.cambiar(e -> cambiarPassword());
    }

    /**
     * Crea una vista para eliminar un jugador.
     * Esta vista solicita la contraseña del jugador actual para confirmar la eliminación.
     */
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

    /**
     * Crea una vista para cambiar el nombre del jugador actual.
     * Esta vista permite al usuario ingresar un nuevo nombre y la contraseña actual para confirmar el cambio.
     */
    private void crearVistaCambiarNombre() {
        if (vCambiar == null || !vCambiar.isDisplayable()) {
            vCambiar = new VistaCambiarDatos("nombre");
            vCambiar.setLocationRelativeTo(null);
        }

        vCambiar.setVisible(true);
        vCambiar.setResizable(false);
        vCambiar.cambiar(e -> cambiarNombre());

    }


    /**
     * Cambia la contraseña del jugador actual.
     * Verifica que la contraseña actual sea correcta y que las nuevas contraseñas coincidan.
     * Si todo es correcto, actualiza la contraseña del jugador y cierra la vista de cambio de datos.
     * Si hay un error, muestra un mensaje de error en la vista de cambio de datos.
     */
    private void cambiarPassword() {
        String passwordActual = new String(vCambiar.getPasswordActual());
        String passwordNueva = new String(vCambiar.getPassword());
        String confirmPassword = new String(vCambiar.getConfirmPassword());

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

    /**
     * Cambia el nombre del jugador actual.
     * Verifica que el nuevo nombre no esté vacío y que la contraseña actual sea correcta.
     * Si todo es correcto, actualiza el nombre del jugador y cierra la vista de cambio de datos.
     * Si hay un error, muestra un mensaje de error en la vista de cambio de datos.
     */
    private void cambiarNombre() {
        String nombre = new String(vCambiar.getNombre());
        String password = new String(vCambiar.getPasswordActual());
        try {
            ctrlDominio.cambiarNombre(nombre, password);
            vCuenta.setNombre(ctrlDominio.getUsuarioActual());
            vCambiar.dispose();
        } catch (Exception e) {
            vCambiar.setError(e.getMessage());
            System.err.println("Error: " + e.getMessage()); // Opcional: mantener también el log en consola
        }
    }

    /**
     * Elimina un jugador del sistema.
     * Solicita la contraseña del jugador para confirmar la eliminación.
     * Si la contraseña es correcta, el jugador es eliminado y se cierra la sesión.
     * Si hay un error, se muestra un mensaje de error en la vista de contraseña.
     */
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

    /**
     * Crea una vista para mostrar el ranking de jugadores.
     * @IOException se lanza si hay un error al obtener el ranking.
    */
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

    /**
     * Crea una vista para gestionar los recursos del juego.
     * Esta vista permite al usuario ver, añadir, modificar y eliminar recursos.
     */
    private void crearVistaRecursos() {

        List<String> ids = ctrlDominio.obtenerRecursos();
        vRecursos.setLista(ids);
        vRecursos.eliminarRecurso(e -> eliminarRecurso());
        vRecursos.modificarRecurso(e -> crearVistaModificarRecurso());
        vRecursos.addRecurso(e -> crearVistaAddRecurso());
        vMenuPrincipal.muestraCard("RECURSOS");

    }

    /**
     * Crea una vista para añadir un nuevo recurso.
     * Esta vista permite al usuario añadir un diccionario y una bolsa de letras.
     */
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

    /**
     * Crea una vista para modificar un recurso existente.
     */
    private void crearVistaModificarRecurso() {
        String idRecurso = vRecursos.getSeleccionado();
        if (vAddRecurso == null || !vAddRecurso.isDisplayable()) {
            vAddRecurso = new VistaGestionRecursos(idRecurso, "Modificar Recurso");

            try {
                List<String> diccionario = ctrlDominio.obtenerDiccionario(idRecurso);
                vAddRecurso.listToTextArea(diccionario, 1);
                List<String> bolsa = ctrlDominio.obtenerBolsa(idRecurso);
                vAddRecurso.listToTextArea(bolsa, 2);
            } catch (Exception e) {
                System.err.println("Error al cargar el recurso: " + e.getMessage());
                e.printStackTrace();
            }

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

    /**
     * Añade un nuevo recurso a la lista de recursos.
     * Muestra mensajes de error si el ID está vacío o si el diccionario o la bolsa están vacíos.
     * También maneja excepciones específicas relacionadas con el formato del diccionario y la bolsa.
     */
    private void addRecurso() {
        String id = vAddRecurso.getID();

        if (id == null || id.trim().isEmpty()) {
            vAddRecurso.setError("El ID no puede estar vacío");
            return;
        }

        List<String> diccionario = vAddRecurso.textAreaToList(1);
        List<String> bolsa = vAddRecurso.textAreaToList(2);

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
            vAddRecurso.dispose();  
            crearVistaRecursos(); 
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

    /**
     * Modifica un recurso existente en la vista de gestión de recursos.
     * Muestra mensajes de error si ocurre algún problema durante la modificación.
     */
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
            crearVistaRecursos(); 
        } catch (FormatoDiccionarioInvalidoException e) {
            vAddRecurso.setError("Formato de diccionario inválido. Verifica que las palabras estén en mayúsculas y ordenadas");
        } catch (FormatoBolsaInvalidoException e) {
            vAddRecurso.setError("Formato de bolsa inválido. Formato correcto: 'LETRA FRECUENCIA PUNTOS'");
        } catch (Exception e) {
            vAddRecurso.setError("Error: " + e.getMessage());
        }
    }

    /**
     * Elimina el recurso seleccionado de la lista de recursos.
     * Muestra un mensaje de error si ocurre algún problema durante la eliminación.
     */
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


    /**
     * Lee un archivo de texto y devuelve una lista de líneas no vacías.
     * @param ruta Ruta del archivo a leer.
     * @return Lista de líneas no vacías del archivo.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
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

    /**
     * Configura la fuente global de la aplicación y carga el icono de la ventana.
     * Este método se llama al inicio de la aplicación para establecer una apariencia consistente.
     */
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
        
        
        try {
            ImageIcon icon = new ImageIcon("FONTS/src/main/Recursos/Imagenes/scrabble_logo.png");

            if (icon.getIconWidth() <= 0) {
                java.net.URL iconURL = getClass().getResource("/Recursos/Imagenes/scrabble_logo.png");
                if (iconURL != null) {
                    icon = new ImageIcon(iconURL);
                }
            }

            if (icon.getIconWidth() > 0) {
                List<Image> iconos = new ArrayList<>();
                int[] tamaños = {16, 24, 32, 48, 64, 128};

                for (int tamaño : tamaños) {
                    Image imagenEscalada = icon.getImage().getScaledInstance(
                            tamaño, tamaño, Image.SCALE_SMOOTH);
                    iconos.add(imagenEscalada);
                }

                aplicarIconoVentana = iconos.get(iconos.size() - 1);

            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono global: " + e.getMessage());
        }
    }

    /**
     * Aplica el icono a la ventana de login y a otras vistas si es necesario.
     * De momento no se usa debido a problemas de compatibilidad con sistema WSL.
     */
    private void aplicarIconos() {
        if (aplicarIconoVentana != null && vLogin != null) {
            try {
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

    /**
     * Crear la vista del manual de usuario.
     */
    private void crearVistaManual() {
        if (vManual == null) {
            vManual = new VistaManual();
            vMenuPrincipal.addCard("MANUAL", vManual);
        }

        vMenuPrincipal.muestraCard("MANUAL");
    }

}
