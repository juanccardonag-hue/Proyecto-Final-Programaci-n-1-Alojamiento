package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckInRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField txtCodigoReserva;
    private JButton btnBuscar;
    private JTextArea textDetalle; // Sincronizado con el .form
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;
    private Reserva reservaSeleccionada;

    public CheckInRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Registro de Check-In", true);
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
        aplicarBordeRedondo(txtCodigoReserva);

        configurarBoton(btnBuscar, "Buscar");
        configurarBoton(buttonOK, "Confirmar Check-In");
        configurarBoton(buttonCancel, "Volver al Menú");

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

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;
        if (titulo != null && !titulo.isEmpty()) boton.setText(titulo);

        Font fuenteNormal = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteHover = new Font("Segoe UI", Font.BOLD, 14);
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
        if (btnBuscar != null) btnBuscar.addActionListener(e -> buscarReserva());
        if (buttonOK != null) buttonOK.addActionListener(e -> procesarCheckIn());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());
    }

    private void buscarReserva() {
        String cod = txtCodigoReserva.getText().trim();
        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el folio/código de la reserva.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        reservaSeleccionada = null;
        if (empresa.getListaReservas() != null) {
            for (Reserva r : empresa.getListaReservas()) {
                if (r.getCodigoFolio() != null && r.getCodigoFolio().equalsIgnoreCase(cod)) {
                    reservaSeleccionada = r;
                    break;
                }
            }
        }

        if (reservaSeleccionada != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("--------------------------------------------------\n");
            sb.append(" Folio: ").append(reservaSeleccionada.getCodigoFolio()).append("\n");
            sb.append(" Canal: ").append(reservaSeleccionada.getCanal()).append("\n");
            sb.append(" Estado Actual: ").append(reservaSeleccionada.getEstado()).append("\n");
            sb.append(" Titular: ").append(reservaSeleccionada.getTitular() != null ? reservaSeleccionada.getTitular().getNombre() : "N/A").append("\n");
            sb.append(" Cédula: ").append(reservaSeleccionada.getTitular() != null ? reservaSeleccionada.getTitular().getCedula() : "N/A").append("\n");
            sb.append(" Apartamento: ").append(reservaSeleccionada.getApartamento() != null ? reservaSeleccionada.getApartamento().getNombre() : "N/A").append("\n");
            sb.append(" Fechas: ").append(reservaSeleccionada.getFechaEntrada()).append("  ->  ").append(reservaSeleccionada.getFechaSalida()).append("\n");
            sb.append("--------------------------------------------------");

            textDetalle.setText(sb.toString());

            if (reservaSeleccionada.getEstado() != EstadoReserva.CONFIRMADA) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Atención: La reserva está en estado '" + reservaSeleccionada.getEstado() + "'. Debe estar CONFIRMADA para realizar Check-In.",
                        "Reserva No Confirmada ⚠", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            textDetalle.setText("");
            JOptionPane.showMessageDialog(this, "No se encontró ninguna reserva con el código: " + cod, "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarCheckIn() {
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Consulta y selecciona una reserva primero.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (reservaSeleccionada.getEstado() != EstadoReserva.CONFIRMADA) {
            JOptionPane.showMessageDialog(this, "Solo se puede realizar Check-In a reservas en estado CONFIRMADA.", "Operación Denegada ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (reservaSeleccionada.getApartamento() != null &&
                reservaSeleccionada.getApartamento().getEstadoOperativo() != EstadoOperativo.PREPARADO) {
            JOptionPane.showMessageDialog(this,
                    "⚠ El apartamento aún no está en estado PREPARADO (Estado actual: " +
                            reservaSeleccionada.getApartamento().getEstadoOperativo() + ").",
                    "Check-In Bloqueado ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean cambioExitoso = reservaSeleccionada.cambiarEstado(EstadoReserva.EN_CURSO);

        if (cambioExitoso) {
            if (reservaSeleccionada.getApartamento() != null) {
                reservaSeleccionada.getApartamento().cambiarEstado(EstadoOperativo.OCUPADO, null);
            }

            JOptionPane.showMessageDialog(this,
                    "¡Check-In completado exitosamente! 🎉\nAcceso habilitado para el apartamento: " +
                            (reservaSeleccionada.getApartamento() != null ? reservaSeleccionada.getApartamento().getNombre() : ""),
                    "Check-In Finalizado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado de la reserva.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}