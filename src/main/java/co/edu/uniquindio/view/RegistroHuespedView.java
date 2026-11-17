package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class RegistroHuespedView extends JDialog {
    private JPanel contentPane;
    private JTextField inputCedula;
    private JTextField inputNombre;
    private JTextField inputFechaNacimiento;
    private JTextField inputTelefono;
    private JTextField inputCorreo;
    private JTextField inputContrasena;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    private Empresa empresa;
    private boolean abiertoDesdeRecepcion;

    public RegistroHuespedView(JFrame parent, Empresa empresa) {
        super(parent, "Registro de Nuevo Huésped", true);
        this.empresa = empresa;
        this.abiertoDesdeRecepcion = (parent == null);

        if (contentPane != null) {
            contentPane.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
            setContentPane(contentPane);
        }

        if (btnRegistrar != null) {
            getRootPane().setDefaultButton(btnRegistrar);
        }

        aplicarEstilos();
        initListeners();

        setSize(560, 620);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        Dimension tamanoInput = new Dimension(300, 35);

        JTextField[] campos = {
                inputCedula,
                inputNombre,
                inputFechaNacimiento,
                inputTelefono,
                inputCorreo,
                inputContrasena
        };

        for (JTextField campo : campos) {
            if (campo != null) {
                campo.setPreferredSize(tamanoInput);
                aplicarBordeRedondo(campo);
            }
        }

        String textoBoton = "Registrarse";
        if (abiertoDesdeRecepcion) {
            textoBoton = "Registrar Huésped";
        }
        configurarBoton(btnRegistrar, textoBoton);
        configurarBoton(btnCancelar, "Cancelar");
    }

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;
        boton.setText(titulo);

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 15);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setFont(fuenteZoom);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
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
        if (btnRegistrar != null) {
            btnRegistrar.addActionListener(e -> registrarHuesped());
        }
        if (btnCancelar != null) {
            btnCancelar.addActionListener(e -> dispose());
        }
    }

    private void registrarHuesped() {
        String cedula = inputCedula.getText().trim();
        String nombre = inputNombre.getText().trim();
        String fechaStr = inputFechaNacimiento.getText().trim();
        String telefono = inputTelefono.getText().trim();
        String correo = inputCorreo.getText().trim();
        String contrasena = inputContrasena.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || fechaStr.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor completa todos los campos obligatorios.",
                    "Campos incompletos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.parse(fechaStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Usa el formato AAAA-MM-DD (ej: 1998-05-20).",
                    "Fecha inválida ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Huesped> listaHuespedes = null;
        if (empresa != null) {
            listaHuespedes = empresa.getListaHuespedes();
        }

        String resultado = Huesped.registrarHuesped(
                listaHuespedes, cedula, nombre, fechaNacimiento, telefono, correo, contrasena
        );

        if ("OK".equals(resultado)) {
            if (abiertoDesdeRecepcion) {
                JOptionPane.showMessageDialog(this,
                        "¡Huésped registrado exitosamente!",
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "¡Registro exitoso! Por favor inicia sesión con tu usuario.",
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();

                JFrame parentFrame = null;
                if (getParent() instanceof JFrame) {
                    parentFrame = (JFrame) getParent();
                }

                LoginHuesped login = new LoginHuesped(parentFrame, empresa);
                login.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ " + resultado,
                    "Error de Registro ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}