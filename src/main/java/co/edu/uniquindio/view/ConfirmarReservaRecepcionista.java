package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConfirmarReservaRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField inputCodigoReserva;
    private JButton btnBuscarReserva;
    private JTextArea txtDetalleReserva;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;
    private Reserva reservaEncontrada;

    public ConfirmarReservaRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Confirmación de Reservas", true);
        this.empresa = empresa;

        if (contentPane != null) {
            setContentPane(contentPane);
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        }

        if (btnBuscarReserva != null) {
            getRootPane().setDefaultButton(btnBuscarReserva);
        }

        icons();
        aplicarBordeRedondo(inputCodigoReserva);

        aplicarEstilos();
        initListeners();
        setSize(580, 500);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        if (txtDetalleReserva != null) {
            txtDetalleReserva.setEditable(false);
            txtDetalleReserva.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            txtDetalleReserva.setMargin(new Insets(10, 10, 10, 10));
        }
    }

    public void icons() {
        configurarBoton(btnBuscarReserva, "/Buscar.png", "Buscar");
        configurarBoton(buttonCancel, "", "Cancelar");
        configurarBoton(buttonOK, "", "Confirmar Reserva");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom   = new Font("Vensim Sans Mono", Font.BOLD, 17);

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

    private void initListeners() {
        if (btnBuscarReserva != null) {
            btnBuscarReserva.addActionListener(e -> buscarReserva());
        }
        if (buttonOK != null) {
            buttonOK.addActionListener(e -> procesarConfirmacion());
        }
        if (buttonCancel != null) {
            buttonCancel.addActionListener(e -> dispose());
        }
    }

    private void buscarReserva() {
        String cod = inputCodigoReserva.getText().trim();
        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el código o folio de la reserva.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Reserva> listaReservas = null;
        if (empresa != null) {
            listaReservas = empresa.getListaReservas();
        }

        reservaEncontrada = Recepcionista.buscarReservaPorFolio(listaReservas, cod);

        if (reservaEncontrada != null) {
            long totalPagado = (long) reservaEncontrada.getTotalAbonado();
            long faltaPara30 = reservaEncontrada.calcularRestanteAnticipo30();

            String nombreTitular = "N/A";
            if (reservaEncontrada.getTitular() != null) {
                nombreTitular = reservaEncontrada.getTitular().getNombre();
            }

            String detalle = " Folio: " + reservaEncontrada.getCodigoFolio() + "\n" +
                    " Canal: " + reservaEncontrada.getCanal() + "\n" +
                    " Estado Actual: " + reservaEncontrada.getEstado() + "\n" +
                    " Titular: " + nombreTitular + "\n" +
                    " Fechas: " + reservaEncontrada.getFechaEntrada() + "  ->  " + reservaEncontrada.getFechaSalida() + "\n" +
                    " Valor Total: $" + String.format("%,d", reservaEncontrada.getValorTotal()) + "\n" +
                    " Total Abonado: $" + String.format("%,d", totalPagado) + "\n";

            if (reservaEncontrada.getCanal() == Canal.PORTAL) {
                if (faltaPara30 == 0) {
                    detalle += " Estado Abono 30%: CUMPLIDO\n";
                } else {
                    detalle += " Estado Abono 30%: PENDIENTE (Restan $" + String.format("%,d", faltaPara30) + ")\n";
                }
            }

            txtDetalleReserva.setText(detalle);
        } else {
            txtDetalleReserva.setText("");
            JOptionPane.showMessageDialog(this, "No se encontró ninguna reserva con el código: " + cod, "Reserva No Encontrada ⚠", JOptionPane.ERROR_MESSAGE);
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
    private void procesarConfirmacion() {
        if (reservaEncontrada == null) {
            JOptionPane.showMessageDialog(this, "Consulta y selecciona una reserva válida primero.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String resultado = reservaEncontrada.confirmar();

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this,
                    "¡Reserva " + reservaEncontrada.getCodigoFolio() + " confirmada exitosamente!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ " + resultado,
                    "Confirmación No Permitida ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}