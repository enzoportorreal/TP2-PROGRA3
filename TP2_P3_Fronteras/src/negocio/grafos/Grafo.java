package negocio.grafos;

import java.util.HashSet;
import java.util.Set;

// Grafo no dirigido con pesos, representado con matrices de adyacencia
public class Grafo {
    private boolean[][] adyacencia;
    private int[][] pesos;

    public Grafo(int vertices) {
        adyacencia = new boolean[vertices][vertices];
        pesos = new int[vertices][vertices];
    }

    public void agregarArista(int i, int j, int peso) {
        verificarVertice(i);
        verificarVertice(j);
        verificarDistintos(i, j);

        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser estrictamente mayor a 0.");
        }

        adyacencia[i][j] = true;
        adyacencia[j][i] = true;
        pesos[i][j] = peso;
        pesos[j][i] = peso;
    }

    public void eliminarArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);
        verificarDistintos(i, j);
        adyacencia[i][j] = false;
        adyacencia[j][i] = false;
        pesos[i][j] = 0;
        pesos[j][i] = 0;
    }

    public boolean existeArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);
        verificarDistintos(i, j);
        return adyacencia[i][j];
    }

    public int pesoArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);
        return pesos[i][j];
    }

    public int tamano() {
        return adyacencia.length;
    }

    public Set<Integer> vecinos(int i) {
        verificarVertice(i);
        Set<Integer> ret = new HashSet<>();
        for (int j = 0; j < this.tamano(); ++j) {
            if (i != j && adyacencia[i][j]) {
                ret.add(j);
            }
        }
        return ret;
    }

    private void verificarVertice(int i) {
        if (i < 0) throw new IllegalArgumentException("Vertice negativo: " + i);
        if (i >= adyacencia.length) throw new IllegalArgumentException("Vertice excedido: " + i);
    }

    private void verificarDistintos(int i, int j) {
        if (i == j) throw new IllegalArgumentException("Loops no permitidos: " + i);
    }
}