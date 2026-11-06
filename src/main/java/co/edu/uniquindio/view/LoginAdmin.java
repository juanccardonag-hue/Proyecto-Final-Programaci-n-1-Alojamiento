package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;

import javax.swing.*;

public class LoginAdmin extends JDialog {
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JLabel tittleLogin;
    private JPasswordField inputPassword;
    private JButton btnIngreso;
    private JButton cancelarBtn;
    private JLabel txtUsuario;
    private JLabel txtContrasena;
    private JTextField inputUsuario;

    private Empresa empresa;

    public LoginAdmin(JFrame parent, Empresa empresa) {
        super(parent, "Login Administrador", true); // true = modal
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void initListeners() {
        btnIngreso.addActionListener(e -> intentarLogin());

        cancelarBtn.addActionListener(e -> dispose());
    }
    private void intentarLogin() {
        String usuario = inputUsuario.getText().trim();
        String contrasena = new String(inputPassword.getPassword()).trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor completa todos los campos.",
                    "Campos vacíos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Administrador admin = empresa.getAdministrador();

        if (admin != null && admin.autenticar(usuario, contrasena)) {
            JOptionPane.showMessageDialog(this,
                    "¡Bienvenido/a Administrador/a " + admin.getNombre() + "! ˶˃ᵕ˂˶",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
            empresa.abrirMenuAdmin();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ Usuario o contraseña incorrectos",
                    "Error de Autenticación ⚠",
                    JOptionPane.ERROR_MESSAGE);

            inputPassword.setText("");
            inputPassword.requestFocus();
        }
    }
}
