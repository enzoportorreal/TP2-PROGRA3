package negocio.algoritmos;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import negocio.grafos.Grafo;

public class RegionalizadorTest {

    @Test
    public void unaSolaRegionTest() {
        List<List<Integer>> regiones = regionesPara(crearCaminoDeCuatro(), 1);

        assertEquals(1, regiones.size());
        assertEquals(Arrays.asList(0, 1, 2, 3), regiones.get(0));
    }

    @Test
    public void maximaCantidadDeRegionesTest() {
        // K = cantidad de vertices: se eliminan todas las aristas del AGM
        List<List<Integer>> regiones = regionesPara(crearCaminoDeCuatro(), 4);

        assertEquals(4, regiones.size());
        for (List<Integer> region : regiones) {
            assertEquals(1, region.size());
        }
    }

    @Test
    public void divisionDosRegionesTest() {
        Grafo grafo = new Grafo(4);
        grafo.agregarArista(0, 1, 5);
        grafo.agregarArista(2, 3, 8);
        grafo.agregarArista(1, 2, 100); // la arista a eliminar

        List<List<Integer>> regiones = regionesPara(grafo, 2);

        assertEquals(2, regiones.size());
        assertEquals(Arrays.asList(0, 1), regiones.get(0));
        assertEquals(Arrays.asList(2, 3), regiones.get(1));
    }

    @Test
    public void tresRegionesTest() {
        Grafo grafo = new Grafo(5);
        grafo.agregarArista(0, 1, 2);
        grafo.agregarArista(1, 2, 3);
        grafo.agregarArista(2, 3, 50);
        grafo.agregarArista(3, 4, 80);

        List<List<Integer>> regiones = regionesPara(grafo, 3);

        assertEquals(3, regiones.size());
        assertEquals(Arrays.asList(0, 1, 2), regiones.get(0));
        assertEquals(Arrays.asList(3), regiones.get(1));
        assertEquals(Arrays.asList(4), regiones.get(2));
    }

    @Test
    public void soloEliminaAristasDelArbolGeneradorMinimoTest() {
        // La arista 0-2 (peso 10) es la mas pesada del grafo, pero no esta en el AGM.
        // El AGM es 0-1 (1), 1-2 (2), 2-3 (3): con K = 2 se elimina 2-3.
        Grafo grafo = new Grafo(4);
        grafo.agregarArista(0, 1, 1);
        grafo.agregarArista(1, 2, 2);
        grafo.agregarArista(0, 2, 10);
        grafo.agregarArista(2, 3, 3);

        List<List<Integer>> regiones = regionesPara(grafo, 2);

        assertEquals(2, regiones.size());
        assertEquals(Arrays.asList(0, 1, 2), regiones.get(0));
        assertEquals(Arrays.asList(3), regiones.get(1));
    }

    @Test
    public void bosqueNoContieneLaAristaMasPesadaDelArbolTest() {
        Grafo bosque = Regionalizador.obtenerBosqueConKRegiones(crearCaminoDeCuatro(), 2);

        assertFalse(bosque.existeArista(1, 2));
        assertTrue(bosque.existeArista(0, 1));
        assertTrue(bosque.existeArista(2, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void kIgualACeroTest() {
        Regionalizador.obtenerBosqueConKRegiones(crearCaminoDeCuatro(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void kMayorALaCantidadDeVerticesTest() {
        Regionalizador.obtenerBosqueConKRegiones(crearCaminoDeCuatro(), 5);
    }

    // ---------- auxiliares ----------

    // 0 - 1 - 2 - 3 con pesos 10, 20, 15
    private Grafo crearCaminoDeCuatro() {
        Grafo grafo = new Grafo(4);
        grafo.agregarArista(0, 1, 10);
        grafo.agregarArista(1, 2, 20);
        grafo.agregarArista(2, 3, 15);
        return grafo;
    }

    private List<List<Integer>> regionesPara(Grafo grafo, int k) {
        return Regionalizador.encontrarRegiones(Regionalizador.obtenerBosqueConKRegiones(grafo, k));
    }
}