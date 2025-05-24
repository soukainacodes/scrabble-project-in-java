package Dominio;

import Dominio.Excepciones.ComandoInvalidoException;
import Dominio.Excepciones.PalabraInvalidaException;
import Dominio.Modelos.Algoritmo;
import Dominio.Modelos.Dawg;
import Dominio.Modelos.Ficha;
import Dominio.Modelos.Pair;
import Dominio.Modelos.Partida;
import Dominio.Modelos.Tablero;
import Dominio.Modelos.Validador;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controlador de la lógica para una partida de Scrabble.
 * <p>
 * Gestiona la partida actual, validación de palabras, turnos de juego,
 * interacción con el algoritmo de IA y finalización de la partida.
 */
public class CtrlPartida {

    /**
     * Partida actualmente en curso.
     */
    private Partida partidaActual;

    /**
     * Estructura DAWG para validación de vocabulario.
     */
    private Dawg dawg;

    /**
     * Validador de palabras y reglas de Scrabble.
     */
    private Validador validador;

    /**
     * Indica si el turno actual ha finalizado.
     */
    private boolean finTurno;

    /**
     * Indica si el modo de juego es con IA.
     */
    private boolean isAlgoritmo;

    /**
     * Motor de IA para jugar automáticamente.
     */
    private Algoritmo algoritmo;

    /**
     * Determina si el jugador 2 es la IA.
     */
    private boolean jugadorAlgoritmo;

    /**
     * Construye un controlador de partida sin partida inicial. Inicializa el
     * validador de palabras.
     */
    public CtrlPartida() {
        this.validador = new Validador();
    }

    /**
     * Crea una nueva partida con el ID y las configuraciones especificadas.
     *
     * @param modo modo de juego (0: contra IA, 1: contra otro jugador).
     * @param id identificador de la partida.
     * @param lineasArchivo lista de palabras válidas.
     * @param lineasArchivoBolsa lista de fichas disponibles.
     * @param seed semilla para la generación aleatoria.
     * @param jugadorAlgoritmo {@code true} si el jugador 2 es la IA.
     */
    public void crearPartida(int modo, String id, List<String> lineasArchivo, List<String> lineasArchivoBolsa,
            long seed, boolean jugadorAlgoritmo) {
        this.dawg = new Dawg(lineasArchivoBolsa, lineasArchivo);
        this.partidaActual = new Partida(id, lineasArchivoBolsa, seed);
        this.finTurno = false;
        this.jugadorAlgoritmo = jugadorAlgoritmo;
        if (modo == 0) {
            this.isAlgoritmo = true;
            this.algoritmo = new Algoritmo();
        } else {
            this.isAlgoritmo = false;
        }
    }

    /**
     * Obtiene las fichas actuales del jugador activo.
     *
     * @return lista de cadenas con las fichas en mano.
     */
    public List<String> obtenerFichas() {
        return partidaActual.obtenerFichas();
    }

    /**
     * Establece la partida actual y su configuración.
     *
     * @param partida la partida a establecer.
     * @param diccionario lista de palabras válidas.
     * @param bolsa lista de fichas disponibles en la bolsa.
     */
    public void setPartida(Partida partida, List<String> diccionario, List<String> bolsa) {
        this.dawg = new Dawg(bolsa, diccionario);
        this.partidaActual = partida;
    }

    /**
     * Devuelve la partida actual para su almacenamiento externo.
     *
     * @return la {@link Partida} en curso.
     */
    public Partida getPartida() {
        return partidaActual;
    }

