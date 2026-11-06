package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(parent);

        estailarComponentes();

        initListeners();
    }

    private void estailarComponentes() {
        Dimension tamanoInput = new Dimension(250, 35);
        inputUsuario.setPreferredSize(tamanoInput);
        inputPassword.setPreferredSize(tamanoInput);

        // Colores de fondo para el efecto Hover
        Color fondoNormal = new Color(245, 245, 245);
        Color fondoHover  = new Color(230, 210, 245);

        //Colores de texto para el efecto Hover
        Color textoNormal = new Color(100, 40, 120);
        Color textoHover  = new Color(60, 20, 80);

        aplicarEfectoHover(btnIngreso, fondoNormal, fondoHover, textoNormal, textoHover);
        aplicarEfectoHover(cancelarBtn, fondoNormal, fondoHover, textoNormal, textoHover);
    }

    private void aplicarEfectoHover(JButton boton, Color colorNormal, Color colorHover, Color textoNormal, Color textoHover) {
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setBackground(colorNormal);
        boton.setForeground(textoNormal);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorHover);
                boton.setForeground(textoHover); // Cambia el color de la letra al entrar
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorNormal);
                boton.setForeground(textoNormal); // Regresa el color de la letra al salir
            }
        });
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

            MenuAdmin menuAdminWindow = new MenuAdmin((JFrame) getOwner(), empresa);
            menuAdminWindow.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ Usuario o contraseña incorrectos ⚠ ",
                    "⚠ Error de Autenticación ⚠",
                    JOptionPane.ERROR_MESSAGE);

            inputPassword.setText("");
            inputPassword.requestFocus();
        }
    }
}