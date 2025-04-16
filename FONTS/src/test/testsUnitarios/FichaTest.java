package testsUnitarios;


import Dominio.Modelos.Ficha;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;




public class FichaTest {

    private Ficha ficha;

    /**
     * Este método se ejecuta antes de cada test.
     * Lo usamos para inicializar la instancia de Ficha o cualquier otro recurso que necesitemos.
     */
    @Before
    public void setUp() {
        // Creamos una Ficha con valores iniciales
        ficha = new Ficha(1, "A", 1);
    }

    @Test
    public void testConstructor() {
        // Verificamos que el constructor haya asignado los valores correctamente
        assertEquals(1, ficha.getId());
        assertEquals("A", ficha.getLetra());
        assertEquals(1, ficha.getPuntuacion());
    }

    @Test
    public void testSetId() {
        ficha.setId(5);
        assertEquals(5, ficha.getId());
    }

    @Test
    public void testSetLetra() {
        ficha.setLetra("Z");
        assertEquals("Z", ficha.getLetra());
    }

    @Test
    public void testSetPuntuacion() {
        ficha.setPuntuacion(10);
        assertEquals(10, ficha.getPuntuacion());
    }

    @Test
    public void testToString() {
        // Modificamos algunos valores
        ficha.setId(2);
        ficha.setLetra("B");
        ficha.setPuntuacion(3);

        String resultado = ficha.toString();

        // Comprobamos que el toString incluye la información esperada
        assertTrue("Debe contener 'id=2'", resultado.contains("id=2"));
        assertTrue("Debe contener 'letra='B''", resultado.contains("letra='B'"));
        assertTrue("Debe contener 'puntuacion=3'", resultado.contains("puntuacion=3"));
    }
}