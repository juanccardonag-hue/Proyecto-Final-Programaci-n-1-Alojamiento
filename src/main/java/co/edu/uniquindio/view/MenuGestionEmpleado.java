package co.edu.uniquindio.view;

import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class MenuGestionEmpleado extends JDialog{
    private JPanel mainPanel;
    private JButton btnContratar;
    private JButton btnEditar;
    private JButton btnDesvincular;
    private JButton btnVolver;
    private JLabel tittle;
    private Empresa empresa;

    public MenuGestionEmpleado  (JDialog parent, Empresa empresa) {
        super(parent, "Gestión de Empleados", true);
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
        btnContratar.addActionListener(e -> {
            gestionEmpleado formContratar = new gestionEmpleado(this, empresa);
            formContratar.setVisible(true);
        });

        btnEditar.addActionListener(e -> {
            editarRecepcionista formEditar = new editarRecepcionista(this, empresa);
            formEditar.setVisible(true);
        });

        btnDesvincular.addActionListener(e -> {
            desvincularRecepcionista formDesvincular = new desvincularRecepcionista(this, empresa);
            formDesvincular.setVisible(true);
        });

    }

    public void icons() {
        configurarBoton(btnEditar, "/editar.png",
                "Editar");

        configurarBoton(btnDesvincular, "/despedido.png",
                "Desvincular");

        configurarBoton(btnContratar, "/contratado.png",
                "Contratar");
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
