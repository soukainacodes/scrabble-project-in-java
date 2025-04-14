package testsUnitarios;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Dominio.CtrlRanking;

/**
 * Test unitario para la clase CtrlRanking.
 * 
 * Este test comprueba la interacción entre la capa de control y el modelo Ranking
 * realizando operaciones reales de I/O sobre un fichero ("ranking.txt"). Se simula
 * el entorno borrando el fichero antes y después de cada test.
 */
public class CtrlRankingTest {

    private static final String FILE_NAME = "ranking.txt";
    private CtrlRanking ctrlRanking;

    @Before
    public void setUp() throws Exception {
        // Borrar el fichero ranking.txt si existe para iniciar desde un entorno limpio.
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        // Crear la instancia del controlador; en el constructor se intenta cargar el ranking.
        // Si el fichero no existe, se crea un nuevo Ranking.
        ctrlRanking = new CtrlRanking();
    }

    @After
    public void tearDown() throws Exception {
        // Al finalizar el test, se elimina el fichero ranking.txt.
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Test para verificar que, al crear un jugador, se guarde el ranking en el fichero.
     */
    @Test
    public void testCrearJugadorGuardaRanking() throws Exception {
        ctrlRanking.crearJugador("Alice", 100);
        File file = new File(FILE_NAME);
        assertTrue("El fichero ranking.txt debe existir tras crear un jugador", file.exists());
        List<String> lines = Files.readAllLines(file.toPath());
        assertFalse("El fichero ranking.txt no debe estar vacío", lines.isEmpty());
        // Se espera que la primera línea tenga el formato "1. Alice - 100"
        String firstLine = lines.get(0);
        assertTrue("La primera línea debe contener 'Alice - 100'", firstLine.contains("Alice - 100"));
    }

    /**
     * Test para verificar que, al modificar la puntuación de un jugador, se actualiza el fichero.
     */
    @Test
    public void testModificarRankingActualizaRanking() throws Exception {
        // Crear un jugador
        ctrlRanking.crearJugador("Alice", 100);
        // Modificar su puntuación
        ctrlRanking.modificarRanking("Alice", 150);
        File file = new File(FILE_NAME);
        assertTrue("El fichero ranking.txt debe existir", file.exists());
        List<String> lines = Files.readAllLines(file.toPath());
        assertFalse("El fichero ranking.txt no debe estar vacío", lines.isEmpty());
        // Se espera que, tras actualizar, se incluya "Alice - 150" en el fichero.
        boolean updated = lines.stream().anyMatch(line -> line.contains("Alice - 150"));
        assertTrue("El fichero ranking.txt debe contener la actualización 'Alice - 150'", updated);
    }

    /**
     * Test para verificar la carga del ranking desde el fichero.
     * Se guarda un ranking, se crea una nueva instancia del controlador (que carga el ranking)
     * y se comprueba que al mostrar el ranking se refleje la información guardada.
     */
    @Test
    public void testCargarRanking() throws Exception {
        // Guardamos un ranking creando un jugador
        ctrlRanking.crearJugador("Alice", 100);
        // Creamos una nueva instancia del controlador, lo que forzará la carga desde el fichero.
        CtrlRanking nuevoCtrlRanking = new CtrlRanking();
        
        // Redirigimos la salida estándar para capturar lo que imprime mostrarRanking().
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        nuevoCtrlRanking.mostrarRanking();
        
        // Restauramos la salida estándar
        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue("La salida debe contener 'Alice - 100'", output.contains("Alice - 100"));
    }
}
