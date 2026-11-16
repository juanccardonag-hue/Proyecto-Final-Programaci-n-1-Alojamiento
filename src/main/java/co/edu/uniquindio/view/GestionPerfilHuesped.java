package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class GestionPerfilHuesped extends JDialog {
    private JPanel contentPane;
    private JTextField inputCedula;
    private JTextField inputNombre;
    private JTextField inputFechaNacimiento;
    private JTextField inputTelefono;
    private JTextField inputCorreo;
    private JTextField inputContrasena;
    private JButton btnActualizar;
    private JButton btnCancelar;

    private Empresa empresa;
    private Huesped huesped;

    public GestionPerfilHuesped(JDialog parent, Empresa empresa, Huesped huesped) {
        super(parent, "Gestión de Perfil - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.huesped = huesped;

        if (contentPane != null) {
            setContentPane(contentPane);
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        cargarDatosHuesped();
        initListeners();
    }

    private void cargarDatosHuesped() {
        if (huesped != null) {
            String cedula = "";
            if (huesped.getCedula() != null) {
                cedula = huesped.getCedula();
            }
            inputCedula.setText(cedula);
            inputCedula.setEditable(false);
            inputCedula.setFocusable(false);
            inputCedula.setBackground(new Color(240, 240, 240));

            String nombre = "";
            if (huesped.getNombre() != null) {
                nombre = huesped.getNombre();
            }
            inputNombre.setText(nombre);

            String fechaNac = "";
            if (huesped.getFechaNacimiento() != null) {
                fechaNac = huesped.getFechaNacimiento().toString();
            }
            inputFechaNacimiento.setText(fechaNac);

            String telefono = "";
            if (huesped.getTelefono() != null) {
                telefono = huesped.getTelefono();
            }
            inputTelefono.setText(telefono);

            String correo = "";
            if (huesped.getCorreo() != null) {
                correo = huesped.getCorreo();
            }
            inputCorreo.setText(correo);

            String contrasena = "";
            if (huesped.getContrasena() != null) {
                contrasena = huesped.getContrasena();
            }
            inputContrasena.setText(contrasena);
        }
    }

    private void initListeners() {
        if (btnActualizar != null) {
            btnActualizar.addActionListener(e -> actualizarPerfil());
        }
        if (btnCancelar != null) {
            btnCancelar.addActionListener(e -> dispose());
        }
    }

    private void actualizarPerfil() {
        String nombre = inputNombre.getText().trim();
        String fechaNacStr = inputFechaNacimiento.getText().trim();
        String telefono = inputTelefono.getText().trim();
        String correo = inputCorreo.getText().trim();
        String contrasena = inputContrasena.getText().trim();

        if (nombre.isEmpty() || fechaNacStr.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Los campos Nombre, Fecha de Nacimiento, Correo y Contraseña son obligatorios.",
                    "Campos requeridos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacStr);

            if (huesped != null) {
                huesped.actualizarPerfil(nombre, fechaNacimiento, telefono, correo, contrasena);
            }

            JOptionPane.showMessageDialog(this,
                    "¡Perfil actualizado con éxito!",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Debe ser AAAA-MM-DD (ej: 1998-05-20).",
                    "Error de Fecha ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}