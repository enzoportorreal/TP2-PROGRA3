package negocio;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class SistemaRegionesTest {

    //registrar fronteras

    @Test(expected = IllegalArgumentException.class)
    public void fronteraConBucleTest() {
        SistemaRegiones sistema = new SistemaRegiones(5);
        sistema.registrarFrontera(1, 1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fronteraConPesoNegativoTest() {
        SistemaRegiones sistema = new SistemaRegiones(5);
        sistema.registrarFrontera(0, 1, -15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fronteraConPesoCeroTest() {
        SistemaRegiones sistema = new SistemaRegiones(5);
        sistema.registrarFrontera(0, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fronteraConProvinciaInexistenteTest() {
        SistemaRegiones sistema = new SistemaRegiones(5);
        sistema.registrarFrontera(0, 5, 10);
    }

    @Test
    public void registrarFronteraTest() {
        SistemaRegiones sistema = new SistemaRegiones(3);
        sistema.registrarFrontera(0, 2, 40);

        assertTrue(sistema.existeFrontera(0, 2));
        assertTrue(sistema.existeFrontera(2, 0));
        assertFalse(sistema.existeFrontera(0, 1));
        assertEquals(40, sistema.pesoFrontera(0, 2));
    }

    @Test
    public void reiniciarAristasTest() {
        SistemaRegiones sistema = new SistemaRegiones(3);
        sistema.registrarFrontera(0, 1, 5);

        sistema.reiniciarAristas();

        assertFalse(sistema.existeFrontera(0, 1));
        assertEquals(3, sistema.cantidadProvincias());
    }

    //conexidad

    @Test
    public void grafoConexoTest() {
        assertTrue(crearSistemaEnLinea().esGrafoConexo());
    }

    @Test
    public void grafoInconexoTest() {
        SistemaRegiones sistema = new SistemaRegiones(3);
        sistema.registrarFrontera(0, 1, 5);

        assertFalse(sistema.esGrafoConexo());
    }

    //generar regiones

    @Test(expected = IllegalArgumentException.class)
    public void kIgualACeroTest() {
        crearSistemaEnLinea().generarRegiones(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void kMayorALaCantidadDeProvinciasTest() {
        crearSistemaEnLinea().generarRegiones(5);
    }

    @Test(expected = IllegalStateException.class)
    public void generarRegionesConGrafoInconexoTest() {
        SistemaRegiones sistema = new SistemaRegiones(3);
        sistema.registrarFrontera(0, 1, 5);

        sistema.generarRegiones(2);
    }

    @Test
    public void generarRegionesSeparaPorLaAristaMasPesadaTest() {
        SistemaRegiones sistema = crearSistemaEnLinea();

        List<List<Integer>> regiones = sistema.generarRegiones(2);

        assertEquals(2, regiones.size());
        assertEquals(Arrays.asList(0, 1), regiones.get(0));
        assertEquals(Arrays.asList(2, 3), regiones.get(1));
    }

    @Test
    public void fronterasResultantesTest() {
        SistemaRegiones sistema = crearSistemaEnLinea();

        sistema.generarRegiones(2);

        assertTrue(sistema.existeFronteraResultante(0, 1));
        assertTrue(sistema.existeFronteraResultante(2, 3));
        assertFalse(sistema.existeFronteraResultante(1, 2));
    }

    @Test
    public void sinCalcularNoHayFronterasResultantesTest() {
        assertFalse(crearSistemaEnLinea().existeFronteraResultante(0, 1));
    }

    @Test
    public void modificarElGrafoDescartaLasRegionesCalculadasTest() {
        SistemaRegiones sistema = crearSistemaEnLinea();
        sistema.generarRegiones(2);

        sistema.registrarFrontera(0, 3, 50);

        assertFalse(sistema.existeFronteraResultante(0, 1));
    }

    //datos reales (necesita provincias.json en la raiz del proyecto)

    @Test
    public void datosDelJSONFormanUnGrafoConexoTest() {
        SistemaRegiones sistema = new SistemaRegiones();

        assertEquals(24, sistema.cantidadProvincias());
        assertTrue(sistema.esGrafoConexo());
    }

    //auxiliar: 0 - 1 - 2 - 3 con pesos 10, 100, 10

    private SistemaRegiones crearSistemaEnLinea() {
        SistemaRegiones sistema = new SistemaRegiones(4);
        sistema.registrarFrontera(0, 1, 10);
        sistema.registrarFrontera(1, 2, 100);
        sistema.registrarFrontera(2, 3, 10);
        return sistema;
    }
}