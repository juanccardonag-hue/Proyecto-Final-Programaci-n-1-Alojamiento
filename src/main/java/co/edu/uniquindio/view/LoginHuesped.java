package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;

public class LoginHuesped extends JDialog {
    // Variables vinculadas exactamente con el Component Tree del diseñador .form
    private JPanel contentPane;
    private JButton btnIngreso;
    private JTextField inputCedula;
    private JPasswordField inputPassword;
    private JButton buttonCancel;

    private Empresa empresa;

    public LoginHuesped(JFrame parent, Empresa empresa) {
        super(parent, "Login Huésped", true);
        this.empresa = empresa;

        setContentPane(contentPane);
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void initListeners() {
        btnIngreso.addActionListener(e -> intentarLogin());
        buttonCancel.addActionListener(e -> dispose());
    }

    private void intentarLogin() {
        String cedula = inputCedula.getText().trim();
        String contrasena = new String(inputPassword.getPassword()).trim();

        if (cedula.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor completa todos los campos.",
                    "Campos vacíos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación del huésped por cédula o correo
        Huesped huespedAutenticado = null;
        for (Huesped h : empresa.getListaHuespedes()) {
            if ((h.getCedula().equalsIgnoreCase(cedula) || h.getCorreo().equalsIgnoreCase(cedula))
                    && h.getContrasena().equals(contrasena)) {
                huespedAutenticado = h;
                break;
            }
        }

        if (huespedAutenticado != null) {
            JOptionPane.showMessageDialog(this,
                    "¡Bienvenido/a Huésped " + huespedAutenticado.getNombre() + "! ˶˃ᵕ˂˶",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
            empresa.abrirMenuHuesped();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ Cédula/correo o contraseña incorrectos",
                    "Error de Autenticación ⚠",
                    JOptionPane.ERROR_MESSAGE);

            inputPassword.setText("");
            inputPassword.requestFocus();
        }
    }
}