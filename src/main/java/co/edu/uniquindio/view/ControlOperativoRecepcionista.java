package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlOperativoRecepcionista extends JDialog {
    private JPanel contentPane;
    private JComboBox<Apartamento> comboApartamentos;
    private JComboBox<EstadoOperativo> comboEstados;
    private JTextField textMotivo;
    private JButton btnActualizar;
    private JButton btnVolver;

    private Empresa empresa;

    public ControlOperativoRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Control Operativo de Apartamentos", true);
        this.empresa = empresa;

        if (contentPane != null) {
            setContentPane(contentPane);
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        cargarApartamentos();
        cargarEstadosOperativos();
        aplicarEstilos();
        initListeners();

        setSize(520, 380);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void cargarApartamentos() {
        if (comboApartamentos != null && empresa != null && empresa.getListaApartamentos() != null) {
            comboApartamentos.removeAllItems();
            for (Apartamento a : empresa.getListaApartamentos()) {
                comboApartamentos.addItem(a);
            }
        }
    }

    private void cargarEstadosOperativos() {
        if (comboEstados != null) {
            comboEstados.removeAllItems();
            for (EstadoOperativo est : EstadoOperativo.values()) {
                comboEstados.addItem(est);
            }
        }
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(textMotivo);

        icons();
    }

    public void icons() {
        configurarBoton(btnActualizar, "", "Actualizar Estado");
        configurarBoton(btnVolver, "", "Volver al Menú");
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

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Segoe UI", Font.BOLD, 14);
        Font fuenteZoom   = new Font("Segoe UI", Font.BOLD, 16);

        boton.setText(titulo);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setMargin(new Insets(5, 10, 5, 10));

        Image imgBase = null;
        if (rutaIcono != null && !rutaIcono.trim().isEmpty()) {
            try {
                java.net.URL resource = getClass().getResource(rutaIcono);
                if (resource != null) {
                    ImageIcon iconOriginal = new ImageIcon(resource);
                    imgBase = iconOriginal.getImage();
                    boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }
            } catch (Exception e) {
                System.err.println("No se encontró la imagen: " + rutaIcono);
            }
        }

        final Image imagenOriginal = imgBase;

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setFont(fuenteZoom);
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoomIcono, tamZoomIcono, Image.SCALE_SMOOTH)));
                }
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                boton.revalidate();
                boton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setFont(fuenteNormal);
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }
                boton.revalidate();
                boton.repaint();
            }
        });
    }

    private void initListeners() {
        if (btnActualizar != null) btnActualizar.addActionListener(e -> procesarCambioEstado());
        if (btnVolver != null) btnVolver.addActionListener(e -> dispose());
    }

    private void procesarCambioEstado() {
        Apartamento aptoSeleccionado = (Apartamento) comboApartamentos.getSelectedItem();
        EstadoOperativo nuevoEstado = (EstadoOperativo) comboEstados.getSelectedItem();
        String motivo = textMotivo.getText().trim();

        if (aptoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un apartamento válido.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (nuevoEstado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona el nuevo estado operativo.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (nuevoEstado == EstadoOperativo.OCUPADO && aptoSeleccionado.getEstadoOperativo() != EstadoOperativo.OCUPADO) {
            JOptionPane.showMessageDialog(this,
                    "⚠ El estado OCUPADO solo se establece automáticamente al realizar el Check-In de un grupo.",
                    "Operación Bloqueada ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa el motivo del cambio de estado.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        aptoSeleccionado.cambiarEstado(nuevoEstado, motivo);
        JOptionPane.showMessageDialog(this,
                "¡Estado del apartamento " + aptoSeleccionado.getCodigo() + " actualizado correctamente a: " + nuevoEstado + "! 🎉",
                "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}