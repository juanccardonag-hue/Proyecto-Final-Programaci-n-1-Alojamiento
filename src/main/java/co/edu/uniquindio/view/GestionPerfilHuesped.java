package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.awt.*;

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

        setContentPane(contentPane);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        cargarDatosHuesped();
        initListeners();
    }

    private void cargarDatosHuesped() {
        if (huesped != null) {
            // Cédula en solo lectura
            inputCedula.setText(huesped.getCedula() != null ? huesped.getCedula() : "");
            inputCedula.setEditable(false);
            inputCedula.setFocusable(false);
            inputCedula.setBackground(new Color(240, 240, 240));

            // Datos modificables
            inputNombre.setText(huesped.getNombre() != null ? huesped.getNombre() : "");
            inputFechaNacimiento.setText(huesped.getFechaNacimiento() != null ? huesped.getFechaNacimiento().toString() : "");
            inputTelefono.setText(huesped.getTelefono() != null ? huesped.getTelefono() : "");
            inputCorreo.setText(huesped.getCorreo() != null ? huesped.getCorreo() : "");
            inputContrasena.setText(huesped.getContrasena() != null ? huesped.getContrasena() : "");
        }
    }

    private void initListeners() {
        btnActualizar.addActionListener(e -> actualizarPerfil());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void actualizarPerfil() {
        String nombre = inputNombre.getText().trim();
        String fechaNac = inputFechaNacimiento.getText().trim();
        String telefono = inputTelefono.getText().trim();
        String correo = inputCorreo.getText().trim();
        String contrasena = inputContrasena.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Los campos Nombre, Correo y Contraseña son obligatorios.",
                    "Campos requeridos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (huesped != null) {
            huesped.setNombre(nombre);
            huesped.setCorreo(correo);
            huesped.setContrasena(contrasena);
            huesped.setTelefono(telefono);
        }

        JOptionPane.showMessageDialog(this,
                "¡Perfil actualizado con éxito! ✨",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}