package negocio.grafos;

import java.util.Collections;
import java.util.List;

public class DatosGrafoJSON {
    private List<ProvinciaJSON> provincias;
    private List<FronteraJSON> fronteras;

    public List<ProvinciaJSON> getProvincias() {
        if (provincias == null)
            return Collections.emptyList();
        return Collections.unmodifiableList(provincias);
    }

    public List<FronteraJSON> getFronteras() {
        if (fronteras == null)
            return Collections.emptyList();
        return Collections.unmodifiableList(fronteras);
    }
}