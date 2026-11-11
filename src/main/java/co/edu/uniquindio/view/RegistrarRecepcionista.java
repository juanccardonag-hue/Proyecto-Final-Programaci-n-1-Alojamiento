package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RegistrarRecepcionista extends JDialog {
    private JPanel mainPanel;
    private JTextField textUsuario;
    private JTextField textCedula;
    private JTextField textNombre;
    private JPasswordField textContrasenia;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Empresa empresa;

    public RegistrarRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Registrar Credenciales de Recepcionista", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(600, 480);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textCedula);
        aplicarBordeRedondo(textNombre);
        aplicarBordeRedondo(textUsuario);
        aplicarBordeRedondo(textContrasenia);

        if (textNombre != null) {
            textNombre.setEnabled(false);
        }

        configurarBoton(btnCancelar, "Cancelar");
        configurarBoton(btnGuardar, "Guardar Credenciales");

        initListeners();
    }

    private void initListeners() {
        if (textCedula != null) {
            textCedula.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    buscarNombre();
                }
            });
        }

        btnGuardar.addActionListener(e -> registrarCredenciales());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void buscarNombre() {
        String cedula = textCedula.getText().trim();
        if (!cedula.isEmpty()) {
            Empleado emp = empresa.getAdministrador().buscarEmpleado(empresa.getListaEmpleados(), cedula);
            if (emp != null) {
                textNombre.setText(emp.getNombre());
            } else {
                textNombre.setText("");
            }
        }
    }

    private void registrarCredenciales() {
        String cedula = textCedula.getText().trim();
        String usuario = textUsuario.getText().trim();
        String contrasena = new String(textContrasenia.getPassword()).trim();

        if (cedula.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "⚠ La cédula, el usuario y la contraseña son obligatorios ⚠",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Administrador admin = empresa.getAdministrador();
        Empleado emp = admin.buscarEmpleado(empresa.getListaEmpleados(), cedula);

        if (emp == null) {
            JOptionPane.showMessageDialog(this,
                    "⚠ No existe ningún empleado contratado con la cédula: " + cedula + ".\nPrimero debes registrarlo en el módulo de Contratación.",
                    "Empleado no encontrado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exito = admin.registrarCredencialesRecepcionista(
                empresa.getListaEmpleados(),
                empresa.getListaRecepcionistas(),
                cedula, usuario, contrasena
        );

        if (exito) {
            JOptionPane.showMessageDialog(this,
                    "¡Credenciales asignadas exitosamente!\nEl recepcionista " + emp.getNombre() + " ya puede ingresar al Login.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ No se pudieron registrar las credenciales ⚠",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;

        if (titulo != null && !titulo.isEmpty()) {
            boton.setText(titulo);
        }

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setFont(fuenteNormal);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteNormal);
            }
        });
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
}