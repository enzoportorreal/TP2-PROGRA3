package presentacion;

import negocio.SistemaRegiones;
import negocio.grafos.Provincia;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String NOMBRE_ISLAS_MALVINAS = "Islas Malvinas";
    private static final String LEYENDA_MALVINAS = "Las Malvinas son argentinas";

    private final SistemaRegiones sistema;
    private JPanel contentPane;
    private JTextField campoK;
    private JTextField campoPeso;
    private JRadioButton radioJson;
    private JRadioButton radioManual;
    private JComboBox<String> comboOrigen;
    private JComboBox<String> comboDestino;
    private JButton botonAgregar;
    private DefaultTableModel modeloTabla;
    private JMapViewer mapViewer;
    private boolean regionesVisibles;

    public VentanaPrincipal(SistemaRegiones sistema) {
        this.sistema = sistema;

        configurarVentana();
        crearMenu();
        crearPanelCalculo();
        crearPanelFuenteDeDatos();
        crearTablaDeRegiones();
        crearMapa();
        mostrarGrafoSinRegiones();
    }

    // ------------------------------------------------------------------
    // Construccion de la interfaz
    // ------------------------------------------------------------------

    private void configurarVentana() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Diseño de Regiones - TP2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 1150, 750); // el enunciado pide no superar 1366 x 768
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    private void crearMenu() {
        JMenuBar barraMenu = new JMenuBar();
        setJMenuBar(barraMenu);

        JMenu menuArchivo = new JMenu("Archivo");
        barraMenu.add(menuArchivo);

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
    }

    private JPanel crearPanel(String titulo, int x, int y, int ancho, int alto) {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, titulo, TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(x, y, ancho, alto);
        panel.setLayout(null);
        contentPane.add(panel);
        return panel;
    }

    private void crearPanelCalculo() {
        JPanel panel = crearPanel("Cálculo", 10, 11, 350, 76);

        JLabel etiquetaK = new JLabel("Regiones (K):");
        etiquetaK.setBounds(10, 31, 100, 14);
        panel.add(etiquetaK);

        campoK = new JTextField();
        campoK.setBounds(100, 28, 40, 30);
        panel.add(campoK);

        JButton botonCalcular = new JButton("Calcular Regiones");
        botonCalcular.setBounds(160, 27, 160, 23);
        botonCalcular.addActionListener(e -> calcularRegiones());
        panel.add(botonCalcular);
    }

    private void crearPanelFuenteDeDatos() {
        JPanel panel = crearPanel("Fuente de Datos", 370, 11, 750, 76);

        radioJson = new JRadioButton("Desde JSON");
        radioJson.setSelected(true);
        radioJson.setBounds(10, 27, 100, 23);
        panel.add(radioJson);

        radioManual = new JRadioButton("Manual");
        radioManual.setBounds(110, 27, 80, 23);
        panel.add(radioManual);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioJson);
        grupo.add(radioManual);

        comboOrigen = new JComboBox<>();
        comboOrigen.setBounds(200, 28, 140, 22);
        panel.add(comboOrigen);

        comboDestino = new JComboBox<>();
        comboDestino.setBounds(350, 28, 140, 22);
        panel.add(comboDestino);

        campoPeso = new JTextField("1");
        campoPeso.setBounds(500, 29, 40, 30);
        panel.add(campoPeso);

        botonAgregar = new JButton("Agregar Arista");
        botonAgregar.setBounds(560, 27, 150, 23);
        botonAgregar.addActionListener(e -> agregarFronteraManual());
        panel.add(botonAgregar);

        for (int id = 0; id < sistema.cantidadProvincias(); id++) {
            String nombre = sistema.obtenerProvincia(id).getNombre();
            comboOrigen.addItem(nombre);
            comboDestino.addItem(nombre);
        }

        radioJson.addActionListener(e -> usarDatosDelJSON());
        radioManual.addActionListener(e -> usarCargaManual());
        actualizarControlesManuales();
    }

    private void crearTablaDeRegiones() {
        modeloTabla = new DefaultTableModel(new Object[] { "Región", "Provincias" }, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 98, 300, 580);
        contentPane.add(scroll);
    }

    private void crearMapa() {
        mapViewer = new JMapViewer();
        mapViewer.setBounds(320, 98, 800, 580);
        new DefaultMapController(mapViewer);
        contentPane.add(mapViewer);

        // Posicion inicial: se fija una sola vez para no pisar el zoom del usuario
        mapViewer.setDisplayPosition(new Coordinate(-43.0, -64.0), 4);
    }

    // ------------------------------------------------------------------
    // Acciones de la interfaz
    // ------------------------------------------------------------------

    private void actualizarControlesManuales() {
        boolean esManual = radioManual.isSelected();
        comboOrigen.setEnabled(esManual);
        comboDestino.setEnabled(esManual);
        campoPeso.setEnabled(esManual);
        botonAgregar.setEnabled(esManual);
    }

    private void usarDatosDelJSON() {
        actualizarControlesManuales();
        try {
            sistema.recargarDesdeJSON();
        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar los datos del archivo: " + e.getMessage());
        }
        mostrarGrafoSinRegiones();
    }

    private void usarCargaManual() {
        actualizarControlesManuales();
        sistema.reiniciarAristas();
        mostrarGrafoSinRegiones();
    }

    private void agregarFronteraManual() {
        try {
            int origen = comboOrigen.getSelectedIndex();
            int destino = comboDestino.getSelectedIndex();
            int peso = Integer.parseInt(campoPeso.getText().trim());

            if (origen == destino) {
                mostrarError("No se permiten bucles: el origen y el destino deben ser distintos.");
                return;
            }
            if (peso <= 0) {
                mostrarError("El peso debe ser un número entero mayor a 0.");
                return;
            }

            boolean habiaRegiones = regionesVisibles;
            sistema.registrarFrontera(origen, destino, peso);
            mostrarGrafoSinRegiones();

            if (habiaRegiones) {
                calcularRegiones(); // recalcula con el grafo modificado
            }

        } catch (NumberFormatException e) {
            mostrarError("Por favor, ingrese un número entero válido en el campo de peso.");
        }
    }

    private void calcularRegiones() {
        if (!sistema.esGrafoConexo()) {
            mostrarAdvertencia("Mapa Incompleto",
                    "El mapa debe estar completamente conectado (grafo conexo) para poder calcular las regiones. "
                            + "Por favor, agregue las fronteras faltantes.");
            return;
        }

        int k;
        try {
            k = Integer.parseInt(campoK.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("Por favor, ingrese un número válido en el campo K.");
            return;
        }

        if (k <= 0 || k > sistema.cantidadProvincias()) {
            mostrarAdvertencia("K Inválido",
                    "Por favor, ingrese un número de regiones válido (entre 1 y " + sistema.cantidadProvincias() + ").");
            return;
        }

        List<List<Integer>> regiones = sistema.generarRegiones(k);
        mostrarRegionesEnTabla(regiones);
        actualizarMapa(regiones);
        regionesVisibles = true;
    }

    private void mostrarRegionesEnTabla(List<List<Integer>> regiones) {
        modeloTabla.setRowCount(0);
        for (int i = 0; i < regiones.size(); i++) {
            List<String> nombres = new ArrayList<>();
            for (int id : regiones.get(i)) {
                nombres.add(sistema.obtenerProvincia(id).getNombre());
            }
            modeloTabla.addRow(new String[] { String.valueOf(i + 1), String.join(", ", nombres) });
        }
    }

    private void mostrarGrafoSinRegiones() {
        regionesVisibles = false;
        modeloTabla.setRowCount(0);
        actualizarMapa(null);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    // ------------------------------------------------------------------
    // Dibujo del mapa (regiones == null: todavia no se calcularon regiones)
    // ------------------------------------------------------------------

    private void actualizarMapa(List<List<Integer>> regiones) {
        mapViewer.removeAllMapMarkers();
        mapViewer.removeAllMapPolygons();

        boolean hayRegiones = regiones != null;
        if (hayRegiones)
            dibujarProvinciasPorRegion(regiones);
        else
            dibujarProvinciasSinRegion();

        dibujarFronteras(hayRegiones);
    }

    private void dibujarProvinciasSinRegion() {
        for (int id = 0; id < sistema.cantidadProvincias(); id++) {
            mapViewer.addMapMarker(crearMarcadorProvincia(sistema.obtenerProvincia(id), Color.BLACK));
        }
    }

    private void dibujarProvinciasPorRegion(List<List<Integer>> regiones) {
        for (int i = 0; i < regiones.size(); i++) {
            Color color = colorDeRegion(i, regiones.size());
            for (int id : regiones.get(i)) {
                mapViewer.addMapMarker(crearMarcadorProvincia(sistema.obtenerProvincia(id), color));
            }
        }
    }

    private void dibujarFronteras(boolean soloResultantes) {
        int cantidad = sistema.cantidadProvincias();
        for (int i = 0; i < cantidad; i++) {
            for (int j = i + 1; j < cantidad; j++) {
                boolean hayFrontera = soloResultantes
                        ? sistema.existeFronteraResultante(i, j)
                        : sistema.existeFrontera(i, j);

                if (hayFrontera) {
                    dibujarFrontera(sistema.obtenerProvincia(i), sistema.obtenerProvincia(j),
                            sistema.pesoFrontera(i, j), soloResultantes);
                }
            }
        }
    }

    private void dibujarFrontera(Provincia desde, Provincia hasta, int peso, boolean esResultado) {
        Coordinate c1 = new Coordinate(desde.getLat(), desde.getLon());
        Coordinate c2 = new Coordinate(hasta.getLat(), hasta.getLon());

        MapPolygonImpl linea = new MapPolygonImpl(Arrays.asList(c1, c2, c1));
        linea.setColor(esResultado ? Color.BLACK : Color.GRAY);
        mapViewer.addMapPolygon(linea);

        Coordinate puntoMedio = new Coordinate(
                (desde.getLat() + hasta.getLat()) / 2,
                (desde.getLon() + hasta.getLon()) / 2);
        mapViewer.addMapMarker(crearEtiquetaPeso(puntoMedio, peso));
    }

    private Color colorDeRegion(int indice, int cantidadRegiones) {
        return Color.getHSBColor(indice / (float) cantidadRegiones, 0.85f, 0.9f);
    }

    // Marcador que solo dibuja el numero del peso sobre la arista
    private MapMarkerDot crearEtiquetaPeso(Coordinate punto, int peso) {
        final String texto = String.valueOf(peso);
        return new MapMarkerDot("", punto) {
            @Override
            public void paint(Graphics g, Point position, int radio) {
                if (g == null)
                    return;
                g.setColor(Color.DARK_GRAY);
                g.setFont(new Font("Arial", Font.PLAIN, 11));
                g.drawString(texto, position.x - 6, position.y + 4);
            }
        };
    }

    // Se redefine paint para poder dibujar la leyenda de Malvinas en el tamanio que queremos
    private MapMarkerDot crearMarcadorProvincia(Provincia provincia, Color colorFondo) {
        final String leyenda = NOMBRE_ISLAS_MALVINAS.equalsIgnoreCase(provincia.getNombre())
                ? LEYENDA_MALVINAS : "";

        MapMarkerDot marcador = new MapMarkerDot("", new Coordinate(provincia.getLat(), provincia.getLon())) {
            @Override
            public void paint(Graphics g, Point position, int radio) {
                super.paint(g, position, radio);

                if (g != null && !leyenda.isEmpty()) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                    g.drawString(leyenda, position.x + 5, position.y + 5);
                }
            }
        };
        marcador.setBackColor(colorFondo);
        return marcador;
    }
}