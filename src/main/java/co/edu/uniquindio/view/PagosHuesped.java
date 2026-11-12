package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.EstadoReserva;
import co.edu.uniquindio.Huesped;
import co.edu.uniquindio.MedioPago;
import co.edu.uniquindio.Reserva;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PagosHuesped extends JDialog {
    private JPanel contentPane;
    private JComboBox<Reserva> comboReservas;
    private JComboBox<MedioPago> comboMedioPago;
    private JTextField inputMonto;
    private JLabel lblSaldoPendiente;
    private JLabel lblAbonoMinimo;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;
    private Huesped huespedActual;

    public PagosHuesped(JDialog parent, Empresa empresa, Huesped huespedActual) {
        super(parent, "Cartera de Pagos - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.huespedActual = huespedActual;

        if (contentPane != null) {
            setContentPane(contentPane);
        }

        if (buttonOK != null) {
            getRootPane().setDefaultButton(buttonOK);
        }

        cargarMediosPago();
        cargarReservasHuesped();

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void cargarMediosPago() {
        if (comboMedioPago != null) {
            comboMedioPago.removeAllItems();
            for (MedioPago mp : MedioPago.values()) {
                comboMedioPago.addItem(mp);
            }
        }
    }

    private void cargarReservasHuesped() {
        if (comboReservas != null && huespedActual != null && huespedActual.getMisReservas() != null) {
            comboReservas.removeAllItems();
            for (Reserva r : huespedActual.getMisReservas()) {
                if (r.estaActiva()) {
                    comboReservas.addItem(r);
                }
            }

            if (comboReservas.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No tienes reservas pendientes de pago.",
                        "Sin Reservas Activas",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                actualizarValoresReserva();
            }
        }
    }

    private void actualizarValoresReserva() {
        Reserva seleccionada = (Reserva) comboReservas.getSelectedItem();
        if (seleccionada != null) {
            long total = seleccionada.getValorTotal();
            double abonado = seleccionada.getTotalAbonado();
            double saldo = total - abonado;
            double abono30Minimo = total * 0.30;

            if (lblSaldoPendiente != null) {
                // Se castea a (long) para mostrar como número entero con separadores de miles
                lblSaldoPendiente.setText(String.format("$ %,d", (long) Math.max(0, saldo)));
            }
            if (lblAbonoMinimo != null) {
                lblAbonoMinimo.setText(String.format("$ %,d", (long) abono30Minimo));
            }
        } else {
            if (lblSaldoPendiente != null) lblSaldoPendiente.setText("$ 0");
            if (lblAbonoMinimo != null) lblAbonoMinimo.setText("$ 0");
        }
    }

    private void initListeners() {
        if (comboReservas != null) {
            comboReservas.addActionListener(e -> actualizarValoresReserva());
        }

        if (buttonOK != null) buttonOK.addActionListener(e -> realizarPago());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void realizarPago() {
        Reserva reservaSeleccionada = (Reserva) comboReservas.getSelectedItem();
        MedioPago medio = (MedioPago) comboMedioPago.getSelectedItem();
        String montoStr = inputMonto.getText().trim();

        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "No tienes reservas activas seleccionadas para abonar.",
                    "Sin Selección ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresa el monto a abonar.",
                    "Campo Vacío ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long monto = Long.parseLong(montoStr);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El monto a abonar debe ser mayor a cero.",
                        "Monto Inválido ⚠",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean abonoExitoso = reservaSeleccionada.registrarAbono(monto, medio);

            if (abonoExitoso) {
                double totalAbonado = reservaSeleccionada.getTotalAbonado();
                double abonoRequerido30 = reservaSeleccionada.getValorTotal() * 0.30;

                String mensajeEstado = "";
                if (totalAbonado >= abonoRequerido30) {
                    reservaSeleccionada.setEstado(EstadoReserva.CONFIRMADA);
                    mensajeEstado = "\n\n🎉 ¡Felicidades! Has cumplido con el 30% mínimo de anticipo.\n" +
                            "Tu reserva ha cambiado a estado: CONFIRMADA.";
                } else {
                    double faltaPara30 = abonoRequerido30 - totalAbonado;
                    mensajeEstado = "\n\n⚠ Recuerda que para confirmar tu reserva debes completar el 30% ($" +
                            String.format("%,d", (long) abonoRequerido30) + "). Te restan $" +
                            String.format("%,d", (long) faltaPara30) + " dentro de las 24 horas.";
                }

                JOptionPane.showMessageDialog(this,
                        "¡Abono registrado exitosamente! 💳\n\n" +
                                "• Reserva: " + reservaSeleccionada.getCodigoFolio() + "\n" +
                                "• Monto Abonado: $" + String.format("%,d", monto) + "\n" +
                                "• Medio de Pago: " + medio + "\n" +
                                "• Nuevo Saldo Pendiente: $" + String.format("%,d", (long) reservaSeleccionada.getSaldoFolio()) +
                                mensajeEstado,
                        "Pago Procesado",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo procesar el abono. Revisa el estado de la reserva.",
                        "Error en Pago ⚠",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un número entero válido para el monto del abono.",
                    "Formato Inválido ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}