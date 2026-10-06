package negocio.grafos;

import static org.junit.Assert.*;
import org.junit.Test;

public class AristaTest {

    @Test
    public void datosDeLaAristaTest() {
        Arista arista = new Arista(2, 5, 30);

        assertEquals(2, arista.getOrigen());
        assertEquals(5, arista.getDestino());
        assertEquals(30, arista.getPeso());
    }

    @Test
    public void aristaMasLivianaEsMenorTest() {
        assertTrue(new Arista(0, 1, 5).compareTo(new Arista(1, 2, 9)) < 0);
    }

    @Test
    public void aristaMasPesadaEsMayorTest() {
        assertTrue(new Arista(0, 1, 9).compareTo(new Arista(1, 2, 5)) > 0);
    }

    @Test
    public void aristasDeIgualPesoSonEquivalentesTest() {
        assertEquals(0, new Arista(0, 1, 7).compareTo(new Arista(2, 3, 7)));
    }
}