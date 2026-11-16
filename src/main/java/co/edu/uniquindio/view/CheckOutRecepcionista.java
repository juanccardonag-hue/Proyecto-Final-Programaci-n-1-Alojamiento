package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckOutRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField textCodigoReserva;
    private JButton btnBuscar;
    private JTextArea textDetalle;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;
    private Reserva reservaSeleccionada;

    public CheckOutRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Registro de Check-Out", true);
        this.empresa = empresa;

        if (contentPane != null) {
            setContentPane(contentPane);
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        }

        if (btnBuscar != null) {
            getRootPane().setDefaultButton(btnBuscar);
        }

        aplicarEstilos();
        initListeners();

        setSize(580, 500);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(textCodigoReserva);

        if (textDetalle != null) {
            textDetalle.setEditable(false);
            textDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            textDetalle.setMargin(new Insets(10, 10, 10, 10));
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

    public void icons() {
        configurarBoton(btnBuscar, "/Buscar.png", "Buscar");
        configurarBoton(buttonCancel,"","Cancelar");
        configurarBoton(buttonOK, "","Confirmar Check-Out");
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

    private void initListeners() {
        if (btnBuscar != null) btnBuscar.addActionListener(e -> buscarReserva());
        if (buttonOK != null) buttonOK.addActionListener(e -> procesarCheckOut());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());
    }

    private void buscarReserva() {
        String cod = textCodigoReserva.getText().trim();
        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el folio/código de la reserva.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        reservaSeleccionada = Recepcionista.buscarReservaPorFolio(
                empresa != null ? empresa.getListaReservas() : null,
                cod
        );

        if (reservaSeleccionada != null) {
            long totalCargosGenerales = reservaSeleccionada.getValorTotal();
            long totalPagado = (long) reservaSeleccionada.getTotalAbonado();
            double saldoPendiente = reservaSeleccionada.getSaldoFolio();
            String estadoCuenta = (saldoPendiente <= 0)
                    ? "PAZ Y SALVO"
                    : "SALDO PENDIENTE (Requiere pago)";

            String detalle = " Folio: " + reservaSeleccionada.getCodigoFolio() + "\n" +
                    " Estado Actual: " + reservaSeleccionada.getEstado() + "\n" +
                    " Titular: " + (reservaSeleccionada.getTitular() != null ? reservaSeleccionada.getTitular().getNombre() : "N/A") + "\n" +
                    " Apartamento: " + (reservaSeleccionada.getApartamento() != null ? reservaSeleccionada.getApartamento().getNombre() : "N/A") + "\n" +
                    " Total Cargos + Hospedaje: $" + String.format("%,d", totalCargosGenerales) + "\n" +
                    " Total Abonado: $" + String.format("%,d", totalPagado) + "\n" +
                    " Saldo Pendiente: $" + String.format("%,d", Math.max(0, (long) saldoPendiente)) + "\n" +
                    " Estado Cuenta: " + estadoCuenta + "\n";

            textDetalle.setText(detalle);

            if (reservaSeleccionada.getEstado() != EstadoReserva.EN_CURSO) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Atención: La reserva se encuentra en estado '" + reservaSeleccionada.getEstado() + "'. Para realizar Check-Out debe estar EN_CURSO.",
                        "Estado No Válido ⚠", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            textDetalle.setText("");
            JOptionPane.showMessageDialog(this, "No se encontró ninguna reserva con el código: " + cod, "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarCheckOut() {
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Consulta y selecciona una reserva primero.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String resultado = reservaSeleccionada.procesarCheckOut();

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this,
                    "¡Check-Out realizado exitosamente!\nLa reserva ha sido FINALIZADA y el apartamento pasa a PENDIENTE DE PREPARACIÓN.",
                    "Check-Out Completo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ " + resultado,
                    "Check-Out No Permitido ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}