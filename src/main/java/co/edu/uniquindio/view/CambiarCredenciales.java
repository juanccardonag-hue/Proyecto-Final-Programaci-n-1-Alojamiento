package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class CambiarCredenciales extends JDialog {
    private JPanel mainPanel;
    private JTextField textNuevoUsuario;
    private JButton btnGuardar;
    private JPasswordField textNuevaContrasenia;
    private JButton btnCancelar;

    private Empresa empresa;

    public CambiarCredenciales (JDialog parent, Empresa empresa) {
        super(parent, "Cambiar credenciales de admin", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(700, 450);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textNuevoUsuario);
        aplicarBordeRedondo(textNuevaContrasenia);

        configurarBoton(btnCancelar, "","Cancelar");
        configurarBoton(btnGuardar, "","Guardar");

        initListeners();
    }

    private void initListeners() {
        btnGuardar.addActionListener(e -> {
            String nuevoUser = textNuevoUsuario.getText().trim();
            String nuevaContrasena = new String(textNuevaContrasenia.getPassword()).trim();

            if (nuevoUser.isEmpty() || nuevaContrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Por favor completa todos los campos ⚠",
                        "Campos Vacíos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Administrador admin = empresa.getAdministrador();
            boolean exito = admin.cambiarCredenciales(nuevoUser, nuevaContrasena);

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "¡Credenciales actualizadas correctamente!",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠ No se pudieron actualizar las credenciales ⚠",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }
    private void aplicarBordeRedondo(JTextField campo) {
        if (campo == null) return;
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
                        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(java.awt.Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormal = 48;
        int tamZoom = 54;

        Image imgBase = null;
        try {
            ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaIcono));
            imgBase = iconOriginal.getImage();
            boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormal, tamNormal, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            System.err.println("No se encontró la imagen: " + rutaIcono);
        }

        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);

        boton.setMargin(new Insets(15, 10, 10, 10));

        String html = "<html><center><font face='Vensim Sans Mono'>"
                + "<b><font size='4' color='#2C3E50'>" + titulo + "</font></b><br>"
                + "</font></center></html>";

        boton.setText(html);

        final Image imagenOriginal = imgBase;
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoom, tamZoom, Image.SCALE_SMOOTH)));
                }
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormal, tamNormal, Image.SCALE_SMOOTH)));
                }
            }
        });
    }
}
