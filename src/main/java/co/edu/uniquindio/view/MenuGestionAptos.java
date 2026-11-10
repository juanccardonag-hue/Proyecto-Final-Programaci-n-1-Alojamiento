package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class MenuGestionAptos extends JDialog {
    private JPanel mainPanel;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnVolver;
    private JButton btnModificar;

    private Empresa empresa;

    public MenuGestionAptos (JDialog parent, Empresa empresa){
        super(parent, "Gestión de apartamentos", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(750, 500);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        icons();
        initListeners();
    }

    private void initListeners() {
        btnVolver.addActionListener(e -> dispose());
        btnAgregar.addActionListener(e -> {
            AgregarApto formAgregar = new AgregarApto(this, empresa);
            formAgregar.setVisible(true);
        });

        btnModificar.addActionListener(e -> {
            ModificarApto formEditar = new ModificarApto(this, empresa);
            formEditar.setVisible(true);
        });

        btnEliminar.addActionListener(e -> {
            EliminarApto eliminarDialog = new EliminarApto(this, empresa);
            eliminarDialog.setVisible(true);
        });

    }

    public void icons() {
        configurarBoton(btnModificar, "/editar.png",
                "Modificar");

        configurarBoton(btnEliminar, "/eliminar.png",
                "Eliminar");

        configurarBoton(btnAgregar, "/agregar.png",
                "Agregar");
        configurarBoton(btnVolver, "/volver.png",
                "Volver");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormal = 48;
        int tamZoom = 54;

        Image imgBase = null;
        try {
            ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaIcono));
            imgBase = iconOriginal.getImage();
            boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormal, tamNormal, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            System.err.println("No se encontró la imagen: " + rutaIcono);
        }

        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);

        boton.setMargin(new Insets(15, 10, 10, 10));

        String html = "<html><center><font face='Vensim Sans Mono'>"
                + "<b><font size='4' color='#2C3E50'>" + titulo + "</font></b><br>"
                + "</font></center></html>";

        boton.setText(html);

        final Image imagenOriginal = imgBase;
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoom, tamZoom, Image.SCALE_SMOOTH)));
                }
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormal, tamNormal, Image.SCALE_SMOOTH)));
                }
            }
        });
    }
}
