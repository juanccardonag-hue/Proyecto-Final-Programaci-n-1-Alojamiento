package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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

        if (btnBuscar != null) {
            getRootPane().setDefaultButton(btnBuscar);
        }

        cargarComboMediosPago();
        aplicarEstilos();
        initListeners();
        setSize(700, 650);
        setResizable(false);
        setLocationRelativeTo(parent);

        aplicarBordeRedondo(txtCodigoReserva);
        aplicarBordeRedondo(textMontoPago);
        aplicarBordeRedondo(textConceptoCargo);
        aplicarBordeRedondo(textMontoCargo);

        icons();

        configurarBoton(btnRegistrarPago,"", "Registrar pago");
        configurarBoton(btnAgregarCargo, "","Agregar Cargo");
        configurarBoton(buttonCancel, "","Cancelar");
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
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    public void icons() {
        configurarBoton(btnBuscar, "/Buscar.png", "Buscar");
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

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setFont(fuenteZoom);
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoomIcono, tamZoomIcono, Image.SCALE_SMOOTH)));
                }
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                boton.revalidate();
                boton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
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

        List<Reserva> listaReservas = null;
        if (empresa != null) {
            listaReservas = empresa.getListaReservas();
        }

        reservaSeleccionada = Recepcionista.buscarReservaPorFolio(listaReservas, cod);

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
        String titular = "N/A";
        if (reservaSeleccionada.getTitular() != null) {
            titular = reservaSeleccionada.getTitular().getNombre();
        }

        String reporte = "°°°°°°°°° ESTADO DE CUENTA °°°°°°°°°°°\n" +
                " Folio Reserva : " + reservaSeleccionada.getCodigoFolio() + "\n" +
                " Titular       : " + titular + "\n" +
                " Estado Reserva: " + reservaSeleccionada.getEstado() + "\n" +
                " CARGOS REGISTRADOS:\n";

        if (folio != null && !folio.getListaCargo().isEmpty()) {
            for (Cargo c : folio.getListaCargo()) {
                reporte += "  • " + c.concepto() + " -> $" + String.format("%,d", c.monto()) + "\n";
            }
        } else {
            reporte += "  (No hay cargos registrados)\n";
        }

        reporte += " PAGOS / ABONOS REGISTRADOS:\n";
        if (folio != null && !folio.getListaPago().isEmpty()) {
            for (Pago p : folio.getListaPago()) {
                reporte += "  • " + p.medioPago() + " -> $" + String.format("%,d", p.monto()) + "\n";
            }
        } else {
            reporte += "  (No hay abonos registrados)\n";
        }

        reporte += " TOTAL CARGOS: $" + String.format("%,d", reservaSeleccionada.getValorTotal()) + "\n" +
                " TOTAL ABONOS: $" + String.format("%,d", (long) reservaSeleccionada.getTotalAbonado()) + "\n" +
                " SALDO PEND. : $" + String.format("%,d", Math.max(0, (long) reservaSeleccionada.getSaldoFolio())) + "\n";

        textEstadoCuenta.setText(reporte);
    }

    private void efectuarPago() {
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Busca primero una reserva válida.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
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
            String resultado = Recepcionista.registrarPagoFolio(reservaSeleccionada, monto, medio);

            if ("OK".equals(resultado)) {
                JOptionPane.showMessageDialog(this,
                        "¡Pago de $" + String.format("%,d", monto) + " registrado exitosamente!\nSaldo actual: $" + String.format("%,d", Math.max(0, (long) reservaSeleccionada.getSaldoFolio())),
                        "Pago Exitoso", JOptionPane.INFORMATION_MESSAGE);
                textMontoPago.setText("");
                actualizarVistaEstadoCuenta();
            } else {
                JOptionPane.showMessageDialog(this, "⚠ " + resultado, "Operación No Permitida ⚠", JOptionPane.ERROR_MESSAGE);
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

        String concepto = textConceptoCargo.getText().trim();
        String montoStr = textMontoCargo.getText().trim();

        if (concepto.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el concepto y monto del cargo.", "Campos Vacíos ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long monto = Long.parseLong(montoStr);
            String resultado = Recepcionista.registrarCargoAdicionalFolio(reservaSeleccionada, concepto, monto);

            if ("OK".equals(resultado)) {
                JOptionPane.showMessageDialog(this,
                        "¡Cargo '" + concepto + "' por $" + String.format("%,d", monto) + " agregado al folio!\nNuevo Valor Total: $" + String.format("%,d", reservaSeleccionada.getValorTotal()),
                        "Cargo Registrado", JOptionPane.INFORMATION_MESSAGE);
                textConceptoCargo.setText("");
                textMontoCargo.setText("");
                actualizarVistaEstadoCuenta();
            } else {
                JOptionPane.showMessageDialog(this, "⚠ " + resultado, "Operación No Permitida ⚠", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un valor entero válido para el monto.", "Error de Formato ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}