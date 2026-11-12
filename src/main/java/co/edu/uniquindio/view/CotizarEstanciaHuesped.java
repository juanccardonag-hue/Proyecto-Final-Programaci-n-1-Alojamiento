package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

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

            // Muestra código, nombre y tarifa en lugar de co.edu.uniquindio.Apartamento@...
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

        if (aptoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor selecciona un apartamento.",
                    "Selección requerida ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (inicioStr.isEmpty() || finStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresa las fechas de inicio y fin.",
                    "Campos vacíos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate inicio = LocalDate.parse(inicioStr);
            LocalDate fin = LocalDate.parse(finStr);

            int personasTotal = 1;
            if (inputPersonas != null && inputPersonas.getValue() != null) {
                personasTotal = Integer.parseInt(inputPersonas.getValue().toString());
            }

            if (personasTotal <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El número de personas debe ser mayor a 0.",
                        "Capacidad Inválida ⚠",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (personasTotal > aptoSeleccionado.getCapaMaxPersona()) {
                JOptionPane.showMessageDialog(this,
                        "El número de personas excede la capacidad máxima del apartamento (" + aptoSeleccionado.getCapaMaxPersona() + " pers.).",
                        "Exceso de Capacidad ⚠",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (fin.isBefore(inicio) || fin.isEqual(inicio)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha final debe ser posterior a la fecha de inicio.",
                        "Fechas Inválidas ⚠",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int pagantes = 0;
            int exonerados = 0;

            if (huespedActual != null && huespedActual.getFechaNacimiento() != null) {
                int edadTitular = Period.between(huespedActual.getFechaNacimiento(), inicio).getYears();
                if (edadTitular >= 6) {
                    pagantes++;
                } else {
                    exonerados++;
                }
            } else {
                pagantes++;
            }

            int numAcompanantes = personasTotal - 1;
            for (int i = 1; i <= numAcompanantes; i++) {
                String fNacStr = JOptionPane.showInputDialog(this,
                        "Fecha de nacimiento del Acompañante #" + i + " (YYYY-MM-DD):",
                        "Cotización - Datos Acompañante " + i + "/" + numAcompanantes,
                        JOptionPane.QUESTION_MESSAGE);

                if (fNacStr != null && !fNacStr.trim().isEmpty()) {
                    try {
                        LocalDate fNac = LocalDate.parse(fNacStr.trim());
                        int edad = Period.between(fNac, inicio).getYears();
                        if (edad >= 6) {
                            pagantes++;
                        } else {
                            exonerados++;
                        }
                    } catch (DateTimeParseException ex) {
                        pagantes++;
                    }
                } else {
                    pagantes++;
                }
            }
            long noches = ChronoUnit.DAYS.between(inicio, fin);
            long tarifaBase = (long) aptoSeleccionado.getTarifaPersonaNoche();
            long costoTotal = tarifaBase * pagantes * noches;

            String fechaNacTitular = (huespedActual != null && huespedActual.getFechaNacimiento() != null)
                    ? huespedActual.getFechaNacimiento().toString()
                    : "No registrada";

            JOptionPane.showMessageDialog(this,
                    "-°°° Resumen de Cotización  °°°\n\n" +
                            "• Titular: " + (huespedActual != null ? huespedActual.getNombre() : "Huésped") + " (F. Nac: " + fechaNacTitular + ")\n" +
                            "• Apartamento: " + aptoSeleccionado.getNombre() + "\n" +
                            "• Duración: " + noches + " noche(s)\n" +
                            "• Personas Totales: " + personasTotal + "\n" +
                            "   - Pagantes (≥6 años): " + pagantes + "\n" +
                            "   - Exonerados (<6 años): " + exonerados + "\n" +
                            "• Tarifa base persona/noche: $" + String.format("%,d", tarifaBase) + "\n\n" +
                            "TOTAL ESTIMADO: $" + String.format("%,d", costoTotal),
                    "Cotización de Reserva",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa las fechas en formato YYYY-MM-DD (ej: 2026-12-01).",
                    "Formato Incorrecto ⚠",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un número válido para las personas.",
                    "Valor Incorrecto ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}