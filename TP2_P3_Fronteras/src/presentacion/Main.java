package presentacion;

import java.awt.EventQueue;
import javax.swing.JOptionPane;
import negocio.SistemaRegiones;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SistemaRegiones sistema = new SistemaRegiones();
                VentanaPrincipal ventana = new VentanaPrincipal(sistema);
                ventana.setLocationRelativeTo(null);
                ventana.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "No se pudo iniciar la aplicación: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}