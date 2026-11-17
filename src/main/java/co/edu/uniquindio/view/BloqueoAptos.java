package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BloqueoAptos extends JDialog {
    private JPanel mainPanel;
    private JComboBox<String> comboApartamentos;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextField txtMotivo;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Empresa empresa;

    public BloqueoAptos(JDialog parent, Empresa empresa) {
        super(parent, "Bloquear Apartamento por Fechas", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(550, 430);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (mainPanel != null) {
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        }

        cargarApartamentos();
        configurarBoton(btnGuardar, "Bloquear Fechas");
        configurarBoton(btnCancelar, "Cancelar");
        initListeners();
    }

    private void cargarApartamentos() {
        if (comboApartamentos == null || empresa == null || empresa.getListaApartamentos() == null) return;
        comboApartamentos.removeAllItems();

        for (Apartamento apto : empresa.getListaApartamentos()) {
            comboApartamentos.addItem(apto.getCodigo() + " - " + apto.getNombre());
        }
    }

    private void initListeners() {
        if (btnGuardar != null) {
            btnGuardar.addActionListener(e -> ejecutarBloqueo());
        }
        if (btnCancelar != null) {
            btnCancelar.addActionListener(e -> dispose());
        }
    }

    private void ejecutarBloqueo() {
        if (comboApartamentos.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No hay apartamentos registrados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String seleccion = comboApartamentos.getSelectedItem().toString();
        String codigoApto = seleccion.split(" - ")[0].trim();

        String strInicio = txtFechaInicio.getText().trim();
        String strFin = txtFechaFin.getText().trim();
        String motivo = txtMotivo.getText().trim();

        if (strInicio.isEmpty() || strFin.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa las fechas y el motivo del bloqueo.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate inicio = LocalDate.parse(strInicio);
            LocalDate fin = LocalDate.parse(strFin);

            if (fin.isBefore(inicio)) {
                JOptionPane.showMessageDialog(this, "La fecha final no puede ser anterior a la fecha inicial.", "Fechas inválidas", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Administrador admin = empresa.getAdministrador();
            Apartamento apto = admin.buscarApartamento(empresa.getListaApartamentos(), codigoApto);

            boolean bloqueado = admin.registrarBloqueo(apto, empresa.getListaReservas(), inicio, fin, motivo);

            if (bloqueado) {
                JOptionPane.showMessageDialog(this,
                        "Apartamento " + codigoApto + " bloqueado del " + inicio + " al " + fin + ".",
                        "Bloqueo Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo bloquear: el apartamento ya tiene una reserva activa o un bloqueo en esas fechas.",
                        "Conflicto de Agenda",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa AAAA-MM-DD (ej: 2026-11-20).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;
        boton.setText(titulo);

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