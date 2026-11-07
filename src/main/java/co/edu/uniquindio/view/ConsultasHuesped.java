package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;
import co.edu.uniquindio.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ConsultasHuesped extends JDialog {
    private JPanel contentPane;
    private JTable tablaReservas;
    private JButton buttonActualizar;
    private JButton buttonCancel;

    private Empresa empresa;
    private Huesped huespedActual;
    private DefaultTableModel tableModel;

    public ConsultasHuesped(JDialog parent, Empresa empresa, Huesped huespedActual) {
        super(parent, "Historial de Reservas - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.huespedActual = huespedActual;

        if (contentPane != null) {
            setContentPane(contentPane);
        }

        configurarTabla();
        cargarHistorial();

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void configurarTabla() {
        String[] columnas = {"Código", "Apartamento", "Canal", "Entrada", "Salida", "Estado", "Total", "Abonado", "Saldo"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bloquear edición directa
            }
        };
        if (tablaReservas != null) {
            tablaReservas.setModel(tableModel);
        }
    }

    private void cargarHistorial() {
        tableModel.setRowCount(0);
        if (huespedActual != null && huespedActual.getMisReservas() != null) {
            List<Reserva> reservas = huespedActual.getMisReservas();
            for (Reserva r : reservas) {
                double abonado = r.getTotalAbonado();
                long total = r.getValorTotal();
                double saldo = total - abonado;

                tableModel.addRow(new Object[]{
                        r.getCodigoReserva(),
                        r.getApartamento() != null ? r.getApartamento().getNombre() : "N/A",
                        r.getCanal(),
                        r.getFechaEntrada(),
                        r.getFechaSalida(),
                        r.getEstado(),
                        "$" + String.format("%,d", total),
                        "$" + String.format("%,.2f", abonado),
                        "$" + String.format("%,.2f", saldo <= 0 ? 0.0 : saldo)
                });
            }

            if (reservas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Actualmente no tienes reservas registradas en tu historial.",
                        "Sin Registros ℹ",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void initListeners() {
        if (buttonActualizar != null) {
            buttonActualizar.addActionListener(e -> cargarHistorial());
        }

        if (buttonCancel != null) {
            buttonCancel.addActionListener(e -> dispose());
        }

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}