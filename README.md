Cambios en el TP2 (Diseñando regiones)

Para que corra

Faltaban las librerías: Gson y JMapViewer. JMapViewer estaba referenciado con una ruta absoluta de otra compu. Los dos jars van ahora en lib/ y agregados al Build Path.
Los 3 tests que fallaban usaban new SistemaRegiones(), que lee el JSON y depende de Gson. Ahora usan new SistemaRegiones(5).

Negocio

SistemaRegiones ahora valida que el grafo sea conexo antes de generar regiones, y lanza IllegalStateException si no lo es. Antes esa regla estaba solo en la interfaz.
SistemaRegiones ya no usa el número mágico 24: usa el tamaño del grafo.
SistemaRegiones invalida las regiones calculadas cuando cambia el grafo.
SistemaRegiones tiene un método nuevo, pesoFrontera.
reiniciarAristas ahora usa el tamaño real del grafo.
EliminaAristas pasó a llamarse Regionalizador. Sus métodos son obtenerBosqueConKRegiones y encontrarRegiones (ya sin el parámetro k que no usaba). Valida K y devuelve las regiones ordenadas.
BFS ya no usa variables estáticas: usa variables locales y una Queue.
AlgoritmoPrim usa int en vez de double, y tiene nombres más claros.
Grafo: la matriz A pasó a llamarse adyacencia.
CargadorDeDatos ya no depende de SistemaRegiones. Ahora devuelve los datos leídos, cierra el archivo con try-with-resources y propaga los errores en vez de tragárselos.
ProvinciaJSON y FronteraJSON tienen campos privados con getters, y DatosGrafoJSON devuelve listas no modificables.

Interfaz

Se ve el peso de cada frontera sobre el mapa.
Los colores de las regiones se generan según K, así que no se repiten con muchas regiones.
La ventana no es redimensionable (1150x750, bajo el límite de 1366x768).
El constructor de VentanaPrincipal se dividió en métodos chicos y se renombraron las variables.
La tabla de regiones no es editable.
El zoom del mapa ya no se resetea al agregar una arista.
Si falla la carga del JSON, se muestra un mensaje en vez de romperse.
Main muestra un cartel de error si la app no puede iniciar.
[Opcional 3, si lo agregaron] Si ya hay regiones calculadas y se agrega una arista, se recalculan solas.

Tests

Archivos nuevos: RegionalizadorTest, AristaTest, ProvinciaTest y CargadorDeDatosTest. Este último usa JSON en un String, sin archivos.
SistemaRegionesTest se amplió: K inválido, grafo inconexo, conexidad, reinicio de aristas, fronteras resultantes y carga del JSON real.
AlgoritmoPrimTest: el test del grafo inconexo ahora verifica el resultado exacto (1 arista, peso 10).
Se eliminaron EliminaAristas.java y EliminaAristasTest.java, que los reemplazan Regionalizador y RegionalizadorTest.

Para la entrega

Los jars van en lib/.
provincias.json va en la raíz del proyecto.
El .classpath no puede tener rutas absolutas.
