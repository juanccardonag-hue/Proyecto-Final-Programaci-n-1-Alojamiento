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

        if (contentPane != null) {
            setContentPane(contentPane);
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void initListeners() {
        if (buttonSesion != null) {
            buttonSesion.addActionListener(e -> iniciarSesion());
        }
        if (buttonVolver != null) {
            buttonVolver.addActionListener(e -> dispose());
        }
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

        Empleado recepcionistaEncontrado = null;

        // 2. Buscar primero en listaRecepcionistas si existe
        if (empresa.getListaRecepcionistas() != null) {
            for (Recepcionista r : empresa.getListaRecepcionistas()) {
                boolean coincideId = (r.getUsuario() != null && r.getUsuario().equalsIgnoreCase(usuario))
                        || (r.getCedula() != null && r.getCedula().equalsIgnoreCase(usuario))
                        || (r.getCorreo() != null && r.getCorreo().equalsIgnoreCase(usuario));

                boolean coincideClave = r.getContrasenia() != null && r.getContrasenia().equals(contrasena);

                if (coincideId && coincideClave) {
                    recepcionistaEncontrado = r;
                    break;
                }
            }
        }

        // 3. Si no se encontró arriba, buscar en listaEmpleados (para los creados desde gestionEmpleado)
        if (recepcionistaEncontrado == null && empresa.getListaEmpleados() != null) {
            for (Empleado emp : empresa.getListaEmpleados()) {
                if (emp instanceof Recepcionista || emp.getClass().getSimpleName().equalsIgnoreCase("Recepcionista") || emp.getUsuario() != null) {
                    boolean coincideId = (emp.getUsuario() != null && emp.getUsuario().equalsIgnoreCase(usuario))
                            || (emp.getCedula() != null && emp.getCedula().equalsIgnoreCase(usuario))
                            || (emp.getCorreo() != null && emp.getCorreo().equalsIgnoreCase(usuario));

                    boolean coincideClave = emp.getContrasenia() != null && emp.getContrasenia().equals(contrasena);

                    if (coincideId && coincideClave) {
                        recepcionistaEncontrado = emp;
                        break;
                    }
                }
            }
        }

        // 4. Resultado de la autenticación
        if (recepcionistaEncontrado != null) {
            JOptionPane.showMessageDialog(this,
                    "¡Bienvenido/a, " + recepcionistaEncontrado.getNombre() + "!",
                    "Acceso Concedido 🎉",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose(); // Cerrar ventana de Login

            // Abrir Menú de Recepcionista
            menuRecepcionista menu = new menuRecepcionista(null, empresa, recepcionistaEncontrado);
            menu.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos.",
                    "Error de Autenticación ❌",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}