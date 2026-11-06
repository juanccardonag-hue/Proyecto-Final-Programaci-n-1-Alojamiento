package co.edu.uniquindio.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PanelConFondo extends JPanel {

    private Image imagenFondo;

    public PanelConFondo(String nombreArchivo) {
        // Busca la imagen directamente en src/main/resources/
        URL url = getClass().getResource(nombreArchivo);
        if (url != null) {
            this.imagenFondo = new ImageIcon(url).getImage();
        } else {
            System.err.println("⚠ No se encontró la imagen: " + nombreArchivo);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            // Estira la imagen para cubrir toda la ventana sin importar su tamaño
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}