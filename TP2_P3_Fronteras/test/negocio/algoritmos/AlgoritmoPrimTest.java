package negocio.algoritmos;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import negocio.grafos.Arista;
import negocio.grafos.Grafo;

public class AlgoritmoPrimTest {

    private AlgoritmoPrim prim = new AlgoritmoPrim();

    @Test
    public void grafoVacioTest() {
        Grafo g = new Grafo(0);

        List<Arista> agm = prim.ejecutar(g);

        assertEquals(0, agm.size());
    }

    @Test
    public void grafoUnVerticeTest() {
        Grafo g = new Grafo(1);

        List<Arista> agm = prim.ejecutar(g);

        assertEquals(0, agm.size());
    }

    @Test
    public void grafoInconexoTest() {
        // Dos islas separadas: {0,1} y {2,3}
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 10);
        g.agregarArista(2, 3, 20);

        List<Arista> agm = prim.ejecutar(g);

        // Prim solo alcanza la isla del vertice 0: una unica arista
        assertEquals(1, agm.size());
        assertEquals(10, calcularPesoTotal(agm));
    }

    @Test
    public void grafoCaminoSimpleTest() {
        // Grafo en forma de linea recta
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 10);
        g.agregarArista(1, 2, 20);
        g.agregarArista(2, 3, 30);

        List<Arista> agm = prim.ejecutar(g);

        // V=4, debe tener 3 aristas. El unico camino posible suma 60
        assertEquals(3, agm.size());
        assertEquals(60, calcularPesoTotal(agm));
    }

    @Test
    public void grafoCompletoTest() {
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 1);
        g.agregarArista(0, 2, 4);
        g.agregarArista(0, 3, 3);
        g.agregarArista(1, 2, 2);
        g.agregarArista(1, 3, 5);
        g.agregarArista(2, 3, 6);

        List<Arista> agm = prim.ejecutar(g);

        // Las aristas mas baratas que conectan a todos: (0,1)=1, (1,2)=2, (0,3)=3. Total = 6
        assertEquals(3, agm.size());
        assertEquals(6, calcularPesoTotal(agm));
    }

    @Test
    public void grafoConMultiplesCaminosTest() {
        Grafo g = new Grafo(5);
        g.agregarArista(0, 1, 10);
        g.agregarArista(0, 2, 20);
        g.agregarArista(1, 3, 50); // Camino muy caro, Prim lo debe evitar
        g.agregarArista(2, 3, 20);
        g.agregarArista(2, 4, 33); // Camino caro
        g.agregarArista(3, 4, 10);

        List<Arista> agm = prim.ejecutar(g);

        // Arbol optimo: 0-1 (10), 0-2 (20), 2-3 (20), 3-4 (10). Total = 60
        assertEquals(4, agm.size());
        assertEquals(60, calcularPesoTotal(agm));
    }

    // metodo auxiliar
    private int calcularPesoTotal(List<Arista> agm) {
        int total = 0;
        for (Arista a : agm) {
            total += a.getPeso();
        }
        return total;
    }
}