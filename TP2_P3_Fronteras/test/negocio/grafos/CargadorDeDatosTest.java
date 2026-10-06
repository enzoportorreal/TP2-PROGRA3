package negocio.grafos;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class CargadorDeDatosTest {

    private static final String JSON_VALIDO =
            "{ \"provincias\": ["
          + "    { \"id\": 0, \"nombre\": \"A\", \"lat\": -30.5, \"lon\": -60.0 },"
          + "    { \"id\": 1, \"nombre\": \"B\", \"lat\": -31.0, \"lon\": -61.0 } ],"
          + "  \"fronteras\": [ { \"origen\": 0, \"destino\": 1, \"peso\": 7 } ] }";

    @Test
    public void cargarJsonValidoTest() {
        DatosGrafoJSON datos = CargadorDeDatos.cargar(new StringReader(JSON_VALIDO));

        assertEquals(2, datos.getProvincias().size());
        assertEquals("A", datos.getProvincias().get(0).getNombre());
        assertEquals(-31.0, datos.getProvincias().get(1).getLat(), 0.0001);
        assertEquals(1, datos.getFronteras().size());
        assertEquals(7, datos.getFronteras().get(0).getPeso());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listaDeProvinciasNoSePuedeModificarTest() {
        DatosGrafoJSON datos = CargadorDeDatos.cargar(new StringReader(JSON_VALIDO));
        datos.getProvincias().clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void jsonMalFormadoTest() {
        CargadorDeDatos.cargar(new StringReader("esto no es json"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void jsonVacioTest() {
        CargadorDeDatos.cargar(new StringReader(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void jsonSinProvinciasTest() {
        CargadorDeDatos.cargar(new StringReader("{ \"fronteras\": [] }"));
    }

    @Test(expected = IOException.class)
    public void archivoInexistenteTest() throws IOException {
        CargadorDeDatos.cargarDesdeArchivo("no-existe.json");
    }
}