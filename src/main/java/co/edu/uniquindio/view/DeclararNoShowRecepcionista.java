package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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

        if (buttonOK != null) {
            getRootPane().setDefaultButton(buttonOK);
        }

        aplicarEstilos();
        initListeners();
        setSize(520, 360);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(textCodigoReserva);

        configurarBoton(buttonOK, "", "Declarar No-Show");
        configurarBoton(buttonCancel, "", "Cancelar");

        if (textDetalleReserva != null) {
            textDetalleReserva.setEditable(false);
            textDetalleReserva.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            textDetalleReserva.setMargin(new Insets(8, 8, 8, 8));
        }
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
        Font fuenteZoom = new Font("Segoe UI", Font.BOLD, 16);

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

        List<Reserva> listaReservas = null;
        if (empresa != null) {
            listaReservas = empresa.getListaReservas();
        }

        Reserva reservaSeleccionada = Recepcionista.buscarReservaPorFolio(listaReservas, cod);

        if (reservaSeleccionada == null) {
            if (textDetalleReserva != null) {
                textDetalleReserva.setText("");
            }
            JOptionPane.showMessageDialog(this, "⚠ No se encontró la reserva con el código: " + cod, "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (textDetalleReserva != null) {
            String nombreTitular = "N/A";
            if (reservaSeleccionada.getTitular() != null) {
                nombreTitular = reservaSeleccionada.getTitular().getNombre();
            }

            String nombreApto = "N/A";
            if (reservaSeleccionada.getApartamento() != null) {
                nombreApto = reservaSeleccionada.getApartamento().getNombre();
            }

            String detalle = " Folio: " + reservaSeleccionada.getCodigoFolio() + "\n" +
                    " Estado Actual: " + reservaSeleccionada.getEstado() + "\n" +
                    " Titular: " + nombreTitular + "\n" +
                    " Apartamento: " + nombreApto + "\n" +
                    " Fechas: " + reservaSeleccionada.getFechaEntrada() + "  ->  " + reservaSeleccionada.getFechaSalida() + "\n";
            textDetalleReserva.setText(detalle);
        }

        String resultado = reservaSeleccionada.declararNoShow();

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this,
                    "¡La reserva " + reservaSeleccionada.getCodigoFolio() + " ha sido declarada como NO-SHOW exitosamente!",
                    "No-Show Confirmado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ " + resultado,
                    "Estado Inválido ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}