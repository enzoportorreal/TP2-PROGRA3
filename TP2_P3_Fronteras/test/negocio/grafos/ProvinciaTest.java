package negocio.grafos;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProvinciaTest {

    @Test
    public void datosDeLaProvinciaTest() {
        Provincia provincia = new Provincia(3, "Chubut", -44.0, -69.0);

        assertEquals(3, provincia.getId());
        assertEquals("Chubut", provincia.getNombre());
        assertEquals(-44.0, provincia.getLat(), 0.0001);
        assertEquals(-69.0, provincia.getLon(), 0.0001);
    }
}