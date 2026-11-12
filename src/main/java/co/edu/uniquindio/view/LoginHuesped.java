package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginHuesped extends JDialog {
    private JPanel contentPane;
    private JButton btnIngreso;
    private JTextField inputCedula;
    private JPasswordField inputPassword;
    private JButton buttonCancel;

    private Empresa empresa;
    private JFrame parentFrame;

    public LoginHuesped(JFrame parent, Empresa empresa) {
        super(parent, "Login Huésped", true);
        this.empresa = empresa;
        this.parentFrame = parent;

        if (contentPane != null) {
            contentPane.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
            setContentPane(contentPane);
        }

        aplicarEstilos();
        initListeners();

        setSize(520, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        Dimension tamanoInput = new Dimension(280, 35);
        if (inputCedula != null) {
            inputCedula.setPreferredSize(tamanoInput);
            aplicarBordeRedondo(inputCedula);
        }
        if (inputPassword != null) {
            inputPassword.setPreferredSize(tamanoInput);
            aplicarBordeRedondo(inputPassword);
        }

        configurarBoton(btnIngreso, "Ingresar");
        configurarBoton(buttonCancel, "Volver");
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
        if (btnIngreso != null) btnIngreso.addActionListener(e -> intentarLogin());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());
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

        Huesped huespedAutenticado = null;
        if (empresa.getListaHuespedes() != null) {
            for (Huesped h : empresa.getListaHuespedes()) {
                if ((h.getCedula().equalsIgnoreCase(cedula) || h.getCorreo().equalsIgnoreCase(cedula))
                        && h.getContrasena().equals(contrasena)) {
                    huespedAutenticado = h;
                    break;
                }
            }
        }

        if (huespedAutenticado != null) {
            JOptionPane.showMessageDialog(this,
                    "¡Bienvenido/a Huésped " + huespedAutenticado.getNombre() + "! ˶˃ᵕ˂˶",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

            MenuHuesped menu = new MenuHuesped(parentFrame, empresa, huespedAutenticado);
            menu.setVisible(true);
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