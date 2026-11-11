package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.TipoTemporada;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginAdmin extends JDialog {
    private JPanel mainPanel;
    private JButton btnIngreso;
    private JTextField textUsuario;
    private JPasswordField textContrasenia;
    private JLabel lblFoto;

    private Empresa empresa;

    public LoginAdmin(JFrame parent, Empresa empresa) {
        super(parent, "Login Administrador", true); // true = modal
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(700, 550);
        setResizable(false);
        setLocationRelativeTo(parent);

        estailarComponentes();

        ImageIcon iconOriginal = new ImageIcon(getClass().getResource("/avatar.png"));

        Image imgEscalada = iconOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);

        lblFoto.setIcon(new ImageIcon(imgEscalada));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

        aplicarBordeRedondo(textContrasenia);
        aplicarBordeRedondo(textUsuario);

        initListeners();
        configurarBoton(btnIngreso, "","Ingresar");
    }

    private void estailarComponentes() {
        Dimension tamanoInput = new Dimension(250, 35);
        textUsuario.setPreferredSize(tamanoInput);
        textContrasenia.setPreferredSize(tamanoInput);

        // Colores de fondo para el efecto Hover
        Color fondoNormal = new Color(245, 245, 245);
        Color fondoHover  = new Color(230, 210, 245);

        //Colores de texto para el efecto Hover
        Color textoNormal = new Color(100, 40, 120);
        Color textoHover  = new Color(60, 20, 80);

    }

    private void initListeners() {
        btnIngreso.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String usuario = textUsuario.getText().trim();
        String contrasena = new String(textContrasenia.getPassword()).trim();

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

            textContrasenia.setText("");
            textContrasenia.requestFocus();
        }
    }
    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;
        boton.setText(titulo);
        boton.setFont(new Font("Vensim Sans Mono", Font.BOLD, 14));
        boton.setForeground(new Color(0x2C3E50));
    }


    private void aplicarBordeRedondo(JTextField campo) {
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
                        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(java.awt.Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15); // 15 es el nivel de curvatura
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10) // Margen interno del texto
        ));
    }
}