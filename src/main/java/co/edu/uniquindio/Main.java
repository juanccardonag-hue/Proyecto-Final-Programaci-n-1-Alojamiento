package co.edu.uniquindio;

import co.edu.uniquindio.view.UI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Empresa empresa = new Empresa("2718-2026", "Aparta-Suites UQ");
        //interfaz grafica
        SwingUtilities.invokeLater(() -> {
            UI ventana = new UI(empresa);
            ventana.setVisible(true);
        });
    }
}