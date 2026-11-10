package co.edu.uniquindio.view;

import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class DesvincularRecepcionista extends JDialog {
    private JPanel mainPanel;
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JButton btnDesvincular;
    private JButton btnBuscar;
    private JButton btnCancelar;
    private JLabel tittle;

    private Empresa empresa;
    private Empleado empleadoEncontrado;

    public DesvincularRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Desvincular recepcionista", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(850, 650);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        txtNombre.setEnabled(false);
        txtCorreo.setEnabled(false);

        aplicarBordeRedondo(txtCedula);
        aplicarBordeRedondo(txtNombre);
        aplicarBordeRedondo(txtCorreo);

        icons();
        initListeners();
    }

    private void initListeners() {
        btnBuscar.addActionListener(e -> {
            String cedula = txtCedula.getText().trim();
            Empleado emp = empresa.getAdministrador().buscarEmpleado(empresa.getListaEmpleados(), cedula);

            if (emp != null) {
                this.empleadoEncontrado = emp;
                txtNombre.setText(emp.getNombre());
                txtCorreo.setText(emp.getCorreo());
            } else {
                this.empleadoEncontrado = null;
                txtNombre.setText("");
                txtCorreo.setText("");
                JOptionPane.showMessageDialog(this,
                        "⚠ No se encontró ningún recepcionista con esa cédula ⚠",
                        "No encontrado",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDesvincular.addActionListener(e -> {
            if (empleadoEncontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Busca un recepcionista antes de desvincular ⚠",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exito = empresa.getAdministrador().desvincularEmpleado(
                    empresa.getListaEmpleados(),
                    empleadoEncontrado.getCedula()
            );

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "¡Recepcionista desvinculado correctamente!",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠ No se pudo realizar la desvinculación ⚠",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public void icons() {
        configurarBoton(btnBuscar, "/Buscar.png", "Buscar");
        configurarBoton(btnCancelar, "", "Cancelar");
        configurarBoton(btnDesvincular, "", "Desvincular");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom   = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setText(titulo);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setMargin(new Insets(5, 10, 5, 10));

        Image imgBase = null;
        if (rutaIcono != null && !rutaIcono.trim().isEmpty()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaIcono));
                imgBase = iconOriginal.getImage();
                boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                System.err.println("No se encontró la imagen: " + rutaIcono);
            }
        }

        final Image imagenOriginal = imgBase;

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoomIcono, tamZoomIcono, Image.SCALE_SMOOTH)));
                }

                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                boton.revalidate();
                boton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // 1. Restaura el texto
                boton.setFont(fuenteNormal);

                // 2. Restaura el ícono
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }

                boton.revalidate();
                boton.repaint();
            }
        });
    }

    private void aplicarBordeRedondo(JTextField campo) {
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
                        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(java.awt.Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15); // 15 es el nivel de curvatura
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10) // Margen interno del texto
        ));
    }
}