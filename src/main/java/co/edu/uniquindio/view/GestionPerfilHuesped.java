package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class GestionPerfilHuesped extends JDialog {
    private JPanel contentPane;
    private JTextField inputCedula;
    private JTextField inputNombre;
    private JTextField inputFechaNacimiento;
    private JTextField inputTelefono;
    private JTextField inputCorreo;
    private JTextField inputContrasena;
    private JButton btnActualizar;
    private JButton btnCancelar;

    private Empresa empresa;
    private Huesped huesped;

    public GestionPerfilHuesped(JDialog parent, Empresa empresa, Huesped huesped) {
        super(parent, "Gestión de perfil | Huesped", true);
        this.empresa = empresa;
        this.huesped = huesped;

        if (contentPane != null) {
            setContentPane(contentPane);
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        cargarDatosHuesped();
        initListeners();

        configurarBoton(btnCancelar, "", "Cancelar");
        configurarBoton(btnActualizar, "", "Actualizar");

        aplicarBordeRedondo(inputCedula);
        aplicarBordeRedondo(inputContrasena);
        aplicarBordeRedondo(inputCorreo);
        aplicarBordeRedondo(inputFechaNacimiento);
        aplicarBordeRedondo(inputTelefono);
        aplicarBordeRedondo(inputNombre);
    }

    private void cargarDatosHuesped() {
        if (huesped != null) {
            String cedula = "";
            if (huesped.getCedula() != null) {
                cedula = huesped.getCedula();
            }
            inputCedula.setText(cedula);
            inputCedula.setEditable(false);
            inputCedula.setFocusable(false);
            inputCedula.setBackground(new Color(240, 240, 240));

            String nombre = "";
            if (huesped.getNombre() != null) {
                nombre = huesped.getNombre();
            }
            inputNombre.setText(nombre);

            String fechaNac = "";
            if (huesped.getFechaNacimiento() != null) {
                fechaNac = huesped.getFechaNacimiento().toString();
            }
            inputFechaNacimiento.setText(fechaNac);

            String telefono = "";
            if (huesped.getTelefono() != null) {
                telefono = huesped.getTelefono();
            }
            inputTelefono.setText(telefono);

            String correo = "";
            if (huesped.getCorreo() != null) {
                correo = huesped.getCorreo();
            }
            inputCorreo.setText(correo);

            String contrasena = "";
            if (huesped.getContrasena() != null) {
                contrasena = huesped.getContrasena();
            }
            inputContrasena.setText(contrasena);
        }
    }

    private void initListeners() {
        if (btnActualizar != null) {
            btnActualizar.addActionListener(e -> actualizarPerfil());
        }
        if (btnCancelar != null) {
            btnCancelar.addActionListener(e -> dispose());
        }
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setText(titulo);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setMargin(new Insets(5, 10, 5, 10));

        Image imgBase = null;
        if (rutaIcono != null && !rutaIcono.trim().isEmpty()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaIcono));
                imgBase = iconOriginal.getImage();
                boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                System.err.println("No se encontró la imagen: " + rutaIcono);
            }
        }

        final Image imagenOriginal = imgBase;

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoomIcono, tamZoomIcono, Image.SCALE_SMOOTH)));
                }

                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                boton.revalidate();
                boton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteNormal);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }

                boton.revalidate();
                boton.repaint();
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

    private void actualizarPerfil() {
        String nombre = inputNombre.getText().trim();
        String fechaNacStr = inputFechaNacimiento.getText().trim();
        String telefono = inputTelefono.getText().trim();
        String correo = inputCorreo.getText().trim();
        String contrasena = inputContrasena.getText().trim();

        if (nombre.isEmpty() || fechaNacStr.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Los campos Nombre, Fecha de Nacimiento, Correo y Contraseña son obligatorios.",
                    "Campos requeridos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacStr);

            if (huesped != null) {
                huesped.actualizarPerfil(nombre, fechaNacimiento, telefono, correo, contrasena);
            }

            JOptionPane.showMessageDialog(this,
                    "¡Perfil actualizado con éxito!",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Debe ser AAAA-MM-DD (ej: 1998-05-20).",
                    "Error de Fecha ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}