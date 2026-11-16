package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;
import co.edu.uniquindio.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        configurarBoton(buttonCancel,"","Cancelar");
        configurarBoton(buttonActualizar, "","Actualizar");

    }

    private void configurarTabla() {
        String[] columnas = {"Código", "Apartamento", "Canal", "Entrada", "Salida", "Estado", "Total", "Abonado", "Saldo"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoomIcono, tamZoomIcono, Image.SCALE_SMOOTH)));
                }

                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                boton.revalidate();
                boton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
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