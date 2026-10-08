package negocio.algoritmos;

import java.util.ArrayList;
import java.util.List;
import negocio.grafos.Arista;
import negocio.grafos.Grafo;

// Prim con arreglo simple sobre matriz de adyacencia
public class AlgoritmoPrim {

    public List<Arista> ejecutar(Grafo grafo) {
        int n = grafo.tamano();
        List<Arista> arbol = new ArrayList<>();
        if (n <= 1)
            return arbol;

        boolean[] visitados = new boolean[n];
        int[] distancias = new int[n];
        int[] padres = new int[n];

        for (int i = 0; i < n; i++) {
            distancias[i] = Integer.MAX_VALUE;
            padres[i] = -1;
        }
        distancias[0] = 0;

        for (int i = 0; i < n; i++) {
            int actual = verticePendienteMasCercano(distancias, visitados);
            if (actual == -1)
                break; // el resto es inalcanzable (grafo inconexo)

            visitados[actual] = true;

            for (int vecino : grafo.vecinos(actual)) {
                int peso = grafo.pesoArista(actual, vecino);
                if (!visitados[vecino] && peso < distancias[vecino]) {
                    padres[vecino] = actual;
                    distancias[vecino] = peso;
                }
            }
        }

        for (int v = 1; v < n; v++) {
            if (padres[v] != -1) {
                arbol.add(new Arista(padres[v], v, grafo.pesoArista(padres[v], v)));
            }
        }
        return arbol;
    }

    private int verticePendienteMasCercano(int[] distancias, boolean[] visitados) {
        int minimo = Integer.MAX_VALUE;
        int elegido = -1;
        for (int v = 0; v < distancias.length; v++) {
            if (!visitados[v] && distancias[v] < minimo) {
                minimo = distancias[v];
                elegido = v;
            }
        }
        return elegido;
    }
}