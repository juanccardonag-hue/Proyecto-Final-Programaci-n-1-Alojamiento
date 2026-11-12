package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.EstadoReserva;
import co.edu.uniquindio.Huesped;
import co.edu.uniquindio.MedioPago;
import co.edu.uniquindio.Reserva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
            setContentPane(contentPane);
        }

        if (buttonOK != null) {
            getRootPane().setDefaultButton(buttonOK);
        }

        aplicarEstilos();
        cargarMediosPago();
        cargarReservasHuesped();

        pack();
        setMinimumSize(new Dimension(500, 420));
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(inputMonto);
        configurarBoton(buttonOK, "Registrar Pago");
        configurarBoton(buttonCancel, "Volver al Menú");
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
        boton.setText(titulo);
        boton.setFocusPainted(false);

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 13);
        Font fuenteHover = new Font("Vensim Sans Mono", Font.BOLD, 14);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));

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
                if (r.estaActiva() && r.getSaldoFolio() > 0) {
                    comboReservas.addItem(r);
                }
            }

            comboReservas.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Reserva) {
                        Reserva r = (Reserva) value;
                        String apto = (r.getApartamento() != null) ? r.getApartamento().getNombre() : "Sin Apto";
                        setText(r.getCodigoFolio() + " - " + apto + " (" + r.getEstado() + ")");
                    }
                    return this;
                }
            });

            if (comboReservas.getItemCount() == 0) {
                actualizarValoresReserva();
                JOptionPane.showMessageDialog(this,
                        "No tienes reservas con saldo pendiente de pago.",
                        "Al Día",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                actualizarValoresReserva();
            }
        }
    }

    private void actualizarValoresReserva() {
        Reserva seleccionada = (Reserva) comboReservas.getSelectedItem();
        if (seleccionada != null) {
            long totalEstancia = (long) seleccionada.getValorTotal();
            long abonado = (long) seleccionada.getTotalAbonado();
            long saldoPendiente = Math.max(0, (long) seleccionada.getSaldoFolio());
            long abono30Minimo = Math.round(totalEstancia * 0.30);
            long restantePara30 = Math.max(0, abono30Minimo - abonado);

            if (lblSaldoPendiente != null) {
                lblSaldoPendiente.setText(String.format("$ %,d", saldoPendiente));
            }
            if (lblAbonoMinimo != null) {
                if (restantePara30 == 0) {
                    lblAbonoMinimo.setText("$ 0 (30% cubierto)");
                } else {
                    lblAbonoMinimo.setText(String.format("$ %,d", restantePara30));
                }
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
            long saldoActual = (long) reservaSeleccionada.getSaldoFolio();

            if (monto <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El monto a abonar debe ser mayor a cero.",
                        "Monto Inválido ⚠",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (monto > saldoActual) {
                JOptionPane.showMessageDialog(this,
                        "El monto ingresado ($" + String.format("%,d", monto) +
                                ") supera el saldo pendiente de la reserva ($" + String.format("%,d", saldoActual) + ").",
                        "Monto Excedido ⚠",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean abonoExitoso = reservaSeleccionada.registrarAbono(monto, medio);

            if (abonoExitoso) {
                long totalAbonado = (long) reservaSeleccionada.getTotalAbonado();
                long abonoRequerido30 = Math.round(reservaSeleccionada.getValorTotal() * 0.30);

                String mensajeEstado = "";
                if (reservaSeleccionada.getEstado() == EstadoReserva.PENDIENTE && totalAbonado >= abonoRequerido30) {
                    reservaSeleccionada.cambiarEstado(EstadoReserva.CONFIRMADA);
                    mensajeEstado = "\n\n Has cumplido con el 30% mínimo de anticipo.\n" +
                            "Tu reserva ha cambiado a estado: CONFIRMADA.";
                } else if (reservaSeleccionada.getEstado() == EstadoReserva.PENDIENTE) {
                    long faltaPara30 = Math.max(0, abonoRequerido30 - totalAbonado);
                    mensajeEstado = "\n\n⚠ Recuerda que para confirmar tu reserva debes completar el 30% ($" +
                            String.format("%,d", abonoRequerido30) + "). Te restan $" +
                            String.format("%,d", faltaPara30) + " dentro de las 24 horas.";
                }

                long nuevoSaldo = Math.max(0, (long) reservaSeleccionada.getSaldoFolio());

                JOptionPane.showMessageDialog(this,
                        "¡Abono registrado exitosamente! \n\n" +
                                "• Reserva: " + reservaSeleccionada.getCodigoFolio() + "\n" +
                                "• Monto Abonado: $" + String.format("%,d", monto) + "\n" +
                                "• Medio de Pago: " + medio + "\n" +
                                "• Nuevo Saldo Pendiente: $" + String.format("%,d", nuevoSaldo) +
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
                    "Ingresa un número entero válido para el monto del abono (sin puntos ni comas).",
                    "Formato Inválido ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}