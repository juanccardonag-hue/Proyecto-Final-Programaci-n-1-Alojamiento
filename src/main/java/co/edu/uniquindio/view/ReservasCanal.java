package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Canal;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class ReservasCanal extends JDialog {
    private JPanel mainPanel;
    private JComboBox<Canal> cbCanal;
    private JLabel lblResultado;
    private JButton btnConsultar;
    private JButton btnCerrar;

    private Empresa empresa;

    public ReservasCanal(JDialog parent, Empresa empresa) {
        super(parent, "Reporte de Reservas por Canal", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(550, 420);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (mainPanel != null) {
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        }

        cargarCanales();
        configurarBoton(btnConsultar, "Consultar");
        configurarBoton(btnCerrar, "Cerrar");

        initListeners();
    }

    private void cargarCanales() {
        if (cbCanal != null) {
            cbCanal.removeAllItems();
            for (Canal c : Canal.values()) {
                cbCanal.addItem(c);
            }
        }
    }

    private void initListeners() {
        btnConsultar.addActionListener(e -> consultarReservas());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void consultarReservas() {
        Canal canalSeleccionado = (Canal) cbCanal.getSelectedItem();

        if (canalSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Selecciona un canal de reserva válido ⚠",
                    "Canal no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Administrador admin = empresa.getAdministrador();
        int totalReservas = admin.contarReservasPorCanal(empresa.getListaReservas(), canalSeleccionado);

        lblResultado.setText("<html><div style='text-align: center;'>" +
                "<b>Canal seleccionado:</b> " + canalSeleccionado + "<br><br>" +
                "<b>Total de Reservas:</b><br>" +
                "<font size='6' color='#2980B9'>" + totalReservas + "</font></div></html>");
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