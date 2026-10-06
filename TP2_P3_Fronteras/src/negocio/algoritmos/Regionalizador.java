package negocio.algoritmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import negocio.grafos.Arista;
import negocio.grafos.Grafo;

public class Regionalizador {

    // Arma el AGM del grafo y le saca las (k-1) aristas de mayor peso.
    // El resultado es un bosque con k componentes conexas.
    public static Grafo obtenerBosqueConKRegiones(Grafo grafoOriginal, int k) {
        if (k < 1 || k > grafoOriginal.tamano())
            throw new IllegalArgumentException("Cantidad de regiones invalida: " + k);

        List<Arista> arbol = AlgoritmoPrim.ejecutar(grafoOriginal);
        Collections.sort(arbol, Collections.reverseOrder());

        Grafo bosque = new Grafo(grafoOriginal.tamano());
        for (int i = k - 1; i < arbol.size(); i++) {
            Arista arista = arbol.get(i);
            bosque.agregarArista(arista.getOrigen(), arista.getDestino(), arista.getPeso());
        }
        return bosque;
    }

    // Cada componente conexa del bosque es una region
    public static List<List<Integer>> encontrarRegiones(Grafo bosque) {
        List<List<Integer>> regiones = new ArrayList<>();
        boolean[] asignados = new boolean[bosque.tamano()];

        for (int vertice = 0; vertice < bosque.tamano(); vertice++) {
            if (!asignados[vertice]) {
                Set<Integer> componente = BFS.alcanzables(bosque, vertice);

                List<Integer> region = new ArrayList<>(componente);
                Collections.sort(region);
                regiones.add(region);

                for (int miembro : componente) {
                    asignados[miembro] = true;
                }
            }
        }
        return regiones;
    }
}