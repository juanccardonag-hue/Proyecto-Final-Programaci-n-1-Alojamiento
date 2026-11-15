package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class CheckInRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField txtCodigoReserva;
    private JButton btnBuscar;
    private JTextArea textDetalle;
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

        reservaSeleccionada = Recepcionista.buscarReservaPorFolio(
                empresa != null ? empresa.getListaReservas() : null,
                cod
        );

        if (reservaSeleccionada != null) {
            LocalDate hoy = LocalDate.now();
            LocalDate entrada = reservaSeleccionada.getFechaEntrada();

                String detalle = " Folio: " + reservaSeleccionada.getCodigoFolio() + "\n" +
                        " Canal: " + reservaSeleccionada.getCanal() + "\n" +
                        " Estado Actual: " + reservaSeleccionada.getEstado() + "\n" +
                        " Titular: " + (reservaSeleccionada.getTitular() != null ? reservaSeleccionada.getTitular().getNombre() : "N/A") + "\n" +
                        " Cédula: " + (reservaSeleccionada.getTitular() != null ? reservaSeleccionada.getTitular().getCedula() : "N/A") + "\n" +
                        " Apartamento: " + (reservaSeleccionada.getApartamento() != null ? reservaSeleccionada.getApartamento().getNombre() : "N/A") + "\n" +
                        " Fechas: " + entrada + "  ->  " + reservaSeleccionada.getFechaSalida() + "\n" +
                        " Fecha Actual (Hoy): " + hoy + "\n";

                textDetalle.setText(detalle);

            if (reservaSeleccionada.getEstado() != EstadoReserva.CONFIRMADA) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Atención: La reserva está en estado '" + reservaSeleccionada.getEstado() + "'. Debe estar CONFIRMADA para realizar Check-In.",
                        "Reserva No Confirmada ⚠", JOptionPane.WARNING_MESSAGE);
            } else if (entrada != null && hoy.isBefore(entrada)) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Aún no es la fecha de ingreso.\nFecha programada de Check-In: " + entrada + " (Hoy es " + hoy + ").",
                        "Check-In Anticipado No Permitido ⚠", JOptionPane.WARNING_MESSAGE);
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

        String resultado = reservaSeleccionada.procesarCheckIn();

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this,
                    "¡Check-In completado exitosamente!\nAcceso habilitado para el apartamento: " +
                            (reservaSeleccionada.getApartamento() != null ? reservaSeleccionada.getApartamento().getNombre() : ""),
                    "Check-In Finalizado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ " + resultado,
                    "Check-In No Permitido ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}