    /**
     * Ejecuta una jugada de Scrabble según la opción especificada.
     *
     * @param opcion código de acción (1:añadir ficha, 2:quitar ficha, 3:cambiar
     * fichas, 4:finalizar turno, 5:intercambiar, 6:abandonar, 7:IA).
     * @param input cadena con los parámetros de la jugada.
     * @return código de fin de turno o fin de partida.
     * @throws ComandoInvalidoException si el formato del comando es incorrecto.
     * @throws PalabraInvalidaException si la palabra formada no es válida.
     */
    public int jugarScrabble(int opcion, String input) throws ComandoInvalidoException, PalabraInvalidaException {
        String[] parts = input.trim().split(" ");

        switch (opcion) {
            case 1: {
                if (parts.length < 3) {
                    throw new ComandoInvalidoException("Uso esperado: <ficha> <x> <y>.");
                }

                String ficha = parts[0];
                if (ficha.matches("[0-7]")) {
                    int num = Integer.parseInt(ficha);
                    List<String> fichasJugador = partidaActual.obtenerFichas();
                    if (num < 0 || num >= fichasJugador.size()) {
                        throw new ComandoInvalidoException("Índice de ficha fuera de rango: " + num);
                    }
                    ficha = fichasJugador.get(num);
                }

                int x, y;

                String puntuacion = parts[1];
                if(puntuacion.equals("0")){
                    System.out.println("Comodin");
                    ficha = ficha + " " + puntuacion;
                }
                x = Integer.parseInt(parts[2]);
                y = Integer.parseInt(parts[3]);

                partidaActual.añadirFicha(ficha, x, y);

                break;
            }

            case 2: {
                if (parts.length != 2) {
                    throw new ComandoInvalidoException("Uso esperado: <x> <y> para quitar ficha.");
                }
                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    partidaActual.quitarFichaTablero(x, y);
                } catch (NumberFormatException e) {
                    throw new ComandoInvalidoException("Coordenadas inválidas: deben ser números.");
                }
                break;
            }

            case 3: {
                List<Pair<Integer, Integer>> coordenadas = new ArrayList<>(partidaActual.getCoordenadasPalabras());
                for (Pair<Integer, Integer> p : coordenadas) {
                    System.out.println("Coordenadas: " + p.getFirst() + ", " + p.getSecond());
                    partidaActual.quitarFichaTablero(p.getFirst(), p.getSecond());
                }

                return finTurno(true, true);
            }

            case 4:

                return finTurno(false, true);

            case 5: {
                List<Pair<Integer, Integer>> coordenadas = new ArrayList<>(partidaActual.getCoordenadasPalabras());
                for (Pair<Integer, Integer> p : coordenadas) {
                    System.out.println("Coordenadas: " + p.getFirst() + ", " + p.getSecond());
                    partidaActual.quitarFichaTablero(p.getFirst(), p.getSecond());
                }

                List<String> fichas = new ArrayList<>(Arrays.asList(parts));
                List<String> letrasJugador = new ArrayList<>();
                for (Ficha f : partidaActual.getFichasJugador()) {
                    letrasJugador.add(f.getLetra());
                }

                for (int i = fichas.size() - 1; i >= 0; --i) {
                    String s = fichas.get(i);
                    if (s.matches("[0-7]")) {
                        int idx = Integer.parseInt(s);
                        List<Ficha> mano = partidaActual.getFichasJugador();
                        if (idx < 0 || idx >= mano.size()) {
                            throw new ComandoInvalidoException("Índice de ficha inválido: " + s);
                        }
                        fichas.add(mano.get(idx).getLetra());
                        fichas.remove(i);
                    }
                }

                for (String s : fichas) {
                    if (!letrasJugador.contains(s)) {
                        throw new ComandoInvalidoException("No tienes la ficha: '" + s + "'");
                    }
                    partidaActual.quitarFicha(s);
                }

                partidaActual.recuperarFichas();
                return finTurno(true, true);
            }

            case 6:
                return finPartida(true);

            case 7:
                if (!jugadorAlgoritmo) {
                    throw new ComandoInvalidoException("No se puede usar la opción 'Ayuda' en este modo de juego (Duo - 2 Jugadores).");
                }
                List<Pair<Integer, Integer>> coordenadas = new ArrayList<>(partidaActual.getCoordenadasPalabras());
                for (Pair<Integer, Integer> p : coordenadas) {
                    System.out.println("Coordenadas: " + p.getFirst() + ", " + p.getSecond());
                    partidaActual.quitarFichaTablero(p.getFirst(), p.getSecond());
                }
                int puntosAlgoritmo = jugarAlgoritmo();
                partidaActual.addPuntos(puntosAlgoritmo);
                if (puntosAlgoritmo == 0 && partidaActual.isBolsaEmpty()
                        && partidaActual.getPuntosJugador2() > partidaActual.getPuntosJugador1()) {
                    return finPartida(false);
                }
                // Para habilitar el botón de Ayuda
                return 0;

            default:
                throw new ComandoInvalidoException("Opción de jugada desconocida: " + opcion);
        }

