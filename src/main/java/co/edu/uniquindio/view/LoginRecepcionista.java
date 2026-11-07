package co.edu.uniquindio.view;

import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Recepcionista;

import javax.swing.*;

public class LoginRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField inputUsuario;
    private JTextField inputContrasena;
    private JButton buttonSesion;
    private JButton buttonVolver;

    private Empresa empresa;

    public LoginRecepcionista(JFrame parent, Empresa empresa) {
        super(parent, "Iniciar Sesión - Recepcionista", true);
        this.empresa = empresa;

        setContentPane(contentPane);
        setSize(420, 320);
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void initListeners() {
        buttonSesion.addActionListener(e -> iniciarSesion());
        buttonVolver.addActionListener(e -> dispose());
    }

    private void iniciarSesion() {
        String usuario = inputUsuario.getText().trim();
        String contrasena = inputContrasena.getText().trim();

        //  Validar campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresa tu usuario y contraseña.",
                    "Campos incompletos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Autenticación con la lista de recepcionistas de la empresa
        Recepcionista recepcionistaEncontrado = null;

        if (empresa.getListaRecepcionistas() != null) {
            for (Recepcionista r : empresa.getListaRecepcionistas()) {
                if ((r.getCedula().equalsIgnoreCase(usuario) || r.getCorreo().equalsIgnoreCase(usuario))
                        && r.getContrasenia().equals(contrasena)) {
                    recepcionistaEncontrado = r;
                    break;
                }
            }
        }

        if (recepcionistaEncontrado != null) {
            JOptionPane.showMessageDialog(this,
                    "¡Bienvenido/a, " + recepcionistaEncontrado.getNombre() + "!",
                    "Acceso Concedido 🎉",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

            // Abrir el Menú de Recepcionista
            menuRecepcionista menu = new menuRecepcionista(null, empresa, (Empleado) recepcionistaEncontrado);
            menu.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos.",
                    "Error de Autenticación ❌",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}