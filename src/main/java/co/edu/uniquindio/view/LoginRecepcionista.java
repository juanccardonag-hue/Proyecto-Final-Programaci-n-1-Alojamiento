package co.edu.uniquindio.view;

import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Recepcionista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField inputUsuario;
    JPasswordField txtContrasena;
    private JButton buttonSesion;
    private JButton buttonVolver;

    private Empresa empresa;

    public LoginRecepcionista(JFrame parent, Empresa empresa) {
        super(parent, "Iniciar Sesión - Recepcionista", true);
        this.empresa = empresa;

        if (contentPane != null) {
            contentPane.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
            setContentPane(contentPane);
        }

        aplicarEstilos();
        initListeners();

        setSize(580, 420);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        Dimension tamanoInput = new Dimension(280, 35);
        if (inputUsuario != null) {
            inputUsuario.setPreferredSize(tamanoInput);
            aplicarBordeRedondo(inputUsuario);
        }
        if (txtContrasena != null) {
            txtContrasena.setPreferredSize(tamanoInput);
            aplicarBordeRedondo(txtContrasena);
        }

        configurarBoton(buttonSesion, "Ingresar");
        configurarBoton(buttonVolver, "Volver");
    }

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;
        boton.setText(titulo);
        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteHover = new Font("Vensim Sans Mono", Font.BOLD, 15);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setFont(fuenteHover);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setFont(fuenteNormal);
            }
        });
    }

    private void aplicarBordeRedondo(JTextField campo) {
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

    private void initListeners() {
        if (buttonSesion != null) buttonSesion.addActionListener(e -> iniciarSesion());
        if (buttonVolver != null) buttonVolver.addActionListener(e -> dispose());
    }

    private void iniciarSesion() {
        String usuario = inputUsuario.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresa tu usuario y contraseña.",
                    "Campos incompletos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Recepcionista recepcionistaEncontrado = null;

        if (empresa.getListaRecepcionistas() != null) {
            for (Recepcionista r : empresa.getListaRecepcionistas()) {
                boolean coincideUsuario = (r.getUsuario() != null && r.getUsuario().equalsIgnoreCase(usuario))
                        || (r.getCedula() != null && r.getCedula().equalsIgnoreCase(usuario))
                        || (r.getCorreo() != null && r.getCorreo().equalsIgnoreCase(usuario));

                if (coincideUsuario && r.getContrasenia() != null && r.getContrasenia().equals(contrasena)) {
                    recepcionistaEncontrado = r;
                    break;
                }
            }
        }

        if (recepcionistaEncontrado != null) {
            JOptionPane.showMessageDialog(this,
                    "¡Bienvenido/a, " + recepcionistaEncontrado.getNombre() + "!",
                    "Acceso Concedido ",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

            MenuRecepcionista menu = new MenuRecepcionista(null, empresa, (Empleado) recepcionistaEncontrado);
            menu.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos.",
                    "Error de Autenticación ",
                    JOptionPane.ERROR_MESSAGE);

            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
    }
}