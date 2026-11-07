package co.edu.uniquindio.view;

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
        // Evento botón Iniciar Sesión
        buttonSesion.addActionListener(e -> iniciarSesion());

        // Evento botón Volver
        buttonVolver.addActionListener(e -> dispose());
    }

    private void iniciarSesion() {
        String usuario = inputUsuario.getText().trim();
        String contrasena = inputContrasena.getText().trim();

        // 1. Validar campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresa tu usuario y contraseña.",
                    "Campos incompletos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Autenticación con la lista de recepcionistas de la empresa
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

        // 3. Resultado de la autenticación
        if (recepcionistaEncontrado != null) {
            JOptionPane.showMessageDialog(this,
                    "¡Bienvenido/a, " + recepcionistaEncontrado.getNombre() + "!",
                    "Acceso Concedido 🎉",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose(); // Cierra esta ventana modal

            // Abrir el menú principal del recepcionista
            // MenuRecepcionistaView menu = new MenuRecepcionistaView((JFrame) getParent(), empresa, recepcionistaEncontrado);
            // menu.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos.",
                    "Error de Autenticación ❌",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}