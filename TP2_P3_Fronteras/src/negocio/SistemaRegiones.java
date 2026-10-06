package negocio;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import negocio.algoritmos.BFS;
import negocio.algoritmos.Regionalizador;
import negocio.grafos.CargadorDeDatos;
import negocio.grafos.DatosGrafoJSON;
import negocio.grafos.FronteraJSON;
import negocio.grafos.Grafo;
import negocio.grafos.Provincia;
import negocio.grafos.ProvinciaJSON;

public class SistemaRegiones {

    private static final String ARCHIVO_PROVINCIAS = "provincias.json";

    private Grafo grafo;
    private Grafo grafoResultante;
    private Map<Integer, Provincia> provincias;

    // Carga las provincias y fronteras desde provincias.json
    public SistemaRegiones() {
        cargarDatosDelArchivo();
    }

    // Sistema con N provincias ficticias y sin fronteras (no lee ningun archivo)
    public SistemaRegiones(int cantidadProvincias) {
        provincias = new HashMap<>();
        grafo = new Grafo(cantidadProvincias);
        for (int id = 0; id < cantidadProvincias; id++) {
            provincias.put(id, new Provincia(id, "Prov. " + id, 0, 0));
        }
    }

    public void recargarDesdeJSON() {
        cargarDatosDelArchivo();
    }

    public void registrarFrontera(int id1, int id2, int similaridad) {
        grafo.agregarArista(id1, id2, similaridad);
        grafoResultante = null; // las regiones calculadas quedaron desactualizadas
    }

    public void reiniciarAristas() {
        grafo = new Grafo(grafo.tamano());
        grafoResultante = null;
    }

    public int cantidadProvincias() {
        return grafo.tamano();
    }

    public Provincia obtenerProvincia(int id) {
        return provincias.get(id);
    }

    public boolean existeFrontera(int i, int j) {
        return grafo.existeArista(i, j);
    }

    public int pesoFrontera(int i, int j) {
        return grafo.pesoArista(i, j);
    }

    public boolean existeFronteraResultante(int i, int j) {
        return grafoResultante != null && grafoResultante.existeArista(i, j);
    }

    public boolean esGrafoConexo() {
        return BFS.esConexo(grafo);
    }

    public List<List<Integer>> generarRegiones(int k) {
        if (k <= 0 || k > grafo.tamano())
            throw new IllegalArgumentException("Cantidad de regiones K invalida: " + k);

        if (!esGrafoConexo())
            throw new IllegalStateException("El grafo debe ser conexo para generar regiones");

        grafoResultante = Regionalizador.obtenerBosqueConKRegiones(grafo, k);
        return Regionalizador.encontrarRegiones(grafoResultante);
    }

    private void cargarDatosDelArchivo() {
        DatosGrafoJSON datos;
        try {
            datos = CargadorDeDatos.cargarDesdeArchivo(ARCHIVO_PROVINCIAS);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo " + ARCHIVO_PROVINCIAS, e);
        }

        Map<Integer, Provincia> nuevasProvincias = new HashMap<>();
        for (ProvinciaJSON p : datos.getProvincias()) {
            nuevasProvincias.put(p.getId(), new Provincia(p.getId(), p.getNombre(), p.getLat(), p.getLon()));
        }

        Grafo nuevoGrafo = new Grafo(nuevasProvincias.size());
        for (FronteraJSON f : datos.getFronteras()) {
            nuevoGrafo.agregarArista(f.getOrigen(), f.getDestino(), f.getPeso());
        }

        provincias = nuevasProvincias;
        grafo = nuevoGrafo;
        grafoResultante = null;
    }
}