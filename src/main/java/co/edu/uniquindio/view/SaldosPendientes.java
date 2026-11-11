package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SaldosPendientes extends JDialog {
    private JPanel mainPanel;
    private JTable tablaSaldos;
    private JButton btnCerrar;

    private Empresa empresa;
    private DefaultTableModel model;

    public SaldosPendientes(JDialog parent, Empresa empresa) {
        super(parent, "Saldos Pendientes", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(650, 420);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (mainPanel != null) {
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        configurarTabla();
        configurarBoton(btnCerrar, "Cerrar");
        initListeners();
        cargarDatos();
    }

    private void configurarTabla() {
        String[] columnas = {"Código Reserva", "Cliente", "Apartamento", "Precio Total", "Saldo Folio"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (tablaSaldos != null) {
            tablaSaldos.setModel(model);
            tablaSaldos.setRowHeight(25);
            tablaSaldos.getTableHeader().setFont(new Font("Vensim Sans Mono", Font.BOLD, 13));
        }
    }

    private void initListeners() {
        if (btnCerrar != null) {
            btnCerrar.addActionListener(e -> dispose());
        }
    }

    private void cargarDatos() {
        if (model == null) return;
        model.setRowCount(0);

        Administrador admin = empresa.getAdministrador();
        List<Reserva> pendientes = admin.obtenerReservasSaldoPendiente(empresa.getListaReservas());

        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay reservas activas con saldo pendiente.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Reserva r : pendientes) {
                Object[] fila = new Object[5];

                fila[0] = r.getCodigoReserva();

                if (r.getTitular() != null) {
                    fila[1] = r.getTitular().getNombre();
                } else {
                    fila[1] = "N/A";
                }

                if (r.getApartamento() != null) {
                    fila[2] = r.getApartamento().getCodigo();
                } else {
                    fila[2] = "N/A";
                }

                if (r.getFolio() != null) {
                    fila[3] = "$" + r.getFolio().calcularTotalCargos();
                } else {
                    fila[3] = "N/A";
                }

                fila[4] = "$" + r.getSaldoFolio();

                model.addRow(fila);
            }
        }
    }

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;

        if (titulo != null && !titulo.isEmpty()) {
            boton.setText(titulo);
        }

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setFont(fuenteNormal);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteNormal);
            }
        });
    }
}