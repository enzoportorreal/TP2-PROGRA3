package negocio.grafos;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class CargadorDeDatos {

    public static DatosGrafoJSON cargarDesdeArchivo(String rutaArchivo) throws IOException {
        try (Reader lector = new FileReader(rutaArchivo)) {
            return cargar(lector);
        }
    }

    public static DatosGrafoJSON cargar(Reader lector) {
        DatosGrafoJSON datos;
        try {
            datos = new Gson().fromJson(lector, DatosGrafoJSON.class);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("JSON invalido: " + e.getMessage(), e);
        }

        if (datos == null || datos.getProvincias().isEmpty())
            throw new IllegalArgumentException("El JSON no contiene provincias");

        return datos;
    }
}