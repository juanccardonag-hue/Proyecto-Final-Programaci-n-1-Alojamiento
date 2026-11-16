package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;
import co.edu.uniquindio.Reserva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

public class CancelarReservaHuesped extends JDialog {
    private JPanel contentPane;
    private JComboBox<Reserva> comboReservas;
    private JLabel lblTotalAbonado;
    private JLabel lblDevolucion;
    private JButton buttonOK;
    private JButton buttonCancel;
    private Empresa empresa;
    private Huesped huespedActual;

    public CancelarReservaHuesped(JDialog parent, Empresa empresa, Huesped huespedActual) {
        super(parent, "Cancelar Reserva - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.huespedActual = huespedActual;

        if (contentPane != null) {
            setContentPane(contentPane);
        }

        if (buttonOK != null) {
            getRootPane().setDefaultButton(buttonOK);
        }

        if (lblTotalAbonado != null) lblTotalAbonado.setText("$ 0.00");
        if (lblDevolucion != null) lblDevolucion.setText("$ 0.00");

        configurarComboReservas();
        cargarReservasCancelables();

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void configurarComboReservas() {
        if (comboReservas != null) {
            comboReservas.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Reserva) {
                        Reserva r = (Reserva) value;
                        String codApto = "N/A";
                        if (r.getApartamento() != null) {
                            codApto = r.getApartamento().getCodigo();
                        }
                        setText(r.getCodigoReserva() + " - Apto " + codApto + " (" + r.getFechaEntrada() + " a " + r.getFechaSalida() + ")");
                    }
                    return this;
                }
            });
        }
    }

    private void cargarReservasCancelables() {
        if (comboReservas != null && huespedActual != null && huespedActual.getMisReservas() != null) {
            comboReservas.removeAllItems();
            for (Reserva r : huespedActual.getMisReservas()) {
                if (r.estaActiva()) {
                    comboReservas.addItem(r);
                }
            }

            if (comboReservas.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No tienes reservas activas disponibles para cancelar.",
                        "Sin Reservas Cancelables ℹ",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                actualizarCalculoDevolucion();
            }
        }
    }

    private void actualizarCalculoDevolucion() {
        Reserva seleccionada = (Reserva) comboReservas.getSelectedItem();
        if (seleccionada != null) {
            double totalAbonado = seleccionada.getTotalAbonado();
            double devolucion = seleccionada.calcularDevolucionCancelacion(LocalDate.now());

            if (lblTotalAbonado != null) {
                lblTotalAbonado.setText(String.format("$ %,.2f", totalAbonado));
            }
            if (lblDevolucion != null) {
                lblDevolucion.setText(String.format("$ %,.2f", devolucion));
            }
        } else {
            if (lblTotalAbonado != null) lblTotalAbonado.setText("$ 0.00");
            if (lblDevolucion != null) lblDevolucion.setText("$ 0.00");
        }
    }

    private void initListeners() {
        if (comboReservas != null) {
            comboReservas.addActionListener(e -> actualizarCalculoDevolucion());
        }

        if (buttonOK != null) buttonOK.addActionListener(e -> procesarCancelacion());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void procesarCancelacion() {
        Reserva reservaSeleccionada = (Reserva) comboReservas.getSelectedItem();

        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor selecciona una reserva para cancelar.",
                    "Selección Requerida ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double totalAbonado = reservaSeleccionada.getTotalAbonado();
        double devolucion = reservaSeleccionada.calcularDevolucionCancelacion(LocalDate.now());

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas cancelar la reserva " + reservaSeleccionada.getCodigoReserva() + "?\n\n" +
                        "• Total Abonado: $" + String.format("%,.2f", totalAbonado) + "\n" +
                        "• Valor a Devolver según Política: $" + String.format("%,.2f", devolucion) + "\n\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmación de Cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Delega la cancelación al modelo (Huesped)
            boolean cancelada = huespedActual.cancelarReserva(reservaSeleccionada.getCodigoReserva());

            if (cancelada) {
                JOptionPane.showMessageDialog(this,
                        "¡Reserva Cancelada Exitosamente! \n\n" +
                                "• Código: " + reservaSeleccionada.getCodigoReserva() + "\n" +
                                "• Devolución Procesada: $" + String.format("%,.2f", devolucion),
                        "Cancelación Completada",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo cancelar la reserva en su estado actual.",
                        "Operación No Permitida ⚠",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}