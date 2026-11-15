package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConfirmarReservaRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField inputCodigoReserva;
    private JButton btnBuscarReserva;
    private JPanel lblTitulo;
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

        aplicarEstilos();
        initListeners();
        setSize(580, 500);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(inputCodigoReserva);
        configurarBoton(btnBuscarReserva, "Consultar Estado");
        configurarBoton(buttonOK, "Confirmar Reserva");
        configurarBoton(buttonCancel, "Volver al Menú");

        if (txtDetalleReserva != null) {
            txtDetalleReserva.setEditable(false);
            txtDetalleReserva.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            txtDetalleReserva.setMargin(new Insets(10, 10, 10, 10));
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

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;
        if (titulo != null && !titulo.isEmpty()) boton.setText(titulo);

        Font fuenteNormal = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteHover = new Font("Segoe UI", Font.BOLD, 15);
        boton.setFont(fuenteNormal);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setFont(fuenteHover);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setFont(fuenteNormal);
            }
        });
    }

    private void initListeners() {
        if (btnBuscarReserva != null) btnBuscarReserva.addActionListener(e -> buscarReserva());
        if (buttonOK != null) buttonOK.addActionListener(e -> procesarConfirmacion());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());
    }

    private void buscarReserva() {
        String cod = inputCodigoReserva.getText().trim();
        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el código o folio de la reserva.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        reservaEncontrada = Recepcionista.buscarReservaPorFolio(
                empresa != null ? empresa.getListaReservas() : null,
                cod
        );

        if (reservaEncontrada != null) {
            long totalPagado = (long) reservaEncontrada.getTotalAbonado();
            long faltaPara30 = reservaEncontrada.calcularRestanteAnticipo30();

            String detalle = " Folio: " + reservaEncontrada.getCodigoFolio() + "\n" +
                    " Canal: " + reservaEncontrada.getCanal() + "\n" +
                    " Estado Actual: " + reservaEncontrada.getEstado() + "\n" +
                    " Titular: " + (reservaEncontrada.getTitular() != null ? reservaEncontrada.getTitular().getNombre() : "N/A") + "\n" +
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