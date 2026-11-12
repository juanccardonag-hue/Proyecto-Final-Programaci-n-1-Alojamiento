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

        reservaEncontrada = null;
        if (empresa.getListaReservas() != null) {
            for (Reserva r : empresa.getListaReservas()) {
                if (r.getCodigoFolio() != null && r.getCodigoFolio().equalsIgnoreCase(cod)) {
                    reservaEncontrada = r;
                    break;
                }
            }
        }

        if (reservaEncontrada != null) {
            double totalPagado = (reservaEncontrada.getFolio() != null) ? reservaEncontrada.getFolio().calcularTotalPagos() : 0.0;
            double anticipoRequerido = reservaEncontrada.getValorTotal() * 0.30;

            StringBuilder sb = new StringBuilder();
            sb.append(" Folio: ").append(reservaEncontrada.getCodigoFolio()).append("\n");
            sb.append(" Canal: ").append(reservaEncontrada.getCanal()).append("\n");
            sb.append(" Estado Actual: ").append(reservaEncontrada.getEstado()).append("\n");
            sb.append(" Titular: ").append(reservaEncontrada.getTitular() != null ? reservaEncontrada.getTitular().getNombre() : "N/A").append("\n");
            sb.append(" Fechas: ").append(reservaEncontrada.getFechaEntrada()).append("  ->  ").append(reservaEncontrada.getFechaSalida()).append("\n");
            sb.append(" Valor Total: $").append(reservaEncontrada.getValorTotal()).append("\n");
            sb.append(" Total Abonado: $").append(totalPagado).append("\n");

            if (reservaEncontrada.getCanal() == Canal.PORTAL) {
                sb.append(" Anticipo 30% Requerido: $").append(anticipoRequerido).append("\n");
                if (totalPagado >= anticipoRequerido) {
                    sb.append(" Estado Abono: CUMPLIDO \n");
                } else {
                    sb.append(" Estado Abono: PENDIENTE  (Falta abonar)\n");
                }
            }

            txtDetalleReserva.setText(sb.toString());
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

        if (reservaEncontrada.getEstado() == EstadoReserva.CONFIRMADA) {
            JOptionPane.showMessageDialog(this, "Esta reserva ya se encuentra CONFIRMADA previamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (reservaEncontrada.getCanal() == Canal.PORTAL) {
            double anticipoRequerido = reservaEncontrada.getValorTotal() * 0.30;
            double totalPagado = (reservaEncontrada.getFolio() != null) ? reservaEncontrada.getFolio().calcularTotalPagos() : 0.0;

            if (totalPagado < anticipoRequerido) {
                JOptionPane.showMessageDialog(this,
                        "⚠ No se puede confirmar la reserva de Canal PORTAL.\nRequiere pago anticipado del 30% ($" + anticipoRequerido + ").\nTotal abonado actual: $" + totalPagado,
                        "Anticipo Insuficiente ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (reservaEncontrada.cambiarEstado(EstadoReserva.CONFIRMADA)) {
            JOptionPane.showMessageDialog(this, "¡Reserva " + reservaEncontrada.getCodigoFolio() + " confirmada exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo cambiar el estado de la reserva. Estado actual: " + reservaEncontrada.getEstado(), "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}