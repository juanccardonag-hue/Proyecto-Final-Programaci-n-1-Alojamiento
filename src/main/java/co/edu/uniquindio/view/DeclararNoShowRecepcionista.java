package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeclararNoShowRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField textCodigoReserva;
    private JTextArea textDetalleReserva;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;

    public DeclararNoShowRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Declarar No-Show", true);
        this.empresa = empresa;

        if (contentPane != null) {
            setContentPane(contentPane);
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }
        aplicarEstilos();
        initListeners();
        setSize(520, 360);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(textCodigoReserva);

        icons();

        if (textDetalleReserva != null) {
            textDetalleReserva.setEditable(false);
            textDetalleReserva.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            textDetalleReserva.setMargin(new Insets(8, 8, 8, 8));
        }
    }

    public void icons() {
        configurarBoton(buttonOK, "", "Declarar No-Show");
        configurarBoton(buttonCancel, "", "Volver al Menú");
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
        if (buttonOK != null) buttonOK.addActionListener(e -> procesarNoShow());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());
    }

    private void procesarNoShow() {
        String cod = textCodigoReserva.getText().trim();

        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el código/folio de la reserva.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Reserva reservaSeleccionada = null;
        if (empresa.getListaReservas() != null) {
            for (Reserva r : empresa.getListaReservas()) {
                if (r.getCodigoFolio() != null && r.getCodigoFolio().equalsIgnoreCase(cod)) {
                    reservaSeleccionada = r;
                    break;
                }
            }
        }
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "⚠ No se encontró la reserva con el código: " + cod, "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reservaSeleccionada.getEstado() != EstadoReserva.CONFIRMADA) {
            JOptionPane.showMessageDialog(this,
                    "⚠ La reserva se encuentra en estado '" + reservaSeleccionada.getEstado() + "'. Para declarar NO-SHOW debe estar en estado CONFIRMADA.",
                    "Estado Inválido ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean exito = reservaSeleccionada.cambiarEstado(EstadoReserva.NO_SHOW);

        if (exito) {
            JOptionPane.showMessageDialog(this,
                    "¡La reserva " + cod + " ha sido declarada como NO-SHOW exitosamente! ",
                    "No-Show Confirmado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado de la reserva.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}