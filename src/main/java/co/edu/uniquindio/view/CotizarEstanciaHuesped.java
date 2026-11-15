package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CotizarEstanciaHuesped extends JDialog {
    private JPanel contentPane;
    private JComboBox<Apartamento> comboBox1;
    private JTextField inputFechaInicio;
    private JTextField inputFechaFinal;
    private JSpinner inputPersonas;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;
    private Huesped huespedActual;

    public CotizarEstanciaHuesped(JDialog parent, Empresa empresa, Huesped huespedActual) {
        super(parent, "Cotizar Estancia - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.huespedActual = huespedActual;

        if (contentPane != null) {
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
            setContentPane(contentPane);
        }

        cargarComboApartamentos();

        if (inputPersonas != null) {
            inputPersonas.setModel(new SpinnerNumberModel(1, 1, 20, 1));
        }

        pack();
        setMinimumSize(new Dimension(480, 360));
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void cargarComboApartamentos() {
        if (comboBox1 != null && empresa != null && empresa.getListaApartamentos() != null) {
            comboBox1.removeAllItems();
            for (Apartamento apto : empresa.getListaApartamentos()) {
                comboBox1.addItem(apto);
            }

            comboBox1.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Apartamento) {
                        Apartamento a = (Apartamento) value;
                        setText(a.getCodigo() + " - " + a.getNombre() + " ($" + String.format("%,d", (long) a.getTarifaPersonaNoche()) + "/noche)");
                    }
                    return this;
                }
            });
        }
    }

    private void initListeners() {
        buttonOK.addActionListener(e -> calcularCotizacion());
        buttonCancel.addActionListener(e -> dispose());
    }

    private void calcularCotizacion() {
        Apartamento aptoSeleccionado = (Apartamento) comboBox1.getSelectedItem();
        String inicioStr = inputFechaInicio.getText().trim();
        String finStr = inputFechaFinal.getText().trim();

        if (aptoSeleccionado == null || inicioStr.isEmpty() || finStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", "Campos incompletos ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            LocalDate inicio = LocalDate.parse(inicioStr);
            LocalDate fin = LocalDate.parse(finStr);

            int personasTotal = (inputPersonas != null && inputPersonas.getValue() != null)
                    ? Integer.parseInt(inputPersonas.getValue().toString()) : 1;

            if (personasTotal <= 0 || personasTotal > aptoSeleccionado.getCapaMaxPersona()) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad de personas debe estar entre 1 y " + aptoSeleccionado.getCapaMaxPersona() + ".",
                        "Capacidad Inválida ⚠",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!inicio.isBefore(fin)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha final debe ser posterior a la fecha de inicio.",
                        "Fechas Inválidas ⚠",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (aptoSeleccionado.estaBloqueadoRango(inicio, fin)) {
                JOptionPane.showMessageDialog(this,
                        "El apartamento " + aptoSeleccionado.getNombre() + " se encuentra bloqueado o fuera de servicio en ese rango de fechas.",
                        "Apartamento No Disponible ⚠", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<LocalDate> fechasNacAcompanantes = new ArrayList<>();
            int numAcompanantes = personasTotal - 1;

            for (int i = 1; i <= numAcompanantes; i++) {
                LocalDate fNac = null;
                while (fNac == null) {
                    String fNacStr = JOptionPane.showInputDialog(this,
                            "Fecha de nacimiento del Acompañante #" + i + " (AAAA-MM-DD):\n(Menores de 6 años están exonerados)",
                            "Datos Acompañante " + i + "/" + numAcompanantes,
                            JOptionPane.QUESTION_MESSAGE);

                    if (fNacStr == null) return;
                    if (fNacStr.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Debes ingresar la fecha de nacimiento.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            fNac = LocalDate.parse(fNacStr.trim());
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Formato inválido. Usa AAAA-MM-DD.", "Error de Fecha ⚠", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                fechasNacAcompanantes.add(fNac);
            }

            String reporte = huespedActual.generarCotizacion(
                    aptoSeleccionado,
                    inicio,
                    fin,
                    fechasNacAcompanantes,
                    empresa != null ? empresa.getListaTemporadas() : null
            );

            JOptionPane.showMessageDialog(this, reporte, "Cotización de Reserva", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Datos inválidos. Verifica que las fechas tengan formato AAAA-MM-DD (ej: 2026-12-01).",
                    "Formato Incorrecto ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}