        return 0;
    }

    /**
     * Obtiene la puntuación acumulada del jugador 1.
     *
     * @return puntos del jugador 1.
     */
    public int getPuntosJugador1() {
        return partidaActual.getPuntosJugador1();
    }

    /**
     * Obtiene la puntuación acumulada del jugador 2 o IA.
     *
     * @return puntos del jugador 2.
     */
    public int getPuntosJugador2() {
        return partidaActual.getPuntosJugador2();
    }

    /**
     * Finaliza el turno actual: valida palabra, actualiza puntos, bloquea
     * celdas y cambia turno.
     *
     * @param pasar {@code true} para pasar sin validar palabra.
     * @param algoritmo {@code true} para ejecutar turno de IA.
     * @return 0 si continúa, código de fin de partida si corresponde.
     * @throws PalabraInvalidaException si la validación de la palabra falla.
     */
    private int finTurno(boolean pasar, boolean algoritmo) throws PalabraInvalidaException {
        if (!pasar) {
            System.out.println(partidaActual.getCoordenadasPalabras());
            int puntos = validador.validarPalabra(partidaActual.getCoordenadasPalabras(), dawg,
                    partidaActual.getTablero(), partidaActual.getContadorTurno());
            if (puntos > 0) {
                partidaActual.addPuntos(puntos);
            } else {
                throw new PalabraInvalidaException();

            }

        }

        partidaActual.bloquearCeldas();
        partidaActual.coordenadasClear();

        partidaActual.cambiarTurnoJugador();
        partidaActual.aumentarContador();

        if (!partidaActual.recuperarFichas() && partidaActual.getListSize() == 0) {

            return finPartida(false);
        }

        if (isAlgoritmo && algoritmo) {
            int puntosAlgoritmo = jugarAlgoritmo();
            partidaActual.addPuntos(puntosAlgoritmo);
            if (puntosAlgoritmo == 0 && partidaActual.isBolsaEmpty()
                    && partidaActual.getPuntosJugador1() > partidaActual.getPuntosJugador2()) {
                return finPartida(false);
            }

            finTurno(true, false);

        }
        return 0;
    }

    /**
     * Finaliza la partida, calculando resultado o abandono.
     *
     * @param abandono {@code true} si el jugador abandona.
     * @return código del jugador ganador (1 o 2).
     */
    private int finPartida(boolean abandono) {
        partidaActual.setPartidaAcabada();
        if (abandono) {
            if (partidaActual.getTurnoJugador()) {
                return 2;
            } else {
                return 1;
            }
        }
        partidaActual.cambiarTurnoJugador();
        for (Ficha ficha : partidaActual.getFichasJugador()) {
            partidaActual.addPuntos(-ficha.getPuntuacion());
        }

        if (partidaActual.getPuntosJugador1() > partidaActual.getPuntosJugador2()) {
            return 1;
        } else {
            return 2;
        }

    }

    /**
     * Ejecuta la jugada automática del algoritmo de IA.
     *
     * @return puntos obtenidos por la IA.
     */
    private int jugarAlgoritmo() {
        Pair<List<Pair<String, Pair<Integer, Integer>>>, Integer> ss = algoritmo
                .find_all_words(partidaActual.getFichasJugador(), dawg, partidaActual.getTablero());
        List<Pair<String, Pair<Integer, Integer>>> s = ss.getFirst();
        for (Pair<String, Pair<Integer, Integer>> aa : s) {
            partidaActual.añadirFicha(aa.getFirst(), aa.getSecond().getFirst(), aa.getSecond().getSecond());

        }
        if (s.size() == 0) {
            return 0;
        }
        return ss.getSecond();
    }

    /**
     * Obtiene el tablero actual de la partida.
     *
     * @return el tablero de la partida.
     */
    public Tablero obtenerTablero() {
        return partidaActual.getTablero();
    }

    /**
     * Establece el estado de la partida como acabada. Esto puede ser útil para
     * marcar la partida como finalizada y no mostrarlas en la lista de partidas
     * guardadas.
     */
    public void setPartidaAcabada() {
        this.partidaActual.setPartidaAcabada();
    }

    /**
     * Obtiene el ID de la partida actual.
     *
     * @return el ID de la partida.
     */
    public String getId() {
        return partidaActual.getIdPartida();
    }

    /**
     * Establece el recurso de la partida actual.
     *
     * @param recurso el recurso de la partida.
     * @param diccionario lista de palabras válidas.
     * @param bolsa lista de fichas disponibles.
     */
    public void setRecursoPartida(String recurso, List<String> diccionario, List<String> bolsa) {
        this.partidaActual.setRecursoPartida(recurso);
        this.dawg = new Dawg(bolsa, diccionario);

    }

    /**
     * Obtiene el recurso de la partida actual.
     *
     * @return el recurso de la partida.
     */
    public String getRecursoPartida() {
        return partidaActual.getRecursoPartida();
    }

    /**
     * Limpia la partida actual y sus recursos. Esto incluye el DAWG, el
     * validador y el estado del turno.
     */
    public void clearPartida() {
        this.partidaActual = null;
        this.dawg = null;
        this.validador = null;
        this.finTurno = false;
        this.isAlgoritmo = false;
        this.algoritmo = null;
    }

    /**
     * Activa el modo de juego contra IA. + * Inicializa el algoritmo de IA para
     * jugar automáticamente.
     */
    public void activarAlgoritmo() {
        this.isAlgoritmo = true;
        this.jugadorAlgoritmo = true;
        this.algoritmo = new Algoritmo();
    }

}
