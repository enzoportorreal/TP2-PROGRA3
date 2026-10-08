package negocio.algoritmos;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import negocio.grafos.Grafo;

public class BFS {

    public boolean esConexo(Grafo grafo) {
        if (grafo == null)
            throw new IllegalArgumentException("El grafo no puede ser null.");

        return grafo.tamano() == 0 || alcanzables(grafo, 0).size() == grafo.tamano();
    }

    public Set<Integer> alcanzables(Grafo grafo, int origen) {
        Set<Integer> alcanzados = new HashSet<>();
        Queue<Integer> pendientes = new LinkedList<>();

        alcanzados.add(origen);
        pendientes.add(origen);

        while (!pendientes.isEmpty()) {
            int actual = pendientes.remove();
            for (int vecino : grafo.vecinos(actual)) {
                if (alcanzados.add(vecino)) {
                    pendientes.add(vecino);
                }
            }
        }
        return alcanzados;
    }
}