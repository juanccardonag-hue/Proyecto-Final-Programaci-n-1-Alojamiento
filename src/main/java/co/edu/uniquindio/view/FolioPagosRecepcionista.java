package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FolioPagosRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField txtCodigoReserva;
    private JButton btnBuscar;
    private JTextArea textEstadoCuenta;
    private JTextField textMontoPago;
    private JComboBox<MedioPago> comboMedioPago;
    private JButton btnRegistrarPago;
    private JTextField textConceptoCargo;
    private JTextField textMontoCargo;
    private JButton btnAgregarCargo;
    private JButton buttonCancel;

    private Empresa empresa;
    private Reserva reservaSeleccionada;

    public FolioPagosRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Gestión de Folio y Pagos", true);
        this.empresa = empresa;

        if (contentPane != null) {
            setContentPane(contentPane);
            contentPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        }

        cargarComboMediosPago();
        aplicarEstilos();
        initListeners();
        setSize(680, 650);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void cargarComboMediosPago() {
        if (comboMedioPago != null) {
            comboMedioPago.removeAllItems();
            for (MedioPago mp : MedioPago.values()) {
                comboMedioPago.addItem(mp);
            }
        }
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(txtCodigoReserva);
        aplicarBordeRedondo(textMontoPago);
        aplicarBordeRedondo(textConceptoCargo);
        aplicarBordeRedondo(textMontoCargo);

        configurarBotonSencillo(btnBuscar, "Buscar");
        configurarBotonSencillo(btnRegistrarPago, "Registrar Pago");
        configurarBotonSencillo(btnAgregarCargo, "Agregar Cargo");
        configurarBotonSencillo(buttonCancel, "Volver al Menú");

        if (textEstadoCuenta != null) {
            textEstadoCuenta.setEditable(false);
            textEstadoCuenta.setFont(new Font("Consolas", Font.PLAIN, 12));
            textEstadoCuenta.setMargin(new Insets(8, 8, 8, 8));
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
                        g2.drawRoundRect(x, y, width - 1, height - 1, 12, 12);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private void configurarBotonSencillo(JButton boton, String texto) {
        if (boton == null) return;
        if (texto != null && !texto.isEmpty()) boton.setText(texto);

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
        if (btnRegistrarPago != null) btnRegistrarPago.addActionListener(e -> efectuarPago());
        if (btnAgregarCargo != null) btnAgregarCargo.addActionListener(e -> efectuarCargo());
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
            actualizarVistaEstadoCuenta();
        } else {
            textEstadoCuenta.setText("");
            JOptionPane.showMessageDialog(this, "No se encontró ninguna reserva con el código: " + cod, "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarVistaEstadoCuenta() {
        if (reservaSeleccionada == null || textEstadoCuenta == null) return;

        Folio folio = reservaSeleccionada.getFolio();
        String titular = (reservaSeleccionada.getTitular() != null) ? reservaSeleccionada.getTitular().getNombre() : "N/A";

        textEstadoCuenta.setText("");
        textEstadoCuenta.append("=================== ESTADO DE CUENTA ===================\n");
        textEstadoCuenta.append(" Folio Reserva : " + reservaSeleccionada.getCodigoFolio() + "\n");
        textEstadoCuenta.append(" Titular       : " + titular + "\n");
        textEstadoCuenta.append(" Estado Reserva: " + reservaSeleccionada.getEstado() + "\n");

        textEstadoCuenta.append(" CARGOS REGISTRADOS:\n");
        if (folio != null && !folio.getListaCargo().isEmpty()) {
            for (Cargo c : folio.getListaCargo()) {
                textEstadoCuenta.append("  • " + c.concepto() + " -> $" + String.format("%,d", c.monto()) + "\n");
            }
        } else {
            textEstadoCuenta.append("  (No hay cargos registrados)\n");
        }

        textEstadoCuenta.append(" PAGOS / ABONOS REGISTRADOS:\n");
        if (folio != null && !folio.getListaPago().isEmpty()) {
            for (Pago p : folio.getListaPago()) {
                textEstadoCuenta.append("  • " + p.medioPago() + " -> $" + String.format("%,d", p.monto()) + "\n");
            }
        } else {
            textEstadoCuenta.append("  (No hay abonos registrados)\n");
        }

        textEstadoCuenta.append(" TOTAL CARGOS: $" + String.format("%,d", reservaSeleccionada.getValorTotal()) + "\n");
        textEstadoCuenta.append(" TOTAL ABONOS: $" + String.format("%,.0f", reservaSeleccionada.getTotalAbonado()) + "\n");
        textEstadoCuenta.append(" SALDO PEND. : $" + String.format("%,.0f", Math.max(0, reservaSeleccionada.getSaldoFolio())) + "\n");
    }

    private void efectuarPago() {
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Busca primero una reserva válida.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!reservaSeleccionada.estaActiva()) {
            JOptionPane.showMessageDialog(this,
                    "⚠ No se pueden registrar pagos a una reserva en estado " + reservaSeleccionada.getEstado() + ".",
                    "Operación Rechazada ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String montoStr = textMontoPago.getText().trim();
        MedioPago medio = (MedioPago) comboMedioPago.getSelectedItem();

        if (montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el monto a abonar.", "Campo Vacío ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long monto = Long.parseLong(montoStr);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto del pago debe ser mayor a 0.", "Monto Inválido ⚠", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exitoso = reservaSeleccionada.registrarAbono(monto, medio);

            if (exitoso) {
                JOptionPane.showMessageDialog(this, "¡Pago de $" + String.format("%,d", monto) + " registrado exitosamente! 🎉\nSaldo actual: $" + String.format("%,.0f", reservaSeleccionada.getSaldoFolio()), "Pago Exitoso", JOptionPane.INFORMATION_MESSAGE);
                textMontoPago.setText("");
                actualizarVistaEstadoCuenta();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo procesar el pago.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un valor entero válido para el monto.", "Error de Formato ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void efectuarCargo() {
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Busca primero una reserva válida.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!reservaSeleccionada.estaActiva()) {
            JOptionPane.showMessageDialog(this,
                    "⚠ No se pueden agregar cargos a una reserva en estado " + reservaSeleccionada.getEstado() + ".",
                    "Operación Rechazada ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String concepto = textConceptoCargo.getText().trim();
        String montoStr = textMontoCargo.getText().trim();

        if (concepto.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el concepto y monto del cargo.", "Campos Vacíos ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            long monto = Long.parseLong(montoStr);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto del cargo debe ser mayor a 0.", "Monto Inválido ⚠", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (reservaSeleccionada.getFolio() != null) {
                reservaSeleccionada.getFolio().agregarCargo(concepto, monto);
                JOptionPane.showMessageDialog(this, "¡Cargo '" + concepto + "' por $" + String.format("%,d", monto) + " agregado al folio! 🛒\nNuevo Valor Total: $" + String.format("%,d", reservaSeleccionada.getValorTotal()), "Cargo Registrado", JOptionPane.INFORMATION_MESSAGE);
                textConceptoCargo.setText("");
                textMontoCargo.setText("");
                actualizarVistaEstadoCuenta();
            } else {
                JOptionPane.showMessageDialog(this, "La reserva no tiene un Folio asociado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un valor entero válido para el monto.", "Error de Formato ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}