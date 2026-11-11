package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class Contratar extends JDialog {
    private JPanel mainPanel;
    private JTextField textCedula;
    private JTextField txtTelefono;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Empresa empresa;
    private Empleado empleadoEditar;

    public Contratar(JDialog parent, Empresa empresa) {
        this(parent, empresa, null);
    }

    public Contratar(JDialog parent, Empresa empresa, Empleado empleadoEditar) {
        super(parent, "Contratar Recepcionista", true);
        this.empresa = empresa;
        this.empleadoEditar = empleadoEditar;

        if (empleadoEditar == null) {
            setTitle("Contratar Nuevo Recepcionista");
        } else {
            setTitle("Editar Recepcionista");
        }

        setContentPane(mainPanel);
        setSize(800, 650);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textCedula);
        aplicarBordeRedondo(txtNombre);
        aplicarBordeRedondo(txtCorreo);
        aplicarBordeRedondo(txtTelefono);

        if (this.empleadoEditar != null) {
            cargarDatosEmpleado();
        }

        initListeners();
        configurarBoton(btnCancelar, "Cancelar");
        configurarBoton(btnGuardar, "Guardar");
    }

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;

        if (titulo != null && !titulo.isEmpty()) {
            boton.setText(titulo);
        }

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setFont(fuenteNormal);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteNormal);
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
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void cargarDatosEmpleado() {
        textCedula.setText(empleadoEditar.getCedula());
        textCedula.setEnabled(false);
        txtNombre.setText(empleadoEditar.getNombre());
        txtTelefono.setText(empleadoEditar.getTelefono());
        txtCorreo.setText(empleadoEditar.getCorreo());
    }

    private void initListeners() {
        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarEmpleado() {
        String cedula = textCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Cédula y nombre son obligatorios ⚠",
                    "Campos Incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Administrador admin = empresa.getAdministrador();
        boolean exito;

        if (empleadoEditar == null) {
            Empleado nuevoEmp = new Empleado(cedula, nombre, LocalDate.now(), telefono, correo, "", "");
            exito = admin.contratarEmpleado(empresa.getListaEmpleados(), nuevoEmp);
        } else {
            exito = admin.editarEmpleado(empleadoEditar, nombre, telefono, correo);
        }

        if (exito) {
            JOptionPane.showMessageDialog(this,
                    (empleadoEditar == null ? "¡Empleado contratado exitosamente!" : "¡Datos actualizados!"),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ No se pudo registrar. La cédula ya existe ⚠",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}