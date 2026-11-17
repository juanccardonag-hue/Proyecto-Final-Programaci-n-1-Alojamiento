package co.edu.uniquindio.view;

import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class EditarRecepcionista extends JDialog {

    private JTextField textCedula;
    private JButton btnBuscar;
    private JTextField textNombre;
    private JTextField textCorreo;
    private JButton btnGuardar;
    private JPanel mainPanel;
    private JTextField textTelefono;
    private JButton btnCancelar;

    private Empresa empresa;
    private Empleado empleadoEncontrado;

    public EditarRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Editar Recepcionista", true);
        this.empresa = empresa;

        if (mainPanel != null) {
            setContentPane(mainPanel);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        }

        aplicarBordeRedondo(textCedula);
        aplicarBordeRedondo(textNombre);
        aplicarBordeRedondo(textCorreo);
        aplicarBordeRedondo(textTelefono);

        icons();
        initListeners();

        setSize(680, 480);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initListeners() {
        btnBuscar.addActionListener(e -> {
            String cedula = textCedula.getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Ingresa una cédula para buscar ⚠",
                        "Campo vacío",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Empleado emp = empresa.getAdministrador().buscarEmpleado(empresa.getListaEmpleados(), cedula);

            if (emp != null) {
                this.empleadoEncontrado = emp;
                textNombre.setText(emp.getNombre());
                textTelefono.setText(emp.getTelefono());
                textCorreo.setText(emp.getCorreo());
            } else {
                this.empleadoEncontrado = null;
                limpiarCampos();
                JOptionPane.showMessageDialog(this,
                        "⚠ No se encontró ningún recepcionista con la cédula ingresada ⚠",
                        "No encontrado",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnGuardar.addActionListener(e -> {
            if (empleadoEncontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Debe buscar a un recepcionista antes de guardar los cambios ⚠",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombre = textNombre.getText().trim();
            String telefono = textTelefono.getText().trim();
            String correo = textCorreo.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Todos los campos personales son obligatorios ⚠",
                        "Campos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exito = empresa.getAdministrador().editarEmpleado(
                    empleadoEncontrado,
                    nombre,
                    telefono,
                    correo
            );

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Datos actualizados exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "⚠ No se pudo realizar la actualización ⚠", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void limpiarCampos() {
        textNombre.setText("");
        textTelefono.setText("");
        textCorreo.setText("");
    }

    public void icons() {
        configurarBoton(btnBuscar, "/Buscar.png", "Buscar");
        configurarBoton(btnCancelar, "", "Cancelar");
        configurarBoton(btnGuardar, "", "Guardar");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

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
                boton.setFont(fuenteNormal);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }

                boton.revalidate();
                boton.repaint();
            }
        });
    }

    private void aplicarBordeRedondo(JTextField campo) {
        if (campo == null) return;
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
}